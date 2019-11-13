package com.notification.propel.services.notifications.email;

import org.springframework.web.multipart.MultipartFile;

import com.notification.propel.models.dto.requests.EmailRequestDTO;

public interface IMimeEmailNotification {

    public void sendEmailNotification(final EmailRequestDTO emailRequestDTO,
                                      final MultipartFile[] files);
}
