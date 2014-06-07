package com.bienprogramming.pound.app.POJO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Christian on 6/04/2014.
 */
@DatabaseTable
public class ContactDetail {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int type;
    @DatabaseField
    private String detail;
    @DatabaseField
    private int petId;


    public ContactDetail(){

    }
    public ContactDetail(int type, String detail){
        this.detail = detail;
        this.type = type;

    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
