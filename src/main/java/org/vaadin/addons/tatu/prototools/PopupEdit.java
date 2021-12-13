package org.vaadin.addons.tatu.prototools;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

class PopupEdit extends Dialog {

    PopupEdit(H3 title, Component editor) {
        Span span = new Span();
        span.addClassNames("flex","flex-row","items-center","justify-between","p-s");
        Icon close = VaadinIcon.CLOSE_SMALL.create();
        close.addClickListener(event -> {
           close(); 
        });
        title.addClassNames("text-header","text-l","my-0");
        span.add(close,title);        
        add(span,editor);
        setResizable(false);
        
    }
}
