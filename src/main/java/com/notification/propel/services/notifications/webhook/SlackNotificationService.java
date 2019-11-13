package com.notification.propel.services.notifications.webhook;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.notification.propel.exceptions.WebhookMsgSendException;
import com.notification.propel.factories.TemplateProcessorFactory;
import com.notification.propel.messages.CustomMessages;
import com.notification.propel.models.db.webhook.WebhookConfig;
import com.notification.propel.models.dto.requests.WebhookRequestDTO;
import com.notification.propel.repositories.IWebhookConfigRepo;
import com.notification.propel.rest.clients.SlackRestClient;
import com.notification.propel.services.templates.processors.ITemplateProcessor;

@Service
public class SlackNotificationService implements IWebhookNotification {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IWebhookConfigRepo webhookConfigRepo;

    @Autowired
    private SlackRestClient slackRestClient;

    @Override
    public void sendEmailNotification(WebhookRequestDTO webhookRequestDTO) {
        Optional<WebhookConfig> config = webhookConfigRepo.findById(
                    webhookRequestDTO.getConfigId());
        if (config.isPresent()) {
            try {
                WebhookConfig webhookConfig = config.get();
                String messageBody = webhookConfig.getPayload();
                if (!StringUtils.isEmpty(webhookRequestDTO.getMessage())) {
                    messageBody = webhookRequestDTO.getMessage();
                }
                if (null != webhookRequestDTO.getTemplateDetails()
                            && webhookRequestDTO.getTemplateDetails().isTemplateUsed()) {

                    ITemplateProcessor templateProcessor = TemplateProcessorFactory.getTemplateProcessor(
                                webhookConfig.getTemplateEngineType());
                    messageBody = templateProcessor.processTemplate(
                                webhookConfig.getPayload(),
                                webhookRequestDTO.getTemplateDetails().getTemplatePlaceholders());
                }
                slackRestClient.sendMessage(webhookConfig, messageBody);

            } catch (Exception exception) {
                logger.error(
                            CustomMessages.WEBHOOK_MSG_SEND_OPERATION_FAILED.concat(
                                        CustomMessages.PLACEHOLDER),
                            exception.getMessage());
                throw new WebhookMsgSendException(
                            CustomMessages.WEBHOOK_MSG_SEND_OPERATION_FAILED);
            }

        }
    }

}
