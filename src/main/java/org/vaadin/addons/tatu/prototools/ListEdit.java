package org.vaadin.addons.tatu.prototools;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

@Tag("div")
@CssImport("./styles.css")
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
public class ListEdit<T> extends AbstractField<ListEdit<T>, List<T>>
        implements HasSize, HasValidation, HasComponents {

    private AutoGrid<T> grid;
    private GridListDataView<T> dataView;
    private Icon addButton;
    private ValueProvider<Void, T> beanProvider;
    private Class<T> beanType;;
    private boolean hasChanges;
    private Column<T> deleteColumn;
    private boolean readOnly;
    private Registration valueChangeRegistration;
    private boolean invalid;
    private Label error = new Label();

    public ListEdit(Class<T> beanType) {
        this(new ArrayList<T>(), beanType, null, true);
    }

    public ListEdit(Class<T> beanType, boolean autoBuild) {
        this(new ArrayList<T>(), beanType, null, autoBuild);
    }

    public ListEdit(Class<T> beanType, ValueProvider<Void, T> beanProvider) {
        this(new ArrayList<T>(), beanType, beanProvider, true);
    }

    public ListEdit(Class<T> beanType, ValueProvider<Void, T> beanProvider,
            boolean autoBuild) {
        this(new ArrayList<T>(), beanType, beanProvider, autoBuild);
    }

    public ListEdit(List<T> defaultValue, Class<T> beanType,
            ValueProvider<Void, T> beanProvider, boolean autoBuild) {
        super(defaultValue);
        this.beanProvider = beanProvider;
        this.beanType = beanType;
        grid = new AutoGrid<>(beanType, autoBuild);
        grid.addClassNames("p-s", "shadow-xs");
        grid.setResponsive(true);

        dataView = grid.setItems(defaultValue);

        grid.getEditor().addCloseListener(event -> {
            if (hasChanges) {
                doSetInternalValue();
            }
        });

        addButton = VaadinIcon.PLUS.create();
        addButton.addClassName("m-s");
        addButton.addClickListener(event -> {
            if (readOnly)
                return;
            hasChanges = false;
            T newItem = null;
            if (beanProvider != null) {
                newItem = this.beanProvider.apply(null);
            } else {
                try {
                    newItem = beanType.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            dataView.addItem(newItem);
            doSetInternalValue();
            grid.getEditor().editItem(newItem);
        });

        grid.setAllRowsVisible(true);
        if (autoBuild) {
            addDeleteColumn();
        }

        setupValueChangeListener();

        if (defaultValue != null) {
            setModelValue(defaultValue, false);
        }

        error.addClassName("text-error");
        error.setVisible(false);

        add(grid, addButton, error);
    }

    private void addDeleteColumn() {
        deleteColumn = grid.addComponentColumn(item -> {
            Icon deleteButton = VaadinIcon.TRASH.create();
            deleteButton.addClickListener(event -> {
                if (readOnly)
                    return;
                dataView.removeItem(item);
                doSetInternalValue();
            });
            return deleteButton;
        });
    }

    private void doSetInternalValue() {
        List<T> newValue = dataView.getItems().collect(Collectors.toList());
        setModelValue(newValue, true);
        fireEvent(createValueChange(newValue, true));
        hasChanges = false;
    }

    private ComponentValueChangeEvent<ListEdit<T>, List<T>> createValueChange(
            List<T> oldValue, boolean fromClient) {
        return new ComponentValueChangeEvent<ListEdit<T>, List<T>>(this, this,
                oldValue, fromClient);
    }

    public void setBeanProvider(ValueProvider<Void, T> beanProvider) {
        this.beanProvider = beanProvider;
        addButton.setVisible(beanProvider != null);
    }

    public void setColumns(String... propertyNames) {
        if (valueChangeRegistration != null)
            valueChangeRegistration.remove();
        grid.setColumns(propertyNames);
        addDeleteColumn();
        setupValueChangeListener();
    }

    private void setupValueChangeListener() {
        valueChangeRegistration = grid.getBinder()
                .addValueChangeListener(event -> {
                    hasChanges = true;
                });
    }

    public void addListColumn(String property, Class listBeanType,
            ValueProvider listBeanProvider) {
        if (valueChangeRegistration != null)
            valueChangeRegistration.remove();
        grid.removeColumn(deleteColumn);
        grid.addListColumn(property, listBeanType, listBeanProvider, true,
                null);
        addDeleteColumn();
        setupValueChangeListener();
    }

    public void addListColumn(String property, Class listBeanType,
            ValueProvider listBeanProvider, String... listProperties) {
        if (valueChangeRegistration != null)
            valueChangeRegistration.remove();
        grid.removeColumn(deleteColumn);
        grid.addListColumn(property, listBeanType, listBeanProvider, false,
                listProperties);
        addDeleteColumn();
        setupValueChangeListener();
    }

    public void addBeanColumn(String property, Class listBeanType) {
        if (valueChangeRegistration != null)
            valueChangeRegistration.remove();
        grid.removeColumn(deleteColumn);
        grid.addBeanColumn(property, listBeanType, true, null);
        addDeleteColumn();
        setupValueChangeListener();
    }

    public void addBeanColumn(String property, Class listBeanType,
            String... listProperties) {
        if (valueChangeRegistration != null)
            valueChangeRegistration.remove();
        grid.removeColumn(deleteColumn);
        grid.addBeanColumn(property, listBeanType, false, listProperties);
        addDeleteColumn();
        setupValueChangeListener();
    }

    @Override
    protected void setPresentationValue(List<T> newPresentationValue) {
        dataView = grid.setItems(newPresentationValue);
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
            grid.addClassNames("border", "border-error");
        } else {
            grid.removeClassNames("border", "border-error");
            error.setVisible(false);
        }
    }

    @Override
    public boolean isInvalid() {
        return invalid;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        grid.setEnabled(!readOnly);
        super.setReadOnly(readOnly);
    }
}
