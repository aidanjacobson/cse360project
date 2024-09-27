package cse360project.utils;
import java.sql.*;
import java.util.ArrayList;

import cse360project.User;

public class DatabaseHelper {
    static final String JDBC_DRIVER = "org.h2.Driver";   
    static final String defaultDatabase = "~/cse360db";
    
    static String DB_URL = "jdbc:h2:" + defaultDatabase;

    /*
     * Set a different location for the database. Useful when running tests.
     */
    public static void setDatabasePath(String databasePath) {
        DB_URL = "jdbc:h2:" + databasePath;
    }

    static final String DB_USER = "sa";
    static final String DB_PASS = "";
    static private Connection connection = null;
    static private Statement statement = null;

    /*
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
            System.err.println("Could not connect to database");
        }
	}

    /*
     * Create the necessary tables if they dont exist already.
     */
    public static void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "username VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
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

    /*
     * Create a PreparedStatement on the connection from the given query.
     */
    public static PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    /*
     * Test if users exist in database
     */
    public static boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

    /*
     * Get a list of all users in database.
     */
    public static ArrayList<User> getAllUsers() {
        return getAllUsers("SELECT * FROM cse360users");
    }

    /*
     * Get one user that matches query.
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

    /*
     * Get one user that matches the PreparedStatement.
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

    /*
     * Get all users that match the query.
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

    /*
     * Get all users that match the prepared statement.
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

    /*
     * If user is not in database (i.e. user.id == -1) add to database.
     * Else update existing user in database.
     */
    public static void addOrUpdateUser(User user) {
        if (user.id == -1) { // we need to add the user
            addUser(user);
        } else { // update existing user
            updateUser(user);
        }
    }

    /*
     * Update existing user with database. If user with given id is not found, this will not affect the database.
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
            updateStatement.setString(2, user.password);
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
            insertStatement.setString(2, user.password);
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

    /*
     * Delete a user from the database.
     */
    public static boolean deleteUser(User user) {
        try {
            String deleteQuery = "DELETE FROM cse360users WHERE id=?";
            PreparedStatement pstmt = connection.prepareStatement(deleteQuery);
            pstmt.setInt(1, user.id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) return false;
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Warning! This function will delete ALL USERS in the connected database!
     * Use with caution!
     */
    public static boolean deleteAllUsers() {
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
