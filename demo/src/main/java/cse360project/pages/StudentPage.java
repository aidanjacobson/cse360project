package cse360project.pages;

import cse360project.utils.PageManager;
import cse360project.utils.Role;
import cse360project.utils.ApplicationStateManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StudentPage implements Page {
    BorderPane root = new BorderPane();

    /**
     * Constructor for Student Page
     */
    public StudentPage() {
        // VBox mainLayout = new VBox(10);
        // mainLayout.setAlignment(Pos.CENTER);

        // // Create the message
        // Text studentText = new Text("You are on the Student page");
        // studentText.setFont(new Font("Arial", 24));

        // // Create the logout button
        // Button logoutButton = new Button("Logout");
        // logoutButton.setOnAction(e -> {
        // ApplicationStateManager.logout(); // Logout the user
        // PageManager.switchToPage("login"); // Switch to login page
        // });

        // // Add the message and button to the layout
        // mainLayout.getChildren().addAll(studentText, logoutButton);
        // root.getChildren().add(mainLayout);

        VBox mainLayout = new VBox(10);
        mainLayout.setAlignment(Pos.CENTER);

        // Create the logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            ApplicationStateManager.logout(); // Logout the user
            PageManager.switchToPage("login"); // Switch to login page
        });

        // add some margin to the logout button
        BorderPane.setMargin(logoutButton, new Insets(20));
        root.setTop(logoutButton);

        // Create the welcome message
        Text studentText = new Text("You are on the Student page");
        studentText.setFont(new Font("Arial", 24));

        // create the "Tools" text
        Text toolsText = new Text("Your Links:");
        toolsText.setFont(new Font("Arial", 20));

        // Add the message to the layout
        mainLayout.getChildren().addAll(studentText, toolsText);

        // create buttons for the following tools:
        // See article list
        Button articleListButton = new Button("See Article List");
        articleListButton.setOnAction(e -> {
            PageManager.switchToPage("listarticles");
        });
        setLinkButtonStyles(articleListButton);

        // Add the buttons to the layout
        mainLayout.getChildren().addAll(articleListButton);
        
        Button studentMessageButton = new Button("See Student Messages");
        studentMessageButton.setOnAction(e -> {
            PageManager.switchToPage("studentmessage");
        });
        setLinkButtonStyles(studentMessageButton);
        
     // Add the buttons to the layout
        mainLayout.getChildren().addAll(studentMessageButton);
        
        // Add the layout to the root
        root.setCenter(mainLayout);
    }

    public void setLinkButtonStyles(Button linkButton) {
        final String btnBackgroundColor = "#0088ff";
        final Color textColor = Color.WHITE;
        final int btnWidth = 200;

        linkButton.setStyle("-fx-background-color: " + btnBackgroundColor
                + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        linkButton.setTextFill(textColor);
        linkButton.setPrefWidth(btnWidth);
    }

    /**
     * Getter for the root element of the page
     * 
     * @return the root element of the page
     */
    @Override
    public Pane getRoot() {
        return root;
    }

    /**
     * Method to run when the page is opened
     */
    @Override
    public void onPageOpen() {
        if (!ApplicationStateManager.isLoggedIn() || !ApplicationStateManager.getRole().equals(Role.STUDENT)) {
            System.err.println("Access denied: You don't have the Student role");
            PageManager.switchToPage("login");
        } // checks is the user is logged in and also has the correct role to be on this
          // page
    }
}
