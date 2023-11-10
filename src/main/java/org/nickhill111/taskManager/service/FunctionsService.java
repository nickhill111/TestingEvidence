package org.nickhill111.taskManager.service;

import org.nickhill111.common.util.DialogUtils;
import org.nickhill111.taskManager.data.Comment;
import org.nickhill111.taskManager.data.TaskManagerComponents;
import org.nickhill111.taskManager.gui.TaskTable;
import org.nickhill111.taskManager.gui.TaskTableModel;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;
import static org.nickhill111.taskManager.data.Priority.MEDIUM;

public class FunctionsService {

    private final TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();

    public void addTask() {
        String newTaskName = DialogUtils.askForTaskName();

        if (isNotEmpty(newTaskName)) {
            TaskTableModel tableModel = taskManagerComponents.getCurrentTaskTable().getModel();
            tableModel.addRow(newTaskName, MEDIUM.getValue(), false, LocalDate.now(), new LinkedList<>());
            taskManagerComponents.getFilter().setText("");
            taskManagerComponents.getCurrentTaskTable().selectRow(tableModel.getRowCount() - 1);
            taskManagerComponents.getTasksTabbedPane().setSelectedIndex(0);
        }
    }

    public void removeTask() {
        TaskTable taskTable = taskManagerComponents.getCurrentTaskTable();

        boolean isCurrentTasks = taskManagerComponents.getTasksTabbedPane().isCurrentTasksVisible();

        if (!isCurrentTasks) {
            taskTable = taskManagerComponents.getCompletedTaskTable();
        } else if (taskTable.getEditingRow() >= 0) {
            taskTable.getCellEditor().stopCellEditing();
        }

        int selectedRow = taskTable.getSelectedRow();

        if (selectedRow >= 0) {
            if (isCurrentTasks) {
                taskManagerComponents.getCompletedTaskTable().getModel().addRow(taskTable.getTaskName(selectedRow),
                    taskTable.getPriority(selectedRow), taskTable.getIsBlocked(selectedRow),
                    taskTable.getDateCreated(selectedRow), taskTable.getComments(selectedRow));
            }

            taskTable.getModel().removeRow(taskTable.convertRowIndexToModel(selectedRow));

            if (selectedRow >= 1) {
                selectedRow--;
            }

            taskTable.selectRow(selectedRow);
        }

        selectedRow = taskTable.getSelectedRow();

        List<Comment> comments = taskTable.getComments(selectedRow);
        taskManagerComponents.refreshInfoPanel(comments, selectedRow);
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

        if (taskTable.getEditingRow() >= 0) {
            taskTable.getCellEditor().stopCellEditing();
        }

        int selectedIndex = taskTable.getSelectedRow();
        int positionToSwapWith = selectedIndex + positionToMoveBy;

        if (!taskManagerComponents.getTasksTabbedPane().isCurrentTasksVisible()
            || taskTable.getRowCount() <= positionToSwapWith || positionToSwapWith < 0) {
            return;
        }

        taskTable.swapTasksInTable(selectedIndex, positionToSwapWith);
    }
}
