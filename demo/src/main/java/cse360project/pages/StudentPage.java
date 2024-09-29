package cse360project.pages;

import cse360project.utils.PageManager;
import cse360project.utils.Role;
import cse360project.utils.ApplicationStateManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StudentPage implements Page {
    StackPane root = new StackPane();

    public StudentPage() {
        VBox mainLayout = new VBox(10);
        mainLayout.setAlignment(Pos.CENTER);

        // Create the message
        Text studentText = new Text("You are on the Student page");
        studentText.setFont(new Font("Arial", 24));

        // Create the logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            ApplicationStateManager.logout();  // Logout the user
            PageManager.switchToPage("login"); // Switch to login page
        });

        // Add the message and button to the layout
        mainLayout.getChildren().addAll(studentText, logoutButton);
        root.getChildren().add(mainLayout);
    }

    @Override
    public StackPane getRoot() {
        return root;
    }

    @Override
    public void onPageOpen() {
        if (!ApplicationStateManager.isLoggedIn() || !ApplicationStateManager.getRole().equals(Role.STUDENT)) {
            System.err.println("Access denied: You don't have the Student role");
            PageManager.switchToPage("login");
        }//checks is the user is logged in and also has the correct role to be on this page
    }
}
