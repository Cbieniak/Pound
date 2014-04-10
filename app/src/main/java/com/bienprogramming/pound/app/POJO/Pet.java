package com.bienprogramming.pound.app.POJO;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Christian on 6/04/2014.
 */
@DatabaseTable
public class Pet {
    //Pet
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(index = true)
    private String imageUrl;
    @DatabaseField
    private String thumbURL;
    @DatabaseField
    private String name;
    @DatabaseField
    private String species;
    private String breed;
    @DatabaseField
    private double reward;
    @DatabaseField
    private String notes;


    //@DatabaseField
    //private ArrayList<String> petColours;
    //Location
    //@DatabaseField(foreign = true)
    //private PetLocation petLocation;
    //Contact Owner
    //@DatabaseField(foreign = true)
    //private ContactDetail contactDetail;


    //private Bitmap thumbImage;
    //private Bitmap image;
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }


    public Pet(){

    }
    public Pet(String name, String species, String breed, ArrayList<String> petColours, PetLocation petLocation, ContactDetail contactDetail){
        this.name = name;
        this.species = species;

        this.breed = breed;

    }

    public Pet(String name, String species, String breed){
        this.name = name;
        this.species = species;
        this.breed = breed;

    }




    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }


    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbURL() {
        return thumbURL;
    }

    public void setThumbURL(String thumbURL) {
        this.thumbURL = thumbURL;
    }
    //Parcelable



}
