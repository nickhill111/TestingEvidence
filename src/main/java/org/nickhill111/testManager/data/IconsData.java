package org.nickhill111.testManager.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.nickhill111.common.data.Configuration;

import java.util.HashMap;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IconsData extends HashMap<String, List<ScenarioIconData>> implements Configuration {
}
