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

@Route("grid")
public class GridView extends Div {

    public GridView() {
        AutoGrid<Person> autoGrid = new AutoGrid<>(Person.class, false);
        autoGrid.setItems(new Person(), new Person(), new Person());
        autoGrid.setColumns("firstName","lastName","gender","weight","email","dateOfBirth");
        autoGrid.addListColumn("cars", Car.class, Void -> new Car("Kia","Ceed"),"brand","model");
        autoGrid.addBeanColumn("license", License.class, "license","licensor");
        
        add(autoGrid);
    }

}
