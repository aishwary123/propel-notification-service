package com.notification.propel.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.notification.propel.models.db.webhook.WebhookConfig;

@Repository
public interface IWebhookConfigRepo extends JpaRepository<WebhookConfig, UUID> {

    @Override
    Optional<WebhookConfig> findById(UUID id);

    @Query("SELECT config FROM WebhookConfig config WHERE config.isDefault = true")
    Optional<WebhookConfig> findDefaultConfig();

    @Query(value = "SELECT * FROM webhook_config config WHERE config.id != :id LIMIT 1", nativeQuery = true)
    Optional<WebhookConfig> findOneConfigByIdNotEquals(UUID id);
}
