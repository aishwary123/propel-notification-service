package com.notification.propel.interfaces;

import com.notification.propel.messages.CustomMessages;

/**
 * This class acts as a Utility class. It holds constant values that can be used
 * for creating API prefixes with versions.
 * 
 * @author aishwaryt
 */
public final class RestAPIPrefix {
    public static final String VERSION_1 = "/v1";
    public static final String REQUEST = "/request";
    public static final String CONFIG = "/config";
    public static final String TEMPLATE = "/template";

    public static final String NOTIFICATION_SERVICE_API_BASE = "/propel/notifications";
    public static final String API_BASE_V1 = NOTIFICATION_SERVICE_API_BASE
                + VERSION_1;
    public static final String API_BASE_V1_REQUEST_ENDPOINT = API_BASE_V1
                + REQUEST;
    public static final String API_BASE_V1_CONFIG_ENDPOINT = API_BASE_V1
                + CONFIG;
    public static final String API_BASE_V1_TEMPLATE_ENDPOINT = API_BASE_V1
                + TEMPLATE;

    private RestAPIPrefix() {
        throw new IllegalStateException(CustomMessages.UTILITY_CLASS);
    }
}
