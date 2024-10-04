package cse360project.pages;

import cse360project.utils.PageManager;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PasswordResetPage implements Page {
    private StackPane root = new StackPane();
    private PasswordField newPasswordField;
    private PasswordField confirmPasswordField;

    public PasswordResetPage() {
        VBox vbox = new VBox(10); // Vertical layout with spacing
        Text titleText = new Text("Reset Password");
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

    private void handlePasswordUpdate() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Password Mismatch", "Passwords do not match. Please try again.");
            return;
        }

        if (!isValidPassword(newPassword)) {
            showAlert("Invalid Password", "Password must be at least 8 characters long and contain a mix of letters and numbers.");
            return;
        }

        // Update the password in the database (assume a method is available)
        updatePasswordInDatabase(newPassword);
        
        // Set the OTP flag to false
        setOtpFlagToFalse();

        // Logout the user and redirect to login
        PageManager.switchToPage("login");
    }

    private boolean isValidPassword(String password) {
        // Simple validation: at least 8 characters, and must contain letters and digits
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*[0-9].*");
    }

    private void updatePasswordInDatabase(String newPassword) {
        // Placeholder for actual password update logic
        System.out.println("Password updated to: " + newPassword); // For demonstration
    }

    private void setOtpFlagToFalse() {
        // Logic to set the OTP flag to false in the user's record
        System.out.println("OTP flag set to false.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void onPageOpen() {
        // Check assumptions
        System.out.println("This is the Password Reset Page");
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

        // Check if OTP expiration is valid
        if (!isOtpValid()) {
            showAlert("Error", "Your OTP has expired. Please request a new OTP.");
            PageManager.switchToPage("login");
            return;
        }
    }
    private boolean isUserLoggedIn() {
        // Implement actual check logic here
        return true;
    }

    private boolean isOtpFlagTrue() {
        // Implement actual check logic here
        return true;
    }

    private boolean isOtpValid() {
        // Implement actual check logic here
        return true;
    }
    @Override
    public StackPane getRoot() {
        return root;
    }
}