package cse360project.pages.viewedit;

import java.awt.Desktop;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cse360project.utils.Role;
import cse360project.Article;
import cse360project.pages.Page;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.Level;
import cse360project.utils.PageManager;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;

/**
 * This class is used to view an article
 */
public class ViewPage implements Page {
    private BorderPane root = new BorderPane();
    private Label titleLabel;
    private Label levelLabel;
    private Label descriptionLabel;
    private Label groupsLabel;
    private Label keywordsValueLabel;
    private Text bodyText;
    private VBox linksContainer;
    private HBox editButtonContainer;

    /**
     * Constructor for the ViewPage
     */
    public ViewPage() {
        // create the interface
        createInterface();
    }

    /**
     * This method is used to create the interface for the view page
     */
    private void createInterface() {
        // first we need an "edit article" button
        Button editButton = new Button("Edit This Article");
        setLinkButtonStyles(editButton);

        // when clicked, it should switch to the edit page and set the article to edit
        editButton.setOnAction(event -> {
            EditPage editPage = (EditPage) PageManager.getPageByName("editpage");
            editPage.setEditingArticle(viewingArticle);
            PageManager.switchToPage("editpage");
        });

        // create a delete button
        Button deleteButton = new Button("Delete This Article");
        setLinkButtonStyles(deleteButton);

        // when clicked, it should confirm deletion and then delete the article, then switch to the list page
        deleteButton.setOnAction(event -> {
            // confirm deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Are you sure you want to delete this article?");
            alert.setContentText("This action cannot be undone.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Perform the deletion of the article
                    DatabaseHelper.deleteArticle(viewingArticle);
                    PageManager.switchToPage("listarticles");
                }
            });
        });

        // create a container for the buttons
        editButtonContainer = new HBox(10);
        editButtonContainer.setAlignment(Pos.CENTER_LEFT);
        editButtonContainer.getChildren().addAll(editButton, deleteButton);

        editButtonContainer.setPadding(new Insets(20, 20, 0, 20));

        root.setTop(editButtonContainer);


        // first we need to create a container for the article to view, should be a vbox
        VBox articleContainer = new VBox(10);
        root.setCenter(articleContainer);

        BorderPane.setMargin(articleContainer, new Insets(20));

        // The article container should contain a splitpane
        // on the right side, it should contain the body of the article
        // the left side should contain everything else
        // this will allow the user to expand the body of the article
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);

        // the max height of the splitpane
        splitPane.setMaxHeight(400);

        // Create the left container for everything except the body
        VBox leftContainer = new VBox();
        leftContainer.setPadding(new Insets(10, 20, 10, 20));

        // the article should have a title at the top
        // the font size should be larger than the rest of the article
        titleLabel = new Label("ARTICLE TITLE");
        leftContainer.getChildren().add(titleLabel);
        titleLabel.setStyle("-fx-font-size: 35px;");

        // now we need to create a level and description label
        // they should be next to each other in an hbox, separated by a dash
        // example: BEGINNER - Article description...
        // the level and description should be individual labels
        levelLabel = new Label("BEGINNER");
        descriptionLabel = new Label("Description");
        leftContainer.getChildren().addAll(levelLabel, descriptionLabel);
        
        // set the font size of the level and description
        // and the text of the description should wrap
        levelLabel.setStyle("-fx-font-size: 20px;");
        descriptionLabel.setStyle("-fx-font-size: 15px;");
        descriptionLabel.setWrapText(true);

        // add some margin to the bottom of the description
        VBox.setMargin(descriptionLabel, new Insets(0, 0, 20, 0));

        // now we need to create the "Appears in" label, followed by a label that will contain the groups
        // should be in an hbox
        Label appearsInLabel = new Label("Appears in: ");
        groupsLabel = new Label("Group 1");
        groupsLabel.setWrapText(true);

        HBox appearsInContainer = new HBox(appearsInLabel, groupsLabel);
        appearsInLabel.setStyle("-fx-font-size: 15px;");
        groupsLabel.setStyle("-fx-font-size: 15px;");

        
        leftContainer.getChildren().addAll(appearsInContainer, appearsInLabel, groupsLabel);

        // Create the "Keywords" label and container
        Label keywordsLabel = new Label("Keywords: ");
        keywordsValueLabel = new Label("Keyword1, Keyword2, Keyword3");
        keywordsValueLabel.setWrapText(true);
        
        HBox keywordsContainer = new HBox(keywordsLabel, keywordsValueLabel);
        leftContainer.getChildren().add(keywordsContainer);
        keywordsLabel.setStyle("-fx-font-size: 15px;");
        keywordsValueLabel.setStyle("-fx-font-size: 15px;");

        // Add the left container to the SplitPane
        splitPane.getItems().add(leftContainer);

        // Create the body of the article
        bodyText = new Text("This is the body of the article. It contains the main content and should wrap appropriately.\nnewline\nnewline\nnewline\nnewline\nnewline\nnewline\nnewline\nnewline\nnewline\nnewline\nnewline\nnewline\nnewline\nnewline\nnewline\nnewline\nnewline\nnewline\nnewline");
        bodyText.setStyle("-fx-font-size: 20px;");
        TextFlow bodyContainer = new TextFlow(bodyText);
        bodyContainer.setPadding(new Insets(10, 0, 0, 30));

        // Make the body container scrollable
        ScrollPane bodyScrollPane = new ScrollPane(bodyContainer);
        bodyScrollPane.setFitToWidth(true);
        bodyScrollPane.setPrefHeight(400);

        // Add the body scroll pane to the right side of the SplitPane
        splitPane.getItems().add(bodyScrollPane);

        // Add the SplitPane to the article container
        articleContainer.getChildren().add(splitPane);

        // now we need a section for links
        // lets add a label that says "Links" followed by a vbox that will contain the links
        Label linksLabel = new Label("Links:");
        linksLabel.setStyle("-fx-font-size: 20px;");
        linksContainer = new VBox(5);
        articleContainer.getChildren().addAll(linksLabel, linksContainer);

        // now we need to add a back button. It should be at the bottom of the page
        // when clicked, it should go back to the list page
        // it should be on the left side of the page
        Button backButton = new Button("Back To Articles");
        backButton.setStyle("-fx-font-size: 15px;");
        backButton.setOnAction(event -> {
            PageManager.switchToPage("listarticles");
        });

        // put in an HBox
        HBox backButtonContainer = new HBox(backButton);
        backButtonContainer.setAlignment(Pos.CENTER_LEFT);
        backButtonContainer.setPadding(new Insets(0, 0, 20, 20));
        root.setBottom(backButtonContainer);

       setLinkButtonStyles(backButton);
    }

    /**
     * This method is used to set the styles of the link buttons
     * @param linkButton the button to set the styles for
     */
    private void setLinkButtonStyles(Button linkButton) {
        final String btnBackgroundColor = "#0088ff";
        final Color textColor = Color.WHITE;
        final int btnWidth = 200;

        linkButton.setStyle("-fx-background-color: " + btnBackgroundColor + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        linkButton.setTextFill(textColor);
        linkButton.setPrefWidth(btnWidth);
    }

    /**
     * This method is used to get the root of the view page
     * @return the root of the view page
     */
    public Pane getRoot() {
        return root;
    }

    private Article viewingArticle = null;
    
    /**
     * This method is used to set the article that is being viewed
     * @param article the article to view
     */
    public void setViewingArticle(Article article) {
        viewingArticle = article;
    }

    /**
     * This method is called when the page is opened
     */
    public void onPageOpen() {
        // if there is no article to view, switch to the list page
        if (viewingArticle == null) {
            System.err.println("Error - no article to view");
            PageManager.switchToPage("listarticles");
            return;
        }

        // update the edit button visibility based on the logged in role
        updateEditButtonVisibility();

        // set the article title
        titleLabel.setText(viewingArticle.title);

        // set the level
        levelLabel.setText(Level.levelToString(viewingArticle.level));

        // set the description
        descriptionLabel.setText(viewingArticle.description);

        // set the groups
        groupsLabel.setText(String.join(", ", viewingArticle.groups));

        // set the keywords
        keywordsValueLabel.setText(viewingArticle.keywords);

        // set the body
        bodyText.setText(viewingArticle.body);

        // set the links
        setLinks();
    }

    /**
     * This method is used to update the visibility of the edit button
     */
    private void updateEditButtonVisibility() {
        Role loggedInRole = ApplicationStateManager.getRole();

        // if the user is an admin or instructor, show the edit and delete buttons
        // otherwise, hide them
        if (loggedInRole == Role.ADMIN || loggedInRole == Role.INSTRUCTOR) {
            editButtonContainer.setVisible(true);
        } else {
            editButtonContainer.setVisible(false);
        }
    }

    /**
     * This method is used to set the links for the article
     */
    private void setLinks() {
        // clear the links container
        linksContainer.getChildren().clear();

        // add the links
        for (int i = 0; i < viewingArticle.links.size(); i++) {
            String link = viewingArticle.links.get(i);

            // if the link contains a valid URL, create a hyperlink
            // otherwise, add a text link
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
     * This method is used to create a hyperlink for a URL
     * @param link the link to create a hyperlink for
     */
    private void createURLLink(String link) {
        Pattern pattern = Pattern.compile(urlRegexp);
        Matcher matcher = pattern.matcher(link);

        // if the URL is not found, add a text link
        if (!matcher.find()) {
            addTextLink(link);
            return;
        }

        // get the URL from the link
        String url = matcher.group(1);

        // create a hyperlink with the text content
        Hyperlink linkLabel = new Hyperlink(link);
        linkLabel.setStyle("-fx-font-size: 15px;");

        // when clicked, open the link in the browser
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
     * @param link
     */
    private void addTextLink(String link) {
        Label linkLabel = new Label(link);
        linkLabel.setStyle("-fx-font-size: 15px;");
        linksContainer.getChildren().add(linkLabel);
    }
}
