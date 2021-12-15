package org.vaadin.addons.tatu.prototools;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.PropertyDefinition;

public class FieldFactory {

    static Component createField(PropertyDefinition<?, ?> property) {
        Class<?> propertyType = property.getType();
        Component component;
        if (property.getType().isAssignableFrom(String.class)) {
            component = new TextField();
            ((TextField) component).setAutoselect(true);
        } else if (propertyType.isAssignableFrom(Integer.class)) {
            component = new IntegerField();
            ((IntegerField) component).setAutoselect(true);
        } else if (propertyType.isAssignableFrom(BigDecimal.class)) {
            component = new BigDecimalField();
            ((BigDecimalField) component).setAutoselect(true);
        } else if (propertyType.isAssignableFrom(Double.class)) {
            component = new NumberField();
            ((NumberField) component).setAutoselect(true);
        } else if (propertyType.isAssignableFrom(LocalDate.class)) {
            component = new DatePicker();
        } else if (propertyType.isAssignableFrom(LocalTime.class)) {
            component = new TimePicker();
        } else if (propertyType.isAssignableFrom(LocalDateTime.class)
                || propertyType.isAssignableFrom(Date.class)) {
            component = new DateTimePicker();
            component.getElement().getThemeList().add("picker-fixes");
        } else if (propertyType.isAssignableFrom(Boolean.class)) {
            component = new Checkbox();
        } else if (propertyType.isEnum()) {
            ComboBox<String> comp = createEnumCombo(property);
            component = comp;
        } else {
            // throw new IllegalStateException("Property type not supported: "
            // + propertyType.getTypeName());
            component = null;
        }
        return component;
    }

    private static ComboBox<String> createEnumCombo(
            PropertyDefinition<?, ?> property) {
        ComboBox<String> comp = new ComboBox<String>();
        Class<Enum<?>> e = (Class<Enum<?>>) property.getType();
        List<String> values = new ArrayList<>();
        for (Enum en : e.getEnumConstants()) {
            values.add(en.toString());
        }
        comp.setItems(values);
        return comp;
    }

}
