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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class AdminPage implements Page {
    BorderPane root = new BorderPane();
    GridPane userListGrid = new GridPane();
    public AdminPage() {
        // root should be aligned top left so logout button resides in top left
        // create the logout button
        createLogoutButton();

        StackPane pageContent = new StackPane();
        pageContent.setPadding(new Insets(20, 20, 20, 20)); // Padding inside the grid container
        BorderPane.setMargin(pageContent, new Insets(100, 100, 100, 100)); // Minimum margin around the grid

        // set the user list border style
        pageContent.setStyle("-fx-border-color: black; -fx-border-width: 5px; -fx-border-style: solid; -fx-border-radius: 25px;");

        // center align grid
        StackPane.setAlignment(pageContent, Pos.CENTER);

        // allow logout button to be clicked
        pageContent.setMouseTransparent(true);
        
        root.setCenter(pageContent);

        // the user list could get pretty long/wide,
        // so lets put it in a scrollpane so the user can scroll as needed
        ScrollPane userListScrollPane = new ScrollPane();
        userListScrollPane.setStyle("-fx-background-color:transparent;");
        pageContent.setMouseTransparent(false); // allow mouse events for scrolling and page interactions
        userListScrollPane.setContent(userListGrid);
        populateUserListHeaders();
        pageContent.getChildren().add(userListScrollPane);
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

        updateUserList();
    }

    void populateUserListHeaders() {
        userListGrid.setPadding(new Insets(20)); // Padding around the grid
        userListGrid.setAlignment(Pos.CENTER);
        userListGrid.setGridLinesVisible(true);
        
        userListGrid.add(createTextElement("Username"),      0, 0, 1, 1);
        userListGrid.add(createTextElement("Preferred Name"),    1, 0, 1, 1);
        userListGrid.add(createTextElement("Full Name"),     2, 0, 1, 1);
        userListGrid.add(createTextElement("Is Admin"),      3, 0, 1, 1);
        userListGrid.add(createTextElement("Is Student"),    4, 0, 1, 1);
        userListGrid.add(createTextElement("Is Instructor"), 5, 0, 1, 1);
        // userListGrid.add(createTextElement("Reset"),         6, 0, 1, 1);
        // userListGrid.add(createTextElement("Delete"),        7, 0, 1, 1);


        // userListGrid.add(createTextElement("aidanjacobson"),      0, 1, 1, 1);
        // userListGrid.add(createTextElement("Aidan"),    1, 1, 1, 1);
        // userListGrid.add(createTextElement("Aidan Joseph Jacobson"),     2, 1, 1, 1);
        // userListGrid.add(createTextElement("Is Admin"),      3, 1, 1, 1);
        // userListGrid.add(createTextElement("Is Student"),    4, 1, 1, 1);
        // userListGrid.add(createTextElement("Is Instructor"), 5, 1, 1, 1);
        // userListGrid.add(createResetButton(1),         6, 1, 1, 1);
        // userListGrid.add(createDeleteButton(),        7, 1, 1, 1);
    }

    void updateUserList() {
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
            userListGrid.add(createDeleteButton(user.id), 7, gridRowIndex, 1, 1);
        }
    }

    Label createTextElement(String text) {
        Label out = new Label(text);
        out.setTextAlignment(TextAlignment.CENTER);
        out.setFont(Font.font(20));
        out.setPadding(new Insets(5, 15, 5, 15));
        
        return out;
    }

    Button createResetButton(int userId) {
        Button resetBtn = new Button("Reset");
        resetBtn.setPrefWidth(100);
        GridPane.setMargin(resetBtn, new Insets(5, 15, 5, 15));

        resetBtn.setOnAction(e -> System.out.println(userId));
        return resetBtn;
    }

    Button createDeleteButton(int userId) {
        Button deleteBtn = new Button("Delete");
        deleteBtn.setPrefWidth(100);
        GridPane.setMargin(deleteBtn, new Insets(5, 15, 5, 15));

        deleteBtn.setOnAction(e -> System.out.println(userId));
        return deleteBtn;
    }
}
