package com.bienprogramming.pound.app.POJO;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Christian on 4/05/2014.
 */
public class Breed {
    @DatabaseField(generatedId = true)
    @Expose private Integer id;
    @DatabaseField
    @Expose  private String name;

    @DatabaseField
    @Expose private int speciesId;

    public Breed() {
    }

    public Breed(String breed, int speciesId) {
        this.name = breed;
        this.speciesId = speciesId;
    }

    public String getBreed() {
        return name;
    }

    public void setBreed(String breed) {
        this.name = breed;
    }

    public int getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(int speciesId) {
        this.speciesId = speciesId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String toString(){return name;}
}
