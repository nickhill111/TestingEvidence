package org.nickhill111.util;

import java.io.File;
import java.util.Arrays;

import static java.util.Objects.nonNull;

public class FileUtils {
    public static void deleteEntireFolder(File[] previousFiles) {
        for (File file : previousFiles) {
            boolean isFileDeleted = !file.isFile() ? deleteDirectory(file) : file.delete();

            if (!isFileDeleted) {
                DialogUtils.cantDeleteActiveFolder();
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
