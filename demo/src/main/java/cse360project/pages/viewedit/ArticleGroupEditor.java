package cse360project.pages.viewedit;

import java.util.ArrayList;

import cse360project.Article;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.GroupUtils;
import cse360project.utils.PageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ArticleGroupEditor {
    static Stage groupsStage;
    static BorderPane root;

    static VBox groupCheckboxContainer;
    static VBox centerBox;
    static TextField newGroupTextField;
    
    static Article editingArticle;
    public static void editArticleGroups(Article article) {
        editingArticle = article;
        extraGroups = new ArrayList<>();

        groupsStage = new Stage();
        groupsStage.setTitle("Edit Article Groups");

        groupsStage.initModality(Modality.APPLICATION_MODAL);

        root = new BorderPane();
        
        Scene groupsScene = new Scene(root, 450, 450);
        groupsStage.setScene(groupsScene);

        groupsStage.show();

        setTitle();

        
        centerBox = new VBox(10);
        centerBox.setAlignment(Pos.TOP_LEFT);
        centerBox.setPadding(new Insets(50));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(centerBox);

        root.setCenter(scrollPane);

        addGroupCheckboxes();

        addCloseButton();
    }

    private static void setTitle() {
        Label titleLabel = new Label("Edit Article Groups");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
    }

    private static void addGroupCheckboxes() {

        // we need a button to add a new group
        // there will be a textfield to enter the group name
        // and a button to add the group to the right side
        HBox addGroupContainer = new HBox(10);
        addGroupContainer.setAlignment(Pos.CENTER_LEFT);

        newGroupTextField = new TextField();
        newGroupTextField.setPromptText("Enter New Group Name");
        newGroupTextField.setMaxWidth(200);

        Button addGroupButton = new Button("Add New Group");
        addGroupButton.setOnAction(e -> {
            doNewGroupClicked();
        });

        addGroupContainer.getChildren().addAll(newGroupTextField, addGroupButton);
        centerBox.getChildren().add(addGroupContainer);

        groupCheckboxContainer = new VBox(10);
        groupCheckboxContainer.setAlignment(Pos.TOP_LEFT);

        centerBox.getChildren().add(groupCheckboxContainer);

        renderCheckboxList();
    }

    private static void addCloseButton() {
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            groupsStage.close();
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

    private static void doNewGroupClicked() {
        String newGroupText = newGroupTextField.getText();
        if (newGroupText.isEmpty()) {
            return;
        }

        ArrayList<String> existingGroups = GroupUtils.consolidateGroups();
        String newGroup = GroupUtils.formatGroupName(newGroupText);

        if (existingGroups.contains(newGroup)) {
            return;
        }

        extraGroups.add(newGroup);
        editingArticle.addGroup(newGroup);
        newGroupTextField.clear();
        sendUpdatePing();
        renderCheckboxList();

        newGroupTextField.requestFocus();
    }

    private static void sendUpdatePing() {
        EditPage editPage = (EditPage) PageManager.getPageByName("editpage");
        editPage.updateGroups();
    }
    
    static ArrayList<String> extraGroups = new ArrayList<>();
    private static void renderCheckboxList() {
        groupCheckboxContainer.getChildren().clear();

        ArrayList<String> groups = GroupUtils.consolidateGroups();
        groups.addAll(extraGroups);

        for (String group : groups) {
            CheckBox groupCheckbox = new CheckBox(group);
            groupCheckbox.setSelected(editingArticle.groups.contains(group));
            groupCheckbox.setOnAction(e -> {
                if (groupCheckbox.isSelected()) {
                    editingArticle.addGroup(group);
                } else {
                    editingArticle.removeGroup(group);
                }

                sendUpdatePing();
            });
            groupCheckboxContainer.getChildren().add(groupCheckbox);
        }
    }
}
