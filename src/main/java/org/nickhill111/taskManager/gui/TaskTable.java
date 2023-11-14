package org.nickhill111.taskManager.gui;

import org.nickhill111.common.data.Config;
import org.nickhill111.common.data.TaskManagerConfigDetails;
import org.nickhill111.taskManager.data.AllTasks;
import org.nickhill111.taskManager.data.Comment;
import org.nickhill111.taskManager.data.Task;
import org.nickhill111.taskManager.data.TaskManagerComponents;
import org.nickhill111.taskManager.data.Tasks;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.nickhill111.taskManager.gui.TasksTabbedPane.BLOCKED_COLUMN;
import static org.nickhill111.taskManager.gui.TasksTabbedPane.DATE_CREATED_COLUMN;
import static org.nickhill111.taskManager.gui.TasksTabbedPane.PRIORITY_COLUMN;
import static org.nickhill111.taskManager.gui.TasksTabbedPane.TASK_COMMENTS_COLUMN;
import static org.nickhill111.taskManager.gui.TasksTabbedPane.TASK_NAME_COLUMN;

public class TaskTable extends JTable {
    private final TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();
    private final Config config = Config.getInstance();

    private final boolean areCellsEditable;

    public TaskTable(boolean areCellsEditable) {
        super(new TaskTableModel());
        this.areCellsEditable = areCellsEditable;

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowHeight(24);
        setupColumnModel();

        AllTasks existingTasks = Config.readTasks();
        if (nonNull(existingTasks)) {
            Tasks tasksToAdd = areCellsEditable ? existingTasks.getCurrentTasks() : existingTasks.getCompletedTasks();
            addTasksToModel(getModel(), tasksToAdd);
        }

        ListSelectionModel selectionModel = getSelectionModel();

        selectionModel.addListSelectionListener(e -> {
            int selectedRow = getSelectedRow();

            if (selectedRow != -1) {
                List<Comment> comments = getModel().getComments(selectedRow);
                taskManagerComponents.refreshInfoPanel(comments, selectedRow);
            }
        });

        setTableHeader();
        orderColumnsFromConfig();

        putClientProperty("terminateEditOnFocusLost", true);
    }

    private void orderColumnsFromConfig() {
        TaskManagerConfigDetails taskManagerConfig = config.getConfigDetails().getTaskManagerConfigDetails();

        String[] savedColumnOrder = areCellsEditable ? taskManagerConfig.getCurrentTasksOrder() : taskManagerConfig.getCompletedTasksOrder();
        if (nonNull(savedColumnOrder)) {
            for (int i = 0; i < savedColumnOrder.length; i++) {
                int columnIndex = getColumnIndex(savedColumnOrder[i]);
                if (columnIndex >= 0) {
                    moveColumn(columnIndex, i);
                }
            }
        }
    }

    private int getColumnIndex(String header) {
        for (int i=0; i < getColumnCount(); i++) {
            if (getColumnName(i).equals(header)) return i;
        }
        return -1;
    }

    private void setTableHeader() {
        setTableHeader(new JTableHeader(getColumnModel()) {
            @Override
            public void setDraggedColumn(TableColumn column) {
                boolean finished = nonNull(draggedColumn) && isNull(column);
                super.setDraggedColumn(column);

                if (finished) {
                    String[] columnOrder = new String[getColumnCount()];
                    for (int i = 0; i < getColumnCount(); i++) {
                        columnOrder[i] = getColumnName(i);
                    }

                    if (areCellsEditable) {
                        config.getConfigDetails().getTaskManagerConfigDetails().setCurrentTasksOrder(columnOrder);
                    } else {
                        config.getConfigDetails().getTaskManagerConfigDetails().setCompletedTasksOrder(columnOrder);
                    }

                    config.saveTaskManagerFrameConfigDetails();
                }
            }
        });
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

    public void swapTasksInTable(int selectedIndex, int positionToSwapWith) {
        TaskTableModel model = getModel();
        String taskName = model.getTaskName(selectedIndex);
        String taskPriority = model.getPriority(selectedIndex);
        boolean isBlocked = model.getIsBlocked(selectedIndex);
        LocalDate dateCreated = model.getDateCreated(selectedIndex);
        List<Comment> taskText = model.getComments(selectedIndex);

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
        TaskTableModel model = getModel();

        tasks.addAll(model.getDataVector().stream()
            .map(vector -> new Task(vector.get(TASK_NAME_COLUMN).toString(),
                model.getPriority(vector.get(PRIORITY_COLUMN)),
                model.getIsBlocked(vector.get(BLOCKED_COLUMN)),
                model.getDateCreated(vector.get(DATE_CREATED_COLUMN)),
                model.getComments(vector.get(TASK_COMMENTS_COLUMN)))).toList());

        return tasks;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        String columnName = getColumnName(column);
        return areCellsEditable && !columnName.equals("Date Created") && !columnName.equals("Text");
    }
}
