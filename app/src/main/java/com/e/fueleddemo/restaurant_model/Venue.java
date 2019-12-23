package com.e.fueleddemo.restaurant_model;

import java.util.ArrayList;

public class Venue {
    String name;
    Location location;
    ArrayList<Category> categories;

    public Venue(String name, Location location, ArrayList<Category> categories) {
        this.name = name;
        this.location = location;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

}
