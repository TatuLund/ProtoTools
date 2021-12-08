package org.vaadin.addons.tatu.prototools;

import java.time.ZoneId;
import java.util.Date;

import com.vaadin.componentfactory.EnhancedFormLayout;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.converter.LocalDateTimeToDateConverter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

@Tag("div")
@CssImport(value = "./picker-responsive.css", themeFor="vaadin-date-time-picker")
public class Form<T> extends AbstractField<Form<T>, T>
        implements HasSize, HasValidation, HasComponents {

    private EnhancedFormLayout form;
    private BeanValidationBinder<T> binder;
    private Class<T> beanType;
    private T bean;
    private Registration valueChangeRegistration;

    public Form(T defaultValue, Class<T> beanType) {
        this(defaultValue, beanType, true);
    }

    public Form(T defaultValue, Class<T> beanType, boolean autoBuild) {
        super(defaultValue);

        this.beanType = beanType;
        this.bean = defaultValue;
        form = new EnhancedFormLayout();
        form.setStickyIndicator(true);
        form.addClassNames("p-s","shadow-xs");

        if (autoBuild) {
            binder = new BeanValidationBinder<>(beanType);

            setupValueChangeListener();

            populateForm(beanType);

            binder.setBean(bean);
        }
        add(form);
    }

    private void setupValueChangeListener() {
        if (valueChangeRegistration != null) valueChangeRegistration.remove();
        valueChangeRegistration = binder.addValueChangeListener(event -> {
            if (binder.isValid()) {
                setModelValue(binder.getBean(), true);
                fireEvent(createValueChange(binder.getBean(), true));
            }
        });
    }

    private ComponentValueChangeEvent<Form<T>, T> createValueChange(T oldValue,
            boolean fromClient) {
        return new ComponentValueChangeEvent<Form<T>, T>(this, this, oldValue,
                fromClient);
    }

    public void setProperties(String... properties) {
        form.removeAll();
        binder = new BeanValidationBinder<>(beanType);
        PropertySet<T> propertySet = BeanPropertySet.get(beanType);
        setupValueChangeListener();

        for (String property : properties) {
            propertySet.getProperty(property).ifPresent(prop -> {
                Component component = FieldFactory.createField(prop);
                configureComponent(prop, component);
            });
        }
        binder.setBean(bean);
    }

    public void addListProperty(String property, Class listBeanType,
            ValueProvider listBeanProvider) {
        this.addListProperty(property, listBeanType, listBeanProvider, true,
                null);
    }

    public void addListProperty(String property, Class listBeanType,
            ValueProvider listBeanProvider, String... listProperties) {
        this.addListProperty(property, listBeanType, listBeanProvider, false,
                listProperties);
    }

    protected void addListProperty(String property, Class listBeanType,
            ValueProvider listBeanProvider, boolean autoCreate,
            String... listProperties) {
        if (valueChangeRegistration != null) valueChangeRegistration.remove();
        PropertySet<T> propertySet = BeanPropertySet.get(beanType);
        propertySet.getProperty(property).ifPresent(prop -> {
            PopupListEdit<?> listEdit = new PopupListEdit<>(listBeanType,
                    listBeanProvider, autoCreate);
            if (listProperties != null)
                listEdit.setColumns(listProperties);
            configureComponent(prop, listEdit);
            listEdit.setLabel(Utils.formatName(property));
        });
        setupValueChangeListener();
    }

    public void addBeanProperty(String property, Class listBeanType) {
        this.addBeanProperty(property, listBeanType, true, null);
    }

    public void addBeanProperty(String property, Class listBeanType,
            String... listProperties) {
        this.addBeanProperty(property, listBeanType, false, listProperties);
    }

    protected void addBeanProperty(String property, Class beanBeanType,
            boolean autoCreate, String... beanProperties) {
        if (valueChangeRegistration != null) valueChangeRegistration.remove();
        PropertySet<T> propertySet = BeanPropertySet.get(beanType);
        propertySet.getProperty(property).ifPresent(prop -> {
            PopupForm<?> form = new PopupForm<>(beanBeanType, autoCreate);
            if (beanProperties != null)
                form.setProperties(beanProperties);
            configureComponent(prop, form);
            form.setLabel(Utils.formatName(property));
        });
        setupValueChangeListener();
    }

    private void populateForm(Class<T> beanType) {
        PropertySet<T> propertySet = BeanPropertySet.get(beanType);
        propertySet.getProperties()
                .filter(property -> !property.isSubProperty()).sorted((prop1,
                        prop2) -> prop1.getName().compareTo(prop2.getName()))
                .forEach(property -> {
                    Component component = FieldFactory.createField(property);
                    configureComponent(property, component);
                });
    }

    private void configureComponent(PropertyDefinition<T, ?> property,
            Component component) {

        if (component == null) return;
        if (component instanceof HasValue) {
            HasValue<?, ?> hasValue = (HasValue) component;
            if (property.getType().isEnum()) {
                Class<Enum<?>> e = (Class<Enum<?>>) property.getType();
                ComboBox<String> comp = (ComboBox<String>) component;
                binder.forField(comp)
                        .withConverter(new StringToEnumConverter(e))
                        .bind(property.getName());
            } else if (property.getType().isAssignableFrom(Date.class)) {
                DateTimePicker comp = (DateTimePicker) component;
                comp.getElement().getThemeList().add("picker-responsive");
                binder.forField(comp)
                        .withConverter(new LocalDateTimeToDateConverter(
                                ZoneId.systemDefault()))
                        .bind(property.getName());
            } else {
                binder.forField(hasValue).bind(property.getName());
            }
        }

        form.addFormItem(component, Utils.formatName(property.getName()));
        component.getElement().getThemeList().add("small");
        component.getElement().getStyle().set("width", "100%");
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getErrorMessage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setInvalid(boolean invalid) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isInvalid() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void setPresentationValue(T newPresentationValue) {
        binder.setBean(newPresentationValue);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        binder.setReadOnly(true);
    }
}
