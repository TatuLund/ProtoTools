package org.vaadin.addons.tatu.prototools;

import java.util.Collection;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.grid.dataview.GridDataView;
import com.vaadin.flow.component.grid.dataview.GridLazyDataView;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.BackEndDataProvider;
import com.vaadin.flow.data.provider.CallbackDataProvider.CountCallback;
import com.vaadin.flow.data.provider.CallbackDataProvider.FetchCallback;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.InMemoryDataProvider;
import com.vaadin.flow.function.ValueProvider;

public abstract class AbstractCrud<T>
        extends Composite<Div> implements HasSize {

    AutoGrid<T> grid;
    Form<T> form;
    Div layout = new Div();

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
        grid.setColumns(properties);
    }

    public void setFormProperties(String... properties) {
        form.setProperties(properties);
    }

    public void addListProperty(String property, Class listBeanType,
            ValueProvider listBeanProvider) {
        form.addListProperty(property, listBeanType, listBeanProvider, true,
                null);
    }

    public void addListProperty(String property, Class listBeanType,
            ValueProvider listBeanProvider, String... listProperties) {
        form.addListProperty(property, listBeanType, listBeanProvider, false,
                listProperties);
    }

    public void addBeanProperty(String property, Class beanBeanType) {
        form.addBeanProperty(property, beanBeanType, true, null);
    }

    public void addBeanProperty(String property, Class beanBeanType,
            String... beanProperties) {
        form.addBeanProperty(property, beanBeanType, false, beanProperties);
    }

    public abstract void editItem(T item); 

    @Override
    protected Div initContent() {
        return layout;
    }
}
