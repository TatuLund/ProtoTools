package org.vaadin.addons.tatu.prototools;

class Utils {

    static String formatName(String propertyName) {
        if (propertyName == null || propertyName.isEmpty())
            return "";
        String name = propertyName.replaceAll(
                String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"),
                " ");
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }
}
