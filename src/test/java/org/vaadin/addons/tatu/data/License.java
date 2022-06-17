package org.vaadin.addons.tatu.data;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Past;

public class License extends AbstractEntity {
    private BigDecimal license = new BigDecimal(332949143);
    private String licensor = "Bar";
    @Past(message = "Granted date must in the past")
    private LocalDate granted = LocalDate.now().minusYears(10);

    public BigDecimal getLicense() {
        return license;
    }

    public void setLicense(BigDecimal license) {
        this.license = license;
    }

    public String getLicensor() {
        return licensor;
    }

    public void setLicensor(String licensor) {
        this.licensor = licensor;
    }

    public LocalDate getGranted() {
        return granted;
    }

    public void setGranted(LocalDate granted) {
        this.granted = granted;
    }

    @Override
    public String toString() {
        return license + " : " + licensor;
    }
}
