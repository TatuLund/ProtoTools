package org.vaadin.addons.tatu;

import org.vaadin.addons.tatu.data.Car;
import org.vaadin.addons.tatu.data.License;
import org.vaadin.addons.tatu.data.Person;
import org.vaadin.addons.tatu.prototools.Form;
import org.vaadin.addons.tatu.prototools.Paging;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Form")
@Route(value = "form", layout = MainLayout.class)
public class FormView extends Div {

    public FormView() {
        // Instantiate a new form
        Form<Person> autoForm = new Form<>(new Person(), Person.class, false);
        // Configure properties
        autoForm.setProperties("firstName", "lastName", "gender", "weight",
                "email", "dateOfBirth");
        autoForm.addListProperty("cars", Car.class,
                Void -> new Car("Kia", "Ceed"), "brand", "model", "weight");
        autoForm.addBeanProperty("license", License.class, "license",
                "licensor", "granted");
        add(autoForm);

        // Form is a field, you can listen to value changes or bind it
        autoForm.addValueChangeListener(event -> {
            Dialog dialog = new Dialog();
            // You can use the form as bean viewer in readonly mode
            Form<Person> form = new Form<>(event.getValue(), Person.class);
            form.setReadOnly(true);
            dialog.add(form);
            dialog.open();
        });
    }

}
