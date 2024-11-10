package cse360project.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

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

    @Test
    public void editPermissionsTest() {
        // reset the database users
        DatabaseHelper.setDatabasePath("~/test");
        DatabaseHelper.connectToDatabase();
        DatabaseHelper.deleteAllUsers();

        // add one admin user, one instructor user, and one student user
        // the users dont need to have any groups
        User admin = new User(-1, "admin", "Password1".toCharArray(), "admin@admin.com", null, true, false, null, "admin", "", "admin", null, true, false, false, new ArrayList<>());
        User instructor = new User(-1, "instructor", "Password1".toCharArray(), "instructor@instructor.com", null, true, false, null, "instructor", "", "instructor", null, false, true, false, new ArrayList<>());
        User student = new User(-1, "student", "Password1".toCharArray(), "student@student.com", null, true, false, null, "student", "", "student", null, false, false, true, new ArrayList<>());

        DatabaseHelper.addUser(admin);
        DatabaseHelper.addUser(instructor);
        DatabaseHelper.addUser(student);

        // make sure the admin can edit the instructor and student, but not the admin
        ArrayList<User> adminEditable = GroupUtils.getAllUsersThatUserCanEditGroups(admin);
        assertFalse(adminEditable.contains(admin));
        assertTrue(adminEditable.contains(instructor));
        assertTrue(adminEditable.contains(student));

        // make sure the instructor can edit the student, but not the admin or instructor
        ArrayList<User> instructorEditable = GroupUtils.getAllUsersThatUserCanEditGroups(instructor);
        assertFalse(instructorEditable.contains(admin));
        assertFalse(instructorEditable.contains(instructor));
        assertTrue(instructorEditable.contains(student));

        // make sure the student cannot edit anyone
        ArrayList<User> studentEditable = GroupUtils.getAllUsersThatUserCanEditGroups(student);
        assertFalse(studentEditable.contains(admin));
        assertFalse(studentEditable.contains(instructor));
        assertFalse(studentEditable.contains(student));
    }

    @Test
    public void getEditableGroupsTest() {
        // set up the database
        DatabaseHelper.setDatabasePath("~/test");
        DatabaseHelper.connectToDatabase();
        DatabaseHelper.deleteAllUsers();
        DatabaseHelper.deleteAllArticles();

        // create 5 articles with these groups
        // 1. javafx, h2
        // 2. javafx
        // 3. h2
        // 4. h2, junit
        // 5. junit

        ArrayList<String> article1Groups = new ArrayList<>();
        article1Groups.add("javafx");
        article1Groups.add("h2");
        Article article1 = new Article(-1, Level.BEGINNER, article1Groups, "title1", "desc1", "kw1", "body1", new ArrayList<String>());
        DatabaseHelper.addArticle(article1);

        ArrayList<String> article2Groups = new ArrayList<>();
        article2Groups.add("javafx");
        Article article2 = new Article(-1, Level.BEGINNER, article2Groups, "title2", "desc2", "kw2", "body2", new ArrayList<String>());
        DatabaseHelper.addArticle(article2);

        ArrayList<String> article3Groups = new ArrayList<>();
        article3Groups.add("h2");
        Article article3 = new Article(-1, Level.BEGINNER, article3Groups, "title3", "desc3", "kw3", "body3", new ArrayList<String>());
        DatabaseHelper.addArticle(article3);

        ArrayList<String> article4Groups = new ArrayList<>();
        article4Groups.add("h2");
        article4Groups.add("junit");
        Article article4 = new Article(-1, Level.BEGINNER, article4Groups, "title4", "desc4", "kw4", "body4", new ArrayList<String>());
        DatabaseHelper.addArticle(article4);

        ArrayList<String> article5Groups = new ArrayList<>();
        article5Groups.add("junit");
        Article article5 = new Article(-1, Level.BEGINNER, article5Groups, "title5", "desc5", "kw5", "body5", new ArrayList<String>());
        DatabaseHelper.addArticle(article5);

        // create users with these groups
        // 1. admin with no groups
        // 2. instructor with javafx group
        // 3. instructor with h2 group
        // 4. instructor with junit and h2 group
        // 5. student with no groups

        User admin = new User(-1, "admin", "Password1".toCharArray(), "a@a.com", null, true, false, null, "admin", "", "admin", null, true, false, false, new ArrayList<>());
        DatabaseHelper.addUser(admin);

        ArrayList<String> instructor1Groups = new ArrayList<>();
        instructor1Groups.add("javafx");
        User instructor1 = new User(-1, "instructor1", "Password1".toCharArray(), "i@i.com", null, true, false, null, "instructor1", "", "instructor1", null, false, true, false, instructor1Groups);
        DatabaseHelper.addUser(instructor1);

        ArrayList<String> instructor2Groups = new ArrayList<>();
        instructor2Groups.add("h2");
        User instructor2 = new User(-1, "instructor2", "Password1".toCharArray(), "i2@i.com", null, true, false, null, "instructor2", "", "instructor2", null, false, true, false, instructor2Groups);
        DatabaseHelper.addUser(instructor2);

        ArrayList<String> instructor3Groups = new ArrayList<>();
        instructor3Groups.add("h2");
        instructor3Groups.add("junit");
        User instructor3 = new User(-1, "instructor3", "Password1".toCharArray(), "i3@i.com", null, true, false, null, "instructor3", "", "instructor3", null, false, true, false, instructor3Groups);
        DatabaseHelper.addUser(instructor3);

        User student = new User(-1, "student", "Password1".toCharArray(), "s@s.com", null, true, false, null, "student", "", "student", null, false, false, true, new ArrayList<>());
        DatabaseHelper.addUser(student);

        // test that the admin can edit all groups
        ArrayList<String> adminEditableGroups = GroupUtils.getAllGroupsThatCanBeEditedByUser(admin);
        assertTrue(adminEditableGroups.contains("Javafx"));
        assertTrue(adminEditableGroups.contains("H2"));
        assertTrue(adminEditableGroups.contains("Junit"));

        // test that instructor1 can only edit javafx
        ArrayList<String> instructor1EditableGroups = GroupUtils.getAllGroupsThatCanBeEditedByUser(instructor1);
        assertTrue(instructor1EditableGroups.contains("Javafx"));
        assertFalse(instructor1EditableGroups.contains("H2"));
        assertFalse(instructor1EditableGroups.contains("Junit"));

        // test that instructor2 can only edit h2
        ArrayList<String> instructor2EditableGroups = GroupUtils.getAllGroupsThatCanBeEditedByUser(instructor2);
        assertFalse(instructor2EditableGroups.contains("Javafx"));
        assertTrue(instructor2EditableGroups.contains("H2"));
        assertFalse(instructor2EditableGroups.contains("Junit"));

        // test that instructor3 can only edit h2 and junit
        ArrayList<String> instructor3EditableGroups = GroupUtils.getAllGroupsThatCanBeEditedByUser(instructor3);
        assertFalse(instructor3EditableGroups.contains("Javafx"));
        assertTrue(instructor3EditableGroups.contains("H2"));
        assertTrue(instructor3EditableGroups.contains("Junit"));

        // test that the student cannot edit any groups
        ArrayList<String> studentEditableGroups = GroupUtils.getAllGroupsThatCanBeEditedByUser(student);
        assertFalse(studentEditableGroups.contains("Javafx"));
        assertFalse(studentEditableGroups.contains("H2"));
        assertFalse(studentEditableGroups.contains("Junit"));
    }
}
