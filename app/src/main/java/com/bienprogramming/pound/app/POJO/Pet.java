package com.bienprogramming.pound.app.POJO;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.renderscript.Element;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Christian on 6/04/2014.
 */
@DatabaseTable
public class Pet implements Serializable {
    //Pet
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(index = true)
    @Expose private String imageUrl;
    @DatabaseField
    @Expose private String thumbURL;
    @DatabaseField
    @Expose private String name;

    private Species species;
    @DatabaseField
    @Expose private int speciesId;
    private ArrayList<Breed> breeds;



    @DatabaseField (dataType = DataType.SERIALIZABLE)
    @Expose private int[] breedIds;
    @DatabaseField
    @Expose private double reward;
    @DatabaseField
    @Expose private String notes;
    @DatabaseField
    @Expose private String contactName;
    @DatabaseField
    @Expose private String contactDetail;
    @DatabaseField
    @Expose private int contactType;
    @DatabaseField
    @Expose private boolean lost;

    @Expose private ArrayList<Color> colours;
    @DatabaseField(foreign = true)
    @Expose private PetLocation petLocation;


    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] imageBlob;


    //private Bitmap thumbImage;
    //private Bitmap image;
    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
        this.speciesId = species.getId();
    }

    public ArrayList<Color> getColours() {
        return colours;
    }

    public void setColours(ArrayList<Color> colours) {
        this.colours = colours;
    }

    public Pet(){

    }
    public Pet(String name, Species species, Breed breed, ArrayList<String> petColours, PetLocation petLocation, ContactDetail contactDetail){
        this.name = name;
        this.species = species;
        this.breeds =new ArrayList<Breed>();
        this.breeds.add(breed);

    }

    public Pet(String name, Species species, Breed breed){
        this.name = name;
        this.species = species;
        this.breeds =new ArrayList<Breed>();
        this.breeds.add(breed);
        this.notes = "THIS IS A REALLY LONG NOTE PROVIDING ADDITION NEWS ABOUT THE PET";
        /*
        Color color1 = new Color("Brown");
        Color color2 = new Color("Orange",);
        ArrayList<Color> colors = new ArrayList<Color>();

        colors.add(color1);
        colors.add(color2);
        this.colours=colors;
        */
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

    public ArrayList<Breed> getBreeds() {
        return breeds;
    }

    public void setBreeds(ArrayList<Breed> breeds) {
        this.breeds = breeds;
        int[] breedIds = new int[this.breeds.size()];
        for(int i = 0; i < this.breeds.size(); i++){
            breedIds[i] = this.breeds.get(i).getId();

        }
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


    public PetLocation getPetLocation() {
        return petLocation;
    }

    public void setPetLocation(PetLocation petLocation) {
        this.petLocation = petLocation;
    }


    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactName() {
        return contactName;
    }

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public int getContactType() {
        return contactType;
    }

    public void setContactType(int contactType) {
        this.contactType = contactType;
    }

    public String getContactDetail() {
        return contactDetail;
    }

    public void setContactDetail(String contactDetail) {
        this.contactDetail = contactDetail;
    }

    public byte[] getImageBlob() {
        return imageBlob;
    }

    public void setImageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
    }

    public String toString(){
        return name;
    }

    public int getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(int speciesId) {
        this.speciesId = speciesId;
    }

    public int[] getBreedIds() {
        return breedIds;
    }

    public void setBreedIds(int[] breedIds) {
        this.breedIds = breedIds;
    }
}
