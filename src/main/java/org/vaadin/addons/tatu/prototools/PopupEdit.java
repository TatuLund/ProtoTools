package org.vaadin.addons.tatu.prototools;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;

@CssImport(value = "./dialog-mobile.css", themeFor="vaadin-dialog-overlay")
class PopupEdit extends Dialog {

    PopupEdit(String title, Component editor) {
        WebBrowser browser = VaadinSession.getCurrent().getBrowser();
        if (browser.isAndroid() || browser.isIPhone()) {
            addThemeVariants(DialogVariant.LUMO_NO_PADDING);
            addThemeName("mobile");
            setSizeFull();
        }
        Icon close = VaadinIcon.CLOSE_SMALL.create();
        close.addClickListener(event -> {
            close();
        });
        setHeaderTitle(title);
        getHeader().add(close);
        add(editor);
        setResizable(false);

    }
}
