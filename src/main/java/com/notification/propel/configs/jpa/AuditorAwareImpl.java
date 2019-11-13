package com.notification.propel.configs.jpa;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.notification.propel.exceptions.AuditProcessingException;
import com.notification.propel.messages.CustomMessages;
import com.notification.propel.utils.JwtTokenUtil;
import com.notification.propel.utils.JwtTokenUtil.JWTTokenPayload;

class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String jwtToken = request.getHeader("Authorization").split(" ")[1];
            JWTTokenPayload tokenPayload = JwtTokenUtil.parseJwtToken(jwtToken);
            return Optional.of(tokenPayload.getUsername());
        } catch (Exception exception) {
            throw new AuditProcessingException(
                        CustomMessages.AUDIT_PROCESSING_FAILED);
        }
    }
}