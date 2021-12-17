package org.vaadin.addons.tatu;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.addons.tatu.data.Car;
import org.vaadin.addons.tatu.data.License;
import org.vaadin.addons.tatu.data.Person;
import org.vaadin.addons.tatu.prototools.ListEdit;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("List Edit")
@Route(value = "listedit", layout = MainLayout.class)
public class ListEditView extends Div implements HasUrlParameter<Integer> {

    private PersonRoster personRoster = new PersonRoster();
    private Binder<PersonRoster> binder = new Binder<>();
    private Integer requiredPersons;

    public ListEditView() {
        ListEdit<Person> listEdit = new ListEdit<Person>(Person.class,
                Void -> new Person());
        listEdit.setColumns("firstName", "lastName", "gender", "weight",
                "email", "dateOfBirth");
        listEdit.addListColumn("cars", Car.class,
                Void -> new Car("Kia", "Ceed"), "brand", "model");
        listEdit.addBeanColumn("license", License.class, "license", "licensor");
        binder.forField(listEdit)
                .withValidator(value -> value.size() == requiredPersons,
                        "Input required entries")
                .bind(PersonRoster::getPersons, PersonRoster::setPersons);
        binder.setBean(personRoster);
        listEdit.setWidth("100%");
        listEdit.addValueChangeListener(event -> {
            Notification.show("Items " + listEdit.getValue().size()
                    + " bound items " + personRoster.getPersons().size());
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

        Button readOnly = new Button(VaadinIcon.STOP.create());
        readOnly.addClickListener(event -> {
            listEdit.setReadOnly(!listEdit.isReadOnly());
        });
        add(listEdit, button, readOnly);
    }

    public class PersonRoster {
        private List<Person> persons = new ArrayList<>();

        public void setPersons(List<Person> persons) {
            this.persons = persons;
        }

        public List<Person> getPersons() {
            return persons;
        }
    }

    @Override
    public void setParameter(BeforeEvent event, Integer parameter) {
        requiredPersons = parameter;
    }

}
