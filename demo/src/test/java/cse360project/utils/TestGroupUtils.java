package test;

import cse360project.utils.GroupUtils;
import cse360project.Article;
import cse360project.utils.Level;

import java.util.ArrayList;

public class TestGroupUtils {

    static int numPassed = 0;
    static int numFailed = 0;

    public static void main(String[] args) {
        System.out.println("____________________________________________________________________________");
        System.out.println("\nGroupUtils Testing Automation");

      
        ArrayList<Article> articles = createTestArticles();

        // test getAllArticlesWithGroup
        performGroupTest(1, "Group 1", 2); 
        performGroupTest(2, "Group 2", 2); 
        performGroupTest(3, "Group 3", 1);
        performGroupTest(4, "NonExistentGroup", 0); 

        // test getAllArticlesWithGroups
        ArrayList<String> groupList = new ArrayList<>();
        groupList.add("Group 1");
        groupList.add("Group 2");
        performGroupsTest(5, groupList, 3);  

        // test consolidateGroups
        performConsolidateGroupsTest(6, articles, 3);

        // test formatGroupName
        performFormatGroupNameTest(7, " group1 test ", "Group1 Test");

        System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    // test getAllArticlesWithGroup
    private static void performGroupTest(int testCase, String group, int expectedCount) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase + " (Group Test)");
        System.out.println("Group: \"" + group + "\"");

        ArrayList<Article> result = GroupUtils.getAllArticlesWithGroup(createTestArticles(), group);
        evaluateResult(result.size() == expectedCount, "Group test", "Expected " + expectedCount + " articles in group: " + group);
    }

    // test getAllArticlesWithGroups
    private static void performGroupsTest(int testCase, ArrayList<String> groups, int expectedCount) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase + " (Groups Test)");
        System.out.println("Groups: " + groups);

        ArrayList<Article> result = GroupUtils.getAllArticlesWithGroups(createTestArticles(), groups);
        evaluateResult(result.size() == expectedCount, "Groups test", "Expected " + expectedCount + " articles in groups: " + groups);
    }

    // test consolidateGroups
    private static void performConsolidateGroupsTest(int testCase, ArrayList<Article> articles, int expectedCount) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase + " (Consolidate Groups Test)");

        ArrayList<String> result = GroupUtils.consolidateGroups(articles);
        evaluateResult(result.size() == expectedCount, "Consolidate groups test", "Expected " + expectedCount + " unique groups");
    }

    // test formatGroupName
    private static void performFormatGroupNameTest(int testCase, String input, String expectedOutput) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase + " (Format Group Name Test)");
        System.out.println("Input: \"" + input + "\"");

        String result = GroupUtils.formatGroupName(input);
        evaluateResult(result.equals(expectedOutput), "Format group name test", "Expected: \"" + expectedOutput + "\"");
    }

    // Evaluates if the test passed or failed
    private static void evaluateResult(boolean result, String testType, String message) {
        if (result) {
            System.out.println("***Success*** The " + testType + " passed as expected. " + message);
            numPassed++;
        } else {
            System.err.println("***Failure*** The " + testType + " failed. " + message);
            numFailed++;
        }
    }

    private static ArrayList<Article> createTestArticles() {
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
