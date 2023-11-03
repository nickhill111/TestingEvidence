package org.nickhill111.taskManager.gui;

import org.nickhill111.common.data.Config;
import org.nickhill111.taskManager.data.TaskManagerComponents;
import org.nickhill111.taskManager.service.FunctionsService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import static org.nickhill111.common.data.Icons.ADD_ICON;
import static org.nickhill111.common.data.Icons.DELETE_ICON;
import static org.nickhill111.common.data.Icons.DOWN_ICON;
import static org.nickhill111.common.data.Icons.SAVE_ICON;
import static org.nickhill111.common.data.Icons.UP_ICON;
import static org.nickhill111.common.util.GuiUtils.MEDIUM_FONT;

public class TaskToolBar extends JToolBar {
    private final Config config = Config.getInstance();
    private final TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();
    private final FunctionsService functionsService = new FunctionsService();

    public TaskToolBar() {
        addAddButton();
        addRemoveButton();
        addSeparator();
        addUpButton();
        addDownButton();
        addSeparator();
        addSaveButton();
        addSeparator();
        addFilter();
    }

    private void addFilter() {
        JLabel taskFilterLabel = new JLabel("Task Filter:  ");
        taskFilterLabel.setFont(MEDIUM_FONT);
        add(taskFilterLabel);

        JTextField filter = new JTextField(20);
        filter.setFont(MEDIUM_FONT);
        filter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateList();
            }

            private void updateList() {
                filterTable(taskManagerComponents.getCurrentTaskTable());
                filterTable(taskManagerComponents.getCompletedTaskTable());


                taskManagerComponents.getInfoTextArea().setText("");
                taskManagerComponents.getInfoTextArea().setEditable(false);
            }

            private void filterTable(TaskTable taskTable) {
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(taskTable.getModel());
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + filter.getText()));

                taskTable.setRowSorter(sorter);
            }
        });
        add(filter);

        taskManagerComponents.setFilter(filter);
    }

    private void addAddButton() {
        JButton addButton = new JButton(ADD_ICON);
        addButton.setToolTipText("Add task to list");
        addButton.addActionListener(e -> functionsService.addTask());

        add(addButton);
    }

    private void addRemoveButton() {
        JButton removeButton = new JButton(DELETE_ICON);
        removeButton.setToolTipText("Remove selected tasks");
        removeButton.addActionListener(event -> functionsService.removeTasks());

        add(removeButton);
    }

    private void addUpButton() {
        JButton upButton = new JButton(UP_ICON);
        upButton.setToolTipText("Up");
        upButton.addActionListener(e -> functionsService.moveTaskUp());

        add(upButton);
    }

    private void addDownButton() {
        JButton downButton = new JButton(DOWN_ICON);
        downButton.setToolTipText("Down");
        downButton.addActionListener(e -> functionsService.moveTaskDown());

        add(downButton);
    }

    private void addSaveButton() {
        JButton saveButton = new JButton(SAVE_ICON);
        saveButton.setToolTipText("Save");
        saveButton.addActionListener(e -> config.saveTaskList());

        add(saveButton);
    }
}
