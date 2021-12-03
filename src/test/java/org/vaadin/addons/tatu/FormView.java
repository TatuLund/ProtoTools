package org.vaadin.addons.tatu;

import org.vaadin.addons.tatu.data.Car;
import org.vaadin.addons.tatu.data.License;
import org.vaadin.addons.tatu.data.Person;
import org.vaadin.addons.tatu.prototools.Form;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("form")
public class FormView extends Div {

    public int indentiefier;

    public FormView() {
        Form<Person> autoForm = new Form<>(new Person(), Person.class, false);
        autoForm.setProperties("firstName", "lastName", "gender", "weight",
                "email", "dateOfBirth");
        autoForm.addListProperty("cars", Car.class,
                Void -> new Car("Kia", "Ceed"), "brand", "model");
        autoForm.addBeanProperty("license", License.class, "license",
                "licensor");
        autoForm.addValueChangeListener(event -> {
            Dialog dialog = new Dialog();
            Form<Person> form = new Form<>(event.getValue(), Person.class);
            form.setReadOnly(true);
            dialog.add(form);
            dialog.open();
        });

        add(autoForm);
    }

}
