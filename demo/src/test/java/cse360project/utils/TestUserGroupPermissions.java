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
}
