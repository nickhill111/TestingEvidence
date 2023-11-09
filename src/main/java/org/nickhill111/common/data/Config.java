package org.nickhill111.common.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.nickhill111.taskManager.data.AllTasks;
import org.nickhill111.taskManager.data.TaskManagerComponents;
import org.nickhill111.taskManager.gui.TaskTable;
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
    private static final String TASK_LIST_CONFIG_NAME = ".taskList";
    private static final File CONFIG_PARENT_FILE = new File(System.getProperty("user.home"));
    public static final File CONFIG_FOLDER = new File(CONFIG_PARENT_FILE, CONFIG_NAME);
    private static final File CONFIG_FILE = new File(CONFIG_FOLDER, CONFIG_NAME + ".txt");
    private static final File TASK_LIST_CONFIG_FILE = new File(CONFIG_FOLDER, TASK_LIST_CONFIG_NAME + ".txt");

    private ConfigDetails configDetails = readConfigDetails();
    private boolean savingInProgress = false;

    public static Config getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Config();
        }

        return INSTANCE;
    }

    public static AllTasks readTasks() {
        try {
            FileInputStream fileInputStream = getFileStream(TASK_LIST_CONFIG_FILE);
            if (nonNull(fileInputStream)) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.findAndRegisterModules();
                AllTasks tasks = objectMapper.readValue(fileInputStream, AllTasks.class);
                fileInputStream.close();

                return tasks;
            }
        } catch (Exception e) {
            handleConfigReadException(e);
        }

        return new AllTasks();
    }

    private static ConfigDetails readConfigDetails() {
        try {
            FileInputStream fileInputStream = getFileStream(CONFIG_FILE);
            if (nonNull(fileInputStream)) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.findAndRegisterModules();
                ConfigDetails configDetails = objectMapper.readValue(fileInputStream, ConfigDetails.class);
                fileInputStream.close();

                return configDetails;
            }
        } catch (Exception e) {
            handleConfigReadException(e);
        }

        return new ConfigDetails();
    }

    private static FileInputStream getFileStream(File configFile) throws FileNotFoundException {
        if (!configFile.exists() || !configFile.isFile()) {
            try {
                if (CONFIG_FOLDER.mkdirs() && !configFile.createNewFile()) {
                    DialogUtils.cantCreateConfig();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return null;
        }

        return new FileInputStream(configFile);
    }

    private static void handleConfigReadException(Exception e) {
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

    public void saveConfigDetails() {
        saveConfigDetails(configDetails, CONFIG_FILE);
    }

    public void saveTaskList() {
        TaskManagerComponents taskManagerComponents = TaskManagerComponents.getInstance();
        TaskTable taskTable = taskManagerComponents.getCurrentTaskTable();
        taskTable.selectCurrentRow();

        AllTasks tasks = new AllTasks(taskManagerComponents.getCurrentTaskTable().getTasks(),
            taskManagerComponents.getCompletedTaskTable().getTasks());

        saveConfigDetails(tasks, TASK_LIST_CONFIG_FILE);
    }

    private void saveConfigDetails(Configuration configuration, File file) {
        if (!savingInProgress) {
            savingInProgress = true;
            new Thread(() -> {
                saveConfigAfterCheck(configuration, file);
                savingInProgress = false;
            }).start();
        }
    }

    public void saveConfigAfterCheck(Configuration configuration, File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(configuration);

            if (!CONFIG_FOLDER.exists() && !CONFIG_FOLDER.mkdirs()) {
                DialogUtils.cantSaveConfig();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
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
        saveConfigDetails();
    }

    public boolean isOpenedFolderPathActive() {
        String openedFolderPath = getConfigDetails().getTestManagerConfigDetails().getOpenedFolderPath();
        return nonNull(openedFolderPath) && new File(openedFolderPath).exists();
    }
}
