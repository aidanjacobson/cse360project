package cse360project.pages;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class LoginPage implements Page {
    StackPane root = new StackPane();
    public LoginPage() {
        Text titleText = new Text("this is the login page");
        root.getChildren().add(titleText);
    }

    public void onPageOpen() {
        System.out.println("You visited the login page");
    }

    public StackPane getRoot() {
        return root;
    }
}