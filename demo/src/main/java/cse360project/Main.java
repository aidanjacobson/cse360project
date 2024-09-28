package cse360project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import cse360project.pages.LoginPage;
import cse360project.pages.RoleSelectionPage;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.PageManager;
import cse360project.pages.UserPassSetupPage;
import cse360project.pages.StudentPage;
import cse360project.pages.InstructorPage;

 
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    public void start(Stage primaryStage) {
        // show the initial window
    	System.out.println("Starting CSE360 Project...");

        // initialize the database
        DatabaseHelper.connectToDatabase();
        
        // set the stage title and show it
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
        PageManager.registerPage("roleselection", new RoleSelectionPage());
        PageManager.registerPage("userpasssetup", new UserPassSetupPage());
        PageManager.registerPage("instructor", new InstructorPage());
        PageManager.registerPage("student", new StudentPage());



        // switch to the login page on open
        PageManager.switchToPage("login");
    }
}