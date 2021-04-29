package com.example.taksi;


public class TaxiLookup {
    public TaxiLookup() {
    }

    public TaxiLookup(String borough, String service_zone, String zone, double latitude, double longitude, int LocationID) {
        this.borough = borough;
        this.service_zone = service_zone;
        this.zone = zone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.LocationID = LocationID;
    }

    private String borough;
    private String service_zone;
    private String zone;
    private double latitude;
    private double longitude;
    private int LocationID;

    public int getLocationID() {
        return LocationID;
    }

    public void setLocationID(int LocationID) {
        this.LocationID = LocationID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getBorough() {
        return borough;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public String getService_zone() {
        return service_zone;
    }

    public void setService_zone(String service_zone) {
        this.service_zone = service_zone;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
