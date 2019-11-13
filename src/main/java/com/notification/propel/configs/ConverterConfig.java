package com.notification.propel.configs;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

@Configuration
public class ConverterConfig {

    @Bean
    public FormattingConversionServiceFactoryBean formattingConversionServiceFactoryBean() {
        FormattingConversionServiceFactoryBean formattingConversionServiceFactoryBean = new FormattingConversionServiceFactoryBean();
        Set<Object> converters = new HashSet<>();
        converters.add(new StringToUUIDConverter());
        formattingConversionServiceFactoryBean.setConverters(converters);
        return formattingConversionServiceFactoryBean;
    }

    public class StringToUUIDConverter implements Converter<String, UUID> {
        @Override
        public UUID convert(String source) {
            return UUID.fromString(source);
        }
    }
}
