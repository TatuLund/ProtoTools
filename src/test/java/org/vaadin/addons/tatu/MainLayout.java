package org.vaadin.addons.tatu;

import org.vaadin.addons.tatu.prototools.MenuLayout;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.lumo.Lumo;

@CssImport("./shared-styles.css")
public class MainLayout extends MenuLayout implements AppShellConfigurator {

    public MainLayout() {
        // Just set menu and app title, the rest is generated
        // automatically
        setAppTitle("ProtoTools");
        setMenuTitle("Menu");

//        Checkbox variant = new Checkbox("Dark");
//        variant.addValueChangeListener(event -> {
//            if (event.getValue()) {
//                setVariant(Lumo.DARK);
//            } else {
//                setVariant(Lumo.LIGHT);                
//            }
//        });
//        addToDrawer(variant);
    }
}
