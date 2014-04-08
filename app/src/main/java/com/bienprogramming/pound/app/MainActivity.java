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

import com.bienprogramming.pound.app.POJO.Pet;

import java.util.ArrayList;

import static com.bienprogramming.pound.app.R.id.foundPetsScrollView;
import static com.bienprogramming.pound.app.R.id.missingPetsScrollView;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

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
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
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
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            lostPets = (HorizontalScrollView) rootView.findViewById(missingPetsScrollView);
            foundPets = (HorizontalScrollView) rootView.findViewById(foundPetsScrollView);
            Pet pet = new Pet("name", "Species", "Breed");
            Pet pet1 = new Pet("name", "Species", "Breed");
            Pet pet2 = new Pet("name", "Species", "Breed");
            Pet pet3 = new Pet("name", "Species", "Breed");
            Pet pet4 = new Pet("name", "Species", "Breed");
            ArrayList<Pet> pets = new ArrayList<Pet>();
            pets.add(pet);
            pets.add(pet1);
            pets.add(pet2);
            pets.add(pet3);
            pets.add(pet4);

            fillPets(lostPets, pets);
            fillPets(foundPets, pets);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        public void fillPets(HorizontalScrollView scrollView, ArrayList<Pet> pets) {
            LinearLayout mainLayout = new LinearLayout(this.getActivity().getApplicationContext());
            LinearLayout.LayoutParams mllp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mainLayout.setLayoutParams(mllp);

            for (Pet pet : pets) {
                //create layout
                LinearLayout layout = new LinearLayout(this.getActivity().getApplicationContext());
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(llp);
                layout.setPadding(20, 20, 20, 20);

                ImageView imageView = new ImageView(this.getActivity().getApplicationContext());
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

                //Set onclickListener
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Fragment fragment = DisplayPetFragment.newInstance(pet);
                        ft.replace(R.layout.fragment_main, fragment);
                        ft.commit();

                    }
                });

                mainLayout.addView(layout);
            }

            scrollView.addView(mainLayout);

        }


    }

    public static class DisplayPetFragment extends Fragment {

        public static DisplayPetFragment newInstance(Pet pet) {
            DisplayPetFragment fragment = new DisplayPetFragment();
            Bundle args = new Bundle();
            args.putSerializable("PET",pet);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_display_pet, container, false);

            Pet pet = (Pet) getArguments().getSerializable("PET");
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
            String colors="";
            for(String color : pet.getPetColours()){
                colors = colors + color + " ";
            }
            colorView.setText(colors);
            locationView.setText(pet.getLocation().getSuburb());
            noteView.setText(pet.getNotes());
            rewardView.setText("$"+pet.getReward());
            ownerView.setText("Contact Owner");




            return rootView;

        }
    }

}
