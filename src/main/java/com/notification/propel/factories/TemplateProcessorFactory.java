package com.notification.propel.factories;

import com.notification.propel.messages.CustomMessages;
import com.notification.propel.models.dto.enums.TemplateEngineType;
import com.notification.propel.services.templates.processors.FreeMarkerTemplateProcessor;
import com.notification.propel.services.templates.processors.ITemplateProcessor;
import com.notification.propel.utils.BeanUtils;

/**
 * Factory class defined to get the correct type of template processor.
 * 
 * @author aishwaryt
 */
public class TemplateProcessorFactory {

    private TemplateProcessorFactory() {
        throw new IllegalStateException(CustomMessages.FACTORY_CLASS);
    }

    public static ITemplateProcessor getTemplateProcessor(final TemplateEngineType templateEngineType) {
        if (templateEngineType == TemplateEngineType.FREE_MARKER) {
            return BeanUtils.getBean(FreeMarkerTemplateProcessor.class);
        }
        return null;
    }
}
