package com.notification.propel.configs.database;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.tool.schema.Action;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.notification.propel.configs.jpa.PhysicalNamingStrategyImpl;
import com.notification.propel.utils.SystemUtils;

/**
 * DatabaseConfiguration class provides DataSource bean with all the
 * configurations taken from System variables.
 * 
 * @author aishwaryt
 */
@Configuration
public class DatabaseConfiguration {

    @Bean
    @Primary
    public DataSource dataSource() {
        final String databaseUrl = SystemUtils.getSystemVariable(
                    "DATABASE_URL");
        final String databasePort = SystemUtils.getSystemVariable(
                    "DATABASE_PORT");
        final String databaseName = SystemUtils.getSystemVariable(
                    "DATABASE_NAME");
        final String databaseUserName = SystemUtils.getSystemVariable(
                    "DATABASE_USERNAME");
        final String databasePassword = SystemUtils.getSystemVariable(
                    "DATABASE_PASSWORD");

        String databaseEndpoint = databaseUrl.concat(":").concat(
                    databasePort).concat("/").concat(databaseName);
        return DataSourceBuilder.create().username(databaseUserName).password(
                    databasePassword).url(databaseEndpoint).build();
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(JpaProperties jpaProperties,
                                                                       DataSource dataSource,
                                                                       MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
                                                                       CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl) {
        Map<String, Object> properties = new HashMap<>();
        properties.putAll(jpaProperties.getProperties());
        properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER,
                    multiTenantConnectionProviderImpl);
        properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER,
                    currentTenantIdentifierResolverImpl);
        properties.put(Environment.IMPLICIT_NAMING_STRATEGY,
                    org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyHbmImpl.class);
        properties.put(Environment.PHYSICAL_NAMING_STRATEGY,
                    PhysicalNamingStrategyImpl.class);
        properties.put(Environment.HBM2DDL_AUTO, Action.UPDATE);
        properties.put(Environment.HBM2DLL_CREATE_SCHEMAS, true);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.notification.propel.*");
        em.setJpaVendorAdapter(jpaVendorAdapter());
        em.setJpaPropertyMap(properties);
        return em;

    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
