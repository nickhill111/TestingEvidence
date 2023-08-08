package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TabNames {
    REGRESSION("Regression"),
    AC("AC");
    private final String value;
}
