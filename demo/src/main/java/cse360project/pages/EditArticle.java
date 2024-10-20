package cse360project.pages;
import cse360project.utils.Level;
import java.util.ArrayList; 
import cse360project.Article;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class EditArticle implements Page{
	private Article article; // Assume this is initialized with an Article object
	public EditArticle() {
        // Sample data
        article = new Article(1, Level.MEDIUM, new ArrayList<>(), "Sample Article",
                "This is a sample article description.", "sample, article", "This is the body of the sample article.", new ArrayList<>());

        VBox layout = new VBox(10);
        
        // Article editing fields
        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField(article.title);
        
        Label descriptionLabel = new Label("Description:");
        TextArea descriptionArea = new TextArea(article.description);
        
        Label keywordsLabel = new Label("Keywords:");
        TextField keywordsField = new TextField(article.keywords);
        
        Label bodyLabel = new Label("Body:");
        TextArea bodyArea = new TextArea(article.body);

        Button saveButton = new Button("Save Changes");

        saveButton.setOnAction(e -> {
            // Logic to save changes
            article.title = titleField.getText();
            article.description = descriptionArea.getText();
            article.keywords = keywordsField.getText();
            article.body = bodyArea.getText();
            System.out.println("Article updated!");
        });

        layout.getChildren().addAll(titleLabel, titleField, descriptionLabel, descriptionArea,
                keywordsLabel, keywordsField, bodyLabel, bodyArea, saveButton);
}

	@Override
	public Pane getRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onPageOpen() {
		// TODO Auto-generated method stub
		
	}
}
