package cse360project.utils;

import java.util.Random;

public class PasswordGenerator {
    static Random rand = new Random();

    /**
     * Generate a random password that meets all the requirements
     * - At least 6 characters
     * - 3 of the following: Uppercase, Lowercase, Numeric, Special
     * @return the generated password
     */
    public static String generate() {
        final int length = 8;
        char characters[] = new char[length];

        // make sure the password contains at least one of each requirement
        characters[0] = generateLowercaseCharacter();
        characters[1] = generateUppercaseCharacter();
        characters[2] = generateNumericCharacter();
        characters[3] = generateSpecialCharacter();

        // generate the remaining characters, with upper/lower twice as likely to occur as numeric/special
        for (int i = 4; i < length; i++) {
            int choice = rand.nextInt(6);
            if (choice >= 0 && choice < 2) {
                characters[i] = generateLowercaseCharacter();
            } else if (choice >= 2 && choice < 4) {
                characters[i] = generateUppercaseCharacter();
            } else if (choice == 4) {
                characters[i] = generateNumericCharacter();
            } else if (choice == 5) {
                characters[i] = generateSpecialCharacter();
            }
        }

        // we have all the characters, but in a semi-predictable order. Shuffle them to make it completely random.
        shuffleCharArray(characters);

        return String.valueOf(characters);
    }

    /**
     * Generate a random uppercase char
     * @return an uppercase char
     */
    static char generateUppercaseCharacter() {
        return (char)randInclusive('A', 'Z');
    }

    /**
     * Generate a random lowercase char
     * @return a lowercase char
     */
    static char generateLowercaseCharacter() {
        return (char)randInclusive('a', 'z');
    }

    /**
     * Generate a random numeric char
     * @return a numeric char
     */
    static char generateNumericCharacter() {
        return (char)randInclusive('0', '9');
    }

    /**
     * Generate a random special character
     * Options: ~${}%&*()
     * @return a special char
     */
    static char generateSpecialCharacter() {
        // String specialCharacters = "~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/";
        String specialCharacters = "!${}%&*()";
        return specialCharacters.charAt(randInclusive(0, specialCharacters.length()-1));
    }

    /**
     * Generate a random int between a and b, inclusive.
     * @param a The lower limit
     * @param b The upper limit
     * @return the random int
     */
    static int randInclusive(int a,  int b) {
        return rand.nextInt(b - a + 1) + a;
    }

    /**
     * Given a char array, shuffle it in-place to a random order.
     * @param array the array to shuffle.
     */
    static void shuffleCharArray(char[] array) {
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = rand.nextInt(array.length);
            char temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }
}
