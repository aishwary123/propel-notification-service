package com.notification.propel.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.notification.propel.models.db.email.EmailTemplate;

@Repository
public interface IEmailTemplateRepo extends JpaRepository<EmailTemplate, UUID> {

    @Override
    Optional<EmailTemplate> findById(UUID id);

    @Query("SELECT template FROM EmailTemplate template WHERE template.isDefault = true")
    Optional<EmailTemplate> findDefaultTemplate();

    @Query(value = "SELECT * FROM email_template template WHERE template.id != :id LIMIT 1", nativeQuery = true)
    Optional<EmailTemplate> findOneTemplateByIdNotEquals(UUID id);
}
