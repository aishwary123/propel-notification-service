package com.notification.propel.services.templates.processors;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.notification.propel.exceptions.TemplateProcessingException;
import com.notification.propel.messages.CustomMessages;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class FreeMarkerTemplateProcessor implements ITemplateProcessor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String processTemplate(final String templateContent,
                                  final Map<String, Object> templatePlaceholders) {
        try {
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            String emailTemplate = "emailTemplate";
            stringLoader.putTemplate(emailTemplate, templateContent);
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setTemplateLoader(stringLoader);
            Template template = cfg.getTemplate(emailTemplate);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template,
                        templatePlaceholders);

        } catch (TemplateException | IOException exception) {
            logger.error(
                        CustomMessages.ERROR_DURING_TEMPLATE_PROCESSING.concat(
                                    CustomMessages.PLACEHOLDER),
                        exception.getMessage());
            throw new TemplateProcessingException(
                        CustomMessages.ERROR_DURING_TEMPLATE_PROCESSING);
        }
    }
}
