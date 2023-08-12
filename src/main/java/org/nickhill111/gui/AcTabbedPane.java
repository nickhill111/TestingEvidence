package org.nickhill111.gui;

import static java.util.Objects.isNull;
import static org.nickhill111.model.TabNames.REGRESSION;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.nickhill111.data.Settings;
import org.nickhill111.model.RegressionTab;
import org.nickhill111.util.DialogUtils;
import org.nickhill111.util.GuiUtils;

public class AcTabbedPane extends JTabbedPane {
    private final Settings settings = Settings.getInstance();

    public AcTabbedPane(List<File> files) {
        addAcTab(files);
        addMouseListener();
    }

    public AcTabbedPane(List<File> files, boolean isRegression) {
        if (isRegression) {
            addRegressionTabs(files);
        } else {
            addAcTab(files);
        }
        addMouseListener();
    }

    private void addAcTab(List<File> files) {
        if (isNull(files) || files.isEmpty()) {
            addNewEmptyTab();
        } else {
            addPopulatedTab(files);
        }
    }

    private void addPopulatedTab(List<File> files) {
        File lastFile = files.get(files.size() -1);
        String[] lastFileNameSplit = lastFile.getName().split("_");

        String userType = lastFileNameSplit[0];
        int lastFileNumber = Integer.parseInt(lastFileNameSplit[2]);

        for (int i = 1; i <= lastFileNumber; i++) {
            int finalI = i;
            List<File> filesForTab = files.stream()
                .filter(file -> file.getName().startsWith(userType + "_AC_" + finalI + "_"))
                .toList();
            PreviewPanel previewPanel = new PreviewPanel(i, filesForTab);

            ScrollPane scrollPane = new ScrollPane(previewPanel);

            addTab("AC " + i, scrollPane);

            selectNewComponent(scrollPane);
        }
    }

    public void addEmptyTab() {
        UserTabbedPane userTabbedPane = settings.getUserTabbedPane();
        if (userTabbedPane.getTitleAt(userTabbedPane.getSelectedIndex()).equals(REGRESSION.getValue())) {
            DialogUtils.cantAddTabUnderRegression();
        } else {
            addNewEmptyTab();
        }
    }

    private void addNewEmptyTab() {
        int acValue = getTabCount() + 1;

        PreviewPanel previewPanel = new PreviewPanel(acValue);

        ScrollPane scrollPane = new ScrollPane(previewPanel);

        addTab("AC " + (acValue), scrollPane);

        selectNewComponent(scrollPane);
    }

    private void addRegressionTabs(List<File> files) {
        if (isNull(files) || files.isEmpty()) {
            addEmptyRegressionTabs();
        } else {
            addPopulatedRegressionTabs(files);
        }
    }

    private void addEmptyRegressionTabs() {
        for (RegressionTab regressionTab : RegressionTab.values()) {
            PreviewPanel previewPanel = new PreviewPanel(regressionTab.getId());

            ScrollPane scrollPane = new ScrollPane(previewPanel);

            addTab(regressionTab.getValue(), scrollPane);
        }
    }

    private void addPopulatedRegressionTabs(List<File> files) {
        for (RegressionTab regressionTab : RegressionTab.values()) {
            List<File> filesForTab = files.stream()
                .filter(file -> file.getName().contains(regressionTab.getValue() + "_"))
                .toList();

            PreviewPanel previewPanel = new PreviewPanel(regressionTab.getId(), filesForTab);

            ScrollPane scrollPane = new ScrollPane(previewPanel);

            addTab(regressionTab.getValue(), scrollPane);

            selectNewComponent(scrollPane);
        }
    }

    public List<PreviewPanel> getPreviewPanels() {
        List<PreviewPanel> previewPanels = new LinkedList<>();

        for (int i = 0; i < getTabCount(); i++) {
            PreviewPanel previewPanel = ((ScrollPane) getComponentAt(i)).getPreviewPanel();
            previewPanels.add(previewPanel);
        }

        return previewPanels;
    }

    private void selectNewComponent(ScrollPane scrollPane) {
        setSelectedComponent(scrollPane);
        GuiUtils.refreshComponent(this);
    }

    private void addMouseListener() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getModifiersEx() == MouseEvent.META_DOWN_MASK && e.getClickCount() == 1) {
                    int selectedIndex = getSelectedIndex();

                    if (getTabCount() > 1 && selectedIndex == getTabCount() - 1 && getTitleAt(selectedIndex).startsWith("AC ")) {
                        JPopupMenu menu = new JPopupMenu();

                        JMenuItem deleteMenuItem = new JMenuItem("Delete");
                        deleteMenuItem.addActionListener(e1 -> {
                            removeTabAt(selectedIndex);
                            GuiUtils.refreshComponent(settings.getFrame());
                        });
                        menu.add(deleteMenuItem);

                        menu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
}
