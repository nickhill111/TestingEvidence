package org.nickhill111.testManager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TabNames {
    REGRESSION("Regression"),
    SCENARIO("Scenario");

    private final String value;
}
