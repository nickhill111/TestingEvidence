package org.nickhill111.common.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static java.util.Objects.isNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ConfigDetails implements Configuration {
    @JsonProperty("globalConfigDetails")
    private GlobalConfigDetails globalConfigDetails;
    @JsonProperty("testManagerConfigDetails")
    private TestManagerConfigDetails testManagerConfigDetails;
    @JsonProperty("taskManagerConfigDetails")
    private TaskManagerConfigDetails taskManagerConfigDetails;

    @JsonIgnore
    public GlobalConfigDetails getGlobalConfigDetails() {
        if (isNull(globalConfigDetails)) {
            globalConfigDetails = new GlobalConfigDetails();
        }
        return globalConfigDetails;
    }

    @JsonIgnore
    public TestManagerConfigDetails getTestManagerConfigDetails() {
        if (isNull(testManagerConfigDetails)) {
            testManagerConfigDetails = new TestManagerConfigDetails();
        }
        return testManagerConfigDetails;
    }

    @JsonIgnore
    public TaskManagerConfigDetails getTaskManagerConfigDetails() {
        if (isNull(taskManagerConfigDetails)) {
            taskManagerConfigDetails = new TaskManagerConfigDetails();
        }
        return taskManagerConfigDetails;
    }
}
