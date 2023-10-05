package org.nickhill111.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.nickhill111.data.Config;
import org.nickhill111.data.FrameComponents;
import org.nickhill111.util.DialogUtils;

import static org.nickhill111.util.FileUtils.GENERATED_TEXT_FILE_NAME;
import static org.nickhill111.util.GuiUtils.PHOTO_SIZE;

public class MainPanel extends JPanel {
    private final Config config = Config.getInstance();

    public MainPanel() {
        setLayout(new BorderLayout());

        add(new ToolBar(), BorderLayout.PAGE_START);

        Users users = new Users();

        JPanel textAreaPanel = new JPanel();
        textAreaPanel.setLayout(new BorderLayout());

        String generatedText = "Currently no text generated. Please save to generate text.";

        if (config.isOpenedFolderPathActive()) {
            generatedText = getGeneratedText(generatedText);
        }

        TextArea generatedTextArea = new TextArea(generatedText);
        generatedTextArea.setEditable(false);
        generatedTextArea.setFont(new Font("Arial", Font.PLAIN, 18));
        textAreaPanel.add(generatedTextArea, BorderLayout.CENTER);

        FrameComponents frameComponents = FrameComponents.getInstance();
        frameComponents.setGeneratedTextArea(generatedTextArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, users, textAreaPanel);
        setUpSplitPane(splitPane);

        add(splitPane, BorderLayout.CENTER);

        frameComponents.setMainPanel(this);
    }

    private String getGeneratedText(String generatedText) {
        File generatedTextFile = new File(config.getConfigDetails().getOpenedFolderPath(), GENERATED_TEXT_FILE_NAME);

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
        splitPane.getLeftComponent().setMinimumSize(new Dimension(PHOTO_SIZE + 50, PHOTO_SIZE + 50));
        splitPane.getRightComponent().setMinimumSize(new Dimension());

        splitPane.setDividerLocation(config.getConfigDetails().getSplitPaneLocation());

        splitPane.addPropertyChangeListener(e -> {
            config.getConfigDetails().setSplitPaneLocation(splitPane.getDividerLocation());
            config.saveConfig();
        });
    }
}
