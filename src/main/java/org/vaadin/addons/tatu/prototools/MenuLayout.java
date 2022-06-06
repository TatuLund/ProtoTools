package org.vaadin.addons.tatu.prototools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteData;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.RouteRegistry;
import com.vaadin.flow.server.SessionRouteRegistry;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.Border;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderColor;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Height;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxHeight;
import com.vaadin.flow.theme.lumo.LumoUtility.MinHeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.Position;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import com.vaadin.flow.theme.lumo.LumoUtility.Whitespace;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;

@CssImport("./styles.css")
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
public class MenuLayout extends AppLayout {

    private H1 viewTitle;
    private H2 appName;
    private H3 views;

    public MenuLayout() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    public void setAppTitle(String appTitle) {
        appName.setText(appTitle);
    }

    public void setMenuTitle(String menuTitle) {
        views.setText(menuTitle);
    }

    public void setVariant(String variant) {
        assert Arrays.asList(Lumo.DARK, Lumo.LIGHT).contains(variant);
        getElement().getThemeList()
                .removeAll(Arrays.asList(Lumo.DARK, Lumo.LIGHT));
        getElement().getThemeList().add(variant);
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassName(TextColor.SECONDARY);
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(Margin.NONE, FontSize.LARGE);

        Header header = new Header(toggle, viewTitle);
        header.addClassNames(Background.BASE, Border.BOTTOM,
                BorderColor.CONTRAST_10, BoxSizing.BORDER, Display.FLEX,
                Height.XLARGE, AlignItems.CENTER, Width.FULL);
        return header;
    }

    private Component createDrawerContent() {
        appName = new H2("Proto");
        appName.addClassNames(Display.FLEX, AlignItems.CENTER, Height.XLARGE,
                Margin.NONE, "px-m", FontSize.MEDIUM);

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(
                appName, createNavigation(), createFooter());
        section.addClassNames(Display.FLEX, FlexDirection.COLUMN,
                AlignItems.STRETCH, MaxHeight.FULL, MinHeight.FULL);
        return section;
    }

    private Nav createNavigation() {
        Nav nav = new Nav();
        nav.addClassNames(Border.BOTTOM, BorderColor.CONTRAST_10, Flex.GROW,
                Overflow.AUTO);
        nav.getElement().setAttribute("aria-labelledby", "views");

        views = new H3("Views");
        views.addClassNames(Display.FLEX, Height.MEDIUM, AlignItems.CENTER,
                Margin.Horizontal.MEDIUM, Margin.Vertical.NONE, FontSize.SMALL,
                TextColor.TERTIARY);
        views.setId("views");

        nav.add(views);

        for (Component link : createLinks()) {
            nav.add(link);
        }
        return nav;
    }

    private List<Component> createLinks() {
        RouteRegistry reg = SessionRouteRegistry
                .getSessionRegistry(VaadinSession.getCurrent());
        List<RouteData> routes = reg.getRegisteredRoutes();
        List<Component> links = new ArrayList<>();
        routes.forEach(route -> {
            Class<? extends Component> navigationTarget = route
                    .getNavigationTarget();
            PageTitle title = navigationTarget.getAnnotation(PageTitle.class);
            String titleString = "";
            if (title != null) {
                titleString = title.value();
            } else {
                String simpleName = navigationTarget.getSimpleName();
                titleString = Utils.formatName(simpleName);
            }
            try {
                RouterLink link = createLink(titleString, navigationTarget);
                links.add(link);
            } catch (IllegalArgumentException e) {
                Span link = createTextField(route, titleString);
                links.add(link);
            }
        });
        return links;
    }

    private Span createTextField(RouteData route, String titleString) {
        Span link = new Span(titleString);
        String path = route.getTemplate().split("/")[0];
        TextField field = new TextField();
        field.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        Icon icon = VaadinIcon.ARROW_FORWARD.create();
        icon.addClickListener(event -> {
            if (field.getValue() != null && !field.getValue().isEmpty()
                    && !field.getValue().isBlank()) {
                getUI().ifPresent(ui -> {
                    String full = path + "/" + field.getValue();
                    RouteRegistry reg = SessionRouteRegistry
                            .getSessionRegistry(VaadinSession.getCurrent());
                    Optional<Class<? extends Component>> target = reg
                            .getNavigationTarget(full);
                    if (target.isPresent()) {
                        ui.navigate(full);
                        field.setInvalid(false);
                    } else {
                        field.setInvalid(true);
                    }
                });
            }
        });
        link.addClassNames(Display.FLEX, Margin.Horizontal.SMALL,
                Padding.SMALL, FontWeight.MEDIUM, AlignItems.BASELINE,
                FontSize.SMALL, Gap.SMALL, Position.RELATIVE,
                TextColor.SECONDARY, Whitespace.NOWRAP);
        field.setSuffixComponent(icon);
        link.add(field);
        return link;
    }

    private static RouterLink createLink(String titleString,
            Class<? extends Component> view) {
        RouterLink link = new RouterLink();
        link.addClassNames(Display.FLEX, Margin.Horizontal.SMALL, Padding.SMALL,
                Position.RELATIVE, TextColor.SECONDARY);
        link.setRoute(view);

        Span text = new Span(titleString);
        text.addClassNames(FontWeight.MEDIUM, FontSize.SMALL);

        link.add(text);
        return link;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames(Display.FLEX, AlignItems.CENTER,
                Margin.Vertical.SMALL, Padding.Horizontal.MEDIUM,
                Padding.Vertical.XSMALL);

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass()
                .getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

}
