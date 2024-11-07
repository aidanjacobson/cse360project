package cse360project.pages;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import cse360project.utils.ApplicationStateManager;
import cse360project.utils.PageManager;

public class StudentMessagePage implements Page {
	StackPane root = new StackPane();
	
	public StudentMessagePage() {
		VBox mainLayout = new VBox(10);
        mainLayout.setAlignment(Pos.TOP_LEFT);
        VBox messageContent = new VBox();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        messageContent.setSpacing(10);
        scrollPane.setContent(messageContent);
//        ArrayList<Message> messages = getAllMessagesInUserThread(ApplicationStateManager.getLoggedInUser());
		 Button backButton = new Button("Back");
	        backButton.setOnAction(e -> {
	            PageManager.switchToPage("student"); // Switch to login page
	        });
	        
	        
	        mainLayout.getChildren().addAll(backButton);
	        root.getChildren().add(mainLayout);
	}
	
	
	public StackPane getRoot() {
        return root;
    }


	@Override
	public void onPageOpen() {
		// TODO Auto-generated method stub
		
	}
}
