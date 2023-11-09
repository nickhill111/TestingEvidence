package org.nickhill111.taskManager.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;

import static org.nickhill111.taskManager.gui.TaskTable.BLOCKED_COLUMN;

public class TaskTableModel extends DefaultTableModel {

    public TaskTableModel() {
        super();

        addColumn("Task");
        addColumn("Priority");
        addColumn("Blocked");
        addColumn("Date created");
        addColumn("Text");
    }

    public void addRow(String task, String priority, boolean isBlocked, LocalDate dateCreated, String text) {
        super.addRow(new Object[]{task, createPriorityComboBox(priority), isBlocked, dateCreated, text});
    }

    public void insertRow(int position, String task, String priority, boolean isBlocked, LocalDate dateCreated, String text) {
        super.insertRow(position, new Object[]{task, createPriorityComboBox(priority), isBlocked, dateCreated, text});
    }

    private JComboBox<String> createPriorityComboBox(String selectedValue) {
        JComboBox<String> priorityComboBox = new JComboBox<>(new String[]{selectedValue});
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
