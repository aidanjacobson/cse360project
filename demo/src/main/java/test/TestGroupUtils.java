package test;

import cse360project.utils.GroupUtils;
import cse360project.Article;

import java.util.ArrayList;

public class TestGroupUtils {

    static int numPassed = 0;
    static int numFailed = 0;

    public static void main(String[] args) {
        System.out.println("____________________________________________________________________________");
        System.out.println("\nGroupUtils Testing Automation");

        GroupUtils groupUtils = new GroupUtils();

        
        ArrayList<Article> articles = createTestArticles();

        // test getAllArticlesWithGroup
        performGroupTest(1, groupUtils, articles, "Group1", 2); 
        performGroupTest(2, groupUtils, articles, "Group2", 2); 
        performGroupTest(3, groupUtils, articles, "Group3", 1); 
        performGroupTest(4, groupUtils, articles, "NonExistentGroup", 0); 

        // test getAllArticlesWithGroups
        ArrayList<String> groupList = new ArrayList<>();
        groupList.add("Group1");
        groupList.add("Group2");
        performGroupsTest(5, groupUtils, articles, groupList, 3);  

        // test consolidateGroups
        performConsolidateGroupsTest(6, groupUtils, articles, 3); 

        // test formatGroupName
        performFormatGroupNameTest(7, groupUtils, " group1 test ", "Group1 Test");

        System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    // test getAllArticlesWithGroup
    private static void performGroupTest(int testCase, GroupUtils groupUtils, ArrayList<Article> articles, String group, int expectedCount) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase + " (Group Test)");
        System.out.println("Group: \"" + group + "\"");

        ArrayList<Article> result = groupUtils.getAllArticlesWithGroup(articles, group);
        evaluateResult(result.size() == expectedCount, "Group test", "Expected " + expectedCount + " articles in group: " + group);
    }

    // test getAllArticlesWithGroups
    private static void performGroupsTest(int testCase, GroupUtils groupUtils, ArrayList<Article> articles, ArrayList<String> groups, int expectedCount) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase + " (Groups Test)");
        System.out.println("Groups: " + groups);

        ArrayList<Article> result = groupUtils.getAllArticlesWithGroups(articles, groups);
        evaluateResult(result.size() == expectedCount, "Groups test", "Expected " + expectedCount + " articles in groups: " + groups);
    }

    // test consolidateGroups
    private static void performConsolidateGroupsTest(int testCase, GroupUtils groupUtils, ArrayList<Article> articles, int expectedCount) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase + " (Consolidate Groups Test)");

        ArrayList<String> result = groupUtils.consolidateGroups(articles);
        evaluateResult(result.size() == expectedCount, "Consolidate groups test", "Expected " + expectedCount + " unique groups");
    }

    // test FormationGroupName
    private static void performFormatGroupNameTest(int testCase, GroupUtils groupUtils, String input, String expectedOutput) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase + " (Format Group Name Test)");
        System.out.println("Input: \"" + input + "\"");

        String result = groupUtils.formatGroupName(input);
        evaluateResult(result.equals(expectedOutput), "Format group name test", "Expected: \"" + expectedOutput + "\"");
    }

    // Evaluates 
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
        
        Article article1 = new Article();
        article1.addGroup("Group1");
        articles.add(article1);
        
        Article article2 = new Article();
        article2.addGroup("Group1");
        article2.addGroup("Group2");
        articles.add(article2);
        
        Article article3 = new Article();
        article3.addGroup("Group2");
        articles.add(article3);
        
        Article article4 = new Article();
        article4.addGroup("Group3");
        articles.add(article4);
        
        return articles;
    }
}
