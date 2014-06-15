package com.bienprogramming.pound.app.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Christian on 6/04/2014.
 */
@DatabaseTable
public class Pet implements Serializable {
    //Pet
    @DatabaseField(generatedId = true,allowGeneratedIdInsert=true )
    private int id;
    @DatabaseField(index = true)
    @SerializedName("image_url") @Expose  private String imageUrl;
    @DatabaseField
    @Expose private String thumbURL;
    @DatabaseField
    @Expose private String name;
    private Species species;
    @DatabaseField
    @SerializedName("species_id")@Expose private int speciesId;
    private Breed breed;
    @DatabaseField
    @SerializedName("breed_id")@Expose private int breedId;
    @DatabaseField
    @Expose private double reward;
    @DatabaseField
    @Expose private String notes;
    @DatabaseField
    @SerializedName("contact_name")@Expose private String contactName;
    @DatabaseField
    @SerializedName("contact_detail")@Expose private String contactDetail;
    @DatabaseField
    @SerializedName("contact_type")@Expose private int contactType;
    @DatabaseField
    @Expose private boolean lost;

    @DatabaseField (dataType = DataType.SERIALIZABLE)
    @SerializedName("color_ids")@Expose private ArrayList<Integer> colorIds;
    @SerializedName("colors")@Expose private ArrayList<Color> colours;
    @DatabaseField(foreign = true)
    @SerializedName("pet_location")@Expose private PetLocation petLocation;

    @DatabaseField
    @Expose private int creator;


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
        this.breed = breed;
        this.breedId = breed.getId();

    }

    public Pet(String name, Species species, Breed breed){
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.breedId = breed.getId();
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

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
        this.breedId = breed.getId();
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

    public int getBreedId() {
        return breedId;
    }

    public void setBreedId(int breedId) {
        this.breedId = breedId;
    }


    public ArrayList<Integer> getColorIds() {
        return colorIds;
    }

    public void setColorIds(ArrayList<Integer> colorIds) {
        this.colorIds = colorIds;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }
}
