package org.vaadin.addons.tatu.data;

import java.time.LocalTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class Car extends AbstractEntity {
    private String brand;
    private String model;
    @Min(value = 800, message = "Weight should be atleast 800")
    @Max(value = 4000, message = "Weight should be maximum 4000")
    private double weight;
    private LocalTime available;

    public Car(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public LocalTime getAvailable() {
        return available;
    }

    public void setAvailable(LocalTime available) {
        this.available = available;
    }


}
