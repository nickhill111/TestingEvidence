package org.nickhill111.taskManager.gui;

import org.nickhill111.common.gui.GenericScrollPane;

import javax.swing.*;
import java.awt.*;

import static org.nickhill111.common.util.GuiUtils.PHOTO_SIZE;

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
        TasksPanel tasksPanel = new TasksPanel();
        InfoPanel infoPanel = new InfoPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            new GenericScrollPane<>(tasksPanel),
            new GenericScrollPane<>(infoPanel));
        setUpSplitPane(splitPane);

        add(splitPane, BorderLayout.CENTER);
    }

    private void setUpSplitPane(JSplitPane splitPane) {
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerSize(15);
        splitPane.getLeftComponent().setMinimumSize(new Dimension(PHOTO_SIZE + 50, PHOTO_SIZE + 50));
        splitPane.getRightComponent().setMinimumSize(new Dimension());
        splitPane.setDividerLocation(0.5);
    }
}
