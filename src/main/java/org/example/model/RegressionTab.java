package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegressionTab {
    ACTION_FURTHER_EVIDENCE(-1, "Action Further Evidence"),
    ISSUE_DIRECTION_NOTICE(-2, "Issue Direction Notice"),
    UPLOAD_RESPONSE(-3, "Upload Response"),
    CREATE_BUNDLE(-4, "Create Bundle"),
    ADJOURNMENT_NOTICE(-5, "Adjournment Notice"),
    FINAL_DECISION(-6, "Final Decision"),
    OTHER(-7, "Other");

    private final int id;
    private final String value;
}
