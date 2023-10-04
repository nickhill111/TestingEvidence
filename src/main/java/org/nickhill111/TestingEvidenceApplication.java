package org.nickhill111;

import static java.util.Objects.nonNull;

import java.awt.*;
import java.io.File;

import org.nickhill111.data.Config;
import org.nickhill111.gui.MainFrame;
import org.nickhill111.util.GuiUtils;

public class TestingEvidenceApplication {
    private final Config config = Config.getInstance();

    public TestingEvidenceApplication() {
        String openedFolderPath = config.getConfigDetails().getOpenedFolderPath();

        if (nonNull(openedFolderPath)) {
            new TestingEvidenceApplication(new File(openedFolderPath));
            return;
        }

        new TestingEvidenceApplication(null);
    }
    
    public TestingEvidenceApplication(File folderToOpenFrom) {
        GuiUtils.setLookAndFeel();
        GraphicsConfiguration screen = GuiUtils.getScreen(config.getConfigDetails().getFrameConfigDetails().getWindowScreenId());
        new MainFrame(screen, folderToOpenFrom);
    }

    public static void main(String[] args) {
        new TestingEvidenceApplication();
    }
}