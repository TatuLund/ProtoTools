package org.vaadin.addons.tatu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vaadin.addons.tatu.data.Car;
import org.vaadin.addons.tatu.data.License;
import org.vaadin.addons.tatu.data.Person;
import org.vaadin.addons.tatu.prototools.AutoGrid;
import org.vaadin.addons.tatu.prototools.GridCrud;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Grid Crud")
@Route(value = "crud", layout = MainLayout.class)
public class CrudView extends Div {

    public CrudView() {
        GridCrud<Person> crud = new GridCrud<>(Person.class, false);
        List<Person> items = Arrays.asList(new Person(), new Person(), new Person());
        crud.setItems(items);
        // Show three properties in Grid
        crud.setGridProperties("firstName", "lastName", "gender");
        // Show all needed properties in the form
        crud.setFormProperties("firstName", "lastName", "gender", "weight",
                "email", "dateOfBirth");
        // Add list editor for car list in the form
        crud.addListProperty("cars", Car.class,
                Void -> new Car("Kia", "Ceed"), "brand", "model", "weight", "available");
        // Add sub form for license propert
        crud.addBeanProperty("license", License.class, "license", "licensor", "granted");

        Button readOnly = new Button(VaadinIcon.STOP.create());
        readOnly.addClickListener(event -> {
            crud.setReadOnly(!crud.isReadOnly());
        });
        
        add(readOnly, crud);
    }
}