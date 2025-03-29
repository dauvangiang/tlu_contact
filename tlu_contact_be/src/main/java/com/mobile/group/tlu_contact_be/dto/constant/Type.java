package com.mobile.group.tlu_contact_be.dto.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Type implements BaseEnum<String> {
    DEPARTMENT("DEP"),
    STAFF("STAFF"),
    STUDENT("STUDENT");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Type fromValue(String value) {
        for (Type column : values()) {
            if (column.value.equals(value))
                return column;
        }
        throw new RuntimeException();
    }

    @Override
    @JsonValue
    public String toValue() {
        return value;
    }
}
