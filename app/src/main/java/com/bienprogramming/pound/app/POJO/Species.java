package com.bienprogramming.pound.app.POJO;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Christian on 4/05/2014.
 */
public class Species {
    @DatabaseField(generatedId = true)
    @Expose private Integer id;

    @DatabaseField
    @Expose private String species;

    public Species() {

    }


    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

}
