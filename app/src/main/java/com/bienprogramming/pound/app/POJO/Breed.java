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
    @Expose  private String breed;

    @DatabaseField
    @Expose private int speciesId;

    public Breed() {
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
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
}
