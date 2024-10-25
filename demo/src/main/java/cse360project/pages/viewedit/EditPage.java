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

/**
 * This class is used to edit an article
 * It is accessed by calling the "setEditingArticle" method, then switching to this page
 */
public class EditPage implements Page {
    private StackPane stackRoot = new StackPane();
    private ScrollPane scrollRoot = new ScrollPane();
    private BorderPane root = new BorderPane();
    
    private VBox editorContainer;
    private TextField titleTextField;
    private TextField descriptionTextField;
    private ComboBox<String> levelComboBox;
    private Label groupsLabel;
    private TextField keywordsTextField;
    private TextArea bodyTextField;
    private VBox linksContainer;

    /**
     * Constructor for EditPage
     */
    public EditPage() {
        scrollRoot.setContent(root);
        stackRoot.getChildren().add(scrollRoot);

        // create the interface
        createInterface();
    }

    /**
     * This method is used to create the interface for the edit page
     */
    private void createInterface() {
        // lets add a vbox for the editor content
        editorContainer = new VBox(10);
        BorderPane.setMargin(editorContainer, new Insets(20));
        root.setCenter(editorContainer);

        // create the title text
        createTitleText();

        // create the description text
        createDescriptionText();

        // create the level selection
        createLevelSelection();

        // create the article groups container
        createArticleGroupsContainer();

        // create the article keywords container
        createArticleKeywordsContainer();

        // create the article body
        createArticleBody();

        // create the links container
        createLinksContainer();

        // create the bottom buttons
        createBottomButtons();
    }

    /**
     * This method is used to create the title text field
     */
    private void createTitleText() {
        // we need a textbox for the title
        // font size 35px
        titleTextField = new TextField();
        titleTextField.setMaxWidth(500);
        titleTextField.setPromptText("Enter Article Title");
        titleTextField.setStyle("-fx-font-size: 35px;");
        editorContainer.getChildren().add(titleTextField);
    }

    /**
     * This method is used to create the description text field
     */
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

    /**
     * This method is used to create the level selection dropdown
     */
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

    /**
     * This method is used to create the article groups container
     */
    private void createArticleGroupsContainer() {
        // there will be a button called "edit groups", followed by a label which will contain the groups
        Button editGroupsButton = new Button("Edit Groups");
        editGroupsButton.setStyle("-fx-font-size: 20px;");

        editGroupsButton.setOnAction(e -> {
            // when the button is clicked, open the group editor
            doEditGroups();
        });

        groupsLabel = new Label("No groups selected");
        groupsLabel.setStyle("-fx-font-size: 20px;");

        HBox groupsContainer = new HBox(10, editGroupsButton, groupsLabel);
        groupsContainer.setAlignment(Pos.CENTER_LEFT);
        editorContainer.getChildren().add(groupsContainer);
    }

    /**
     * This method is used to create the article keywords container
     */
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

    /**
     * This method is used to create the article body container
     */
    private void createArticleBody() {
        // this will be a large textbox for the body of the article
        bodyTextField = new TextArea();
        
        bodyTextField.setPrefWidth(800);
        bodyTextField.setPromptText("Enter Article Body");
        bodyTextField.setStyle("-fx-font-size: 20px;");
        editorContainer.getChildren().add(bodyTextField);
        bodyTextField.setWrapText(true);
    }

    /**
     * This method is used to create the links container
     */
    private void createLinksContainer() {
        // there will be a button called "edit links" followed by a vbox containing the links
        Button editLinksButton = new Button("Edit Links");
        editLinksButton.setStyle("-fx-font-size: 20px;");

        linksContainer = new VBox(10);
        linksContainer.setAlignment(Pos.TOP_LEFT);

        editLinksButton.setOnAction(e -> {
            // when the button is clicked, open the link editor
            doEditLinks();
        });

        VBox linksBox = new VBox(10, editLinksButton, linksContainer);
        editorContainer.getChildren().add(linksBox);
    }

    /**
     * This method is used to create the bottom buttons
     * There will be a "Save and Close" button and a "Cancel" button
     */
    private void createBottomButtons() {
        // create the save and close button
        Button saveButton = new Button("Save and Close");
        saveButton.setStyle("-fx-font-size: 15px;");
        saveButton.setOnAction(event -> {
            doAttemptSave();
        });
        setLinkButtonStyles(saveButton);

        // create the cancel button
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

    /**
     * This method is used to set the styles for the link buttons
     * @param linkButton the button to set the styles for
     */
    private void setLinkButtonStyles(Button linkButton) {
        final String btnBackgroundColor = "#0088ff";
        final Color textColor = Color.WHITE;
        final int btnWidth = 150;

        linkButton.setStyle("-fx-background-color: " + btnBackgroundColor + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        linkButton.setTextFill(textColor);
        linkButton.setPrefWidth(btnWidth);
    }

    /**
     * This method is used to get the root of the page
     * @return the root of the page
     */
    public Pane getRoot() {
        return stackRoot;
    }

    private Article editingArticle;

    /**
     * This method is used to set the article that is being edited
     * @param article the article to edit
     */
    public void setEditingArticle(Article article) {
        editingArticle = article;
    }

    /**
     * This method is called when the page is opened
     */
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

    /**
     * Update the groups label with the current groups
     * This is called when the article is opened, and when the groups recieve an update ping
     */
    public void updateGroups() {
        ArrayList<String> groups = editingArticle.groups;
        if (groups.size() == 0) {
            groupsLabel.setText("No groups selected");
        } else {
            groupsLabel.setText(String.join(", ", groups));
        }
    }

    /**
     * Update the links container with the current links
     * This is called when the article is opened, and when the links recieve an update ping
     */
    public void updateLinks() {
        linksContainer.getChildren().clear();

        // loop through the links in the article and add them to the link container
        for (String link : editingArticle.links) {
            // if the link contains a url, make it a hyperlink that opens in the browser
            // otherwise, just add it as text
            if (linkContainsValidURL(link)) {
                createURLLink(link);
            } else {
                addTextLink(link);
            }
        }
    }

    private String urlRegexp = "((?:https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])";

    /**
     * This method is used to check if a link contains a valid URL
     * @param link the link to check
     * @return true if the link contains a valid URL, false otherwise
     */
    private boolean linkContainsValidURL(String link) {
        Pattern pattern = Pattern.compile(urlRegexp);
        Matcher matcher = pattern.matcher(link);
        boolean found = matcher.find();
        return found;
    }

    /**
     * This method is used to create a URL link
     * @param link the link text to create a URL link for
     */
    private void createURLLink(String link) {
        Pattern pattern = Pattern.compile(urlRegexp);
        Matcher matcher = pattern.matcher(link);

        // if the link doesn't contain a URL, just add it as text
        if (!matcher.find()) {
            addTextLink(link);
            return;
        }

        // extract the URL from the link
        String url = matcher.group(1);

        // create a hyperlink with the text content
        Hyperlink linkLabel = new Hyperlink(link);
        linkLabel.setStyle("-fx-font-size: 20px;");

        // when the hyperlink is clicked, open the link in the browser
        linkLabel.setOnAction(e -> {
            try {
                // open the link in the browser
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        });

        linksContainer.getChildren().add(linkLabel);
    }

    /**
     * This method is used to add a text link to the links container
     * It will not be clickable
     * @param link the link text to add
     */
    private void addTextLink(String link) {
        Label linkLabel = new Label(link);
        linkLabel.setStyle("-fx-font-size: 20px;");
        linksContainer.getChildren().add(linkLabel);
    }

    /**
     * This method is used to set the level combobox to the level of the article
     */
    private void setLevelComboBox() {
        Level articleLevel = editingArticle.level;
        String levelString = Level.levelToString(articleLevel);
        levelComboBox.setValue(levelString);
    }

    /**
     * This method is called when the "Save and Close" button is clicked
     */
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

        // save the article
        // it might be new or existing
        DatabaseHelper.addOrUpdateArticle(editingArticle);
        
        // go back to the view page
        ViewPage viewPage = (ViewPage) PageManager.getPageByName("viewpage");
        viewPage.setViewingArticle(editingArticle);
        PageManager.switchToPage("viewpage");
    }

    /**
     * This method is called when the save fails
     * @param message the message to display
     */
    private void failSave(String message) {
        Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * This method is called when the "Cancel" button is clicked
     */
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

    /**
     * This method is called when the "Edit Groups" button is clicked
     */
    private void doEditGroups() {
        ArticleGroupEditor.editArticleGroups(editingArticle);
    }

    /**
     * This method is called when the "Edit Links" button is clicked
     */
    private void doEditLinks() {
        ArticleLinkEditor.editArticleLinks(editingArticle);
    }
}
