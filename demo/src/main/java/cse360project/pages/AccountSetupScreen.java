package cse360project.pages;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import cse360project.User;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.PageManager;
import cse360project.utils.ValidationHelper;


public class AccountSetupScreen implements Page {
    StackPane root = new StackPane();

    /**
     * Constructor for Account Setup Screen
     */
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
        Label preferredNameLabel = new Label("Preferred Name (Optional):");
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
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            if (!ValidationHelper.isValidName(firstName)) {
                errorMessage.setText("Invalid first name.");
                return;
            }
            if (!ValidationHelper.isValidName(lastName)) {
                errorMessage.setText("Invalid last name.");
                return;
            }
            
            // determine preferred name by validating the given name or setting it to null.
            String preferredName = preferredNameField.getText().trim();
            if (preferredName.isEmpty()) {
                preferredName = null;
            } else {
                if (!ValidationHelper.isValidName(preferredName)) {
                    errorMessage.setText("Invalid preferred name.");
                    return;
                }
            }

            // validate the middle name
            String middleName = middleNameField.getText().trim();
            // if the middle name is not empty and not valid, error
            // if it is empty, should be null
            if (middleName.isEmpty()) {
                middleName = null;
            } else {
                if (!ValidationHelper.isValidName(preferredName)) {
                    errorMessage.setText("Invalid middle name.");
                    return;
                }
            }

            // If all checks pass
            errorMessage.setText("Account setup successful!");

            // update the user's info
            User current = ApplicationStateManager.getLoggedInUser();
            current.email =  email;
            current.firstName = firstName;
            current.middleName = middleName;
            current.lastName = lastName;
            current.preferredName = preferredName;
            current.accountSetUp = true;

            // clear fields
            firstNameField.clear();
            middleNameField.clear();
            lastNameField.clear();
            preferredNameField.clear();
            emailField.clear();

            DatabaseHelper.updateUser(current);
            PageManager.switchToPage("roleselection");
        });

        root.getChildren().add(vbox);
    }

    /**
     * Method to run when the page is opened
     */
    public void onPageOpen() {
        // check assumptions
        // assumption: user is logged in
        // failure mode: log the user out
        if (! ApplicationStateManager.isLoggedIn()) {
            ApplicationStateManager.logout();
            return;
        }

        // assumption: The account is not set up
        // failure mode: redirect to role selection
        if (ApplicationStateManager.getLoggedInUser().accountSetUp) {
            PageManager.switchToPage("roleselection");
            return;
        }
    }

    /**
     * get the root stackpane, for use in PageManager class
     * @return the root stackpane
     */
    public StackPane getRoot() {
        return root;
    }
}