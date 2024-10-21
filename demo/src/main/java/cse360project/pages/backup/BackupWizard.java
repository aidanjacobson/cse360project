package cse360project.pages.backup;

import java.io.File;
import java.util.ArrayList;

import cse360project.utils.BackupRestoreUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;

/**
 * A class that handles the backup wizard window
 */
public class BackupWizard {
    static Stage backupStage;
    static VBox centerBox;
    static BorderPane root;
    static VBox groupCheckboxContainer;
    static TextField searchBar;
    static Button backupButton;

    static RadioButton allGroupsOption;
    static RadioButton specificGroupsOption;

    static File backupLocation;
    
    public static void openBackupWindow() {
        backupStage = new Stage();
        backupStage.setTitle("Backup Database");
        
        // the root should be a borderpane
        root = new BorderPane();
        
        // create the scene
        Scene backupScene = new Scene(root, 450, 450);
        backupStage.setScene(backupScene);

        // open the window
        backupStage.show();

        // create the header of the wizard
        setTitle();

        // the center of the borderpane should be a radio list that allows the user to select whether they want to backup all groups or specific groups
        centerBox = new VBox(10);
        centerBox.setAlignment(Pos.TOP_LEFT);

        // the center box should have some inset padding
        centerBox.setPadding(new Insets(50));
        root.setCenter(centerBox);

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

            File newLocation = fileChooser.showSaveDialog(backupStage);

            // if the user selects a location, update the location label
            if (newLocation != null && atLeastOneGroupSelected()) {
                backupLocation = newLocation;
                locationLabel.setText(backupLocation.getName());
                updateBackupButtonVisibility();
            }
        });

        locationBox.getChildren().addAll(selectLocationButton, locationLabel);
        centerBox.getChildren().add(locationBox);

        // add some margin to the bottom of the location box
        VBox.setMargin(locationBox, new Insets(0, 0, 20, 0));

        createGroupSelectionOptions();
        createButtons();
    }

    private static void updateBackupButtonVisibility() {
        backupButton.setDisable(backupLocation == null || !atLeastOneGroupSelected());
    }

    private static boolean atLeastOneGroupSelected() {
        if (allGroupsOption.isSelected()) {
            return true;
        }
        for (javafx.scene.Node node : groupCheckboxContainer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    return true;
                }
            }
        }
        return false;
    }

    static void setTitle() {
        // the top of the borderpane should be a title that says "Backup Database"
        Label titleLabel = new Label("Backup Database");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
    }

    static void createGroupSelectionOptions() {
        ToggleGroup backupOptions = new ToggleGroup();

        allGroupsOption = new RadioButton("Backup All Groups");
        allGroupsOption.setToggleGroup(backupOptions);
        allGroupsOption.setSelected(true);

        specificGroupsOption = new RadioButton("Backup Specific Groups");
        specificGroupsOption.setToggleGroup(backupOptions);

        centerBox.getChildren().addAll(allGroupsOption, specificGroupsOption);

        // when a radio button is selected, call the groupSelectionUpdated method
        backupOptions.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            groupSelectionUpdated(newValue);
            updateBackupButtonVisibility();
        });

        createGroupCheckboxContent();
    }

    static void createGroupCheckboxContent() {
        // create a section underneath the backup radio selection
        // this section should contain checkboxes for each group
        // as well as a search bar to filter the groups
        groupCheckboxContainer = new VBox(10);
        groupCheckboxContainer.setPadding(new Insets(20, 0, 0, 20));
        groupCheckboxContainer.setAlignment(Pos.TOP_LEFT);

        // Add a search bar
        searchBar = new TextField();
        searchBar.setPromptText("Search groups...");
        groupCheckboxContainer.getChildren().add(searchBar);

        // groupCheckboxContainer.getChildren().addAll(group1, group2, group3);

        ArrayList<String> groups = BackupRestoreUtils.getAllArticleGroups();
        for (String group : groups) {
            CheckBox groupCheckbox = new CheckBox(group);
            groupCheckboxContainer.getChildren().add(groupCheckbox);

            // when a checkbox is clicked, update the visibility of the checkboxes
            groupCheckbox.setOnAction((event) -> {
                renderCheckboxVisibility();
                updateBackupButtonVisibility();
            });
        }

        // Add the groupCheckboxContainer to the centerBox
        centerBox.getChildren().add(groupCheckboxContainer);

        // Initially hide the groupCheckboxContainer
        groupCheckboxContainer.setVisible(false);
        

        // Filter checkboxes based on search bar input
        searchBar.textProperty().addListener((event) -> {
            renderCheckboxVisibility();
        });
    }

    static void renderCheckboxVisibility() {
        for (javafx.scene.Node node : groupCheckboxContainer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                String textValue = searchBar.getText();
                boolean nameMatches = checkBox.getText().toLowerCase().contains(textValue.toLowerCase());
                boolean shouldShow = checkBox.isSelected() || nameMatches;
                checkBox.setVisible(shouldShow);
                checkBox.setManaged(shouldShow);
            }
        }
    }

    static ArrayList<String> getSelectedGroups() {
        ArrayList<String> selectedGroups = new ArrayList<>();
        for (javafx.scene.Node node : groupCheckboxContainer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    selectedGroups.add(checkBox.getText());
                }
            }
        }
        return selectedGroups;
    }

    static void groupSelectionUpdated(Object newValue) {
        // if the user selects the "Backup All Groups" option, hide the group checkbox container
        // if the user selects the "Backup Specific Groups" option, show the group checkbox container
        if (((RadioButton) newValue).getText().equals("Backup All Groups")) {
            groupCheckboxContainer.setVisible(false);
        } else {
            groupCheckboxContainer.setVisible(true);
            renderCheckboxVisibility();
        }
    }

    static void createButtons() {
        // the bottom of the borderpane should have a cancel button
        // when the cancel button is clicked, close the window
        // the bottom of the borderpane should have a backup button
        // when the backup button is clicked, call the startBackup method
        // the cancel button should be on the left and the backup button should be on the right, horizontally centered
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 20, 20, 20));

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> backupStage.close());

        backupButton = new Button("Create Backup");
        backupButton.setOnAction(event -> startBackup());

        // initially disable the backup button
        backupButton.setDisable(true);

        buttonBox.getChildren().addAll(cancelButton, backupButton);
        root.setBottom(buttonBox);
    }

    static void startBackup() {
        // if the user has selected a backup location, start the backup process
        // otherwise, show an error message
        if (backupLocation != null) {
            ArrayList<String> selectedGroups = getSelectedGroups();
            
            boolean successful = false;
            if (allGroupsOption.isSelected()) {
                successful = BackupRestoreUtils.backupDatabase(backupLocation.getAbsolutePath());
            } else {
                successful = BackupRestoreUtils.backupDatabase(backupLocation.getAbsolutePath(), selectedGroups);
            }

            if (successful) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Backup successful", ButtonType.OK);
                successAlert.showAndWait();
                backupStage.close();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while backing up the database", ButtonType.OK);
                errorAlert.showAndWait();
            }
        } else {
            // show an error message
            System.out.println("Please select a backup location");
        }
    }
}
