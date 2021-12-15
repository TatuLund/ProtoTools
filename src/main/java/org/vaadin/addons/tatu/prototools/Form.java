package org.vaadin.addons.tatu.prototools;

import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Handler;

import com.vaadin.componentfactory.EnhancedFormLayout;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.converter.LocalDateTimeToDateConverter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

@Tag("div")
@CssImport("./styles.css")
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
@CssImport(value = "./picker-responsive.css", themeFor = "vaadin-date-time-picker")
public class Form<T> extends AbstractField<Form<T>, T>
        implements HasSize, HasValidation, HasComponents {

    private EnhancedFormLayout form;
    private BeanValidationBinder<T> binder;
    private Class<T> beanType;
    private T bean;
    private Registration valueChangeRegistration;
    private Label error = new Label();
    private boolean invalid;

    public Form(T defaultValue, Class<T> beanType) {
        this(defaultValue, beanType, true);
    }

    public Form(T defaultValue, Class<T> beanType, boolean autoBuild) {
        super(defaultValue);

        this.beanType = beanType;
        this.bean = defaultValue;
        form = new EnhancedFormLayout();
        form.setStickyIndicator(true);
        form.addClassNames("p-s", "shadow-xs");

        if (autoBuild) {
            binder = new BeanValidationBinder<>(beanType);

            setupValueChangeListener();

            populateForm(beanType);

            binder.setBean(bean);
        }

        error.addClassName("text-error");
        error.setVisible(false);

        add(form, error);
    }

    private void setupValueChangeListener() {
        if (valueChangeRegistration != null)
            valueChangeRegistration.remove();
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
        if (valueChangeRegistration != null)
            valueChangeRegistration.remove();
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

    public void addBeanProperty(String property, Class beanBeanType) {
        this.addBeanProperty(property, beanBeanType, true, null);
    }

    public void addBeanProperty(String property, Class beanBeanType,
            String... beanProperties) {
        this.addBeanProperty(property, beanBeanType, false, beanProperties);
    }

    protected void addBeanProperty(String property, Class beanBeanType,
            boolean autoCreate, String... beanProperties) {
        if (valueChangeRegistration != null)
            valueChangeRegistration.remove();
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

        if (component == null)
            return;
        if (component instanceof HasValue) {
            HasValue<?, ?> hasValue = (HasValue) component;
            if (property.getType().isEnum()) {
                Class<Enum<?>> e = (Class<Enum<?>>) property.getType();
                ComboBox<String> comp = (ComboBox<String>) component;
                binder.forField(comp)
                        .withConverter(new StringToEnumConverter(e))
                        .withValidationStatusHandler(
                                event -> handleValidationStatus(hasValue, event))
                        .bind(property.getName());
            } else if (property.getType().isAssignableFrom(Date.class)) {
                DateTimePicker comp = (DateTimePicker) component;
                binder.forField(comp)
                        .withConverter(new LocalDateTimeToDateConverter(
                                ZoneId.systemDefault()))
                        .withValidationStatusHandler(
                                event -> handleValidationStatus(hasValue,
                                        event))
                        .bind(property.getName());
            } else {
                if (component instanceof DatePicker
                        || component instanceof TimePicker) {
                    component.getElement()
                            .executeJs("this.$.input.autoselect=true;");
                } else if (component instanceof DateTimePicker) {
                    component.getElement().getThemeList()
                            .add("picker-responsive");
                    component.getElement().executeJs(
                            "this.$.dateSlot.getElementsByTagName('vaadin-date-time-picker-date-picker')[0].$.input.autoselect=true;");
                }
                binder.forField(hasValue).withValidationStatusHandler(
                        event -> handleValidationStatus(hasValue, event))
                        .bind(property.getName());
            }
        }

        form.addFormItem(component, Utils.formatName(property.getName()));
        // component.getElement().getThemeList().add("small");
        component.getElement().getStyle().set("width", "100%");
    }

    // Binder is not normally having "sticky" fields. With this custom
    // handler we force focus back to invalid field
    private void handleValidationStatus(HasValue<?, ?> hasValue,
            BindingValidationStatus<?> event) {
        event.getResult().ifPresent(result -> {
            if (result.isError()) {
                if (hasValue instanceof HasValidation) {
                    HasValidation hasValidation = (HasValidation) hasValue;
                    hasValidation.setInvalid(true);
                    event.getMessage().ifPresent(
                            message -> hasValidation.setErrorMessage(message));
                }
                if (hasValue instanceof Focusable) {
                    Focusable focusable = (Focusable) hasValue;
                    focusable.focus();
                }
            } else {
                if (hasValue instanceof HasValidation) {
                    HasValidation hasValidation = (HasValidation) hasValue;
                    hasValidation.setInvalid(false);
                    hasValidation.setErrorMessage(null);
                }                
            }
        });
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        if (errorMessage != null && !errorMessage.isEmpty()) {
            error.setVisible(true);
            error.setText(errorMessage);
        } else {
            error.setVisible(false);
        }
    }

    @Override
    public String getErrorMessage() {
        return error.getText();
    }

    @Override
    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
        if (invalid) {
            form.addClassNames("border", "border-error");
        } else {
            form.removeClassNames("border", "border-error");
        }
    }

    @Override
    public boolean isInvalid() {
        return invalid;
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
