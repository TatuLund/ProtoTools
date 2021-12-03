package org.vaadin.addons.tatu;

import java.util.List;

import javax.validation.constraints.Size;

import org.vaadin.addons.tatu.data.Car;
import org.vaadin.addons.tatu.data.License;
import org.vaadin.addons.tatu.data.Person;
import org.vaadin.addons.tatu.prototools.Form;
import org.vaadin.addons.tatu.prototools.AutoGrid;
import org.vaadin.addons.tatu.prototools.ListEdit;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

@Route("form")
public class FormView extends Div {

    public int indentiefier;

    public FormView() {
        Form<Person> autoForm = new Form<>(new Person(), Person.class, false);
        autoForm.setProperties("firstName","lastName","gender","weight","email","dateOfBirth");
        autoForm.addListProperty("cars", Car.class, Void -> new Car("Kia","Ceed"),"brand","model");
        autoForm.addBeanProperty("license", License.class, "license","licensor");
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
