package com.bienprogramming.pound.app.Activity;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
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
import com.bienprogramming.pound.app.POJO.DBHelper;
import com.bienprogramming.pound.app.POJO.Filter;
import com.bienprogramming.pound.app.POJO.Pet;
import com.bienprogramming.pound.app.R;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

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
    public void onItemChosenListener(CreatePetFragment.Field field,String item) {
        if(createdPet != null)
            createdPet.updateField(field, item);
        else
            filterFragment.updateField(field,item);
    }

    @Override
    public void onItemsChosen(CreatePetFragment.Field field,ArrayList<String> items) {
        //createdPet.updateField(field,items);
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


}
