package org.nickhill111.taskManager.gui;

import org.nickhill111.common.data.Config;
import org.nickhill111.taskManager.data.Task;
import org.nickhill111.taskManager.data.TaskManagerComponents;
import org.nickhill111.common.util.DialogUtils;
import org.nickhill111.taskManager.data.Tasks;

import javax.swing.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isNotEmpty;
import static org.nickhill111.common.data.Icons.ADD_ICON;
import static org.nickhill111.common.data.Icons.DELETE_ICON;
import static org.nickhill111.common.data.Icons.DOWN_ICON;
import static org.nickhill111.common.data.Icons.SAVE_ICON;
import static org.nickhill111.common.data.Icons.UP_ICON;

public class TaskToolBar extends JToolBar {
    private final Config config = Config.getInstance();
    private final TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();

    public TaskToolBar() {
        addAddButton();
        addRemoveButton();
        addSeparator();
        addUpButton();
        addDownButton();
        addSeparator();
        addSaveButton();
    }

    private void addAddButton() {
        JButton addButton = new JButton(ADD_ICON);
        addButton.setToolTipText("Add task to list");

        addButton.addActionListener(e -> {
            String newTaskName = DialogUtils.askForTaskName();

            if (isNotEmpty(newTaskName)) {
                DefaultListModel<String> demoList = taskManagerComponents.getDemoList();
                demoList.addElement(newTaskName);
                taskManagerComponents.getTaskList().setSelectedValue(newTaskName, true);

                config.getConfigDetails().getTaskManagerConfigDetails().getTasks().put(demoList.getSize() - 1, new Task(newTaskName, ""));
                config.saveConfig();
            }
        });

        add(addButton);
    }

    private void addRemoveButton() {
        JButton removeButton = new JButton(DELETE_ICON);
        removeButton.setToolTipText("Remove selected task");

        removeButton.addActionListener(event -> {
            JList<String> taskList = taskManagerComponents.getTaskList();
            int[] selectedIndices = taskList.getSelectedIndices();

            if (selectedIndices.length >= 1) {
                Tasks tasks = config.getConfigDetails().getTaskManagerConfigDetails().getTasks();
                DefaultListModel<String> demoList = taskManagerComponents.getDemoList();

                taskManagerComponents.setRemovingTask(true);
                for (int i = selectedIndices.length - 1; i >= 0; i--) {
                    int selectedIndex = selectedIndices[i];

                    if (selectedIndex >= 0) {
                        tasks.remove(selectedIndex);

                        for (int e = selectedIndex + 1; e <= tasks.size(); e++) {
                            Task task = tasks.get(e);

                            if (nonNull(task)) {
                                tasks.remove(e);
                                tasks.put(e - 1, task);
                            }
                        }

                        demoList.removeElementAt(selectedIndex);

                        if (demoList.getSize() > selectedIndex) {
                            taskList.setSelectedIndex(selectedIndex);
                        } else if (selectedIndex >= 1) {
                            taskList.setSelectedIndex(selectedIndex - 1);
                        }
                    }
                }

                JTextArea infoTextArea = taskManagerComponents.getInfoTextArea();
                Task task = tasks.get(taskList.getSelectedIndex());
                infoTextArea.setText(isNull(task) ? null : task.taskText());

                taskManagerComponents.setRemovingTask(false);
                config.saveConfig();
            }

        });

        add(removeButton);
    }

    private void addUpButton() {
        JButton upButton = new JButton(UP_ICON);
        upButton.setToolTipText("Up");

        upButton.addActionListener(e -> moveSelectedTask(-1));

        add(upButton);
    }

    private void addDownButton() {
        JButton downButton = new JButton(DOWN_ICON);
        downButton.setToolTipText("Down");

        downButton.addActionListener(e -> moveSelectedTask(1));

        add(downButton);
    }

    private void moveSelectedTask(int positionToMoveBy) {
        JList<String> taskList = taskManagerComponents.getTaskList();
        int[] selectedIndices = taskList.getSelectedIndices();

        if (selectedIndices.length > 1) {
            DialogUtils.cantMoveTasks();
            return;
        }

        Tasks tasks = config.getConfigDetails().getTaskManagerConfigDetails().getTasks();
        int selectedIndex = selectedIndices[0];
        int positionToSwapWith = selectedIndex + positionToMoveBy;

        if (!tasks.containsKey(positionToSwapWith)) {
            return;
        }

        taskManagerComponents.setRemovingTask(true);
        swapTasksInConfig(tasks, selectedIndex, positionToSwapWith);
        swapTasksInDemoList(selectedIndex, positionToSwapWith);

        taskList.setSelectedIndex(positionToSwapWith);
        taskManagerComponents.setRemovingTask(false);

        config.saveConfig();
    }

    private void swapTasksInConfig(Tasks tasks, int selectedIndex, int positionToSwapWith) {
        Task taskToSwapWith = tasks.get(positionToSwapWith);
        tasks.put(positionToSwapWith, tasks.get(selectedIndex));
        tasks.put(selectedIndex, taskToSwapWith);
    }

    private void swapTasksInDemoList(int selectedIndex, int positionToSwapWith) {
        DefaultListModel<String> demoList = taskManagerComponents.getDemoList();

        String taskToSwapWithInList = demoList.get(positionToSwapWith);
        demoList.removeElementAt(positionToSwapWith);
        demoList.add(selectedIndex, taskToSwapWithInList);
    }

    private void addSaveButton() {
        JButton saveButton = new JButton(SAVE_ICON);
        saveButton.setToolTipText("Save");

        saveButton.addActionListener(e -> config.saveConfig());

        add(saveButton);
    }
}
