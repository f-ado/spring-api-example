package com.springapi.utils;

import org.apache.commons.lang3.EnumUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class StringUtils {

    private StringUtils() {
        // Nothing to do here.
    }

    public static <T extends Enum<T>> List<T> convertStringToEnumList(final String string, final Class<T> enumClazz) {
        if (string == null || string.length() == 0) {
            return Collections.emptyList();
        }

        List<T> enumResult = new ArrayList<>();
        Arrays.stream(string.split(",")).forEach(s -> {
            T value = EnumUtils.getEnum(enumClazz, s);
            if (value != null) {
                enumResult.add(value);
            }
        });

        return enumResult;
    }
}
