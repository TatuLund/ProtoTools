package org.vaadin.addons.tatu;

import org.vaadin.addons.tatu.prototools.MenuLayout;

import com.vaadin.flow.component.UI;

public class MainLayout extends MenuLayout {

    public MainLayout() {
        // Just set menu and app title, the rest is generated
        // automatically
        setAppTitle("ProtoTools");
        setMenuTitle("Menu");
    }

}
