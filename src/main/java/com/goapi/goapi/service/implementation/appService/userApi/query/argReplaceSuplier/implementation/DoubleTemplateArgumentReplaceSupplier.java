package com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.implementation;

import com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.TemplateArgumentReplaceSupplier;

/**
 * @author Daniil Dmitrochenkov
 **/
public class DoubleTemplateArgumentReplaceSupplier implements TemplateArgumentReplaceSupplier {
    @Override
    public String getTemplateArgumentReplacement(Object argValue) {
        return String.format("CAST('%s' as float)", argValue.toString());
    }
}
