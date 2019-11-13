package com.notification.propel.controllers.request;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notification.propel.interfaces.RestAPIPrefix;
import com.notification.propel.messages.CustomMessages;
import com.notification.propel.models.dto.requests.WebhookRequestDTO;
import com.notification.propel.services.notifications.webhook.IWebhookNotification;

@RestController
@RequestMapping(value = WebhookRequestController.REST_ENDPOINT)
public class WebhookRequestController {

    public static final String REST_ENDPOINT = RestAPIPrefix.API_BASE_V1_REQUEST_ENDPOINT
                + "/webhook";

    @Autowired
    private IWebhookNotification webhookNotification;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendEmail(@Valid @RequestBody WebhookRequestDTO webhookRequestDTO) {

        webhookNotification.sendEmailNotification(webhookRequestDTO);
        return new ResponseEntity<>(
                    CustomMessages.WEBHOOK_MSG_SENT_SUCCESSFULLY,
                    HttpStatus.OK);
    }
}
