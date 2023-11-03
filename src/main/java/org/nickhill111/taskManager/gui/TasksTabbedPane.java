package org.nickhill111.taskManager.gui;

import org.nickhill111.common.gui.GenericScrollPane;
import org.nickhill111.taskManager.data.TaskManagerComponents;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TasksTabbedPane extends JTabbedPane implements ChangeListener {

    TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();

    public TasksTabbedPane() {
        super();
        addTasksTab();
        addHistoryTab();

        taskManagerComponents.setTasksTabbedPane(this);

        addChangeListener(this);
    }

    private void addTasksTab() {
        CurrentTasksPanel currentTasksPanel = new CurrentTasksPanel();
        addTab("Current Tasks", new GenericScrollPane<>(currentTasksPanel));
    }

    private void addHistoryTab() {
        CompletedTasksPanel completedTasksPanel = new CompletedTasksPanel();
        addTab("Completed Tasks", new GenericScrollPane<>(completedTasksPanel));
    }

    public boolean isCurrentTasksVisible() {
        return getSelectedIndex() == 0;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        taskManagerComponents.getCompletedTaskTable().clearSelection();
        taskManagerComponents.getCurrentTaskTable().clearSelection();

        JTextArea infoTextArea = taskManagerComponents.getInfoTextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setText("");
    }
}
