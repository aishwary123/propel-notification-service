package com.notification.propel.validators.email;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.notification.propel.exceptions.ValidationException;
import com.notification.propel.messages.CustomMessages;
import com.notification.propel.models.dto.requests.EmailRequestDTO;
import com.notification.propel.validators.IValidator;

/**
 * This validator will check whether the email id is correct or not.<br>
 * We can not use annotation based validator as it does not support list.
 * 
 * @author aishwaryt
 */
public class EmailIDFormatValidator extends ValidationLinker<EmailRequestDTO> {

    public EmailIDFormatValidator(IValidator<EmailRequestDTO> validator) {
        super(validator);
    }

    @Override
    public void validate(EmailRequestDTO emailRequestDTO) {
        nextValidator.validate(emailRequestDTO);
        List<String> validationErrors = new ArrayList<>();

        // Validate Sender email Id format
        if (!StringUtils.isEmpty(emailRequestDTO.getSender())
                    && !isValid(emailRequestDTO.getSender())) {

            validationErrors.add(
                        CustomMessages.INCORRECT_EMAIL_ID_FORMAT.concat(
                                    ":").concat("Sender"));

        }

        // Validate To email Id format
        if (!CollectionUtils.isEmpty(emailRequestDTO.getTo())) {
            Optional<String> invalidEmailId = emailRequestDTO.getTo().stream().parallel().filter(
                        emailID -> !EmailIDFormatValidator.isValid(
                                    emailID)).findAny();
            if (invalidEmailId.isPresent()) {
                validationErrors.add(
                            CustomMessages.INCORRECT_EMAIL_ID_FORMAT.concat(
                                        ":").concat("To"));
            }

        }

        // Validate To email Cc format
        if (!CollectionUtils.isEmpty(emailRequestDTO.getCc())) {
            Optional<String> invalidEmailId = emailRequestDTO.getCc().stream().parallel().filter(
                        emailID -> !EmailIDFormatValidator.isValid(
                                    emailID)).findAny();
            if (invalidEmailId.isPresent()) {
                validationErrors.add(
                            CustomMessages.INCORRECT_EMAIL_ID_FORMAT.concat(
                                        ":").concat("Cc"));
            }
        }

        // Validate To email Bcc format
        if (!CollectionUtils.isEmpty(emailRequestDTO.getBcc())) {
            Optional<String> invalidEmailId = emailRequestDTO.getBcc().stream().parallel().filter(
                        emailID -> !EmailIDFormatValidator.isValid(
                                    emailID)).findAny();
            if (invalidEmailId.isPresent()) {
                validationErrors.add(
                            CustomMessages.INCORRECT_EMAIL_ID_FORMAT.concat(
                                        ":").concat("Bcc"));
            }
        }

        // Validate Sender email Reply To format
        if (!StringUtils.isEmpty(emailRequestDTO.getReplyTo())
                    && !isValid(emailRequestDTO.getReplyTo())) {

            validationErrors.add(
                        CustomMessages.INCORRECT_EMAIL_ID_FORMAT.concat(
                                    ":").concat("Reply To"));

        }
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

    }

    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

}
