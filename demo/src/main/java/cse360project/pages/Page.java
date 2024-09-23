package cse360project.pages;

import javafx.scene.layout.StackPane;

public interface Page {
    public StackPane getRoot();
    public void onPageOpen();
}
