package org.nickhill111.taskManager.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nickhill111.common.data.Configuration;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllTasks implements Configuration {
    private Tasks currentTasks;
    private Tasks completedTasks;
}
