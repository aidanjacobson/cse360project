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
        ScrollPane scrollPane = new ScrollPane(messageContent);
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
        	message.getChildren().add(pad);
        	if (messages.get(i).getSender() == ApplicationStateManager.getLoggedInUser()) {
        		
        		message.setAlignment(Pos.BOTTOM_RIGHT);
        	}
        	else {
        		message.setAlignment(Pos.BOTTOM_RIGHT);
        	}
        	messageContent.getChildren().add(message);
        }
		 Button backButton = new Button("Back");
	        backButton.setOnAction(e -> {
	            PageManager.switchToPage("student"); // Switch to login page
	        });
	        // add new message button open to page thing a magig
	        messageContent.setAlignment(Pos.CENTER);
	        scrollPane.setMaxWidth(700);
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


