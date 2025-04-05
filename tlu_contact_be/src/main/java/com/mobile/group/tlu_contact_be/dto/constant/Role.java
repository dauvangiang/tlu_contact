package com.mobile.group.tlu_contact_be.dto.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role implements BaseEnum<String> {
    STAFF("CBGV"),
    STUDENT("SV"),
    ADMIN("ADMIN");

    private final String value;

    Role(String value) {this.value = value;}

    @JsonCreator
    public static Role fromValue(String value) {
        for (Role column : values()) {
            if (column.value.equalsIgnoreCase(value))
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
