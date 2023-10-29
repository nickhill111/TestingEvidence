package org.nickhill111.common.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.nickhill111.taskManager.data.TaskManagerComponents;
import org.nickhill111.testManager.data.TestManagerComponents;
import org.nickhill111.common.util.DialogUtils;
import org.nickhill111.common.util.FileUtils;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import static java.util.Objects.nonNull;
import static org.nickhill111.common.util.FileUtils.deleteOldFiles;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
public class Config {
    private static Config INSTANCE;

    private static final String CONFIG_NAME = ".testingEvidence";
    private static final File CONFIG_PARENT_FILE = new File(System.getProperty("user.home"));
    public static final File CONFIG_FOLDER = new File(CONFIG_PARENT_FILE, CONFIG_NAME);
    private static final File CONFIG_FILE = new File(CONFIG_FOLDER, CONFIG_NAME + ".txt");

    private ConfigDetails configDetails = readConfigDetails();
    private boolean savingInProgress = false;

    public static Config getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Config();
        }

        return INSTANCE;
    }

    public static ConfigDetails readConfigDetails() {
        if (!CONFIG_FILE.exists() || !CONFIG_FILE.isFile()) {
            try {
                if (CONFIG_FOLDER.mkdirs() && !CONFIG_FILE.createNewFile()) {
                    DialogUtils.cantCreateConfig();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return new ConfigDetails();
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE);
            ConfigDetails readConfigDetails = new ObjectMapper().readValue(fileInputStream, ConfigDetails.class);
            fileInputStream.close();

            return readConfigDetails;
        } catch (Exception e) {
            if (e.getClass().equals(MismatchedInputException.class)) {
                if (FileUtils.deleteDirectory(CONFIG_FOLDER)) {
                    DialogUtils.configHasBeenReset();
                } else {
                    DialogUtils.cantDeleteConfig();
                }
            } else {
                e.printStackTrace();
            }
        }

        return new ConfigDetails();
    }

    public void saveConfig() {
        if (!savingInProgress) {
            savingInProgress = true;
            new Thread(() -> {
                saveConfigAfterCheck();
                savingInProgress = false;
            }).start();
        }
    }

    private void saveConfigAfterCheck() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(configDetails);

            if (!CONFIG_FOLDER.exists() && !CONFIG_FOLDER.mkdirs()) {
                DialogUtils.cantSaveConfig();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            if (!FileNotFoundException.class.equals(e.getClass())) {
                throw new RuntimeException(e);
            }
        }
    }

    public void reset() {
        configDetails = null;
        if (CONFIG_FOLDER.exists()) {
            deleteOldFiles(new File[]{CONFIG_FOLDER});
        }

        DialogUtils.configHasBeenReset();
    }

    public void saveTestManagerFrameConfigDetails() {
        saveFrameConfigDetails(getConfigDetails().getTestManagerConfigDetails().getFrameConfigDetails(),
            TestManagerComponents.getInstance().getFrame());
    }

    public void saveTaskManagerFrameConfigDetails() {
        saveFrameConfigDetails(getConfigDetails().getTaskManagerConfigDetails().getFrameConfigDetails(),
            TaskManagerComponents.getInstance().getFrame());
    }

    private void saveFrameConfigDetails(FrameConfigDetails frameConfigDetails, JFrame gui) {
        frameConfigDetails.setWindowSize(gui.getSize());
        frameConfigDetails.setWindowState(gui.getExtendedState());
        frameConfigDetails.setWindowScreenId(gui.getGraphicsConfiguration().getDevice().getIDstring());
        saveConfig();
    }

    public boolean isOpenedFolderPathActive() {
        String openedFolderPath = getConfigDetails().getTestManagerConfigDetails().getOpenedFolderPath();
        return nonNull(openedFolderPath) && new File(openedFolderPath).exists();
    }
}
