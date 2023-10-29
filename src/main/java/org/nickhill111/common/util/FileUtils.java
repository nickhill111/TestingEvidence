package org.nickhill111.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import static java.util.Objects.nonNull;
import static org.nickhill111.testManager.model.TabNames.SCENARIO;

public class FileUtils {
    public final static String GENERATED_TEXT_FILE_NAME = "GeneratedText.txt";

    public static void deleteOldFiles(File[] previousFiles) {
        for (File file : previousFiles) {
            if (file.exists()) {
                boolean isFileDeleted = file.isFile() ? file.delete() : deleteDirectory(file);

                if (!isFileDeleted) {
                    DialogUtils.cantDeleteFile(file.getAbsolutePath());
                }
            }
        }
    }

    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();

        if (nonNull(allContents)) {
            Arrays.stream(allContents).forEachOrdered(FileUtils::deleteDirectory);
        }

        return directoryToBeDeleted.delete();
    }

    public static String getScenariosFromFile(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (nonNull(line)) {
                if (line.startsWith(SCENARIO.getValue() + "_")) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                }

                line = br.readLine();
            }
            br.close();

            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
