package cse360project.utils;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import cse360project.Article;

public class NewArticleDBTest {
	@Before
	public void setup() throws SQLException {
		DatabaseHelper.setDatabasePath("~/testdb");
	    DatabaseHelper.connectToDatabase();
	    DatabaseHelper.deleteAllArticles();
	    
	    DatabaseHelper.createArticleTables();
	}

	
	@Test
	public void testDatabaseEmpty() {
		assertTrue("Running test for database is empty", DatabaseHelper.isArticleDatabaseEmpty());
	}
	
	@Test
	public void testCreateArticle() {
		ArrayList<String> groups = new ArrayList<>();
    	groups.add("Group");
    	groups.add("Group2");
    	ArrayList<String> links = new ArrayList<>();
    	links.add("Link");
        Article newArticle = new Article(-1, Level.BEGINNER, groups, "Title", "Description", "Keywords", "Body", links);
        DatabaseHelper.addArticle(newArticle);
        assertFalse("Running test to see if article is in database", DatabaseHelper.isArticleDatabaseEmpty());
	}
	
	@Test
	public void testGetOneArticle() {
		ArrayList<String> groups = new ArrayList<>();
    	groups.add("Group");
    	groups.add("Group2");
    	ArrayList<String> links = new ArrayList<>();
    	links.add("Link");
        Article newArticle = new Article(-1, Level.BEGINNER, groups, "Title", "Description", "Keywords", "Body", links);
        DatabaseHelper.addArticle(newArticle);
		Article found = DatabaseHelper.getOneArticle("SELECT * FROM cse360articles WHERE title='Title'");
		assertTrue("Finding article with title", found != null);
	}
	
	@Test
	public void testUpdateArticle() {
		ArrayList<String> groups = new ArrayList<>();
    	groups.add("Group");
    	groups.add("Group2");
    	ArrayList<String> links = new ArrayList<>();
    	links.add("Link");
        Article newArticle = new Article(-1, Level.BEGINNER, groups, "Title", "Description", "Keywords", "Body", links);
        DatabaseHelper.addArticle(newArticle);
		Article found = DatabaseHelper.getOneArticle("SELECT * FROM cse360articles WHERE title='Title'");
        found.title = "new preferred title";
        DatabaseHelper.updateArticle(found);

        found = DatabaseHelper.getOneArticle("SELECT * FROM cse360articles WHERE title='new preferred title'");
        assertTrue("Finding article with new title", found != null);
	}
	
	@Test
	public void testDeleteArticle() {
		ArrayList<String> groups = new ArrayList<>();
    	groups.add("Group");
    	groups.add("Group2");
    	ArrayList<String> links = new ArrayList<>();
    	links.add("Link");
        Article newArticle = new Article(-1, Level.BEGINNER, groups, "Title", "Description", "Keywords", "Body", links);
        DatabaseHelper.addArticle(newArticle);
        assertTrue("Testing deleteArticle", DatabaseHelper.deleteArticle(newArticle));
	}
}
