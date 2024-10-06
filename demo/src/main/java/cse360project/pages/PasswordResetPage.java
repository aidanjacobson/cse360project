package cse360project.pages;

import cse360project.User;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.PageManager;
import cse360project.utils.ValidationHelper;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PasswordResetPage implements Page {
    private StackPane root = new StackPane();
    private PasswordField newPasswordField;
    private PasswordField confirmPasswordField;

    /**
     * Constructor for the PasswordResetPage class
     */
    public PasswordResetPage() {
        VBox vbox = new VBox(10); // Vertical layout with spacing
        vbox.setMaxWidth(200);

        // Title for the page
        Text titleText = new Text("Reset Password");
        titleText.setFont(Font.font(25));

        vbox.getChildren().add(titleText);
        vbox.setAlignment(Pos.CENTER);

        // Input field for new password
        newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter new password");
        vbox.getChildren().add(newPasswordField);
        
        // Input field for confirming new password
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm new password");
        vbox.getChildren().add(confirmPasswordField);

        // Button to submit the new password
        Button resetButton = new Button("Update Password");
        resetButton.setOnAction(e -> handlePasswordUpdate());
        vbox.getChildren().add(resetButton);     
        root.getChildren().add(vbox);
    }

    /**
     * Method to handle the password update, when reset button is clicked
     */
    private void handlePasswordUpdate() {
        char[] newPassword = newPasswordField.getText().toCharArray();
        char[] confirmPassword = confirmPasswordField.getText().toCharArray();

        // make sure the passwords match
        if (! ValidationHelper.doPasswordsMatch(newPassword, confirmPassword)) {
            showAlert("Password Mismatch", "Passwords do not match. Please try again.");
            return;
        }

        // Validate the new password
        if (! ValidationHelper.isValidPassword(newPassword)) {
            showAlert("Invalid Password", "Password must be at least 6 characters long and contain at least 3 of the following character types: uppercase, lowercase, numeric, special");
            return;
        }

        // Update the password in the database (assume a method is available)
        updatePasswordInDatabase(newPassword);
        
        // Set the OTP flag to false
        setOtpFlagToFalse();

        // clear the fields
        clearFields();

        // Logout the user and redirect to login
        PageManager.switchToPage("login");
    }

    /**
     * Method to update the password in the database
     * @param newPassword the new password to be updated
     */
    private void updatePasswordInDatabase(char[] newPassword) {
        User current = ApplicationStateManager.getLoggedInUser();
        current.password = newPassword;
        DatabaseHelper.updateUser(current);
    }

    /**
     * Method to set the OTP flag to false in the user's record
     */
    private void setOtpFlagToFalse() {
        // Logic to set the OTP flag to false in the user's record
        User current = ApplicationStateManager.getLoggedInUser();
        current.OTP = false;
        DatabaseHelper.updateUser(current);
    }

    /**
     * Method to show an alert dialog
     * @param title the title of the alert
     * @param message the message to be displayed
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Method to clear the input fields
     */
    private void clearFields() {
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    /**
     * Method to check assumptions when the page is opened
     */
    @Override
    public void onPageOpen() {
        // Check assumptions
        // Check if user is logged in
        if (!isUserLoggedIn()) {
            showAlert("Error", "You must be logged in to reset your password.");
            PageManager.switchToPage("login");
            return;
        }

        // Check if the OTP flag is true
        if (!isOtpFlagTrue()) {
            showAlert("Error", "Your OTP has already been used or is invalid.");
            PageManager.switchToPage("login");
            return;
        }
    }

    /**
     * Method to check if the user is logged in
     * @return true if the user is logged in, false otherwise
     */
    private boolean isUserLoggedIn() {
        return ApplicationStateManager.isLoggedIn();
    }

    /**
     * Method to check if the OTP flag is true
     * @return true if the OTP flag is true, false otherwise
     */
    private boolean isOtpFlagTrue() {
        // Implement actual check logic here
        return ApplicationStateManager.getLoggedInUser().OTP;
    }

    @Override
    public StackPane getRoot() {
        return root;
    }
}