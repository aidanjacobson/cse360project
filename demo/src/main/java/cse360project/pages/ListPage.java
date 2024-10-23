package cse360project.pages;

import cse360project.Article;
import cse360project.utils.ApplicationStateManager;
import cse360project.utils.Level;
import cse360project.utils.PageManager;
import cse360project.utils.Role;
import cse360project.utils.SearchUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class ListPage implements Page {

    // Root element for this page
    StackPane root = new StackPane();

    // List to store all articles
    ArrayList<Article> allArticles = new ArrayList<>();

    // UI Elements for displaying feedback
    Label feedbackLabel = new Label(); // Label to display feedback messages
    ListView<Article> articleListView = new ListView<>(); // ListView for displaying articles

    // Constructor for the ListPage
    public ListPage() {
        // Sample hardcoded articles (add your actual data or database connection later)
        allArticles.add(new Article(1, Level.BEGINNER, new ArrayList<>(List.of("Group1", "Group2")), "Beginner Java Tutorial", "Learn Java from scratch", "Java, programming", "This article introduces Java basics.", new ArrayList<>()));
        allArticles.add(new Article(2, Level.INTERMEDIATE, new ArrayList<>(List.of("Group2", "Group3")), "Intermediate SQL Guide", "Deep dive into SQL queries", "SQL, database, joins", "This article explains intermediate SQL concepts.", new ArrayList<>()));
        allArticles.add(new Article(3, Level.BEGINNER, new ArrayList<>(List.of("Group1")), "Java Basics for Beginners", "A basic introduction to Java", "Java, basics", "This article explains basic concepts in Java.", new ArrayList<>()));
        allArticles.add(new Article(4, Level.ADVANCED, new ArrayList<>(List.of("Group3", "Group4")), "Advanced SQL Techniques", "Learn advanced SQL techniques", "SQL, advanced", "This article explores advanced SQL concepts.", new ArrayList<>()));
        allArticles.add(new Article(5, Level.BEGINNER, new ArrayList<>(List.of("Group1")), "Intro to Java", "A beginner's guide to Java", "Java, programming", "This article explains introductory Java concepts.", new ArrayList<>()));
        allArticles.add(new Article(6, Level.INTERMEDIATE, new ArrayList<>(List.of("Group2", "Group3")), "Intermediate Python Tutorial", "Enhance your Python skills", "Python, programming, intermediate", "This article explains intermediate concepts in Python.", new ArrayList<>()));
        allArticles.add(new Article(7, Level.EXPERT, new ArrayList<>(List.of("Group4")), "Expert SQL Performance Tuning", "Optimize SQL queries for performance", "SQL, performance, tuning", "This article discusses SQL performance tuning.", new ArrayList<>()));
        allArticles.add(new Article(8, Level.BEGINNER, new ArrayList<>(List.of("Group1", "Group2")), "Java Programming Basics", "A beginner’s guide to Java programming", "Java, basics, programming", "This article introduces Java basics.", new ArrayList<>()));
        allArticles.add(new Article(9, Level.INTERMEDIATE, new ArrayList<>(List.of("Group2")), "Python Functions and Modules", "An intermediate guide to Python functions", "Python, functions, modules", "This article covers Python functions and modules.", new ArrayList<>()));
        allArticles.add(new Article(10, Level.ADVANCED, new ArrayList<>(List.of("Group3", "Group4")), "Advanced Java Techniques", "Learn advanced Java techniques", "Java, advanced", "This article explains advanced techniques in Java.", new ArrayList<>()));

        // Create the UI
        setupUI();
    }

    /**
     * Set up the UI for the list page
     */
    private void setupUI() {
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
        ObservableList<String> groups = FXCollections.observableArrayList("All Groups", "Group1", "Group2", "Group3");
        ComboBox<String> groupComboBox = new ComboBox<>(groups);
        groupComboBox.setValue("All Groups");
        groupComboBox.setOnAction(e -> handleGroupFilter(groupComboBox.getValue()));

        feedbackLabel.setFont(new Font("Arial", 14));
        feedbackLabel.setStyle("-fx-text-fill: red;");

        // Increase the height of the ListView
        articleListView.setPrefHeight(500); // Increased from 300 to 500
        articleListView.setCellFactory(param -> new ListCell<Article>() {
            @Override
            protected void updateItem(Article article, boolean empty) {
                super.updateItem(article, empty);
                if (empty || article == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox articleBox = new VBox(5);
                    Label titleLabel = new Label(article.title + " - " + Level.levelToString(article.level));
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

        // Handle article click to switch to view page
        articleListView.setOnMouseClicked(event -> {
            Article selectedArticle = articleListView.getSelectionModel().getSelectedItem();
            if (selectedArticle != null) {
                // Get the ViewArticle page from the PageManager
                ViewArticle viewPage = (ViewArticle) PageManager.getPageByName("viewpage");

                // Set the selected article to be viewed
                viewPage.setViewingArticle(selectedArticle);

                // Switch to the viewpage
                PageManager.switchToPage("viewpage");
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
            EditArticle editPage = (EditArticle) PageManager.getPageByName("editarticle");

            // Set up the page with placeholders for creating a new article
            Article newArticle = new Article(-1, Level.BEGINNER, new ArrayList<>(), "Enter Title", "Enter description", "Enter keywords", "Enter article body", new ArrayList<>());
            editPage.setEditingArticle(newArticle); // Pass the new article to the edit page

            // Switch to the edit article page
            PageManager.switchToPage("editarticle");
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
            feedbackLabel.setText("Please enter a search query.");
            return;
        }

        ArrayList<Article> filteredArticles = SearchUtil.searchArticles(allArticles, query);
        if (filteredArticles.isEmpty()) {
            feedbackLabel.setText("No articles found for query: \"" + query + "\".");
        } else {
            feedbackLabel.setText(filteredArticles.size() + " articles found for query: " + query);
        }
        updateArticleList(filteredArticles);
    }

    private void handleGroupFilter(String group) {
        feedbackLabel.setText("Filtering by group: " + group);
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
    }
}
