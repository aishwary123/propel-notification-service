package com.notification.propel.services.configs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notification.propel.exceptions.EntityProcessingFailedException;
import com.notification.propel.messages.CustomMessages;
import com.notification.propel.models.db.commons.INotificationConfig;
import com.notification.propel.models.db.webhook.WebhookConfig;
import com.notification.propel.models.dto.commons.INotificationConfigDTO;
import com.notification.propel.models.dto.webhook.WebhookConfigDTO;
import com.notification.propel.models.dto.webhook.WebhookConfigUpdateDTO;
import com.notification.propel.repositories.IWebhookConfigRepo;

@Service
public class WebhookNotificationConfigService implements
            INotificationConfigService<INotificationConfigDTO, UUID> {

    private IWebhookConfigRepo webhookConfigRepo;
    private ModelMapper modelMapper;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public WebhookNotificationConfigService(final IWebhookConfigRepo webhookConfigRepo,
                                            final ModelMapper modelMapper) {
        this.webhookConfigRepo = webhookConfigRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public INotificationConfigDTO createConfig(INotificationConfigDTO config) {

        WebhookConfigDTO webhookConfigDTO = (WebhookConfigDTO) config;
        // If we create a configuration and consider it as default. We mark the
        // previous default as
        // non-default if there is any default configuration.
        if (webhookConfigDTO.isDefault() && webhookConfigRepo.count() > 0) {
            unmarkCurrentDefault();
        }

        // If this is the first configuration then we make it as a default.
        if (webhookConfigRepo.count() == 0) {
            webhookConfigDTO.setDefault(true);
        }
        WebhookConfig webhookConfig = (WebhookConfig) convertDTOtoDAO(config);
        webhookConfig = webhookConfigRepo.save(webhookConfig);
        final String message = new CustomMessages.CustomMessagesBuilder().addMessage(
                    CustomMessages.WEBHOOK_NOTIFICATION_CONFIG).addMessage(
                                CustomMessages.EntityOperation.CREATION.name()).addMessage(
                                            CustomMessages.OperationResultStatus.SUCCESS.name()).build();
        logger.info(message);
        return convertDAOtoDTO(webhookConfig);
    }

    @Override
    @Transactional
    public INotificationConfigDTO updateConfig(UUID configId,
                                               INotificationConfigDTO config) {
        // Fetch the configuration
        WebhookConfigDTO currentSavedConfig = (WebhookConfigDTO) getConfig(
                    configId);
        WebhookConfigDTO webhookConfigInRequest = (WebhookConfigDTO) config;

        // If the existing value is not default and new value is default, we
        // mark the current default value as non-default.
        if (!currentSavedConfig.isDefault()
                    && webhookConfigInRequest.isDefault()) {
            unmarkCurrentDefault();
        }

        // If the existing value was default but the new value passed is not
        // default, then we check if there exist any entry other than the
        // configuration we are processing. If there exists any configuration we
        // make it as default,otherwise raise exception.
        if (currentSavedConfig.isDefault()
                    && !webhookConfigInRequest.isDefault()) {

            Optional<WebhookConfig> webhookConfig = webhookConfigRepo.findOneConfigByIdNotEquals(
                        configId);
            if (webhookConfig.isPresent()) {
                WebhookConfig someOtherConfig = webhookConfig.get();
                someOtherConfig.setDefault(true);
                webhookConfigRepo.save(someOtherConfig);
            } else {
                throw new EntityProcessingFailedException(
                            CustomMessages.LAST_CONFIG_CAN_NOT_BE_NON_DEFAULT);
            }
        }

        WebhookConfig webhookConfig = (WebhookConfig) convertDTOtoDAO(config);

        webhookConfig.setId(configId);

        // Maintain the original audit info to avoid override
        webhookConfig.setCreatedBy(currentSavedConfig.getCreatedBy());
        webhookConfig.setCreatedDate(currentSavedConfig.getCreatedDate());

        webhookConfig = webhookConfigRepo.save(webhookConfig);
        final String message = new CustomMessages.CustomMessagesBuilder().addMessage(
                    CustomMessages.WEBHOOK_NOTIFICATION_CONFIG).addMessage(
                                CustomMessages.EntityOperation.UPDATION.name()).addMessage(
                                            CustomMessages.OperationResultStatus.SUCCESS.name()).build();
        logger.info(message);
        return modelMapper.map(webhookConfig, WebhookConfigDTO.class);
    }

    @Override
    public INotificationConfigDTO getConfig(UUID configId) {
        Optional<WebhookConfig> config = webhookConfigRepo.findById(configId);
        if (!config.isPresent()) {
            final String message = new CustomMessages.CustomMessagesBuilder().addMessage(
                        CustomMessages.WEBHOOK_NOTIFICATION_CONFIG).addMessage(
                                    CustomMessages.EntityOperation.READ.name()).addMessage(
                                                CustomMessages.OperationResultStatus.FAILED.name()).build();
            logger.info(message);
            throw new EntityProcessingFailedException(
                        CustomMessages.ENTITY_NOT_FOUND);
        }
        return convertDAOtoDTO(config.get());
    }

    @Override
    public List<INotificationConfigDTO> getAllConfig() {
        List<WebhookConfig> configs = webhookConfigRepo.findAll();
        return convertDAOListToDTOList(configs);
    }

    @Override
    @Transactional
    public void deleteConfig(UUID configId) {
        // Validate the presence of record
        INotificationConfigDTO notificationConfigDTO = getConfig(configId);
        webhookConfigRepo.deleteById(configId);

        // If the entity requested to delete is default then we choose some
        // other configuration as the default one if there is exists any
        // configuration after deletion.
        WebhookConfigDTO webhookConfigDTO = (WebhookConfigDTO) notificationConfigDTO;
        if (webhookConfigDTO.isDefault() && webhookConfigRepo.count() > 0) {
            WebhookConfig webhookConfig = webhookConfigRepo.findAll().get(0);
            webhookConfig.setDefault(true);
            webhookConfigRepo.save(webhookConfig);
        }
        final String message = new CustomMessages.CustomMessagesBuilder().addMessage(
                    CustomMessages.WEBHOOK_NOTIFICATION_CONFIG).addMessage(
                                CustomMessages.EntityOperation.DELETION.name()).addMessage(
                                            CustomMessages.OperationResultStatus.SUCCESS.name()).build();
        logger.info(message);

    }

    protected INotificationConfig convertDTOtoDAO(INotificationConfigDTO notificationConfigDTO) {
        WebhookConfigDTO webhookConfigDTO = (WebhookConfigDTO) notificationConfigDTO;
        return modelMapper.map(webhookConfigDTO, WebhookConfig.class);
    }

    protected INotificationConfigDTO convertDAOtoDTO(INotificationConfig notificationConfig) {
        return modelMapper.map(notificationConfig,
                    WebhookConfigUpdateDTO.class);
    }

    protected List<INotificationConfigDTO> convertDAOListToDTOList(List<? extends INotificationConfig> configList) {
        Type listType = new TypeToken<List<WebhookConfigUpdateDTO>>() {
        }.getType();
        return modelMapper.map(configList, listType);
    }

    @Override
    public INotificationConfigDTO getDefaultConfig() {
        Optional<WebhookConfig> defaultWebhookConfig = webhookConfigRepo.findDefaultConfig();
        if (!defaultWebhookConfig.isPresent()) {
            final String message = new CustomMessages.CustomMessagesBuilder().addMessage(
                        CustomMessages.DEFAULT_WEBHOOK_TEMPLATE).addMessage(
                                    CustomMessages.EntityOperation.READ.name()).addMessage(
                                                CustomMessages.OperationResultStatus.FAILED.name()).build();
            logger.info(message);
            throw new EntityProcessingFailedException(
                        CustomMessages.DEFAULT_ENTITY_NOT_FOUND);
        }
        return modelMapper.map(defaultWebhookConfig.get(),
                    WebhookConfigUpdateDTO.class);
    }

    @Override
    @Transactional
    public INotificationConfigDTO changeDefaultConfig(UUID configId) {
        // Fetching the configuration that has to be selected as default
        INotificationConfigDTO notificationConfigDTO = getConfig(configId);
        WebhookConfigDTO webhookConfigDTO = (WebhookConfigDTO) notificationConfigDTO;

        // Fetching the current default configuration
        INotificationConfigDTO defaultConfigDTO = getDefaultConfig();
        WebhookConfigUpdateDTO defaultWebhookConfigDTO = (WebhookConfigUpdateDTO) defaultConfigDTO;

        // Switching the default flag
        webhookConfigDTO.setDefault(true);
        defaultWebhookConfigDTO.setDefault(false);

        WebhookConfig currDefaultWebhookConfig = modelMapper.map(
                    webhookConfigDTO, WebhookConfig.class);
        WebhookConfig prevDefaultWebhookConfig = modelMapper.map(
                    defaultWebhookConfigDTO, WebhookConfig.class);
        List<WebhookConfig> configList = new ArrayList<>();
        configList.add(currDefaultWebhookConfig);
        configList.add(prevDefaultWebhookConfig);
        webhookConfigRepo.saveAll(configList);
        return webhookConfigDTO;
    }

    /**
     * It will mark the current default configuration as non-default.
     */
    private void unmarkCurrentDefault() {
        WebhookConfigDTO prevDefaultConfigDTO = (WebhookConfigDTO) getDefaultConfig();
        prevDefaultConfigDTO.setDefault(false);
        WebhookConfig prevDefaultConfig = modelMapper.map(prevDefaultConfigDTO,
                    WebhookConfig.class);
        webhookConfigRepo.save(prevDefaultConfig);
    }
}
