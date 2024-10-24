package cse360project.pages;

import java.util.ArrayList;

import cse360project.Article;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.Level;
import cse360project.utils.PageManager;
import cse360project.utils.Role;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Optional;
import cse360project.utils.DatabaseHelper;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


public class ViewArticle extends Application {
    private Article viewingArticle; // Article to be viewed
    private String userRole; // User role based on logged-in user

    @Override
    public void start(Stage primaryStage) {
        // Sample data (Replace with actual article retrieval logic)
        viewingArticle = new Article(1, Level.EXPERT, new ArrayList<>(), "Sample Article",
                "This is a sample article description.", "sample, article", "This is the body of the sample article.", new ArrayList<>());
        userRole = "admin"; // Change as needed for testing
        
        VBox layout = new VBox(10);
        
        // Article details
        Label titleLabel = new Label("Title: " + viewingArticle.title);
        Label descriptionLabel = new Label("Description: " + viewingArticle.description);
        Label keywordsLabel = new Label("Keywords: " + viewingArticle.keywords);
        Label bodyLabel = new Label("Body: " + viewingArticle.body);

        // Action buttons
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");

        // Set visibility based on user role
        Role userRole = ApplicationStateManager.getRole();
        if (userRole == Role.ADMIN || userRole == Role.INSTRUCTOR) {
            editButton.setVisible(true);
            deleteButton.setVisible(true);
        } else {
            editButton.setVisible(false);
            deleteButton.setVisible(false);
        }

        editButton.setOnAction(e -> editArticle());
        deleteButton.setOnAction(e -> deleteArticle());

        layout.getChildren().addAll(titleLabel, descriptionLabel, keywordsLabel, bodyLabel, editButton, deleteButton);
        
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setTitle("View Article");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void editArticle() {
    	System.out.println("Navigating to edit page for article: " + viewingArticle.getTitle());       
        // Switch to the EditArticle page and pass the current article
        EditArticle editPage = (EditArticle) PageManager.getPageByName("editarticle");
        editPage.setEditingArticle(viewingArticle); // Pass the article to be edited
        PageManager.switchToPage("editarticle"); // Switch to the edit page
    }

    private void deleteArticle() {
        if (viewingArticle != null) {
            // Confirm deletion with the user
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Article");
            alert.setHeaderText("Are you sure you want to delete this article?");
            alert.setContentText("Title: " + viewingArticle.title);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Call to DatabaseHelper to delete the article
                boolean success = DatabaseHelper.deleteArticle(viewingArticle);
                
                if (success) {
                    System.out.println("Article deleted!");
                    // Optionally, navigate back to the list page or update UI
                    PageManager.switchToPage("listpage"); // Example: switch back to the list page
                } else {
                    System.err.println("Failed to delete article.");
                    // Show error message to the user
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Deletion Failed");
                    errorAlert.setContentText("Could not delete the article. Please try again.");
                    errorAlert.showAndWait();
                }
            }
        } else {
            System.err.println("No article selected for deletion.");
            // Optionally notify the user
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Article Selected");
            alert.setContentText("Please select an article to delete.");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setViewingArticle(Article article) {
        this.viewingArticle = article;
    }
}
