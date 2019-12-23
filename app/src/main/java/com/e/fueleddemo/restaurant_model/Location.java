package com.e.fueleddemo.restaurant_model;

public class Location {
    String address;
    String postalCode;
    double lat;
    double lng;


    public Location(String address, String postalCode, double lat, double lng) {
        this.address = address;
        this.postalCode = postalCode;
        this.lat = lat;
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Location{" +
                "address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
