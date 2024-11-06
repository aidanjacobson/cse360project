package cse360project.utils;

import cse360project.Article;
import java.util.ArrayList;
import java.util.Collections;

public class SearchUtil {

    /**
     * This method searches the list of articles and returns the articles that match the query.
     * The match is determined by how many words in the query match with the article title, 
     * description, groups, level, keywords, and ID. Partial matches are also considered.
     *
     * @param articles The list of articles to search
     * @param query The search query string
     * @return A filtered and sorted list of articles that match the query
     */
    public static ArrayList<Article> searchArticles(ArrayList<Article> articles, String query) {
        ArrayList<ArticleMatch> scoredArticles = new ArrayList<>();
        boolean idMatchFound = false;

        // If query is empty, return all articles without error
        if (query.trim().isEmpty()) {
            return articles;
        }

        // Split the query into individual words
        String[] queryWords = query.toLowerCase().split("\\s+");

        // Search through all articles and compute match scores
        for (Article article : articles) {
            int matchScore = 0;

            // Check if the query contains an ID match
            for (String queryWord : queryWords) {
                try {
                    long queryId = Long.parseLong(queryWord);
                    if (article.getID() == queryId) {
                        // Assign a very high match score for ID match
                        scoredArticles.add(new ArticleMatch(article, Integer.MAX_VALUE));
                        idMatchFound = true;
                        break; // Skip other fields for this article since ID match is prioritized
                    }
                } catch (NumberFormatException e) {
                    // Not a number, continue with the rest of the search
                }
            }

            // Skip other field checks if an ID match was found for this article
            if (idMatchFound) {
                idMatchFound = false; // Reset for the next article
                continue;
            }

            // Check article's title
            matchScore += countMatchingWords(article.title.toLowerCase(), queryWords);

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
                scoredArticles.add(new ArticleMatch(article, matchScore));
            }
        }

        // Sort articles by their match score in descending order
        Collections.sort(scoredArticles);

        // Extract the articles from the sorted list
        ArrayList<Article> matchingArticles = new ArrayList<>();
        for (ArticleMatch match : scoredArticles) {
            matchingArticles.add(match.article);
        }

        return matchingArticles;
    }

    /**
     * Helper method to count how many query words partially match a given text (title, description, etc.).
     *
     * @param text The text to be searched (article title, description, etc.)
     * @param queryWords The array of words from the search query
     * @return The number of partial matches found
     */
    private static int countMatchingWords(String text, String[] queryWords) {
        int count = 0;

        // Convert the text into a set of words
        String[] wordsInText = text.split("\\s+");

        // Check how many query words are present as partial matches in the text
        for (String queryWord : queryWords) {
            for (String wordInText : wordsInText) {
                if (wordInText.contains(queryWord)) { // Partial match
                    count++;
                    break; // No need to check further words in the text for this query word
                }
            }
        }

        return count;
    }

    /**
     * A helper class to associate an article with its match score.
     */
    private static class ArticleMatch implements Comparable<ArticleMatch> {
        Article article;
        int matchScore;

        public ArticleMatch(Article article, int matchScore) {
            this.article = article;
            this.matchScore = matchScore;
        }

        @Override
        public int compareTo(ArticleMatch other) {
            return Integer.compare(other.matchScore, this.matchScore); // Sort descending by match score
        }
    }
}
