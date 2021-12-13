package org.vaadin.addons.tatu.prototools;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;

public class PopupCrud<T> extends AbstractCrud<T> {

    PopupEdit dialog;
    
    public PopupCrud(Class<T> beanType) {
        this(beanType, true);
    }

    public PopupCrud(Class<T> beanType, boolean autoBuild) {
        grid = new AutoGrid<>(beanType,autoBuild);
        grid.setEditorDisabled(true);
        form = new Form<>(null, beanType, autoBuild);
        H3 title = new H3(Utils.formatName(beanType.getSimpleName()));
        dialog = new PopupEdit(title,form);
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
                form.setValue(item);
                dialog.open(); 
            });
            return button;
        });
    }

    @Override
    public void setGridProperties(String... properties) {
        super.setGridProperties(properties);
        addEditColumn();
    }

}
