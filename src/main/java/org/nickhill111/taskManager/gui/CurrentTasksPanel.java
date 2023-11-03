package org.nickhill111.taskManager.gui;

import org.nickhill111.taskManager.data.TaskManagerComponents;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class CurrentTasksPanel extends JPanel {

    public CurrentTasksPanel() {
        super(new BorderLayout());

        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        TaskTable taskTable = new TaskTable(new TaskTableModel(), true);

        TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();
        taskManagerComponents.setCurrentTaskTable(taskTable);

        add(taskTable, BorderLayout.CENTER);
    }
}
