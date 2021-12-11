package org.vaadin.addons.tatu.prototools;

import java.util.ArrayList;
import java.util.List;

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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteData;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.RouteRegistry;
import com.vaadin.flow.server.SessionRouteRegistry;
import com.vaadin.flow.server.VaadinSession;

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

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassName("text-secondary");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("m-0", "text-l");

        Header header = new Header(toggle, viewTitle);
        header.addClassNames("bg-base", "border-b", "border-contrast-10",
                "box-border", "flex", "h-xl", "items-center", "w-full");
        return header;
    }

    private Component createDrawerContent() {
        appName = new H2("Proto");
        appName.addClassNames("flex", "items-center", "h-xl", "m-0", "px-m",
                "text-m");

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(
                appName, createNavigation(), createFooter());
        section.addClassNames("flex", "flex-col", "items-stretch", "max-h-full",
                "min-h-full");
        return section;
    }

    private Nav createNavigation() {
        Nav nav = new Nav();
        nav.addClassNames("border-b", "border-contrast-10", "flex-grow",
                "overflow-auto");
        nav.getElement().setAttribute("aria-labelledby", "views");

        views = new H3("Views");
        views.addClassNames("flex", "h-m", "items-center", "mx-m", "my-0",
                "text-s", "text-tertiary");
        views.setId("views");

        nav.add(views);

        for (RouterLink link : createLinks()) {
            nav.add(link);
        }
        return nav;
    }

    private List<RouterLink> createLinks() {
        RouteRegistry reg = SessionRouteRegistry
                .getSessionRegistry(VaadinSession.getCurrent());
        List<RouteData> routes = reg.getRegisteredRoutes();
        List<RouterLink> links = new ArrayList<>();
        routes.forEach(route -> {
            Class<? extends Component> navigationTarget = route
                    .getNavigationTarget();
            PageTitle title = navigationTarget.getAnnotation(PageTitle.class);
            String titleString = "";
            if (title != null) {
                titleString = title.value();
            } else {
                String typeName = navigationTarget.getTypeName();
                String simpleName = navigationTarget.getSimpleName();
                titleString = Utils.formatName(simpleName);
            }
            links.add(createLink(titleString, navigationTarget));
        });
        return links;
    }

    private static RouterLink createLink(String titleString,
            Class<? extends Component> view) {
        RouterLink link = new RouterLink();
        link.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary");
        link.setRoute(view);

        Span text = new Span(titleString);
        text.addClassNames("font-medium", "text-s");

        link.add(text);
        return link;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("flex", "items-center", "my-s", "px-m", "py-xs");

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
