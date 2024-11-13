package cse360project.utils;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import cse360project.User;

public class UserDBTest {

	@Before
	public void setup() throws SQLException {
		DatabaseHelper.setDatabasePath("~/testdb");
        DatabaseHelper.connectToDatabase();
        DatabaseHelper.deleteAllUsers();

        DatabaseHelper.createTables();
	}
	@Test
	public void testDBEmpty() {
		assertTrue("Running test for database is empty", DatabaseHelper.isDatabaseEmpty());
	}
	
	@Test
	public void testUserAdd() {
		ArrayList<String> groups = new ArrayList<>();
    	groups.add("Group");
    	groups.add("Group2");
		User newUser = new User(-1, "admin", "password".toCharArray(), "a@a.a", null, true, false, null, "a", "b", "c", "a", true, false, false, groups);
        DatabaseHelper.addUser(newUser);
        assertFalse("Running test to see if user is in database", DatabaseHelper.isDatabaseEmpty());
	}
	
	@Test
	public void testGetOneUser() {
		ArrayList<String> groups = new ArrayList<>();
    	groups.add("Group");
    	groups.add("Group2");
		User newUser = new User(-1, "admin", "password".toCharArray(), "a@a.a", null, true, false, null, "a", "b", "c", "a", true, false, false, groups);
        DatabaseHelper.addUser(newUser);
        User found = DatabaseHelper.getOneUser("SELECT * FROM cse360users WHERE username='admin'");
        assertTrue("Finding user with new username 'admin'", found != null);
	}
	
	@Test
	public void testUpdateUser() {
		ArrayList<String> groups = new ArrayList<>();
    	groups.add("Group");
    	groups.add("Group2");
		User newUser = new User(-1, "admin", "password".toCharArray(), "a@a.a", null, true, false, null, "a", "b", "c", "a", true, false, false, groups);
        DatabaseHelper.addUser(newUser);
        User found = DatabaseHelper.getOneUser("SELECT * FROM cse360users WHERE username='admin'");
        found.preferredName = "new preferred name";
        DatabaseHelper.updateUser(found);

        found = DatabaseHelper.getOneUser("SELECT * FROM cse360users WHERE preferredName='new preferred name'");
        assertTrue("Finding user with new prefered name", found != null);
	}
	
	@Test
	public void testDeleteUser() {
		ArrayList<String> groups = new ArrayList<>();
    	groups.add("Group");
    	groups.add("Group2");
		User newUser = new User(-1, "admin", "password".toCharArray(), "a@a.a", null, true, false, null, "a", "b", "c", "a", true, false, false, groups);
        DatabaseHelper.addUser(newUser);
        assertTrue("Testing deleting a user", DatabaseHelper.deleteUser(newUser));
	}

}
