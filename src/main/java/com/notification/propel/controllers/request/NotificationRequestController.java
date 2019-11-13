package com.notification.propel.controllers.request;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.notification.propel.interfaces.RestAPIPrefix;
import com.notification.propel.messages.CustomMessages;
import com.notification.propel.models.dto.requests.EmailRequestDTO;
import com.notification.propel.services.notifications.email.IMimeEmailNotification;
import com.notification.propel.validators.IValidator;
import com.notification.propel.validators.email.AttachmentsValidator;
import com.notification.propel.validators.email.EmailIDFormatValidator;
import com.notification.propel.validators.email.EmailTemplateValidator;
import com.notification.propel.validators.email.FileLimitValidator;

@RestController
@RequestMapping(value = NotificationRequestController.REST_ENDPOINT)
public class NotificationRequestController {

    public static final String REST_ENDPOINT = RestAPIPrefix.API_BASE_V1_REQUEST_ENDPOINT
                + "/email";
    @Autowired
    private IMimeEmailNotification mimeEmailNotification;

    private IValidator<EmailRequestDTO> validator;

    public NotificationRequestController() {
        validator = new EmailIDFormatValidator(
                    new AttachmentsValidator(new EmailTemplateValidator()));
    }

    @PostMapping
    public ResponseEntity<String> sendEmail(@Valid @RequestPart(value = "requestDetails") EmailRequestDTO emailRequestDTO,
                                            @RequestPart(value = "attachments") MultipartFile[] files) {

        validator.validate(emailRequestDTO);
        FileLimitValidator.validate(emailRequestDTO, files);
        mimeEmailNotification.sendEmailNotification(emailRequestDTO, files);
        return new ResponseEntity<>(CustomMessages.EMAIL_SENT_SUCCESSFULLY,
                    HttpStatus.OK);
    }
}
