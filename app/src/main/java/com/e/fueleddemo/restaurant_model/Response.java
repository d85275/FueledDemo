package com.e.fueleddemo.restaurant_model;

import java.util.ArrayList;

public class Response {

    ArrayList<Venue> venues;

    public Response(ArrayList<Venue> venues) {
        this.venues = venues;
    }

    public ArrayList<Venue> getVenues() {
        return venues;
    }

    public void setVenues(ArrayList<Venue> venues) {
        this.venues = venues;
    }
}
