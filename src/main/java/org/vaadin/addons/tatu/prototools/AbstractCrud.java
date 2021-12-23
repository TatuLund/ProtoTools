package org.vaadin.addons.tatu.prototools;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

public abstract class AbstractCrud<T> extends Composite<Div> implements HasSize {

    Form<T> form;
    Div layout = new Div();
    private ValueProvider<Void, T> beanProvider;
    Div formPlus;
    private Icon addButton;
    private Icon deleteButton;
    private boolean readOnly;
    
    public AbstractCrud(Class<T> beanType, boolean autoBuild) {
        form = new Form<>(null, beanType, autoBuild);
        formPlus = new Div();

        addButton = VaadinIcon.PLUS.create();
        addButton.addClassName("m-s");
        addButton.addClickListener(event -> {
            T newItem = null;
            if (beanProvider != null) {
                newItem = this.beanProvider.apply(null);
            } else {
                try {
                    newItem = beanType.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            try {
                addItem(newItem);
            } catch (IllegalStateException e) {
                fireEvent(new NewItemEvent(this, true, newItem));
            }
            form.setValue(newItem);
        });

        deleteButton = VaadinIcon.TRASH.create();
        deleteButton.addClickListener(event -> {
            T item = form.getValue();
            try {
                removeItem(item);
            } catch (IllegalStateException e) {
                fireEvent(new ItemRemoveEvent(this, true, item));
            }            
        });

        formPlus.add(form, addButton, deleteButton);
    }

    protected abstract void addItem(T newItem);

    protected abstract void removeItem(T item); 

    public abstract void editItem(T item); 

    public void setFormProperties(String... properties) {
        form.setProperties(properties);
    }

    public void addListProperty(String property, Class listBeanType,
            ValueProvider listBeanProvider) {
        form.addListProperty(property, listBeanType, listBeanProvider, true,
                null);
    }

    public void addListProperty(String property, Class listBeanType,
            ValueProvider listBeanProvider, String... listProperties) {
        form.addListProperty(property, listBeanType, listBeanProvider, false,
                listProperties);
    }

    public void addBeanProperty(String property, Class beanBeanType) {
        form.addBeanProperty(property, beanBeanType, true, null);
    }

    public void addBeanProperty(String property, Class beanBeanType,
            String... beanProperties) {
        form.addBeanProperty(property, beanBeanType, false, beanProperties);
    }

    public void setAddButtonVisble(boolean visible) {
        addButton.setVisible(visible);
    }

    public void setBeanProvider(ValueProvider<Void, T> beanProvider) {
        this.beanProvider = beanProvider;
        addButton.setVisible(beanProvider != null);
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        form.setReadOnly(readOnly);
        addButton.setVisible(!readOnly);
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    protected Div initContent() {
        return layout;
    }

    public Registration addItemRemoveHandler(
            ComponentEventListener<ItemRemoveEvent> listener) {
        return addListener(ItemRemoveEvent.class, listener);
    }

    public class ItemRemoveEvent<R> extends ComponentEvent<AbstractCrud<R>> {
        private final R item;

        public ItemRemoveEvent(AbstractCrud<R> source, boolean fromClient,
                R item) {
            super(source, fromClient);
            this.item = item;
        }

        public R getItem() {
            return item;
        }
    }

    public Registration addNewItemHandler(
            ComponentEventListener<NewItemEvent> listener) {
        return addListener(NewItemEvent.class, listener);
    }

    public class NewItemEvent<R> extends ComponentEvent<AbstractCrud<R>> {
        private final R item;

        public NewItemEvent(AbstractCrud<R> source, boolean fromClient,
                R item) {
            super(source, fromClient);
            this.item = item;
        }

        public R getItem() {
            return item;
        }
    }

}