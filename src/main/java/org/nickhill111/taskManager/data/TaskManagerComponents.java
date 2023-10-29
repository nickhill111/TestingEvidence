package org.nickhill111.taskManager.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;

@Getter
@Setter
@NoArgsConstructor
public class TaskManagerComponents {
    private static TaskManagerComponents INSTANCE;

    private DefaultListModel<String> demoList;
    private JList<String> taskList;
    private JTextArea infoTextArea;
    private JFrame frame;
    private boolean isRemovingTask = false;

    public static TaskManagerComponents getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TaskManagerComponents();
        }

        return INSTANCE;
    }
}
