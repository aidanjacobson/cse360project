package cse360project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import cse360project.pages.LoginPage;
 
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    public void start(Stage primaryStage) {
        // show the initial window
    	System.out.println("Starting CSE360 Project...");
        primaryStage.setTitle("CSE360 Project");
        primaryStage.show();

        // 1500x700 window with nothing in it...yet
        Scene scene = new Scene(new StackPane(), 1500, 700);
        primaryStage.setScene(scene);
        // initialize the PageManager
        PageManager.setPrimaryScene(scene);

        // Register page names to pages
        // add your pages here
        PageManager.registerPage("login", new LoginPage());

        // switch to the login page on open
        PageManager.switchToPage("login");
    }
}