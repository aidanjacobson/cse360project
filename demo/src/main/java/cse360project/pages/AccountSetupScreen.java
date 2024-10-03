package cse360project.pages;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import cse360project.utils.ValidationHelper;


public class AccountSetupScreen implements Page {
    StackPane root = new StackPane();

    public AccountSetupScreen() {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        Text titleText = new Text("Account Setup Screen");
        vbox.getChildren().add(titleText);

        // First name field
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Enter First Name");
        firstNameField.setMaxWidth(200);
        vbox.getChildren().addAll(firstNameLabel, firstNameField);

        // Middle name (optional) field
        Label middleNameLabel = new Label("Middle Name (Optional):");
        TextField middleNameField = new TextField();
        middleNameField.setPromptText("Enter Middle Name");
        middleNameField.setMaxWidth(200);
        vbox.getChildren().addAll(middleNameLabel, middleNameField);

        // Last name field
        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Enter Last Name");
        lastNameField.setMaxWidth(200);
        vbox.getChildren().addAll(lastNameLabel, lastNameField);

        // Preferred name field
        Label preferredNameLabel = new Label("Preferred Name:");
        TextField preferredNameField = new TextField();
        preferredNameField.setPromptText("Enter Preferred Name");
        preferredNameField.setMaxWidth(200);
        vbox.getChildren().addAll(preferredNameLabel, preferredNameField);

        // Email field
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email");
        emailField.setMaxWidth(200);
        vbox.getChildren().addAll(emailLabel, emailField);

        Label errorMessage = new Label();
        vbox.getChildren().add(errorMessage);

        Button submitButton = new Button("Submit");
        vbox.getChildren().add(submitButton);

        submitButton.setOnAction(event -> {
            // Validate email using ValidationHelper
            String email = emailField.getText();
            if (!ValidationHelper.isValidEmail(email)) {
                errorMessage.setText("Invalid email");
                return;
            }

            // Validate first name and last name (no empty or whitespace-only strings)
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            if (firstName.isEmpty() || lastName.isEmpty()) {
                errorMessage.setText("First name and last name cannot be empty or whitespace");
                return;
            }
            
            String preferredName = preferredNameField.getText().trim();
            if (preferredName.isEmpty()) {
                preferredName = firstName + " " + lastName;
            }

            // Optionally, you can validate other fields like preferred name and middle name here

            // If all checks pass
            errorMessage.setText("Account setup successful!");
        });

        root.getChildren().add(vbox);
    }

    public void onPageOpen() {
        System.out.println("You visited the account setup page");
    }

    public StackPane getRoot() {
        return root;
    }
}