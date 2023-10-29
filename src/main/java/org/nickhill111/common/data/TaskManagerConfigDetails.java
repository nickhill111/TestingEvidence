package org.nickhill111.common.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nickhill111.taskManager.data.Tasks;

import static java.util.Objects.isNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class TaskManagerConfigDetails {
    @JsonProperty("tasks")
    private Tasks tasks;

    @JsonProperty("frameConfigDetails")
    private FrameConfigDetails frameConfigDetails;

    @JsonIgnore
    public Tasks getTasks() {
        if (isNull(tasks)) {
            tasks = new Tasks();
        }
        return tasks;
    }

    @JsonIgnore
    public FrameConfigDetails getFrameConfigDetails() {
        if (isNull(frameConfigDetails)) {
            frameConfigDetails = new FrameConfigDetails();
        }
        return frameConfigDetails;
    }
}
