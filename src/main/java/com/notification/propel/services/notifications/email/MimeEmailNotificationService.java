package com.notification.propel.services.notifications.email;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
public class MimeEmailNotificationService extends EmailNotificationService
            implements IMimeEmailNotification {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public MimeEmailNotificationService(@Qualifier("emailNotificationConfigService") final INotificationConfigService<INotificationConfigDTO, UUID> notificationConfigService,
                                        @Qualifier("emailNotificationTemplateService") final INotificationTemplateService<INotificationTemplateDTO, UUID> notificationTemplateService) {
        super(notificationConfigService, notificationTemplateService);

    }

    @Override
    @Retryable(value = {
                Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public void sendEmailNotification(EmailRequestDTO emailRequestDTO,
                                      MultipartFile[] files) {
        try {
            EmailConfigDTO emailConfig = (EmailConfigDTO) getConfig(
                        emailRequestDTO);
            JavaMailSender mailSender = MailServerConfig.getMailSender(
                        emailConfig);
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(emailRequestDTO.getSender());
            helper.setTo(emailRequestDTO.getTo().toArray(new String[0]));

            if (!CollectionUtils.isEmpty(emailRequestDTO.getCc())) {
                helper.setCc(emailRequestDTO.getCc().toArray(new String[0]));
            }

            if (!CollectionUtils.isEmpty(emailRequestDTO.getBcc())) {
                helper.setBcc(emailRequestDTO.getBcc().toArray(new String[0]));
            }

            if (!StringUtils.isEmpty(emailRequestDTO.getReplyTo())) {
                helper.setReplyTo(emailRequestDTO.getReplyTo());
            }

            helper.setSubject(emailRequestDTO.getSubject());

            if (emailRequestDTO.isBodyInHTML()) {
                helper.setText(getEmailBody(emailRequestDTO), true);
            } else {
                helper.setText(getEmailBody(emailRequestDTO));
            }
            includeAttachments(emailRequestDTO, files, helper);
            mailSender.send(message);
        } catch (MessagingException exception) {
            logger.error(
                        CustomMessages.MAIL_SEND_OPERATION_FAILED.concat(
                                    CustomMessages.PLACEHOLDER),
                        exception.getMessage());
            throw new MailSendException(
                        CustomMessages.MAIL_SEND_OPERATION_FAILED);
        }
    }

    private void includeAttachments(final EmailRequestDTO emailRequestDTO,
                                    MultipartFile[] files,
                                    MimeMessageHelper helper)
        throws MessagingException {
        // Add files passed in the request
        // Only files passed in request can be added as inline
        if (null != files) {
            for (MultipartFile file : files) {
                if (null != emailRequestDTO.getAttachmentDetails()
                            && emailRequestDTO.getAttachmentDetails().isInlineAttachmentsEnabled()) {

                    helper.addInline(file.getOriginalFilename(),
                                new InputStreamSource() {

                                    @Override
                                    public InputStream getInputStream()
                                        throws IOException {
                                        return file.getInputStream();
                                    }
                                }, file.getContentType());

                } else {
                    helper.addAttachment(file.getOriginalFilename(), file);
                }
            }
        }

        if (null != emailRequestDTO.getAttachmentDetails()) {

            // Add attachment for files whose links were added as URL
            for (Map.Entry<String, String> entry : emailRequestDTO.getAttachmentDetails().getAttachmentLinks().entrySet()) {

                helper.addAttachment(entry.getKey(), new InputStreamSource() {

                    @Override
                    public InputStream getInputStream()
                        throws IOException {
                        URL url = new URL(entry.getValue());
                        return url.openConnection().getInputStream();
                    }
                });

            }
        }
    }

}
