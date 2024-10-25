package cse360project.pages.viewedit;

import java.util.ArrayList;

import cse360project.Article;
import cse360project.pages.Page;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.Level;
import cse360project.utils.PageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Desktop;
import java.net.URI;

public class EditPage implements Page {
    StackPane stackRoot = new StackPane();
    ScrollPane scrollRoot = new ScrollPane();
    BorderPane root = new BorderPane();
    
    VBox editorContainer;
    TextField titleTextField;
    TextField descriptionTextField;
    ComboBox<String> levelComboBox;
    Label groupsLabel;
    TextField keywordsTextField;
    TextArea bodyTextField;
    VBox linksContainer;

    public EditPage() {
        scrollRoot.setContent(root);
        stackRoot.getChildren().add(scrollRoot);
        createInterface();
    }

    private void createInterface() {
        
        // lets add a vbox for the editor content
        editorContainer = new VBox(10);
        BorderPane.setMargin(editorContainer, new Insets(20));
        root.setCenter(editorContainer);

        createTitleText();

        createDescriptionText();

        createLevelSelection();

        createArticleGroupsContainer();

        createArticleKeywordsContainer();

        createArticleBody();

        createLinksContainer();

        createBottomButtons();
    }

    private void createTitleText() {
        // we need a textbox for the title
        // font size 35px
        titleTextField = new TextField();
        titleTextField.setMaxWidth(500);
        titleTextField.setPromptText("Enter Article Title");
        titleTextField.setStyle("-fx-font-size: 35px;");
        editorContainer.getChildren().add(titleTextField);
    }

    private void createDescriptionText() {
        // we need a textbox for the description
        // font size 20px
        descriptionTextField = new TextField();
        descriptionTextField.setPrefWidth(800);
        descriptionTextField.setMaxWidth(800);
        descriptionTextField.setPromptText("Enter Article Description");
        descriptionTextField.setStyle("-fx-font-size: 20px;");
        editorContainer.getChildren().add(descriptionTextField);
    }

    private void createLevelSelection() {
        // we need a dropdown selection for the level
        // font size 20px
        // it will have options: BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
        // default to BEGINNER
        levelComboBox = new ComboBox<String>();
        levelComboBox.getItems().addAll("BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT");
        levelComboBox.setValue("BEGINNER");
        levelComboBox.setStyle("-fx-font-size: 20px;");
        editorContainer.getChildren().add(levelComboBox);
    }

    private void createArticleGroupsContainer() {
        // there will be a button called "edit groups", followed by a label which will contain the groups
        Button editGroupsButton = new Button("Edit Groups");
        editGroupsButton.setStyle("-fx-font-size: 20px;");

        editGroupsButton.setOnAction(e -> {
            doEditGroups();
        });

        groupsLabel = new Label("No groups selected");
        groupsLabel.setStyle("-fx-font-size: 20px;");

        HBox groupsContainer = new HBox(10, editGroupsButton, groupsLabel);
        groupsContainer.setAlignment(Pos.CENTER_LEFT);
        editorContainer.getChildren().add(groupsContainer);
    }

    private void createArticleKeywordsContainer() {
        // There will be a label called "Keywords", followed by a textbox to enter keywords
        // the textbox will be to the right of the label
        // everything 20px on one line
        Label keywordsLabel = new Label("Keywords:");
        keywordsLabel.setStyle("-fx-font-size: 20px;");

        keywordsTextField = new TextField();
        keywordsTextField.setPrefWidth(400);
        keywordsTextField.setPromptText("Enter keywords separated by commas");
        keywordsTextField.setStyle("-fx-font-size: 20px;");

        HBox keywordsContainer = new HBox(10, keywordsLabel, keywordsTextField);
        keywordsContainer.setAlignment(Pos.CENTER_LEFT);
        editorContainer.getChildren().add(keywordsContainer);
    }

    private void createArticleBody() {
        // this will be a large textbox for the body of the article
        bodyTextField = new TextArea();
        
        bodyTextField.setPrefWidth(800);
        bodyTextField.setPromptText("Enter Article Body");
        bodyTextField.setStyle("-fx-font-size: 20px;");
        editorContainer.getChildren().add(bodyTextField);
        // bodyTextField.setAlignment(Pos.TOP_LEFT);
        bodyTextField.setWrapText(true);
    }

    private void createLinksContainer() {
        // there will be a button called "edit links" followed by a vbox containing the links
        Button editLinksButton = new Button("Edit Links");
        editLinksButton.setStyle("-fx-font-size: 20px;");

        linksContainer = new VBox(10);
        linksContainer.setAlignment(Pos.TOP_LEFT);

        editLinksButton.setOnAction(e -> {
            doEditLinks();
        });

        VBox linksBox = new VBox(10, editLinksButton, linksContainer);
        editorContainer.getChildren().add(linksBox);
    }

    private void createBottomButtons() {
        Button saveButton = new Button("Save and Close");
        saveButton.setStyle("-fx-font-size: 15px;");
        saveButton.setOnAction(event -> {
            doAttemptSave();
        });
        setLinkButtonStyles(saveButton);

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 15px;");
        cancelButton.setOnAction(event -> {
            doAttemptCancel();
        });
        setLinkButtonStyles(cancelButton);

        // put in an HBox
        HBox bottomButtonContainer = new HBox(10, saveButton, cancelButton);
        bottomButtonContainer.setAlignment(Pos.CENTER_LEFT);
        bottomButtonContainer.setPadding(new Insets(0, 0, 20, 20));
        root.setBottom(bottomButtonContainer);
    }

    private void setLinkButtonStyles(Button linkButton) {
        final String btnBackgroundColor = "#0088ff";
        final Color textColor = Color.WHITE;
        final int btnWidth = 150;

        linkButton.setStyle("-fx-background-color: " + btnBackgroundColor + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        linkButton.setTextFill(textColor);
        linkButton.setPrefWidth(btnWidth);
    }

    public Pane getRoot() {
        return stackRoot;
    }

    Article editingArticle;
    public void setEditingArticle(Article article) {
        editingArticle = article;
    }

    public void onPageOpen() {
        if (editingArticle == null) {
            System.err.println("No article to edit!");
            PageManager.switchToPage("listarticles");
            return;
        }

        // set the title
        titleTextField.setText(editingArticle.title);

        // set the description
        descriptionTextField.setText(editingArticle.description);

        // set the level
        setLevelComboBox();

        // set the groups
        updateGroups();

        // set the keywords
        keywordsTextField.setText(editingArticle.keywords);

        // set the body
        bodyTextField.setText(editingArticle.body);

        // set the links
        updateLinks();
    }

    public void updateGroups() {
        ArrayList<String> groups = editingArticle.groups;
        if (groups.size() == 0) {
            groupsLabel.setText("No groups selected");
        } else {
            groupsLabel.setText(String.join(", ", groups));
        }
    }

    public void updateLinks() {
        linksContainer.getChildren().clear();

        for (String link : editingArticle.links) {
            if (linkContainsValidURL(link)) {
                createURLLink(link);
            } else {
                addTextLink(link);
            }
        }
    }

    private String urlRegexp = "((?:https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])";

    private boolean linkContainsValidURL(String link) {
        Pattern pattern = Pattern.compile(urlRegexp);
        Matcher matcher = pattern.matcher(link);
        boolean found = matcher.find();
        return found;
    }

    private void createURLLink(String link) {
        Pattern pattern = Pattern.compile(urlRegexp);
        Matcher matcher = pattern.matcher(link);

        if (!matcher.find()) {
            addTextLink(link);
            return;
        }

        String url = matcher.group(1);

        Hyperlink linkLabel = new Hyperlink(link);
        linkLabel.setStyle("-fx-font-size: 20px;");

        linkLabel.setOnAction(e -> {
            try {
                // open the link in the browser
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                ex.printStackTrace();
                addTextLink(link);
                return;
            }
        });

        linksContainer.getChildren().add(linkLabel);
    }

    private void addTextLink(String link) {
        Label linkLabel = new Label(link);
        linkLabel.setStyle("-fx-font-size: 20px;");
        linksContainer.getChildren().add(linkLabel);
    }

    private void setLevelComboBox() {
        Level articleLevel = editingArticle.level;
        String levelString = Level.levelToString(articleLevel);
        levelComboBox.setValue(levelString);
    }

    private void doAttemptSave() {
        // check if all fields are filled
        // only some fields are required
        // title
        if (titleTextField.getText().isEmpty()) {
            failSave("Please enter a title");
            return;
        }

        // body
        if (bodyTextField.getText().isEmpty()) {
            failSave("Please enter a body");
            return;
        }

        // set the article fields
        editingArticle.title = titleTextField.getText();
        editingArticle.description = descriptionTextField.getText();
        editingArticle.level = Level.stringToLevel(levelComboBox.getValue());
        editingArticle.keywords = keywordsTextField.getText();
        editingArticle.body = bodyTextField.getText();

        DatabaseHelper.addOrUpdateArticle(editingArticle);
        
        ViewPage viewPage = (ViewPage) PageManager.getPageByName("viewpage");
        viewPage.setViewingArticle(editingArticle);
        PageManager.switchToPage("viewpage");
    }

    private void failSave(String message) {
        Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void doAttemptCancel() {
        // confirm cancel
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to cancel? Any unsaved changes will be lost.", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (editingArticle.ID == -1) {
                    // if the article is new, just go back to the list
                    PageManager.switchToPage("listarticles");
                } else {
                    // if the article is existing, go back to view
                    ViewPage viewPage = (ViewPage) PageManager.getPageByName("viewpage");
                    viewPage.setViewingArticle(editingArticle);
                    PageManager.switchToPage("viewpage");
                }
            }
        });
    }

    private void doEditGroups() {
        ArticleGroupEditor.editArticleGroups(editingArticle);
    }

    private void doEditLinks() {
        ArticleLinkEditor.editArticleLinks(editingArticle);
    }
}
