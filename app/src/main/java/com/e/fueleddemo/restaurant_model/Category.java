package com.e.fueleddemo.restaurant_model;

public class Category {
    String id;
    String name;
    String pluralName;
    String shortName;
    Icon icon;

    public Category(String id, String name, String pluralName, String shortName, Icon icon) {
        this.id = id;
        this.name = name;
        this.pluralName = pluralName;
        this.shortName = shortName;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPluralName() {
        return pluralName;
    }

    public void setPluralName(String pluralName) {
        this.pluralName = pluralName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
}
