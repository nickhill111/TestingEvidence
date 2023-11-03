package org.nickhill111.taskManager.gui;

import org.nickhill111.taskManager.data.Priority;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PriorityCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    private JComboBox<?> selectedPriority;

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof JComboBox<?> priorityValue) {
            this.selectedPriority = priorityValue;
        }

        JComboBox<String> priorityComboBox = new JComboBox<>();

        for (String priority : Priority.getAllValues()) {
            priorityComboBox.addItem(priority);
        }

        priorityComboBox.setSelectedItem(selectedPriority.getSelectedItem());
        priorityComboBox.addActionListener(this);

        priorityComboBox.setBackground(isSelected ? table.getSelectionBackground() : table.getSelectionForeground());

        return priorityComboBox;
    }

    @Override
    public Object getCellEditorValue() {
        return this.selectedPriority;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JComboBox<?> priorityComboBox) {
            this.selectedPriority = priorityComboBox;
        }
    }
}
