package cse360project.pages;
import cse360project.utils.Level;
import java.util.ArrayList; 
import cse360project.Article;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditArticle extends Application implements Page{
	private Article article; // Assume this is initialized with an Article object
	
	StackPane root = new StackPane();
	public EditArticle() {
        // Sample data
        article = new Article(1, Level.BEGINNER, new ArrayList<>(), "Sample Article",
                "This is a sample article description.", "sample, article", "This is the body of the sample article.", new ArrayList<>());
        article = new Article(1, Level.INTERMEDIATE, new ArrayList<>(), "Sample Article",
                "This is a sample article description.", "sample, article", "This is the body of the sample article.", new ArrayList<>());
        article = new Article(1, Level.ADVANCED, new ArrayList<>(), "Sample Article",
                "This is a sample article description.", "sample, article", "This is the body of the sample article.", new ArrayList<>());
        article = new Article(1, Level.EXPERT, new ArrayList<>(), "Sample Article",
                "This is a sample article description.", "sample, article", "This is the body of the sample article.", new ArrayList<>());
        
        VBox layout = new VBox(10);
        
        // Article editing fields
        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField(article.getTitle());
        
        Label descriptionLabel = new Label("Description:");
        TextArea descriptionArea = new TextArea(article.getDescription());
        
        Label keywordsLabel = new Label("Keywords:");
        TextField keywordsField = new TextField(article.getKeywords());
        
        Label bodyLabel = new Label("Body:");
        TextArea bodyArea = new TextArea(article.getBody());

        Button saveButton = new Button("Save Changes");

        saveButton.setOnAction(e -> {
            // Logic to save changes
            article.setTitle(titleField.getText());
            article.setDescription(descriptionArea.getText());
            article.setKeywords(keywordsField.getText());
            article.setBody(bodyArea.getText());
            System.out.println("Article updated!");
        });

        layout.getChildren().addAll(titleLabel, titleField, descriptionLabel, descriptionArea,
                keywordsLabel, keywordsField, bodyLabel, bodyArea, saveButton);
        root.getChildren().add(layout);
}
	Article editingArticle=null;
	public void setEditingArticle(Article article) {
    	editingArticle=article;
    }
	@Override
	public Pane getRoot() {
		return root;
	}	
	
	private TextField titleField;
	private TextArea descriptionArea;
	private TextField keywordsField;
	private TextArea bodyArea;
	
	@Override
	public void onPageOpen() {
		if (editingArticle != null) {
	        titleField.setText(editingArticle.getTitle());
	        descriptionArea.setText(editingArticle.getDescription());
	        keywordsField.setText(editingArticle.getKeywords());
	        bodyArea.setText(editingArticle.getBody());
	    } else {
	        System.err.println("No article set for editing.");
	    }
	}
	public static void main(String[] args) {
        launch(args);
    }
	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Edit Article");
        primaryStage.setScene(scene);
        primaryStage.show();
		
	}
}
