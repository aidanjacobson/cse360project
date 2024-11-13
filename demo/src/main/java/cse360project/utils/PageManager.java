package cse360project.utils;

import java.util.HashMap;

import cse360project.pages.Page;
import javafx.scene.Scene;

public class PageManager {
    // keep a reference to the primaryScene so we can set the root node
    public static Scene primaryScene;

    // keep a reference to the active page, incase another class asks for it
    static Page activePage;

    /**
     * Switch to the page with registered ID corresponding to name.
     * @param name The id of the page to switch to
     */
    public static void switchToPage(String name) {
        // find the page corresponding to the id
        Page page = pageNames.get(name);
        if (page == null) {
            System.err.println("Page " + name + " does not exist");
            return;
        }
        // switch to that page, update the reference to active page
        switchToPage(page);
        activePage = page;

        // call the onPageOpen of the page we switched to
        page.onPageOpen();
    }

    /**
     * Get a Page reference by id
     * @param name the id to search for e.g. "login"
     * @return the Page associated with the id
     */
    public static Page getPageByName(String name) {
        return pageNames.get(name);
    }

    /**
     * Switch to page by page reference
     * @param page
     */
    public static void switchToPage(Page page) {
        primaryScene.setRoot(page.getRoot());
    }

    // a map containing id/page pairs
    static HashMap<String, Page> pageNames = new HashMap<>();

    /**
     * Register a page in the PageManager.
     * @param name the new id of the page
     * @param page the page to associate with id
     */
    public static void registerPage(String name, Page page) {
        pageNames.put(name, page);
    }

    /**
     * Allow setting the primary scene from main class
     * @param scene the primaryScene
     */
    public static void setPrimaryScene(Scene scene) {
        primaryScene = scene;
    }

    /**
     * Get the active page
     * @return the page object that is currently active
     */
    public static Page getActivePage() {
        return activePage;
    }
    /**
     * Get the active page as a string
     * @return the page object that is currently active as a string
     */
	public static String getCurrentPageId() {
		return activePage.toString();
	}
   
}
