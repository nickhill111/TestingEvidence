package org.nickhill111.taskManager.gui;

import org.nickhill111.common.data.Config;
import org.nickhill111.taskManager.data.TaskManagerComponents;
import org.nickhill111.taskManager.service.FunctionsService;

import javax.swing.*;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static org.nickhill111.common.util.MenuUtils.*;

public class TaskMenuBar extends JMenuBar {
    private static final Config config = Config.getInstance();
    private static final TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();
    private final FunctionsService functionsService = new FunctionsService();

    public TaskMenuBar() {
        addFileMenu();
        addFunctionsMenu();
        addPersonalisationMenu();
        addHelpMenu();
    }

    private void addFileMenu() {
        JMenu fileMenu = createFileMenu();

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> config.saveTaskList());
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(saveMenuItem);

        fileMenu.addSeparator();

        JMenuItem resetMenuItem = new JMenuItem("Reset");
        resetMenuItem.addActionListener(e -> {
            TaskTableModel tableModel = taskManagerComponents.getCurrentTaskTable().getModel();

            while (tableModel.getRowCount() > 0) {
                tableModel.removeRow(0);
            }

            config.saveTaskList();
        });
        resetMenuItem.setMnemonic(KeyEvent.VK_R);
        resetMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(resetMenuItem);

        fileMenu.addSeparator();

        fileMenu.add(createExitMenuItem());

        add(fileMenu);
    }

    private void addFunctionsMenu() {
        JMenu functionsMenu = createFunctionMenu();

        JMenuItem addTaskMenuItem = new JMenuItem("Add task");
        addTaskMenuItem.addActionListener(e -> functionsService.addTask());
        addTaskMenuItem.setMnemonic(KeyEvent.VK_T);
        addTaskMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
        functionsMenu.add(addTaskMenuItem);

        JMenuItem removeTasksMenuItem = new JMenuItem("Remove tasks");
        removeTasksMenuItem.addActionListener(e -> functionsService.removeTasks());
        removeTasksMenuItem.setMnemonic(KeyEvent.VK_U);
        removeTasksMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
        functionsMenu.add(removeTasksMenuItem);

        JMenuItem moveTaskUpMenuItem = new JMenuItem("Move task up");
        moveTaskUpMenuItem.addActionListener(e -> functionsService.moveTaskUp());
        moveTaskUpMenuItem.setMnemonic(KeyEvent.VK_UP);
        moveTaskUpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK));
        functionsMenu.add(moveTaskUpMenuItem);

        JMenuItem moveTaskDownMenuItem = new JMenuItem("Move task down");
        moveTaskDownMenuItem.addActionListener(e -> functionsService.moveTaskDown());
        moveTaskDownMenuItem.setMnemonic(KeyEvent.VK_DOWN);
        moveTaskDownMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK));
        functionsMenu.add(moveTaskDownMenuItem);

        add(functionsMenu);
    }

    private void addPersonalisationMenu() {
        add(createPersonalisationMenu(taskManagerComponents.getFrame()));
    }

    private void addHelpMenu() {
        add(createHelpMenu());
    }
}
