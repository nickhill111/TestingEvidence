package org.nickhill111.taskManager.gui;

import org.nickhill111.taskManager.data.Comment;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.nickhill111.taskManager.gui.TasksTabbedPane.BLOCKED_COLUMN;
import static org.nickhill111.taskManager.gui.TasksTabbedPane.DATE_CREATED_COLUMN;
import static org.nickhill111.taskManager.gui.TasksTabbedPane.PRIORITY_COLUMN;
import static org.nickhill111.taskManager.gui.TasksTabbedPane.TASK_COMMENTS_COLUMN;
import static org.nickhill111.taskManager.gui.TasksTabbedPane.TASK_NAME_COLUMN;

public class TaskTableModel extends DefaultTableModel {

    public TaskTableModel() {
        super(new Object[]{"Task", "Priority", "Blocked", "Date Created", "Text"}, 0);
    }

    public void addRow(String task, String priority, boolean isBlocked, LocalDate dateCreated, List<Comment> text) {
        super.addRow(new Object[]{task, createPriorityComboBox(priority), isBlocked, dateCreated, text});
    }

    public void insertRow(int position, String task, String priority, boolean isBlocked, LocalDate dateCreated, List<Comment> text) {
        super.insertRow(position, new Object[]{task, createPriorityComboBox(priority), isBlocked, dateCreated, text});
    }

    private ComboBox createPriorityComboBox(String selectedValue) {
        ComboBox priorityComboBox = new ComboBox(new String[]{selectedValue});
        priorityComboBox.setSelectedItem(selectedValue);
        return priorityComboBox;
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

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (BLOCKED_COLUMN == columnIndex) {
            return Boolean.class;
        }

        return super.getColumnClass(columnIndex);
    }
}
