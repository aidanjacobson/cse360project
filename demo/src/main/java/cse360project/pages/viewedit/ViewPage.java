package cse360project.pages.viewedit;

import cse360project.Article;
import cse360project.pages.Page;
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
import javafx.scene.control.Button;

public class ViewPage implements Page {
    BorderPane root = new BorderPane();
    Label titleLabel;
    Label levelLabel;
    Label descriptionLabel;
    Label groupsLabel;
    Label keywordsValueLabel;
    Text bodyText;
    VBox linksContainer;

    public ViewPage() {
        createInterface();
    }

    void createInterface() {
        // first we need an "edit article" button
        Button editButton = new Button("Edit This Article");
        editButton.setOnAction(event -> {
            System.out.println("Editing!");
        });
        setLinkButtonStyles(editButton);

        HBox editButtonContainer = new HBox();
        editButtonContainer.setAlignment(Pos.CENTER_LEFT);
        editButtonContainer.getChildren().add(editButton);
        editButtonContainer.setPadding(new Insets(0, 0, 20, 20));

        root.setTop(editButtonContainer);


        // first we need to create a container for the article to view, should be a vbox
        VBox articleContainer = new VBox(10);
        root.setCenter(articleContainer);

        BorderPane.setMargin(articleContainer, new Insets(20));

        // Create the SplitPane
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);

        // the max height of the splitpane
        splitPane.setMaxHeight(400);

        // Create the left container for the existing components
        VBox leftContainer = new VBox();

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
        descriptionLabel = new Label("kdfbbjk...Article description...Article description...Article description...Article description...Article description...Article description...Article description...");

        
        leftContainer.getChildren().addAll(levelLabel, descriptionLabel);
        levelLabel.setStyle("-fx-font-size: 20px;");
        descriptionLabel.setStyle("-fx-font-size: 20px;");
        descriptionLabel.setWrapText(true);

        // add some margin to the bottom of the description
        VBox.setMargin(descriptionLabel, new Insets(0, 0, 20, 0));

        // now we need to create the "Appears in" label, followed by a label that will contain the groups
        // should be in an hbox
        Label appearsInLabel = new Label("Appears in: ");
        groupsLabel = new Label("Group 1, group 2 group 3");
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

        // adding some dummy links
        Label link1 = new Label("Link 1: https://example.com/1");
        Label link2 = new Label("Link 2: https://example.com/2");
        Label link3 = new Label("Link 3: https://example.com/3");
        linksContainer.getChildren().addAll(link1, link2, link3);

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

    private void setLinkButtonStyles(Button linkButton) {
        final String btnBackgroundColor = "#0088ff";
        final Color textColor = Color.WHITE;
        final int btnWidth = 200;

        linkButton.setStyle("-fx-background-color: " + btnBackgroundColor + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        linkButton.setTextFill(textColor);
        linkButton.setPrefWidth(btnWidth);
    }

    public Pane getRoot() {
        return root;
    }

    Article viewingArticle = null;
    public void setViewingArticle(Article article) {
        viewingArticle = article;
    }

    public void onPageOpen() {
        if (viewingArticle == null) {
            System.err.println("Error - no article to view");
            PageManager.switchToPage("listarticles");
            return;
        }

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

    private void setLinks() {
        // clear the links container
        linksContainer.getChildren().clear();

        // add the links
        for (int i = 0; i < viewingArticle.links.size(); i++) {
            Label linkLabel = new Label(viewingArticle.links.get(i));
            linksContainer.getChildren().add(linkLabel);
        }
    }
}
