package org.vaadin.addons.tatu.prototools;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;

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

        H3 title = new H3(Utils.formatName(beanType.getSimpleName()));
        dialog = new PopupEdit(title,formPlus);
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
