package org.nickhill111.taskManager.gui;

import org.nickhill111.common.gui.GenericScrollPane;
import org.nickhill111.taskManager.data.TaskManagerComponents;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.List;

public class TasksTabbedPane extends JTabbedPane implements ChangeListener {
    public static final int TASK_NAME_COLUMN = 0;
    public static final int PRIORITY_COLUMN = 1;
    public static final int BLOCKED_COLUMN = 2;
    public static final int DATE_CREATED_COLUMN = 3;
    public static final int TASK_COMMENTS_COLUMN = 4;

    TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();

    public TasksTabbedPane() {
        super();
        addTasksTab();
        addHistoryTab();

        taskManagerComponents.setTasksTabbedPane(this);

        addChangeListener(this);
    }

    private void addTasksTab() {
        TaskTable taskTable = new TaskTable(new TaskTableModel(), true);
        taskManagerComponents.setCurrentTaskTable(taskTable);

        GenericScrollPane scrollPane = new GenericScrollPane(taskTable);
        scrollPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        addTab("Current Tasks", scrollPane);
    }

    private void addHistoryTab() {
        TaskTable taskTable = new TaskTable(new TaskTableModel(), false);
        taskManagerComponents.setCompletedTaskTable(taskTable);

        GenericScrollPane scrollPane = new GenericScrollPane(taskTable);
        scrollPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        addTab("Completed Tasks", scrollPane);
    }

    public boolean isCurrentTasksVisible() {
        return getSelectedIndex() == 0;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        taskManagerComponents.getCompletedTaskTable().clearSelection();
        taskManagerComponents.getCurrentTaskTable().clearSelection();

        taskManagerComponents.refreshInfoPanel(List.of(), -1);
    }
}
