package com.notification.propel.services.notifications.email;

import com.notification.propel.models.dto.requests.EmailRequestDTO;

public interface ISimpleEmailNotification {

    public void sendEmailNotification(final EmailRequestDTO emailRequestDTO);
}
