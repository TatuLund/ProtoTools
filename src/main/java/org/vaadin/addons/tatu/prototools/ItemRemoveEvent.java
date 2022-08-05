package org.vaadin.addons.tatu.prototools;

import com.vaadin.flow.component.ComponentEvent;

public class ItemRemoveEvent<T> extends ComponentEvent<AbstractCrud<T>> {
    private final T item;

    public ItemRemoveEvent(AbstractCrud<T> source, boolean fromClient,
            T item) {
        super(source, fromClient);
        this.item = item;
    }

    public T getItem() {
        return item;
    }
}
