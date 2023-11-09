package org.nickhill111.taskManager.gui;

import org.nickhill111.taskManager.data.Priority;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class PriorityRenderer extends JComboBox<String> implements TableCellRenderer {

    public PriorityRenderer() {
        super(Priority.getAllValues());
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        if (value instanceof JComboBox<?> comboBox) {
            setSelectedItem(comboBox.getSelectedItem());
        }

        return this;
    }
}
