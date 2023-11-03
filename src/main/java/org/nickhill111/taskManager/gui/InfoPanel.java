package org.nickhill111.taskManager.gui;

import org.nickhill111.taskManager.data.TaskManagerComponents;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class InfoPanel extends JPanel {

    public InfoPanel() {
        super(new BorderLayout());

        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));


        TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();

        JTextArea textArea = new JTextArea();
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTextInTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTextInTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTextInTable();
            }

            private void updateTextInTable() {
                int selectedRow = taskManagerComponents.getCurrentTaskTable().getSelectedRow();

                if (selectedRow > 0) {
                    taskManagerComponents.getCurrentTaskTable().setText(selectedRow, textArea.getText());
                }
            }
        });
        textArea.setEditable(false);

        taskManagerComponents.setInfoTextArea(textArea);

        add(textArea, BorderLayout.CENTER);
    }
}
