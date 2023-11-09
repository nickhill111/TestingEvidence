package org.nickhill111.taskManager.service;

import org.nickhill111.common.util.DialogUtils;
import org.nickhill111.taskManager.data.TaskManagerComponents;
import org.nickhill111.taskManager.gui.TaskTable;
import org.nickhill111.taskManager.gui.TaskTableModel;

import javax.swing.*;
import java.time.LocalDate;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;
import static org.nickhill111.taskManager.data.Priority.MEDIUM;

public class FunctionsService {

    private final TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();

    public void addTask() {
        String newTaskName = DialogUtils.askForTaskName();

        if (isNotEmpty(newTaskName)) {
            TaskTableModel tableModel = taskManagerComponents.getCurrentTaskTable().getModel();
            tableModel.addRow(newTaskName, MEDIUM.getValue(), false, LocalDate.now(), "");
            taskManagerComponents.getCurrentTaskTable().selectRow(tableModel.getRowCount() - 1);
            taskManagerComponents.getTasksTabbedPane().setSelectedIndex(0);
        }
    }

    public void removeTasks() {
        TaskTable taskTable = taskManagerComponents.getCurrentTaskTable();

        boolean isCurrentTasks = taskManagerComponents.getTasksTabbedPane().isCurrentTasksVisible();

        if (!isCurrentTasks) {
            taskTable = taskManagerComponents.getCompletedTaskTable();
        }

        int[] selectedRows = taskTable.getSelectedRows();

        if (selectedRows.length >= 1) {
            TaskTableModel tableModel = taskTable.getModel();

            for (int i = selectedRows.length - 1; i >= 0; i--) {
                int selectedRow = selectedRows[i];

                if (selectedRow >= 0) {
                    if (isCurrentTasks) {
                        taskManagerComponents.getCompletedTaskTable().getModel().addRow(taskTable.getTaskName(selectedRow),
                            taskTable.getPriority(selectedRow), taskTable.getIsBlocked(selectedRow),
                            taskTable.getDateCreated(selectedRow), taskTable.getText(selectedRow));
                    }

                    tableModel.removeRow(selectedRow);

                    if (tableModel.getRowCount() > selectedRow) {
                        taskTable.selectCurrentRow();
                    } else if (selectedRow >= 1) {
                        taskTable.selectRow(selectedRow - 1);
                    }
                }
            }

            JTextArea infoTextArea = taskManagerComponents.getInfoTextArea();

            int selectedRow = taskTable.getSelectedRow();
            boolean isRowSelected = selectedRow >= 0;
            infoTextArea.setText(isRowSelected ? taskTable.getText(selectedRow) : null);
            infoTextArea.setEditable(isRowSelected);
        }
    }

    public void moveTaskUp() {
        moveSelectedTask(-1);
    }

    public void moveTaskDown() {
        moveSelectedTask(1);

    }

    private void moveSelectedTask(int positionToMoveBy) {
        if (isNotEmpty(taskManagerComponents.getFilter().getText())) {
            DialogUtils.cantMoveTasksDueToFilter();
            return;
        }

        TaskTable taskTable = taskManagerComponents.getCurrentTaskTable();

        int selectedIndex = taskTable.getSelectedRow();
        int positionToSwapWith = selectedIndex + positionToMoveBy;

        if (!taskManagerComponents.getTasksTabbedPane().isCurrentTasksVisible() || areMultipleTasksSelected()
            || taskTable.getRowCount() <= positionToSwapWith || positionToSwapWith < 0) {
            return;
        }

        JTextArea infoTextArea = taskManagerComponents.getInfoTextArea();
        taskTable.setText(selectedIndex, infoTextArea.getText());

        taskTable.swapTasksInTable(selectedIndex, positionToSwapWith);
    }

    private boolean areMultipleTasksSelected() {
        TaskTable taskTable = taskManagerComponents.getCurrentTaskTable();
        int[] selectedRows = taskTable.getSelectedRows();

        if (selectedRows.length > 1) {
            DialogUtils.cantMoveTasks();
        }

        return selectedRows.length != 1;
    }
}
