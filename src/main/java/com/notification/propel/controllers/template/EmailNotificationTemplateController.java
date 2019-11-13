package com.notification.propel.controllers.template;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notification.propel.interfaces.RestAPIPrefix;
import com.notification.propel.models.dto.commons.INotificationTemplateDTO;
import com.notification.propel.models.dto.email.EmailTemplateDTO;
import com.notification.propel.services.templates.INotificationTemplateService;

@RestController
@RequestMapping(value = EmailNotificationTemplateController.REST_ENDPOINT)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EmailNotificationTemplateController {

    public static final String REST_ENDPOINT = RestAPIPrefix.API_BASE_V1_TEMPLATE_ENDPOINT
                + "/emailTemplates";

    @Autowired
    @Qualifier("emailNotificationTemplateService")
    private INotificationTemplateService<INotificationTemplateDTO, UUID> notificationTemplateService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public INotificationTemplateDTO createTemplate(@Valid @RequestBody EmailTemplateDTO emailTemplate) {
        return notificationTemplateService.createTemplate(emailTemplate);
    }

    @GetMapping(value = "/{templateId}")
    public INotificationTemplateDTO getTemplate(@PathVariable UUID templateId) {
        return notificationTemplateService.getTemplate(templateId);
    }

    @GetMapping()
    public List getAllTemplates() {
        return notificationTemplateService.getAllTemplate();
    }

    @PutMapping(value = "/{templateId}")
    public INotificationTemplateDTO updateTemplate(@PathVariable UUID templateId,
                                                   @RequestBody EmailTemplateDTO emailTemplate) {
        return notificationTemplateService.updateTemplate(templateId,
                    emailTemplate);
    }

    @DeleteMapping(value = "/{templateId}")
    public void deleteTemplate(@PathVariable UUID templateId) {
        notificationTemplateService.deleteTemplate(templateId);
    }

    @GetMapping(value = "/default")
    public INotificationTemplateDTO defaultTemplate() {
        return notificationTemplateService.getDefaultTemplate();
    }

    @PutMapping(value = "/markDefault/{templateId}")
    public INotificationTemplateDTO changeDefaultTemplate(@PathVariable UUID templateId) {
        return notificationTemplateService.changeDefaultTemplate(templateId);
    }

}
