package cse360project.utils;

public class ValidationHelper {

    /**
     * Validates the username to ensure it is alphanumeric, underscore, and period
     * with a minimum length of 3 characters.
     * @param username The username to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidUsername(String username) {
        return username.matches("[a-zA-Z0-9._]{3,}");
    }

    /**
     * Validates that the password meets at least 3 of the following:
     * uppercase, lowercase, numeric, special character with a minimum length of 6.
     * @param password The password to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidPassword(String password) {
        int count = 0;
        if (password.length() < 6) return false;
        if (password.matches(".*[a-z].*")) count++; // Lowercase letter
        if (password.matches(".*[A-Z].*")) count++; // Uppercase letter
        if (password.matches(".*[0-9].*")) count++; // Numeric digit
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) count++; // Special character
        return count >= 3;
    }

    /**
     * Validates that two passwords match.
     * @param password The password entered.
     * @param confirmPassword The confirmation password.
     * @return true if passwords match, false otherwise.
     */
    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    /**
     * Validates a name to ensure it contains only alphabetic characters, dashes,
     * apostrophes, periods, and spaces, but no leading or trailing spaces.
     * @param name The name to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidName(String name) {
        return name.matches("[a-zA-Z'-\\.\\s]+") && !name.trim().isEmpty();
    }

    /**
     * Validates an email address using a regular expression to match a standard email format.
     * @param email The email to validate.
     * @return true if the email is valid, false otherwise.
     */
    public static boolean isValidEmail(String email) {
        // Basic email regex pattern: checks for name@domain.something(>2)
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
}
