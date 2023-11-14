package org.nickhill111.testManager.gui;

import org.nickhill111.common.data.Config;
import org.nickhill111.common.data.FrameConfigDetails;
import org.nickhill111.common.gui.GenericScrollPane;
import org.nickhill111.common.util.GuiUtils;
import org.nickhill111.testManager.data.TestManagerComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import static java.util.Objects.nonNull;
import static org.nickhill111.common.util.GuiUtils.convertZoomValueToScale;

public class PreviewEvidenceFrame extends JFrame implements ComponentListener, MouseWheelListener  {
    private final Config config = Config.getInstance();
    private final PreviewEvidencePanel previewEvidencePanel;
    private final PreviewEvidenceToolBar toolBar;

    public PreviewEvidenceFrame(BufferedImage originalImage, GraphicsConfiguration graphicsConfiguration) {
        super(graphicsConfiguration);

        addComponentListener(this);
        addMouseWheelListener(this);

        this.previewEvidencePanel = new PreviewEvidencePanel(originalImage,
            convertZoomValueToScale(config.getConfigDetails().getTestManagerConfigDetails().getPreviewZoomValue()));
        add(new GenericScrollPane(previewEvidencePanel));

        ScenarioPanel selectedScenario = TestManagerComponents.getInstance().getUsers().getSelectedScenarios().getSelectedScenario();
        this.toolBar = new PreviewEvidenceToolBar(selectedScenario, previewEvidencePanel);
        add(toolBar, BorderLayout.PAGE_END);

        setTitle("Preview Evidence");
        setAlwaysOnTop(true);
        FrameConfigDetails frameConfigDetails = config.getConfigDetails().getTestManagerConfigDetails().getPreviewFrameConfigDetails();

        Dimension windowSize = frameConfigDetails.getWindowSize();

        if (nonNull(windowSize)) {
            setSize(windowSize);
            setExtendedState(frameConfigDetails.getWindowState());
        }

        GuiUtils.setupGui(this);
    }

    @Override
    public void componentResized(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        FrameConfigDetails frameConfigDetails = config.getConfigDetails().getTestManagerConfigDetails().getPreviewFrameConfigDetails();
        frameConfigDetails.setWindowSize(getSize());
        frameConfigDetails.setWindowState(getExtendedState());
        frameConfigDetails.setWindowScreenId(getGraphicsConfiguration().getDevice().getIDstring());
        config.saveConfigDetails();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int wheelRotation = e.getWheelRotation();
        double newScale = previewEvidencePanel.getScale() - (double) wheelRotation/10;

        if (newScale <= 1.5 && newScale >= 0.3) {
            previewEvidencePanel.setScale(newScale);
            JSlider zoomSlider = toolBar.getZoomSlider();
            zoomSlider.setValue(zoomSlider.getValue() - (wheelRotation * 10));
        }
    }
}
