package com.bienprogramming.pound.app.POJO;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Christian on 10/04/2014.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] classes = new Class[] {
       ContactDetail.class,Pet.class,PetLocation.class,PetColor.class,Color.class,Species.class,Breed.class
    };
    public static void main(String[] args) throws SQLException,IOException {
        writeConfigFile(new File("C:\\Users\\Christian\\Dropbox\\3027Pound\\Pound\\app\\src\\main\\res\\raw\\ormlite_config.txt"),classes);
        //writeConfigFile(new File("/Users/gladys/Work/Pound/app/src/main/res/raw/ormlite_config.txt"),classes);
    }
}
