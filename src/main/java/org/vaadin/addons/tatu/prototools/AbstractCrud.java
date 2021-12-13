package org.vaadin.addons.tatu.prototools;

import java.util.Arrays;
import java.util.Collection;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.function.ValueProvider;

public abstract class AbstractCrud<T> extends Composite<Div> {

    AutoGrid<T> grid;
    Form<T> form;
    Div layout = new Div();

    public void setItems(T... items) {
        grid.setItems(items);
    }

    public void setItems(Collection<T> items) {
        grid.setItems(items);
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

    @Override
    protected Div initContent() {
        return layout;
    }
}
