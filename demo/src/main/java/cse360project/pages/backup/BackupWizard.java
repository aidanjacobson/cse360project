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
import javafx.stage.Modality;
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
    // create the necessary fields for the backup wizard
    // these fields should will be accessed by the other methods in the class
    static Stage backupStage;
    static VBox centerBox;
    static BorderPane root;

    // where the group checkboxes will be stored
    static VBox groupCheckboxContainer;

    // create the search bar
    static TextField searchBar;

    // create the backup button
    static Button backupButton;

    // create the radio buttons for the group selection options
    static RadioButton allGroupsOption;
    static RadioButton specificGroupsOption;

    // create the backup location field
    static File backupLocation;
    
    /**
     * Call this method from any location to open the backup window
     */
    public static void openBackupWindow() {
        backupStage = new Stage();
        backupStage.setTitle("Backup Database");

        // Set the modality to block interaction with other windows
        backupStage.initModality(Modality.APPLICATION_MODAL);
        
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

        // create the select location button and the location label
        Button selectLocationButton = new Button("Select Backup Location");
        Label locationLabel = new Label("No location selected");

        locationLabel.setWrapText(false);
        locationLabel.setEllipsisString("...");
        locationLabel.setMaxWidth(200);

        // when the select location button is clicked, open a file chooser dialog
        selectLocationButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Backup Location");

            // set the file type to .backup
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Backup files (*.backup)", "*.backup");
            fileChooser.getExtensionFilters().add(extFilter);

            // show the file chooser dialog
            File newLocation = fileChooser.showSaveDialog(backupStage);

            // if the user selects a location, update the location label
            if (newLocation != null && atLeastOneGroupSelected()) {
                backupLocation = newLocation;
                locationLabel.setText(backupLocation.getName());

                // either enable or disable the backup button
                updateBackupButtonVisibility();
            }
        });

        locationBox.getChildren().addAll(selectLocationButton, locationLabel);
        centerBox.getChildren().add(locationBox);

        // add some margin to the bottom of the location box
        VBox.setMargin(locationBox, new Insets(0, 0, 20, 0));

        // create the group selection options
        createGroupSelectionOptions();

        // create the buttons at the bottom of the borderpane
        createButtons();
    }

    /**
     * Update the visibility of the backup button based on whether a backup location has been selected and whether at least one group has been selected
     */
    private static void updateBackupButtonVisibility() {
        backupButton.setDisable(backupLocation == null || !atLeastOneGroupSelected());
    }

    /**
     * Check if at least one group has been selected
     * @return True if at least one group has been selected, false otherwise
     */
    private static boolean atLeastOneGroupSelected() {
        // if the "Backup All Groups" option is selected, then at least one group has been selected
        if (allGroupsOption.isSelected()) {
            return true;
        }

        // otherwise, check if at least one group checkbox is selected
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

    /**
     * Method to set the title of the backup wizard
     */
    static void setTitle() {
        // the top of the borderpane should be a title that says "Backup Database"
        Label titleLabel = new Label("Backup Database");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
    }

    /**
     * Method to create the group selection radio options, as well as the group checkboxes
     */
    static void createGroupSelectionOptions() {
        ToggleGroup backupOptions = new ToggleGroup();

        allGroupsOption = new RadioButton("Backup All Groups");
        allGroupsOption.setToggleGroup(backupOptions);
        allGroupsOption.setSelected(true);

        specificGroupsOption = new RadioButton("Backup Specific Groups");
        specificGroupsOption.setToggleGroup(backupOptions);

        centerBox.getChildren().addAll(allGroupsOption, specificGroupsOption);

        // when a radio button is selected, call the groupSelectionUpdated method to update the visibility of the group checkboxes
        // and update the visibility of the backup button
        backupOptions.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            groupSelectionUpdated(newValue);
            updateBackupButtonVisibility();
        });

        // create the group checkboxes
        createGroupCheckboxContent();
    }

    /**
     * Method to create the group checkboxes, one for each group
     * This will also create a search bar to filter the groups
     */
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

        // get a list of all groups (no duplicates) and make a checkbox for each group
        // TODO: replace getAllArticleGroups() with the actual method to get all article groups pending Div and Dan's changes
        ArrayList<String> groups = BackupRestoreUtils.getAllArticleGroups();
        for (String group : groups) {
            CheckBox groupCheckbox = new CheckBox(group);
            groupCheckboxContainer.getChildren().add(groupCheckbox);

            // when a checkbox is clicked, update the visibility of the checkboxes and the backup button
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

    /**
     * Method to render the visibility of the group checkboxes based on the search bar input
     * A checkbox should always be visible if it is selected or if the search bar input is contained in the checkbox text
     */
    static void renderCheckboxVisibility() {
        for (javafx.scene.Node node : groupCheckboxContainer.getChildren()) {
            if (node instanceof CheckBox) {
                // get the checkbox and the text value of the search bar
                CheckBox checkBox = (CheckBox) node;
                String textValue = searchBar.getText();

                // check if the checkbox text contains the search bar input
                boolean nameMatches = checkBox.getText().toLowerCase().contains(textValue.toLowerCase());

                // the checkbox should be visible if it is selected or if the search bar input is contained in the checkbox text
                boolean shouldShow = checkBox.isSelected() || nameMatches;

                // set the visibility and managed properties of the checkbox
                // this will remove the checkbox from the layout flow if it should not be shown
                checkBox.setVisible(shouldShow);
                checkBox.setManaged(shouldShow);
            }
        }
    }

    /**
     * Method to get the selected groups from the group checkboxes
     * @return An ArrayList<String> of the selected groups
     */
    static ArrayList<String> getSelectedGroups() {
        // go through each checkbox and add the text of the selected checkboxes to the selectedGroups list
        ArrayList<String> selectedGroups = new ArrayList<>();
        for (javafx.scene.Node node : groupCheckboxContainer.getChildren()) {
            if (node instanceof CheckBox) { // hopefully it is lol
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    selectedGroups.add(checkBox.getText());
                }
            }
        }
        return selectedGroups;
    }

    /**
     * Method to update the visibility of the group checkboxes based on the selected radio button
     * This will only show the group checkboxes if the "Backup Specific Groups" option is selected
     * @param newValue
     */
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

    /**
     * Method to create the buttons at the bottom of the borderpane
     */
    static void createButtons() {
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 20, 20, 20));
        
        // the bottom of the borderpane should have a cancel button
        Button cancelButton = new Button("Cancel");
        // when the cancel button is clicked, close the window
        cancelButton.setOnAction(event -> backupStage.close());
        
        // the bottom of the borderpane should have a backup button
        backupButton = new Button("Create Backup");
        // when the backup button is clicked, call the startBackup method
        backupButton.setOnAction(event -> startBackup());
        
        // initially disable the backup button
        backupButton.setDisable(true);
        
        // the cancel button should be on the left and the backup button should be on the right, horizontally centered
        buttonBox.getChildren().addAll(cancelButton, backupButton);
        root.setBottom(buttonBox);
    }

    /**
     * Method to actually start the backup process
     * Called when the user clicks the backup button
     * Meaning the user has selected a backup location and at least one group to backup
     */
    static void startBackup() {
        // if the user has selected a backup location, start the backup process
        // otherwise, show an error message
        if (backupLocation != null) {
            // get the selected groups
            // this will be null if the "Backup All Groups" option is selected, but that's okay
            ArrayList<String> selectedGroups = getSelectedGroups();
            
            boolean successful = false;
            if (allGroupsOption.isSelected()) {
                // if the "Backup All Groups" option is selected, backup all groups by not passing any groups
                successful = BackupRestoreUtils.backupDatabase(backupLocation.getAbsolutePath());
            } else {
                // otherwise, backup the selected groups
                successful = BackupRestoreUtils.backupDatabase(backupLocation.getAbsolutePath(), selectedGroups);
            }

            // show a success or error message based on the success of the backup
            // close the window if the backup was successful
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
