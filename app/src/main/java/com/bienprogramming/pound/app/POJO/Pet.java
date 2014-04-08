package com.bienprogramming.pound.app.POJO;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Christian on 6/04/2014.
 */

public class Pet implements Parcelable {
    //Pet
    private URL imageUrl;
    private Bitmap thumbURL;
    private Bitmap image;
    private String name;
    private String species;
    private String breed;
    private ArrayList<String> petColours;
    private double reward;
    private String notes;
    //Location
    private PetLocation petLocation;
    //Contact Owner
    private ContactDetail contactDetail;

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public PetLocation getPetLocation() {
        return petLocation;
    }

    public void setPetLocation(PetLocation petLocation) {
        this.petLocation = petLocation;
    }

    public Pet(String name, String species, String breed, ArrayList<String> petColours, PetLocation petLocation, ContactDetail contactDetail){
        this.name = name;
        this.species = species;

        this.breed = breed;
        this.petColours = petColours;
        this.petLocation = petLocation;
        this.contactDetail = contactDetail;
    }

    public Pet(String name, String species, String breed){
        this.name = name;
        this.species = species;
        this.breed = breed;

    }

    public ContactDetail getContactDetail() {
        return contactDetail;
    }

    public void setContactDetail(ContactDetail contactDetail) {
        this.contactDetail = contactDetail;
    }

    public PetLocation getLocation() {
        return petLocation;
    }

    public void setLocation(PetLocation location) {
        this.petLocation = location;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(URL imageUrl) {
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

    public ArrayList<String> getPetColours() {
        return petColours;
    }

    public void setPetColours(ArrayList<String> petColours) {
        this.petColours = petColours;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
