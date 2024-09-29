package cse360project.pages;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import cse360project.utils.DatabaseHelper;
import cse360project.utils.PageManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LoginPage implements Page {
    StackPane root = new StackPane();
    public LoginPage() {
    	boolean isDatabaseEmpty = DatabaseHelper.isDatabaseEmpty();
    	if(isDatabaseEmpty) {
//    		PageManager.registerPage("accountsetup", new AccountSetUp());
//    		PageManager.switchToPage("accountsetup");
    	}
        VBox vbox = new VBox(10);
        Text titleText = new Text("this is the login page");
        vbox.getChildren().add(titleText);
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
        root.getChildren().add(vbox);
        login.setOnAction(e -> {
        	try {
				PreparedStatement ps = DatabaseHelper.prepareStatement("SELECT * FROM cse360users WHERE username=?");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        	
        });
        /*
         * if user exist
         * 		if user has OTP flag
         * 				if OTP expired
         * 					"error expired OTP
         * 				else 
         * 					Login
         * 					redirect to password rest
         * 		else
         * 			if !accountsetup
         * 				login 
         * 				redirect account set up
         * 			else
         * 				login
         * 				redirect to role selection
         * else
         * 		clear fields
         * 		"incorrect username or password" 			
         */
        VBox vbox2 = new VBox(10);
        vbox2.setAlignment(Pos.BOTTOM_CENTER);
        final TextField invite_code = new TextField();
        invite_code.setPromptText("Enter Invite Code");
        invite_code.setMaxWidth(200);
        invite_code.getText();
        vbox2.getChildren().add(invite_code);
        Button join = new Button("Join here");
        vbox2.getChildren().add(join);
        root.getChildren().add(vbox2);
        /* join here button selected
         * 	if invite code is valid
         * 		login
         * 		redirect user/pass set up screen
         * 		send invite code
         * 	else
         * 		clear fields
         * 		"Incorrect Invite Code
         */
    }

    public void onPageOpen() {
        // System.out.println("You visited the login page");
    }

    public StackPane getRoot() {
        return root;
    }
}