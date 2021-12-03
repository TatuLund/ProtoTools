package org.vaadin.addons.tatu.prototools;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.function.ValueProvider;

@Tag("div")
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

        dataView = grid.setItems(defaultValue);

        grid.getEditor().addCloseListener(event -> {
            if (hasChanges) {
                doSetInternalValue();
            }
        });

        addButton = VaadinIcon.PLUS.create();
        addButton.addClickListener(event -> {
            if (readOnly)
                return;
            hasChanges = false;
            T newItem = this.beanProvider.apply(null);
            dataView.addItem(newItem);
            doSetInternalValue();
            grid.getEditor().editItem(newItem);
        });
        if (beanProvider == null) {
            addButton.setVisible(false);
        }

        grid.setAllRowsVisible(true);
        if (autoBuild) {
            addDeleteColumn();
        }

        grid.getBinder().addValueChangeListener(event -> {
            hasChanges = true;
        });

        if (defaultValue != null) {
            setModelValue(defaultValue, false);
        }
        add(grid, addButton);
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
        grid.setColumns(propertyNames);
        addDeleteColumn();
        grid.getBinder().addValueChangeListener(event -> {
            hasChanges = true;
        });
    }

    public void addListColumn(String property, Class listBeanType,
            ValueProvider listBeanProvider) {
        grid.removeColumn(deleteColumn);
        grid.addListColumn(property, listBeanType, listBeanProvider, true,
                null);
        addDeleteColumn();
    }

    public void addListColumn(String property, Class listBeanType,
            ValueProvider listBeanProvider, String... listProperties) {
        grid.removeColumn(deleteColumn);
        grid.addListColumn(property, listBeanType, listBeanProvider, false,
                listProperties);
        addDeleteColumn();
    }

    public void addBeanColumn(String property, Class listBeanType) {
        grid.removeColumn(deleteColumn);
        grid.addBeanColumn(property, listBeanType, true, null);
        addDeleteColumn();
    }

    public void addBeanColumn(String property, Class listBeanType,
            String... listProperties) {
        grid.removeColumn(deleteColumn);
        grid.addBeanColumn(property, listBeanType, false, listProperties);
        addDeleteColumn();
    }

    @Override
    protected void setPresentationValue(List<T> newPresentationValue) {
        dataView = grid.setItems(newPresentationValue);
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
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        grid.setEnabled(!readOnly);
        super.setReadOnly(readOnly);
    }
}
