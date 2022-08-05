package org.vaadin.addons.tatu.prototools;

import java.util.Arrays;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Grid.Column;

@NpmPackage(value = "@vaadin/vaadin-lumo-styles", version = "23.2.0-alpha2")
@JsModule("@vaadin/vaadin-lumo-styles/utility.js")
@JsModule("./lumo-utility.ts")
public class GridCrud<T> extends AbstractGridCrud<T> {

    private Registration browserListener;
    private boolean wide;

    public GridCrud(Class<T> beanType) {
        this(beanType, true);
    }

    public GridCrud(Class<T> beanType, boolean autoBuild) {
        super(beanType, autoBuild);
        grid = new AutoGrid<>(beanType, autoBuild);
        grid.setMinHeight("200px");
        formPlus.setVisible(false);

        layout.addClassNames(Display.GRID, Column.COLUMNS_2, Gap.SMALL);
        // layout.setSizeFull();

        grid.getElement().getClassList()
                .addAll(Arrays.asList("col-span-2", "shadow-xs"));

        grid.setEditorDisabled(true);
        grid.addSelectionListener(event -> {
            event.getFirstSelectedItem().ifPresentOrElse(item -> {
                editItem(item);
            }, () -> {
                formPlus.setVisible(false);
                if (wide) {
                    layout.removeClassName(Column.COLUMNS_3);
                    layout.addClassName(Column.COLUMNS_2);
                }
                grid.setSizeUndefined();
            });
        });
        form.addValueChangeListener(event -> {
            grid.getDataProvider().refreshItem(event.getValue());
        });
        // Add cursor up/down navi selection
        grid.getElement()
                .executeJs("this.addEventListener('keydown', function(e) {\r\n"
                        + " let delta = 0;\r\n"
                        + " if (e.key === 'ArrowUp') {\r\n" + " delta = -1;\r\n"
                        + " } else if (e.key === 'ArrowDown') {\r\n"
                        + " delta = 1;\r\n" + " }\r\n"
                        + " if (this.selectedItems[0] && delta) {\r\n"
                        + " const currentIndex = +this._cache.getCacheAndIndexByKey(this.selectedItems[0].key).scaledIndex;\r\n"
                        + " const itemToSelect = this._cache.getItemForIndex(currentIndex + delta)\r\n"
                        + " itemToSelect && this.$connector.doSelection([itemToSelect], true);\r\n"
                        + " }\r\n" + "}.bind(this));");

        layout.add(grid, formPlus);
    }

    @Override
    public void editItem(T item) {
        formPlus.setVisible(true);
        form.setValue(item);
        if (wide) {
            layout.removeClassName(Column.COLUMNS_1);
            layout.addClassName(Column.COLUMNS_3);
        } else {
            layout.addClassName(Column.COLUMNS_1);
            layout.removeClassName(Column.COLUMNS_3);
        }
        grid.setHeight("100%");
    }

    private void adjustLayout(int width) {
        if (width < 900) {
            wide = false;
            if (formPlus.isVisible()) {
                layout.removeClassName(Column.COLUMNS_3);
                layout.removeClassName(Column.COLUMNS_2);
                layout.addClassName(Column.COLUMNS_1);
            } else {
                layout.removeClassName(Column.COLUMNS_3);
                layout.removeClassName(Column.COLUMNS_2);
                layout.addClassName(Column.COLUMNS_1);
            }
        } else {
            wide = true;
            layout.removeClassName(Column.COLUMNS_1);
            if (formPlus.isVisible()) {
                layout.addClassName(Column.COLUMNS_3);
            } else {
                layout.addClassName(Column.COLUMNS_1);
            }
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe width change
        getUI().ifPresent(ui -> browserListener = ui.getPage()
                .addBrowserWindowResizeListener(event -> {
                    adjustLayout(event.getWidth());
                }));
        // Adjust Grid according to initial width of the screen
        getUI().ifPresent(
                ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
                    int browserWidth = receiver.getBodyClientWidth();
                    adjustLayout(browserWidth);
                }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource
        // leak
        if (browserListener != null) {
            browserListener.remove();
        }
        super.onDetach(detachEvent);
    }

}