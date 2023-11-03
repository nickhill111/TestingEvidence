package org.nickhill111.taskManager.gui;

import org.nickhill111.taskManager.data.TaskManagerComponents;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class CompletedTasksPanel extends JPanel {

    public CompletedTasksPanel() {
        super(new BorderLayout());

        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        TaskTable taskTable = new TaskTable(new TaskTableModel(), false);

        TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();
        taskManagerComponents.setCompletedTaskTable(taskTable);

        add(taskTable, BorderLayout.CENTER);
    }
}
