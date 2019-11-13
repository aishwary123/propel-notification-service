package com.notification.propel.services.notifications.webhook;

import com.notification.propel.models.dto.requests.WebhookRequestDTO;

public interface IWebhookNotification {

    public void sendEmailNotification(final WebhookRequestDTO webhookRequestDTO);
}
