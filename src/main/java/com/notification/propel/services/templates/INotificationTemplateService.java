package com.notification.propel.services.templates;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.notification.propel.models.dto.commons.INotificationTemplateDTO;

@Service
public interface INotificationTemplateService<T extends INotificationTemplateDTO, I> {

    public T createTemplate(T template);

    public T getTemplate(UUID templateId);

    public List<T> getAllTemplate();

    public void deleteTemplate(UUID templateId);

    public T updateTemplate(UUID templateId,
                            T template);

    public T getDefaultTemplate();

    public T changeDefaultTemplate(I configId);
}
