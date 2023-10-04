package org.nickhill111.util;

import java.io.File;
import java.util.Arrays;

import static java.util.Objects.nonNull;

public class FileUtils {
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

    private static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();

        if (nonNull(allContents)) {
            Arrays.stream(allContents).forEachOrdered(FileUtils::deleteDirectory);
        }

        return directoryToBeDeleted.delete();
    }
}
