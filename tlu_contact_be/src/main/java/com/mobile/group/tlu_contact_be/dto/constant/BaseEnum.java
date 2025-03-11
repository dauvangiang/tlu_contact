package com.mobile.group.tlu_contact_be.dto.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public interface BaseEnum<T> {
    @JsonValue
    T toValue();
}
