package com.notification.propel.services.notifications.email;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.notification.propel.configs.MailServerConfig;
import com.notification.propel.exceptions.MailSendException;
import com.notification.propel.messages.CustomMessages;
import com.notification.propel.models.dto.commons.INotificationConfigDTO;
import com.notification.propel.models.dto.commons.INotificationTemplateDTO;
import com.notification.propel.models.dto.email.EmailConfigDTO;
import com.notification.propel.models.dto.requests.EmailRequestDTO;
import com.notification.propel.services.configs.INotificationConfigService;
import com.notification.propel.services.templates.INotificationTemplateService;

@Service
public class SimpleEmailNotificationService extends EmailNotificationService
            implements ISimpleEmailNotification {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public SimpleEmailNotificationService(@Qualifier("emailNotificationConfigService") final INotificationConfigService<INotificationConfigDTO, UUID> notificationConfigService,
                                          @Qualifier("emailNotificationTemplateService") final INotificationTemplateService<INotificationTemplateDTO, UUID> notificationTemplateService) {
        super(notificationConfigService, notificationTemplateService);

    }

    @Override
    @Retryable(value = {
                Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public void sendEmailNotification(EmailRequestDTO emailRequestDTO) {
        SimpleMailMessage message = new SimpleMailMessage();

        if (!StringUtils.isEmpty(emailRequestDTO.getSender())) {
            message.setFrom(emailRequestDTO.getSender());
        }
        if (!CollectionUtils.isEmpty(emailRequestDTO.getTo())) {
            message.setTo(emailRequestDTO.getTo().toArray(new String[0]));
        }

        if (!CollectionUtils.isEmpty(emailRequestDTO.getCc())) {
            message.setCc(emailRequestDTO.getCc().toArray(new String[0]));
        }

        if (!CollectionUtils.isEmpty(emailRequestDTO.getBcc())) {
            message.setBcc(emailRequestDTO.getBcc().toArray(new String[0]));
        }

        if (!StringUtils.isEmpty(emailRequestDTO.getReplyTo())) {
            message.setReplyTo(emailRequestDTO.getReplyTo());
        }
        message.setSubject(emailRequestDTO.getSubject());
        message.setText(getEmailBody(emailRequestDTO));

        final EmailConfigDTO emailConfigDTO = (EmailConfigDTO) getConfig(
                    emailRequestDTO);
        final JavaMailSender mailSender = MailServerConfig.getMailSender(
                    emailConfigDTO);
        try {
            mailSender.send(message);
        } catch (MailException mailException) {
            logger.error(
                        CustomMessages.MAIL_SEND_OPERATION_FAILED.concat(
                                    CustomMessages.PLACEHOLDER),
                        mailException.getMessage());
            throw new MailSendException(
                        CustomMessages.MAIL_SEND_OPERATION_FAILED);
        }
    }

}
