package com.notification.propel.utils;

import org.springframework.util.StringUtils;

import com.notification.propel.exceptions.VariableNotFoundException;
import com.notification.propel.messages.CustomMessages;

/**
 * System Utility class provides all the system related functionalities. Few
 * functionalities like 1. Getting environment variable if not found then
 * default 2. Getting environment variable if not found then it will raise
 * exception
 * 
 * @author aishwaryt
 */
public class SystemUtils {

    private SystemUtils() {
        throw new IllegalStateException(CustomMessages.UTILITY_CLASS);
    }

    public static String getSystemVariableOrDefault(final String property,
                                                    final String defaultValue) {
        try {
            return getSystemVariable(property);
        } catch (final VariableNotFoundException variableNotFoundException) {
            return defaultValue;
        }
    }

    public static String getSystemVariable(final String property) {
        final String value = System.getenv(property);
        if (StringUtils.isEmpty(value)) {
            throw new VariableNotFoundException(property.concat(" ").concat(
                        CustomMessages.SYSTEM_VARIABLE_NOT_FOUND));
        }
        return value;
    }
}
