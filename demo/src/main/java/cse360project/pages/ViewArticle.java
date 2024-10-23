package cse360project.pages;

import java.util.ArrayList;

import cse360project.Article;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.Level;
import cse360project.utils.Role;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

	public class ViewArticle extends Application {
	    private Article article; // Assume this is initialized with an Article object
	    private String userRole; // Assume this is set based on logged-in user

	    @Override
	    public void start(Stage primaryStage) {
	        // Sample data
	    	Article article = new Article(1, Level.EXPERT, new ArrayList<>(), "Sample Article",
	                "This is a sample article description.", "sample, article", "This is the body of the sample article.", new ArrayList<>());
	        userRole = "admin"; // Change as needed for testing
	        
	        VBox layout = new VBox(10);
	        
	        // Article details
	        Label titleLabel = new Label("Title: " + article.title);
	        Label descriptionLabel = new Label("Description: " + article.description);
	        Label keywordsLabel = new Label("Keywords: " + article.keywords);
	        Label bodyLabel = new Label("Body: " + article.body);

	        // Action buttons
	        Button editButton = new Button("Edit");
	        Button deleteButton = new Button("Delete");

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
	    
	    Article viewingArticle=null;
	    public void setViewingArticle(Article article) {
	    	viewingArticle=article;
	    }
	    private void editArticle() {
	        
	        System.out.println("Navigating to edit page for article: " + article.title);
	    }

	    private void deleteArticle() {
	        
	        System.out.println("Article deleted!");
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
	}
