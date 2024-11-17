package cse360project.utils;

import cse360project.Article;

import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestGroupUtils {

	private ArrayList<Article> articles;

    /**
     * Setup the test articles before each test
     */
    @Before
    public void setUp() {
        // Initialize the test articles before each test
        articles = createTestArticles();
    }

    /**
     * Test the getAllArticlesWithGroup method
     */
    @Test
    public void testGetAllArticlesWithGroup_Group1() {
        ArrayList<Article> result = GroupUtils.getAllArticlesWithGroup(articles, "Group 1");
        assertEquals("Expected 2 articles in group: Group 1", 2, result.size());
    }

    /**
     * Test the getAllArticlesWithGroup method
     */
    @Test
    public void testGetAllArticlesWithGroup_Group2() {
        ArrayList<Article> result = GroupUtils.getAllArticlesWithGroup(articles, "Group 2");
        assertEquals("Expected 2 articles in group: Group 2", 2, result.size());
    }

    /**
     * Test the getAllArticlesWithGroup method
     */
    @Test
    public void testGetAllArticlesWithGroup_Group3() {
        ArrayList<Article> result = GroupUtils.getAllArticlesWithGroup(articles, "Group 3");
        assertEquals("Expected 1 article in group: Group 3", 1, result.size());
    }

    /**
     * Test the getAllArticlesWithGroup method
     */
    @Test
    public void testGetAllArticlesWithGroup_NonExistentGroup() {
        ArrayList<Article> result = GroupUtils.getAllArticlesWithGroup(articles, "NonExistentGroup");
        assertEquals("Expected 0 articles in non-existent group", 0, result.size());
    }

    /**
     * Test the getAllArticlesWithGroups method
     */
    @Test
    public void testGetAllArticlesWithGroups() {
        ArrayList<String> groupList = new ArrayList<>();
        groupList.add("Group 1");
        groupList.add("Group 2");
        ArrayList<Article> result = GroupUtils.getAllArticlesWithGroups(articles, groupList);
        assertEquals("Expected 3 articles in groups: Group 1, Group 2", 3, result.size());
    }

    /**
     * Test the consolidateGroups method
     */
    @Test
    public void testConsolidateGroups() {
        ArrayList<String> result = GroupUtils.consolidateGroups(articles);
        assertEquals("Expected 3 unique groups", 3, result.size());
        assertTrue("Result should contain Group 1", result.contains("Group 1"));
        assertTrue("Result should contain Group 2", result.contains("Group 2"));
        assertTrue("Result should contain Group 3", result.contains("Group 3"));
    }

    /**
     * Test the formatGroupName method
     */
    @Test
    public void testFormatGroupName() {
        String result = GroupUtils.formatGroupName(" group1 test ");
        assertEquals("Expected formatted group name to be 'Group1 Test'", "Group1 Test", result);
    }

    /**
     * Method to create test articles
     */
    private ArrayList<Article> createTestArticles() {
        ArrayList<Article> articles = new ArrayList<>();

        ArrayList<String> groups1 = new ArrayList<>();
        groups1.add("Group 1");
        groups1.add("Group 2");

        ArrayList<String> links1 = new ArrayList<>();
        links1.add("Link 1");
        links1.add("http://link1.com");

        Article article1 = new Article(1L, Level.BEGINNER, groups1, "Title 1", "Description 1", "Keywords 1", "Body 1", links1);
        articles.add(article1);

        ArrayList<String> groups2 = new ArrayList<>();
        groups2.add("Group 2");

        ArrayList<String> links2 = new ArrayList<>();
        links2.add("Link 2");

        Article article2 = new Article(2L, Level.INTERMEDIATE, groups2, "Title 2", "Description 2", "Keywords 2", "Body 2", links2);
        articles.add(article2);

        ArrayList<String> groups3 = new ArrayList<>();
        groups3.add("Group 1");
        groups3.add("Group 3");

        ArrayList<String> links3 = new ArrayList<>();
        links3.add("Link 3");
        links3.add("http://link3.com");

        Article article3 = new Article(3L, Level.ADVANCED, groups3, "Title 3", "Description 3", "Keywords 3", "Body 3", links3);
        articles.add(article3);

        return articles;
    }
}
