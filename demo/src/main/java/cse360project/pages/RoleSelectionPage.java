package cse360project.pages;

import java.util.ArrayList;

import cse360project.User;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.PageManager;
import cse360project.utils.Role;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class RoleSelectionPage implements Page {
    // create the root StackPane
    StackPane root = new StackPane();

    // create the 3 role buttons
    Button adminButton = new Button("Admin");
    Button studentButton = new Button("Student");
    Button instructorButton = new Button("Instructor");

    /**
     * Constructor for Role Selection Page
     */
    public RoleSelectionPage() {
        // make a vbox for main page content and center it on screen
        VBox mainContent = new VBox(10);
        mainContent.setAlignment(Pos.CENTER);

        // add instructions for the user to select a role
        Text instructions = new Text("You have multiple roles.\nPlease select one for this session.");
        instructions.setFont(Font.font(20));
        instructions.setTextAlignment(TextAlignment.CENTER);
        mainContent.getChildren().add(instructions);

        // set button width, height, color, etc.
        setButtonStyles();

        // add the buttons to the page
        HBox roleButtons = new HBox(10);
        roleButtons.setAlignment(Pos.CENTER);
        roleButtons.getChildren().addAll(adminButton, studentButton, instructorButton);
        mainContent.getChildren().add(roleButtons);

        // set the handlers for the buttons
        addButtonClickHandlers();

        // add the main content to the root StackPane
        root.getChildren().add(mainContent);
    }

    /**
     * get the root stackpane, for use in PageManager class
     * Requirement of Page interface
     */
    public StackPane getRoot() {
        return root;
    }

    /**
     * The function that should run when the user visits the page
     * Requirement of Page interface
     */
    public void onPageOpen() {
        // check assumptions:
        // assumption: user is logged in
        // failure mode: return user to login screen
        if (! ApplicationStateManager.isLoggedIn()) {
            System.err.println("User is not logged in!");
            ApplicationStateManager.logout();
            return;
        }

        // get reference to current user
        User user = ApplicationStateManager.getLoggedInUser();

        // check if user needs account to be set up
        if (! user.accountSetUp) {
            PageManager.switchToPage("accountsetup"); // change this line when we have the actual id
            return;
        }

        // get a list of the roles the logged in user has
        ArrayList<Role> userRoles = user.getRoles();

        // assumption: The user has at least one role
        // failure mode: Log the user out
        if (userRoles.size() == 0) {
            System.err.println("Error - this user has no roles!");
            ApplicationStateManager.logout();
            return;
        }

        // if the user only has one role, they should switch to the correct page automatically
        if (userRoles.size() == 1) {
            selectUserRole(userRoles.get(0));
            return;
        }

        // the user has more than one role.
        // Reveal the buttons the user has access to
        // so they can select the one they want.
        setButtonVisibility();

    }

    /**
     * Select the role the user wants to log in as.
     * @param role
     */
    void selectUserRole(Role role) {
        // check the user actually has this role
        // if all goes well they should not be able to call this function with any role except ones they have
        if (! ApplicationStateManager.getLoggedInUser().hasRole(role)) {
            System.err.println("You don't have that role!");
            return;
        }

        // save the users role to the ApplicationStateManager
        ApplicationStateManager.setRole(role);

        // switch to the landing page for the selected role
        switch(role) {
            case ADMIN:
                PageManager.switchToPage("admin");
                break;
            case STUDENT:
                PageManager.switchToPage("student");
                break;
            case INSTRUCTOR:
                PageManager.switchToPage("instructor");
                break;
        }
    }

    /**
     * Set the visibility of the buttons to reflect which roles the user actually has
     */
    void setButtonVisibility() {
        // get the current user
        User user = ApplicationStateManager.getLoggedInUser();

        // for each role, determine whether the user has that role
        // setVisible makes the element visible/invisible depending on if they have the role
        // setManaged(false) collapses the element so it does not take up space if it is invisible

        boolean isAdmin = user.hasRole(Role.ADMIN);
        adminButton.setVisible(isAdmin);
        adminButton.setManaged(isAdmin);

        boolean isStudent = user.hasRole(Role.STUDENT);
        studentButton.setVisible(isStudent);
        studentButton.setManaged(isStudent);

        boolean isInstructor = user.hasRole(Role.INSTRUCTOR);
        instructorButton.setVisible(isInstructor);
        instructorButton.setManaged(isInstructor);
    }

    /**
     * Set the click handlers for the 3 buttons
     */
    void addButtonClickHandlers() {
        // set the handler for admin button
        EventHandler<ActionEvent> adminClick = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                selectUserRole(Role.ADMIN);
            }
        };
        adminButton.setOnAction(adminClick);

        // set the handler for student button
        EventHandler<ActionEvent> studentClick = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                selectUserRole(Role.STUDENT);
            }
        };
        studentButton.setOnAction(studentClick);

        // set the handler for instructor button
        EventHandler<ActionEvent> instructorClick = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                selectUserRole(Role.INSTRUCTOR);
            }
        };
        instructorButton.setOnAction(instructorClick);
    }

    /**
     * Set the width, height, background color, text color, and font size for 3 buttons
     */
    void setButtonStyles() {
        // set button widths
        final int btnWidth = 200;
        adminButton.setPrefWidth(btnWidth);
        studentButton.setPrefWidth(btnWidth);
        instructorButton.setPrefWidth(btnWidth);

        // set button heights
        final int btnHeight = 100;
        adminButton.setPrefHeight(btnHeight);
        studentButton.setPrefHeight(btnHeight);
        instructorButton.setPrefHeight(btnHeight);

        // set button background colors
        final String btnBackgroundColor = "#0088ff";
        adminButton.setStyle("-fx-background-color: " + btnBackgroundColor);
        studentButton.setStyle("-fx-background-color: " + btnBackgroundColor);
        instructorButton.setStyle("-fx-background-color: " + btnBackgroundColor);

        // set button text colors
        final Color btnTextColor = Color.WHITE;
        adminButton.setTextFill(btnTextColor);
        studentButton.setTextFill(btnTextColor);
        instructorButton.setTextFill(btnTextColor);

        // set button font sizes
        final Font buttonFont = Font.font(20);
        adminButton.setFont(buttonFont);
        studentButton.setFont(buttonFont);
        instructorButton.setFont(buttonFont);
    }
}
