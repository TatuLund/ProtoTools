package org.vaadin.addons.tatu.prototools;

import java.util.Collection;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.function.ValueProvider;

@CssImport("./styles.css")
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
public class GridCrud<T> extends Composite<Div> {

    AutoGrid<T> grid;
    Form<T> form;
    Div layout = new Div();

    public GridCrud(Class<T> beanType) {
        this(beanType, true);
    }

    public GridCrud(Class<T> beanType, boolean autoBuild) {
        grid = new AutoGrid<>(beanType,autoBuild);
        form = new Form<>(null,beanType,autoBuild);
        form.setVisible(false);

        layout.addClassNames("grid","grid-cols-2","gap-s");
//        layout.setSizeFull();

        grid.getElement().getClassList().add("col-span-2");
        grid.setEditorDisabled(true);
        grid.addSelectionListener(event -> {
            event.getFirstSelectedItem().ifPresentOrElse(item -> {
                form.setVisible(true);
                form.setValue(item);
                layout.removeClassName("grid-cols-2");
                layout.addClassName("grid-cols-3");
            }, () -> {
                form.setVisible(false);
                layout.removeClassName("grid-cols-3");
                layout.addClassName("grid-cols-2");
            });
        });
        form.addValueChangeListener(event -> {
           grid.getDataProvider().refreshItem(event.getValue()); 
        });
        layout.add(grid,form);
    }
    
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
