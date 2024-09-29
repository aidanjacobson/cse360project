package test;

import cse360project.User;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.PageManager;

public class TestRoleSelection {
    public static void testRoleSelection() {
        // code to test the role selection page
        DatabaseHelper.setDatabasePath("~/testdb");
        DatabaseHelper.connectToDatabase();
        DatabaseHelper.deleteAllUsers();
        User user = new User(-1, "admin", "password", "admin@cse360.com", null, true, false, null, "", "", "", "", true, true, true);
        DatabaseHelper.addUser(user);
        ApplicationStateManager.setLoggedInUser(user);
        PageManager.switchToPage("roleselection");
    }
}
