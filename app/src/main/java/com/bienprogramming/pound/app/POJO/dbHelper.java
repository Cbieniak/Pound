package com.bienprogramming.pound.app.POJO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bienprogramming.pound.app.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Christian on 10/04/2014.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "Pound.db";
    private static final int DATABASE_VERSION = 1;
    private Dao<Pet,Integer> petDao = null;
    private Dao<PetLocation,Integer> petLocationDao = null;
    private Dao<ContactDetail,Integer> contactDetailDao = null;
    private Dao<PetColor,Integer> petColorDao = null;
    private Dao<Color,Integer> colorDao = null;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
        //super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource){
        try {
            TableUtils.createTable(connectionSource, Pet.class);
            TableUtils.createTable(connectionSource, PetLocation.class);
            TableUtils.createTable(connectionSource, ContactDetail.class);
            TableUtils.createTable(connectionSource, PetColor.class);
            TableUtils.createTable(connectionSource, Color.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Pet.class, true);
            TableUtils.dropTable(connectionSource, PetLocation.class, true);
            TableUtils.dropTable(connectionSource, ContactDetail.class, true);
            TableUtils.dropTable(connectionSource, PetColor.class, true);
            TableUtils.dropTable(connectionSource, Color.class, true);
            onCreate(db,connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Pet, Integer> getPetDao() throws SQLException {
        if (petDao == null) {
            petDao = getDao(Pet.class);
        }
        return petDao;
    }

    public Dao<PetLocation, Integer> getPetLocationDao() throws SQLException {
        if (petLocationDao == null) {
            petLocationDao = getDao(PetLocation.class);
        }
        return petLocationDao;
    }
    public Dao<PetColor, Integer> getPetColorDao() throws SQLException {
        if (petColorDao == null) {
            petColorDao = getDao(PetColor.class);
        }
        return petColorDao;
    }

    public Dao<Color, Integer> getColorDao() throws SQLException {
        if (colorDao == null) {
            colorDao = getDao(Color.class);
        }
        return colorDao;
    }


    public Dao<ContactDetail, Integer> getContactDetailDao() throws SQLException {
        if (contactDetailDao == null) {
            contactDetailDao = getDao(ContactDetail.class);
        }
        return contactDetailDao;
    }

    @Override
    public void close() {
        super.close();
        petDao = null;
        petLocationDao = null;
        contactDetailDao = null;
        petColorDao = null;
        colorDao = null;

    }
}
