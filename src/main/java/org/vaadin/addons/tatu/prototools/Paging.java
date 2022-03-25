package org.vaadin.addons.tatu.prototools;

import java.util.concurrent.atomic.AtomicInteger;

import org.vaadin.addons.badge.Badge;
import org.vaadin.addons.badge.Badge.BadgeVariant;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.shared.Registration;

public class Paging extends Composite<Div> implements HasSize {

    private Div layout = new Div();
    private Div pagesDiv = new Div();
    private Button previous = new Button(VaadinIcon.CARET_LEFT.create());
    private Button next = new Button(VaadinIcon.CARET_RIGHT.create());
    private int selected = 0;
    private int max;
    private int pages;

    public Paging(int pages) {
        max = pages;
        this.pages = pages;
        for (int i=0;i<pages;i++) {
            Badge badge = new Badge(""+i);
            badge.getElement().getStyle().set("margin-left", "5px");
            badge.getElement().getStyle().set("margin-right", "5px");
            if (i == selected) {
                badge.setVariant(BadgeVariant.SUCCESS);                
            } else {
                badge.setVariant(BadgeVariant.NORMAL);
            }
            badge.setPill(true);
            pagesDiv.add(badge);
        }
        previous.addThemeVariants(ButtonVariant.LUMO_SMALL);
        previous.setEnabled(false);
        previous.addClickListener(event -> {
           if (selected != 0) {
               selected--;
               doSelectInternal(selected);
               next.setEnabled(true);
               if (selected == 0) {
                   previous.setEnabled(false);
               }
               fireEvent(new PageChangedEvent(this, true, selected));
           }
        });
        next.addThemeVariants(ButtonVariant.LUMO_SMALL);
        next.addClickListener(event -> {
            if (selected < (max-1)) {
                selected++;
                doSelectInternal(selected);
                previous.setEnabled(true);               
                if (selected == (max-1)) {
                    next.setEnabled(false);
                }
                fireEvent(new PageChangedEvent(this, true, selected));
            }
         });
    }

    public void setSelected(int index) {
        doSelectInternal(index);
        fireEvent(new PageChangedEvent(this, false, selected));
    }

    private void doSelectInternal(int index) {
        selected = index;
        AtomicInteger i = new AtomicInteger(0);
        pagesDiv.getChildren().forEach(component -> {
            Badge badge = (Badge) component;
            if (selected == i.get()) {
                badge.setVariant(BadgeVariant.SUCCESS);
            } else {
                badge.setVariant(BadgeVariant.NORMAL);
            }
            i.incrementAndGet();
        });
    }

    @Override
    protected Div initContent() {
        layout.getStyle().set("display", "flex");
        layout.getStyle().set("width", "100%");
        pagesDiv.getStyle().set("flex-grow", "1");
        pagesDiv.getStyle().set("text-align", "center");
        layout.add(previous,pagesDiv,next);
        return layout;
    }

    public Registration addPageChangedListener(
            ComponentEventListener<PageChangedEvent> listener) {
        return addListener(PageChangedEvent.class, listener);
    }

    public class PageChangedEvent extends ComponentEvent<Paging> {
        private final int index;

        public PageChangedEvent(Paging source, boolean fromClient,
                int index) {
            super(source, fromClient);
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
}
