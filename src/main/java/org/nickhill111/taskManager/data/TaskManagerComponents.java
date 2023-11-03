package org.nickhill111.taskManager.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nickhill111.taskManager.gui.TaskTable;
import org.nickhill111.taskManager.gui.TasksTabbedPane;

import javax.swing.*;

@Getter
@Setter
@NoArgsConstructor
public class TaskManagerComponents {
    private static TaskManagerComponents INSTANCE;

    private TasksTabbedPane tasksTabbedPane;
    private TaskTable currentTaskTable;
    private TaskTable completedTaskTable;
    private JTextArea infoTextArea;
    private JTextField filter;
    private JFrame frame;

    public static TaskManagerComponents getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TaskManagerComponents();
        }

        return INSTANCE;
    }
}
