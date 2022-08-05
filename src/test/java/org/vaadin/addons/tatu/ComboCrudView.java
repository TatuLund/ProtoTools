package org.vaadin.addons.tatu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vaadin.addons.tatu.data.Car;
import org.vaadin.addons.tatu.data.License;
import org.vaadin.addons.tatu.data.Person;
import org.vaadin.addons.tatu.prototools.ComboCrud;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Combo Crud")
@Route(value = "combo-crud", layout = MainLayout.class)
public class ComboCrudView extends Div {

    public ComboCrudView() {
        ComboCrud<Person> crud = new ComboCrud<>(Person.class, false);
        List<Person> items = new ArrayList<Person>(
                Arrays.asList(new Person(), new Person(), new Person()));
        crud.setItems(items);
        // Show three properties in Grid
        crud.setComboProperties("firstName", "lastName", "weight");
        // Show all needed properties in the form
        crud.setFormProperties("firstName", "lastName", "gender", "weight",
                "email", "dateOfBirth");
        // Add list editor for car list in the form
        crud.addListProperty("cars", Car.class,
                Void -> new Car("Kia", "Ceed"), "brand", "model");
        // Add sub form for license property
        crud.addBeanProperty("license", License.class, "license", "licensor");

        add(crud);
    }
}