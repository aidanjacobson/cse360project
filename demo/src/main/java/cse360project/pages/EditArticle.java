package cse360project.pages;
import cse360project.Article;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class EditArticle implements Page{
	
	private Article article; // Assume this is initialized with an Article object
	private TextField titleField;
    private TextArea descriptionArea;
    private TextField keywordsField;
    private TextArea bodyArea;
	StackPane root = new StackPane();
	
	public EditArticle() {
		titleField = new TextField();
        descriptionArea = new TextArea();
        keywordsField = new TextField();
        bodyArea = new TextArea();
        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            new Label("Title:"), titleField,
            new Label("Description:"), descriptionArea,
            new Label("Keywords:"), keywordsField,
            new Label("Body:"), bodyArea,
            createSaveButton());
        
        root.getChildren().add(layout);
}
	private Button createSaveButton() {
        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> saveArticle());
        return saveButton;
    }
	
	private void saveArticle() {
        if (article != null) {
            article.setTitle(titleField.getText());
            article.setDescription(descriptionArea.getText());
            article.setKeywords(keywordsField.getText());
            article.setBody(bodyArea.getText());
            showAlert("Success", "Article updated!");
        } else {
            showAlert("Error", "No article to save.");
        }
    }
	private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
	
	
	public void setEditingArticle(Article article) {
    	this.article=article;
    	onPageOpen();
    }
	
	@Override
	public Pane getRoot() {
		return root;
	}	
	
	@Override
	public void onPageOpen() {
		if (article != null) {
	        titleField.setText(article.getTitle());
	        descriptionArea.setText(article.getDescription());
	        keywordsField.setText(article.getKeywords());
	        bodyArea.setText(article.getBody());
	    } else {
	        System.err.println("No article set for editing.");
	    }
	}
}
