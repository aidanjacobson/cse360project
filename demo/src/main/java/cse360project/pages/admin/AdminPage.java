package cse360project.pages.admin;

import java.util.ArrayList;

import cse360project.User;
import cse360project.pages.Page;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.PageManager;
import cse360project.utils.Role;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class AdminPage implements Page {
    BorderPane root = new BorderPane();
    // Pane main = new Pane();
    GridPane userListGrid = new GridPane();
    StackPane pageContent = new StackPane();

    /**
     * Constructor for admin page
     */
    public AdminPage() {
        // create the main page content window
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

        // create the toolbar at the top of the borderpane
        HBox topContent = new HBox();
        root.setTop(topContent);

        // the toolbar should have logout and invite buttons
        topContent.getChildren().add(createLogoutButton());
        topContent.getChildren().add(createInviteButton());
    }
    
    /** 
     * Get the root of this page, for use in PageManager class
     * @return Pane
     */
    public Pane getRoot() {
        return root;
    }

    /**
     * Run on page open
     */
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

    /**
     * Clear user table and re-render
     */
    void renderUserTable() {
        // remove everything from the table so we can rebuild it
        clearUserTable();

        // populate the header column of the user list
        populateUserListHeaders();

        // update the list of users
        updateUserList();
    }

    /**
     * Clear all users from table, reset gridlines
     */
    void clearUserTable() {
        userListGrid.getChildren().clear();
        userListGrid.setGridLinesVisible(false);
        userListGrid.setGridLinesVisible(true);
    }

    /**
     * Add the header row to the table
     */
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

    /**
     * Add all the users to the table
     */
    void updateUserList() {
        // get all the users in the database
        ArrayList<User> allUsers = DatabaseHelper.getAllUsers();

        // add each user to the table
        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);

            // the first row is the header row, so move one row down
            int gridRowIndex = i+1;

            // obtain username, name
            String username = user.username;
            String prefName = user.getPreferredName();
            String fullName = user.getFullName();

            // add info to table
            userListGrid.add(createTextElement(username), 0, gridRowIndex, 1, 1);
            userListGrid.add(createTextElement(prefName), 1, gridRowIndex, 1, 1);
            userListGrid.add(createTextElement(fullName), 2, gridRowIndex, 1, 1);
            userListGrid.add(createRoleCheckbox(user, Role.ADMIN), 3, gridRowIndex, 1, 1);
            userListGrid.add(createRoleCheckbox(user, Role.STUDENT), 4, gridRowIndex, 1, 1);
            userListGrid.add(createRoleCheckbox(user, Role.INSTRUCTOR), 5, gridRowIndex, 1, 1);
            userListGrid.add(createResetButton(user.id), 6, gridRowIndex, 1, 1);
            userListGrid.add(createDeleteButton(user.id), 7, gridRowIndex, 1, 1);
        }
    }

    
    /** 
     * Create a text element in the table
     * @param text the text string
     * @param bold whether it should be bold
     * @return Label
     */
    Label createTextElement(String text, boolean bold) {
        // create label, set alignment, set padding
        Label out = new Label(text);
        out.setTextAlignment(TextAlignment.CENTER);
        out.setPadding(new Insets(5, 15, 5, 15));
        
        // set bold status
        if (bold) {
            out.setFont(Font.font("system", FontWeight.BOLD, 20));
        } else {
            out.setFont(Font.font("system", 20));
        }
        
        return out;
    }

    /**
     * Create a text element in the table
     * @param text the text string
     * @return Label
     */
    Label createTextElement(String text) {
        // if bold is not specified, default to false
        return createTextElement(text, false);
    }

    /**
     * Create a reset button that will reset a particular user
     * @param userId the id of the user to reset when clicked
     * @return the button
     */
    Button createResetButton(int userId) {
        Button resetBtn = new Button("Reset");
        resetBtn.setPrefWidth(100);
        GridPane.setMargin(resetBtn, new Insets(5, 15, 5, 15));

        resetBtn.setOnAction(e -> attemptUserReset(userId));
        return resetBtn;
    }

    /**
     * Create a delete button that will delete a particular user
     * @param userId the id of the user to delete when clicked
     * @return the button
     */
    Button createDeleteButton(int userId) {
        Button deleteBtn = new Button("Delete");
        deleteBtn.setPrefWidth(100);
        GridPane.setMargin(deleteBtn, new Insets(5, 15, 5, 15));

        // if creating a delete button for ourselves, disable it
        if (userId == ApplicationStateManager.getLoggedInUser().id) {
            deleteBtn.setDisable(true);
        }

        deleteBtn.setOnAction(e -> attemptUserDelete(userId));
        return deleteBtn;
    }

    /**
     * Ask the user if they want to reset, then take action accordingly
     * @param userId the user to reset
     */
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
        String newPass =  String.valueOf(userToReset.password);

        userToReset.OTP = true;
        userToReset.OTP_expiration = User.getNewOTPExpirationTimestamp();

        // update the user in the db
        DatabaseHelper.updateUser(userToReset);
        
        // draft and show email with OTP
        String emailContents = String.format("From: passwords@cse360.com%nTo: %s%nSubject: Password Reset%nHi %s,%nYour temporary password is \"%s\".%nPlease log in and reset it within 30 days.", userToReset.email, userToReset.getPreferredName(), newPass);
        Alert emailAlert = new Alert(AlertType.INFORMATION, emailContents, ButtonType.OK);
        emailAlert.showAndWait();

        // if we just reset ourselves, logout
        if (userToReset.id == ApplicationStateManager.getLoggedInUser().id) {
            ApplicationStateManager.logout();
        }
    }

    /**
     * Ask the user if they want to delete, then take action accordingly
     * @param userId the user to delete
     */
    void attemptUserDelete(int userId) {
        // since we cant delete ourselves, if we somehow manage to click the button, silently fail
        if (userId == ApplicationStateManager.getLoggedInUser().id) return;

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

    /**
     * Create a checkbox that controls whether a particular user has a particular role.
     * @param user - the user that the checkbox controls
     * @param role - the role that the checkbox controls
     * @return the checkbox
     */
    CheckBox createRoleCheckbox(User user, Role role) {
        // create/align checkbox
        CheckBox check = new CheckBox();
        GridPane.setHalignment(check, HPos.CENTER);

        // should the checkbox be selected
        check.setSelected(user.hasRole(role));

        check.setOnAction(e -> {
            boolean hasRole = check.isSelected();
            user.setRole(role, hasRole);
        });

        // we cannot demote ourselves from admin
        if (user.id == ApplicationStateManager.getLoggedInUser().id && role == Role.ADMIN) {
            check.setDisable(true);
        }

        return check;
    }

    /**
     * Create/style invite button and set click action
     * @return the invite button's stackpane
     */
    StackPane createInviteButton() {
        // create the invite button
        Button inviteButton = new Button("Invite User");

        // create a container for the invite button that can be shifted to the right
        StackPane stack = new StackPane();
        stack.setAlignment(Pos.CENTER_RIGHT);
        stack.getChildren().add(inviteButton);

        // set the margin of the button
        StackPane.setMargin(inviteButton, new Insets(20, 20, 20, 20));

        // grow the stack to full size horizontally so the button is on the far right side
        HBox.setHgrow(stack, Priority.ALWAYS);

        // set the click action
        inviteButton.setOnAction(e -> UserInviteWindow.openInviteUserDialog());

        return stack;
    }

    
    /**
     * Create/style logout button and set click action
     * @return the button
     */
    Button createLogoutButton() {
        // create the logout button
        Button logoutButton = new Button("Log Out");
        
        // set logout button action
        logoutButton.setOnAction(e -> ApplicationStateManager.logout());

        // it will be in top left, so pad it away from the corner
        HBox.setMargin(logoutButton, new Insets(20, 20, 20, 20));
        
        // set the top node to be the logout button
        // root.setTop(logoutButton);
        return logoutButton;
    }
}
