package org.vaadin.addons.tatu;

import org.vaadin.addons.tatu.data.Car;
import org.vaadin.addons.tatu.data.License;
import org.vaadin.addons.tatu.data.Person;
import org.vaadin.addons.tatu.prototools.GridCrud;
import org.vaadin.addons.tatu.prototools.PopupCrud;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Popup Crud")
@Route(value = "popup-crud", layout = MainLayout.class)
public class PopupCrudView extends Div {

    public PopupCrudView() {
        PopupCrud<Person> crud = new PopupCrud<>(Person.class, false);
        crud.setItems(new Person(), new Person(), new Person());
        crud.setFormProperties("firstName", "lastName", "gender", "weight",
                "email", "dateOfBirth");
        crud.setGridProperties("firstName", "lastName", "gender");
        crud.addListProperty("cars", Car.class,
                Void -> new Car("Kia", "Ceed"), "brand", "model");
        crud.addBeanProperty("license", License.class, "license", "licensor");

        add(crud);
    }
}
