package cse360project.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Label;
import java.sql.SQLException;
import java.util.ArrayList;

import cse360project.Message;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.MessengerUtils;
import cse360project.utils.PageManager;

public class StudentMessagePage implements Page {
	BorderPane root = new BorderPane();
	VBox messageContent;
	
    /**
     * constructor for StudentMessagePage
     */
	public StudentMessagePage() throws SQLException {
        messageContent = new VBox();

		// messages should be aligned to the bottom of the container
        messageContent.setAlignment(Pos.BOTTOM_LEFT);

		// add a scroll pane to the message content
		// it should be scrolled to the bottom by default
        ScrollPane scrollPane = new ScrollPane(messageContent);
        scrollPane.setVvalue(1.0);

        
		Button backButton = new Button("Back");

		// when the back button is clicked, switch to the student page
		backButton.setOnAction(e -> {
			PageManager.switchToPage("student"); // Switch to login page
		});

		scrollPane.setMaxWidth(700);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		
		root.setTop(backButton);
		BorderPane.setMargin(scrollPane, new Insets(100, 100, 100, 100));
		root.setCenter(scrollPane);
		
		Button createMessage = new Button("New Message");
		setLinkButtonStyles(createMessage);
		createMessage.setOnAction(e-> {
			PageManager.switchToPage("createmessage");
		});
		root.setBottom(createMessage);
	}
	
	
	public BorderPane getRoot() {
        return root;
    }


	@Override
	public void onPageOpen() {
		messageContent.getChildren().clear();
		ArrayList<Message> messages = MessengerUtils.getAllMessagesInUserThread(ApplicationStateManager.getLoggedInUser());
		
		
        for (int i = 0; i < messages.size(); i++) {
			VBox message = createMessage(messages.get(i));
        	messageContent.getChildren().add(message);

			if (messages.get(i).getSender().getUserName().equals(ApplicationStateManager.getLoggedInUser().getUserName())) {
				message.setAlignment(Pos.CENTER_RIGHT);
			} else {
				message.setAlignment(Pos.CENTER_LEFT);
			}
        }
        
		
	}
    /**
     * Creates a vbox 
     * @return a VBox containing the Message
     */
	private VBox createMessage(Message messageObject) {
		VBox message = new VBox();
		Label text = new Label(messageObject.getMessageContent());


		String time = (messageObject.getMessageTimestamp().toString());

		TextFlow bodyContainer = new TextFlow(text);
        bodyContainer.setPadding(new Insets(5, 0, 0, 30));

        // Make the body container scrollable
        ScrollPane bodyScrollPane = new ScrollPane(bodyContainer);
        bodyScrollPane.setFitToWidth(false);
        bodyScrollPane.setPrefHeight(600);
        bodyScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);

		Label Sender = new Label(messageObject.getSender().getPreferredName() + "(" + messageObject.getSenderRole().toString() + ") " + time);
		message.getChildren().add(Sender);
		message.getChildren().add(bodyScrollPane);

		message.setPadding(new Insets(10));

		return message;
	}
	
    private void setLinkButtonStyles(Button linkButton) {
        final String btnBackgroundColor = "#0088ff";
        final Color textColor = Color.WHITE;
        final int btnWidth = 150;

        linkButton.setStyle("-fx-background-color: " + btnBackgroundColor + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        linkButton.setTextFill(textColor);
        linkButton.setPrefWidth(btnWidth);
    }
}


