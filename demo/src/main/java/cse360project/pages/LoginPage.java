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
    	boolean isDatabaseEmpty = DatabaseHelper.isDatabaseEmpty();
    	if(isDatabaseEmpty) {
    		PageManager.switchToPage("accountsetup");
    	}

		root.setPadding(new Insets(100, 100, 100, 100));
 
        VBox vbox = new VBox(10);
		
        Label label = new Label("Login here");
        vbox.getChildren().add(label);
        vbox.setAlignment(Pos.CENTER);
        final TextField username = new TextField();
        username.setPromptText("Enter Username");
        username.setMaxWidth(200);
        username.getText();
        vbox.getChildren().add(username);
        final TextField password = new TextField();
        password.setPromptText("Enter Password");
        password.setMaxWidth(200);
        password.getText();
        vbox.getChildren().add(password);
        Button login = new Button("Login");
        vbox.getChildren().add(login);
        login.setOnAction(e -> {
        try { 
				Timestamp today = Timestamp.valueOf(LocalDateTime.now());
        		PreparedStatement ps = DatabaseHelper.prepareStatement("SELECT * FROM cse360users WHERE username=?");
				ps.setString(1, username.getText());
				User user = DatabaseHelper.getOneUser(ps);
	        	if (user != null) {
	        		if (password.getText().equals(user.password)) {
	        			if(user.OTP) {
	        				if(today.before(user.OTP_expiration)) {
	        					ApplicationStateManager.setLoggedInUser(user);
	        		    		PageManager.switchToPage("userpasssetup");
	        				}else {
	        			        Alert emailAlert = new Alert(AlertType.ERROR, "Your One Time Password has expired", ButtonType.OK);
	        			        emailAlert.showAndWait();
	        				}
	        			}else {
	        				ApplicationStateManager.setLoggedInUser(user);
        		    		PageManager.switchToPage("roleselection");
        			}
	        		}else {
	        			Alert emailAlert = new Alert(AlertType.ERROR, "Your Username and/or Password is invalid", ButtonType.OK);
    			        emailAlert.showAndWait();
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
        final TextField invite_code = new TextField();
        invite_code.setPromptText("Enter Invite Code");
        invite_code.setMaxWidth(200);
        invite_code.getText();
        vbox2.getChildren().add(invite_code);
        Button join = new Button("Join here");
        vbox2.getChildren().add(join);
        join.setOnAction(e -> {
            try { 
				PreparedStatement ps2 = DatabaseHelper.prepareStatement("SELECT * FROM cse360users WHERE inviteCode=?");
				ps2.setString(1, invite_code.getText());
				User user2 = DatabaseHelper.getOneUser(ps2);
				if (user2 != null) {
					ApplicationStateManager.setLoggedInUser(user2);
					PageManager.switchToPage("userpasssetup");
				}else {
					Alert emailAlert = new Alert(AlertType.ERROR, "Your Username and/or Password is invalid", ButtonType.OK);
					emailAlert.showAndWait();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            	
		});
		root.setCenter(vbox);
		root.setBottom(vbox2);
	}

    public void onPageOpen() {
        // System.out.println("You visited the login page");
    }

    public BorderPane getRoot() {
        return root;
    }
}