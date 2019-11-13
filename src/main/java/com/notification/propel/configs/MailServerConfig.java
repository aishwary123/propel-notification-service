package com.notification.propel.configs;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.notification.propel.models.dto.email.EmailConfigDTO;
import com.notification.propel.models.dto.email.EmailConfigDTO.EmailEncryptionMethod;

public class MailServerConfig {

    public static JavaMailSender getMailSender(final EmailConfigDTO emailConfig) {
        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setHost(emailConfig.getHost());
        javaMailSenderImpl.setUsername(emailConfig.getUsername());
        javaMailSenderImpl.setPassword(emailConfig.getPassword());
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", emailConfig.isAuthRequired());
        mailProperties.put("mail.smtp.port", emailConfig.getPort());

        // SSL configurations
        if (emailConfig.getEmailEncryptionMethod() == EmailEncryptionMethod.SSL) {
            mailProperties.put("mail.smtp.socketFactory.port",
                        emailConfig.getPort());
            mailProperties.put("mail.smtp.socketFactory.class",
                        "javax.net.ssl.SSLSocketFactory");
            mailProperties.put("mail.smtp.ssl.checkserveridentity", true);
        }

        // TLS configurations
        if (emailConfig.getEmailEncryptionMethod() == EmailEncryptionMethod.TLS) {
            mailProperties.put("mail.smtp.starttls.enable", "true");
        }

        // Debug enabled
        mailProperties.put("mail.debug", "true");

        // Timeout properties added
        mailProperties.put("mail.smtp.timeout", emailConfig.getTimeout());
        mailProperties.put("mail.smtp.connectiontimeout",
                    emailConfig.getConnectionTimeout());
        mailProperties.put("mail.smtp.writetimeout",
                    emailConfig.getWriteTimeout());

        // Encoding properties added
        mailProperties.put("file.encoding", StandardCharsets.UTF_8.name());
        mailProperties.put("mail.mime.charset", StandardCharsets.UTF_8.name());

        javaMailSenderImpl.setJavaMailProperties(mailProperties);
        return javaMailSenderImpl;
    }
}
