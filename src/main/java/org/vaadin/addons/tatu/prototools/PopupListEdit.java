package org.vaadin.addons.tatu.prototools;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.function.ValueProvider;

@Tag("div")
public class PopupListEdit<T> extends AbstractField<PopupListEdit<T>, List<T>>
        implements HasSize, HasValidation {

    Dialog dialog = new Dialog();
    Button button = new Button();
    ListEdit<T> listEdit;
    private String text = "";
    private boolean invalid;

    public PopupListEdit(Class<T> beanType,
            ValueProvider<Void, T> beanProvider) {
        this(new ArrayList<T>(), beanType, beanProvider, true);
    }

    public PopupListEdit(Class<T> beanType, ValueProvider<Void, T> beanProvider,
            boolean autoBuild) {
        this(new ArrayList<T>(), beanType, beanProvider, autoBuild);
    }

    public PopupListEdit(List<T> defaultValue, Class<T> beanType,
            ValueProvider<Void, T> beanProvider, boolean autoBuild) {
        super(defaultValue);
        listEdit = new ListEdit<>(defaultValue, beanType, beanProvider,
                autoBuild);
        dialog.add(listEdit);
        dialog.setResizable(false);
        button.setText("(" + defaultValue.size() + ")");
        button.setIcon(VaadinIcon.EDIT.create());
        button.addClickListener(event -> {
            dialog.open();
        });
        button.addThemeVariants(ButtonVariant.LUMO_SMALL);

        listEdit.addValueChangeListener(event -> {
            doSetInternalValue();
        });
        getElement().appendChild(button.getElement());
    }

    private void doSetInternalValue() {
        List<T> newValue = listEdit.getValue();
        setModelValue(newValue, true);
        fireEvent(createValueChange(newValue, true));
        setLabel(text);
    }

    private ComponentValueChangeEvent<PopupListEdit<T>, List<T>> createValueChange(
            List<T> oldValue, boolean fromClient) {
        return new ComponentValueChangeEvent<PopupListEdit<T>, List<T>>(this,
                this, oldValue, fromClient);
    }

    public void setLabel(String text) {
        this.text = text;
        button.setText(text + " (" + getValue().size() + ")");
        button.setIcon(VaadinIcon.EDIT.create());
    }

    public void setColumns(String... propertyNames) {
        listEdit.setColumns(propertyNames);
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        button.getElement().setAttribute("title", errorMessage);
    }

    @Override
    public String getErrorMessage() {
        return button.getElement().getAttribute("title");
    }

    @Override
    public void setInvalid(boolean invalid) {
        this.invalid = true;
    }

    @Override
    public boolean isInvalid() {
        return invalid;
    }

    @Override
    protected void setPresentationValue(List<T> newPresentationValue) {
        listEdit.setValue(newPresentationValue);
    }

}
