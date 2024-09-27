package cse360project.utils;

import cse360project.User;

public class ApplicationStateManager {
    private static User loggedInUser = null;
    // set the logged in user
    // call this whenever a user logs in
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    // get the logged in user
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    // check if user is logged in
    public static boolean isLoggedIn() {
        return (loggedInUser != null);
    }

    // log the user out
    public static void logout() {
        loggedInUser = null;
        loggedInRole = null;
        PageManager.switchToPage("login");
    }

    static Role loggedInRole;
    // get the role the user has selected for this session
    public static Role getRole() {
        return loggedInRole;
    }

    // set the role the user has selected for this session
    // call this on the role selection page
    public static void setRole(Role newRole) {
        loggedInRole = newRole;
    }
}
