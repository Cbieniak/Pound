package com.bienprogramming.pound.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.bienprogramming.pound.app.Fragment.AboutFragment;
import com.bienprogramming.pound.app.Fragment.AttributeListFragment;
import com.bienprogramming.pound.app.Fragment.ColorListFragment;
import com.bienprogramming.pound.app.Fragment.ContactDetailFragment;
import com.bienprogramming.pound.app.Fragment.CreatePetFragment;
import com.bienprogramming.pound.app.Fragment.DisplayPetFragment;
import com.bienprogramming.pound.app.Fragment.FilterFragment;
import com.bienprogramming.pound.app.Fragment.LoginFragment;
import com.bienprogramming.pound.app.Fragment.MainFragment;
import com.bienprogramming.pound.app.Fragment.NavigationDrawerFragment;
import com.bienprogramming.pound.app.Fragment.PetListFragment;
import com.bienprogramming.pound.app.Fragment.SettingsFragment;
import com.bienprogramming.pound.app.Helper.InternetHelper;
import com.bienprogramming.pound.app.POJO.Breed;
import com.bienprogramming.pound.app.POJO.Color;
import com.bienprogramming.pound.app.POJO.DBHelper;
import com.bienprogramming.pound.app.POJO.Filter;
import com.bienprogramming.pound.app.POJO.Pet;
import com.bienprogramming.pound.app.POJO.PetColor;
import com.bienprogramming.pound.app.POJO.PetLocation;
import com.bienprogramming.pound.app.POJO.Species;
import com.bienprogramming.pound.app.POJO.User;
import com.bienprogramming.pound.app.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends OrmLiteBaseActivity<DBHelper>
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, CreatePetFragment.OnPetCreatedListener, AttributeListFragment.OnItemChosenListener, AttributeListFragment.OnItemsChosenListener,
        ColorListFragment.OnColorsChosenListener, ContactDetailFragment.OnContactChosenListener, PetListFragment.OnPetClickedListener, FilterFragment.OnFiltersChosenListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CreatePetFragment createdPet;
    private FilterFragment filterFragment;
    private PetListFragment searchFragment;


    private UiLifecycleHelper uiHelper;
    public AtomicBoolean canUpdate;
    private User currentUser;
    private Menu menu;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    //Settings Values
    Boolean settingLocation,settingPush,settingLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Setting theme from user preferences
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        switch (sharedPref.getInt("theme",0)){
            case R.id.radioFrog :
                setTheme(R.style.FrogTheme);
                break;
            case R.id.radioGreen :
                setTheme(R.style.GreenTheme);
                break;
            case R.id.radioPolar :
                setTheme(R.style.PolarTheme);
                break;
            case R.id.radioRed :
                setTheme(R.style.RedTheme);
                break;
            case R.id.radioPurple :
                setTheme(R.style.PurpleTheme);
                break;
            default:
                setTheme(R.style.GreenTheme);
                break;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        updateSettings();
        new UpdatePetTasks().execute(getString(R.string.server_base_address)+"/pets.json");

        //Facebook helper
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        canUpdate = new AtomicBoolean(true);
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                ft.replace(R.id.container, MainFragment.newInstance(), "MAIN").commit();
                break;
            case 1:
                LoginFragment frags = LoginFragment.newInstance();
                ft.replace(R.id.container, frags);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 2:
                searchFragment = PetListFragment.newInstance();
                ft.replace(R.id.container, searchFragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 3:
                createdPet = CreatePetFragment.newInstance(false);
                ft.replace(R.id.container, createdPet);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 4:
                createdPet = CreatePetFragment.newInstance(true);
                ft.replace(R.id.container, createdPet);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 5:
                Fragment settingsFragment = SettingsFragment.newInstance();
                ft.replace(R.id.container, settingsFragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 6:
                Fragment aboutFragment = AboutFragment.newInstance();
                ft.replace(R.id.container, aboutFragment);
                ft.addToBackStack(null);
                ft.commit();
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

            menu = m;
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();

            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            PetListFragment fragment = PetListFragment.newInstance();
            searchFragment = fragment;
            ft.replace(R.id.container, fragment);
            ft.addToBackStack(null);
            ft.commit();
            return true;
        } else if (item.getItemId() == R.id.action_filter) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            FilterFragment fragment = FilterFragment.newInstance(0);
            filterFragment = fragment;
            ft.replace(R.id.container, fragment);
            ft.addToBackStack(null);
            ft.commit();

        }
        return super.onOptionsItemSelected(item);
    }

    public void setCreatedPet(CreatePetFragment fragment) {
        createdPet = fragment;
        getMenuInflater().inflate(R.menu.create, menu);
    }

    //Fragment Callback methods

    /**CreatePetFragment callback. Currently just sets the fragment to null so we can tell that subsequent callbacks are from the
     * filter fragment rather than the create pet fragment
     * @param id The id of the created pet
     */
    @Override
    public void onPetCreated(int id) {
        createdPet = null;
    }

    /**AttributeList fragment callback.
     *
     * @param field The field that the item is from IE Species or Breed
     * @param item The item selected
     */
    @Override
    public void onItemChosenListener(CreatePetFragment.Field field, Object item) {
        if (createdPet != null)
            createdPet.updateField(field, item);
        else
            filterFragment.updateField(field, item);
    }

    /**The colorListFragment callback after a user chooses a series of colors
     *
     * @param field - Always color but is here in case this is used for anything else
     * @param colors - The colors chosen in an arraylist
     */
    @Override
    public void colorsChosen(CreatePetFragment.Field field, ArrayList<com.bienprogramming.pound.app.POJO.Color> colors) {
        if (createdPet != null)
            createdPet.updateField(field, colors);
        else
            filterFragment.updateField(field, colors);
        getFragmentManager().popBackStack();
    }

    /**ContactChosen fragment callback.
     *
     * @param type - The type of the contact detail - 0 - phone, 1- address, 2- email
     * @param contactDetail - The string of the detail.
     */
    @Override
    public void onContactChosen(int type, String contactDetail) {
        createdPet.updateField(CreatePetFragment.Field.FIELD_CONTACT_DETAIL, contactDetail, type);
    }

    /**Search Fragment callback.
     *
     * @param id - The id of the pet selected
     */
    @Override
    public void OnPetClicked(int id) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DisplayPetFragment fragment = DisplayPetFragment.newInstance(id);
        ft.replace(R.id.container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**FilterFragment callback once the filters are selected
     *
     * @param filter - The filter object representing what the filters that have been chosen
     */
    @Override
    public void onFiltersChosen(Filter filter) {
        searchFragment.filterResults(filter);

    }

    /**A has many contact items callback. Ie if I choose to add an attribute that allows many items
     *
     * @param field - THe field on the pet
     * @param items - The items in an array list
     */
    @Override
    public void onItemsChosen(CreatePetFragment.Field field, ArrayList<String> items) {

    }

    //Facebook session information
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {}

    /** The Callback for when the facebook sessions changes.
     * If they login(session opened) They I log them into the backend and recieve a token
     *else I remove the user data
     */
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {

            if(state.isOpened())
                new LogInUserToBackend().execute(session.getAccessToken());
            else if (state.isClosed())
                currentUser = null;

        }
    };

    /**Called from the settings fragment and oncreate to ensure that settings are up to date.
     *
     */
    public void updateSettings()
    {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        settingLocation = sharedPref.getBoolean("location",true);
        settingPush = sharedPref.getBoolean("push",true);
        settingLocal = sharedPref.getBoolean("local",true);
    }

    /**Async task to retrieve the pets from a url and place them in a database.
     *
     */
    public class UpdatePetTasks extends AsyncTask<String, Integer, Void> {
        int TIMEOUT_MILLISEC = 10000;

        @Override
        protected Void doInBackground(String... urls) {

            try {
                String result = InternetHelper.fetchData(urls[0], TIMEOUT_MILLISEC);
                Gson responseGSon = new GsonBuilder().create();

                Pet[] pets = responseGSon.fromJson(result, Pet[].class);

                Dao<Pet, Integer> petDao = OpenHelperManager.getHelper(getBaseContext(), DBHelper.class).getPetDao();
                Dao<PetLocation, Integer> petlocationDao = OpenHelperManager.getHelper(getBaseContext(), DBHelper.class).getPetLocationDao();
                Dao<Color, Integer> colorDao = OpenHelperManager.getHelper(getApplicationContext(), DBHelper.class).getColorDao();
                Dao<PetColor, Integer> petColorDao = OpenHelperManager.getHelper(getApplicationContext(), DBHelper.class).getPetColorDao();
                Dao<Breed, Integer> breedDao = OpenHelperManager.getHelper(getApplicationContext(), DBHelper.class).getBreedDao();
                Dao<Species, Integer> speciesDao = OpenHelperManager.getHelper(getApplicationContext(), DBHelper.class).getSpeciesDao();


                for (Pet pet : pets) {
                    //If temp image get temp image
                    if (petDao.queryForId(pet.getId()) == null) {
                        petDao.createOrUpdate(pet);
                    }
                    if (pet.getPetLocation() != null) {
                        pet.getPetLocation().setPetId(pet.getId());
                        petlocationDao.createOrUpdate(pet.getPetLocation());
                    }

                    for (Color color : pet.getColours()) {
                        colorDao.createOrUpdate(color);

                        if (petColorDao.queryBuilder().where().eq("petId", pet.getId()).and().eq("colorId", color.getId()).query().size()  == 0) {
                            PetColor petColor = new PetColor(pet, color);
                            petColorDao.createOrUpdate(petColor);
                        }
                    }

                    if (pet.getSpecies() != null) {
                        speciesDao.createOrUpdate(pet.getSpecies());
                    }

                    if (pet.getBreed() != null) {
                        breedDao.createOrUpdate(pet.getBreed());
                    }

                }

            } catch (Exception e) {
                Log.d("JSONRESULT", e.toString());
            }
            return null;
        }

        /**Uses an atomic boolean as a semaphore to determine if its safe to recreate the fragment
         *
         * @param result - No important
         */
        @Override
        protected void onPostExecute(Void result) {
            if(canUpdate.get()) {
                getFragmentManager().beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit();
                //canUpdate.set(false);
            } else {
                canUpdate.set(true);
            }

        }
    }

    /**Async task to send a facebook token to the backend and recieve a user based on the facebook user id of the token.
     *
     */
    public class LogInUserToBackend extends AsyncTask<String, Integer, String> {
        int TIMEOUT_MILLISEC = 10000;

        @Override
        protected String doInBackground(String... accessToken) {
            try {
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpClient client = new DefaultHttpClient(httpParams);

                HttpGet request = new HttpGet(getString(R.string.server_base_address) + "/facebook_logins/check_mobile_login?token=" + accessToken[0]);
                request.setHeader("Accept", "application/json");
                request.setHeader("Content-type", "application/json");
                HttpResponse response = client.execute(request);

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                return sb.toString();
            } catch (Exception e){
                Log.d("Log in to backend error", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            currentUser = new GsonBuilder().create().fromJson(result,User.class);

            Toast.makeText(getApplicationContext(), "Welcome "+currentUser.getName(),Toast.LENGTH_SHORT).show();
        }
    }


    //Life cycle methods
    @Override
    public void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    //Getters for fragments to get information from the main activity

    public User getCurrentUser(){
        return currentUser;
    }

    public Boolean getSettingLocation() { return settingLocation; }

    public Boolean getSettingPush() { return settingPush; }

    public Boolean getSettingLocal() { return settingLocal; }

    public void refreshMain() {
        getFragmentManager().beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit();
    }
}
