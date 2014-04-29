package com.bienprogramming.pound.app.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Christian on 13/04/2014.
 */
@DatabaseTable
public class Color implements Parcelable{
    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField
    private String colorName;

    @DatabaseField
    private int colorValue;

    public Color(){};

    public Color(String colorName, int colorValue)
    {
        this.colorName=colorName;
        this.colorValue=colorValue;
    }

    public int getColorValue() {
        return colorValue;
    }

    public void setColorValue(int colorValue) {
        this.colorValue = colorValue;
    }

    public Color(String colorName){
        this.colorName = colorName;
    };

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String toString(){return colorName;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(colorName);
        parcel.writeInt(colorValue);

    }
}
