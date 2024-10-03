package cse360project.pages;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import cse360project.User;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.PageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LoginPage implements Page {
    BorderPane root = new BorderPane();
    public LoginPage() {
    	boolean isDatabaseEmpty = DatabaseHelper.isDatabaseEmpty(); //check to see if there are no users in the database
    	if(isDatabaseEmpty) {
    		PageManager.switchToPage("accountsetup"); //if database is empty immediately redirect to the accountsetup page to set up admin
    	}

		root.setPadding(new Insets(100, 100, 100, 100)); // padding to distance the vboxes
 
        VBox vbox = new VBox(10);
		
        Label label = new Label("Login here"); // indicate where to login
        vbox.getChildren().add(label);
        vbox.setAlignment(Pos.CENTER);
        final TextField username = new TextField(); // the textbox which they will enter their username in
        username.setPromptText("Enter Username"); //prompt for username
        username.setMaxWidth(200); // dimensions of the box
        username.getText(); //get password
        vbox.getChildren().add(username);
        final TextField password = new TextField(); // the textbox which they will enter their password in
        password.setPromptText("Enter Password"); //prompt for password
        password.setMaxWidth(200); // dimensions of the box
        password.getText(); //get password
        vbox.getChildren().add(password);
        Button login = new Button("Login"); // the login button
        vbox.getChildren().add(login);
        login.setOnAction(e -> { // clicking the button
        try { 
				Timestamp today = Timestamp.valueOf(LocalDateTime.now()); //get todays date
        		PreparedStatement ps = DatabaseHelper.prepareStatement("SELECT * FROM cse360users WHERE username=?"); //check if the user exist
				ps.setString(1, username.getText());
				User user = DatabaseHelper.getOneUser(ps); // get the user, if they don't exist then sets to null
	        	if (user != null) {
	        		if (password.getText().equals(user.password)) { // make sure passwords are equal
	        			if(user.OTP) {
	        				if(today.before(user.OTP_expiration)) {
	        					ApplicationStateManager.setLoggedInUser(user);
	        		    		PageManager.switchToPage("userpasssetup");
	        				}else {
	        			        Alert emailAlert = new Alert(AlertType.ERROR, "Your One Time Password has expired", ButtonType.OK); 
	        			        emailAlert.showAndWait(); // alert them if OTP is expired
	        				}
	        			}else {
	        				ApplicationStateManager.setLoggedInUser(user);//log them in
	        				username.clear();//clear username
        					password.clear();// clear password
        		    		PageManager.switchToPage("roleselection"); //switch to role selection page
        			}
	        		}else {
	        			Alert emailAlert = new Alert(AlertType.ERROR, "Your Username and/or Password is invalid", ButtonType.OK);
    			        emailAlert.showAndWait();//alert them their username and/or password is wrong
	        		}
	        	}
	        	else {
	        		Alert emailAlert = new Alert(AlertType.ERROR, "Your Username is invalid please set up an account", ButtonType.OK);
			        emailAlert.showAndWait();
	        	}
        	} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        });
        VBox vbox2 = new VBox(10);
		
        vbox2.setAlignment(Pos.BOTTOM_CENTER);
        final TextField invite_code = new TextField(); //text box to enter invite code
        invite_code.setPromptText("Enter Invite Code"); //prompt them to enter invite code
        invite_code.setMaxWidth(200); //dimensions for text field
        invite_code.getText(); //get the invite code
        vbox2.getChildren().add(invite_code);
        Button join = new Button("Join here"); //button to submit invite code
        vbox2.getChildren().add(join);
        join.setOnAction(e -> {
            try { 
				PreparedStatement ps2 = DatabaseHelper.prepareStatement("SELECT * FROM cse360users WHERE inviteCode=?"); //check if the invite code exist
				ps2.setString(1, invite_code.getText());
				User user2 = DatabaseHelper.getOneUser(ps2); // get the user, if they don't exist then sets to null
				if (user2 != null) {
					ApplicationStateManager.setLoggedInUser(user2); //log the user in
					invite_code.clear(); //clear the invite code field
					PageManager.switchToPage("userpasssetup"); //redirect them to userpasssetup
				}else {
					Alert emailAlert = new Alert(AlertType.ERROR, "Your Invite Code invalid", ButtonType.OK);
					emailAlert.showAndWait();//alert them their invite code is invalid
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            	
		});
		root.setCenter(vbox); //position vbox on screen
		root.setBottom(vbox2); //position vbox2 on screen
	}

    public void onPageOpen() {
        // System.out.println("You visited the login page");
    }

    public BorderPane getRoot() {
        return root;
    }
}