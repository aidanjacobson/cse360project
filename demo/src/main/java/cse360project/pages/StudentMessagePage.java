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
import java.sql.Timestamp;
import java.util.ArrayList;

import cse360project.Message;
import cse360project.User;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.MessageType;
import cse360project.utils.MessengerUtils;
import cse360project.utils.PageManager;
import cse360project.utils.Role;

public class StudentMessagePage implements Page {
	BorderPane root = new BorderPane();
	
	public StudentMessagePage() throws SQLException {
        VBox messageContent = new VBox();
        messageContent.setAlignment(Pos.BOTTOM_LEFT);
        ScrollPane scrollPane = new ScrollPane(messageContent);
        scrollPane.setVvalue(1.0);
        DatabaseHelper.deleteAllMessages();
        DatabaseHelper.deleteAllUsers();
        DatabaseHelper.createMessagesTable();
        DatabaseHelper.createTables();
        User sender = new User(-1, "senderUser", "password".toCharArray(), "sender@example.com", null, true, false, null, "John", null, "Doe", "Johnny", false, false, true, new ArrayList<>());
        User threadUser = new User(-1, "threadUser", "password".toCharArray(), "thread@example.com", null, true, false, null, "Jane", null, "Doe", "Janie", false, false, true, new ArrayList<>());
        DatabaseHelper.addUser(sender);
        DatabaseHelper.addUser(threadUser);
        Message messag = new Message(MessageType.SPECIFIC, "Test message content", sender, Role.STUDENT, threadUser, new Timestamp(System.currentTimeMillis()));
        Message messag2 = new Message(MessageType.SPECIFIC, "Test message content", threadUser, Role.INSTRUCTOR, sender, new Timestamp(System.currentTimeMillis()));
        DatabaseHelper.addMessage(messag);
        DatabaseHelper.addMessage(messag2);
        ApplicationStateManager.setLoggedInUser(sender);
        ArrayList<Message> messages = MessengerUtils.getAllMessagesInUserThread(ApplicationStateManager.getLoggedInUser());
        for (int i = 0; i < messages.size(); i++) {
        	VBox message = new VBox();
        	Label text = new Label(messages.get(i).getMessageContent());
        	Label Sender = new Label(messages.get(i).getSender().getUserName() + ":");
        	Label pad = new Label("\n");
        	message.getChildren().add(Sender);
        	message.getChildren().add(text);
        	if (i != messages.size() - 1 ) {
        		message.getChildren().add(pad);
        	}
        	messageContent.getChildren().add(message);

			if (i % 2 == 0) {
				message.setAlignment(Pos.CENTER_LEFT);
			} else {
				message.setAlignment(Pos.CENTER_RIGHT);
			}
        }
		 Button backButton = new Button("Back");
	        backButton.setOnAction(e -> {
	            PageManager.switchToPage("student"); // Switch to login page
	        });
	        // add new message button open to page thing a magig
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
		// TODO Auto-generated method stub
		
	}
}


