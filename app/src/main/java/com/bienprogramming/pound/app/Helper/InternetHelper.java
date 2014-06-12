package com.bienprogramming.pound.app.Helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.bienprogramming.pound.app.POJO.Breed;
import com.bienprogramming.pound.app.POJO.Color;
import com.bienprogramming.pound.app.POJO.DBHelper;
import com.bienprogramming.pound.app.POJO.Pet;
import com.bienprogramming.pound.app.POJO.PetColor;
import com.bienprogramming.pound.app.POJO.PetLocation;
import com.bienprogramming.pound.app.POJO.Species;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gladys on 22/05/2014.
 */
public class InternetHelper {

    public static String fetchData(String url,int timeout) throws IOException
    {

        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
        HttpConnectionParams.setSoTimeout(httpParams, timeout);
        HttpClient client = new DefaultHttpClient(httpParams);

        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"),8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }

    public static String deleteData(String url,String token,int timeout) throws IOException
    {

        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
        HttpConnectionParams.setSoTimeout(httpParams, timeout);
        HttpClient client = new DefaultHttpClient(httpParams);

        HttpDelete request = new HttpDelete(url+"?auth_token="+token);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"),8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }



    public static Pet fetchPet(Context context, Integer petId) throws SQLException{
        Pet pet = new Pet();
        Dao<Pet,Integer> petDao = OpenHelperManager.getHelper(context,DBHelper.class).getPetDao();
        Dao<Color, Integer> colorDao =  OpenHelperManager.getHelper(context, DBHelper.class).getColorDao();
        Dao<PetColor, Integer> petColorDao =  OpenHelperManager.getHelper(context, DBHelper.class).getPetColorDao();
        Dao<PetLocation, Integer> petLocationDao =  OpenHelperManager.getHelper(context, DBHelper.class).getPetLocationDao();
        Dao<Species, Integer> speciesDao =  OpenHelperManager.getHelper(context, DBHelper.class).getSpeciesDao();
        Dao<Breed, Integer> breedDao =  OpenHelperManager.getHelper(context, DBHelper.class).getBreedDao();
        pet = petDao.queryForId(petId);
        Species species = speciesDao.queryForId(pet.getSpeciesId());
        Breed breed = breedDao.queryForId(pet.getBreedId());
        pet.setSpecies(species);
        pet.setBreed(breed);
        List<PetLocation> petLocationList = petLocationDao.queryForEq("petId",petId);
        if(petLocationList.size()>0)
            pet.setPetLocation(petLocationList.get(0));
        List<PetColor> petCOlo = petColorDao.queryForAll();
        List<PetColor> petColorList = petColorDao.queryForEq("petId",petId);
        ArrayList<Color> colorArrayList = new ArrayList<Color>();
        for(PetColor petColor : petColorList){
            colorArrayList.add(colorDao.queryForId(petColor.getColorId()));

        }

        pet.setColours(colorArrayList);


        return pet;
    }




}
