package com.mobile.group.tlu_contact_be.utils;

import com.mobile.group.tlu_contact_be.dto.constant.Type;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class Utils {
    public static String genCode(Type type) {
        String year = String.valueOf(LocalDateTime.now().getYear()).substring(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
        String time = LocalDateTime.now().format(formatter);
        String randomNum = String.format("%03d", new Random().nextInt(1000));

        return type.toValue() + year + time + randomNum;
    }
}
