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
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class CreateMessage implements Page{
	VBox messageContainer;
	TextArea messageBody;
	ComboBox<String> typeComboBox;
	Message question;
	private BorderPane root = new BorderPane();
	
    /**
     * This method is used to create the interface for the page
     */
	private void createInterface() {
        // lets add a vbox for the editor content
        messageContainer = new VBox(10);
        BorderPane.setMargin(messageContainer, new Insets(20));
        root.setCenter(messageContainer);

        Text prompt = new Text("Does your message suggest information for a new Article?");
        prompt.setStyle("-fx-font-size: 20px;");
        root.setTop(prompt);
        
     // create the level selection
        createTypeSelection();
        
        // create the title text
        createMessageBody();
        
        // create the bottom buttons
        createBottomButtons();
    }
	
    /**
     * This method is used to create the message body
     */
    private void createMessageBody() {
        // we need a textbox for the message
        messageBody = new TextArea();
        
        messageBody.setPrefWidth(800);
        messageBody.setPromptText("Enter Message");
        messageBody.setStyle("-fx-font-size: 20px;");
        messageContainer.getChildren().add(messageBody);
        messageBody.setWrapText(true);
    }
    
    /**
     * This method is used to create the type selection
     */
    private void createTypeSelection() {
        // we need a dropdown selection for the type
        // font size 20px
        typeComboBox = new ComboBox<String>();
        typeComboBox.getItems().addAll("Yes", "No");
        typeComboBox.setValue("No");
        typeComboBox.setStyle("-fx-font-size: 20px;");
        messageContainer.getChildren().add(typeComboBox);
    }
    
    /**
     * This method is used to create the bottom buttons
     */
    private void createBottomButtons() {
        // create the send and close button
        Button saveButton = new Button("Send Message");
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
    
    /**
     * This method is used to set the styles for the link buttons
     * @param linkButton the button to set the styles for
     */
    private void setLinkButtonStyles(Button linkButton) {
        final String btnBackgroundColor = "#0088ff";
        final Color textColor = Color.WHITE;
        final int btnWidth = 150;

        linkButton.setStyle("-fx-background-color: " + btnBackgroundColor + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        linkButton.setTextFill(textColor);
        linkButton.setPrefWidth(btnWidth);
    }
    
    /**
     * This method is used to attempt to save the message
     */
    private void doAttemptSave() {
        // check if all fields are filled
        // only some fields are required
        // messageBody
        if (messageBody.getText().isEmpty()) {
            failSave("Please enter a message");
            return;
        }
        MessageType type;
        if (typeComboBox.getValue() == "Yes") {
        	type = MessageType.SPECIFIC;
        }
        else {
        	type = MessageType.GENERIC; 
        }
        question = new Message(-1, type, messageBody.getText(), ApplicationStateManager.getLoggedInUser(), Role.STUDENT, ApplicationStateManager.getLoggedInUser(), Timestamp.valueOf(LocalDateTime.now()));

        // save the Message
        // it might be new or existing
        DatabaseHelper.addOrUpdateMessage(question);
        
        // go back to the studentMessage page
        PageManager.switchToPage("studentmessage");
    }
    
    /**
     * This method is used to show a failure message
     * @param message the message to show
     */
    private void failSave(String message) {
        Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
    
    /**
     * This method is used to attempt to cancel the message
     */
    private void doAttemptCancel() {
        // confirm cancel
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to cancel? Any unsaved changes will be lost.", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
            	PageManager.switchToPage("studentmessage");
            }
        });
    }

    /**
     * This method is used to get the root of the page
     */
	@Override
	public BorderPane getRoot() {
		return root;
	}

    /**
     * This method is used to set the title of the page
     */
	@Override
	public void onPageOpen() {
		// create the interface
        createInterface();
		
	}

}
