package com.notification.propel.validators.email;

import org.apache.commons.validator.routines.UrlValidator;

import com.notification.propel.exceptions.ValidationException;
import com.notification.propel.messages.CustomMessages;
import com.notification.propel.models.dto.requests.EmailRequestDTO;
import com.notification.propel.validators.IValidator;

/**
 * This validator will check whether links are included or not when it is
 * specified that attachments are included as links.<br>
 * It also validates the URL format for links.
 * 
 * @author aishwaryt
 */
public class AttachmentsValidator extends ValidationLinker<EmailRequestDTO> {

    public AttachmentsValidator(IValidator<EmailRequestDTO> validator) {
        super(validator);
    }

    @Override
    public void validate(EmailRequestDTO emailRequestDTO) {
        nextValidator.validate(emailRequestDTO);

        if (null != emailRequestDTO.getAttachmentDetails()
                    && !emailRequestDTO.getAttachmentDetails().getAttachmentLinks().isEmpty()) {
            // Validating the URL for the links
            UrlValidator urlValidator = new UrlValidator();
            emailRequestDTO.getAttachmentDetails().getAttachmentLinks().values().stream().forEach(
                        link -> {
                            if (!urlValidator.isValid(link)) {
                                throw new ValidationException(
                                            CustomMessages.INCORRECT_ATTCHMENTS_LINK);
                            }
                        });
        }
    }

}
