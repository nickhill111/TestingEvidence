package org.nickhill111.testManager.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import org.nickhill111.testManager.TestingEvidenceApplication;
import org.nickhill111.common.data.Config;
import org.nickhill111.testManager.data.TestManagerComponents;
import org.nickhill111.testManager.service.SavingService;
import org.nickhill111.testManager.service.ScreenshotService;
import org.nickhill111.common.util.DialogUtils;

import static org.nickhill111.common.util.GuiUtils.addNewScenarioTab;
import static org.nickhill111.common.util.MenuUtils.createExitMenuItem;
import static org.nickhill111.common.util.MenuUtils.createFileMenu;
import static org.nickhill111.common.util.MenuUtils.createFunctionMenu;
import static org.nickhill111.common.util.MenuUtils.createHelpMenu;
import static org.nickhill111.common.util.MenuUtils.createPersonalisationMenu;

public class MenuBar extends JMenuBar {
    private final TestManagerComponents testManagerComponents = TestManagerComponents.getInstance();
    private final Config config = Config.getInstance();
    private final SavingService savingService = new SavingService();
    private final ScreenshotService screenshotService = new ScreenshotService();

    public MenuBar() {
        addFileMenu();
        addFunctionsMenu();
        addPersonalisationMenu();
        addHelpMenu();
    }

    private void addFileMenu() {
        JMenu fileMenu = createFileMenu();

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(e -> {
            testManagerComponents.getFrame().dispose();
            config.getConfigDetails().getTestManagerConfigDetails().setOpenedFolderPath(null);
            new TestingEvidenceApplication();
        });
        newMenuItem.setMnemonic(KeyEvent.VK_N);
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(newMenuItem);

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(e -> savingService.openScreenshots());
        openMenuItem.setMnemonic(KeyEvent.VK_O);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> savingService.saveAllScreenshots());
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(saveMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("Save as");
        saveAsMenuItem.addActionListener(e -> savingService.saveAllScreenshotsWithFileChooser());
        saveAsMenuItem.setMnemonic(KeyEvent.VK_S);
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
        fileMenu.add(saveAsMenuItem);

        JMenuItem closeMenuItem = new JMenuItem("Close");
        closeMenuItem.addActionListener(e -> {
            config.getConfigDetails().getTestManagerConfigDetails().setOpenedFolderPath(null);
            config.saveTestManagerFrameConfigDetails();
            System.exit(0);
        });
        fileMenu.add(closeMenuItem);

        fileMenu.addSeparator();

        fileMenu.add(createExitMenuItem());


        add(fileMenu);
    }

    private void addFunctionsMenu() {
        JMenu functionsMenu = createFunctionMenu();

        JMenuItem takeScreenshotMenuItem = new JMenuItem("Take Screenshot");
        takeScreenshotMenuItem.addActionListener(e -> screenshotService.takeAndAddScreenshot());
        takeScreenshotMenuItem.setMnemonic(KeyEvent.VK_F8);
        takeScreenshotMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
        functionsMenu.add(takeScreenshotMenuItem);

        JMenuItem addUserMenuItem = new JMenuItem("Add User");
        addUserMenuItem.addActionListener(e -> testManagerComponents.getUsers().addEmptyTab());
        addUserMenuItem.setMnemonic(KeyEvent.VK_U);
        addUserMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
        functionsMenu.add(addUserMenuItem);

        JMenuItem addScenarioMenuItem = new JMenuItem("Add Scenario");
        addScenarioMenuItem.addActionListener(e -> addNewScenarioTab());
        addScenarioMenuItem.setMnemonic(KeyEvent.VK_A);
        addScenarioMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        functionsMenu.add(addScenarioMenuItem);

        JMenuItem addRegressionMenuItem = new JMenuItem("Add Regression Testing");
        addRegressionMenuItem.addActionListener(e -> {
            Users users = testManagerComponents.getUsers();
            users.addEmptyRegressionTab();
        });
        addRegressionMenuItem.setMnemonic(KeyEvent.VK_R);
        addRegressionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        functionsMenu.add(addRegressionMenuItem);

        JMenuItem changeTicketNumberMenuItem = new JMenuItem("Change Ticket Number");
        changeTicketNumberMenuItem.addActionListener(e -> DialogUtils.askForTicketNumber());
        changeTicketNumberMenuItem.setMnemonic(KeyEvent.VK_T);
        changeTicketNumberMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
        functionsMenu.add(changeTicketNumberMenuItem);

        JMenuItem copyGeneratedTextMenuItem = new JMenuItem("Copy Generated Text");
        copyGeneratedTextMenuItem.addActionListener(e -> {
            StringSelection selection = new StringSelection(testManagerComponents.getGeneratedTextArea().getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        });
        copyGeneratedTextMenuItem.setMnemonic(KeyEvent.VK_C);
        copyGeneratedTextMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        functionsMenu.add(copyGeneratedTextMenuItem);

        add(functionsMenu);
    }

    private void addPersonalisationMenu() {
        add(createPersonalisationMenu(testManagerComponents.getFrame()));
    }

    private void addHelpMenu() {
        add(createHelpMenu());
    }
}
