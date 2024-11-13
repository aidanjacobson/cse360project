package test;

import cse360project.utils.EncryptionUtils;

public class TestEncryptionUtils {

    static int numPassed = 0;
    static int numFailed = 0;

    public static void main(String[] args) {
        System.out.println("____________________________________________________________________________");
        System.out.println("\nEncryptionUtils Testing Automation");

        // Encrypt and decrypt tests
        performEncryptionDecryptionTest(1, "HelloWorld");
        performEncryptionDecryptionTest(2, "This is a longer text with spaces and special characters!@#");
        performEncryptionDecryptionTest(3, ""); // Empty string

        // Invalid decryption test
        performInvalidDecryptionTest(4, "InvalidCipherText");

        System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    // Helper method for testing encryption and decryption
    private static void performEncryptionDecryptionTest(int testCase, String inputText) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase + " (Encryption and Decryption Test)");
        System.out.println("Input: \"" + inputText + "\"");

        // Encrypt text
        String encryptedText = EncryptionUtils.encryptString(inputText);
        boolean encryptionSuccess = encryptedText != null && !encryptedText.equals(inputText);
        evaluateResult(encryptionSuccess, true, "Encryption", inputText);

        // Decrypt text
        String decryptedText = EncryptionUtils.decryptString(encryptedText);
        boolean decryptionSuccess = inputText.equals(decryptedText);
        evaluateResult(decryptionSuccess, true, "Decryption", encryptedText);
    }

    // Helper method
    private static void performInvalidDecryptionTest(int testCase, String invalidCipherText) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase + " (Invalid Decryption Test)");
        System.out.println("Input: \"" + invalidCipherText + "\"");

        try {
            EncryptionUtils.decryptString(invalidCipherText);
            evaluateResult(false, true, "Invalid Decryption", invalidCipherText); // Expected to fail
        } catch (RuntimeException e) {
            evaluateResult(true, true, "Invalid Decryption", invalidCipherText); // Expected exception
        }
    }

    
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