package com.notification.propel.validators.email;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import com.notification.propel.exceptions.ValidationException;
import com.notification.propel.messages.CustomMessages;
import com.notification.propel.models.dto.requests.EmailRequestDTO;
import com.notification.propel.utils.FileUtils;

public class FileLimitValidator {

    private FileLimitValidator() {
        throw new IllegalStateException(CustomMessages.UTILITY_CLASS);
    }

    // Allowed file size is 10MB
    public static final long MAX_ALLOWED_FILE_SIZE = 10 * 1024 * 1024;

    public static void validate(EmailRequestDTO emailRequestDTO,
                                MultipartFile[] files) {

        long totalFileSize = 0;

        // Validating the file size if attachments are available
        if (null != emailRequestDTO.getAttachmentDetails()) {
            List<String> fileURLs = emailRequestDTO.getAttachmentDetails().getAttachmentLinks().values().stream().collect(
                        Collectors.toList());

            // Fetch the file size for each of the file
            totalFileSize += FileUtils.getFileSizeFromURL(
                        fileURLs.toArray(new String[0]));
        }

        // Files received in request body
        for (MultipartFile file : files) {
            totalFileSize += file.getSize();
        }
        if (totalFileSize > MAX_ALLOWED_FILE_SIZE) {
            throw new ValidationException(CustomMessages.FILE_SIZE_EXCEEDED);
        }

    }

}
