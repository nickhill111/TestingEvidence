package org.nickhill111.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import org.nickhill111.TestingEvidenceApplication;
import org.nickhill111.data.Config;
import org.nickhill111.data.FrameComponents;
import org.nickhill111.service.SavingService;
import org.nickhill111.service.ScreenshotService;
import org.nickhill111.util.DialogUtils;

import static org.nickhill111.util.GuiUtils.addNewScenarioTab;

public class MenuBar extends JMenuBar {
    private final FrameComponents frameComponents = FrameComponents.getInstance();
    private final Config config = Config.getInstance();
    private final SavingService savingService = new SavingService();
    private final ScreenshotService screenshotService = new ScreenshotService();

    public MenuBar() {
        add(createFileMenu());
        add(createFunctionsMenu());
        add(createPersonalisationMenu());
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(e -> {
            frameComponents.getFrame().dispose();
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

        fileMenu.addSeparator();

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);


        return fileMenu;
    }

    private JMenu createFunctionsMenu() {
        JMenu functionsMenu = new JMenu("Functions");

        JMenuItem takeScreenshotMenuItem = new JMenuItem("Take Screenshot");
        takeScreenshotMenuItem.addActionListener(e -> screenshotService.takeAndAddScreenshot());
        takeScreenshotMenuItem.setMnemonic(KeyEvent.VK_F8);
        takeScreenshotMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
        functionsMenu.add(takeScreenshotMenuItem);

        JMenuItem addUserMenuItem = new JMenuItem("Add User");
        addUserMenuItem.addActionListener(e -> frameComponents.getUsers().addEmptyTab());
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
            Users users = frameComponents.getUsers();
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
            StringSelection selection = new StringSelection(frameComponents.getGeneratedTextArea().getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        });
        copyGeneratedTextMenuItem.setMnemonic(KeyEvent.VK_C);
        copyGeneratedTextMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        functionsMenu.add(copyGeneratedTextMenuItem);

        return functionsMenu;
    }

    private JMenu createPersonalisationMenu() {
        JMenu personalisationMenu = new JMenu("Personalisation");

        JMenu lookAndFeelMenu = new JMenu("Look and Feel");

        for (UIManager.LookAndFeelInfo installedLookAndFeel : UIManager.getInstalledLookAndFeels()) {
            JMenuItem lookAndFeelMenuItem = new JMenuItem(installedLookAndFeel.getName());
            lookAndFeelMenuItem.addActionListener(e -> {
                config.getConfigDetails().getPersonalisation().setLookAndFeel(installedLookAndFeel.getClassName());
                config.saveConfig();
                DialogUtils.lookAndFeelChangedSuccessfully();
            });
            lookAndFeelMenu.add(lookAndFeelMenuItem);
        }
        personalisationMenu.add(lookAndFeelMenu);

        return personalisationMenu;
    }
}
