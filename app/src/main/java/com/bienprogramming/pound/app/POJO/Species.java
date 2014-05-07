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
    @Expose private String name;

    public Species() {

    }
    public Species(String name) {
        this.name = name;
    }


    public String getSpecies() {
        return name;
    }

    public void setSpecies(String species) {
        this.name = species;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return name;
    }

}
