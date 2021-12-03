package org.vaadin.addons.tatu.prototools;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

class StringToEnumConverter implements Converter<String, Enum> {

    private Class<Enum<?>> en;

    StringToEnumConverter(Class<Enum<?>> en) {
        this.en = en;
    }

    @Override
    public Result<Enum> convertToModel(String value, ValueContext context) {
        Enum match = null;
        for (Enum e : en.getEnumConstants()) {
            if (e.toString().equals(value)) {
                match = e;
            }
        }
        return Result.ok(match);
        // if (match != null) {
        // return Result.ok(match);
        // } else {
        // return Result.error("Cannot convert `"+value+"` to enum
        // "+en.getName());
        // }
    }

    @Override
    public String convertToPresentation(Enum value, ValueContext context) {
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }

}
