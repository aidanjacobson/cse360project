package cse360project.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestValidationHelper {

    /**
     * Test valid username
     */
    @Test
    public void testValidUsername() {
        assertTrue("Valid username should pass", ValidationHelper.isValidUsername("validUser_123"));
        assertTrue("Valid username with period should pass", ValidationHelper.isValidUsername("user.name"));
    }

    /**
     * Test invalid username
     */
    @Test
    public void testInvalidUsername() {
        assertFalse("Empty username should fail", ValidationHelper.isValidUsername(""));
        assertFalse("Username less than 3 characters should fail", ValidationHelper.isValidUsername("ab"));
        assertFalse("Username with invalid character '@' should fail", ValidationHelper.isValidUsername("user@name"));
    }

    /**
     * Test valid password
     */
    @Test
    public void testValidPassword() {
        assertTrue("Valid password should pass", ValidationHelper.isValidPassword("Passw0rd!".toCharArray()));
        assertTrue("Valid password with special characters should pass", ValidationHelper.isValidPassword("P@ssw0rd".toCharArray()));
    }

    /**
     * Test invalid password
     */
    @Test
    public void testInvalidPassword() {
        assertFalse("Password less than 6 characters should fail", ValidationHelper.isValidPassword("short".toCharArray()));
        assertFalse("Password missing at least 3 character types should fail", ValidationHelper.isValidPassword("password".toCharArray()));
        assertFalse("Password with only numeric characters should fail", ValidationHelper.isValidPassword("123456".toCharArray()));
    }

    /**
     * Test password match
     */
    @Test
    public void testPasswordMatch() {
        char[] password = "Password123".toCharArray();
        char[] confirmPassword = "Password123".toCharArray();
        assertTrue("Passwords should match", ValidationHelper.doPasswordsMatch(password, confirmPassword));

        confirmPassword = "WrongPassword123".toCharArray();
        assertFalse("Passwords should not match", ValidationHelper.doPasswordsMatch(password, confirmPassword));
    }

    /**
     * Test valid name
     */
    @Test
    public void testValidName() {
        assertTrue("Valid name should pass", ValidationHelper.isValidName("John Doe"));
        assertTrue("Name with apostrophe should pass", ValidationHelper.isValidName("O'Neil"));
        assertTrue("Name with hyphen should pass", ValidationHelper.isValidName("Mary-Jane"));
    }

    /**
     * Test invalid name
     */
    @Test
    public void testInvalidName() {
        assertFalse("Empty name should fail", ValidationHelper.isValidName(""));
        assertFalse("Name with only spaces should fail", ValidationHelper.isValidName("   "));
        assertFalse("Name with invalid character '#' should fail", ValidationHelper.isValidName("John#Doe"));
    }

    /**
     * Test valid email
     */
    @Test
    public void testValidEmail() {
        assertTrue("Valid email should pass", ValidationHelper.isValidEmail("test@example.com"));
        assertTrue("Valid email with domain extension should pass", ValidationHelper.isValidEmail("user.name@example.co"));
    }

    /**
     * Test invalid email
     */
    @Test
    public void testInvalidEmail() {
        assertFalse("Email missing '@' should fail", ValidationHelper.isValidEmail("invalidemail.com"));
        assertFalse("Email missing domain extension should fail", ValidationHelper.isValidEmail("user@domain"));
        assertFalse("Email with multiple '@' should fail", ValidationHelper.isValidEmail("user@domain@com"));
    }
}