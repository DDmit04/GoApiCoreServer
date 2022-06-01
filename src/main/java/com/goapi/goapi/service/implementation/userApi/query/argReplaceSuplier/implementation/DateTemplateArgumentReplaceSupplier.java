package com.goapi.goapi.service.implementation.userApi.query.argReplaceSuplier.implementation;

import com.goapi.goapi.service.implementation.userApi.query.argReplaceSuplier.TemplateArgumentReplaceSupplier;

/**
 * @author Daniil Dmitrochenkov
 **/
public class DateTemplateArgumentReplaceSupplier implements TemplateArgumentReplaceSupplier {
    @Override
    public String getTemplateArgumentReplacement(Object argValue) {
        return String.format("CAST('%s' as date)", argValue.toString());
    }
}
