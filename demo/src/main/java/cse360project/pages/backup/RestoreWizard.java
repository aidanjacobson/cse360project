package cse360project.pages.backup;

import java.io.File;

import cse360project.utils.BackupRestoreUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class RestoreWizard {
    static Stage restoreStage;
    static BorderPane root;
    static VBox centerBox;
    static Button restoreButton;

    static RadioButton mergeOverwriteButton;
    static RadioButton mergeKeepButton;
    static RadioButton replaceAllButton;

    static File backupLocation;

    public static void openRestoreWindow() {
        restoreStage = new Stage();
        restoreStage.setTitle("Restore Database");

        // the root should be a borderpane
        root = new BorderPane();

        // create the scene
        Scene restoreScene = new Scene(root, 600, 600);
        restoreStage.setScene(restoreScene);

        // open the window
        restoreStage.show();

        // create the header of the wizard
        setTitle();

        // the content at the center of the borderpane
        centerBox = new VBox(10);
        centerBox.setAlignment(Pos.TOP_LEFT);

        // the center box should have some inset padding
        centerBox.setPadding(new Insets(50));
        root.setCenter(centerBox);

        createFileChooser();

        createRestoreTypeSelection();

        createButtons();
    }

    private static void setTitle() {
        Label titleLabel = new Label("Restore Database");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
    }

    private static void createFileChooser() {
        // the first content item should be a select backup location button
        // next to the button should be a text field that shows the selected backup location
        HBox locationBox = new HBox(10);
        locationBox.setAlignment(Pos.CENTER_LEFT);

        Button selectLocationButton = new Button("Select Backup Location");
        Label locationLabel = new Label("No location selected");

        locationLabel.setWrapText(false);
        locationLabel.setEllipsisString("...");
        locationLabel.setMaxWidth(200);

        selectLocationButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Backup Location");

            // set the file type to .backup
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Backup files (*.backup)", "*.backup");
            fileChooser.getExtensionFilters().add(extFilter);

            File newLocation = fileChooser.showOpenDialog(restoreStage);

            // if the user selects a location, update the location label
            if (newLocation != null) {
                backupLocation = newLocation;
                locationLabel.setText(backupLocation.getName());
                updateRestoreButtonVisibility();
            }
        });

        locationBox.getChildren().addAll(selectLocationButton, locationLabel);
        centerBox.getChildren().add(locationBox);

        // add some margin to the bottom of the location box
        VBox.setMargin(locationBox, new Insets(0, 0, 20, 0));
    }

    private static void createButtons() {
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 20, 20, 20));

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> restoreStage.close());

        restoreButton = new Button("Restore Backup");
        restoreButton.setOnAction(event -> startRestore());

        // initially disable the backup button
        restoreButton.setDisable(true);

        buttonBox.getChildren().addAll(cancelButton, restoreButton);
        root.setBottom(buttonBox);
    }

    private static void createRestoreTypeSelection() {
        // create a radio list with the following options. Each option should have the option name with a description below
        // Merge with existing articles - overwrite duplicates
        //     This option will keep the existing articles, but overwrite any articles that have the same ID.
        //     If two articles have the same ID, the article in the backup will overwrite the existing article.
        // Merge with existing articles - keep originals
        //     This option will keep the existing articles, but will not overwrite any articles that have the same ID.
        //     If two articles have the same ID, the article already in the database will be kept.
        // Replace all articles
        //     This option will delete all existing articles and replace them with the articles in the backup.

        VBox restoreTypeBox = new VBox(10);
        restoreTypeBox.setAlignment(Pos.TOP_LEFT);

        ToggleGroup restoreTypeGroup = new ToggleGroup();

        mergeOverwriteButton = new RadioButton("Merge with existing articles - overwrite duplicates");
        mergeOverwriteButton.setToggleGroup(restoreTypeGroup);
        TextFlow mergeOverwriteDescription = wrapText("This option will keep the existing articles, but overwrite any articles that have the same ID. If two articles have the same ID, the article in the backup will overwrite the existing article.");
        mergeOverwriteDescription.setVisible(false);
        mergeOverwriteDescription.setManaged(false);

        mergeKeepButton = new RadioButton("Merge with existing articles - keep originals");
        mergeKeepButton.setToggleGroup(restoreTypeGroup);
        TextFlow mergeKeepDescription = wrapText("This option will keep the existing articles, but will not overwrite any articles that have the same ID. If two articles have the same ID, the article already in the database will be kept.");
        mergeKeepDescription.setVisible(false);
        mergeKeepDescription.setManaged(false);

        replaceAllButton = new RadioButton("Replace all articles");
        replaceAllButton.setToggleGroup(restoreTypeGroup);
        TextFlow replaceAllDescription = wrapText("This option will delete all existing articles and replace them with the articles in the backup.");
        replaceAllDescription.setVisible(false);
        replaceAllDescription.setManaged(false);

        // show the description when the radio button is selected
        mergeOverwriteButton.setOnAction(event -> {
            mergeOverwriteDescription.setVisible(true);
            mergeOverwriteDescription.setManaged(true);
            mergeKeepDescription.setVisible(false);
            mergeKeepDescription.setManaged(false);
            replaceAllDescription.setVisible(false);
            replaceAllDescription.setManaged(false);

            updateRestoreButtonVisibility();
        });

        mergeKeepButton.setOnAction(event -> {
            mergeOverwriteDescription.setVisible(false);
            mergeOverwriteDescription.setManaged(false);
            mergeKeepDescription.setVisible(true);
            mergeKeepDescription.setManaged(true);
            replaceAllDescription.setVisible(false);
            replaceAllDescription.setManaged(false);

            updateRestoreButtonVisibility();
        });

        replaceAllButton.setOnAction(event -> {
            mergeOverwriteDescription.setVisible(false);
            mergeOverwriteDescription.setManaged(false);
            mergeKeepDescription.setVisible(false);
            mergeKeepDescription.setManaged(false);
            replaceAllDescription.setVisible(true);
            replaceAllDescription.setManaged(true);

            updateRestoreButtonVisibility();
        });

        restoreTypeBox.getChildren().addAll(
            mergeOverwriteButton, mergeOverwriteDescription,
            mergeKeepButton, mergeKeepDescription,
            replaceAllButton, replaceAllDescription
        );

        centerBox.getChildren().add(restoreTypeBox);
        // add some margin to all sides of the restore type box
        VBox.setMargin(restoreTypeBox, new Insets(20));
    }

    private static void updateRestoreButtonVisibility() {
        if (backupLocation != null && (mergeOverwriteButton.isSelected() || mergeKeepButton.isSelected() || replaceAllButton.isSelected())) {
            restoreButton.setDisable(false);
        } else {
            restoreButton.setDisable(true);
        }
    }

    private static TextFlow wrapText(String text) {
        TextFlow textFlow = new TextFlow(new Text(text));
        // textFlow.setMaxWidth(400); // Set the max width to wrap the text
        return textFlow;
    }

    private static void startRestore() {
        boolean successful = false;

        if (mergeOverwriteButton.isSelected()) {
            successful = BackupRestoreUtils.softRestoreDatabase(backupLocation.getAbsolutePath(), true);
        } else if (mergeKeepButton.isSelected()) {
            successful = BackupRestoreUtils.softRestoreDatabase(backupLocation.getAbsolutePath(), false);
        } else if (replaceAllButton.isSelected()) {
            successful = BackupRestoreUtils.hardRestoreDatabase(backupLocation.getAbsolutePath());
        }

        if (successful) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Restore successful", ButtonType.OK);
                successAlert.showAndWait();
                restoreStage.close();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while restoring the database", ButtonType.OK);
                errorAlert.showAndWait();
            }

    }
}
