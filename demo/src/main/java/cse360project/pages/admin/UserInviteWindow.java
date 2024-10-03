package cse360project.pages.admin;

import cse360project.User;
import cse360project.utils.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserInviteWindow {
    static CheckBox adminCB, studentCB, instructorCB;
    static Button inviteButton;
    static Stage inviteStage;

    public static void openInviteUserDialog() {
        // prepare to create a new window for inviting the user
        inviteStage = new Stage();
        inviteStage.setTitle("Invite user");

        StackPane root = new StackPane();

        // it should be 450x450 px
        Scene inviteScene = new Scene(root, 450, 450);
        inviteStage.setScene(inviteScene);

        // open the window
        inviteStage.show();

        // create the page content holder
        VBox pageContent = new VBox();
        pageContent.setAlignment(Pos.CENTER);
        root.getChildren().add(pageContent);

        // create the instruction text
        Text instructions = new Text("Select the roles for the new user:");
        instructions.setFont(Font.font(20));
        pageContent.getChildren().add(instructions);

        // create a grid that will hold 3 checkboxes and 3 labels
        GridPane checkboxGrid = new GridPane();
        checkboxGrid.setHgap(10);
        checkboxGrid.setVgap(10);
        checkboxGrid.setAlignment(Pos.CENTER);
        VBox.setMargin(checkboxGrid, new Insets(20));
        pageContent.getChildren().add(checkboxGrid);

        // create checkbox for admin
        adminCB = new CheckBox("Admin");
        adminCB.setFont(Font.font(15));

        // create checkbox for student
        studentCB = new CheckBox("Student");
        studentCB.setFont(Font.font(15));

        // create checkbox for instructor
        instructorCB = new CheckBox("Instructor");
        instructorCB.setFont(Font.font(15));

        // add all checkboxes to grid
        checkboxGrid.add(adminCB,      0, 0, 1, 1);
        checkboxGrid.add(studentCB,    0, 1, 1, 1);
        checkboxGrid.add(instructorCB, 0, 2, 1, 1);

        // create the invite button in center of vbox
        inviteButton = new Button("Invite User");
        inviteButton.setAlignment(Pos.CENTER);
        VBox.setMargin(inviteButton, new Insets(20));

        // should be light blue with white text
        inviteButton.setStyle("-fx-background-color: #0088FF");
        inviteButton.setTextFill(Color.WHITE);
        inviteButton.setFont(Font.font(15));

        // should be disabled to begin with
        inviteButton.setDisable(true);

        // when any checkbox is clicked, update the invite button
        adminCB.setOnAction(e->checkboxWasClicked());
        studentCB.setOnAction(e->checkboxWasClicked());
        instructorCB.setOnAction(e->checkboxWasClicked());

        // when the invite button is clicked, run invite user code
        inviteButton.setOnAction(e->submitInvite());

        // add to page
        pageContent.getChildren().add(inviteButton);
    }

    static void checkboxWasClicked() {
        // count how many roles are selected
        int selectedCount = 0;
        if (adminCB.isSelected()) selectedCount++;
        if (studentCB.isSelected()) selectedCount++;
        if (instructorCB.isSelected()) selectedCount++;

        // if no roles are selected, disable the checkbox
        if (selectedCount > 0) {
            inviteButton.setDisable(false);
        } else {
            inviteButton.setDisable(true);
        }
    }

    static void submitInvite() {
        // determine new roles
        boolean is_admin = adminCB.isSelected();
        boolean is_student = studentCB.isSelected();
        boolean is_instructor = instructorCB.isSelected();

        // create the user with the roles and add to database
        User newUser = User.createInvitedUser(is_admin, is_instructor, is_student);
        DatabaseHelper.addUser(newUser);

        System.out.printf("A new user has been invited with code: \"%s\"%n", newUser.inviteCode);

        // show a popup with the new invite code
        String roleString = getRoleListString(is_admin, is_student, is_instructor);
        String messageContents = String.format("A new user has been created with the following roles:%n%s%nTheir invite code is \"%s\".%nPlease write this down as it will not be shown again.", roleString, newUser.inviteCode);
        Alert inviteAlert = new Alert(AlertType.INFORMATION, messageContents, ButtonType.OK);
        inviteAlert.showAndWait();
        inviteStage.hide();
    }

    static String getRoleListString(boolean is_admin, boolean is_student, boolean is_instructor) {
        String out = "";
        if (is_admin) {
            if (out.equals("")) {
                out = "Admin";
            } else {
                out += ", Admin";
            }
        }
        if (is_student) {
            if (out.equals("")) {
                out = "Student";
            } else {
                out += ", Student";
            }
        }
        if (is_instructor) {
            if (out.equals("")) {
                out = "Instructor";
            } else {
                out += ", Instructor";
            }
        }
        return out;
    }
}
