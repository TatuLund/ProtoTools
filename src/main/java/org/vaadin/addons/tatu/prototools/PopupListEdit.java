package org.vaadin.addons.tatu.prototools;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.function.ValueProvider;

@Tag("div")
@CssImport("./styles.css")
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
public class PopupListEdit<T> extends AbstractField<PopupListEdit<T>, List<T>>
        implements HasSize, HasValidation {

    Dialog dialog = new Dialog();
    Button button = new Button();
    ListEdit<T> listEdit;
    private String text = "";
    private boolean invalid;
    private H3 title;

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
        Span span = new Span();
        span.addClassNames("flex","flex-row","items-center","justify-between","p-s");
        Icon close = VaadinIcon.CLOSE_SMALL.create();
        close.addClickListener(event -> {
           dialog.close(); 
        });
        title = new H3();
        title.addClassNames("text-header","text-l","my-0");
        span.add(close,title);
        dialog.add(span,listEdit);
        dialog.setResizable(false);
        dialog.setMinWidth("1000px");
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
        title.setText(text);
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
        if (invalid) {
            button.addClassNames("bg-error", "text-error-contrast");
        } else {
            button.removeClassNames("bg-error", "text-error-contrast");            
        }
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
