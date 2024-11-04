package cse360project.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import cse360project.Article;
import cse360project.User;

public class TestUserGroupPermissions {

    /**
     * Test if an admin can access an article that is not in their group. Should return true because admins can access all articles.
     */
    @Test
    public void test1() {
        // create an admin
        ArrayList<String> adminGroups = new ArrayList<>();
        adminGroups.add("javafx");
        adminGroups.add("eclipse");
        User user = new User(-1, "user", "Password1".toCharArray(), "user@user.com", null, true, false, null, "user", "", "user", null, true, true, false, new ArrayList<>());
        Article article = new Article(-1, Level.BEGINNER, new ArrayList<>(), "title", "desc", "kw1", "body", new ArrayList<String>());
        assertTrue(GroupUtils.userCanAccessArticle(user, article));
    }

    /**
     * Test that an instructor can access an article that is in their group.
     */
    @Test
    public void test2() {
        // create a user
        ArrayList<String> userGroups = new ArrayList<>();
        userGroups.add("javafx");
        userGroups.add("eclipse");
        User user = new User(-1, "user", "Password1".toCharArray(), "user@user.com", null, true, false, null, "user", "", "user", null, false, true, false, userGroups);

        // create an article
        ArrayList<String> articleGroups = new ArrayList<>();
        articleGroups.add("javafx");
        articleGroups.add("junit");
        Article article = new Article(-1, Level.BEGINNER, articleGroups, "title", "desc", "kw1", "body", new ArrayList<String>());

        // test that the user can access the article
        assertTrue(GroupUtils.userCanAccessArticle(user, article));
    }

    /**
     * Test that an instructor cannot access an article that is not in their group.
     */
    @Test
    public void test3() {
        // create a user
        ArrayList<String> userGroups = new ArrayList<>();
        userGroups.add("h2");
        userGroups.add("eclipse");
        User user = new User(-1, "user", "Password1".toCharArray(), "user@user.com", null, true, false, null, "user", "", "user", null, false, true, false, userGroups);

        // create an article
        ArrayList<String> articleGroups = new ArrayList<>();
        articleGroups.add("javafx");
        articleGroups.add("junit");
        Article article = new Article(-1, Level.BEGINNER, articleGroups, "title", "desc", "kw1", "body", new ArrayList<String>());

        // test that the user can access the article
        assertFalse(GroupUtils.userCanAccessArticle(user, article));
    }

    /**
     * Add 5 articles to the database with multiple groups
     * 3 of the articles are in the user's group
     * 2 of the articles are not in the user's group
     * The user is not an admin
     * Test that the user can access all 3 articles in their group
     * Test that the user cannot access the 2 articles not in their group
     */
    @Test
    public void test4() {
        // create a user
        ArrayList<String> userGroups = new ArrayList<>();
        userGroups.add("javafx");
        userGroups.add("eclipse");
        User user = new User(-1, "user", "Password1".toCharArray(), "user@user.com", null, true, false, null, "user", "", "user", null, false, true, false, userGroups);

        // reset the database articles
        DatabaseHelper.setDatabasePath("~/test");
        DatabaseHelper.connectToDatabase();
        DatabaseHelper.deleteAllArticles();

        // create the first article with group javafx
        ArrayList<String> articleGroups = new ArrayList<>();
        articleGroups.add("javafx");
        Article article1 = new Article(-1, Level.BEGINNER, articleGroups, "title1", "desc1", "kw1", "body1", new ArrayList<String>());
        DatabaseHelper.addArticle(article1);
        
        // create the second article with group javafx
        ArrayList<String> articleGroups2 = new ArrayList<>();
        articleGroups2.add("javafx");
        Article article2 = new Article(-1, Level.BEGINNER, articleGroups2, "title2", "desc2", "kw2", "body2", new ArrayList<String>());
        DatabaseHelper.addArticle(article2);

        // create the third article with group javafx
        ArrayList<String> articleGroups3 = new ArrayList<>();
        articleGroups3.add("javafx");
        Article article3 = new Article(-1, Level.BEGINNER, articleGroups3, "title3", "desc3", "kw3", "body3", new ArrayList<String>());
        DatabaseHelper.addArticle(article3);

        // create the fourth article with group h2
        ArrayList<String> articleGroups4 = new ArrayList<>();
        articleGroups4.add("h2");
        Article article4 = new Article(-1, Level.BEGINNER, articleGroups4, "title4", "desc4", "kw4", "body4", new ArrayList<String>());
        DatabaseHelper.addArticle(article4);

        // create the fifth article with group h2
        ArrayList<String> articleGroups5 = new ArrayList<>();
        articleGroups5.add("h2");
        Article article5 = new Article(-1, Level.BEGINNER, articleGroups5, "title5", "desc5", "kw5", "body5", new ArrayList<String>());
        DatabaseHelper.addArticle(article5);

        ArrayList<Article> accessibleArticles = GroupUtils.getAllArticlesForUser(user);
        assertTrue(accessibleArticles.contains(article1));
        assertTrue(accessibleArticles.contains(article2));
        assertTrue(accessibleArticles.contains(article3));
        assertFalse(accessibleArticles.contains(article4));
        assertFalse(accessibleArticles.contains(article5));
    }
}
