package org.vaadin.addons.tatu.data;

public class License extends AbstractEntity {
    private String license = "Foo";
    private String licensor = "Bar";

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicensor() {
        return licensor;
    }

    public void setLicensor(String licensor) {
        this.licensor = licensor;
    }

    @Override
    public String toString() {
        return license + " : " + licensor;
    }
}
