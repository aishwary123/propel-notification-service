package com.notification.propel.models.dto.requests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class EmailRequestDTO {

    @NotNull
    private String sender;

    @NotNull
    private String subject;

    @NotNull
    private List<String> to;

    private List<String> cc;

    private List<String> bcc;

    private String replyTo;

    private String body;

    private boolean bodyInHTML;

    private Template templateDetails;

    private Attachment attachmentDetails;

    @NotNull
    private UUID configId;

    @Data
    @ToString
    @EqualsAndHashCode
    public class Attachment {

        // It will only attach the files passed directly in request as inline.
        private boolean inlineAttachmentsEnabled;

        // It keeps filename and file URL
        private Map<String, String> attachmentLinks = new HashMap<>();

    }

    @Data
    @ToString
    @EqualsAndHashCode
    public class Template {

        // If it is set and templateId is not present, default will be used.
        private boolean templateUsed;

        private UUID templateId;

        private Map<String, Object> templatePlaceholders = new HashMap<>();
    }
}
