package com.notification.propel.validators.email;

import java.util.ArrayList;
import java.util.List;

import com.notification.propel.exceptions.ValidationException;
import com.notification.propel.messages.CustomMessages;
import com.notification.propel.models.dto.requests.EmailRequestDTO;
import com.notification.propel.validators.IValidator;

public class EmailTemplateValidator implements IValidator<EmailRequestDTO> {

    @Override
    public void validate(EmailRequestDTO emailRequestDTO) {
        List<String> validationErrors = new ArrayList<>();
        if (null != emailRequestDTO.getTemplateDetails()
                    && null == emailRequestDTO.getTemplateDetails().getTemplateId()) {
            validationErrors.add(
                        CustomMessages.TEMPLATE_ID_REQUIRED_FOR_TEMPLATE_BASED_NOTIFICATION);
        }
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

    }

}
