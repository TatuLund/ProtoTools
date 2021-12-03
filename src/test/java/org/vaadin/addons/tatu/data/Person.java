package org.vaadin.addons.tatu.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

public class Person extends AbstractEntity {
    @Size(min = 1, max = 100, message = "Name must be 1 to 100 characters long")
    private String firstName;
    @Size(min = 1, max = 100, message = "Name must be 1 to 100 characters long")
    private String lastName;
    @Min(value = 40, message = "Weight should be atleast 40")
    private int weight;
    @Past(message = "Date of birth must in the past")
    private Date dateOfBirth;
    @Email(message = "Email should be valid")
    private String email;
    @NotNull(message = "Gender is mandatory to fill")
    private Gender gender;
    private List<Car> cars;
    private License license = new License();
    
    public Person() {
        firstName = "John";
        lastName = "Doe";
        weight = 70;
//        dateOfBirth = LocalDate.now().minusYears(25);
        dateOfBirth = new Date();
        cars = new ArrayList<>();
        cars.add(new Car("Ford","Mondeo"));
        cars.add(new Car("Toyota","Yaris"));
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int age) {
        this.weight = age;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

}

