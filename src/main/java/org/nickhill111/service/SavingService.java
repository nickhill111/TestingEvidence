package org.nickhill111.service;

import static java.util.Objects.nonNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.nickhill111.TestingEvidenceApplication;
import org.nickhill111.data.Settings;
import org.nickhill111.gui.PreviewPanel;
import org.nickhill111.util.DialogUtils;
public class SavingService {
    private final Settings settings = Settings.getInstance();
    private boolean isSaving = false;

    public void saveAllScreenshots() {
        if (!isSaving) {
            isSaving = true;

            new Thread(() -> {
                try {
                    if (settings.isActivefolder()) {
                        saveScreenshots();
                    } else {
                        saveAllScreenshotsWithFileChooser();
                    }
                } catch (Exception e) {
                    DialogUtils.cantSaveDialog(e);
                }

                isSaving = false;
            }).start();

        } else {
            DialogUtils.currentlySavingDialog();
        }
    }

    public void saveAllScreenshotsWithFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Save Screenshots");

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File folderToSave = fileChooser.getSelectedFile();
            saveScreenshots(folderToSave);
        }
    }

    private void saveScreenshots() {
        saveScreenshots(settings.getActiveFolder());
    }

    private void saveScreenshots(File folder) {
        if (!folder.equals(settings.getActiveFolder())) {
            folder = getFolderName(folder);
        }

        File[] existingFiles = folder.listFiles();

        settings.getFrame().setTitle(folder.getName());

        if (folder.exists() || folder.mkdirs()) {
            settings.setActiveFolder(folder);

            generateTextAndScreenshots(folder);

            DialogUtils.screenshotsSavedDialog(folder);
        } else {
            DialogUtils.cantSaveDialog(new Exception("Unable to create folder"));

            return;
        }

        if (nonNull(existingFiles)) {
            deletePreviousFiles(existingFiles);
        }

        saveGeneratedTextToFile(folder);
    }

    private void generateTextAndScreenshots(File folder) {
        StringBuilder generatedText = new StringBuilder();

        Map<String, List<PreviewPanel>> allPreviewPanels = settings.getUserTabbedPane().getAllPreviewPanels();
        for (String userType : allPreviewPanels.keySet()) {
            generatedText.append(userType).append(" testing:\n\n");
            List<PreviewPanel> previewPanels = allPreviewPanels.get(userType);

            for (PreviewPanel previewPanel : previewPanels) {
                String generatedTextForAc = previewPanel.saveAllEvidence(userType, folder);

                if (nonNull(generatedTextForAc)) {
                    generatedText.append(generatedTextForAc).append("\n\n");
                } else {
                    settings.getGeneratedTextArea().setText("Error when saving!");
                    return;
                }
            }
        }

        settings.getGeneratedTextArea().setText(generatedText.toString());
    }

    private void saveGeneratedTextToFile(File folder) {
        String text = settings.getGeneratedTextArea().getText();
        try {
            FileUtils.writeStringToFile(new File(folder, "GeneratedText.txt"), text, StandardCharsets.UTF_8);
        } catch (IOException e) {
            DialogUtils.cantSaveGeneratedText(e);
        }
    }

    private void deletePreviousFiles(File[] previousFiles) {
        for (File file : previousFiles) {
            boolean isFileDeleted = file.isFile() ? deleteDirectory(file) : file.delete();

            if (!isFileDeleted) {
                DialogUtils.cantDeleteActiveFolder();
            }
        }
    }

    public File getFolderName(File folder) {
        return getFolderName(folder, 1);
    }

    public File getFolderName(File folder, int number) {
        folder = new File(folder, settings.getFrame().getTitle());

        File temporaryFolder = new File(folder.getAbsolutePath());

        while (temporaryFolder.exists()) {
            temporaryFolder = new File(folder.getAbsolutePath() + "-" + number);
            number++;
        }

        return temporaryFolder;
    }

    public void openScreenshots() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Open Screenshots");

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File folderToOpen = fileChooser.getSelectedFile();

            settings.getFrame().dispose();
            new TestingEvidenceApplication(folderToOpen);
        }
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();

        if (nonNull(allContents)) {
            Arrays.stream(allContents).forEachOrdered(this::deleteDirectory);
        }

        return directoryToBeDeleted.delete();
    }
}
