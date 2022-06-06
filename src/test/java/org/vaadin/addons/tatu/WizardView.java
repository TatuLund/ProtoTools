package org.vaadin.addons.tatu;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import org.vaadin.addons.tatu.data.Car;
import org.vaadin.addons.tatu.data.License;
import org.vaadin.addons.tatu.data.Person;
import org.vaadin.addons.tatu.prototools.Form;
import org.vaadin.addons.tatu.prototools.Paging;
import org.vaadin.addons.tatu.prototools.Wizard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Wizard")
@Route(value = "wizard", layout = MainLayout.class)
public class WizardView extends Div {

    public WizardView() {

        Wizard wizard = new Wizard();
        Person person = new Person();
        wizard.withBeanPage(person, Person.class, "firstName", "lastName",
                "gender", "weight", "email", "dateOfBirth");
        wizard.withBeanPage(person.getLicense(), License.class, "license",
                "licensor", "granted");
        wizard.withListPage(person.getCars(), Car.class,
                Void -> new Car("Kia", "Ceed"), "brand", "model", "weight");
        wizard.build();

        add(wizard);
    }

}
