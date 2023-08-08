package org.example.gui;

import static java.util.Objects.isNull;
import static org.example.model.TabNames.REGRESSION;

import javax.swing.*;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.example.data.Settings;
import org.example.model.RegressionTab;
import org.example.util.DialogUtils;
import org.example.util.GuiUtils;

public class AcTabbedPane extends JTabbedPane {
    private final Settings settings = Settings.getInstance();

    public AcTabbedPane(List<File> files) {
        addAcTab(files);
    }

    public AcTabbedPane(List<File> files, boolean isRegression) {
        if (isRegression) {
            addRegressionTabs(files);
        } else {
            addAcTab(files);
        }
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
}
