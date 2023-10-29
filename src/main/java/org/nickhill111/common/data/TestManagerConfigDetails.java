package org.nickhill111.common.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class TestManagerConfigDetails {
    @JsonProperty("frameConfigDetails")
    private FrameConfigDetails frameConfigDetails;
    @JsonProperty("previewFrameConfigDetails")
    private FrameConfigDetails previewFrameConfigDetails;
    @JsonProperty("previewZoomValue")
    private Integer previewZoomValue;
    @JsonProperty("fileChooserLocation")
    private String fileChooserLocation;
    @JsonProperty("openedFolderPath")
    private String openedFolderPath;
    @JsonProperty("selectedScreenId")
    private String selectedScreenId;
    @JsonProperty("splitPaneLocation")
    private Integer splitPaneLocation;

    @JsonIgnore
    public FrameConfigDetails getFrameConfigDetails() {
        if (isNull(frameConfigDetails)) {
            frameConfigDetails = new FrameConfigDetails();
        }
        return frameConfigDetails;
    }

    @JsonIgnore
    public FrameConfigDetails getPreviewFrameConfigDetails() {
        if (isNull(previewFrameConfigDetails)) {
            previewFrameConfigDetails = new FrameConfigDetails();
        }
        return previewFrameConfigDetails;
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
    public Integer getPreviewZoomValue() {
        if (isNull(previewZoomValue)) {
            previewZoomValue = 90;
        }

        return previewZoomValue;
    }

    @JsonIgnore
    public Integer getSplitPaneLocation() {
        if (isNull(splitPaneLocation)) {
            splitPaneLocation = 1000;
        }

        return splitPaneLocation;
    }
}
