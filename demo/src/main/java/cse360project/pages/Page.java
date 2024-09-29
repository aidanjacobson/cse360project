package cse360project.pages;

import javafx.scene.layout.Pane;

public interface Page {
    public Pane getRoot();
    public void onPageOpen();
}
