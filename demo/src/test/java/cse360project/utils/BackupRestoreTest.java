package test;

import java.util.ArrayList;

import cse360project.Article;
import cse360project.utils.BackupRestoreUtils;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.Level;

public class BackupRestoreTest {
    public static void main(String[] args) {
        System.out.println("BackupRestoreTest: Running tests...");

        boolean allTestsPassed = true;
        boolean allBackupAndRestore = testAllBackupAndRestore();
        if (!allBackupAndRestore) {
            allTestsPassed = false;
        }
        System.out.println("BackupRestoreTest: testAllBackupAndRestore: " + allBackupAndRestore);

        boolean groupBackupAndHardRestore = testGroupBackupAndHardRestore();
        if (!groupBackupAndHardRestore) {
            allTestsPassed = false;
        }
        System.out.println("BackupRestoreTest: testGroupBackupAndHardRestore: " + groupBackupAndHardRestore);
        
        boolean groupBackupAndSoftRestoreWithOverwrite = testGroupBackupAndSoftRestoreWithOverwrite();
        if (!groupBackupAndSoftRestoreWithOverwrite) {
            allTestsPassed = false;
        }
        System.out.println("BackupRestoreTest: testGroupBackupAndSoftRestoreWithOverwrite: " + groupBackupAndSoftRestoreWithOverwrite);

        boolean groupBackupAndSoftRestoreWithoutOverwrite = testGroupBackupAndSoftRestoreWithoutOverwrite();
        if (!groupBackupAndSoftRestoreWithoutOverwrite) {
            allTestsPassed = false;
        }
        System.out.println("BackupRestoreTest: testGroupBackupAndSoftRestoreWithoutOverwrite: " + groupBackupAndSoftRestoreWithoutOverwrite);

        if (allTestsPassed) {
            System.out.println("BackupRestoreTest: All tests passed!");
        } else {
            System.out.println("BackupRestoreTest: Some tests failed.");
        }
    }

    static boolean testAllBackupAndRestore() {
        // Test the BackupRestoreUtils class
        resetTestDatabaseEnvironment();

        String backupPath = System.getProperty("user.home") + "/test_backup.backup";
        BackupRestoreUtils.backupDatabase(backupPath);

        DatabaseHelper.deleteAllArticles();
        BackupRestoreUtils.hardRestoreDatabase(backupPath);

        ArrayList<Article> articles = DatabaseHelper.getAllArticles();

        // check the number of articles
        if (articles.size() != 3) {
            System.out.println("BackupRestoreTest: testAllBackupAndRestore: Failed to restore all articles");
            return false;
        }

        // check the IDs
        if (articles.get(0).getID() != 1 || articles.get(1).getID() != 2 || articles.get(2).getID() != 3) {
            System.out.println("BackupRestoreTest: testAllBackupAndRestore: Failed to restore article IDs");
            return false;
        }
        // check the titles
        if (!articles.get(0).getTitle().equals("Title 1") || !articles.get(1).getTitle().equals("Title 2") || !articles.get(2).getTitle().equals("Title 3")) {
            System.out.println("BackupRestoreTest: testAllBackupAndRestore: Failed to restore article titles");
            return false;
        }

        // assume if those match, the rest of the data is correct
        return true;
    }

    static boolean testGroupBackupAndHardRestore() {
        // Test the BackupRestoreUtils class
        resetTestDatabaseEnvironment();

        ArrayList<String> backupGroups = new ArrayList<String>();
        backupGroups.add("Group 1");

        String backupPath = System.getProperty("user.home") + "/test_backup.backup";
        BackupRestoreUtils.backupDatabase(backupPath, backupGroups);

        DatabaseHelper.deleteAllArticles();
        BackupRestoreUtils.hardRestoreDatabase(backupPath);

        ArrayList<Article> articles = DatabaseHelper.getAllArticles();

        // check the number of articles
        if (articles.size() != 2) {
            System.out.println("BackupRestoreTest: testGroupBackupAndHardRestore: Failed to restore all articles");
            return false;
        }

        // check the IDs
        if (articles.get(0).getID() != 1 || articles.get(1).getID() != 3) {
            System.out.println("BackupRestoreTest: testGroupBackupAndHardRestore: Failed to restore article IDs");
            return false;
        }

        // check the titles
        if (!articles.get(0).getTitle().equals("Title 1") || !articles.get(1).getTitle().equals("Title 3")) {
            System.out.println("BackupRestoreTest: testGroupBackupAndHardRestore: Failed to restore article titles");
            return false;
        }

        // assume if those match, the rest of the data is correct
        return true;
    }

    static boolean testGroupBackupAndSoftRestoreWithOverwrite() {
        // Test the BackupRestoreUtils class
        resetTestDatabaseEnvironment();

        ArrayList<String> backupGroups = new ArrayList<String>();
        backupGroups.add("Group 1");

        String backupPath = System.getProperty("user.home") + "/test_backup.backup";
        BackupRestoreUtils.backupDatabase(backupPath, backupGroups);

        // change the title of article 1
        Article article1 = DatabaseHelper.getOneArticle("SELECT * FROM cse360articles WHERE article_id=1");
        article1.setTitle("New Title 1");
        DatabaseHelper.updateArticle(article1);

        // restore the backup
        BackupRestoreUtils.softRestoreDatabase(backupPath, true);

        // check the title of article 1
        Article restoredArticle1 = DatabaseHelper.getOneArticle("SELECT * FROM cse360articles WHERE article_id=1");
        if (!restoredArticle1.getTitle().equals("Title 1")) {
            System.out.println("BackupRestoreTest: testGroupBackupAndSoftRestoreWithOverwrite: Failed to restore article title");
            return false;
        }

        // check the number of articles
        if (DatabaseHelper.getAllArticles().size() != 3) {
            System.out.println("BackupRestoreTest: testGroupBackupAndSoftRestoreWithOverwrite: Failed to restore all articles");
            return false;
        }

        // if correct so far, assume the rest of the data is correct
        return true;
    }

    static boolean testGroupBackupAndSoftRestoreWithoutOverwrite() {
        resetTestDatabaseEnvironment();

        ArrayList<String> backupGroups = new ArrayList<String>();
        backupGroups.add("Group 1");

        String backupPath = System.getProperty("user.home") + "/test_backup.backup";
        BackupRestoreUtils.backupDatabase(backupPath, backupGroups);

        // change the title of article 1
        Article article1 = DatabaseHelper.getOneArticle("SELECT * FROM cse360articles WHERE article_id=1");
        article1.setTitle("New Title 1");
        DatabaseHelper.updateArticle(article1);

        // restore the backup
        BackupRestoreUtils.softRestoreDatabase(backupPath, false);

        // check the title of article 1
        Article restoredArticle1 = DatabaseHelper.getOneArticle("SELECT * FROM cse360articles WHERE article_id=1");
        if (!restoredArticle1.getTitle().equals("New Title 1")) {
            System.out.println("BackupRestoreTest: testGroupBackupAndSoftRestoreWithOverwrite: Failed to restore article title");
            return false;
        }

        // check the number of articles
        if (DatabaseHelper.getAllArticles().size() != 3) {
            System.out.println("BackupRestoreTest: testGroupBackupAndSoftRestoreWithOverwrite: Failed to restore all articles");
            return false;
        }

        // if correct so far, assume the rest of the data is correct
        return true;
    }

    static void resetTestDatabaseEnvironment() {
        // Reset the database environment to its original state
        DatabaseHelper.setDatabasePath("~/test.db");
        DatabaseHelper.connectToDatabase();
        DatabaseHelper.deleteAllArticles();

        // Add some articles
        ArrayList<String> groups1 = new ArrayList<String>();
        groups1.add("Group 1");

        ArrayList<String> links1 = new ArrayList<String>();
        links1.add("Link 1");

        Article article1 = new Article(1, Level.BEGINNER, groups1, "Title 1", "Description 1", "Keywords 1", "Body 1", links1);
        DatabaseHelper.addArticle(article1);

        ArrayList<String> groups2 = new ArrayList<String>();
        groups2.add("Group 2");

        ArrayList<String> links2 = new ArrayList<String>();
        links2.add("Link 2");

        Article article2 = new Article(2, Level.INTERMEDIATE, groups2, "Title 2", "Description 2", "Keywords 2", "Body 2", links2);
        DatabaseHelper.addArticle(article2);

        ArrayList<String> groups3 = new ArrayList<String>();
        groups3.add("Group 1");
        groups3.add("Group 2");

        ArrayList<String> links3 = new ArrayList<String>();
        links3.add("Link 3");

        Article article3 = new Article(3, Level.ADVANCED, groups3, "Title 3", "Description 3", "Keywords 3", "Body 3", links3);
        DatabaseHelper.addArticle(article3);
    }
}
