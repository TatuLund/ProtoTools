package org.vaadin.addons.tatu;

import java.util.List;

import javax.validation.constraints.Size;

import org.vaadin.addons.tatu.data.Car;
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

@Route("listedit")
public class ListEditView extends Div {

    private PersonRoster personRoster = new PersonRoster();
    private Binder<PersonRoster> binder = new Binder<>();

    public ListEditView() {
        ListEdit<Person> gridField = new ListEdit<Person>(Person.class, Void -> new Person());
        gridField.setColumns("firstName","lastName","gender","weight","email","dateOfBirth");
        binder.forField(gridField).bind(PersonRoster::getPersons,
                PersonRoster::setPersons);
        binder.setBean(personRoster);
        gridField.setWidth("100%");
        gridField.addValueChangeListener(event -> {
            Notification.show("Items " + gridField.getValue().size() + " bound items " + personRoster.getPersons().size());
        });

        Button button = new Button(VaadinIcon.TABLE.create());
        button.addClickListener(event -> {
            Dialog dialog = new Dialog();
            Grid<Person> grid = new Grid<>(Person.class);
            grid.setItems(personRoster.getPersons());
            grid.setWidth("600px");
            dialog.add(grid);
            dialog.open();
        });
        
        add(gridField, button);
    }

    public class PersonRoster {
        private List<Person> persons;

        public void setPersons(List<Person> persons) {
            this.persons = persons;
        }

        public List<Person> getPersons() {
            return persons;
        }
    }
    


}
