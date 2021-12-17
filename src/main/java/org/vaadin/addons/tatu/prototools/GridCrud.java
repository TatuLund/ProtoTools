package org.vaadin.addons.tatu.prototools;

import java.util.Arrays;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.NpmPackage;

@CssImport("./styles.css")
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
public class GridCrud<T> extends AbstractCrud<T> {

    public GridCrud(Class<T> beanType) {
        this(beanType, true);
    }

    public GridCrud(Class<T> beanType, boolean autoBuild) {
        grid = new AutoGrid<>(beanType, autoBuild);
        form = new Form<>(null, beanType, autoBuild);
        form.setVisible(false);

        layout.addClassNames("grid", "grid-cols-2", "gap-s");
        // layout.setSizeFull();

        grid.getElement().getClassList()
                .addAll(Arrays.asList("col-span-2", "shadow-xs"));

        grid.setEditorDisabled(true);
        grid.addSelectionListener(event -> {
            event.getFirstSelectedItem().ifPresentOrElse(item -> {
                editItem(item);
            }, () -> {
                form.setVisible(false);
                layout.removeClassName("grid-cols-3");
                layout.addClassName("grid-cols-2");
                grid.setSizeUndefined();
            });
        });
        form.addValueChangeListener(event -> {
            grid.getDataProvider().refreshItem(event.getValue());
        });
        layout.add(grid, form);
    }

    @Override
    public void editItem(T item) {
        form.setVisible(true);
        form.setValue(item);
        layout.removeClassName("grid-cols-2");
        layout.addClassName("grid-cols-3");
        grid.setHeight("100%");
    }

    
}
