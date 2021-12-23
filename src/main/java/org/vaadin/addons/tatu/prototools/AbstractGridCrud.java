package org.vaadin.addons.tatu.prototools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.flow.component.grid.dataview.GridDataView;
import com.vaadin.flow.component.grid.dataview.GridLazyDataView;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.data.provider.BackEndDataProvider;
import com.vaadin.flow.data.provider.CallbackDataProvider.CountCallback;
import com.vaadin.flow.data.provider.CallbackDataProvider.FetchCallback;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.InMemoryDataProvider;
import com.vaadin.flow.function.ValueProvider;

public abstract class AbstractGridCrud<T>
        extends AbstractCrud<T> {

    public AbstractGridCrud(Class<T> beanType, boolean autoBuild) {
        super(beanType, autoBuild);
    }

    @Override
    protected void addItem(T item) {
        grid.getListDataView().addItem(item);
    }

    @Override
    protected void removeItem(T item) {
        grid.getListDataView().removeItem(item);
    }

    AutoGrid<T> grid;

    public GridListDataView<T> setItems(T... items) {
        return grid.setItems(items);
    }

    public GridListDataView<T> setItems(Collection<T> items) {
        return grid.setItems(items);
    }

    public GridLazyDataView<T> setItems(BackEndDataProvider<T, Void> dataProvider) {
        return grid.setItems(dataProvider);
    }

    public GridLazyDataView<T> setItems(FetchCallback<T, Void> fetchCallback) {
        return grid.setItems(fetchCallback);
    }

    public GridDataView<T> setItems(DataProvider<T, Void> dataProvider) {
        return grid.setItems(dataProvider);
    }

    public GridDataView<T> setItems(InMemoryDataProvider<T> dataProvider) {
        return grid.setItems(dataProvider);
    }

    public GridLazyDataView<T> setItems(FetchCallback<T, Void> fetchCallback, CountCallback<T, Void> countCallback) {
        return grid.setItems(fetchCallback, countCallback);
    }
    
    public void setGridProperties(String... properties) {
        List<ValueProvider<T, ?>> getters = new  ArrayList<>();
        grid.setColumns(properties);
        for (String property : properties) {
            grid.getPropertySet().getProperty(property).ifPresent(prop -> {
                ValueProvider<T, ?> getter = prop.getGetter();
                getters.add(getter);
            });
        }
    }

}
