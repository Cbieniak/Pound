package com.bienprogramming.pound.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.Switch;

import com.bienprogramming.pound.app.Fragment.AttributeListFragment;
import com.bienprogramming.pound.app.Fragment.ColorListFragment;
import com.bienprogramming.pound.app.Fragment.ContactDetailFragment;
import com.bienprogramming.pound.app.Fragment.CreatePetFragment;
import com.bienprogramming.pound.app.Fragment.DisplayPetFragment;
import com.bienprogramming.pound.app.Fragment.FilterFragment;
import com.bienprogramming.pound.app.Fragment.MainFragment;
import com.bienprogramming.pound.app.Fragment.NavigationDrawerFragment;
import com.bienprogramming.pound.app.Fragment.PetFragment;
import com.bienprogramming.pound.app.POJO.Breed;
import com.bienprogramming.pound.app.POJO.Color;
import com.bienprogramming.pound.app.POJO.DBHelper;
import com.bienprogramming.pound.app.POJO.Filter;
import com.bienprogramming.pound.app.POJO.Pet;
import com.bienprogramming.pound.app.POJO.PetColor;
import com.bienprogramming.pound.app.POJO.PetLocation;
import com.bienprogramming.pound.app.POJO.Species;
import com.bienprogramming.pound.app.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends OrmLiteBaseActivity<DBHelper>
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, CreatePetFragment.OnPetCreatedListener, AttributeListFragment.OnItemChosenListener,AttributeListFragment.OnItemsChosenListener,
    ColorListFragment.OnColorsChosenListener, ContactDetailFragment.OnContactChosenListener, PetFragment.OnPetClickedListener, FilterFragment.OnFiltersChosenListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Pet currentPet;
    private CreatePetFragment createdPet;
    private FilterFragment filterFragment;
    private PetFragment searchFragment;
    private Filter filter;

    private Menu menu;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    static HorizontalScrollView lostPets;
    static HorizontalScrollView foundPets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        new UpdatePetTasks().execute("Tempo");
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch(position)
        {
            case 0:
                ft.replace(R.id.container, MainFragment.newInstance()).commit();
                break;
            case 1:
                searchFragment = PetFragment.newInstance();
                ft.replace(R.id.container,searchFragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 2:
                createdPet = CreatePetFragment.newInstance(false);
                ft.replace(R.id.container,createdPet);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 3:
                createdPet= CreatePetFragment.newInstance(true);
                ft.replace(R.id.container,createdPet);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 4:
                //ABout
                break;
            case 5:
               //Settings
                break;
        }


    }



    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            menu = m;
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();

            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if(item.getItemId() == R.id.action_search) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                PetFragment fragment = PetFragment.newInstance();
                searchFragment = fragment;
                ft.replace(R.id.container,fragment);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            } else if(item.getItemId() == R.id.action_filter) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                FilterFragment fragment = FilterFragment.newInstance(0);
                filterFragment = fragment;
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();

        }
        return super.onOptionsItemSelected(item);
    }
    public void setCreatedPet(CreatePetFragment fragment)
    {
        createdPet = fragment;
        getMenuInflater().inflate(R.menu.create, menu);
    }
    @Override
    public void onPetCreated(int id) {
        createdPet = null;
    }

    @Override
    public void onItemChosenListener(CreatePetFragment.Field field,Object item) {
        if(createdPet != null)
            createdPet.updateField(field, item);
        else
            filterFragment.updateField(field,item);
    }



    @Override
    public void colorsChosen(CreatePetFragment.Field field,ArrayList<com.bienprogramming.pound.app.POJO.Color> colors) {
        if(createdPet !=null)
            createdPet.updateField(field,colors);
        else
            filterFragment.updateField(field, colors);
        getFragmentManager().popBackStack();
    }

    @Override
    public void onContactChosen(int type, String contactDetail) {
        createdPet.updateField(CreatePetFragment.Field.FIELD_CONTACT_DETAIL,contactDetail,type);
    }

    @Override
    public void OnPetClicked(int id) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DisplayPetFragment fragment = DisplayPetFragment.newInstance(id);
        ft.replace(R.id.container,fragment);
        ft.addToBackStack(null);


        ft.commit();
    }

    @Override
    public void onFiltersChosen(Filter filter) {
        searchFragment.filterResults(filter);

    }


    @Override
    public void onItemsChosen(CreatePetFragment.Field field, ArrayList<String> items) {

    }

    public class UpdatePetTasks extends AsyncTask<String,Integer,String>
    {
        int TIMEOUT_MILLISEC = 10000;
        @Override
        protected String doInBackground(String... strings) {

            try{
                String result;

                InputStream is = null;
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpClient client = new DefaultHttpClient(httpParams);

                HttpGet request = new HttpGet(getString(R.string.server_base_address)+"/pets.json");
                request.setHeader("Accept", "application/json");
                request.setHeader("Content-type", "application/json");
                HttpResponse response = client.execute(request);

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Gson responseGSon = new GsonBuilder().create();

                Pet[] pets = responseGSon.fromJson(result,Pet[].class);

                Dao<Pet, Integer> petDao =  OpenHelperManager.getHelper(getBaseContext(), DBHelper.class).getPetDao();
                Dao<PetLocation, Integer> petlocationDao = OpenHelperManager.getHelper(getBaseContext(), DBHelper.class).getPetLocationDao();
                Dao<Color, Integer> colorDao =  OpenHelperManager.getHelper(getApplicationContext(), DBHelper.class).getColorDao();
                Dao<PetColor, Integer> petColorDao =  OpenHelperManager.getHelper(getApplicationContext(), DBHelper.class).getPetColorDao();
                Dao<Breed, Integer> breedDao =  OpenHelperManager.getHelper(getApplicationContext(), DBHelper.class).getBreedDao();
                Dao<Species, Integer> speciesDao =  OpenHelperManager.getHelper(getApplicationContext(), DBHelper.class).getSpeciesDao();


                for(Pet pet : pets)
                {
                    //If temp image get temp image
                    if(petDao.queryForId(pet.getId())== null)
                    {
                        petDao.createOrUpdate(pet);
                    }
                    if(pet.getPetLocation() != null)
                    {
                        petlocationDao.createOrUpdate(pet.getPetLocation());
                    }

                    //Pet color creates a new color. going to have to search by pet color to see if it exists
                    //Possibly using query builder. "Pet_id" eq and "Color_id" eq
                    for(Color color : pet.getColours()){
                        colorDao.createOrUpdate(color);
                        PetColor petColor = new PetColor(pet,color);
                        petColorDao.create(petColor);
                    }

                    if(pet.getSpecies() != null)
                    {
                        speciesDao.createOrUpdate(pet.getSpecies());
                    }

                    if(pet.getBreed() != null)
                    {
                        breedDao.createOrUpdate(pet.getBreed());
                    }

                }

            }catch (Exception e){
                Log.d("JSONRESULT", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //Use TAG?
            //Fragment fragment = getFragmentManager().findFragmentById(R.layout.fragment_main);
            //getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();

        }
    }

}
