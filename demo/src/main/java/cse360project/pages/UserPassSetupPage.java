package cse360project.pages;

import cse360project.utils.ApplicationStateManager;
import cse360project.User;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.PageManager;
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
            // Validate passwords match
            if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                System.err.println("Passwords do not match");
                return;
            }

            // Validate that the username is at least 3 characters long and contains no spaces
            if (!usernameField.getText().matches("[a-zA-Z0-9_]{3,}")) {
                System.err.println("Invalid username. Must be at least 3 characters and contain no spaces.");
                return;
            }

            // Check if the username is already taken in the database
            User existingUser = DatabaseHelper.getOneUser("SELECT * FROM cse360users WHERE username = '" + usernameField.getText() + "'");
            if (existingUser != null) {
                System.err.println("Username is already taken");
                return;
            }

            // Check if the database is empty to determine if the user should be the admin
            boolean isDatabaseEmpty = DatabaseHelper.isDatabaseEmpty();

            // Get the logged-in user (assumed to be an invited user)
            User newUser = ApplicationStateManager.getLoggedInUser(); 

            if (isDatabaseEmpty) {
                // First user, automatically set as admin
                newUser.is_admin = true;
                System.out.println("Setting up first user as admin");
            } else {
                // Ensure an invite code was provided for non-admin user setup
                if (newUser.inviteCode != null && !newUser.inviteCode.isEmpty()) {
                    // Update the invited user's details with the username and password they entered
                    newUser.username = usernameField.getText();
                    newUser.password = passwordField.getText();
                    newUser.accountSetUp = true; // Mark the account as set up
                    newUser.inviteCode = null; // Invalidate the invite code after it's used
                } else {
                    System.err.println("Invite code is required for new user setup"); // Error if no invite code provided
                    return;
                }
            }

            // Save the user details to the database (either add or update)
            DatabaseHelper.addOrUpdateUser(newUser);

            // Redirect to the login page after setup
            PageManager.switchToPage("login");
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
