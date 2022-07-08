package org.vaadin.addons.tatu.prototools;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.icon.VaadinIcon;

@NpmPackage(value = "@vaadin/vaadin-lumo-styles", version = "23.2.0-alpha2")
@JsModule("@vaadin/vaadin-lumo-styles/utility.js")
@JsModule("./lumo-utility.ts")
public class PopupCrud<T> extends AbstractGridCrud<T> {

    PopupEdit dialog;
    
    public PopupCrud(Class<T> beanType) {
        this(beanType, true);
    }

    public PopupCrud(Class<T> beanType, boolean autoBuild) {
        super(beanType,autoBuild);
        grid = new AutoGrid<>(beanType,autoBuild);
        grid.setEditorDisabled(true);
        grid.setSelectionMode(SelectionMode.NONE);

        dialog = new PopupEdit(Utils.formatName(beanType.getSimpleName()),formPlus);
        addEditColumn();
        form.addValueChangeListener(event -> {
            grid.getDataProvider().refreshItem(event.getValue());
        });
        layout.add(grid);
    }

    private void addEditColumn() {
        grid.addComponentColumn(item -> {
            Button button = new Button();
            button.setIcon(VaadinIcon.EDIT.create());
            button.addClickListener(event -> {
                editItem(item); 
            });
            return button;
        });
    }

    @Override
    public void editItem(T item) {
        form.setValue(item);
        dialog.open();
    }

    @Override
    public void setGridProperties(String... properties) {
        super.setGridProperties(properties);
        addEditColumn();
    }

}
