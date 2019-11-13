package com.notification.propel.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.notification.propel.models.db.email.EmailConfig;

@Repository
public interface IEmailConfigRepo extends JpaRepository<EmailConfig, UUID> {

    @Override
    Optional<EmailConfig> findById(UUID id);

    @Query("SELECT config FROM EmailConfig config WHERE config.isDefault = true")
    Optional<EmailConfig> findDefaultConfig();

    @Query(value = "SELECT * FROM email_config config WHERE config.id != :id LIMIT 1", nativeQuery = true)
    Optional<EmailConfig> findOneConfigByIdNotEquals(UUID id);

}
