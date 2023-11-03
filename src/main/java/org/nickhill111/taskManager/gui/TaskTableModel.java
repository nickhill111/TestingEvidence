package org.nickhill111.taskManager.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;

public class TaskTableModel extends DefaultTableModel {

    public TaskTableModel() {
        super();

        addColumn("Task");
        addColumn("Priority");
        addColumn("Date created");
        addColumn("Text");
    }

    public void addRow(String task, String priority, LocalDate dateCreated, String text) {
        super.addRow(new Object[]{task, createPriorityComboBox(priority), dateCreated, text});
    }

    public void insertRow(int position, String task, String priority, LocalDate dateCreated, String text) {
        super.insertRow(position, new Object[]{task, createPriorityComboBox(priority), dateCreated, text});
    }

    private JComboBox<String> createPriorityComboBox(String selectedValue) {
        JComboBox<String> priorityComboBox = new JComboBox<>(new String[]{selectedValue});
        priorityComboBox.setSelectedItem(selectedValue);
        return priorityComboBox;
    }
}
