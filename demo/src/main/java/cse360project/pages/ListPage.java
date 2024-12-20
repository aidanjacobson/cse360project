package cse360project.pages;

import cse360project.Article;
import cse360project.User;
import cse360project.pages.viewedit.EditPage;
import cse360project.pages.viewedit.ViewPage;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.Level;
import cse360project.utils.PageManager;
import cse360project.utils.GroupUtils;
import cse360project.utils.SearchUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;

/**
 * This class represents the ListPage, which displays a list of articles
 */
public class ListPage implements Page {

    // Root element for this page
    StackPane root = new StackPane();

    // List to store all articles (combining both dummy and database articles)
    ArrayList<Article> allArticles = new ArrayList<>();
    ArrayList<Article> filteredArticles = new ArrayList<>(); // Store filtered articles based on search query

    // UI Elements for displaying feedback
    Label feedbackLabel = new Label(); // Label to display feedback messages
    ListView<Article> articleListView = new ListView<>(); // ListView for displaying articles

    // Group dropdown
    ComboBox<String> groupComboBox;

    /**
     * Constructor for the ListPage class
     */
    public ListPage() {
        
    }

    /**
     * Set up the UI for the list page
     */
    private void setupUI() {
        User current = ApplicationStateManager.getLoggedInUser();
        allArticles = GroupUtils.getAllArticlesForUser(current); // Fetch articles from the database
        root.getChildren().clear(); // Clear the root element before adding new elements
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("Help Articles");
        titleLabel.setFont(new Font("Arial", 24));

        TextField searchField = new TextField();
        searchField.setPromptText("Search for articles...");
        searchField.setPrefWidth(400);
        searchField.setOnKeyReleased(e -> handleSearch(searchField.getText()));

        Label groupLabel = new Label("Filter by Group:");

        // Use GroupUtils to consolidate groups and format them
        ArrayList<String> distinctGroups = GroupUtils.consolidateGroups(allArticles);
        distinctGroups.add("All Groups"); // Include an option for "All Groups"
        distinctGroups.replaceAll(GroupUtils::formatGroupName); // Format group names consistently
        ObservableList<String> groups = FXCollections.observableArrayList(distinctGroups);
        groupComboBox = new ComboBox<>(groups);
        groupComboBox.setValue("All Groups");
        groupComboBox.setOnAction(e -> handleGroupFilter(groupComboBox.getValue()));

        feedbackLabel.setFont(new Font("Arial", 14));
        feedbackLabel.setStyle("-fx-text-fill: red;");

        articleListView.setPrefHeight(500);
        articleListView.setCellFactory(param -> new ListCell<Article>() {
            @Override
            protected void updateItem(Article article, boolean empty) {
                super.updateItem(article, empty);
                if (empty || article == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox articleBox = new VBox(5);
                    // Label titleLabel = new Label(article.title + " - " + Level.levelToString(article.level));
                    Label titleLabel = new Label(String.format("%s - %s (#%d)", article.title, Level.levelToString(article.level), article.getID()));
                    titleLabel.setFont(new Font("Arial", 16));
                    titleLabel.setStyle("-fx-font-weight: bold;");
                    Label descriptionLabel = new Label(article.description);
                    descriptionLabel.setStyle("-fx-font-size: 12;");
                    Label keywordsLabel = new Label("Keywords: " + article.keywords);
                    keywordsLabel.setStyle("-fx-font-size: 11; -fx-font-style: italic;");
                    Label tagsLabel = new Label("Tags: " + String.join(", ", article.groups));
                    tagsLabel.setStyle("-fx-font-size: 11;");
                    articleBox.getChildren().addAll(titleLabel, descriptionLabel, keywordsLabel, tagsLabel);
                    setGraphic(articleBox);
                }
            }
        });
        
        articleListView.setOnMouseClicked((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                    	Article selectedArticle = articleListView.getSelectionModel().getSelectedItem();
                    	if (selectedArticle != null) {
                            // Get the ViewArticle page from the PageManager
                            ViewPage viewPage = (ViewPage) PageManager.getPageByName("viewpage");

                            // Set the selected article to be viewed
                            viewPage.setViewingArticle(selectedArticle);

                            // Switch to the viewpage
                            PageManager.switchToPage("viewpage");
                        }
                    }
                }
            }
        });

        updateArticleList(allArticles); // Initially populate with all articles

        HBox searchGroupBox = new HBox(10);
        searchGroupBox.setAlignment(Pos.CENTER);
        searchGroupBox.getChildren().addAll(searchField, groupLabel, groupComboBox);

        // Back and Logout buttons
        Button backButton = new Button("Back");
        Button logoutButton = new Button("Logout");

        backButton.setOnAction(e -> {
            ApplicationStateManager.switchToRolePage(); // Switch to the respective role page
        });

        logoutButton.setOnAction(e -> {
            ApplicationStateManager.logout(); // Log the user out
        });

        // Create New Article button
        Button createArticleButton = new Button("Create New Article");
        createArticleButton.setOnAction(e -> {
            // Get the EditArticle page from the PageManager
            EditPage editPage = (EditPage) PageManager.getPageByName("editpage");

            // Set up the page with placeholders for creating a new article
            Article newArticle = new Article(-1, Level.BEGINNER, new ArrayList<>(), "", "", "", "", new ArrayList<>());
            editPage.setEditingArticle(newArticle); // Pass the new article to the edit page

            // Switch to the edit article page
            PageManager.switchToPage("editpage");
        });

        // HBox for the back, logout, and create article buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(backButton, logoutButton, createArticleButton);

        // Add elements to the main layout
        mainLayout.getChildren().addAll(titleLabel, searchGroupBox, feedbackLabel, articleListView, buttonBox);

        root.getChildren().add(mainLayout);
    }

    private void handleSearch(String query) {
        if (query.isEmpty()) {
            feedbackLabel.setText("Showing all articles.");
            filteredArticles = new ArrayList<>(allArticles); // Reset to show all articles if no search query
        } else {
            filteredArticles = SearchUtil.searchArticles(allArticles, query); // Store the search results
            if (filteredArticles.isEmpty()) {
                feedbackLabel.setText("No articles found for query: \"" + query + "\".");
            } else {
                feedbackLabel.setText(filteredArticles.size() + " articles found for query: " + query);
            }
        }

        // After searching, apply the group filter if any is selected
        String selectedGroup = groupComboBox.getValue();
        if (!"All Groups".equals(selectedGroup)) {
            handleGroupFilter(selectedGroup); // Reapply the group filter if a group is selected
        } else {
            updateArticleList(filteredArticles); // Otherwise, just show the search results
        }
    }


    private void handleGroupFilter(String group) {
        feedbackLabel.setText("Filtering by group: " + group);

        // Use filteredArticles list which holds the articles from the search query
        ArrayList<Article> articlesToFilter = filteredArticles.isEmpty() ? allArticles : filteredArticles;

        if ("All Groups".equals(group)) {
            updateArticleList(articlesToFilter); // If "All Groups" is selected, show the filteredArticles (search results)
        } else {
            // Filter based on the group from the already filtered list (either search results or allArticles)
            ArrayList<Article> filteredArticlesByGroup = GroupUtils.getAllArticlesWithGroup(articlesToFilter, group);
            updateArticleList(filteredArticlesByGroup);
        }
    }


    private void updateArticleList(ArrayList<Article> articles) {
        ObservableList<Article> observableArticles = FXCollections.observableArrayList(articles);
        articleListView.setItems(observableArticles);
    }

    @Override
    public StackPane getRoot() {
        return root;
    }

    @Override
    public void onPageOpen() {
        feedbackLabel.setText("");
        setupUI();
    }
}
