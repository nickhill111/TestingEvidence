package org.nickhill111.testManager;

import static java.util.Objects.nonNull;

import java.awt.*;
import java.io.File;

import org.nickhill111.common.data.Config;
import org.nickhill111.testManager.gui.MainFrame;
import org.nickhill111.common.util.GuiUtils;

public class TestingEvidenceApplication {
    private final Config config = Config.getInstance();

    public TestingEvidenceApplication() {
        String openedFolderPath = config.getConfigDetails().getTestManagerConfigDetails().getOpenedFolderPath();

        if (nonNull(openedFolderPath)) {
            new TestingEvidenceApplication(new File(openedFolderPath));
            return;
        }

        new TestingEvidenceApplication(null);
    }
    
    public TestingEvidenceApplication(File folderToOpenFrom) {
        GuiUtils.setLookAndFeel();
        GraphicsConfiguration screen = GuiUtils.getScreen(config.getConfigDetails().getTestManagerConfigDetails().getFrameConfigDetails().getWindowScreenId());
        new MainFrame(screen, folderToOpenFrom);
    }

    public static void main(String[] args) {
        new TestingEvidenceApplication();
    }
}