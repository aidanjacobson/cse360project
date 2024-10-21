package cse360project.utils;

import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import cse360project.Article;

public class BackupRestoreUtils {
    /**
     * Backup all article groups to the specified path
     * @param path The path to backup the database to
     * @return True if the backup was successful, false otherwise
     */
    public static boolean backupDatabase(String path) {
        // TODO: replace getAllArticleGroups() with the actual method to get all article groups
        return backupDatabase(path, getAllArticleGroups());
    }

    /**
     * Backup the specified article groups to the specified path
     * @param path The path to backup the database to
     * @param groups The groups to backup
     * @return True if the backup was successful, false otherwise
     */
    public static boolean backupDatabase(String path, ArrayList<String> groups) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        ArrayList<Article> articles = getArticlesByGroups(groups);
        System.out.println(articles);
        try {
            ObjectOutputStream write = new ObjectOutputStream(new FileOutputStream(path));
            write.writeObject(articles);
            write.close();
            return true;
        } catch (NotSerializableException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Restore the database from the specified path
     * @param path The path to restore the database from
     * @return The groups that were restored
     */
    public static boolean hardRestoreDatabase(String path) {
        try {
            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fin);
            ArrayList<Article> articles = (ArrayList<Article>) ois.readObject();
            for (Article article : articles) {
                // print out the articles to show that they were restored
                System.out.println("Restored article:");
                System.out.println("ID: " + article.ID);
                System.out.println("Level: " + article.level);
                System.out.println("Groups: " + article.groups);
                System.out.println("Title: " + article.title);
                System.out.println("Description: " + article.description);
                System.out.println("Keywords: " + article.keywords);
                System.out.println("Body: " + article.body);
                System.out.println("Links: " + article.links);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean softRestoreDatabase(String path, boolean mergeOverwrite) {
        // TODO: implement this method
        return hardRestoreDatabase(path);
    }

    public static boolean softRestoreDatabase(String path) {
        return softRestoreDatabase(path, true);
    }

    // temporary method to get all article groups
    // TODO: delete this method when the actual method is implemented
    static ArrayList<Article> getArticlesByGroups(ArrayList<String> groups) {
        ArrayList<Article> articles = new ArrayList<>();
        for (Article article : testArticles) {
            for (String group : groups) {
                if (article.groups.contains(group) && !articles.contains(article)) {
                    articles.add(article);
                    break;
                }
            }
        }
        return articles;
    }

    // temporary method to get all article groups
    // TODO: delete this method when the actual method is implemented
    public static ArrayList<String> getAllArticleGroups() {
        ArrayList<String> groups = new ArrayList<String>();
        for (Article article : testArticles) {
            for (String group : article.groups) {
                if (!groups.contains(group)) {
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    static ArrayList<Article> testArticles = new ArrayList<Article>();
    public static void initializeTestArticles() {
        Article article = new Article(1, Level.LOW, new ArrayList<String>(Arrays.asList("Group 1", "Group 2")), "Title", "Description", "Keywords", "Body", new ArrayList<String>());
        Article article2 = new Article(2, Level.MEDIUM, new ArrayList<String>(Arrays.asList("The third group")), "Title2", "Description2", "Keywords2", "Body2", new ArrayList<String>());
        Article article3 = new Article(3, Level.HIGH, new ArrayList<String>(Arrays.asList("Group 2", "a fourth group")), "Title3", "Description3", "Keywords3", "Body3", new ArrayList<String>());
        testArticles = new ArrayList<Article>();
        testArticles.add(article);
        testArticles.add(article2);
        testArticles.add(article3);
    }
}
