package cse360project.pages;

import cse360project.utils.ApplicationStateManager;
import cse360project.User;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.PageManager;
import cse360project.utils.ValidationHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserPassSetupPage implements Page {
    // Root element for this page
    StackPane root = new StackPane();

    // Constructor for the UserPassSetupPage
    public UserPassSetupPage() {
        // VBox for the main layout, holding all the elements vertically
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        // Page title setup
        Label titleLabel = new Label("Username and Password Setup");
        titleLabel.setFont(new Font("Arial", 24));

        // GridPane to arrange the labels and text fields in a grid layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20)); // Padding around the grid
        grid.setVgap(10); // Vertical spacing between elements
        grid.setHgap(10); // Horizontal spacing between elements
        grid.setAlignment(Pos.CENTER); // Center the grid

        // Username label and text field
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        // Password label and password field
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        // Confirm Password label and password field
        Label confirmPasswordLabel = new Label("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Re-enter your password");

        // Submit button and its action handling
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            // Convert password fields to char[]
            char[] password = passwordField.getText().toCharArray();
            char[] confirmPassword = confirmPasswordField.getText().toCharArray();

            // Validate passwords match using ValidationHelper
            if (!ValidationHelper.doPasswordsMatch(password, confirmPassword)) {
                System.err.println("Passwords do not match");
                java.util.Arrays.fill(password, '\0'); // Clear immediately if validation fails
                java.util.Arrays.fill(confirmPassword, '\0'); 
                return;
            }

            // Validate password strength using ValidationHelper
            if (!ValidationHelper.isValidPassword(password)) {
                System.err.println("Password must be at least 6 characters and include at least 3 of the following: uppercase, lowercase, number, special character.");
                java.util.Arrays.fill(password, '\0'); // Clear immediately if validation fails
                java.util.Arrays.fill(confirmPassword, '\0'); 
                return;
            }

            // Validate that the username meets the constraints using ValidationHelper
            if (!ValidationHelper.isValidUsername(usernameField.getText())) {
                System.err.println("Invalid username. Must be at least 3 characters and contain only alphanumeric characters, underscores, or periods.");
                java.util.Arrays.fill(password, '\0'); // Clear immediately if validation fails
                java.util.Arrays.fill(confirmPassword, '\0'); 
                return;
            }

            // Use prepared statement to check if the username is already taken in the database
            try {
                PreparedStatement ps = DatabaseHelper.prepareStatement("SELECT * FROM cse360users WHERE username = ?");
                ps.setString(1, usernameField.getText());
                User existingUser = DatabaseHelper.getOneUser(ps);
                if (existingUser != null) {
                    System.err.println("Username is already taken");
                    java.util.Arrays.fill(password, '\0'); // Clear immediately if validation fails
                    java.util.Arrays.fill(confirmPassword, '\0'); 
                    return;
                }
            } catch (SQLException ex) {
                System.err.println("Error checking for existing username: " + ex.getMessage());
                java.util.Arrays.fill(password, '\0'); // Clear immediately if an error occurs
                java.util.Arrays.fill(confirmPassword, '\0'); 
                return;
            }

            // Check if the database is empty to determine if the user should be the admin
            boolean isDatabaseEmpty = DatabaseHelper.isDatabaseEmpty();
            User newUser;

            if (isDatabaseEmpty) {
                // First user, automatically set as admin
                newUser = new User(-1, usernameField.getText(), new String(password), null, null, false, false, null, "", "", "", "", true, false, false);
                System.out.println("Setting up the first user as admin");
            } else {
                // Get the logged-in user (assumed to be an invited user)
                newUser = ApplicationStateManager.getLoggedInUser();

                if (newUser == null || newUser.inviteCode == null || newUser.inviteCode.isEmpty()) {
                    System.err.println("Invite code is required for new user setup");
                    java.util.Arrays.fill(password, '\0'); // Clear immediately if validation fails
                    java.util.Arrays.fill(confirmPassword, '\0'); 
                    return;
                }

                // Update the invited user's details with the username and password they entered
                newUser.username = usernameField.getText();
                newUser.password = new String(password); // Convert char[] to String for the User object
                newUser.inviteCode = null; // Invalidate the invite code after it's used
                
            }
            
            // Save the user details using the DatabaseHelper
            DatabaseHelper.addOrUpdateUser(newUser);
            System.out.println("User saved successfully");

            // Clear the char[] for security reasons
            java.util.Arrays.fill(password, '\0');
            java.util.Arrays.fill(confirmPassword, '\0');

           

            // Clear logging-in user and return to login screen
            ApplicationStateManager.logout();
        });


        // HBox for aligning the submit button in the center
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(submitButton);

        // Adding the labels and text fields to the grid layout
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(confirmPasswordLabel, 0, 2);
        grid.add(confirmPasswordField, 1, 2);

        // Adding the grid and the button to the main layout
        mainLayout.getChildren().addAll(titleLabel, grid, buttonBox);
        root.getChildren().add(mainLayout); // Add the main layout to the root StackPane
    }

    @Override
    public StackPane getRoot() {
        // Return the root StackPane when needed
        return root;
    }

    @Override
    public void onPageOpen() {
        // Actions to perform when the page is opened (if needed)
    }
}
