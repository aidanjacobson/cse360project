package cse360project.utils;

import cse360project.User;

public class ApplicationStateManager {
    private static User loggedInUser = null;
    // set the logged in user
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
    }

    static Role loggedInRole;
    public static Role getRole() {
        return loggedInRole;
    }

    public static void setRole(Role newRole) {
        loggedInRole = newRole;
    }
}
