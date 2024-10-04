package cse360project.utils;
import java.sql.*;
import java.util.ArrayList;

import cse360project.User;

public class DatabaseHelper {
    static final String JDBC_DRIVER = "org.h2.Driver";   
    static final String defaultDatabase = "~/cse360db";
    
    static String DB_URL = "jdbc:h2:" + defaultDatabase;

    /**
     * Set a different location for the database. Useful when running tests.
     * @param databasePath path to database e.g. "~/testdb" to store in user home directory in database called testdb
     */
    public static void setDatabasePath(String databasePath) {
        DB_URL = "jdbc:h2:" + databasePath;
    }

    static final String DB_USER = "sa";
    static final String DB_PASS = "";
    static private Connection connection = null;
    static private Statement statement = null;

    /**
     * Connect to the database. Defaults to ~/cse360db unless setDataBasePath() has been called.
     */
    public static void connectToDatabase() {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			statement = connection.createStatement(); 
			createTables();  // Create the necessary tables if they don't exist
            System.out.println("Connection successful");
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		} catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Could not connect to database");
        }
	}

    /**
     * Create the necessary tables if they dont exist already.
     * @throws SQLException
     */
    public static void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "username VARCHAR(255) UNIQUE, "
				+ "password BLOB, "
				+ "email VARCHAR(255) UNIQUE, "
                + "inviteCode VARCHAR(255), "
                + "accountSetUp BOOLEAN, "
                + "OTP BOOLEAN, "
                + "OTP_expiration TIMESTAMP, "
                + "firstName VARCHAR(255), "
                + "middleName VARCHAR(255), "
                + "lastName VARCHAR(255), "
                + "preferredName VARCHAR(255), "
                + "is_admin BOOLEAN, "
                + "is_student BOOLEAN, "
                + "is_instructor BOOLEAN"
                +")";
		statement.execute(userTable);
	}

    /**
     * Create a PreparedStatement on the connection from the given query.
     * @param query the SQL query to select the user(s)
     * @return a PreparedStatement that can be used in getOneUser(), getAllUsers()
     * @throws SQLException
     */
    public static PreparedStatement prepareStatement(String query) {
        try {
            return connection.prepareStatement(query);
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Test if users exist in database
     * @return true/false
     * @throws SQLException
     */
    public static boolean isDatabaseEmpty() {
        try {
            String query = "SELECT COUNT(*) AS count FROM cse360users";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt("count") == 0;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Could not access the database");
            return true;
        }
	}

    public static User getUserByID(int userID) {
        try {
            PreparedStatement pstmt = prepareStatement("SELECT * FROM cse360users WHERE id = ?");
            pstmt.setInt(1, userID);
            return getOneUser(pstmt);
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a list of all users in database.
     * @return an ArrayList<User> with all users in the database.
     */
    public static ArrayList<User> getAllUsers() {
        return getAllUsers("SELECT * FROM cse360users");
    }

    /**
     * Get one user that matches query.
     * @param query the SQL query to select a user
     * @return the first user that matches the selection, or null if none match
     */
    public static User getOneUser(String query) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            return getOneUser(pstmt);
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get one user that matches the PreparedStatement.
     * @param pstmt a PreparedStatement created with DatabaseHelper.prepareStatement(query)
     * @return the first user that matches the selection, or null if none match
     */
    public static User getOneUser(PreparedStatement pstmt) {
        try {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return User.fromResultSet(rs);
            } else {
                return null;
            }
        } catch(SQLException e) {
            System.out.println("Error in getOneUser()");
            return null;
        }
    }

    /**
     * Get all users that match the query.
     * @param query the query to select the users
     * @return an ArrayList<User> of all matching users
     */
    public static ArrayList<User> getAllUsers(String query) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            return getAllUsers(pstmt);
        } catch(SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Get all users that match the prepared statement.
     * @param pstmt a PreparedStatement created with DatabaseHelper.prepareStatement(query)
     * @return an ArrayList<User> of all matching users
     */
    public static ArrayList<User> getAllUsers(PreparedStatement pstmt) {
        ArrayList<User> users = new ArrayList<>();
        try {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User newUser = User.fromResultSet(rs);
                users.add(newUser);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    /**
      * If user is not in database (i.e. user.id == -1) add to database.
      * Else update existing user in database.
      * @param user the user to add/update
      */
    public static void addOrUpdateUser(User user) {
        if (user.id == -1) { // we need to add the user
            addUser(user);
        } else { // update existing user
            updateUser(user);
        }
    }

    /**
     * Update existing user with database. If user with given id is not found, this will not affect the database.
     * @param user the user to update
     */
    public static void updateUser(User user) {
        // first, check to see if user with id exists
        String existingUserQuery = "SELECT * FROM cse360users WHERE id=?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(existingUserQuery);
            pstmt.setInt(1, user.id);
            ResultSet rs = pstmt.executeQuery();
            if (! rs.next()) { // requesting to update existing user with id, but id does not exist
                System.err.printf("Error: Attempted to update user with id %d, but id was not found%n", user.id);
                return;
            }

            // the user exists, craft the UPDATE query for the user
            String updateQuery = "UPDATE cse360users SET username=?, password=?, email=?, inviteCode=?, accountSetUp=?, OTP=?, OTP_expiration=?, firstName=?, middleName=?, lastName=?, preferredName=?, is_admin=?, is_instructor=?, is_student=? WHERE id=?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, user.username);
            
            // convert password char array to byte array for db storage
            byte[] passwordBytes = new byte[user.password.length];
            for (int i = 0; i < user.password.length; i++) {
                passwordBytes[i] = (byte) user.password[i];
            }
            updateStatement.setBytes(2, passwordBytes);

            updateStatement.setString(3, user.email);
            updateStatement.setString(4, user.inviteCode);
            updateStatement.setBoolean(5, user.accountSetUp);
            updateStatement.setBoolean(6, user.OTP);
            updateStatement.setTimestamp(7, user.OTP_expiration);
            updateStatement.setString(8, user.firstName);
            updateStatement.setString(9, user.middleName);
            updateStatement.setString(10, user.lastName);
            updateStatement.setString(11, user.preferredName);
            updateStatement.setBoolean(12, user.is_admin);
            updateStatement.setBoolean(13, user.is_instructor);
            updateStatement.setBoolean(14, user.is_student);
            updateStatement.setInt(15, user.id);

            // execute the query
            updateStatement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a user to the database. If the user already exists in the database, this command will fail.
     * @param user The user to update
     */
    public static void addUser(User user) {
        // if the user id is not -1, we should not add to database
        if (user.id != -1) {
            System.err.printf("Error: Attempted to add user to db, but user.id was not -1 (got %d)%n", user.id);
            return;
        }
        try {
            // we want to obtain the new id of the added user
            String[] returnId = { "id" };

            // craft INSERT query
            String insertQuery = "INSERT INTO cse360users (username, password, email, inviteCode, accountSetUp, OTP, OTP_expiration, firstName, middleName, lastName, preferredName, is_admin, is_instructor, is_student) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery, returnId);
            insertStatement.setString(1, user.username);

            // convert password char array to byte array for db storage
            byte[] passwordBytes = new byte[user.password.length];
            for (int i = 0; i < user.password.length; i++) {
                passwordBytes[i] = (byte) user.password[i];
            }
            insertStatement.setBytes(2, passwordBytes);

            insertStatement.setString(3, user.email);
            insertStatement.setString(4, user.inviteCode);
            insertStatement.setBoolean(5, user.accountSetUp);
            insertStatement.setBoolean(6, user.OTP);
            insertStatement.setTimestamp(7, user.OTP_expiration);
            insertStatement.setString(8, user.firstName);
            insertStatement.setString(9, user.middleName);
            insertStatement.setString(10, user.lastName);
            insertStatement.setString(11, user.preferredName);
            insertStatement.setBoolean(12, user.is_admin);
            insertStatement.setBoolean(13, user.is_instructor);
            insertStatement.setBoolean(14, user.is_student);

            // execute the query
            insertStatement.executeUpdate();

            // capture id of new user, and update the User object id to match
            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.id = generatedKeys.getInt(1);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a user from the database.
     * @param user The user object to delete
     * @return true/false whether the delete succeeded
     */
    public static boolean deleteUser(User user) {
        try {
            String deleteQuery = "DELETE FROM cse360users WHERE id=?";
            PreparedStatement pstmt = connection.prepareStatement(deleteQuery);
            pstmt.setInt(1, user.id);
            int affectedRows = pstmt.executeUpdate();
            user.id = -1;
            if (affectedRows == 0) return false;
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
      * Warning! This function will delete ALL USERS in the connected database!
      * Use with caution!
      * @return true/false whether the delete succeeded
      */
    public static boolean deleteAllUsers() {
        if (isDatabaseEmpty()) return true;
        String sql = "DELETE FROM cse360users";
        try {
            int rows = statement.executeUpdate(sql);
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
