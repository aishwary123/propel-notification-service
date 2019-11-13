package com.notification.propel.services.notifications.email;

import java.util.UUID;

import org.springframework.util.StringUtils;

import com.notification.propel.factories.TemplateProcessorFactory;
import com.notification.propel.models.dto.commons.INotificationConfigDTO;
import com.notification.propel.models.dto.commons.INotificationTemplateDTO;
import com.notification.propel.models.dto.email.EmailTemplateDTO;
import com.notification.propel.models.dto.requests.EmailRequestDTO;
import com.notification.propel.services.configs.INotificationConfigService;
import com.notification.propel.services.templates.INotificationTemplateService;
import com.notification.propel.services.templates.processors.ITemplateProcessor;

public abstract class EmailNotificationService {

    protected INotificationConfigService<INotificationConfigDTO, UUID> notificationConfigService;
    protected INotificationTemplateService<INotificationTemplateDTO, UUID> notificationTemplateService;

    public EmailNotificationService(final INotificationConfigService<INotificationConfigDTO, UUID> notificationConfigService,
                                    final INotificationTemplateService<INotificationTemplateDTO, UUID> notificationTemplateService) {
        this.notificationConfigService = notificationConfigService;
        this.notificationTemplateService = notificationTemplateService;
    }

    protected INotificationConfigDTO getConfig(final EmailRequestDTO emailRequestDTO) {
        if (null != emailRequestDTO.getConfigId()) {
            return notificationConfigService.getConfig(
                        emailRequestDTO.getConfigId());
        } else {
            return notificationConfigService.getDefaultConfig();
        }
    }

    protected String getEmailBody(final EmailRequestDTO emailRequestDTO) {
        String emailBody = "Sent From Propel Notification Service";
        if (!StringUtils.isEmpty(emailRequestDTO.getBody())) {
            emailBody = emailRequestDTO.getBody();
            return emailBody;
        }

        if (null != emailRequestDTO.getTemplateDetails()
                    && emailRequestDTO.getTemplateDetails().isTemplateUsed()) {
            EmailTemplateDTO emailTemplateDTO = null;
            if (null != emailRequestDTO.getTemplateDetails().getTemplateId()) {
                emailTemplateDTO = (EmailTemplateDTO) notificationTemplateService.getTemplate(
                            emailRequestDTO.getTemplateDetails().getTemplateId());
            } else {
                emailTemplateDTO = (EmailTemplateDTO) notificationTemplateService.getDefaultTemplate();
            }
            ITemplateProcessor templateProcessor = TemplateProcessorFactory.getTemplateProcessor(
                        emailTemplateDTO.getTemplateEngineType());
            emailBody = templateProcessor.processTemplate(
                        emailTemplateDTO.getTemplateBody(),
                        emailRequestDTO.getTemplateDetails().getTemplatePlaceholders());
        }
        return emailBody;
    }

}
