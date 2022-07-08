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
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

@Tag("div")
@NpmPackage(value = "@vaadin/vaadin-lumo-styles", version = "23.2.0-alpha2")
@JsModule("@vaadin/vaadin-lumo-styles/utility.js")
@JsModule("./lumo-utility.ts")
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
        WebBrowser browser = VaadinSession.getCurrent().getBrowser();
        if (!(browser.isAndroid() || browser.isIPhone())) {
            listEdit.setWidth("calc(100vw - 90px)");
        }
        dialog = new PopupEdit("", listEdit);
        setLabel(Utils.formatName(beanType.getSimpleName()));
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
        dialog.setHeaderTitle(text);
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
    public void setReadOnly(boolean readOnly) {
        listEdit.setReadOnly(readOnly);
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
    public boolean isInvalid() {
        return invalid;
    }

    @Override
    protected void setPresentationValue(List<T> newPresentationValue) {
        listEdit.setValue(newPresentationValue);
    }

}
