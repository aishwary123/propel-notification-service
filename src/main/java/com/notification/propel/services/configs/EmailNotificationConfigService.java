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
import com.notification.propel.models.db.email.EmailConfig;
import com.notification.propel.models.dto.commons.INotificationConfigDTO;
import com.notification.propel.models.dto.email.EmailConfigDTO;
import com.notification.propel.models.dto.email.EmailConfigUpdateDTO;
import com.notification.propel.repositories.IEmailConfigRepo;

@Service
public class EmailNotificationConfigService implements
            INotificationConfigService<INotificationConfigDTO, UUID> {

    private IEmailConfigRepo emailConfigRepo;
    private ModelMapper modelMapper;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public EmailNotificationConfigService(final IEmailConfigRepo emailConfigRepo,
                                          final ModelMapper modelMapper) {
        this.emailConfigRepo = emailConfigRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public INotificationConfigDTO createConfig(INotificationConfigDTO config) {
        EmailConfigDTO emailConfigDTO = (EmailConfigDTO) config;

        // If we create a configuration and consider it as default. We mark the
        // previous default as
        // non-default if there is any default configuration.
        if (emailConfigDTO.isDefault() && emailConfigRepo.count() > 0) {
            unmarkCurrentDefault();
        }

        // If this is the first configuration then we make it as a default.
        if (emailConfigRepo.count() == 0) {
            emailConfigDTO.setDefault(true);
        }
        EmailConfig emailConfig = (EmailConfig) convertDTOtoDAO(config);
        emailConfig = emailConfigRepo.save(emailConfig);
        final String message = new CustomMessages.CustomMessagesBuilder().addMessage(
                    CustomMessages.EMAIL_NOTIFICATION_CONFIG).addMessage(
                                CustomMessages.EntityOperation.CREATION.name()).addMessage(
                                            CustomMessages.OperationResultStatus.SUCCESS.name()).build();
        logger.info(message);
        return convertDAOtoDTO(emailConfig);
    }

    @Override
    @Transactional
    public INotificationConfigDTO updateConfig(UUID configId,
                                               INotificationConfigDTO config) {
        // Fetch the configuration
        EmailConfigDTO currentSavedConfig = (EmailConfigDTO) getConfig(
                    configId);
        EmailConfigDTO emailConfigInRequest = (EmailConfigDTO) config;

        // If the existing value is not default and new value is default, we
        // mark the current default value as non-default.
        if (!currentSavedConfig.isDefault()
                    && emailConfigInRequest.isDefault()) {
            unmarkCurrentDefault();
        }

        // If the existing value was default but the new value passed is not
        // default, then we check if there exist any entry other than the
        // configuration we are processing. If there exists any configuration we
        // make it as default,otherwise raise exception.
        if (currentSavedConfig.isDefault()
                    && !emailConfigInRequest.isDefault()) {

            Optional<EmailConfig> emailConfig = emailConfigRepo.findOneConfigByIdNotEquals(
                        configId);
            if (emailConfig.isPresent()) {
                EmailConfig someOtherConfig = emailConfig.get();
                someOtherConfig.setDefault(true);
                emailConfigRepo.save(someOtherConfig);
            } else {
                throw new EntityProcessingFailedException(
                            CustomMessages.LAST_CONFIG_CAN_NOT_BE_NON_DEFAULT);
            }
        }

        EmailConfig emailConfig = (EmailConfig) convertDTOtoDAO(config);

        emailConfig.setId(configId);

        // Maintain the original audit info to avoid override
        emailConfig.setCreatedBy(currentSavedConfig.getCreatedBy());
        emailConfig.setCreatedDate(currentSavedConfig.getCreatedDate());

        emailConfig = emailConfigRepo.save(emailConfig);
        final String message = new CustomMessages.CustomMessagesBuilder().addMessage(
                    CustomMessages.EMAIL_NOTIFICATION_CONFIG).addMessage(
                                CustomMessages.EntityOperation.UPDATION.name()).addMessage(
                                            CustomMessages.OperationResultStatus.SUCCESS.name()).build();
        logger.info(message);
        return modelMapper.map(emailConfig, EmailConfigUpdateDTO.class);
    }

    @Override
    public INotificationConfigDTO getConfig(UUID configId) {
        Optional<EmailConfig> config = emailConfigRepo.findById(configId);
        if (!config.isPresent()) {
            final String message = new CustomMessages.CustomMessagesBuilder().addMessage(
                        CustomMessages.EMAIL_NOTIFICATION_CONFIG).addMessage(
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
        List<EmailConfig> configs = emailConfigRepo.findAll();
        return convertDAOListToDTOList(configs);
    }

    @Override
    @Transactional
    public void deleteConfig(UUID configId) {
        // Validate the presence of record
        INotificationConfigDTO notificationConfigDTO = getConfig(configId);
        emailConfigRepo.deleteById(configId);
        // If the entity requested to delete is default then we choose some
        // other configuration as the default one if there is exists any
        // configuration after deletion.
        EmailConfigDTO emailConfigDTO = (EmailConfigDTO) notificationConfigDTO;
        if (emailConfigDTO.isDefault() && emailConfigRepo.count() > 0) {
            EmailConfig emailConfig = emailConfigRepo.findAll().get(0);
            emailConfig.setDefault(true);
            emailConfigRepo.save(emailConfig);
        }
        final String message = new CustomMessages.CustomMessagesBuilder().addMessage(
                    CustomMessages.EMAIL_NOTIFICATION_CONFIG).addMessage(
                                CustomMessages.EntityOperation.DELETION.name()).addMessage(
                                            CustomMessages.OperationResultStatus.SUCCESS.name()).build();
        logger.info(message);

    }

    protected INotificationConfig convertDTOtoDAO(INotificationConfigDTO notificationConfigDTO) {
        EmailConfigDTO emailConfigDTO = (EmailConfigDTO) notificationConfigDTO;
        return modelMapper.map(emailConfigDTO, EmailConfig.class);
    }

    protected INotificationConfigDTO convertDAOtoDTO(INotificationConfig notificationConfig) {
        return modelMapper.map(notificationConfig, EmailConfigUpdateDTO.class);
    }

    protected List<INotificationConfigDTO> convertDAOListToDTOList(List<? extends INotificationConfig> configList) {
        Type listType = new TypeToken<List<EmailConfigUpdateDTO>>() {
        }.getType();
        return modelMapper.map(configList, listType);
    }

    @Override
    public INotificationConfigDTO getDefaultConfig() {
        Optional<EmailConfig> defaultEmailConfig = emailConfigRepo.findDefaultConfig();
        if (!defaultEmailConfig.isPresent()) {
            final String message = new CustomMessages.CustomMessagesBuilder().addMessage(
                        CustomMessages.DEFAULT_EMAIL_TEMPLATE).addMessage(
                                    CustomMessages.EntityOperation.READ.name()).addMessage(
                                                CustomMessages.OperationResultStatus.FAILED.name()).build();
            logger.info(message);
            throw new EntityProcessingFailedException(
                        CustomMessages.DEFAULT_ENTITY_NOT_FOUND);
        }
        return modelMapper.map(defaultEmailConfig.get(),
                    EmailConfigUpdateDTO.class);
    }

    @Override
    @Transactional
    public INotificationConfigDTO changeDefaultConfig(UUID configId) {
        // Fetching the configuration that has to be selected as default
        INotificationConfigDTO notificationConfigDTO = getConfig(configId);
        EmailConfigDTO emailConfigDTO = (EmailConfigDTO) notificationConfigDTO;

        // Fetching the current default configuration
        INotificationConfigDTO defaultConfigDTO = getDefaultConfig();
        EmailConfigUpdateDTO defaultEmailConfigDTO = (EmailConfigUpdateDTO) defaultConfigDTO;

        // Switching the default flag
        emailConfigDTO.setDefault(true);
        defaultEmailConfigDTO.setDefault(false);

        EmailConfig currDefaultEmailConfig = modelMapper.map(emailConfigDTO,
                    EmailConfig.class);
        EmailConfig prevDefaultEmailConfig = modelMapper.map(
                    defaultEmailConfigDTO, EmailConfig.class);
        List<EmailConfig> configList = new ArrayList<>();
        configList.add(currDefaultEmailConfig);
        configList.add(prevDefaultEmailConfig);
        emailConfigRepo.saveAll(configList);
        return emailConfigDTO;
    }

    /**
     * It will mark the current default config as non-default.
     */
    private void unmarkCurrentDefault() {
        EmailConfigDTO prevDefaultConfigDTO = (EmailConfigDTO) getDefaultConfig();
        prevDefaultConfigDTO.setDefault(false);
        EmailConfig prevDefaultConfig = modelMapper.map(prevDefaultConfigDTO,
                    EmailConfig.class);
        emailConfigRepo.save(prevDefaultConfig);
    }
}
