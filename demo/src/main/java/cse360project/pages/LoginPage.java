package cse360project.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LoginPage implements Page {
    StackPane root = new StackPane();
    public LoginPage() {
    	Text titleText = new Text("this is the login page");
        root.getChildren().add(titleText);
        VBox vbox = new VBox();
        Label label = new Label("Login here");
        vbox.getChildren().add(label);
        vbox.setAlignment(Pos.CENTER);
        final TextField username = new TextField();
        username.setPromptText("Enter Username");
        username.setPrefColumnCount(10);
        username.getText();
        vbox.getChildren().add(username);
        final TextField password = new TextField();
        password.setPromptText("Enter Password");
        password.setPrefColumnCount(10);
        password.getText();
        vbox.getChildren().add(password);
        final TextField invite_code = new TextField();
        invite_code.setPromptText("Enter Invite Code");
        invite_code.setPrefColumnCount(10);
        invite_code.getText();
        vbox.getChildren().add(invite_code);
        Button login = new Button("Login");
        Button join = new Button("Join now");
        vbox.getChildren().add(login);
        vbox.getChildren().add(join);
        root.getChildren().add(vbox);
    }

    public void onPageOpen() {
        System.out.println("You visited the login page");
    }

    public StackPane getRoot() {
        return root;
    }
}