package cse360project.utils;
import java.sql.*;
import java.util.ArrayList;

import cse360project.Article;
import cse360project.Message;
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
            createArticleTables();
            createMessagesTable(); // Create the necessary table for messages
            System.out.println("Connection successful");
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		} catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Could not connect to database");
        }
	}

    /**
     * Create the necessary tables if they don't exist already.
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
                + "is_instructor BOOLEAN, "
                + "groups VARCHAR(500)"
                +")";
		statement.execute(userTable);
	}

    /**
     * Create the necessary tables if they don't exist already.
     * @throws SQLException
     */
    public static void createArticleTables() throws SQLException {
		String ArticleTable = "CREATE TABLE IF NOT EXISTS cse360articles ("
				+ "article_id BIGINT AUTO_INCREMENT PRIMARY KEY, "
				+ "level VARCHAR(15) NOT NULL, "
				+ "groups VARCHAR(500), "
				+ "title VARCHAR(50) NOT NULL, "
				+ "description VARCHAR(500) NOT NULL, "
                + "keywords VARCHAR(300) NOT NULL, "
                + "body VARCHAR(10000) NOT NULL, "
                + "links VARCHAR(1000) NOT NULL"
                +")";
		statement.execute(ArticleTable);
	}
    
    /**
     * Create the necessary tables for messages if they don't exist already.
     * @throws SQLException
     */
    public static void createMessagesTable() throws SQLException {
        String messagesTable = "CREATE TABLE IF NOT EXISTS cse360messages (" 
                               + "message_id INT AUTO_INCREMENT PRIMARY KEY, "
                               + "messageType VARCHAR(15) NOT NULL, "
                               + "messageContent VARCHAR(10000) NOT NULL, "
                               + "senderUsername VARCHAR(255) NOT NULL, "
                               + "senderRole VARCHAR(15) NOT NULL, "
                               + "threadUsername VARCHAR(255), "
                               + "messageTimestamp TIMESTAMP NOT NULL"
                               +")";
        statement.execute(messagesTable);
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
            String updateQuery = "UPDATE cse360users SET username=?, password=?, email=?, inviteCode=?, accountSetUp=?, OTP=?, OTP_expiration=?, firstName=?, middleName=?, lastName=?, preferredName=?, is_admin=?, is_instructor=?, is_student=?, groups=? WHERE id=?";
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
            
            String groups = String.join("\n", user.groups);
            updateStatement.setString(15, groups);
            
            updateStatement.setInt(16, user.id);

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
            String insertQuery = "INSERT INTO cse360users (username, password, email, inviteCode, accountSetUp, OTP, OTP_expiration, firstName, middleName, lastName, preferredName, is_admin, is_instructor, is_student, groups) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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

            String groups = String.join("\n", user.groups);
            insertStatement.setString(15, groups);

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
            statement.executeUpdate(sql);
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Add an article to the database. If the article already exists in the database, this command will fail.
     * @param article The article to add
     */
    public static void addArticle(Article article) {
        // if the article id is not -1, we should not add to database
        try {
            // we want to obtain the new id of the added article
            String[] returnId = { "article_id" };
            ArrayList<String> unprocessedGroups = article.groups;
            ArrayList<String> groups = new ArrayList<>();
            for (String group : unprocessedGroups) {
                groups.add(GroupUtils.formatGroupName(group));
            }
            String group = String.join("\n", groups);
            String link = String.join("\n", article.links);
            String lev = Level.levelToString(article.level);
            PreparedStatement insertStatement;

            if (article.ID == -1) {
                // craft INSERT query
                String insertQuery = "INSERT INTO cse360articles (level, groups, title, description, keywords, body, links) VALUES (?, ?, ?, ?, ?, ?, ?)";
                insertStatement = connection.prepareStatement(insertQuery, returnId);
                insertStatement.setString(1, lev);
                insertStatement.setString(2, group);
                insertStatement.setString(3, article.title);
                insertStatement.setString(4, article.description);
                insertStatement.setString(5, article.keywords);
                insertStatement.setString(6, article.body);
                insertStatement.setString(7, link);

                // execute the query
                insertStatement.executeUpdate();
            } else {
                // craft INSERT query
                String insertQuery = "INSERT INTO cse360articles (article_id, level, groups, title, description, keywords, body, links) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                insertStatement = connection.prepareStatement(insertQuery, returnId);
                insertStatement.setLong(1, article.ID);
                insertStatement.setString(2, lev);
                insertStatement.setString(3, group);
                insertStatement.setString(4, article.title);
                insertStatement.setString(5, article.description);
                insertStatement.setString(6, article.keywords);
                insertStatement.setString(7, article.body);
                insertStatement.setString(8, link);

                // execute the query
                insertStatement.executeUpdate();
            }

            // capture id of new article, and update the Article object id to match
            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    article.ID = generatedKeys.getInt(1);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get a list of all articles in database.
     * @return an ArrayList<Article> with all articles in the database.
     */
    public static ArrayList<Article> getAllArticles() {
        return getAllArticles("SELECT * FROM cse360articles");
    }
    
    /**
     * Get one article that matches query.
     * @param query the SQL query to select an article
     * @return the first article that matches the selection, or null if none match
     */
    public static Article getOneArticle(String query) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            return getOneArticle(pstmt);
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get one article that matches the PreparedStatement.
     * @param pstmt a PreparedStatement created with DatabaseHelper.prepareStatement(query)
     * @return the first article that matches the selection, or null if none match
     */
    public static Article getOneArticle(PreparedStatement pstmt) {
        try {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Article.fromResultSet(rs);
            } else {
                return null;
            }
        } catch(SQLException e) {
            System.out.println("Error in getOneArticle(()");
            return null;
        }
    }
    
    /**
     * Test if article with id exists in the database
     * @param id the id of the article
     * @return true/false whether the article exists
     */
    public static boolean articleWithIdExists(long id) {
        String existingUserQuery = "SELECT * FROM cse360articles WHERE article_id=?";
        try {
        PreparedStatement pstmt = connection.prepareStatement(existingUserQuery);
        pstmt.setLong(1, id);
        ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

  /**
   * Get all articles that match the query.
   * @param query the query to select the articles
   * @return an ArrayList<Article> of all matching articles
   */
    public static ArrayList<Article> getAllArticles(String query) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            return getAllArticles(pstmt);
        } catch(SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

  /**
   * Get all articles that match the prepared statement.
   * @param pstmt a PreparedStatement created with DatabaseHelper.prepareStatement(query)
   * @return an ArrayList<Article> of all matching articles
   */
    public static ArrayList<Article> getAllArticles(PreparedStatement pstmt) {
        ArrayList<Article> article = new ArrayList<>();
        try {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Article newArticle = Article.fromResultSet(rs);
                article.add(newArticle);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

  /**
   * If article is not in database (i.e. article.ID == -1) add to database.
   * Else update existing article in database.
   * @param article the article to add/update
   */
    public static void addOrUpdateArticle(Article article) {
        if (article.ID == -1) { // we need to add the user
            addArticle(article);
        } else { // update existing user
            updateArticle(article);
        }
    }
  
  /**
   * Update existing article with database. If article with given article.ID is not found, this will not affect the database.
   * @param article the article to update
   */
    public static void updateArticle(Article article) {
        // first, check to see if article with id exists
        String existingUserQuery = "SELECT * FROM cse360articles WHERE article_id=?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(existingUserQuery);
            pstmt.setLong(1, article.ID);
            ResultSet rs = pstmt.executeQuery();
            if (! rs.next()) { // requesting to update existing article with id, but id does not exist
                System.err.printf("Error: Attempted to update article with id %d, but id was not found%n", article.ID);
                return;
            }
            String group = String.join("\n", article.groups);
            String link = String.join("\n", article.links);
            String lev = Level.levelToString(article.level);
            
            // the user exists, craft the UPDATE query for the article
            String updateQuery = "UPDATE cse360articles SET level=?, groups=?, title=?, description=?, keywords=?, body=?, links=? WHERE article_id=?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, lev);
            updateStatement.setString(2, group);
            updateStatement.setString(3, article.title);
            updateStatement.setString(4, article.description);
            updateStatement.setString(5, article.keywords);
            updateStatement.setString(6, article.body);
            updateStatement.setString(7, link);
            updateStatement.setLong(8, article.ID);

            // execute the query
            updateStatement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
  
  /**
   * Delete an Article from the database.
   * @param article The article object to delete
   * @return true/false whether the delete succeeded
   */
    public static boolean deleteArticle(Article article) {
        try {
            String deleteQuery = "DELETE FROM cse360articles WHERE article_id=?";
            PreparedStatement pstmt = connection.prepareStatement(deleteQuery);
            pstmt.setLong(1, article.ID);
            int affectedRows = pstmt.executeUpdate();
            article.ID = -1;
            if (affectedRows == 0) return false;
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
  
  /**
   * Warning! This function will delete ALL ARTICLES in the connected database!
   * Use with caution!
   * @return true/false whether the delete succeeded
   */
    public static boolean deleteAllArticles() {
        if (isArticleDatabaseEmpty()) return true;
        String sql = "DELETE FROM cse360articles";
        try {
            int rows = statement.executeUpdate(sql);
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
  
  /**
   * Test if Articles exist in database
   * @return true/false
   * @throws SQLException
   */
    public static boolean isArticleDatabaseEmpty() {
        try {
            String query = "SELECT COUNT(*) AS count FROM cse360articles";
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
    
    /**
     * Get a list of all messages in the database.
     * @return an ArrayList<Message> with all messages in the database.
     */
    public static ArrayList<Message> getAllMessages() {
        return getAllMessages("SELECT * FROM cse360messages");
    }
    
    /**
     * Get all messages that match the query.
     * @param query the SQL query to select the messages
     * @return an ArrayList<Message> of all matching messages
     */
    public static ArrayList<Message> getAllMessages(String query) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            return getAllMessages(pstmt);
        } catch(SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Get all messages that match the prepared statement.
     * @param pstmt a PreparedStatement created with DatabaseHelper.prepareStatement(query)
     * @return an ArrayList<Message> of all matching messages
     */
    public static ArrayList<Message> getAllMessages(PreparedStatement pstmt) {
        ArrayList<Message> messages = new ArrayList<>();
        try {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Message message = Message.fromResultSet(rs);
                messages.add(message);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }



}
