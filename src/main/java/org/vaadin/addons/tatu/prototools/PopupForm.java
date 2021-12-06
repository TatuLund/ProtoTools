package org.vaadin.addons.tatu.prototools;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.function.ValueProvider;

@Tag(Tag.DIV)
public class PopupForm<T> extends AbstractField<PopupForm<T>, T>
        implements HasSize, HasValidation {

    Dialog dialog = new Dialog();
    Button button = new Button();
    Form<T> form;
    private String text = "";
    private boolean invalid;
    private H3 title;

    public PopupForm(Class<T> beanType, ValueProvider<Void, T> beanProvider) {
        this(null, beanType, true);
    }

    public PopupForm(Class<T> beanType, boolean autoBuild) {
        this(null, beanType, autoBuild);
    }

    public PopupForm(T defaultValue, Class<T> beanType, boolean autoBuild) {
        super(defaultValue);
        form = new Form<>(defaultValue, beanType, autoBuild);
        Span span = new Span();
        span.addClassNames("flex","flex-row","items-center","justify-between","p-s");
        Icon close = VaadinIcon.CLOSE_SMALL.create();
        close.addClickListener(event -> {
           dialog.close(); 
        });
        title = new H3();
        title.addClassNames("text-header","text-l","my-0");
        span.add(close,title);        
        dialog.add(span,form);
        dialog.setResizable(false);
        button.setIcon(VaadinIcon.EDIT.create());
        button.addClickListener(event -> {
            dialog.open();
        });
        button.addThemeVariants(ButtonVariant.LUMO_SMALL);

        form.addValueChangeListener(event -> {
            doSetInternalValue();
        });
        getElement().appendChild(button.getElement());
    }

    private void doSetInternalValue() {
        T newValue = form.getValue();
        setModelValue(newValue, true);
        fireEvent(createValueChange(newValue, true));
        setLabel(text);
    }

    private ComponentValueChangeEvent<PopupForm<T>, T> createValueChange(
            T oldValue, boolean fromClient) {
        return new ComponentValueChangeEvent<PopupForm<T>, T>(this, this,
                oldValue, fromClient);
    }

    public void setLabel(String text) {
        this.text = text;
        button.setText(text);
        button.setIcon(VaadinIcon.EDIT.create());
        title.setText(text);
    }

    public void setProperties(String... propertyNames) {
        form.setProperties(propertyNames);
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
    protected void setPresentationValue(T newPresentationValue) {
        form.setValue(newPresentationValue);
    }

}
