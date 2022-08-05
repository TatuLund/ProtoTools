package org.vaadin.addons.tatu.prototools;

import org.vaadin.addons.tatu.GridLayout;
import org.vaadin.addons.tatu.GridLayout.Align;
import org.vaadin.addons.tatu.GridLayout.Content;
import org.vaadin.addons.tatu.GridLayout.Gap;
import org.vaadin.addons.tatu.GridLayout.Justify;
import org.vaadin.addons.tatu.GridLayout.Orientation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropEffect;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.dnd.EffectAllowed;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxShadow;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

@NpmPackage(value = "@vaadin/vaadin-lumo-styles", version = "23.2.0-alpha2")
@JsModule("@vaadin/vaadin-lumo-styles/utility.js")
@JsModule("./lumo-utility.ts")
public class Dashboard extends Composite<Div> {

    private GridLayout gridLayout;
    private Div widgetGenerators = new Div();
    private Div widgetUpdaters = new Div();
    private Div tools;
    private Div layout = new Div();

    public Dashboard(Orientation orientation, int rowscols) {
        gridLayout = new GridLayout(orientation, rowscols);
        DropTarget<GridLayout> dropTarget = DropTarget.create(gridLayout);
        dropTarget.setDropEffect(DropEffect.MOVE);
        dropTarget.setActive(true);
        dropTarget.addDropListener(event -> {
            event.getDragSourceComponent().ifPresent(div -> {
                if (div instanceof Div) {
                    gridLayout.remove(div);
                    gridLayout.add(div);
                } else if (div instanceof WidgetGenerator) {
                    WidgetGenerator widgetGenerator = (WidgetGenerator) div;
                    Widget widget = widgetGenerator.produce();
                    gridLayout.add(widget);
                }
            });
        });
        gridLayout.addClassName(Flex.GROW);
        gridLayout.setHeightFull();
        gridLayout.setAlign(Align.STRETCH);
        gridLayout.setContent(Content.STRETCH);
        gridLayout.setJustify(Justify.START);
        gridLayout.setGap(Gap.XSMALL);

        tools = new Div();
        tools.setHeight("100%");
        tools.setWidth("300px");
        tools.addClassNames(Display.FLEX, FlexDirection.COLUMN);
        Span widgets = new Span("Add widgets");
        widgets.addClassNames(FontSize.LARGE, Margin.MEDIUM,
                FontWeight.SEMIBOLD);
        Span update = new Span("Update widgets");
        update.addClassNames(FontSize.LARGE, Margin.MEDIUM,
                FontWeight.SEMIBOLD);
        tools.add(widgets, widgetGenerators, update, widgetUpdaters);

        layout.addClassNames(Display.FLEX, FlexDirection.ROW);
        layout.add(gridLayout, tools);
        layout.setSizeFull();
    }

    public void addWidgetGenerator(Icon icon, String title,
            ValueProvider<Void, Component> componentProvider) {
        WidgetGenerator widgetGenerator = new WidgetGenerator(icon, title,
                componentProvider);
        DragSource<WidgetGenerator> dragSource = DragSource
                .create(widgetGenerator);
        dragSource.addDragStartListener(event -> {
        });
        dragSource.addDragEndListener(event -> {

        });
        widgetGenerators.add(widgetGenerator);
    }

    public void addWidgetUpdater(Icon icon, String title,
            ValueProvider<Void, Component> componentProvider) {
        WidgetUpdater widgetGenerator = new WidgetUpdater(icon, title,
                componentProvider);
        DragSource<WidgetUpdater> dragSource = DragSource
                .create(widgetGenerator);
        dragSource.addDragStartListener(event -> {
        });
        dragSource.addDragEndListener(event -> {

        });
        widgetUpdaters.add(widgetGenerator);
    }

    public Widget addWidget(String title, Component content) {
        Widget widget = new Widget(title, content);
        gridLayout.add(widget);
        return widget;
    }

    @Override
    public Div initContent() {
        return layout;
    }

    private int adjustColSpan(Widget widget, int i) {
        int s = gridLayout.getColSpan(widget);
        if (i == 0)
            return s;
        if ((s + i) > 0 && (s + i) < 13) {
            fireEvent(new WidgetResizedEvent(this, true, widget));
            return s + i;
        } else if (i > 0) {
            return s;
        } else {
            return 1;
        }
    }

    private int adjustRowSpan(Widget widget, int i) {
        int s = gridLayout.getRowSpan(widget);
        if (i == 0)
            return s;
        if ((s + i) > 0 && (s + i) < 7) {
            fireEvent(new WidgetResizedEvent(this, true, widget));
            return s + i;
        } else if (i > 0) {
            return s;
        } else {
            return 1;
        }
    }

    public Registration addWidgetResizedListener(
            ComponentEventListener<WidgetResizedEvent> listener) {
        return addListener(WidgetResizedEvent.class, listener);
    }

    public class WidgetResizedEvent extends ComponentEvent<Dashboard> {

        private Widget widget;

        public WidgetResizedEvent(Dashboard source, boolean fromClient,
                Widget widget) {
            super(source, fromClient);
            this.widget = widget;
        }

        public Widget getWidget() {
            return widget;
        }
    }

    public void setReadOnly(boolean readOnly) {
        tools.setVisible(!readOnly);
        layout.setEnabled(!readOnly);
    }

    public class WidgetGenerator extends AbstractGenerator {

        public WidgetGenerator(Icon icon, String title,
                ValueProvider<Void, Component> componentProvider) {
            super(icon, title, componentProvider);
        }
    }

    public class WidgetUpdater extends AbstractGenerator {

        public WidgetUpdater(Icon icon, String title,
                ValueProvider<Void, Component> componentProvider) {
            super(icon, title, componentProvider);
        }
    }

    class AbstractGenerator extends Composite<Div> {

        private String title;
        private Icon icon;
        private Div gen = new Div();
        private ValueProvider<Void, Component> componentProvider;

        public AbstractGenerator(Icon icon, String title,
                ValueProvider<Void, Component> componentProvider) {
            this.icon = icon;
            this.title = title;
            this.componentProvider = componentProvider;
            gen.addClassNames(Display.FLEX, LumoUtility.Gap.MEDIUM,
                    Margin.SMALL, Padding.MEDIUM, BoxShadow.SMALL);
            gen.add(icon, new Text(title));
        }

        public Widget produce() {
            Component content = componentProvider.apply(null);
            return new Widget(title, content);
        }

        @Override
        public Div initContent() {
            return gen;
        }
    }

    public class Widget extends Composite<Div> {

        Div widget = new Div();
        Component content;
        private Div header;

        public Widget(String title, Component content) {
            this.content = content;
            widget.addClassName(BoxShadow.SMALL);

            DragSource<Div> dragSource = DragSource.create(widget);
            dragSource.setDraggable(true);
            dragSource.setEffectAllowed(EffectAllowed.MOVE);

            DropTarget<Div> dropTarget = DropTarget.create(widget);
            dropTarget.setDropEffect(DropEffect.MOVE);
            dropTarget.setActive(true);
            dropTarget.addDropListener(event -> {
                event.getDragSourceComponent().ifPresent(dragged -> {
                    if (dragged == widget) {
                        return;
                    }
                    if (dragged instanceof WidgetGenerator) {
                        WidgetGenerator widgetGenerator = (WidgetGenerator) dragged;
                        gridLayout.add(widgetGenerator.produce());
                        return;
                    }
                    if (dragged instanceof WidgetUpdater) {
                        WidgetUpdater widgetUpdater = (WidgetUpdater) dragged;
                        this.setWidgetContent(
                                widgetUpdater.produce().getWidgetContent());
                        return;
                    }
                    int indexDiv = gridLayout.indexOf(this);
                    gridLayout.remove(dragged);
                    gridLayout.addComponentAtIndex(indexDiv, dragged);
                });
            });

            header = new Div();
            header.setWidth("97%");
            header.getStyle().set("display", "flex");
            header.getStyle().set("justify-content", "space-between");
            TextField titleField = new TextField();
            titleField.setValue(title);
            titleField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
            header.add(titleField);
            header.addClassNames(BoxShadow.XSMALL, Margin.XSMALL);

            MenuBar menu = new MenuBar();
            menu.setWidth("32px");
            menu.addThemeVariants(MenuBarVariant.LUMO_SMALL);
            menu.addItem(VaadinIcon.ARROW_RIGHT.create(),
                    event -> setColSpan(adjustColSpan(this, 1)));
            menu.addItem(VaadinIcon.ARROW_LEFT.create(),
                    event -> setColSpan(adjustColSpan(this, -1)));
            menu.addItem(VaadinIcon.ARROW_DOWN.create(),
                    event -> setRowSpan(adjustRowSpan(this, 1)));
            menu.addItem(VaadinIcon.ARROW_UP.create(),
                    event -> setRowSpan(adjustRowSpan(this, -1)));

            header.add(menu);
            widget.add(header, content);
        }

        public void setColSpan(int colSpan) {
            gridLayout.setColSpan(this, colSpan);
        }

        public void setRowSpan(int rowSpan) {
            gridLayout.setRowSpan(this, rowSpan);
        }

        public int getColSpan() {
            return gridLayout.getColSpan(this);
        }

        public int getRowSpan() {
            return gridLayout.getRowSpan(this);
        }

        public void setWidgetContent(Component content) {
            widget.removeAll();
            widget.add(header, content);
            this.content = content;
        }

        public Component getWidgetContent() {
            return content;
        }

        @Override
        public Div initContent() {
            return widget;
        }
    }
}
