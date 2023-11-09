package org.nickhill111.common.util;

import org.nickhill111.common.data.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URI;

public class MenuUtils {
    private static final Config config = Config.getInstance();

    public static JMenu createFileMenu() {
        return new JMenu("File");
    }

    public static JMenu createFunctionMenu() {
        return new JMenu("Functions");
    }

    public static JMenu createPersonalisationMenu(JFrame frame) {
        JMenu personalisationMenu = new JMenu("Personalisation");

        personalisationMenu.add(createLookAndFeelMenu());
        personalisationMenu.add(createRefreshGuiMenuItem(frame));
        personalisationMenu.add(createResetConfigMenuItem());

        return personalisationMenu;
    }

    public static JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");

        helpMenu.add(createAboutMenuItem());
        helpMenu.add(createReportBugMenuItem());
        helpMenu.add(createHelpMenuItem());

        return helpMenu;
    }

    private static JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Look and Feel");

        for (UIManager.LookAndFeelInfo installedLookAndFeel : UIManager.getInstalledLookAndFeels()) {
            JMenuItem lookAndFeelMenuItem = new JMenuItem(installedLookAndFeel.getName());
            lookAndFeelMenuItem.addActionListener(e -> {
                config.getConfigDetails().getGlobalConfigDetails().getPersonalisation().setLookAndFeel(installedLookAndFeel.getClassName());
                config.saveConfigDetails();
                DialogUtils.lookAndFeelChangedSuccessfully();
            });
            lookAndFeelMenu.add(lookAndFeelMenuItem);
        }

        return lookAndFeelMenu;
    }

    private static JMenuItem createRefreshGuiMenuItem(JFrame frame) {
        JMenuItem refreshGui = new JMenuItem("Refresh GUI");
        refreshGui.addActionListener(e -> GuiUtils.refreshComponent(frame));
        refreshGui.setMnemonic(KeyEvent.VK_G);
        refreshGui.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
        return refreshGui;
    }

    private static JMenuItem createResetConfigMenuItem() {
        JMenuItem resetConfig = new JMenuItem("Reset Config");
        resetConfig.addActionListener(e -> config.reset());
        return resetConfig;
    }

    public static JMenuItem createExitMenuItem() {
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> {
            config.saveConfigDetails();
            System.exit(0);
        });

        return exitMenuItem;
    }

    private static JMenuItem createAboutMenuItem() {
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

    private static JMenuItem createReportBugMenuItem() {
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

    private static JMenuItem createHelpMenuItem() {
        JMenuItem helpMenuItem = new JMenuItem("Help");
        helpMenuItem.addActionListener(e -> {
            JFrame helpFrame = new JFrame();
            helpFrame.setTitle("Help");

            JLabel helpTitle = new JLabel("Help");
            helpFrame.add(helpTitle);

            JLabel helpDialog = new JLabel("<html>" +
                "<h1>Help:</h1>" +
                "<br>" +
                "Please refer to the readme file <a href=\"https://github.com/nickhill111/TestingEvidence/blob/main/README.md\">here</a>" +
                "<br>" +
                "<br>" +
                "Alternatively if you can't find an answer there then <br>" +
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
