package gui;

import com.vaadin.ui.UI;

public interface BasicWindow {
    BasicWindow parentWindow = null;

    public void onNotify(String message);

    UI getCurrent();
}
