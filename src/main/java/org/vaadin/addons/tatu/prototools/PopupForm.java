package org.vaadin.addons.tatu.prototools;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

@Tag(Tag.DIV)
@NpmPackage(value = "@vaadin/vaadin-lumo-styles", version = "23.2.0-alpha2")
@JsModule("@vaadin/vaadin-lumo-styles/utility.js")
@JsModule("./lumo-utility.ts")
public class PopupForm<T> extends AbstractField<PopupForm<T>, T>
        implements HasSize, HasValidation {

    PopupEdit dialog;
    Button button = new Button();
    Form<T> form;
    private String text = "";
    private boolean invalid;

    public PopupForm(Class<T> beanType, ValueProvider<Void, T> beanProvider) {
        this(null, beanType, true);
    }

    public PopupForm(Class<T> beanType, boolean autoBuild) {
        this(null, beanType, autoBuild);
    }

    public PopupForm(T defaultValue, Class<T> beanType, boolean autoBuild) {
        super(defaultValue);
        form = new Form<>(defaultValue, beanType, autoBuild);
        dialog = new PopupEdit("",form);
        setLabel(Utils.formatName(beanType.getSimpleName()));
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
        dialog.setHeaderTitle(text);
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
            button.addClassNames(Background.ERROR, TextColor.ERROR_CONTRAST);
        } else {
            button.removeClassNames(Background.ERROR, TextColor.ERROR_CONTRAST);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        form.setReadOnly(readOnly);
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
