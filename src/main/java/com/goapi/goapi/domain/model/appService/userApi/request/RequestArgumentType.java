package com.goapi.goapi.domain.model.appService.userApi.request;

import com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.TemplateArgumentReplaceSupplier;
import com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.implementation.DateTemplateArgumentReplaceSupplier;
import com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.implementation.DateTimeTemplateArgumentReplaceSupplier;
import com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.implementation.FloatTemplateArgumentReplaceSupplier;
import com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.implementation.IntegerTemplateArgumentReplaceSupplier;
import com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.implementation.StringTemplateArgumentReplaceSupplier;

public enum RequestArgumentType {
    INTEGER(new IntegerTemplateArgumentReplaceSupplier()),
    STRING(new StringTemplateArgumentReplaceSupplier()),
    DATE(new DateTemplateArgumentReplaceSupplier()),
    FLOAT(new FloatTemplateArgumentReplaceSupplier()),
    DATETIME(new DateTimeTemplateArgumentReplaceSupplier());
    final TemplateArgumentReplaceSupplier supplier;

    RequestArgumentType(TemplateArgumentReplaceSupplier supplier) {
        this.supplier = supplier;
    }

    public TemplateArgumentReplaceSupplier getSupplier() {
        return supplier;
    }
}
