package org.vaadin.addons.tatu;

import org.vaadin.addons.tatu.data.Car;
import org.vaadin.addons.tatu.data.License;
import org.vaadin.addons.tatu.data.Person;
import org.vaadin.addons.tatu.prototools.AutoGrid;
import org.vaadin.addons.tatu.prototools.ComboCrud;
import org.vaadin.addons.tatu.prototools.GridCrud;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Combo Crud")
@Route(value = "combo-crud", layout = MainLayout.class)
public class ComboCrudView extends Div {

    public ComboCrudView() {
        ComboCrud<Person> crud = new ComboCrud<>(Person.class, false);
        crud.setItems(new Person(), new Person(), new Person());
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