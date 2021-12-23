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

    PopupEdit(H3 title, Component editor) {
        WebBrowser browser = VaadinSession.getCurrent().getBrowser();
        if (browser.isAndroid() || browser.isIPhone()) {
            addThemeVariants(DialogVariant.LUMO_NO_PADDING);
            addThemeName("mobile");
            setSizeFull();
        }
        Span span = new Span();
        span.addClassNames("flex", "flex-row", "items-center",
                "justify-between", "p-s");
        Icon close = VaadinIcon.CLOSE_SMALL.create();
        close.addClickListener(event -> {
            close();
        });
        title.addClassNames("text-header", "text-l", "my-0");
        span.add(close, title);
        add(span, editor);
        setResizable(false);

    }
}
