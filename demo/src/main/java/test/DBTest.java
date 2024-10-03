package test;

import java.sql.SQLException;

import cse360project.User;
import cse360project.utils.DatabaseHelper;

public class DBTest {
    public static void main(String[] args) {
        initialTest();
    }
    public static void initialTest() {
        /*
         * This is a general test of the database.
         * It will destroy the testdb database, then will test the commands.
         * First it verifies the db is empty.
         * Then it adds an admin user.
         * Then it attempts to find the admin user.
         * Then it changes the admin preferred name.
         * Then it deletes the admin user, and verifies the db is once again empty.
         */
        try {
            DatabaseHelper.setDatabasePath("~/testdb");
            DatabaseHelper.connectToDatabase();
            DatabaseHelper.deleteAllUsers();

            DatabaseHelper.createTables();


            boolean empty = DatabaseHelper.isDatabaseEmpty();
            System.out.printf("Database empty: %s%n", empty ? "true" : "false");

            if (empty) {
                User newUser = new User(-1, "admin", "password", "a@a.a", null, true, false, null, "a", "b", "c", "a", true, false, false);
                DatabaseHelper.addUser(newUser);

                System.out.printf("New User ID: %d%n", newUser.id);

                User found = DatabaseHelper.getOneUser("SELECT * FROM cse360users WHERE username='admin'");
                System.out.printf("Name info: %s %s %s (%s)%n", found.firstName, found.middleName, found.lastName, found.preferredName);

                found.preferredName = "new preferred name";
                DatabaseHelper.updateUser(found);

                found = DatabaseHelper.getOneUser("SELECT * FROM cse360users WHERE username='admin'");
                System.out.printf("Name info after changing preferred name: %s %s %s (%s)%n", found.firstName, found.middleName, found.lastName, found.preferredName);

                System.out.println("Attempting delete...");
                boolean successful = DatabaseHelper.deleteUser(found);
                System.out.printf("Succeeded: %s%n", successful ? "true" : "false");

                empty = DatabaseHelper.isDatabaseEmpty();
                System.out.printf("Database empty: %s%n", empty ? "true" : "false");
            }


        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
