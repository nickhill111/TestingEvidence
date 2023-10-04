package org.nickhill111.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.nickhill111.util.FileUtils.GENERATED_TEXT_FILE_NAME;
import static org.nickhill111.util.FileUtils.deleteOldFiles;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.nickhill111.TestingEvidenceApplication;
import org.nickhill111.data.Config;
import org.nickhill111.data.ConfigDetails;
import org.nickhill111.data.FrameComponents;
import org.nickhill111.gui.ProgressBar;
import org.nickhill111.gui.ScenarioPanel;
import org.nickhill111.util.DialogUtils;
public class SavingService {
    private final Config config = Config.getInstance();
    private final FrameComponents frameComponents = FrameComponents.getInstance();
    private boolean isSaving = false;
    private XWPFDocument document;

    public void saveAllScreenshots() {
        if (!isSaving) {
            isSaving = true;

            new Thread(() -> {
                try {
                    if (config.isOpenedFolderPathActive()) {
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
        String previousTitle = frameComponents.getFrame().getTitle();

        if (!config.isOpenedFolderPathActive()) {
            String ticketNumber = DialogUtils.askForTicketNumber();
            if (isNull(ticketNumber)) {
                return;
            }
        }
        ConfigDetails configDetails = config.getConfigDetails();
        JFileChooser fileChooser = new JFileChooser(configDetails.getFileChooserLocation());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Save Screenshots");

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File folderToSave = getFolderName(fileChooser.getSelectedFile());
            saveScreenshots(folderToSave);

            configDetails.setFileChooserLocation(folderToSave.getAbsolutePath());
            config.saveConfig();
        } else {
            frameComponents.getFrame().setTitle(previousTitle);
        }
    }

    private void saveScreenshots() {
        saveScreenshots(new File(config.getConfigDetails().getOpenedFolderPath()));
    }

    private void saveScreenshots(File folder) {
        File[] existingFiles = folder.listFiles();

        frameComponents.getFrame().setTitle(folder.getName());

        if (folder.exists() || folder.mkdirs()) {
            config.getConfigDetails().setOpenedFolderPath(folder.getAbsolutePath());

            generateTextAndScreenshots(folder);

            DialogUtils.screenshotsSavedDialog(folder);
        } else {
            DialogUtils.cantSaveDialog(new Exception("Unable to create folder"));

            return;
        }

        if (nonNull(existingFiles)) {
            deleteOldFiles(existingFiles);
        }

        saveGeneratedTextToFile(folder);

        config.getConfigDetails().setOpenedFolderPath(folder.getAbsolutePath());
    }

    private void generateTextAndScreenshots(File folder) {
        document = new XWPFDocument();
        StringBuilder generatedText = new StringBuilder();

        Map<String, List<ScenarioPanel>> allPreviewPanels = frameComponents.getUsers().getAllScenarioPanels();

        ProgressBar progressBar = new ProgressBar(allPreviewPanels.keySet().size(), frameComponents.getFrame().getGraphicsConfiguration());

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
            FileUtils.writeStringToFile(new File(folder, GENERATED_TEXT_FILE_NAME), text, StandardCharsets.UTF_8);
            FileOutputStream out = new FileOutputStream( new File(folder, frameComponents.getFrame().getTitle() + "-Evidence.docx"));
            document.write(out);
            out.close();
            document.close();
        } catch (IOException e) {
            DialogUtils.cantSaveGeneratedText(e);
        }
    }

    public File getFolderName(File folder) {
        folder = new File(folder, frameComponents.getFrame().getTitle());

        File temporaryFolder = new File(folder.getAbsolutePath());

        int number = 1;
        while (temporaryFolder.exists()) {
            temporaryFolder = new File(folder.getAbsolutePath() + "-" + number);
            number++;
        }

        return temporaryFolder;
    }

    public void openScreenshots() {
        ConfigDetails configDetails = config.getConfigDetails();
        JFileChooser fileChooser = new JFileChooser(configDetails.getFileChooserLocation());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Open Screenshots");

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File folderToOpen = fileChooser.getSelectedFile();

            configDetails.setOpenedFolderPath(folderToOpen.getAbsolutePath());
            configDetails.setFileChooserLocation(folderToOpen.getParentFile().getAbsolutePath());
            config.saveConfig();

            frameComponents.getFrame().dispose();
            new TestingEvidenceApplication(folderToOpen);
        }
    }
}
