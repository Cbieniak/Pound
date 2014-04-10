package com.bienprogramming.pound.app.POJO;

import android.location.Location;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Christian on 6/04/2014.
 */
@DatabaseTable
public class PetLocation {
    @DatabaseField
    private int id;
    @DatabaseField
    private String suburb;
    @DatabaseField
    private double latitude;
    @DatabaseField
    private double longitude;
    public PetLocation(){}

    public PetLocation(String suburb, double latitude, double longitude) {
        this.suburb = suburb;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
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

    public PetLocation(Location location){
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
}
