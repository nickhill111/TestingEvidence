package org.nickhill111.common.gui;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class GenericScrollPane extends JScrollPane {
    private final Component component;

    public GenericScrollPane(Component component) {
        super(component);
        this.component = component;

        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        getVerticalScrollBar().setUnitIncrement(30);
        getHorizontalScrollBar().setUnitIncrement(30);
    }

    public void scrollToTheRight() {
        JScrollBar scrollBar = getHorizontalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
    }
}
