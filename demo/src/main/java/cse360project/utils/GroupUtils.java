package cse360project.utils;

import java.util.ArrayList;
import java.util.HashSet;
import cse360project.Article;

public class GroupUtils {

    /**
     * Get all articles that belong to a specified group.
     * @param articles The list of articles to search.
     * @param group The group to filter by.
     * @return A list of articles that belong to the specified group.
     */
    public ArrayList<Article> getAllArticlesWithGroup(ArrayList<Article> articles, String group) {
        ArrayList<Article> articlesInGroup = new ArrayList<>();
        for (Article article : articles) {
            if (article.hasGroup(group)) {
                articlesInGroup.add(article);
            }
        }
        return articlesInGroup;
    }

    /**
     * Get all articles that belong to any of the specified groups.
     * @param articles The list of articles to search.
     * @param groups The list of groups to search for.
     * @return A list of articles that are in any of the specified groups.
     */
    public ArrayList<Article> getAllArticlesWithGroups(ArrayList<Article> articles, ArrayList<String> groups) {
        ArrayList<Article> articlesInGroups = new ArrayList<>();
        for (Article article : articles) {
            for (String group : groups) {
                if (article.hasGroup(group)) {
                    articlesInGroups.add(article);
                    break; // Stop checking after the first matching group
                }
            }
        }
        return articlesInGroups;
    }

    /**
     * Consolidate all unique group names from a list of articles.
     * @param articles The list of articles to extract groups from.
     * @return A list of unique group names.
     */
    public ArrayList<String> consolidateGroups(ArrayList<Article> articles) {
        HashSet<String> uniqueGroups = new HashSet<>(); // To ensure uniqueness
        for (Article article : articles) {
            uniqueGroups.addAll(article.groups); // Directly access the groups field
        }
        return new ArrayList<>(uniqueGroups);
    }


    /**
     * Format the group name properly.
     * @param groupName The group name to format.
     * @return A formatted group name.
     */
    public static String formatGroupName(String groupName) {
        groupName = groupName.trim();
        String[] words = groupName.split("\\s+");
        StringBuilder formattedGroupName = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                formattedGroupName.append(Character.toUpperCase(word.charAt(0)))
                                  .append(word.substring(1).toLowerCase())
                                  .append(" ");
            }
        }

        return formattedGroupName.toString().trim();
    }

    /**
     * Get all articles from the database that belong to a specific group.
     * @param group The group name to filter by.
     * @return A list of articles from the database that match the specified group.
     */
    public ArrayList<Article> getAllArticlesWithGroup(String group) {
        ArrayList<Article> allArticles = DatabaseHelper.getAllArticles(); // Retrieve articles from the database
        return getAllArticlesWithGroup(allArticles, group);
    }
    
    /**
     * Get all articles from the database that belong to any of the specified groups.
     * @param groups The list of group names to filter by.
     * @return A list of articles from the database that match any of the specified groups.
     */
    public ArrayList<Article> getAllArticlesWithGroups(ArrayList<String> groups) {
        ArrayList<Article> allArticles = DatabaseHelper.getAllArticles(); // Retrieve articles from the database
        return getAllArticlesWithGroups(allArticles, groups);
    }

}
