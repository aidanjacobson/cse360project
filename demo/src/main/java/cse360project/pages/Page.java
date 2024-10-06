package cse360project.pages;

import javafx.scene.layout.Pane;

/**
 * Interface for a page in the application
 */
public interface Page {
    public Pane getRoot();
    public void onPageOpen();
}
