package org.nickhill111.taskManager.gui;

import org.nickhill111.taskManager.data.TaskManagerComponents;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

import static org.nickhill111.common.data.Icons.ADD_ICON;
import static org.nickhill111.common.data.Icons.REMOVE_ICON;

public class NewCommentPanel extends JPanel {

    public NewCommentPanel() {
        super(new BorderLayout());
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        taskManagerComponents.setNewCommentTextArea(textArea);

        add(textArea, BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();

        JButton addButton = new JButton(ADD_ICON);
        addButton.setToolTipText("Add Comment");
        addButton.addActionListener(e -> {
            if (textArea.isEditable() && !textArea.getText().trim().equals("")) {
                taskManagerComponents.getCommentsPanel().addNewComment(textArea.getText());
                textArea.setText("");
            }
        });
        toolBar.add(addButton);

        JButton clearButton = new JButton(REMOVE_ICON);
        clearButton.setToolTipText("Clear");
        clearButton.addActionListener(e -> textArea.setText(""));
        toolBar.add(clearButton);

        add(toolBar, BorderLayout.PAGE_END);
    }
}
