package org.nickhill111.taskManager;

import org.nickhill111.common.data.Config;
import org.nickhill111.taskManager.gui.TaskManagerFrame;
import org.nickhill111.common.util.GuiUtils;

import java.awt.*;

public class TaskManagerApplication {

    private TaskManagerApplication() {
        GuiUtils.setLookAndFeel();
        Config config = Config.getInstance();
        GraphicsConfiguration screen = GuiUtils.getScreen(config.getConfigDetails().getTaskManagerConfigDetails().getFrameConfigDetails().getWindowScreenId());
        new TaskManagerFrame(screen);
    }

    public static void main(String[] args) {
        new TaskManagerApplication();
    }
}
