package com.notification.propel.services.configs;

import java.util.List;

import com.notification.propel.models.dto.commons.INotificationConfigDTO;

public interface INotificationConfigService<T extends INotificationConfigDTO, I> {

    public T createConfig(T config);

    public T getConfig(I configId);

    public List<INotificationConfigDTO> getAllConfig();

    public void deleteConfig(I configId);

    public T updateConfig(I configId,
                          T config);

    public T getDefaultConfig();

    public T changeDefaultConfig(I configId);
}
