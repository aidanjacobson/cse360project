package cse360project;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import cse360project.pages.LoginPage;
import cse360project.pages.PasswordResetPage;
import cse360project.pages.RoleSelectionPage;
import cse360project.pages.StudentMessagePage;
import cse360project.utils.DatabaseHelper;
import cse360project.utils.MessageType;
import cse360project.utils.PageManager;
import cse360project.pages.UserPassSetupPage;
import cse360project.pages.admin.AdminPage;
import cse360project.pages.usergroups.UserGroupEditListPage;
import cse360project.pages.StudentPage;
import cse360project.utils.ApplicationStateManager;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import cse360project.utils.Role;

import cse360project.pages.AccountSetupScreen;
import cse360project.pages.viewedit.EditPage;
import cse360project.pages.viewedit.ViewPage;
import cse360project.pages.InstructorPage;
import cse360project.pages.ListPage;

 
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    public void start(Stage primaryStage) throws SQLException {
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
        PageManager.registerPage("admin", new AdminPage());
        PageManager.registerPage("accountsetup", new AccountSetupScreen());
        PageManager.registerPage("passwordreset", new PasswordResetPage());
        PageManager.registerPage("listarticles", new ListPage());

        PageManager.registerPage("viewpage", new ViewPage());
        PageManager.registerPage("editpage", new EditPage());

        PageManager.registerPage("editusergroups", new UserGroupEditListPage());
        PageManager.registerPage("studentmessage", new StudentMessagePage());

        // init test db
        DatabaseHelper.setDatabasePath("~/testdb");
        DatabaseHelper.connectToDatabase();
        DatabaseHelper.deleteAllUsers();
        DatabaseHelper.deleteAllMessages();

        // add test users
        User student = new User(-1, "student", "password".toCharArray(), "sender@example.com", null, true, false, null, "John", null, "Doe", "Johnny", false, false, true, new ArrayList<>());
        User instructor = new User(-1, "instructor", "password".toCharArray(), "thread@example.com", null, true, false, null, "Jane", null, "Doe", "Janie", false, true, false, new ArrayList<>());

        DatabaseHelper.addUser(student);
        DatabaseHelper.addUser(instructor);

        ApplicationStateManager.setLoggedInUser(student);

        // add test messages
        Message message = new Message(MessageType.GENERIC, "Hi i have a problem", student, Role.STUDENT, student, new Timestamp(System.currentTimeMillis()));
        Message message2 = new Message(MessageType.GENERIC, "Hi i can help", instructor, Role.INSTRUCTOR, student, new Timestamp(System.currentTimeMillis()));

        DatabaseHelper.addMessage(message);
        DatabaseHelper.addMessage(message2);

        // add a really long message
        String longMessage = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus. Suspendisse lectus tortor, dignissim sit amet, adipiscing nec, ultricies sed, dolor. Cras elementum ultrices diam. Maecenas ligula massa, varius a, semper congue, euismod non, mi. Proin porttitor, orci nec nonummy molestie, enim est eleifend mi, non fermentum diam nisl sit amet erat. Duis semper. Duis arcu massa, scelerisque vitae, consequat in, pretium a, enim. Pellentesque congue. Ut in risus volutpat libero pharetra tempor. Cras vestibulum bibendum augue. Praesent egestas leo in pede. Praesent blandit odio eu enim. Pellentesque sed d";
        Message message3 = new Message(MessageType.GENERIC, longMessage, student, Role.STUDENT, student, new Timestamp(System.currentTimeMillis()));
        DatabaseHelper.addMessage(message3);

        // add 50 instructor messages to test scrolling
        for (int i = 0; i < 50; i++) {
            Message message4 = new Message(MessageType.GENERIC, "Message " + i, instructor, Role.INSTRUCTOR, student, new Timestamp(System.currentTimeMillis()));
            DatabaseHelper.addMessage(message4);
        }

        // switch to the login page on open
        PageManager.switchToPage("studentmessage");
    }
}