package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.example.data.Settings;
import org.example.util.DialogUtils;

public class MainPanel extends JPanel {
    Settings settings = Settings.getInstance();

    public MainPanel() {
        setLayout(new BorderLayout());

        add(new ToolBar(), BorderLayout.PAGE_START);

        UserTabbedPane userTabbedPane = new UserTabbedPane();

        JPanel textAreaPanel = new JPanel();
        textAreaPanel.setLayout(new BorderLayout());

        String generatedText = "Currently no text generated. Please save to generate text.";

        if (settings.isActivefolder()) {
            generatedText = getGeneratedText(generatedText);
        }

        TextArea generatedTextArea = new TextArea(generatedText);
        generatedTextArea.setEditable(false);
        textAreaPanel.add(generatedTextArea, BorderLayout.CENTER);

        settings.setGeneratedTextArea(generatedTextArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, userTabbedPane, textAreaPanel);
        setUpSplitPane(splitPane);

        add(splitPane, BorderLayout.CENTER);

        settings.setMainPanel(this);
    }

    private String getGeneratedText(String generatedText) {
        File generatedTextFile = new File(settings.getActiveFolder(), "GeneratedText.txt");

        if (generatedTextFile.exists() && generatedTextFile.isFile()) {
            try {
                return FileUtils.readFileToString(generatedTextFile, "UTF-8");
            } catch (IOException e) {
                DialogUtils.cantReadGeneratedTextFile(generatedTextFile);
            }
        }

        return generatedText;
    }

    private void setUpSplitPane(JSplitPane splitPane) {
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerSize(15);
        splitPane.getLeftComponent().setMinimumSize(new Dimension(0,300));
        splitPane.getRightComponent().setMinimumSize(new Dimension());
    }
}
