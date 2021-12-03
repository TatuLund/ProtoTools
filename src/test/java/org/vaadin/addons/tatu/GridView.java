package org.vaadin.addons.tatu;


import org.vaadin.addons.tatu.data.Car;
import org.vaadin.addons.tatu.data.License;
import org.vaadin.addons.tatu.data.Person;
import org.vaadin.addons.tatu.prototools.AutoGrid;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Auto Grid")
@Route(value = "grid", layout = MainLayout.class)
public class GridView extends Div {

    public GridView() {
        AutoGrid<Person> autoGrid = new AutoGrid<>(Person.class, false);
        autoGrid.setItems(new Person(), new Person(), new Person());
        autoGrid.setColumns("firstName", "lastName", "gender", "weight",
                "email", "dateOfBirth");
        autoGrid.addListColumn("cars", Car.class,
                Void -> new Car("Kia", "Ceed"), "brand", "model");
        autoGrid.addBeanColumn("license", License.class, "license", "licensor");

        add(autoGrid);
    }

}
