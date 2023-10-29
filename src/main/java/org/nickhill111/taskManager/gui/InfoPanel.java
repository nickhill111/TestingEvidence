package org.nickhill111.taskManager.gui;

import org.nickhill111.taskManager.data.TaskManagerComponents;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class InfoPanel extends JPanel {

    public InfoPanel() {
        super(new BorderLayout());

        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();
        taskManagerComponents.setInfoTextArea(textArea);

        add(textArea, BorderLayout.CENTER);
    }
}
