package cse360project;

import java.util.ArrayList;
import java.util.Arrays;

import cse360project.utils.DatabaseHelper;
import cse360project.utils.Level;

public class InitTestDB {
    public static void init() {
        // Ensure that the database is clean before each test
        DatabaseHelper.setDatabasePath("~/testdb");
        DatabaseHelper.connectToDatabase();
        DatabaseHelper.deleteAllArticles();
        DatabaseHelper.deleteAllUsers();
        DatabaseHelper.deleteAllMessages();

        // set up the following users and add to database:
        // 1. username: "admin", password: "Password1", role: "admin", firstname: "Admin", lastname: "Admin"
        // 2. username: "instructor1", password: "Password1", role: "instructor", firstname: "Instructor", lastname: "Group One", groups: ["Group 1"]
        // 3. username: "instructor2", password: "Password1", role: "instructor", firstname: "Instructor", lastname: "Group Two", groups: ["Group 2"]
        // 4. username: "instructor3", password: "Password1", role: "instructor", firstname: "Instructor", lastname: "Group Both", groups: ["Group 1", "Group 2"]
        // 5. username: "student1", password: "Password1", role: "student", firstname: "Student", lastname: "Group One", groups: ["Group 1"]
        // 6. username: "student2", password: "Password1", role: "student", firstname: "Student", lastname: "Group Two", groups: ["Group 2"]
        // 7. username: "student3", password: "Password1", role: "student", firstname: "Student", lastname: "Group Both", groups: ["Group 1", "Group 2"]

        User admin = new User(-1, "admin", "Password1".toCharArray(), "admin@admin.com", null, true, false, null, "Admin", "", "Admin", "", true, false, false, new ArrayList<>());
        DatabaseHelper.addUser(admin);

        User instructor1 = new User(-1, "instructor1", "Password1".toCharArray(), "instructor1@instructor.com", null, true, false, null, "Instructor", "", "Group One", "", false, true, false, new ArrayList<>(Arrays.asList("Group 1")));
        DatabaseHelper.addUser(instructor1);

        User instructor2 = new User(-1, "instructor2", "Password1".toCharArray(), "instructor2@instructor.com", null, true, false, null, "Instructor", "", "Group Two", "", false, true, false, new ArrayList<>(Arrays.asList("Group 2")));
        DatabaseHelper.addUser(instructor2);

        User instructor3 = new User(-1, "instructor3", "Password1".toCharArray(), "instructor3@instructor.com", null, true, false, null, "Instructor", "", "Group Both", "", false, true, false, new ArrayList<>(Arrays.asList("Group 1", "Group 2")));
        DatabaseHelper.addUser(instructor3);

        User student1 = new User(-1, "student1", "Password1".toCharArray(), "student1@student.com", null, true, false, null, "Student", "", "Group One", "", false, false, true, new ArrayList<>(Arrays.asList("Group 1")));
        DatabaseHelper.addUser(student1);

        User student2 = new User(-1, "student2", "Password1".toCharArray(), "student2@student.com", null, true, false, null, "Student", "", "Group Two", "", false, false, true, new ArrayList<>(Arrays.asList("Group 2")));
        DatabaseHelper.addUser(student2);

        User student3 = new User(-1, "student3", "Password1".toCharArray(), "student3@student.com", null, true, false, null, "Student", "", "Group Both", "", false, false, true, new ArrayList<>(Arrays.asList("Group 1", "Group 2")));
        DatabaseHelper.addUser(student3);
        
        // level group title desc kw body link
        
        // set up the following articles and add to database:
        // 1. level: Beginner, group: arraylist["Group 1"], title: "Article 1", desc: "Article 1 Description", kw: "Article 1 Keywords", body: "Article 1 Body", link: empty arraylist
        // 2. level: Intermediate, group: arraylist["Group 2"], title: "Article 2", desc: "Article 2 Description", kw: "Article 2 Keywords", body: "Article 2 Body", link: empty arraylist
        // 3. level: Advanced, group: arraylist["Group 1", "Group 2"], title: "Article 3", desc: "both groups in this one", kw: "Article 3 Keywords", body: "Article 3 Body", link: empty arraylist

        Article article1 = new Article(-1, Level.BEGINNER, new ArrayList<>(Arrays.asList("Group 1")), "Article 1", "Article 1 Description", "Article 1 Keywords", "Article 1 Body", new ArrayList<>());
        DatabaseHelper.addArticle(article1);

        Article article2 = new Article(-1, Level.INTERMEDIATE, new ArrayList<>(Arrays.asList("Group 2")), "Article 2", "Article 2 Description", "Article 2 Keywords", "Article 2 Body", new ArrayList<>());
        DatabaseHelper.addArticle(article2);

        Article article3 = new Article(-1, Level.ADVANCED, new ArrayList<>(Arrays.asList("Group 1", "Group 2")), "Article 3", "both groups in this one", "Article 3 Keywords", "Article 3 Body", new ArrayList<>());
        DatabaseHelper.addArticle(article3);
    }
}
