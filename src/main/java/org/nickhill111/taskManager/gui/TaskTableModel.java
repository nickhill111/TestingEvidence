package org.nickhill111.taskManager.gui;

import org.nickhill111.taskManager.data.Comment;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;

import static org.nickhill111.taskManager.gui.TaskTable.BLOCKED_COLUMN;

public class TaskTableModel extends DefaultTableModel {

    public TaskTableModel() {
        super(new Object[]{"Task", "Priority", "Blocked", "Date created", "Text"}, 0);
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

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (BLOCKED_COLUMN == columnIndex) {
            return Boolean.class;
        }

        return super.getColumnClass(columnIndex);
    }
}
