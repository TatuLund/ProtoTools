package org.vaadin.addons.tatu.prototools;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.converter.LocalDateTimeToDateConverter;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

@CssImport(value = "./picker-fixes.css", themeFor="vaadin-combo-box-overlay")
@CssImport(value = "./time-picker-width.css", themeFor="vaadin-date-time-picker-time-picker")
public class AutoGrid<T> extends Grid<T> {

    private BeanValidationBinder<T> binder;
    private String itemErrorLabel;
    private Class<T> beanType;
    private Registration browserListener;
    private boolean responsive;
    private PropertySet<T> propertySet;

    public AutoGrid(Class<T> beanType) {
        this(beanType, true);
    }

    public AutoGrid(Class<T> beanType, boolean autoBuild) {
        super(beanType, autoBuild);

        this.beanType = beanType;
        this.propertySet = BeanPropertySet.get(beanType);

        setDetailsVisibleOnClick(false);

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

    public void setResponsive(boolean responsive) {
        this.responsive = responsive;
    }

    public Binder<T> getBinder() {
        return binder;
    }

    private void configureColumns(Class<T> beanType) {        
        propertySet.getProperties()
                .filter(property -> !property.isSubProperty()).sorted((prop1,
                        prop2) -> prop1.getName().compareTo(prop2.getName()))
                .forEach(property -> {
                    Component component = FieldFactory.createField(property);
                    configureComponent(property, component);
                });
        getColumns().forEach(col -> {
            if (col.getEditorComponent() instanceof DateTimePicker) {
                col.setWidth("220px");
            }
        });
    }

    public void setColumns(String... propertyNames) {
        binder = new BeanValidationBinder<>(beanType);

        getColumns().forEach(this::removeColumn);
        createCompactColumn(propertyNames);

        Stream.of(propertyNames).forEach(this::addColumn);
        configureColumns(beanType);
        getEditor().setBinder(binder);
    }

    private void createCompactColumn(String... propertyNames) {
        addComponentColumn(item -> {
            Div div = new Div();
            div.addClassNames("grid","grid-cols-2","shadow-m","p-s");
            Stream.of(propertyNames).limit(4).forEach(propName -> {
                PropertyDefinition<T, ?> property = propertySet.getProperty(propName).get();
                Object valueObj = property.getGetter().apply(item);
                String valueString = (valueObj != null) ? valueObj.toString() : "";
                Label name = new Label(Utils.formatName(propName));
                name.addClassName("text-secondary");
                Span value = new Span(valueString);
                div.add(name,value);
            });
            
            Icon edit = VaadinIcon.EDIT.create();
            edit.addClassNames("col-span-2","text-xs","text-primary","ml-auto");

            edit.addClickListener(event -> {
                Dialog dialog = new Dialog();
                Form<T> popup = new Form<>(item, beanType, false);
                popup.setProperties(propertyNames);
                dialog.add(popup);
                dialog.open();
                popup.addValueChangeListener(e -> {
                    getDataProvider().refreshItem(item);
                });
            });
            div.add(edit);

            return div;
        }).setHeader(Utils.formatName(beanType.getSimpleName()))
                .setVisible(false);
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
                form.setProperties(beanProperties);
            configureComponent(prop, form);
            form.setLabel(Utils.formatName(prop.getName()));
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

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if (!responsive)
            return;
        // Add browser window listener to observe width change
        getUI().ifPresent(ui -> browserListener = ui.getPage()
                .addBrowserWindowResizeListener(event -> {
                    adjustVisibleGridColumns(event.getWidth());
                }));
        // Adjust Grid according to initial width of the screen
        getUI().ifPresent(
                ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
                    int browserWidth = receiver.getBodyClientWidth();
                    adjustVisibleGridColumns(browserWidth);
                }));
    }

    private void adjustVisibleGridColumns(int width) {
        // Change which columns are visible depending on browser width
        if (width > 1000) {
            getColumns().get(0).setVisible(false);
            for (int c = 1; c < getColumns().size(); c++) {
                getColumns().get(c).setVisible(true);
            }
        } else {
            getColumns().get(0).setVisible(true);
            for (int c = 1; c < getColumns().size(); c++) {
                getColumns().get(c).setVisible(false);
            }
        }

    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource
        // leak
        if (responsive && browserListener != null) {
            browserListener.remove();
        }
        super.onDetach(detachEvent);
    }
}
