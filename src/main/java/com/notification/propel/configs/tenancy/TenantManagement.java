package com.notification.propel.configs.tenancy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.notification.propel.exceptions.NotificationTableCreationException;
import com.notification.propel.messages.CustomMessages;
import com.notification.propel.rest.clients.PropelAPIRestClient;

@Configuration
public class TenantManagement {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SET_SCHEMA_SQL_COMMAND_TEMPLATE = "SET SCHEMA '%s';";

    @Value("classpath:create.sql")
    private Resource resourceFile;

    @Autowired
    private PropelAPIRestClient propelAPIRestClient;

    @Autowired
    private DataSource dataSource;

    @EventListener(ApplicationContextEvent.class)
    public void createNotificationTables() {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            for (String shardId : propelAPIRestClient.getAllShardId()) {
                String setSchemaCommand = String.format(
                            SET_SCHEMA_SQL_COMMAND_TEMPLATE, shardId);
                jdbcTemplate.execute(setSchemaCommand);
                List<String> ddlCommands = new ArrayList<>();
                try (InputStream resource = resourceFile.getInputStream()) {
                    ddlCommands = new BufferedReader(new InputStreamReader(
                                resource,
                                StandardCharsets.UTF_8)).lines().collect(
                                            Collectors.toList());
                }
                for (String ddlCommand : ddlCommands) {
                    jdbcTemplate.execute(ddlCommand);
                }
            }
        } catch (IOException ioException) {
            logger.error(
                        CustomMessages.FETCHING_CUSTOM_DDL_STATEMENTS_FAILED.concat(
                                    CustomMessages.PLACEHOLDER),
                        ioException.getMessage());
            throw new NotificationTableCreationException(
                        CustomMessages.FETCHING_CUSTOM_DDL_STATEMENTS_FAILED);
        }

    }
}
