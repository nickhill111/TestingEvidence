package org.nickhill111.taskManager.gui;

import org.nickhill111.common.data.Config;
import org.nickhill111.common.data.FrameConfigDetails;
import org.nickhill111.common.util.GuiUtils;
import org.nickhill111.taskManager.data.TaskManagerComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import static java.util.Objects.nonNull;

public class TaskManagerFrame extends JFrame implements ComponentListener {
    private final Config config = Config.getInstance();

    public TaskManagerFrame(GraphicsConfiguration screen) {
        super(screen);
        if (nonNull(screen)) {
            setLocation(screen.getBounds().getLocation());
        }

        addComponentListener(this);
        TaskManagerComponents.getInstance().setFrame(this);

        MainTaskManagerPanel mainTaskManagerPanel = new MainTaskManagerPanel();

        add(mainTaskManagerPanel);

        FrameConfigDetails frameConfigDetails = config.getConfigDetails().getTaskManagerConfigDetails().getFrameConfigDetails();
        Dimension windowSize = frameConfigDetails.getWindowSize();

        if (nonNull(windowSize)) {
            setSize(windowSize);
            setExtendedState(frameConfigDetails.getWindowState());
        }

        setTitle("Task Manager");
        GuiUtils.setupGui(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        config.saveTaskManagerFrameConfigDetails();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        config.saveTaskManagerFrameConfigDetails();
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
