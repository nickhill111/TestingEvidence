package org.nickhill111.taskManager.gui;

import org.nickhill111.common.data.Config;
import org.nickhill111.taskManager.data.AllTasks;
import org.nickhill111.taskManager.data.Comment;
import org.nickhill111.taskManager.data.Task;
import org.nickhill111.taskManager.data.TaskManagerComponents;
import org.nickhill111.taskManager.data.Tasks;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.nonNull;

public class TaskTable extends JTable {
    public static final int TASK_NAME_COLUMN = 0;
    public static final int PRIORITY_COLUMN = 1;
    public static final int BLOCKED_COLUMN = 2;
    public static final int DATE_CREATED_COLUMN = 3;
    public static final int TASK_COMMENTS_COLUMN = 4;
    private final TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();

    private final boolean areCellsEditable;

    public TaskTable(TaskTableModel model, boolean areCellsEditable) {
        super(model);
        this.areCellsEditable = areCellsEditable;

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowHeight(24);
        setupColumnModel();

        AllTasks existingTasks = Config.readTasks();
        if (nonNull(existingTasks)) {
            Tasks tasksToAdd = areCellsEditable ? existingTasks.getCurrentTasks() : existingTasks.getCompletedTasks();
            addTasksToModel(model, tasksToAdd);
        }

        ListSelectionModel selectionModel = getSelectionModel();

        selectionModel.addListSelectionListener(e -> {
            int selectedRow = getSelectedRow();

            if (selectedRow != -1) {
                List<Comment> comments = getComments(selectedRow);
                taskManagerComponents.refreshInfoPanel(comments, selectedRow);
            }
        });

        putClientProperty("terminateEditOnFocusLost", true);
    }

    private void addTasksToModel(TaskTableModel model, Tasks tasks) {
        if (nonNull(tasks)) {
            for (Task task : tasks) {
                model.addRow(task.taskName(), task.priority(), task.isBlocked(), task.dateCreated(), task.comments());
            }
        }
    }

    private void setupColumnModel() {
        TableColumnModel columnModel = getColumnModel();
        TableColumn priorityColumn = columnModel.getColumn(PRIORITY_COLUMN);
        priorityColumn.setCellRenderer(new PriorityRenderer());
        priorityColumn.setCellEditor(new PriorityCellEditor());

        getColumnModel().getColumn(TASK_COMMENTS_COLUMN).setMinWidth(0);
        getColumnModel().getColumn(TASK_COMMENTS_COLUMN).setMaxWidth(0);
        getColumnModel().getColumn(TASK_COMMENTS_COLUMN).setWidth(0);

    }

    public String getTaskName(int row) {
        return getValueAt(row, TASK_NAME_COLUMN).toString();
    }

    public String getPriority(int row) {
        Object object = getValueAt(row, PRIORITY_COLUMN);
        return getPriority(object);
    }
    public String getPriority(Object object) {
        if (object instanceof ComboBox comboBox) {
            return String.valueOf(comboBox.getSelectedItem());
        }

        return (String) object;
    }

    public boolean getIsBlocked(int row) {
        Object object = getValueAt(row, BLOCKED_COLUMN);
        return getIsBlocked(object);
    }

    public boolean getIsBlocked(Object object) {
        return (boolean) object;
    }


    public List<Comment> getComments(int row) {
        if (row < 0 || row > getRowCount() - 1) {
            return new LinkedList<>();
        }

        Object value = getValueAt(row, TASK_COMMENTS_COLUMN);
        return getComments(value);
    }

    public List<Comment> getComments(Object value) {
        if (value instanceof List<?> list) {
            return new LinkedList<>(list.stream().map(ob -> (Comment) ob).toList());
        }

        return new LinkedList<>();
    }

    public LocalDate getDateCreated(int row) {
        Object dateAsObject = getValueAt(row, DATE_CREATED_COLUMN);
        return getDateCreated(dateAsObject);
    }

    public LocalDate getDateCreated(Object dateAsObject) {
        return nonNull(dateAsObject) ? LocalDate.parse(dateAsObject.toString()) : null;
    }

    public void removeComment(int row, Comment comment) {
        List<Comment> comments = getComments(row);
        comments.remove(comment);
        setValueAt(comments, row, TASK_COMMENTS_COLUMN);
    }

    public void addComment(int row, Comment comment) {
        List<Comment> comments = getComments(row);
        comments.add(comment);
        setValueAt(comments, row, TASK_COMMENTS_COLUMN);
    }

    public void swapTasksInTable(int selectedIndex, int positionToSwapWith) {
        String taskName = getTaskName(selectedIndex);
        String taskPriority = getPriority(selectedIndex);
        boolean isBlocked = getIsBlocked(selectedIndex);
        LocalDate dateCreated = getDateCreated(selectedIndex);
        List<Comment> taskText = getComments(selectedIndex);

        TaskTableModel model = getModel();
        model.removeRow(selectedIndex);
        model.insertRow(positionToSwapWith, taskName, taskPriority, isBlocked, dateCreated, taskText);

        selectRow(positionToSwapWith);
    }

    public TaskTableModel getModel() {
        return (TaskTableModel) super.getModel();
    }

    public void selectCurrentRow() {
        selectRow(getSelectedRow());
    }

    public void selectRow(int row) {
        if (row >= 0 && row < getRowCount()) {
            setRowSelectionInterval(row, row);
        }
    }

    public Tasks getTasks() {
        Tasks tasks = new Tasks();

        tasks.addAll(getModel().getDataVector().stream()
            .map(vector -> new Task(vector.get(TASK_NAME_COLUMN).toString(),
                getPriority(vector.get(PRIORITY_COLUMN)),
                getIsBlocked(vector.get(BLOCKED_COLUMN)),
                getDateCreated(vector.get(DATE_CREATED_COLUMN)),
                getComments(vector.get(TASK_COMMENTS_COLUMN)))).toList());

        return tasks;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return areCellsEditable && column != DATE_CREATED_COLUMN && column != TASK_COMMENTS_COLUMN;
    }
}
