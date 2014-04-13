package com.bienprogramming.pound.app;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bienprogramming.pound.app.POJO.DBHelper;
import com.bienprogramming.pound.app.POJO.Pet;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;

import static com.bienprogramming.pound.app.R.id.foundPetsScrollView;
import static com.bienprogramming.pound.app.R.id.missingPetsScrollView;


public class MainActivity extends OrmLiteBaseActivity<DBHelper>
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Pet currentPet;

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
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
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
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public static class MainFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MainFragment newInstance(int sectionNumber) {
            MainFragment fragment = new MainFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public MainFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            lostPets = (HorizontalScrollView) rootView.findViewById(missingPetsScrollView);
            foundPets = (HorizontalScrollView) rootView.findViewById(foundPetsScrollView);
            try {
                Dao<Pet, Integer> petDao = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getPetDao();
                List<Pet> pets = petDao.queryForAll();
                if(pets.size() == 0){
                    Pet pet = new Pet("name", "Species2", "Breed2");
                    Pet pet1 = new Pet("name", "Species1", "Breed1");
                    Pet pet2= new Pet("name", "Species3", "Breed3");
                    petDao.create(pet);
                    petDao.create(pet1);
                    petDao.create(pet2);
                    pets.add(pet);
                    pets.add(pet1);
                    pets.add(pet2);
                }

                fillPets(lostPets, pets);
                fillPets(foundPets, pets);
            } catch(Exception e){

            }




            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        public void fillPets(HorizontalScrollView scrollView, List<Pet> pets) {
            LinearLayout mainLayout = new LinearLayout(this.getActivity().getApplicationContext());
            LinearLayout.LayoutParams mllp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mainLayout.setLayoutParams(mllp);

            for (Pet pet : pets) {
                final int id = pet.getId();
                //create layout
                LinearLayout layout = new LinearLayout(this.getActivity().getApplicationContext());
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(llp);
                layout.setPadding(20, 20, 20, 20);

                ImageView imageView = new ImageView(this.getActivity().getApplicationContext());
                //Download image set placeholder
                imageView.setImageResource(R.drawable.paw_print);
                LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 6f);
                imageView.setLayoutParams(ilp);
                imageView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
                //load image
                //set image
                //textview
                TextView petName = new TextView(this.getActivity().getApplicationContext());
                petName.setText(pet.getName());
                petName.setGravity(Gravity.CENTER_HORIZONTAL);


                layout.addView(imageView);
                layout.addView(petName);
                final Pet petCopy = pet;
                //Set onclickListener
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        DisplayPetFragment fragment = DisplayPetFragment.newInstance(id);

                        fragment.setPet(petCopy);
                        ft.replace(R.id.container,fragment);
                        ft.addToBackStack(null);


                        ft.commit();

                    }
                });

                mainLayout.addView(layout);
            }

            scrollView.addView(mainLayout);


        }
        //Download images



    }

    public static class DisplayPetFragment extends Fragment {
        Pet pet;

        public static DisplayPetFragment newInstance(int id) {
            DisplayPetFragment fragment = new DisplayPetFragment();
            Bundle args = new Bundle();
            args.putInt("id", id);
            fragment.setArguments(args);
            return fragment;
        }
        public DisplayPetFragment() {
        }
        public void setPet(Pet pet){
            this.pet = pet;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_display_pet, container, false);
            int petId = getArguments().getInt("id");



            try {
                //DBHelper a = OpenHelperManager.getHelper(getActivity().getApplicationContext(),DBHelper.class);
                //Dao<Pet,Integer> dao = a.getPetDao();
                getActivity().getActionBar().setTitle(pet.getName());
                ImageView petImage = (ImageView) rootView.findViewById(R.id.displayPetImage);
                TextView breedView = (TextView) rootView.findViewById(R.id.displayBreed);
                TextView speciesView = (TextView) rootView.findViewById(R.id.displaySpecies);
                TextView colorView = (TextView) rootView.findViewById(R.id.displayColor);
                TextView locationView = (TextView) rootView.findViewById(R.id.displayLocation);
                TextView noteView = (TextView) rootView.findViewById(R.id.displayNotes);
                TextView rewardView = (TextView) rootView.findViewById(R.id.displayReward);
                TextView ownerView = (TextView) rootView.findViewById(R.id.displayContactOwner);

                petImage.setImageResource(R.drawable.dog_sill);

                breedView.setText(pet.getBreed());
                speciesView.setText(pet.getSpecies());
                noteView.setText(pet.getNotes());
                rewardView.setText("$" + pet.getReward());
                ownerView.setText("Contact Owner");

            } catch (Exception e){

                Log.d("TAG",e.getLocalizedMessage());
            }


            return rootView;

        }

    }

}
