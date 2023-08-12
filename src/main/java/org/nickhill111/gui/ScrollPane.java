package org.nickhill111.gui;

import javax.swing.*;

public class ScrollPane extends JScrollPane {
    PreviewPanel previewPanel;

    public ScrollPane(PreviewPanel previewPanel) {
        super(previewPanel);
        this.previewPanel = previewPanel;

        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    }

    public PreviewPanel getPreviewPanel() {
        return previewPanel;
    }
}
