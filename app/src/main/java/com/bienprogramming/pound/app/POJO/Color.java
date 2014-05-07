package com.bienprogramming.pound.app.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Christian on 13/04/2014.
 */
@DatabaseTable
public class Color implements Serializable{
    @DatabaseField(generatedId = true)
    @Expose private Integer id;
    @DatabaseField
    @Expose  private String name;

    @DatabaseField
    @Expose private String value;

    public Color(){};

    public Color(String colorName, String colorValue)
    {
        this.name=colorName;
        this.value=colorValue;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
