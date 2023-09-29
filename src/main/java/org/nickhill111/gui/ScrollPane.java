package org.nickhill111.gui;

import lombok.Getter;

import javax.swing.*;

@Getter
public class ScrollPane extends JScrollPane {
    private final ScenarioPanel scenarioPanel;

    public ScrollPane(ScenarioPanel scenarioPanel) {
        super(scenarioPanel);
        this.scenarioPanel = scenarioPanel;

        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    }
}
