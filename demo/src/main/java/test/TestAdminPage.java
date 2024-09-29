package test;

import cse360project.User;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.PageManager;
import cse360project.utils.Role;

public class TestAdminPage {
    public static void resetAndTest() {
        // code to test the admin page
        DatabaseHelper.setDatabasePath("~/testdb");
        DatabaseHelper.connectToDatabase();
        DatabaseHelper.deleteAllUsers();
        User user = new User(-1, "admin", "password", "admin@cse360.com", null, true, false, null, "a_first", "a_mid", "a_last", "a_pref", true, true, true);
        User student = new User(-1, "student", "password", "student@cse360.com", null, true, false, null, "s_first", null, "s_last", "s_pref", false, false, true);
        User instructor = new User(-1, "instructor", "password", "instructor@cse360.com", null, true, false, null, "i_first", "i_mid", "i_last", null, false, true, false);
        DatabaseHelper.addUser(user);
        DatabaseHelper.addUser(student);
        DatabaseHelper.addUser(instructor);

        ApplicationStateManager.setLoggedInUser(user);
        ApplicationStateManager.setRole(Role.ADMIN);
        PageManager.switchToPage("admin");
    }

    public static void testWithoutResetting() {
        // code to test the admin page
        DatabaseHelper.setDatabasePath("~/testdb");
        DatabaseHelper.connectToDatabase();
        ApplicationStateManager.setLoggedInUser(DatabaseHelper.getOneUser("SELECT * FROM cse360users WHERE username='admin'"));
        
        ApplicationStateManager.setRole(Role.ADMIN);
        PageManager.switchToPage("admin");
    }
}
