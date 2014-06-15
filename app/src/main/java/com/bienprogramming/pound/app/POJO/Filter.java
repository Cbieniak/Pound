package com.bienprogramming.pound.app.POJO;

import java.util.ArrayList;

/**
 * Created by Christian on 29/04/2014.
 */

public class Filter {
    Species species;
    Breed breed;
    ArrayList<Color> colours;
    PetLocation location;
    String reward;
    String notes;

    public Filter() {
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public ArrayList<Color> getColours() {
        return colours;
    }

    public void setColours(ArrayList<Color> colours) {
        this.colours = colours;
    }

    public PetLocation getLocation() {
        return location;
    }

    public void setLocation(PetLocation location) {
        this.location = location;
    }

    public String getRewards() {
        return reward;
    }

    public void setRewards(String reward) {
        this.reward = reward;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


}
