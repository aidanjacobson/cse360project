package cse360project.pages;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import cse360project.Message;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.MessageType;
import cse360project.utils.PageManager;
import cse360project.utils.Role;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CreateMessage implements Page{
	VBox messageContainer;
	TextField messageBody;
	ComboBox<String> typeComboBox;
	Message question;
	private BorderPane root = new BorderPane();
	
	private void createInterface() {
        // lets add a vbox for the editor content
        messageContainer = new VBox(10);
        BorderPane.setMargin(messageContainer, new Insets(20));
        root.setCenter(messageContainer);

     // create the level selection
        createTypeSelection();
        
        // create the title text
        createMessageBody();
        
        // create the bottom buttons
        createBottomButtons();
    }
	
    private void createMessageBody() {
        // we need a textbox for the message
        messageBody = new TextField();
        messageBody.setMaxWidth(500);
        messageBody.setPromptText("Enter Your Message");
        messageBody.setStyle("-fx-font-size: 20px;");
        messageContainer.getChildren().add(messageBody);
    }
    
    private void createTypeSelection() {
        // we need a dropdown selection for the level
        // font size 20px
        typeComboBox = new ComboBox<String>();
        typeComboBox.getItems().addAll("GENERIC", "SPECIFIC");
        typeComboBox.setValue("GENERIC");
        typeComboBox.setStyle("-fx-font-size: 20px;");
        messageContainer.getChildren().add(typeComboBox);
    }
    
    private void createBottomButtons() {
        // create the save and close button
        Button saveButton = new Button("Save and Close");
        saveButton.setStyle("-fx-font-size: 15px;");
        saveButton.setOnAction(event -> {
            doAttemptSave();
        });
        setLinkButtonStyles(saveButton);

        // create the cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 15px;");
        cancelButton.setOnAction(event -> {
            doAttemptCancel();
        });
        setLinkButtonStyles(cancelButton);

        // put in an HBox
        HBox bottomButtonContainer = new HBox(10, saveButton, cancelButton);
        bottomButtonContainer.setAlignment(Pos.CENTER_LEFT);
        bottomButtonContainer.setPadding(new Insets(0, 0, 20, 20));
        root.setBottom(bottomButtonContainer);
    }
    
    private void setLinkButtonStyles(Button linkButton) {
        final String btnBackgroundColor = "#0088ff";
        final Color textColor = Color.WHITE;
        final int btnWidth = 150;

        linkButton.setStyle("-fx-background-color: " + btnBackgroundColor + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        linkButton.setTextFill(textColor);
        linkButton.setPrefWidth(btnWidth);
    }
    
    private void doAttemptSave() {
        // check if all fields are filled
        // only some fields are required
        // title
        if (messageBody.getText().isEmpty()) {
            failSave("Please enter a message");
            return;
        }

        question = new Message(-1, MessageType.stringToMessageType(typeComboBox.getValue()), messageBody.getText(), ApplicationStateManager.getLoggedInUser(), Role.STUDENT, ApplicationStateManager.getLoggedInUser(), Timestamp.valueOf(LocalDateTime.now()));

        // save the article
        // it might be new or existing
        DatabaseHelper.addOrUpdateMessage(question);
        
        // go back to the view page
        PageManager.switchToPage("studentmessage");
    }
    
    private void failSave(String message) {
        Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
    
    private void doAttemptCancel() {
        // confirm cancel
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to cancel? Any unsaved changes will be lost.", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
            	PageManager.switchToPage("studentmessage");
            }
        });
    }
	@Override
	public BorderPane getRoot() {
		// TODO Auto-generated method stub
		return root;
	}

	@Override
	public void onPageOpen() {
		// create the interface
        createInterface();
		
	}

}
