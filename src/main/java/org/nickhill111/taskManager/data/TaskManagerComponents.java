package org.nickhill111.taskManager.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nickhill111.common.util.GuiUtils;
import org.nickhill111.taskManager.gui.CommentsPanel;
import org.nickhill111.taskManager.gui.TaskTable;
import org.nickhill111.taskManager.gui.TasksTabbedPane;

import javax.swing.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TaskManagerComponents {
    private static TaskManagerComponents INSTANCE;

    private TasksTabbedPane tasksTabbedPane;
    private TaskTable currentTaskTable;
    private TaskTable completedTaskTable;
    private JTextArea newCommentTextArea;
    private JTextField filter;
    private JFrame frame;
    private CommentsPanel commentsPanel;

    public static TaskManagerComponents getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TaskManagerComponents();
        }

        return INSTANCE;
    }

    public void refreshInfoPanel(List<Comment> comments, int selectedRow) {
        commentsPanel.setComments(comments);
        newCommentTextArea.setText("");

        newCommentTextArea.setEditable(selectedRow >= 0 && tasksTabbedPane.isCurrentTasksVisible());
        GuiUtils.refreshComponent(tasksTabbedPane);
    }
}
