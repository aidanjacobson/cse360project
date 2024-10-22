package cse360project.utils;

import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

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
        
        ArrayList<Article> articles = getArticlesByGroups(groups);

        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

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
            
            // delete all articles in the database, then add the new articles
            DatabaseHelper.deleteAllArticles();
            for (Article article : articles) {
                DatabaseHelper.addArticle(article);
            }
            ois.close();
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
        try {
            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fin);
            ArrayList<Article> articlesToMerge = (ArrayList<Article>) ois.readObject();
            
            for (Article article : articlesToMerge) {
                if (DatabaseHelper.articleWithIdExists(article.ID)) {
                    if (mergeOverwrite) {
                        DatabaseHelper.updateArticle(article);
                    } else {
                        continue;
                    }
                } else {
                    DatabaseHelper.addArticle(article);
                }
            }
            ois.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean softRestoreDatabase(String path) {
        return softRestoreDatabase(path, true);
    }

    // temporary method to get all article groups
    // TODO: delete this method when the actual method is implemented
    static ArrayList<Article> getArticlesByGroups(ArrayList<String> groups) {
        ArrayList<Article> articles = new ArrayList<>();
        ArrayList<Article> testArticles = DatabaseHelper.getAllArticles();
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
        ArrayList<Article> testArticles = DatabaseHelper.getAllArticles();
        for (Article article : testArticles) {
            for (String group : article.groups) {
                if (!groups.contains(group)) {
                    groups.add(group);
                }
            }
        }
        return groups;
    }
}
