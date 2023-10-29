package org.nickhill111.taskManager.gui;

import org.nickhill111.common.data.Config;
import org.nickhill111.taskManager.data.Task;
import org.nickhill111.taskManager.data.TaskManagerComponents;
import org.nickhill111.taskManager.data.Tasks;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.util.List;

import static java.util.Objects.nonNull;

public class TasksPanel extends JPanel {
    private final TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();
    private final Config config = Config.getInstance();

    private String previousSelectedValue = null;
    private Integer previousSelectedIndex = null;

    public TasksPanel() {
        super(new BorderLayout());

        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        DefaultListModel<String> demoList = new DefaultListModel<>();
        taskManagerComponents.setDemoList(demoList);

        JList<String> taskList = new JList<>(demoList);
        taskManagerComponents.setTaskList(taskList);

        List<String> existingTasks = config.getConfigDetails().getTaskManagerConfigDetails().getTasks().getAllTaskNames();
        demoList.addAll(existingTasks);

        taskList.addListSelectionListener(e -> {
            String selectedValue = taskList.getSelectedValue();
            int selectedIndex = taskList.getSelectedIndex();

            if (!taskManagerComponents.isRemovingTask()) {
                Tasks tasks = config.getConfigDetails().getTaskManagerConfigDetails().getTasks();

                JTextArea infoTextArea = taskManagerComponents.getInfoTextArea();

                if (nonNull(previousSelectedValue) && tasks.containsKey(previousSelectedIndex)) {
                    tasks.put(previousSelectedIndex, new Task(previousSelectedValue, infoTextArea.getText()));
                    config.saveConfig();
                }

                infoTextArea.setText(tasks.get(selectedIndex).taskText());

                infoTextArea.setEditable(nonNull(selectedValue));
            }

            previousSelectedValue = selectedValue;
            previousSelectedIndex = selectedIndex;
        });

        add(taskList, BorderLayout.CENTER);
    }
}
