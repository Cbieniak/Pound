package com.bienprogramming.pound.app.POJO;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.renderscript.Element;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
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
    @DatabaseField
    private String breed;
    @DatabaseField
    private double reward;
    @DatabaseField
    private String notes;
    @DatabaseField
    private boolean lost;

    private ArrayList<Color> colours;
    @DatabaseField(foreign = true)
    private PetLocation petLocation;
    @DatabaseField(foreign = true)
    private ContactDetail contactDetail;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] imageBlob;


    //private Bitmap thumbImage;
    //private Bitmap image;
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public ArrayList<Color> getColours() {
        return colours;
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
        this.notes = "THIS IS A REALLY LONG NOTE PROVIDING ADDITION NEWS ABOUT THE PET";
        Color color1 = new Color("Brown");
        Color color2 = new Color("Orange");
        ArrayList<Color> colors = new ArrayList<Color>();
        colors.add(color1);
        colors.add(color2);
        this.colours=colors;
        this.reward=25.0;


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
