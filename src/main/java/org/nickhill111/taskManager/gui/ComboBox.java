package org.nickhill111.taskManager.gui;

import javax.swing.*;
import java.util.Objects;

public class ComboBox extends JComboBox<String> {

    public ComboBox() {
        super();
    }

    public ComboBox(String[] allValues) {
        super(allValues);
    }

    @Override
    public String toString() {
        return Objects.requireNonNull(getSelectedItem()).toString();
    }
}
