package cse360project.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * Validates that the password (char array) meets at least 3 of the following:
     * uppercase, lowercase, numeric, special character with a minimum length of 6.
     * @param password The password as a char array to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidPassword(char[] password) {
        int count = 0;
        
        // Check if the password is less than 6 characters
        if (password.length < 6) return false;

        // Check for lowercase letters
        for (char c : password) {
            if (Character.isLowerCase(c)) {
                count++;
                break;
            }
        }

        // Check for uppercase letters
        for (char c : password) {
            if (Character.isUpperCase(c)) {
                count++;
                break;
            }
        }

        // Check for numeric digits
        for (char c : password) {
            if (Character.isDigit(c)) {
                count++;
                break;
            }
        }

        // Check for special characters
        for (char c : password) {
            if ("!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?".indexOf(c) >= 0) {
                count++;
                break;
            }
        }

        // Return true if the password contains at least 3 types of characters
        return count >= 3;
    }

    /**
     * Validates that two passwords match by comparing char arrays.
     * @param password The original password as a char array.
     * @param confirmPassword The confirmation password as a char array.
     * @return true if passwords match, false otherwise.
     */
    public static boolean doPasswordsMatch(char[] password, char[] confirmPassword) {
        // Check if both arrays have the same length
        if (password.length != confirmPassword.length) {
            return false;
        }

        // Compare each character in both arrays
        for (int i = 0; i < password.length; i++) {
            if (password[i] != confirmPassword[i]) {
                return false;
            }
        }

        // Return true if all characters match
        return true;
    }

    public static boolean doPasswordsMatch(String p1, String p2) { // temporary stopgap until password is switched to char[]
        return doPasswordsMatch(p1.toCharArray(), p2.toCharArray());
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
        //return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        boolean matchFound = matcher.find();
        return matchFound;
    }
}
