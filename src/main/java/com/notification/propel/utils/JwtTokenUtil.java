package com.notification.propel.utils;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.propel.exceptions.JwtParsingException;
import com.notification.propel.messages.CustomMessages;

import lombok.Data;

public class JwtTokenUtil {

    private JwtTokenUtil() {
        throw new IllegalStateException(CustomMessages.UTILITY_CLASS);
    }

    public static JWTTokenPayload parseJwtToken(final String jwtToken) {
        try {
            String[] jwtTokenSections = jwtToken.split("[.]");
            String jwtPayload = jwtTokenSections[1];
            byte[] jwtPayloadInBytes = Base64.getDecoder().decode(jwtPayload);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jwtPayloadInBytes, JWTTokenPayload.class);

        } catch (IOException exception) {
            throw new JwtParsingException(
                        CustomMessages.JWT_TOKEN_PARSING_FAILED);
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JWTTokenPayload {
        private String currentOrgId;
        private String defaultOrgId;
        private String orgRole;
        private String sub;
        private String username;
    }
}
