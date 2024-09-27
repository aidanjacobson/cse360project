package cse360project.utils;

import java.util.HashMap;

import cse360project.pages.Page;
import javafx.scene.Scene;

public class PageManager {
    static Scene primaryScene;
    static Page activePage;
    public static void switchToPage(String name) {
        Page page = pageNames.get(name);
        if (page == null) {
            System.err.println("Page " + name + " does not exist");
            return;
        }
        switchToPage(page);
        activePage = page;
        page.onPageOpen();
    }

    public static Page getPageByName(String name) {
        return pageNames.get(name);
    }

    public static void switchToPage(Page page) {
        primaryScene.setRoot(page.getRoot());
    }

    static HashMap<String, Page> pageNames = new HashMap<>();
    public static void registerPage(String name, Page page) {
        pageNames.put(name, page);
    }

    public static void setPrimaryScene(Scene scene) {
        primaryScene = scene;
    }

    public static Page getActivePage() {
        return activePage;
    }
}
