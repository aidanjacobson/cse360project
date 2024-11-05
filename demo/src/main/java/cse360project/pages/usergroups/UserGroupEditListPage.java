package cse360project.pages.usergroups;

import java.util.ArrayList;

import cse360project.User;
import cse360project.pages.Page;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.GroupUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * This class is a page that shows a list of users and a button to edit the groups of that user.
 * The users in the list are dynamic based on who is logged in:
 *  - If the user is an admin, then all students and instructors are shown
 *  - If the user is an instructor, then all students are shown
 * The list will be a similar format to the one on the admin page
 */
public class UserGroupEditListPage implements Page {
    private BorderPane root = new BorderPane();
    private GridPane userListGrid = new GridPane();
    private StackPane pageContent = new StackPane();

    private ArrayList<User> displayedUsers = new ArrayList<User>();

    public UserGroupEditListPage() {
        // create the main page content window
        pageContent.setPadding(new Insets(20, 20, 20, 20)); // Padding inside the grid container
        BorderPane.setMargin(pageContent, new Insets(100, 100, 100, 100)); // Minimum margin around the grid

        // set the user list border style
        pageContent.setStyle("-fx-border-color: black; -fx-border-width: 5px; -fx-border-style: solid; -fx-border-radius: 25px;");

        // center align grid
        StackPane.setAlignment(pageContent, Pos.CENTER);

        // allow back button to be clicked
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

         // the toolbar should have back button
         topContent.getChildren().add(createBackButton());
    }

    /**
     * Create/style back button and set click action
     * @return the button
     */
    private Button createBackButton() {
        // create the back button
        Button backButton = new Button("Back");
        
        // set back button action
        backButton.setOnAction(e -> ApplicationStateManager.switchToRolePage());

        // it will be in top left, so pad it away from the corner
        HBox.setMargin(backButton, new Insets(20, 20, 20, 20));
        
        // set the top node to be the back button
        return backButton;
    }

    public Pane getRoot() {
        return root;
    }

    public void onPageOpen() {
        renderUserTable();
    }

    private void renderUserTable() {
        // remove everything from the table so we can rebuild it
        clearUserTable();

        // populate the header column of the user list
        populateUserListHeaders();

        // update the list of users
        updateUserList();
    }

    private void clearUserTable() {
        userListGrid.getChildren().clear();
        userListGrid.setGridLinesVisible(false);
        userListGrid.setGridLinesVisible(true);
    }

    private void populateUserListHeaders() {
        userListGrid.setPadding(new Insets(20)); // Padding around the grid
        userListGrid.setAlignment(Pos.CENTER);
        
        // headers for all columns except edit button
        userListGrid.add(createTextElement("Username", true),          0, 0, 1, 1);
        userListGrid.add(createTextElement("Preferred Name", true),    1, 0, 1, 1);
        userListGrid.add(createTextElement("Full Name", true),         2, 0, 1, 1);
    }

    private void updateUserList() {
        User loggedInUser = ApplicationStateManager.getLoggedInUser();
        displayedUsers = GroupUtils.getAllUsersThatUserCanEditGroups(loggedInUser);

        for (int i = 0; i < displayedUsers.size(); i++) {
            User user = displayedUsers.get(i);

            // the first row is the header row, so move one row down
            int gridRowIndex = i+1;

            // obtain username, name
            String username = user.username;
            String prefName = user.getPreferredName();
            String fullName = user.getFullName();
            
            // add the user to the grid
            userListGrid.add(createTextElement(username), 0, gridRowIndex, 1, 1);
            userListGrid.add(createTextElement(prefName), 1, gridRowIndex, 1, 1);
            userListGrid.add(createTextElement(fullName), 2, gridRowIndex, 1, 1);
            userListGrid.add(createEditButton(user), 3, gridRowIndex, 1, 1);
        }
    }

    private Label createTextElement(String text) {
        // if bold is not specified, default to false
        return createTextElement(text, false);
    }

    private Label createTextElement(String text, boolean bold) {
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

    private Button createEditButton(User user) {
        // create the edit button
        Button editButton = new Button("Edit Groups");
        editButton.setPrefWidth(100);
        GridPane.setMargin(editButton, new Insets(5, 15, 5, 15));

        // set the action for the edit button
        editButton.setOnAction(e -> {
            UserGroupEditWizard.editUserGroups(user);
        });

        return editButton;
    }
}
