package org.vaadin.addons.tatu.prototools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.dataview.ComboBoxDataView;
import com.vaadin.flow.component.combobox.dataview.ComboBoxLazyDataView;
import com.vaadin.flow.component.combobox.dataview.ComboBoxListDataView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.provider.BackEndDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.InMemoryDataProvider;
import com.vaadin.flow.data.provider.CallbackDataProvider.CountCallback;
import com.vaadin.flow.data.provider.CallbackDataProvider.FetchCallback;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@NpmPackage(value = "@vaadin/vaadin-lumo-styles", version = "23.2.0-alpha2")
@JsModule("@vaadin/vaadin-lumo-styles/utility.js")
@JsModule("./lumo-utility.ts")
public class ComboCrud<T> extends AbstractCrud<T> {

    ComboBox<T> selector;
    private Class<T> beanType;

    public ComboCrud(Class<T> beanType) {
        this(beanType, true);
    }

    public ComboCrud(Class<T> beanType, boolean autoBuild) {
        super(beanType, autoBuild);
        selector = new ComboBox<>();
        formPlus.setVisible(false);
        this.beanType = beanType;

        layout.addClassNames(Display.GRID, "grid-cols-1", Gap.SMALL);

        selector.addValueChangeListener(event -> {
            T item = event.getValue();
            if (item != null) {
                formPlus.setVisible(true);
                form.setValue(item);
            } else {
                formPlus.setVisible(false);
            }
        });
        form.addValueChangeListener(event -> {
            selector.getDataProvider().refreshItem(event.getValue());
        });
        layout.add(selector, formPlus);
    }

    @Override
    protected void addItem(T item) {
        selector.getListDataView().addItem(item);
    }

    @Override
    protected void removeItem(T item) {
        selector.getListDataView().removeItem(item);
    }

    @Override
    public void editItem(T item) {
        selector.setValue(item);
        // form.setVisible(true);
        // form.setValue(item);
    }

    public ComboBoxListDataView<T> setItems(T... items) {
        return selector.setItems(items);
    }

    public ComboBoxListDataView<T> setItems(Collection<T> items) {
        return selector.setItems(items);
    }

    public ComboBoxLazyDataView<T> setItems(
            BackEndDataProvider<T, String> dataProvider) {
        return selector.setItems(dataProvider);
    }

    public ComboBoxLazyDataView<T> setItems(
            FetchCallback<T, String> fetchCallback) {
        return selector.setItems(fetchCallback);
    }

    public ComboBoxDataView<T> setItems(DataProvider<T, String> dataProvider) {
        return selector.setItems(dataProvider);
    }

    public ComboBoxDataView<T> setItems(InMemoryDataProvider<T> dataProvider) {
        return selector.setItems(dataProvider);
    }

    public ComboBoxLazyDataView<T> setItems(
            FetchCallback<T, String> fetchCallback,
            CountCallback<T, String> countCallback) {
        return selector.setItems(fetchCallback, countCallback);
    }

    public void setComboProperties(String... properties) {
        List<ValueProvider<T, ?>> getters = new ArrayList<>();
        PropertySet<T> propertySet = BeanPropertySet.get(beanType);
        for (String property : properties) {
            propertySet.getProperty(property).ifPresent(prop -> {
                ValueProvider<T, ?> getter = prop.getGetter();
                getters.add(getter);
            });
        }
        selector.setItemLabelGenerator(item -> {
            StringBuilder label = new StringBuilder();
            AtomicBoolean first = new AtomicBoolean(true);
            getters.forEach(getter -> {
                if (first.get()) {
                    label.append(getter.apply(item).toString());
                } else {
                    label.append(" | " + getter.apply(item));
                }
                first.set(false);
            });
            return label.toString();
        });
    }

}
