package test;

import cse360project.utils.ValidationHelper;

public class TestValidationHelper {

    static int numPassed = 0;
    static int numFailed = 0;

    public static void main(String[] args) {
        System.out.println("____________________________________________________________________________");
        System.out.println("\nValidationHelper Testing Automation");

        // Username tests
        performUsernameTest(1, "validUser_123", true);
        performUsernameTest(2, "in validUser", false);
        performUsernameTest(3, "aB.", true);
        performUsernameTest(4, "no", false);
        performUsernameTest(5, "invalidUser.", false); // Ends with period
        performUsernameTest(6, ".invalidUser", false); // Starts with period
        performUsernameTest(7, "user__name", true);    // Consecutive underscores
        performUsernameTest(8, "user..name", true);    // Consecutive periods

        // Password tests
        performPasswordTest(9, "Abc@123", true);
        performPasswordTest(10, "Abc", false);
        performPasswordTest(11, "123456", false);
        performPasswordTest(12, "Strong@Pass12", true);
        performPasswordTest(13, "AllLowercase", false);
        performPasswordTest(14, "ALLUPPERCASE123", false);
        performPasswordTest(15, "Short1!", false);

        // Password match tests
        performPasswordMatchTest(16, "Password123", "Password123", true);
        performPasswordMatchTest(17, "Password123", "password123", false);

        // Name tests
        performNameTest(18, "John Doe", true);
        performNameTest(19, "John O'Neil", true);
        performNameTest(20, "123 John", false);
        performNameTest(21, "John  ", false);
        performNameTest(22, " Mary-Anne", false); // Leading space
        performNameTest(23, "Anna-Maria", true);
        performNameTest(24, "O'Brien-Smith", true);
        performNameTest(25, "John  Doe", false); // Multiple spaces between names

        // Email tests
        performEmailTest(26, "email@example.com", true);
        performEmailTest(27, "invalidEmail@", false);
        performEmailTest(28, "name+alias@example.co.uk", true);
        performEmailTest(29, "email@domain.c", false); // TLD too short
        performEmailTest(30, "email@-domain.com", false); // Domain starts with hyphen

        System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    // Helper method for testing username validation
    private static void performUsernameTest(int testCase, String username, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase + " (Username Test)");
        System.out.println("Input: \"" + username + "\"");

        boolean result = ValidationHelper.isValidUsername(username);
        evaluateResult(result, expectedPass, "Username validation", username);
    }

    // Helper method for testing password validation
    private static void performPasswordTest(int testCase, String password, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase + " (Password Test)");
        System.out.println("Input: \"" + password + "\"");

        boolean result = ValidationHelper.isValidPassword(password);
        evaluateResult(result, expectedPass, "Password validation", password);
    }

    // Helper method for testing password match validation
    private static void performPasswordMatchTest(int testCase, String password, String confirmPassword, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase + " (Password Match Test)");
        System.out.println("Input: \"" + password + "\" and \"" + confirmPassword + "\"");

        boolean result = ValidationHelper.doPasswordsMatch(password, confirmPassword);
        evaluateResult(result, expectedPass, "Password match validation", password + ", " + confirmPassword);
    }

    // Helper method for testing name validation
    private static void performNameTest(int testCase, String name, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase + " (Name Test)");
        System.out.println("Input: \"" + name + "\"");

        boolean result = ValidationHelper.isValidName(name);
        evaluateResult(result, expectedPass, "Name validation", name);
    }

    // Helper method for testing email validation
    private static void performEmailTest(int testCase, String email, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase + " (Email Test)");
        System.out.println("Input: \"" + email + "\"");

        boolean result = ValidationHelper.isValidEmail(email);
        evaluateResult(result, expectedPass, "Email validation", email);
    }

    // Evaluates if the test passed or failed based on the result and expected output
    private static void evaluateResult(boolean result, boolean expectedPass, String validationType, String input) {
        if (result == expectedPass) {
            System.out.println("***Success*** The " + validationType + " for <" + input + "> passed as expected.");
            numPassed++;
        } else {
            System.err.println("***Failure*** The " + validationType + " for <" + input + "> failed.");
            numFailed++;
        }
    }
}
