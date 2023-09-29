package org.nickhill111.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.nickhill111.util.DialogUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class Config {
    private static Config INSTANCE;

    private static final String CONFIG_NAME = ".testingEvidence";
    private static final File CONFIG_PARENT_FILE = new File(System.getProperty("user.home"));
    private static final File CONFIG_FOLDER = new File(CONFIG_PARENT_FILE, CONFIG_NAME);
    private static final File CONFIG_FILE = new File(CONFIG_FOLDER, CONFIG_NAME + ".txt");

    private final ConfigDetails configDetails = readConfigDetails();

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
            return new ObjectMapper().readValue(new FileInputStream(CONFIG_FILE), ConfigDetails.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ConfigDetails();
    }

    public void saveConfig() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(configDetails);

            BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
