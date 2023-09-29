package org.nickhill111.service;

import static java.util.Objects.nonNull;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.nickhill111.TestingEvidenceApplication;
import org.nickhill111.data.FrameComponents;
import org.nickhill111.gui.ProgressBar;
import org.nickhill111.gui.ScenarioPanel;
import org.nickhill111.util.DialogUtils;
public class SavingService {
    private final FrameComponents frameComponents = FrameComponents.getInstance();
    private boolean isSaving = false;
    private XWPFDocument document;

    public void saveAllScreenshots() {
        if (!isSaving) {
            isSaving = true;

            new Thread(() -> {
                try {
                    if (frameComponents.isActiveFolder()) {
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
        saveScreenshots(frameComponents.getActiveFolder());
    }

    private void saveScreenshots(File folder) {
        if (!folder.equals(frameComponents.getActiveFolder())) {
            folder = getFolderName(folder);
        }

        File[] existingFiles = folder.listFiles();

        frameComponents.getFrame().setTitle(folder.getName());

        if (folder.exists() || folder.mkdirs()) {
            frameComponents.setActiveFolder(folder);

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
        document = new XWPFDocument();
        StringBuilder generatedText = new StringBuilder();

        Map<String, List<ScenarioPanel>> allPreviewPanels = frameComponents.getUsers().getAllScenarioPanels();

        ProgressBar progressBar = new ProgressBar(allPreviewPanels.keySet().size());

        for (String userType : allPreviewPanels.keySet()) {
            String textTitle = userType + " testing:";

            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun userRun = paragraph.createRun();
            userRun.setText(textTitle);
            userRun.addBreak();
            userRun.addBreak();

            generatedText.append(textTitle).append("\n\n");
            List<ScenarioPanel> scenarioPanels = allPreviewPanels.get(userType);

            for (ScenarioPanel scenarioPanel : scenarioPanels) {
                XWPFRun scenarioRun = paragraph.createRun();
                String generatedTextForScenario = scenarioPanel.saveAllEvidence(userType, folder, scenarioRun);

                if (nonNull(generatedTextForScenario)) {
                    generatedText.append(generatedTextForScenario).append("\n\n");
                } else {
                    frameComponents.getGeneratedTextArea().setText("Error when saving!");
                    return;
                }
            }

            progressBar.incrementValue();
        }

        progressBar.dispose();

        frameComponents.getGeneratedTextArea().setText(generatedText.toString());
    }

    private void saveGeneratedTextToFile(File folder) {
        String text = frameComponents.getGeneratedTextArea().getText();
        try {
            FileUtils.writeStringToFile(new File(folder, "GeneratedText.txt"), text, StandardCharsets.UTF_8);
            FileOutputStream out = new FileOutputStream( new File(folder, frameComponents.getFrame().getTitle() + "-Evidence.docx"));
            document.write(out);
            out.close();
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
        folder = new File(folder, frameComponents.getFrame().getTitle());

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

            frameComponents.getFrame().dispose();
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
