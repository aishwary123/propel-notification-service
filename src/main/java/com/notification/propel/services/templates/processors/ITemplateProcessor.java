package com.notification.propel.services.templates.processors;

import java.util.Map;

public interface ITemplateProcessor {

    public String processTemplate(final String templateContent,
                                  final Map<String, Object> templatePlaceholders);
}
