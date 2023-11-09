package org.nickhill111.common.gui;

import lombok.Getter;

import javax.swing.*;

@Getter
public class GenericScrollPane<T extends JPanel> extends JScrollPane {
    private final T panel;

    public GenericScrollPane(T panel) {
        super(panel);
        this.panel = panel;

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
