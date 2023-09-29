package org.nickhill111.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ConfigDetails implements Serializable {
    @JsonProperty("personalisation")
    private Personalisation personalisation;
    @JsonProperty("fileChooserLocation")
    private String fileChooserLocation;
    @JsonProperty("openedFolderPath")
    private String openedFolderPath;
    @JsonProperty("selectedScreenId")
    private String selectedScreenId;
    @JsonProperty("selectedZoomIndex")
    private Integer selectedZoomIndex;

    @JsonIgnore
    public Personalisation getPersonalisation() {
        if (isNull(personalisation)) {
            personalisation = new Personalisation();
        }
        return personalisation;
    }

    @JsonIgnore
    public String getFileChooserLocation() {
        if (nonNull(fileChooserLocation)) {
            File fileChooser = new File(fileChooserLocation);

            if (!fileChooser.isDirectory()) {
                fileChooserLocation = null;
            }
        }

        return fileChooserLocation;
    }

    @JsonIgnore
    public String getOpenedFolderPath() {
        if (nonNull(openedFolderPath)) {
            File openedFolder = new File(openedFolderPath);

            if (!openedFolder.isDirectory() || Objects.requireNonNull(openedFolder.listFiles()).length == 0) {
                openedFolderPath = null;
            }
        }

        return openedFolderPath;
    }

    @JsonIgnore
    public int getSelectedZoomIndex() {
        if (isNull(selectedZoomIndex)) {
            selectedZoomIndex = 2;
        }

        return selectedZoomIndex;
    }
}
