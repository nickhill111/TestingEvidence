package org.nickhill111.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static java.util.Objects.isNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ConfigDetails implements Serializable {
    @JsonProperty("personalisation")
    private Personalisation personalisation;

    @JsonIgnore
    public Personalisation getPersonalisation() {
        if (isNull(personalisation)) {
            personalisation = new Personalisation();
        }
        return personalisation;
    }
}
