package org.nickhill111.taskManager.gui;

import org.nickhill111.common.gui.GenericScrollPane;

import javax.swing.*;
import java.awt.*;

public class MainTaskManagerPanel extends JPanel {

    public MainTaskManagerPanel() {
        super(new BorderLayout());
        
        addToolBar();
        addTaskAndInfoSplitPanel();
    }

    private void addToolBar() {
        TaskToolBar toolBar = new TaskToolBar();
        add(toolBar, BorderLayout.PAGE_START);
    }

    private void addTaskAndInfoSplitPanel() {
        CommentsPanel commentsPanel = new CommentsPanel();
        NewCommentPanel newCommentPanel = new NewCommentPanel();
        JSplitPane infoSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new GenericScrollPane(commentsPanel), new GenericScrollPane(newCommentPanel));
        setUpSplitPane(infoSplitPane);
        infoSplitPane.setResizeWeight(0.75);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            new TasksTabbedPane(),
            infoSplitPane);
        setUpSplitPane(splitPane);

        add(splitPane, BorderLayout.CENTER);
    }

    private void setUpSplitPane(JSplitPane splitPane) {
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerSize(15);
        splitPane.setResizeWeight(0.2);
        splitPane.setDividerLocation(0.5);
    }
}
