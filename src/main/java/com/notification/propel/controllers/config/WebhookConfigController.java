package com.notification.propel.controllers.config;

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
import com.notification.propel.models.dto.commons.INotificationConfigDTO;
import com.notification.propel.models.dto.webhook.WebhookConfigDTO;
import com.notification.propel.services.configs.INotificationConfigService;

@RestController
@RequestMapping(value = WebhookConfigController.REST_ENDPOINT)
public class WebhookConfigController {

    public static final String REST_ENDPOINT = RestAPIPrefix.API_BASE_V1_CONFIG_ENDPOINT
                + "/webhookConfigs";

    @Autowired
    @Qualifier("webhookNotificationConfigService")
    private INotificationConfigService<INotificationConfigDTO, UUID> notificationConfigService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public INotificationConfigDTO createConfig(@Valid @RequestBody WebhookConfigDTO webhookConfig) {
        return notificationConfigService.createConfig(webhookConfig);
    }

    @GetMapping(value = "/{configId}")
    public INotificationConfigDTO getConfig(@PathVariable UUID configId) {
        return notificationConfigService.getConfig(configId);
    }

    @GetMapping()
    public List<INotificationConfigDTO> getAllConfigs() {
        return notificationConfigService.getAllConfig();
    }

    @PutMapping(value = "/{configId}")
    public INotificationConfigDTO updateConfig(@PathVariable UUID configId,
                                               @RequestBody WebhookConfigDTO webhookConfigDTO) {
        return notificationConfigService.updateConfig(configId,
                    webhookConfigDTO);
    }

    @DeleteMapping(value = "/{configId}")
    public void deleteConfig(@PathVariable UUID configId) {
        notificationConfigService.deleteConfig(configId);
    }

    @GetMapping(value = "/default")
    public INotificationConfigDTO defaultConfig() {
        return notificationConfigService.getDefaultConfig();
    }

    @PutMapping(value = "/markDefault/{configId}")
    public INotificationConfigDTO changeDefaultConfig(@PathVariable UUID configId) {
        return notificationConfigService.changeDefaultConfig(configId);
    }
}
