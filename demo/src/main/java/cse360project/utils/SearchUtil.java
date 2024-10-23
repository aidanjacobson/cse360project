package cse360project.utils;

import cse360project.Article;
import java.util.ArrayList;
import java.util.HashSet;

public class SearchUtil {

    /**
     * This method searches the list of articles and returns the articles that match the query.
     * The match is determined by how many words in the query match with the article title, body,
     * description, groups, level, and keywords.
     *
     * @param articles The list of articles to search
     * @param query The search query string
     * @return A filtered list of articles that match the query
     */
    public static ArrayList<Article> searchArticles(ArrayList<Article> articles, String query) {
        ArrayList<Article> matchingArticles = new ArrayList<>();

        // Split the query into individual words
        String[] queryWords = query.toLowerCase().split("\\s+");

        // Search through all articles
        for (Article article : articles) {
            int matchScore = 0;

            // Check article's title
            matchScore += countMatchingWords(article.title.toLowerCase(), queryWords);

            // Check article's body
            matchScore += countMatchingWords(article.body.toLowerCase(), queryWords);

            // Check article's description
            matchScore += countMatchingWords(article.description.toLowerCase(), queryWords);

            // Check article's groups
            matchScore += countMatchingWords(String.join(" ", article.groups).toLowerCase(), queryWords);

            // Check article's level (convert level to string)
            matchScore += countMatchingWords(Level.levelToString(article.level).toLowerCase(), queryWords);

            // Check article's keywords
            matchScore += countMatchingWords(article.keywords.toLowerCase(), queryWords);

            // If there's any match, add the article with its score
            if (matchScore > 0) {
                matchingArticles.add(article);
            }
        }

        return matchingArticles;
    }

    /**
     * Helper method to count how many words from the query match a given text (title, body, description, etc.).
     *
     * @param text The text to be searched (article title, body, etc.)
     * @param queryWords The array of words from the search query
     * @return The number of matching words found
     */
    private static int countMatchingWords(String text, String[] queryWords) {
        int count = 0;

        // Convert the text into a set of words
        HashSet<String> wordsInText = new HashSet<>();
        for (String word : text.split("\\s+")) {
            wordsInText.add(word);
        }

        // Check how many query words are present in the text
        for (String queryWord : queryWords) {
            if (wordsInText.contains(queryWord)) {
                count++;
            }
        }

        return count;
    }
}
