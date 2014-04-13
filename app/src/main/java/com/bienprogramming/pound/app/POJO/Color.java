package com.bienprogramming.pound.app.POJO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Christian on 13/04/2014.
 */
@DatabaseTable
public class Color {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String colorName;
    @DatabaseField
    private int colorValue;

    public Color(){};
}
