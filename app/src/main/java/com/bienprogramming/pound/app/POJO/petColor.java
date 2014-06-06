package com.bienprogramming.pound.app.POJO;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Christian on 13/04/2014.
 */
@DatabaseTable
public class PetColor {
    @DatabaseField(generatedId = true,allowGeneratedIdInsert=true )
    private int id;
    @DatabaseField
    private int colorId;
    @DatabaseField
    private int petId;

    public PetColor(){};

    public PetColor(Pet pet, Color color){
        this.petId = pet.getId();
        this.colorId = color.getId();
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }
}
