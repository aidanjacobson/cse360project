package cse360project.pages.viewedit;

import java.util.ArrayList;

import cse360project.Article;
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

/**
 * This class is used to edit the groups of an article
 */
public class ArticleGroupEditor {
    private static Stage groupsStage;
    private static BorderPane root;

    private static VBox groupCheckboxContainer;
    private static VBox centerBox;
    private static TextField newGroupTextField;
    
    // The article that is being edited
    private static Article editingArticle;

    /**
     * This method is used to edit the groups of an article
     * It can be called from the edit page and will open a new window
     * @param article the article to edit the groups of
     */
    public static void editArticleGroups(Article article) {
        editingArticle = article;
        extraGroups = new ArrayList<>();

        groupsStage = new Stage();
        groupsStage.setTitle("Edit Article Groups");

        // do not allow user to interact with other windows
        groupsStage.initModality(Modality.APPLICATION_MODAL);

        root = new BorderPane();
        
        Scene groupsScene = new Scene(root, 450, 450);
        groupsStage.setScene(groupsScene);

        groupsStage.show();

        // set the title of the window
        setTitle();

        // create the center content box
        centerBox = new VBox(10);
        centerBox.setAlignment(Pos.TOP_LEFT);
        centerBox.setPadding(new Insets(50));

        // add a scrollpane to the center box
        // this will allow the user to scroll if there are too many groups
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(centerBox);

        root.setCenter(scrollPane);

        // add the group checkboxes
        addGroupCheckboxes();

        // add a close button to the bottom of the window
        addCloseButton();
    }

    /**
     * This method sets the title of the window
     */
    private static void setTitle() {
        Label titleLabel = new Label("Edit Article Groups");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
    }

    /**
     * This method adds the "add group" textfield and button, then renders the group checkboxes
     */
    private static void addGroupCheckboxes() {
        // we need a button to add a new group
        // there will be a textfield to enter the group name
        // and a button to add the group to the right side
        HBox addGroupContainer = new HBox(10);
        addGroupContainer.setAlignment(Pos.CENTER_LEFT);

        // create the textfield to add a new group
        newGroupTextField = new TextField();
        newGroupTextField.setPromptText("Enter New Group Name");
        newGroupTextField.setMaxWidth(200);

        // create the button to add the new group
        Button addGroupButton = new Button("Add New Group");
        addGroupButton.setOnAction(e -> {
            // when the button is clicked, attempt to add the new group
            doNewGroupClicked();
        });

        addGroupContainer.getChildren().addAll(newGroupTextField, addGroupButton);
        centerBox.getChildren().add(addGroupContainer);

        // create a container for the group checkboxes
        groupCheckboxContainer = new VBox(10);
        groupCheckboxContainer.setAlignment(Pos.TOP_LEFT);

        centerBox.getChildren().add(groupCheckboxContainer);

        // render the checkboxes
        renderCheckboxList();
    }

    /**
     * This method adds a close button to the bottom of the window
     */
    private static void addCloseButton() {
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            groupsStage.close();
        });

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
     * This method is called when the "add new group" button is clicked
     * It will attempt to add the new group to the article
     */
    private static void doNewGroupClicked() {
        String newGroupText = newGroupTextField.getText();

        // if the textfield is empty, do nothing
        if (newGroupText.isEmpty()) {
            return;
        }

        // get a list of all existing groups
        ArrayList<String> existingGroups = GroupUtils.consolidateGroups();
        String newGroup = GroupUtils.formatGroupName(newGroupText);

        // if the group already exists, do nothing
        if (existingGroups.contains(newGroup)) {
            return;
        }

        // add the new group to the list of extra groups
        // this will show up in the list of checkboxes, even though it doesn't exist in the database
        extraGroups.add(newGroup);
        editingArticle.addGroup(newGroup);
        newGroupTextField.clear();

        // tell the edit page to update the groups
        sendUpdatePing();

        // render the checkboxes
        renderCheckboxList();

        // focus the textfield
        newGroupTextField.requestFocus();
    }

    /**
     * This method sends a ping to the edit page to update the groups
     */
    private static void sendUpdatePing() {
        EditPage editPage = (EditPage) PageManager.getPageByName("editpage");
        editPage.updateGroups();
    }
    
    // This is a list of extra groups that don't exist in the database but are added by the user
    private static ArrayList<String> extraGroups = new ArrayList<>();

    /**
     * This method renders the list of checkboxes for the groups
     */
    private static void renderCheckboxList() {
        // clear the existing checkboxes
        groupCheckboxContainer.getChildren().clear();

        // make a list of all existing groups and new groups
        ArrayList<String> groups = GroupUtils.consolidateGroups();
        groups.addAll(extraGroups);

        // for each group, create a checkbox
        for (String group : groups) {
            CheckBox groupCheckbox = new CheckBox(group);

            // if the article is in the group, check the checkbox
            groupCheckbox.setSelected(editingArticle.groups.contains(group));

            // when the checkbox is clicked, add or remove the group from the article
            groupCheckbox.setOnAction(e -> {
                if (groupCheckbox.isSelected()) {
                    editingArticle.addGroup(group);
                } else {
                    editingArticle.removeGroup(group);
                }

                // tell the edit page to update the groups
                sendUpdatePing();
            });
            groupCheckboxContainer.getChildren().add(groupCheckbox);
        }
    }
}
