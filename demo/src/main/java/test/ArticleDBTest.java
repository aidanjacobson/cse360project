package test;

import java.sql.SQLException;
import java.util.ArrayList;

import cse360project.utils.DatabaseHelper;
import cse360project.utils.Level;
import cse360project.Article;

public class ArticleDBTest {
    public static void main(String[] args) {
        initialTest();
    }
    public static void initialTest() {
        /*
         * This is a general test of the database.
         * It will destroy the testdb database, then will test the commands.
         * First it verifies the db is empty.
         * Then it adds an an article.
         * Then it attempts to find the article.
         * Then it changes the article title.
         * Then it deletes the article, and verifies the db is once again empty.
         */
        try {
            DatabaseHelper.setDatabasePath("~/testdb");
            DatabaseHelper.connectToDatabase();
            DatabaseHelper.deleteAllArticles();
            
            DatabaseHelper.createArticleTables();


            boolean empty = DatabaseHelper.isArticleDatabaseEmpty();
            System.out.printf("Database empty: %s%n", empty ? "true" : "false");

            if (empty) {
            	ArrayList<String> groups = new ArrayList<>();
            	groups.add("Group");
            	groups.add("Group2");
            	ArrayList<String> links = new ArrayList<>();
            	links.add("Link");
                Article newArticle = new Article(-1, Level.BEGINNER, groups, "Title", "Description", "Keywords", "Body", links);
                DatabaseHelper.addArticle(newArticle);

                System.out.printf("New User ID: %d%n", newArticle.ID);

                Article found = DatabaseHelper.getOneArticle("SELECT * FROM cse360articles WHERE title='Title'");
                System.out.printf("Name info: %s %s (%s)%n", found.description, found.keywords, found.body);

                found.title = "new preferred title";
                DatabaseHelper.updateArticle(found);

                found = DatabaseHelper.getOneArticle("SELECT * FROM cse360articles WHERE title='new preferred title'");
                System.out.printf("Name info after changing preferred name: %s %s %s (%s)%n", found.description, found.keywords, found.body, found.groups);

                System.out.println("Attempting delete...");
                boolean successful = DatabaseHelper.deleteArticle(found);
                System.out.printf("Succeeded: %s%n", successful ? "true" : "false");

                empty = DatabaseHelper.isArticleDatabaseEmpty();
                System.out.printf("Database empty: %s%n", empty ? "true" : "false");
            }


        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
