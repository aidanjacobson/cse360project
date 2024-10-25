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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * A wizard to restore the database from a backup file
 */
public class RestoreWizard {
    // global variables for the restore wizard
    private static Stage restoreStage;
    private static BorderPane root;
    private static VBox centerBox;
    private static Button restoreButton;

    private static RadioButton mergeOverwriteButton;
    private static RadioButton mergeKeepButton;
    private static RadioButton replaceAllButton;

    private static File backupLocation;

    /**
     * Call this method from any location to open the restore wizard
     */
    public static void openRestoreWindow() {
        restoreStage = new Stage();
        restoreStage.setTitle("Restore Database");

        // Set the modality to block interaction with other windows
        restoreStage.initModality(Modality.APPLICATION_MODAL);

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

        // create the content of the wizard
        // this includes the file chooser, the restore type selection, and the buttons
        createFileChooser();
        createRestoreTypeSelection();
        createButtons();
    }

    /**
     * Set the title of the restore wizard
     */
    private static void setTitle() {
        Label titleLabel = new Label("Restore Database");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
    }

    /**
     * Create the file chooser for the user to select the backup file
     */
    private static void createFileChooser() {
        // the first content item should be a select backup location button
        // next to the button should be a text field that shows the selected backup location
        HBox locationBox = new HBox(10);
        locationBox.setAlignment(Pos.CENTER_LEFT);

        // create the select location button and the location label
        Button selectLocationButton = new Button("Select Backup Location");
        Label locationLabel = new Label("No location selected");

        locationLabel.setWrapText(false);
        locationLabel.setEllipsisString("...");
        locationLabel.setMaxWidth(200);

        // when the user clicks the select location button, open a file chooser
        selectLocationButton.setOnAction(event -> {
            // create a file chooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Backup Location");

            // set the file type to .backup
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Backup files (*.backup)", "*.backup");
            fileChooser.getExtensionFilters().add(extFilter);

            // show the file chooser and get the selected file
            File newLocation = fileChooser.showOpenDialog(restoreStage);

            // if the user selects a location, update the location label
            if (newLocation != null) {
                backupLocation = newLocation;
                locationLabel.setText(backupLocation.getName());

                // update the restore button visibility
                updateRestoreButtonVisibility();
            }
        });

        locationBox.getChildren().addAll(selectLocationButton, locationLabel);
        centerBox.getChildren().add(locationBox);

        // add some margin to the bottom of the location box
        VBox.setMargin(locationBox, new Insets(0, 0, 20, 0));
    }

    /**
     * Create the buttons at the bottom of the wizard
     */
    private static void createButtons() {
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 20, 20, 20));

        // create the cancel button
        // when the user clicks the cancel button, close the restore wizard
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> restoreStage.close());

        // create the restore button
        // when the user clicks the restore button, start the restore process
        restoreButton = new Button("Restore Backup");
        restoreButton.setOnAction(event -> startRestore());

        // initially disable the backup button
        restoreButton.setDisable(true);

        // add the buttons to the button box
        // the cancel button should be on the left, the restore button should be on the right
        buttonBox.getChildren().addAll(cancelButton, restoreButton);
        root.setBottom(buttonBox);
    }

    /**
     * Create the restore type selection radio buttons
     */
    private static void createRestoreTypeSelection() {
        // create a radio list with the following options. Each option should have the option name with a description below
        
        VBox restoreTypeBox = new VBox(10);
        restoreTypeBox.setAlignment(Pos.TOP_LEFT);
        
        ToggleGroup restoreTypeGroup = new ToggleGroup();
        
        // Merge with existing articles - overwrite duplicates
        //     This option will keep the existing articles, but overwrite any articles that have the same ID.
        //     If two articles have the same ID, the article in the backup will overwrite the existing article.
        mergeOverwriteButton = new RadioButton("Merge with existing articles - overwrite duplicates");
        mergeOverwriteButton.setToggleGroup(restoreTypeGroup);
        TextFlow mergeOverwriteDescription = wrapText("This option will keep the existing articles, but overwrite any articles that have the same ID. If two articles have the same ID, the article in the backup will overwrite the existing article.");
        mergeOverwriteDescription.setVisible(false);
        mergeOverwriteDescription.setManaged(false);
        
        // Merge with existing articles - keep originals
        //     This option will keep the existing articles, but will not overwrite any articles that have the same ID.
        //     If two articles have the same ID, the article already in the database will be kept.
        mergeKeepButton = new RadioButton("Merge with existing articles - keep originals");
        mergeKeepButton.setToggleGroup(restoreTypeGroup);
        TextFlow mergeKeepDescription = wrapText("This option will keep the existing articles, but will not overwrite any articles that have the same ID. If two articles have the same ID, the article already in the database will be kept.");
        mergeKeepDescription.setVisible(false);
        mergeKeepDescription.setManaged(false);
        
        // Replace all articles
        //     This option will delete all existing articles and replace them with the articles in the backup.
        replaceAllButton = new RadioButton("Replace all articles");
        replaceAllButton.setToggleGroup(restoreTypeGroup);
        TextFlow replaceAllDescription = wrapText("This option will delete all existing articles and replace them with the articles in the backup.");
        replaceAllDescription.setVisible(false);
        replaceAllDescription.setManaged(false);
        
        // show the description when the radio button is selected
        // hide all other descriptions
        // this handles the merge overwrite button
        mergeOverwriteButton.setOnAction(event -> {
            mergeOverwriteDescription.setVisible(true);
            mergeOverwriteDescription.setManaged(true);
            mergeKeepDescription.setVisible(false);
            mergeKeepDescription.setManaged(false);
            replaceAllDescription.setVisible(false);
            replaceAllDescription.setManaged(false);

            updateRestoreButtonVisibility();
        });

        // this handles the merge keep button
        mergeKeepButton.setOnAction(event -> {
            mergeOverwriteDescription.setVisible(false);
            mergeOverwriteDescription.setManaged(false);
            mergeKeepDescription.setVisible(true);
            mergeKeepDescription.setManaged(true);
            replaceAllDescription.setVisible(false);
            replaceAllDescription.setManaged(false);

            updateRestoreButtonVisibility();
        });

        // this handles the replace all button
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

    /**
     * Update the visibility of the restore button
     * The restore button should only be enabled if a backup location is selected and a restore type is selected
     */
    private static void updateRestoreButtonVisibility() {
        if (backupLocation != null && (mergeOverwriteButton.isSelected() || mergeKeepButton.isSelected() || replaceAllButton.isSelected())) {
            restoreButton.setDisable(false);
        } else {
            restoreButton.setDisable(true);
        }
    }

    /**
     * Wrap text in a TextFlow object
     * @param text The text to wrap
     * @return The wrapped text
     */
    private static TextFlow wrapText(String text) {
        TextFlow textFlow = new TextFlow(new Text(text));
        return textFlow;
    }

    /**
     * Start the restore process
     * This method will call the appropriate restore method based on the selected restore type and backup location
     */
    private static void startRestore() {
        boolean successful = false;

        if (mergeOverwriteButton.isSelected()) {
            // call the soft restore method with the backup location
            // the second parameter should be true to overwrite duplicates
            successful = BackupRestoreUtils.softRestoreDatabase(backupLocation.getAbsolutePath(), true);
        } else if (mergeKeepButton.isSelected()) {
            // call the soft restore method with the backup location
            // the second parameter should be false to keep the original articles
            successful = BackupRestoreUtils.softRestoreDatabase(backupLocation.getAbsolutePath(), false);
        } else if (replaceAllButton.isSelected()) {
            // call the hard restore method with the backup location
            successful = BackupRestoreUtils.hardRestoreDatabase(backupLocation.getAbsolutePath());
        }

        // show an alert based on the success of the restore
        // if the restore was successful, close the restore wizard
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
