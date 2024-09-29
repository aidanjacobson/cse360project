package cse360project.pages;

import cse360project.utils.PageManager;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.Role;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class InstructorPage implements Page {
    StackPane root = new StackPane();

    public InstructorPage() {
        VBox mainLayout = new VBox(10);
        mainLayout.setAlignment(Pos.CENTER);

        // Create the message
        Text instructorText = new Text("You are on the Instructor page");
        instructorText.setFont(new Font("Arial", 24));

        // Create the logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            ApplicationStateManager.logout();  // Logout the user
            PageManager.switchToPage("login"); // Switch to login page
        });

        // Add the message and button to the layout
        mainLayout.getChildren().addAll(instructorText, logoutButton);
        root.getChildren().add(mainLayout);
    }

    @Override
    public StackPane getRoot() {
        return root;
    }

    @Override
    public void onPageOpen() {
        if (!ApplicationStateManager.isLoggedIn() || !ApplicationStateManager.getRole().equals(Role.INSTRUCTOR)) {
            System.err.println("Access denied: You don't have the Instructor role");
            PageManager.switchToPage("login");
        }//checks is the user is logged in and also has the correct role to be on this page
    }

}
