package cse360project.pages.admin;

import cse360project.User;
import cse360project.pages.Page;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.PageManager;
import cse360project.utils.Role;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AdminPage implements Page {
    StackPane root = new StackPane();
    GridPane userListGrid = new GridPane();
    public AdminPage() {
        // root should be aligned top left so logout button resides in top left
        root.setAlignment(Pos.TOP_LEFT);
        // create the logout button
        Button logoutButton = new Button("Log Out");
        logoutButton.setAlignment(Pos.TOP_LEFT);
        
        // set logout button action
        EventHandler<ActionEvent> logoutClick = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                ApplicationStateManager.logout();
            }
        };
        logoutButton.setOnAction(logoutClick);
        root.getChildren().add(logoutButton);

        // since page content should be centered,
        // put everything into a vbox that is centered
        // this fixes all the alignment issues
        VBox pageContent = new VBox();
        pageContent.setMouseTransparent(true); // allow logout button to be clicked
        pageContent.setAlignment(Pos.CENTER);
        root.getChildren().add(pageContent);

        // the user list could get pretty long/wide,
        // so lets put it in a scrollpane so the user can scroll as needed
        ScrollPane userListScrollPane = new ScrollPane();
        userListScrollPane.setContent(userListGrid);
        populateUserListHeaders();
        pageContent.getChildren().add(userListScrollPane);
    }

    public StackPane getRoot() {
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

        System.out.println("Admin page visited");
    }

    void populateUserListHeaders() {
        userListGrid.setAlignment(Pos.CENTER);
        userListGrid.setHgap(10);
        userListGrid.setVgap(10);
        userListGrid.add(new Text("Username"), 0, 0, 1, 2);
        userListGrid.add(new Text("Pref. Name"), 1, 0, 1, 2);
        userListGrid.add(new Text("Full Name"), 2, 0, 1, 2);
        userListGrid.add(new Text("Roles"), 3, 0, 3, 1);
        userListGrid.add(new Text("Admin"), 3, 1, 1, 1);
        userListGrid.add(new Text("Student"), 4, 1, 1, 1);
        userListGrid.add(new Text("Instructor"), 5, 1, 1, 1);
        userListGrid.add(new Text("Reset"), 6, 0, 1, 2);
        userListGrid.add(new Text("Delete"), 7, 0, 1, 2);
    }
}
