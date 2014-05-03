package com.bienprogramming.pound.app.POJO;

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Christian on 6/04/2014.
 */
@DatabaseTable
public class PetLocation {
    @DatabaseField(generatedId = true)
    @Expose private int id;
    @DatabaseField
    @Expose private String suburb;
    @DatabaseField
    @Expose private double latitude;
    @DatabaseField
    @Expose private double longitude;
    @DatabaseField
    @Expose private int petId;
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
    public String toString(){
        return this.suburb;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

}
