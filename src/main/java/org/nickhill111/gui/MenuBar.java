package org.nickhill111.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URI;

import org.nickhill111.TestingEvidenceApplication;
import org.nickhill111.data.Config;
import org.nickhill111.data.FrameComponents;
import org.nickhill111.service.SavingService;
import org.nickhill111.service.ScreenshotService;
import org.nickhill111.util.DialogUtils;
import org.nickhill111.util.GuiUtils;

import static org.nickhill111.util.GuiUtils.addNewScenarioTab;

public class MenuBar extends JMenuBar {
    private final FrameComponents frameComponents = FrameComponents.getInstance();
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
        JMenu fileMenu = new JMenu("File");

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(e -> {
            frameComponents.getFrame().dispose();
            config.getConfigDetails().setOpenedFolderPath(null);
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
            config.getConfigDetails().setOpenedFolderPath(null);
            config.saveFrameConfigDetails();
            System.exit(0);
        });
        fileMenu.add(closeMenuItem);

        fileMenu.addSeparator();

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> {
            config.saveFrameConfigDetails();
            System.exit(0);
        });
        fileMenu.add(exitMenuItem);


        add(fileMenu);
    }

    private void addFunctionsMenu() {
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

        add(functionsMenu);
    }

    private void addPersonalisationMenu() {
        JMenu personalisationMenu = new JMenu("Personalisation");

        personalisationMenu.add(createLookAndFeelMenu());
        personalisationMenu.add(createRefreshGuiMenuItem());
        personalisationMenu.add(createResetConfigMenuItem());

        add(personalisationMenu);
    }

    private JMenu createLookAndFeelMenu() {
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

        return lookAndFeelMenu;
    }

    private JMenuItem createRefreshGuiMenuItem() {
        JMenuItem refreshGui = new JMenuItem("Refresh GUI");
        refreshGui.addActionListener(e -> GuiUtils.refreshComponent(frameComponents.getFrame()));
        refreshGui.setMnemonic(KeyEvent.VK_L);
        refreshGui.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
        return refreshGui;
    }

    private JMenuItem createResetConfigMenuItem() {
        JMenuItem resetConfig = new JMenuItem("Reset Config");
        resetConfig.addActionListener(e -> config.reset());
        return resetConfig;
    }

    private void addHelpMenu() {
        JMenu helpMenu = new JMenu("Help");

        helpMenu.add(createAboutMenuItem());
        helpMenu.add(createReportBugMenuItem());
        helpMenu.add(createHelpMenuItem());

        add(helpMenu);
    }

    private JMenuItem createAboutMenuItem() {
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(e -> {
            JFrame aboutFrame = new JFrame();
            aboutFrame.setTitle("About");

            JLabel aboutTitle = new JLabel("About");
            aboutFrame.add(aboutTitle);

            JLabel aboutDialog = new JLabel("<html>" +
                "<h1>About:</h1>" +
                "<br>" +
                "Using this as a todo section for the moment:<br>" +
                "finish this dialog and the help dialog (need to change this to html)<br>" +
                "change icon<br>" +
                "Rearrange toolbar<br>" +
                "update labels to icons<br>" +
                "pack-up into a jar" +
                "</html>");
            aboutFrame.add(aboutDialog);
            aboutFrame.pack();
            Dimension currentSize = aboutFrame.getSize();

            aboutFrame.setSize(currentSize.width + 20, currentSize.height + 20);
            GuiUtils.setupGui(aboutFrame);
            aboutFrame.setResizable(false);
        });

        return aboutMenuItem;
    }

    private JMenuItem createReportBugMenuItem() {
        JMenuItem reportBugMenuItem = new JMenuItem("Report bug");
        reportBugMenuItem.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/nickhill111/TestingEvidence/issues/new"));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        return reportBugMenuItem;
    }

    private JMenuItem createHelpMenuItem() {
        JMenuItem helpMenuItem = new JMenuItem("Help");
        helpMenuItem.addActionListener(e -> {
            JFrame helpFrame = new JFrame();
            helpFrame.setTitle("Help");

            JLabel helpTitle = new JLabel("Help");
            helpFrame.add(helpTitle);

            JLabel helpDialog = new JLabel("<html>" +
                "<h1>Help:</h1>" +
                "<br>" +
                "Please refer to the readme file <a href=\"www.google.com\">here</a>" + //TODO: update url
                "<br>" +
                "<br>" +
                "Alternatively if you cant find an answer there then <br>" +
                "please reach out to Nicholas Hill on slack" +
                "</html>");
            helpFrame.add(helpDialog);
            helpFrame.pack();
            Dimension currentSize = helpFrame.getSize();

            helpFrame.setSize(currentSize.width + 20, currentSize.height + 20);
            GuiUtils.setupGui(helpFrame);
            helpFrame.setResizable(false);
        });

        return helpMenuItem;
    }
}
