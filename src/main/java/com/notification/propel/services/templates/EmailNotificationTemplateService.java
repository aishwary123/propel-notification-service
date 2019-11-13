package com.notification.propel.services.templates;

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
import com.notification.propel.models.db.email.EmailTemplate;
import com.notification.propel.models.dto.commons.INotificationTemplateDTO;
import com.notification.propel.models.dto.email.EmailTemplateDTO;
import com.notification.propel.models.dto.email.EmailTemplateUpdateDTO;
import com.notification.propel.repositories.IEmailTemplateRepo;

/**
 * This class deals with the CRUD operation on email templates.
 * 
 * @author aishwaryt
 */

@Service
public class EmailNotificationTemplateService implements
            INotificationTemplateService<INotificationTemplateDTO, UUID> {

    private IEmailTemplateRepo emailTemplateRepo;
    private ModelMapper modelMapper;

    private Logger logger = LoggerFactory.getLogger(
                EmailNotificationTemplateService.class);

    @Autowired
    public EmailNotificationTemplateService(final IEmailTemplateRepo emailTemplateRepo,
                                            ModelMapper modelMapper) {
        this.emailTemplateRepo = emailTemplateRepo;
        this.modelMapper = modelMapper;
    }

    /**
     * This method will create an email template.
     */
    @Override
    @Transactional
    public INotificationTemplateDTO createTemplate(final INotificationTemplateDTO template) {
        EmailTemplateDTO emailTemplateDTO = (EmailTemplateDTO) template;

        // If we create a template and consider it as default. We mark the
        // previous default as
        // non-default if there is any default template.
        if (emailTemplateDTO.isDefault() && emailTemplateRepo.count() > 0) {
            unmarkCurrentDefault();
        }

        // If this is the first template then we make it as a default.
        if (emailTemplateRepo.count() == 0) {
            emailTemplateDTO.setDefault(true);
        }
        EmailTemplate emailTemplate = modelMapper.map(emailTemplateDTO,
                    EmailTemplate.class);
        emailTemplate = emailTemplateRepo.save(emailTemplate);
        final String message = new CustomMessages.CustomMessagesBuilder().addMessage(
                    CustomMessages.EMAIL_TEMPLATE).addMessage(
                                CustomMessages.EntityOperation.CREATION.name()).addMessage(
                                            CustomMessages.OperationResultStatus.SUCCESS.name()).build();
        logger.info(message);
        return modelMapper.map(emailTemplate, EmailTemplateUpdateDTO.class);
    }

    /**
     * This method will return an email template if the template already exists.
     * 
     * @throws EntityProcessingFailedException If the entity is not found.
     */
    @Override
    public INotificationTemplateDTO getTemplate(final UUID templateId) {
        Optional<EmailTemplate> emailTemplate = emailTemplateRepo.findById(
                    templateId);
        if (!emailTemplate.isPresent()) {
            final String message = new CustomMessages.CustomMessagesBuilder().addMessage(
                        CustomMessages.EMAIL_TEMPLATE).addMessage(
                                    CustomMessages.EntityOperation.READ.name()).addMessage(
                                                CustomMessages.OperationResultStatus.FAILED.name()).build();
            logger.info(message);
            throw new EntityProcessingFailedException(
                        CustomMessages.ENTITY_NOT_FOUND);
        }
        return modelMapper.map(emailTemplate.get(),
                    EmailTemplateUpdateDTO.class);
    }

    /**
     * This method will return all the available templates.
     */
    @Override
    public List<INotificationTemplateDTO> getAllTemplate() {
        List<EmailTemplate> emailTemplate = emailTemplateRepo.findAll();
        Type listType = new TypeToken<List<EmailTemplateUpdateDTO>>() {
        }.getType();
        return modelMapper.map(emailTemplate, listType);
    }

    /**
     * This method will delete the template with id {@code templateId}
     * 
     * @param templateId Id of the template to delete.
     * @throws EntityProcessingFailedException If the entity is not found.
     */
    @Override
    @Transactional
    public void deleteTemplate(UUID templateId) {
        // Fetch the template
        INotificationTemplateDTO notificationTemplateDTO = getTemplate(
                    templateId);
        emailTemplateRepo.deleteById(templateId);

        // If the entity requested to delete is default then we choose some
        // other template as the default one if there is exists any template
        // after template deletion.
        EmailTemplateDTO emailTemplateDTO = (EmailTemplateDTO) notificationTemplateDTO;
        if (emailTemplateDTO.isDefault() && emailTemplateRepo.count() > 0) {
            EmailTemplate emailTemplate = emailTemplateRepo.findAll().get(0);
            emailTemplate.setDefault(true);
            emailTemplateRepo.save(emailTemplate);
        }
        final String message = new CustomMessages.CustomMessagesBuilder().addMessage(
                    CustomMessages.EMAIL_TEMPLATE).addMessage(
                                CustomMessages.EntityOperation.DELETION.name()).addMessage(
                                            CustomMessages.OperationResultStatus.SUCCESS.name()).build();

        logger.info(message);
    }

    /**
     * This method will update an existing template with new template.
     * 
     * @param templateId Id of the template to update.
     * @param emailTemplateDTO New Email template.
     * @throws EntityProcessingFailedException If the entity is not found.
     */
    @Override
    @Transactional
    public INotificationTemplateDTO updateTemplate(final UUID templateId,
                                                   final INotificationTemplateDTO template) {
        // Fetch the template
        EmailTemplateDTO currentSavedTemplate = (EmailTemplateDTO) getTemplate(
                    templateId);
        EmailTemplateDTO emailTemplateInRequest = (EmailTemplateDTO) template;

        // If the existing value is not default and new value is default, we
        // mark the current default value as non-default.
        if (!currentSavedTemplate.isDefault()
                    && emailTemplateInRequest.isDefault()) {
            unmarkCurrentDefault();
        }

        // If the existing value was default but the new value passed is not
        // default, then we check if there exist any entry other than the
        // template
        // we are processing. If there exists any template we make it as
        // default,
        // otherwise raise exception.
        if (currentSavedTemplate.isDefault()
                    && !emailTemplateInRequest.isDefault()) {

            Optional<EmailTemplate> emailTemplate = emailTemplateRepo.findOneTemplateByIdNotEquals(
                        templateId);
            if (emailTemplate.isPresent()) {
                EmailTemplate someOtherTemplate = emailTemplate.get();
                someOtherTemplate.setDefault(true);
                emailTemplateRepo.save(someOtherTemplate);
            } else {
                throw new EntityProcessingFailedException(
                            CustomMessages.LAST_TEMPLATE_CAN_NOT_BE_NON_DEFAULT);
            }
        }
        EmailTemplate emailTemplate = modelMapper.map(template,
                    EmailTemplate.class);
        emailTemplate.setId(templateId);

        // Maintain the original audit info to avoid override
        emailTemplate.setCreatedBy(currentSavedTemplate.getCreatedBy());
        emailTemplate.setCreatedDate(currentSavedTemplate.getCreatedDate());

        emailTemplate = emailTemplateRepo.save(emailTemplate);

        final String message = new CustomMessages.CustomMessagesBuilder().addMessage(
                    CustomMessages.EMAIL_TEMPLATE).addMessage(
                                CustomMessages.EntityOperation.UPDATION.name()).addMessage(
                                            CustomMessages.OperationResultStatus.SUCCESS.name()).build();
        logger.info(message);
        return modelMapper.map(emailTemplate, EmailTemplateUpdateDTO.class);

    }

    @Override
    @Transactional
    public INotificationTemplateDTO changeDefaultTemplate(UUID templateId) {
        // Fetching the template that has to be selected as default
        INotificationTemplateDTO notificationTemplateDTO = getTemplate(
                    templateId);
        EmailTemplateDTO emailTemplateDTO = (EmailTemplateDTO) notificationTemplateDTO;

        // Fetching the current default template
        INotificationTemplateDTO defaultTemplateDTO = getDefaultTemplate();
        EmailTemplateUpdateDTO defaultEmailTemplateDTO = (EmailTemplateUpdateDTO) defaultTemplateDTO;

        // Switching the default flag
        emailTemplateDTO.setDefault(true);
        defaultEmailTemplateDTO.setDefault(false);

        EmailTemplate currDefaultEmailTemplate = modelMapper.map(
                    emailTemplateDTO, EmailTemplate.class);
        EmailTemplate prevDefaultEmailTemplate = modelMapper.map(
                    defaultEmailTemplateDTO, EmailTemplate.class);
        List<EmailTemplate> templateList = new ArrayList<>();
        templateList.add(currDefaultEmailTemplate);
        templateList.add(prevDefaultEmailTemplate);
        emailTemplateRepo.saveAll(templateList);
        return emailTemplateDTO;
    }

    @Override
    public INotificationTemplateDTO getDefaultTemplate() {
        Optional<EmailTemplate> defaultEmailTemplate = emailTemplateRepo.findDefaultTemplate();
        if (!defaultEmailTemplate.isPresent()) {
            final String message = new CustomMessages.CustomMessagesBuilder().addMessage(
                        CustomMessages.DEFAULT_EMAIL_TEMPLATE).addMessage(
                                    CustomMessages.EntityOperation.READ.name()).addMessage(
                                                CustomMessages.OperationResultStatus.FAILED.name()).build();
            logger.info(message);
            throw new EntityProcessingFailedException(
                        CustomMessages.DEFAULT_ENTITY_NOT_FOUND);
        }
        return modelMapper.map(defaultEmailTemplate.get(),
                    EmailTemplateUpdateDTO.class);
    }

    /**
     * It will mark the current default template as non-default.
     */
    private void unmarkCurrentDefault() {
        EmailTemplateDTO prevDefaultTemplateDTO = (EmailTemplateDTO) getDefaultTemplate();
        prevDefaultTemplateDTO.setDefault(false);
        EmailTemplate prevDefaultTemplate = modelMapper.map(
                    prevDefaultTemplateDTO, EmailTemplate.class);
        emailTemplateRepo.save(prevDefaultTemplate);
    }

}
