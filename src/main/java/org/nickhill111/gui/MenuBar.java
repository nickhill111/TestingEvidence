package org.nickhill111.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import org.nickhill111.TestingEvidenceApplication;
import org.nickhill111.data.Settings;
import org.nickhill111.service.SavingService;
import org.nickhill111.service.ScreenshotService;
import org.nickhill111.util.DialogUtils;

public class MenuBar extends JMenuBar {
    private final Settings settings = Settings.getInstance();
    private final SavingService savingService = new SavingService();
    private final ScreenshotService screenshotService = new ScreenshotService();

    public MenuBar() {
        add(createFileMenu());
        add(createFunctionsMenu());
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(e -> {
            settings.getFrame().dispose();
            new TestingEvidenceApplication();
        });
        newMenuItem.setMnemonic(KeyEvent.VK_N);
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(newMenuItem);

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(e -> {
            savingService.openScreenshots();
        });
        openMenuItem.setMnemonic(KeyEvent.VK_O);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> {
            savingService.saveAllScreenshots();
        });
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(saveMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("Save as");
        saveAsMenuItem.addActionListener(e -> {
            savingService.saveAllScreenshotsWithFileChooser();
        });
        saveAsMenuItem.setMnemonic(KeyEvent.VK_S);
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
        fileMenu.add(saveAsMenuItem);

        fileMenu.addSeparator();

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> {
            System.exit(0);
        });
        fileMenu.add(exitMenuItem);


        return fileMenu;
    }

    private JMenu createFunctionsMenu() {
        JMenu functionsMenu = new JMenu("Functions");

        JMenuItem takeScreenshotMenuItem = new JMenuItem("Take Screenshot");
        takeScreenshotMenuItem.addActionListener(e -> {
            screenshotService.takeAndAddScreenshot();
        });
        takeScreenshotMenuItem.setMnemonic(KeyEvent.VK_F8);
        takeScreenshotMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
        functionsMenu.add(takeScreenshotMenuItem);

        JMenuItem addUserMenuItem = new JMenuItem("Add User");
        addUserMenuItem.addActionListener(e -> {
            settings.getUserTabbedPane().addEmptyTab();
        });
        addUserMenuItem.setMnemonic(KeyEvent.VK_U);
        addUserMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
        functionsMenu.add(addUserMenuItem);

        JMenuItem addAcMenuItem = new JMenuItem("Add AC");
        addAcMenuItem.addActionListener(e -> {
            AcTabbedPane acTabbedPane = settings.getUserTabbedPane().getSelectedAcTabbedPane();
            acTabbedPane.addEmptyTab();
        });
        addAcMenuItem.setMnemonic(KeyEvent.VK_A);
        addAcMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        functionsMenu.add(addAcMenuItem);

        JMenuItem addRegressionMenuItem = new JMenuItem("Add Regression Testing");
        addRegressionMenuItem.addActionListener(e -> {
            UserTabbedPane userTabbedPane = settings.getUserTabbedPane();
            userTabbedPane.addEmptyRegressionTab();
        });
        addRegressionMenuItem.setMnemonic(KeyEvent.VK_R);
        addRegressionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        functionsMenu.add(addRegressionMenuItem);

        JMenuItem changeTicketNumberMenuItem = new JMenuItem("Change Ticket Number");
        changeTicketNumberMenuItem.addActionListener(e -> {
            DialogUtils.askForTicketNumber();
        });
        changeTicketNumberMenuItem.setMnemonic(KeyEvent.VK_T);
        changeTicketNumberMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
        functionsMenu.add(changeTicketNumberMenuItem);

        JMenuItem copyGeneratedTextMenuItem = new JMenuItem("Copy Generated Text");
        copyGeneratedTextMenuItem.addActionListener(e -> {
            StringSelection selection = new StringSelection(settings.getGeneratedTextArea().getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        });
        copyGeneratedTextMenuItem.setMnemonic(KeyEvent.VK_C);
        copyGeneratedTextMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        functionsMenu.add(copyGeneratedTextMenuItem);

        return functionsMenu;
    }
}
