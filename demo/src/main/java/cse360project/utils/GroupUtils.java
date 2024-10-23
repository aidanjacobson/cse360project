package cse360project.utils;


import java.util.ArrayList;
import java.util.HashMap;

public class GroupUtils {

    public ArrayList<Article> getAllArticlesWithGroup(ArrayList<Article> articles, String group) {
        ArrayList<Article> articlesInGroup = new ArrayList<>();
        for (Article article : articles) {
            if (article.getGroups().contains(group)) {
                articlesInGroup.add(article);
            }
        }
        return articlesInGroup;
    }

    public ArrayList<Article> getAllArticlesWithGroups(ArrayList<Article> articles, ArrayList<String> groups) {
        ArrayList<Article> articlesInGroups = new ArrayList<>();
        for (Article article : articles) {
            for (String group : groups) {
                if (article.getGroups().contains(group)) {
                    articlesInGroups.add(article);
                    break;
                }
            }
        }
        return articlesInGroups;
    }

    public ArrayList<String> consolidateGroups(ArrayList<Article> articles, ArrayList<String> groups) {
        HashMap<String, ArrayList<Article>> groupMap = new HashMap<>();

        for (String group : groups) {
            ArrayList<Article> articlesInGroup = getAllArticlesWithGroup(articles, group);
            groupMap.put(group, articlesInGroup);
        }

        return groups;
    }

    public String formatGroupName(String groupName) {
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
}

