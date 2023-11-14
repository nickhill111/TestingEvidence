package org.nickhill111.taskManager.gui;

import org.nickhill111.common.util.GuiUtils;
import org.nickhill111.taskManager.data.Comment;
import org.nickhill111.taskManager.data.TaskManagerComponents;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.util.Objects.isNull;
import static org.nickhill111.common.data.Icons.REMOVE_ICON;
import static org.nickhill111.common.util.GuiUtils.SMALL_FONT;

public class CommentPanel extends JPanel {
    private final GridBagConstraints constraints;

    private final TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();

    public CommentPanel(String comment, String timeCreated) {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        Color color = Color.BLACK;

        if (GuiUtils.isDarkTheme()) {
            color = Color.WHITE;
        }
        setBorder(new MatteBorder(0, 0, 2, 0, color));

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(0, 10, 0, 10);

        if (isNull(timeCreated)) {
            timeCreated = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString().replace("T", " ");

            int selectedRow = taskManagerComponents.getCurrentTaskTable().getSelectedRow();

            if (selectedRow >= 0) {
                taskManagerComponents.getCurrentTaskTable().getModel().addComment(selectedRow, new Comment(comment, timeCreated));
            }
        }

        addComment(comment);
        addTimeCreated(timeCreated);
        addRemoveButton(comment, timeCreated);
    }

    private void addComment(String comment) {
        JTextArea commentTextArea = new JTextArea(comment);
        commentTextArea.setFont(SMALL_FONT);
        commentTextArea.setEditable(false);
        add(commentTextArea, constraints);
    }

    private void addTimeCreated(String timeCreated) {
        constraints.gridy++;
        JLabel timeLabel = new JLabel("Created: " + timeCreated);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        add(timeLabel, constraints);
    }

    private void addRemoveButton(String comment, String timeCreated) {
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx++;
        JButton removeButton = new JButton(REMOVE_ICON);
        removeButton.setToolTipText("Remove Comment");
        removeButton.addActionListener(e -> {
            int selectedRow = taskManagerComponents.getCurrentTaskTable().getSelectedRow();

            if (selectedRow >= 0) {
                Container parent = getParent();
                parent.remove(this);
                GuiUtils.refreshComponent(parent);
                taskManagerComponents.getCurrentTaskTable().getModel().removeComment(selectedRow, new Comment(comment, timeCreated));
            }
        });
        add(removeButton, constraints);
    }
}
