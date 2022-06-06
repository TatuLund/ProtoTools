package org.vaadin.addons.tatu.prototools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;

public class Wizard extends Composite<Div> {

    Paging paging;
    Div layout = new Div();
    Map<Integer, Component> forms = new HashMap<>();
    private boolean built;
    int size = 0;
    int max = 1;
    private Component currentForm;

    public Wizard() {
    }

    public Wizard withListPage(List<?> defaultValue, Class<?> beanType,
            ValueProvider<Void, ?> beanProvider) {
        ListEdit form = new ListEdit(defaultValue, beanType, beanProvider,
                true);
        forms.put(size, form);
        size++;
        return this;
    }

    public Wizard withListPage(List<?> defaultValue, Class<?> beanType,
            ValueProvider<Void, ?> beanProvider, String... properties) {
        ListEdit form = new ListEdit(defaultValue, beanType, beanProvider,
                false);
        form.setColumns(properties);
        forms.put(size, form);
        size++;
        return this;
    }

    public Wizard wihtBeanPage(Object defaultValue, Class<?> beanType) {
        Form<?> form = new Form(defaultValue, beanType, true);
        forms.put(size, form);
        size++;
        return this;
    }

    public Wizard withBeanPage(Object defaultValue, Class<?> beanType,
            String... properties) {
        Form<?> form = new Form(defaultValue, beanType, false);
        form.setProperties(properties);
        forms.put(size, form);
        size++;
        return this;
    }

    public void build() {
        paging = new Paging(forms.size());
        paging.getElement().getStyle().set("margin-top", "10px");
        currentForm = forms.get(0);
        ((HasValidation) currentForm).isInvalid();
        layout.add(currentForm, paging);
        built = true;
        paging.addPageChangedListener(event -> {
            if (event.isFromClient()) {
                int selected = event.getIndex();
                if (((HasValidation) currentForm).isInvalid()) {
                    selected = event.getIndex() - 1;
                    paging.setSelected(selected);
                }
                currentForm = forms.get(selected);
                currentForm.getElement().getStyle().set("flex-grow", "1");
                ((HasValidation) currentForm).isInvalid();
                layout.removeAll();
                layout.add(currentForm, paging);
            }
        });
    }

    @Override
    protected Div initContent() {
        layout.addClassNames(Display.FLEX, FlexDirection.COLUMN, Width.FULL);
        return layout;
    }

}
