package cse360project.pages.usergroups;

import java.util.ArrayList;

import cse360project.User;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.GroupUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserGroupEditWizard {
    private static Stage userGroupEditStage;
    private static BorderPane root;

    private static VBox groupCheckboxContainer;
    private static VBox centerBox;

    // The user who's groups are being edited
    private static User editingUser;

    /**
     * This method is used to edit the groups of a user
     * It can be called from the user group list page and will open a new window
     * @param user the user to edit the groups of
     */
    public static void editUserGroups(User user) {
        editingUser = user;

        userGroupEditStage = new Stage();
        userGroupEditStage.setTitle("Edit User Groups");

        // do not allow user to interact with other windows
        userGroupEditStage.initModality(Modality.APPLICATION_MODAL);

        root = new BorderPane();

        Scene userGroupEditScene = new Scene(root, 450, 450);
        userGroupEditStage.setScene(userGroupEditScene);

        userGroupEditStage.show();

        // set the title of the window
        setTitle();

        // create the center box
        centerBox = new VBox(10);
        centerBox.setAlignment(Pos.TOP_LEFT);
        centerBox.setPadding(new Insets(50));

        // add a scrollpane to the center box
        // this will allow the user to scroll if there are a lot of groups
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(centerBox);

        root.setCenter(scrollPane);

        // add the group checkboxes
        addGroupCheckboxes();

        // add a close button to the bottom of the window
        addCloseButton();
    }

    private static void setTitle() {
        Label titleLabel = new Label("Edit Groups for " + editingUser.getPreferredName());
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
    }

    private static void addGroupCheckboxes() {
        // create a container for the group checkboxes
        groupCheckboxContainer = new VBox(10);
        groupCheckboxContainer.setAlignment(Pos.TOP_LEFT);

        // add the group checkboxes to the center box
        centerBox.getChildren().add(groupCheckboxContainer);

        // render the group checkboxes
        renderCheckboxList();
    }

    private static void renderCheckboxList() {
        // clear the group checkbox container
        groupCheckboxContainer.getChildren().clear();

        User loggedInUser = ApplicationStateManager.getLoggedInUser();

        // get a list of all groups that the logged in user can edit
        ArrayList<String> groups = GroupUtils.getAllGroupsThatCanBeEditedByUser(loggedInUser);

        // create a checkbox for each group
        for (String group : groups) {
            CheckBox groupCheckBox = new CheckBox(group);

            // check the box if the user is already in the group
            groupCheckBox.setSelected(editingUser.hasGroup(group));

            // when the checkbox is clicked, update the user's groups, and update the database
            groupCheckBox.setOnAction(e -> {
                if (groupCheckBox.isSelected()) {
                    editingUser.addGroup(group);
                } else {
                    editingUser.removeGroup(group);
                }

                // update the user's groups in the database
                DatabaseHelper.updateUser(editingUser);
            });

            groupCheckboxContainer.getChildren().add(groupCheckBox);
        }
    }

    private static void addCloseButton() {
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            userGroupEditStage.close();
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
}
