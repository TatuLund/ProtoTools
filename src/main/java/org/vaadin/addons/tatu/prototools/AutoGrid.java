package org.vaadin.addons.tatu.prototools;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.converter.LocalDateTimeToDateConverter;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;

public class AutoGrid<T> extends Grid<T> {

    private BeanValidationBinder<T> binder;
    private String itemErrorLabel;
    private Class<T> beanType;

    public AutoGrid(Class<T> beanType) {
        this(beanType, true);
    }

    public AutoGrid(Class<T> beanType, boolean autoBuild) {
        super(beanType, autoBuild);

        this.beanType = beanType;

        addItemClickListener(event -> {
            T item = event.getItem();
            // If editor is open already, lets close it
            if (getEditor().isOpen() && getEditor().getItem().equals(item)) {
            } else {
                getEditor().editItem(item);
                // Tip: Find the field from the colum and focus it for better UX
                Component component = event.getColumn().getEditorComponent();
                if (component instanceof Focusable) {
                    Focusable<?> field = (Focusable<?>) component;
                    field.focus();
                }
            }
        });

        getStyle().set("--lumo-space-xs", "1px");
        getStyle().set("--lumo-space-m", "1px");

        binder = new BeanValidationBinder<>(beanType);
        getEditor().setBinder(binder);

        if (autoBuild) {
            configureColumns(beanType);
        }

        setItemDetailsRenderer(new ComponentRenderer(item -> {
            Label label = new Label(itemErrorLabel);
            label.addClassName("text-error");
            return label;
        }));

    }

    public Binder<T> getBinder() {
        return binder;
    }

    private void configureColumns(Class<T> beanType) {
        PropertySet<T> propertySet = BeanPropertySet.get(beanType);
        propertySet.getProperties()
                .filter(property -> !property.isSubProperty()).sorted((prop1,
                        prop2) -> prop1.getName().compareTo(prop2.getName()))
                .forEach(property -> {
                    Component component = FieldFactory.createField(property);
                    configureComponent(property, component);
                });
    }

    public void setColumns(String... propertyNames) {
        binder = new BeanValidationBinder<>(beanType);
        super.setColumns(propertyNames);
        configureColumns(beanType);
        getEditor().setBinder(binder);
    }

    public void addListColumn(String property, Class listBeanType,
            ValueProvider listBeanProvider) {
        this.addListColumn(property, listBeanType, listBeanProvider, true,
                null);
    }

    public void addListColumn(String property, Class listBeanType,
            ValueProvider listBeanProvider, String... listProperties) {
        this.addListColumn(property, listBeanType, listBeanProvider, false,
                listProperties);
    }

    protected void addListColumn(String property, Class listBeanType,
            ValueProvider listBeanProvider, boolean autoCreate,
            String... listProperties) {
        PropertySet<T> propertySet = BeanPropertySet.get(beanType);
        propertySet.getProperty(property).ifPresent(prop -> {
            String name = Utils.formatName(property);
            PopupListEdit<?> listEdit = new PopupListEdit<>(listBeanType,
                    listBeanProvider, autoCreate);
            addColumn(item -> {
                List<?> list = (List<?>) prop.getGetter().apply(item);
                String text = name + " (" + list.size() + ")";
                return text;
            }).setHeader(name).setEditorComponent(listEdit);
            if (listProperties != null)
                listEdit.setColumns(listProperties);
            binder.forField(listEdit).withValidationStatusHandler(
                    handler -> validationStatusHandler(handler, listEdit))
                    .bind(property);
            listEdit.setLabel(name);
        });
    }

    public void addBeanColumn(String property, Class listBeanType) {
        this.addBeanColumn(property, listBeanType, true, null);
    }

    public void addBeanColumn(String property, Class listBeanType,
            String... listProperties) {
        this.addBeanColumn(property, listBeanType, false, listProperties);
    }

    protected void addBeanColumn(String property, Class beanBeanType,
            boolean autoCreate, String... beanProperties) {
        PropertySet<T> propertySet = BeanPropertySet.get(beanType);
        propertySet.getProperty(property).ifPresent(prop -> {
            addColumn(property);
            PopupForm<?> form = new PopupForm<>(beanBeanType, autoCreate);
            if (beanProperties != null)
                form.setColumns(beanProperties);
            configureComponent(prop, form);
            form.setLabel(prop.getName());
        });
    }

    private void configureComponent(PropertyDefinition<T, ?> property,
            Component component) {
        Column<T> column = getColumnByKey(property.getName());
        if (column == null || component == null) {
            return;
        }
        if (component instanceof HasValue) {
            HasValue<?, ?> hasValue = (HasValue) component;
            if (property.getType().isEnum()) {
                Class<Enum<?>> e = (Class<Enum<?>>) property.getType();
                ComboBox<String> comp = (ComboBox<String>) component;
                binder.forField(comp)
                        .withConverter(new StringToEnumConverter(e))
                        .withValidationStatusHandler(
                                handler -> validationStatusHandler(handler,
                                        (HasValidation) component))
                        .bind(property.getName());
            } else if (property.getType().isAssignableFrom(Date.class)) {
                DateTimePicker comp = (DateTimePicker) component;
                binder.forField(comp)
                        .withConverter(new LocalDateTimeToDateConverter(
                                ZoneId.systemDefault()))
                        .withValidationStatusHandler(
                                handler -> validationStatusHandler(handler,
                                        (HasValidation) component))
                        .bind(property.getName());
            } else {
                binder.forField(hasValue)
                        .withValidationStatusHandler(
                                handler -> validationStatusHandler(handler,
                                        (HasValidation) component))
                        .bind(property.getName());
            }
        }
        if (component instanceof KeyNotifier) {
            KeyNotifier keyNotifier = (KeyNotifier) component;
            keyNotifier.addKeyDownListener(Key.ENTER, event -> {
                getEditor().closeEditor();
            });
            keyNotifier.addKeyDownListener(Key.ESCAPE, event -> {
                getEditor().closeEditor();
            });
        }
        column.setEditorComponent(component);
        if (component instanceof DateTimePicker) {
            DateTimePicker picker = (DateTimePicker) component;
            picker.addThemeName("small");
        } else {
            component.getElement().getThemeList().add("small");
        }
        component.getElement().getStyle().set("width", "100%");

    }

    private void validationStatusHandler(BindingValidationStatus<?> handler,
            HasValidation hasValidation) {
        handler.getMessage().ifPresent(message -> itemErrorLabel = message);
        setDetailsVisible(binder.getBean(), handler.isError());
        hasValidation.setInvalid(handler.isError());
    }

}
