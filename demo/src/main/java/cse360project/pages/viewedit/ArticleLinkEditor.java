package cse360project.pages.viewedit;

import cse360project.Article;
import cse360project.utils.PageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is used to edit the links of an article
 */
public class ArticleLinkEditor {
    static Stage linksStage;
    static BorderPane root;

    static VBox linkContainer;
    static VBox centerBox;
    static TextField newLinkTextField;

    // The article that is being edited
    static Article editingArticle;

    /**
     * This method is used to edit the links of an article
     * It can be called from the edit page and will open a new window
     * @param article the article to edit the links of
     */
    public static void editArticleLinks(Article article) {
        editingArticle = article;

        linksStage = new Stage();
        linksStage.setTitle("Edit Article Links");

        // do not allow user to interact with other windows
        linksStage.initModality(Modality.APPLICATION_MODAL);

        root = new BorderPane();
        
        Scene linksScene = new Scene(root, 450, 450);
        linksStage.setScene(linksScene);

        linksStage.show();

        // set the title of the window
        setTitle();

        // create the center content box
        centerBox = new VBox(10);
        centerBox.setAlignment(Pos.TOP_LEFT);
        centerBox.setPadding(new Insets(50));

        // add a scroll pane to the center box
        // so that the user can scroll through the links
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(centerBox);

        root.setCenter(scrollPane);

        // add the link text fields
        addLinkTextFields();

        // add a close button to the bottom of the window
        addCloseButton();
    }

    /**
     * This method is used to set the title of the window
     */
    private static void setTitle() {
        Label titleLabel = new Label("Edit Article Links");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
    }

    /**
     * This method adds the "Add Link" text field and button to the window
     * Then it renders the links that are already in the article
     */
    private static void addLinkTextFields() {
        // create an HBox to hold the text field and button
        HBox addLinkContainer = new HBox(10);
        addLinkContainer.setAlignment(Pos.CENTER_LEFT);

        // create the text field for the user to enter a new link
        newLinkTextField = new TextField();
        newLinkTextField.setPromptText("Enter a new link");
        newLinkTextField.setMaxWidth(300);

        // create the button to add the link
        Button addLinkButton = new Button("Add Link");
        addLinkButton.setOnAction(event -> {
            // call the doAddLink method when the button is clicked
            doAddLink();
        });

        addLinkContainer.getChildren().addAll(newLinkTextField, addLinkButton);
        centerBox.getChildren().add(addLinkContainer);

        // create a VBox to hold the links
        linkContainer = new VBox(10);
        linkContainer.setAlignment(Pos.TOP_LEFT);

        centerBox.getChildren().add(linkContainer);

        // render the links
        renderLinks();
    }

    /**
     * Attempt to add a new link to the article
     */
    private static void doAddLink() {
        String link = newLinkTextField.getText();

        // if the link is empty, do not add it
        if (link.isEmpty()) {
            return;
        }

        // add the link to the article and clear/focus the text field
        editingArticle.links.add(link);
        newLinkTextField.clear();
        newLinkTextField.requestFocus();

        // render the links
        renderLinks();

        // send an update ping to the edit page
        sendUpdatePing();
    }

    /**
     * This method renders the links that are in the article
     */
    private static void renderLinks() {
        // clear the link container
        linkContainer.getChildren().clear();

        // loop through the links in the article and add them to the link container
        for (String link : editingArticle.links) {
            // create an HBox to hold the link and remove button
            HBox linkRow = new HBox(10);
            linkRow.setAlignment(Pos.CENTER_LEFT);

            // create the remove button
            Button removeLinkButton = new Button("Remove");
            removeLinkButton.setOnAction(event -> {
                editingArticle.links.remove(link);
                renderLinks();
                sendUpdatePing();
            });

            // create the link label
            Label linkLabel = new Label(link);
            linkLabel.setStyle("-fx-font-size: 16px;");

            linkRow.getChildren().addAll(removeLinkButton, linkLabel);
            linkContainer.getChildren().add(linkRow);
        }
    }

    /**
     * This method adds a close button to the bottom of the window
     */
    private static void addCloseButton() {
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            linksStage.close();
        });

        // set the styles for the close button
        setLinkButtonStyles(closeButton);

        root.setBottom(closeButton);
        BorderPane.setAlignment(closeButton, Pos.CENTER_LEFT);
    }

    /**
     * This method sets the styles for the link buttons
     * @param linkButton the button to set the styles for
     */
    private static void setLinkButtonStyles(Button linkButton) {
        final String btnBackgroundColor = "#0088ff";
        final Color textColor = Color.WHITE;
        final int btnWidth = 150;

        linkButton.setStyle("-fx-background-color: " + btnBackgroundColor + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        linkButton.setTextFill(textColor);
        linkButton.setPrefWidth(btnWidth);
    }

    /**
     * This method sends an update ping to the edit page
     * This will update the links on the edit page
     */
    private static void sendUpdatePing() {
        EditPage editPage = (EditPage) PageManager.getPageByName("editpage");
        editPage.updateLinks();
    }
}
