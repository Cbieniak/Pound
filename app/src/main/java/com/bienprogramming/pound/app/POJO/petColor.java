package com.bienprogramming.pound.app.POJO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Christian on 13/04/2014.
 */
@DatabaseTable
public class PetColor {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true)
    private Color color;
    @DatabaseField(foreign = true)
    private Pet pet;

    public PetColor(){};


}
