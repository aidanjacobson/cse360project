package cse360project.utils;

import cse360project.User;

public class ApplicationStateManager {
    private static User loggedInUser = null;
    /**
     * Set the logged in user
     * Call this whenever the user logs in
     * @param user the user to log in
     */
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    /**
     * Get the logged in user
     * @return the user that is logged in
     */
    public static User getLoggedInUser() {
        return loggedInUser;
    }

   /**
    * Check if a user has logged in yet
    * @return true or false depending on if they are logged in
    */
    public static boolean isLoggedIn() {
        return (loggedInUser != null);
    }

    /**
     * Log the user out
     * Sets loggedinuser and loggedinrole to null,
     * then switches to login page
     */
    public static void logout() {
        loggedInUser = null;
        loggedInRole = null;
        PageManager.switchToPage("login");
    }

    static Role loggedInRole;
    /**
     * get the role the user has selected for this session
     * @return the selected role
     */
    public static Role getRole() {
        return loggedInRole;
    }

    /**
     * set the role the user has selected for this session
     * call this on the role selection page
     * @param newRole the role the user wants to select
     */
    public static void setRole(Role newRole) {
        loggedInRole = newRole;
    }
}
