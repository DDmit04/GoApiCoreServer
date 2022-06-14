package com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier;

import com.goapi.goapi.domain.model.appService.userApi.request.RequestArgumentType;
import com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.implementation.DateTemplateArgumentReplaceSupplier;
import com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.implementation.DateTimeTemplateArgumentReplaceSupplier;
import com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.implementation.FloatTemplateArgumentReplaceSupplier;
import com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.implementation.IntegerTemplateArgumentReplaceSupplier;
import com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.implementation.StringTemplateArgumentReplaceSupplier;

/**
 * @author Daniil Dmitrochenkov
 **/
public class TemplateArgumentReplaceSupplierFactory {

    public static TemplateArgumentReplaceSupplier getArgumentReplaceSupplier(RequestArgumentType requestArgumentType) {
        TemplateArgumentReplaceSupplier argumentSetter = new StringTemplateArgumentReplaceSupplier();
        switch (requestArgumentType) {
            case INTEGER -> argumentSetter = new IntegerTemplateArgumentReplaceSupplier();
            case STRING -> argumentSetter = new StringTemplateArgumentReplaceSupplier();
            case FLOAT -> argumentSetter = new FloatTemplateArgumentReplaceSupplier();
            case DATE -> argumentSetter = new DateTemplateArgumentReplaceSupplier();
            case DATETIME -> argumentSetter = new DateTimeTemplateArgumentReplaceSupplier();
        }
        return argumentSetter;
    }

}
