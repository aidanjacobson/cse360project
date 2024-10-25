package cse360project.pages.viewedit;

import java.util.ArrayList;

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

public class ArticleLinkEditor {
    static Stage linksStage;
    static BorderPane root;

    static VBox linkContainer;
    static VBox centerBox;
    static TextField newLinkTextField;

    static Article editingArticle;
    public static void editArticleLinks(Article article) {
        editingArticle = article;

        linksStage = new Stage();
        linksStage.setTitle("Edit Article Links");

        linksStage.initModality(Modality.APPLICATION_MODAL);

        root = new BorderPane();
        
        Scene linksScene = new Scene(root, 450, 450);
        linksStage.setScene(linksScene);

        linksStage.show();

        setTitle();

        
        centerBox = new VBox(10);
        centerBox.setAlignment(Pos.TOP_LEFT);
        centerBox.setPadding(new Insets(50));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(centerBox);

        root.setCenter(scrollPane);

        addLinkTextFields();

        addCloseButton();
    }

    private static void setTitle() {
        Label titleLabel = new Label("Edit Article Links");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
    }

    private static void addLinkTextFields() {

        HBox addLinkContainer = new HBox(10);
        addLinkContainer.setAlignment(Pos.CENTER_LEFT);

        newLinkTextField = new TextField();
        newLinkTextField.setPromptText("Enter a new link");
        newLinkTextField.setMaxWidth(300);

        Button addLinkButton = new Button("Add Link");
        addLinkButton.setOnAction(event -> {
            doAddLink();
        });

        addLinkContainer.getChildren().addAll(newLinkTextField, addLinkButton);
        centerBox.getChildren().add(addLinkContainer);

        linkContainer = new VBox(10);
        linkContainer.setAlignment(Pos.TOP_LEFT);

        centerBox.getChildren().add(linkContainer);

        renderLinks();
    }

    private static void doAddLink() {
        String link = newLinkTextField.getText();
        if (link.isEmpty()) {
            return;
        }

        editingArticle.links.add(link);
        newLinkTextField.clear();
        newLinkTextField.requestFocus();
        renderLinks();
        sendUpdatePing();
    }

    private static void renderLinks() {
        linkContainer.getChildren().clear();

        for (String link : editingArticle.links) {
            HBox linkRow = new HBox(10);
            linkRow.setAlignment(Pos.CENTER_LEFT);

            Button removeLinkButton = new Button("Remove");
            removeLinkButton.setOnAction(event -> {
                editingArticle.links.remove(link);
                renderLinks();
                sendUpdatePing();
            });

            Label linkLabel = new Label(link);
            linkLabel.setStyle("-fx-font-size: 16px;");

            linkRow.getChildren().addAll(removeLinkButton, linkLabel);
            linkContainer.getChildren().add(linkRow);
        }
    }

    private static void addCloseButton() {
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            linksStage.close();
        });

        setLinkButtonStyles(closeButton);

        root.setBottom(closeButton);
        BorderPane.setAlignment(closeButton, Pos.CENTER_LEFT);
    }

    private static void setLinkButtonStyles(Button linkButton) {
        final String btnBackgroundColor = "#0088ff";
        final Color textColor = Color.WHITE;
        final int btnWidth = 150;

        linkButton.setStyle("-fx-background-color: " + btnBackgroundColor + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        linkButton.setTextFill(textColor);
        linkButton.setPrefWidth(btnWidth);
    }

    private static void sendUpdatePing() {
        EditPage editPage = (EditPage) PageManager.getPageByName("editpage");
        editPage.updateLinks();
    }
}
