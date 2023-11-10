package org.nickhill111.taskManager.gui;

import org.nickhill111.common.util.GuiUtils;
import org.nickhill111.taskManager.data.Comment;
import org.nickhill111.taskManager.data.TaskManagerComponents;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CommentsPanel extends JPanel {
    private final GridBagConstraints constraints;

    public CommentsPanel() {
        setLayout(new GridBagLayout());

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);

        TaskManagerComponents.getInstance().setCommentsPanel(this);
    }

    public void addNewComment(String comment) {
        addNewComment(comment, null);
    }

    public void addNewComment(String comment, String timeCreated) {
        constraints.gridy++;
        add(new CommentPanel(comment, timeCreated), constraints);
        GuiUtils.refreshComponent(this);
    }

    public void setComments(List<Comment> comments) {
        removeAll();
        GuiUtils.refreshComponent(this);
        constraints.gridy = 0;

        for (Comment comment : comments) {
            addNewComment(comment.text(), comment.timeCreated());
        }
    }
}
