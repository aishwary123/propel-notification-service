package com.notification.propel.messages;

/**
 * All the text which is used within the application is listed here. Log
 * messages are not listed here.
 * 
 * @author aishwaryt
 */
public class CustomMessages {

    public static final String UTILITY_CLASS = "Utility class";
    public static final String FACTORY_CLASS = "Factory class";
    public static final String PROPEL_NOTIFICATION_SERVICER = "Propel Notification Service";
    public static final String SERVER_SIDE_ISSUE = "Server side issue";
    public static final String NAME_MISSING_IN_TEMPLATE = "Name missing in template";
    public static final String DESCRIPTION_MISSING_IN_TEMPLATE = "Description missing in template";
    public static final String BODY_MISSING_IN_TEMPLATE = "Body missing in template";
    public static final String SYSTEM_VARIABLE_NOT_FOUND = "System variable not found";
    public static final String ENTITY_NOT_FOUND = "Entity not found";
    public static final String DEFAULT_ENTITY_NOT_FOUND = "Default Entity not found";
    public static final String EMAIL_TEMPLATE = "Email Template";
    public static final String DEFAULT_EMAIL_TEMPLATE = "Default Email Template";
    public static final String DEFAULT_WEBHOOK_TEMPLATE = "Default Webhook Template";
    public static final String EMAIL_NOTIFICATION_CONFIG = "Email Notification Config";
    public static final String WEBHOOK_NOTIFICATION_CONFIG = "Webhook Notification Config";
    public static final String LAST_TEMPLATE_CAN_NOT_BE_NON_DEFAULT = "Last template can not be non-default";
    public static final String LAST_CONFIG_CAN_NOT_BE_NON_DEFAULT = "Last config can not be non-default";
    public static final String TEMPLATE_ID_REQUIRED_FOR_TEMPLATE_BASED_NOTIFICATION = "Template Id is required when template based notification";
    public static final String ERROR_DURING_TEMPLATE_PROCESSING = "Error during template processing";
    public static final String ATTCHMENTS_LINKS_MISSING = "Attachment links are missing";
    public static final String INCORRECT_ATTCHMENTS_LINK = "Attachment links is incorrect";
    public static final String FILE_SIZE_EXCEEDED = "File size exceeds maximum allowed limit";
    public static final String MAIL_SEND_OPERATION_FAILED = "Mail send operation failed";
    public static final String WEBHOOK_MSG_SEND_OPERATION_FAILED = "Webhook message send operation failed";
    public static final String REQUEST_FAILED_FOR_FILE_SIZE_FETCH = "Request failed for fetching file size";
    public static final String INCORRECT_EMAIL_ID_FORMAT = "Incorrect email id format";
    public static final String EMAIL_SENT_SUCCESSFULLY = "Email sent successfully";
    public static final String WEBHOOK_MSG_SENT_SUCCESSFULLY = "Webhook message sent successfully";
    public static final String JWT_TOKEN_PARSING_FAILED = "JWT token parsing failed";
    public static final String AUDIT_PROCESSING_FAILED = "Audit processing failed";
    public static final String FETCHING_TENAND_ID_FROM_ORG_ID_FAILED = "Fetching tenantId from orgId failed";
    public static final String FETCHING_SHARD_DETAILS_FAILED = "Fetching shard details failed";
    public static final String FETCHING_CUSTOM_DDL_STATEMENTS_FAILED = "Fetching custom DDL statement from create.sql failed";
    public static final String TENANT_ONBOARDED_SUCCESSFULLY = "Tenant onboarded successfully";

    public static final String PLACEHOLDER = " {}";

    public enum EntityOperation {
        CREATION, UPDATION, DELETION, READ
    }

    public enum OperationResultStatus {
        SUCCESS, FAILED
    }

    public static class CustomMessagesBuilder {
        private StringBuilder message;
        private String separator = " ";

        public CustomMessagesBuilder() {
            message = new StringBuilder();
        }

        public CustomMessagesBuilder(final String separator) {
            this();
            this.separator = separator;
        }

        public CustomMessagesBuilder addMessage(final String message) {
            this.message.append(separator).append(message);
            return this;
        }

        public String build() {
            return message.toString();
        }
    }

    private CustomMessages() {
        throw new IllegalStateException(CustomMessages.UTILITY_CLASS);
    }

}
