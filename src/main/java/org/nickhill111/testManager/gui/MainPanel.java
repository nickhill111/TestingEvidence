package org.nickhill111.testManager.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.nickhill111.common.data.Config;
import org.nickhill111.testManager.data.TestManagerComponents;
import org.nickhill111.common.util.DialogUtils;

import static org.nickhill111.common.util.FileUtils.GENERATED_TEXT_FILE_NAME;
import static org.nickhill111.common.util.GuiUtils.PHOTO_SIZE;

public class MainPanel extends JPanel {
    private final Config config = Config.getInstance();

    public MainPanel() {
        setLayout(new BorderLayout());

        add(new TestingToolBar(), BorderLayout.PAGE_START);

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

        TestManagerComponents testManagerComponents = TestManagerComponents.getInstance();
        testManagerComponents.setGeneratedTextArea(generatedTextArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, users, textAreaPanel);
        setUpSplitPane(splitPane);

        add(splitPane, BorderLayout.CENTER);

        testManagerComponents.setMainPanel(this);
    }

    private String getGeneratedText(String generatedText) {
        File generatedTextFile = new File(config.getConfigDetails().getTestManagerConfigDetails().getOpenedFolderPath(),
            GENERATED_TEXT_FILE_NAME);

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

        splitPane.setDividerLocation(config.getConfigDetails().getTestManagerConfigDetails().getSplitPaneLocation());

        splitPane.addPropertyChangeListener(e -> {
            config.getConfigDetails().getTestManagerConfigDetails().setSplitPaneLocation(splitPane.getDividerLocation());
            config.saveConfig();
        });
    }
}
