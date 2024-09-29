package cse360project.pages.admin;

import java.util.ArrayList;

import cse360project.User;
import cse360project.pages.Page;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.PageManager;
import cse360project.utils.Role;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class AdminPage implements Page {
    BorderPane root = new BorderPane();
    GridPane userListGrid = new GridPane();
    public AdminPage() {
        // create, position, and style the logout button
        createLogoutButton();

        // create the main page content window
        StackPane pageContent = new StackPane();
        pageContent.setPadding(new Insets(20, 20, 20, 20)); // Padding inside the grid container
        BorderPane.setMargin(pageContent, new Insets(100, 100, 100, 100)); // Minimum margin around the grid

        // set the user list border style
        pageContent.setStyle("-fx-border-color: black; -fx-border-width: 5px; -fx-border-style: solid; -fx-border-radius: 25px;");

        // center align grid
        StackPane.setAlignment(pageContent, Pos.CENTER);

        // allow logout button to be clicked
        pageContent.setMouseTransparent(true);
        
        // the page content should be in the center of the borderpane
        root.setCenter(pageContent);

        // the user list could get pretty long/wide,
        // so lets put it in a scrollpane so the user can scroll as needed
        ScrollPane userListScrollPane = new ScrollPane();
        userListScrollPane.setStyle("-fx-background-color:transparent;");
        pageContent.getChildren().add(userListScrollPane);

        pageContent.setMouseTransparent(false); // allow mouse events for scrolling and page interactions
        userListScrollPane.setContent(userListGrid);
    }

    public void createLogoutButton() {
        // create the logout button
        Button logoutButton = new Button("Log Out");
        
        // set logout button action
        logoutButton.setOnAction(e -> ApplicationStateManager.logout());

        // it will be in top left, so pad it away from the corner
        BorderPane.setMargin(logoutButton, new Insets(20, 20, 20, 20));
        
        // set the top node to be the logout button
        root.setTop(logoutButton);
    }

    public Pane getRoot() {
        return root;
    }

    public void onPageOpen() {
        // check assumptions:
        // assumption: The user is logged in
        // failure mode: return to login screen
        if (! ApplicationStateManager.isLoggedIn()) {
            System.err.println("User is not logged in!");
            ApplicationStateManager.logout();
            return;
        }

        // assumption: The user is an admin
        // failure mode: Return to role selection screen
        User currentUser = ApplicationStateManager.getLoggedInUser();
        if (!currentUser.hasRole(Role.ADMIN)) {
            System.err.println("User is not an admin!");
            PageManager.switchToPage("roleselection");
            return;
        }

        renderUserTable();
    }

    void renderUserTable() {
        // remove everything from the table so we can rebuild it
        clearUserTable();

        // populate the header column of the user list
        populateUserListHeaders();

        // update the list of users
        updateUserList();
    }

    void clearUserTable() {
        userListGrid.getChildren().clear();
        userListGrid.setGridLinesVisible(false);
        userListGrid.setGridLinesVisible(true);
    }

    void populateUserListHeaders() {
        userListGrid.setPadding(new Insets(20)); // Padding around the grid
        userListGrid.setAlignment(Pos.CENTER);
        
        // headers for all columns except reset and delete buttons
        userListGrid.add(createTextElement("Username", true),      0, 0, 1, 1);
        userListGrid.add(createTextElement("Preferred Name", true),    1, 0, 1, 1);
        userListGrid.add(createTextElement("Full Name", true),     2, 0, 1, 1);
        userListGrid.add(createTextElement("Is Admin", true),      3, 0, 1, 1);
        userListGrid.add(createTextElement("Is Student", true),    4, 0, 1, 1);
        userListGrid.add(createTextElement("Is Instructor", true), 5, 0, 1, 1);
    }

    void updateUserList() {
        // get all the users in the database
        ArrayList<User> allUsers = DatabaseHelper.getAllUsers();

        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            int gridRowIndex = i+1;

            String username = user.username;
            String prefName = user.getPreferredName();
            String fullName = user.getFullName();

            String isAdmin = user.is_admin ? "true" : "false";
            String isStudent = user.is_student ? "true" : "false";
            String isInstructor = user.is_instructor ? "true" : "false";

            userListGrid.add(createTextElement(username), 0, gridRowIndex, 1, 1);
            userListGrid.add(createTextElement(prefName), 1, gridRowIndex, 1, 1);
            userListGrid.add(createTextElement(fullName), 2, gridRowIndex, 1, 1);
            userListGrid.add(createTextElement(isAdmin), 3, gridRowIndex, 1, 1);
            userListGrid.add(createTextElement(isStudent), 4, gridRowIndex, 1, 1);
            userListGrid.add(createTextElement(isInstructor), 5, gridRowIndex, 1, 1);

            userListGrid.add(createResetButton(user.id), 6, gridRowIndex, 1, 1);

            // we should only add a delete button if it is not the logged in user
            if (user.id != ApplicationStateManager.getLoggedInUser().id) {
                userListGrid.add(createDeleteButton(user.id), 7, gridRowIndex, 1, 1);
            }
        }
    }

    Label createTextElement(String text, boolean bold) {
        Label out = new Label(text);
        out.setTextAlignment(TextAlignment.CENTER);
        out.setPadding(new Insets(5, 15, 5, 15));
        
        if (bold) {
            out.setFont(Font.font("system", FontWeight.BOLD, 20));
        } else {
            out.setFont(Font.font("system", 20));
        }
        
        return out;
    }

    Label createTextElement(String text) {
        return createTextElement(text, false);
    }

    Button createResetButton(int userId) {
        Button resetBtn = new Button("Reset");
        resetBtn.setPrefWidth(100);
        GridPane.setMargin(resetBtn, new Insets(5, 15, 5, 15));

        resetBtn.setOnAction(e -> attemptUserReset(userId));
        return resetBtn;
    }

    Button createDeleteButton(int userId) {
        Button deleteBtn = new Button("Delete");
        deleteBtn.setPrefWidth(100);
        GridPane.setMargin(deleteBtn, new Insets(5, 15, 5, 15));

        deleteBtn.setOnAction(e -> attemptUserDelete(userId));
        return deleteBtn;
    }

    void attemptUserReset(int userId) {
        // who are we resetting?
        User userToReset = DatabaseHelper.getUserByID(userId);

        // ask to reset
        Alert resetAlert = new Alert(AlertType.CONFIRMATION, "Really reset user '" + userToReset.username + "'?", ButtonType.YES, ButtonType.NO);
        resetAlert.showAndWait();

        // if the user hit no, return
        if (resetAlert.getResult() == ButtonType.NO) return;

        // reset the user's password, set OTP status
        userToReset.password = User.getRandomOTP();
        userToReset.OTP = true;
        userToReset.OTP_expiration = User.getNewOTPExpirationTimestamp();

        // draft and show email with OTP
        String emailContents = String.format("From: passwords@cse360.com%nTo: %s%nSubject: Password Reset%nHi %s,%nYour temporary password is %s.%nPlease log in and reset it within 30 days.", userToReset.email, userToReset.preferredName, userToReset.password);
        Alert emailAlert = new Alert(AlertType.INFORMATION, emailContents, ButtonType.OK);
        emailAlert.showAndWait();

        // update the user in the db
        DatabaseHelper.updateUser(userToReset);

        // if we just reset ourselves, logout
        if (userToReset.id == ApplicationStateManager.getLoggedInUser().id) {
            ApplicationStateManager.logout();
        }
    }

    void attemptUserDelete(int userId) {
        // who are we deleting?
        User userToDelete = DatabaseHelper.getUserByID(userId);

        // ask to delete
        Alert deleteAlert = new Alert(AlertType.CONFIRMATION, "Really delete user '" + userToDelete.username + "'?", ButtonType.YES, ButtonType.NO);
        deleteAlert.showAndWait();

        // if the user hit no, return
        if (deleteAlert.getResult() == ButtonType.NO) return;

        // delete the user and update the table
        DatabaseHelper.deleteUser(userToDelete);
        renderUserTable();
    }
}
