package cse360project.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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

	}
	
	
	public BorderPane getRoot() {
        return root;
    }


	@Override
	public void onPageOpen() {
		ArrayList<Message> messages = MessengerUtils.getAllMessagesInUserThread(ApplicationStateManager.getLoggedInUser());

		// @Liam TODO: Sort messages by timestamp

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

	private VBox createMessage(Message messageObject) {
		VBox message = new VBox();
		Label text = new Label(messageObject.getMessageContent());

		// @Liam TODO: Add timestamp to message
		// @Liam TODO: Wrap message text in textflow

		// @Liam TODO: Sender label should be in this format: "[prefname] (role)" e.g. "Johnny (Student)" or "Janie (Instructor)"

		Label Sender = new Label(messageObject.getSender().getUserName() + ":");
		message.getChildren().add(Sender);
		message.getChildren().add(text);

		message.setPadding(new Insets(10));

		return message;
	}
}


