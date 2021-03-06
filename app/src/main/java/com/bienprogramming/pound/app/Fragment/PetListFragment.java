package com.bienprogramming.pound.app.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bienprogramming.pound.app.Activity.MainActivity;
import com.bienprogramming.pound.app.POJO.Color;
import com.bienprogramming.pound.app.POJO.DBHelper;
import com.bienprogramming.pound.app.POJO.Pet;
import com.bienprogramming.pound.app.POJO.PetLocation;
import com.bienprogramming.pound.app.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A fragment representing a list of Pets.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView. //Not tested on tablets
 * <p/>
 */
public class PetListFragment extends Fragment implements AbsListView.OnItemClickListener, LocationListener {

    private LocationManager locationManager;
    private Location currentLocation;
    private OnPetClickedListener mListener;
    private List<Pet> pets;
    private Button sortNameButton;
    private Button sortLocationButton;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    public static PetListFragment newInstance() {
        PetListFragment fragment = new PetListFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PetListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        try {
            Dao<Pet, Integer> petDao = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getPetDao();
            Dao<PetLocation, Integer> petLocationDao = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getPetLocationDao();

            pets = petDao.queryForAll();
            for(Pet pet:pets)
            {
                try {
                    pet.setPetLocation(petLocationDao.queryForEq("petId", pet.getId()).get(0));
                }catch (Exception e){}
            }
            mAdapter = new ArrayAdapter<Pet>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, pets);
        } catch (Exception e) {
            Log.d("Failed to retrieve pets", e.getLocalizedMessage());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pet, container, false);

        // Set the adapter
        mListView = (AbsListView) rootView.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        sortNameButton = (Button) rootView.findViewById(R.id.list_button_name);
        sortLocationButton = (Button) rootView.findViewById(R.id.list_button_location);

        //GET LOCATION
        if(((MainActivity)getActivity()).getSettingLocation()) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        } else {
            sortLocationButton.setEnabled(false);
        }

        //Get Colours
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.highlight_color, typedValue, true);
        final int highlightColor = typedValue.data;
        theme.resolveAttribute(R.attr.background_color, typedValue, true);
        final int backgroundColor = typedValue.data;
        //Order alphabetically initially - doesn't make sense who searches for pets by name.
        Comparator<Pet> ALPHABETICAL_ORDER = new Comparator<Pet>() {
            public int compare(Pet object1, Pet object2) {
                if (object1.getName() == null)
                    object1.setName("");
                if (object2.getName() == null)
                    object2.setName("");
                return String.CASE_INSENSITIVE_ORDER.compare(object1.toString(), object2.toString());

            }
        };
        Collections.sort(pets, ALPHABETICAL_ORDER);

        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        sortNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortLocationButton.setBackgroundColor(highlightColor);
                sortNameButton.setBackgroundColor(backgroundColor);
                Comparator<Pet> ALPHABETICAL_ORDER = new Comparator<Pet>() {
                    public int compare(Pet object1, Pet object2) {
                        if (object1.getName() == null)
                            object1.setName("");
                        if (object2.getName() == null)
                            object2.setName("");


                        return String.CASE_INSENSITIVE_ORDER.compare(object1.toString(), object2.toString());

                    }
                };
                Collections.sort(pets, ALPHABETICAL_ORDER);

                ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

            }
        });
        //Sorts the pets by location nearest. not enabled if the user has disabled locations
        sortLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sortNameButton.setBackgroundColor(highlightColor);
                sortLocationButton.setBackgroundColor(backgroundColor);

                Comparator<Pet> DISTANCE_ORDER = new Comparator<Pet>() {
                    public int compare(Pet pet1, Pet pet2) {
                        if (pet1.getPetLocation() == null || pet2.getPetLocation() == null) {
                            return 100000000;
                        }

                        double pet1Dist = distFrom(currentLocation.getLatitude(), currentLocation.getLongitude(), pet1.getPetLocation().getLatitude(), pet1.getPetLocation().getLongitude());
                        double pet2Dist = distFrom(currentLocation.getLatitude(), currentLocation.getLongitude(), pet2.getPetLocation().getLatitude(), pet2.getPetLocation().getLongitude());
                        return (int) (pet1Dist - pet2Dist);


                    }
                };
                Collections.sort(pets, DISTANCE_ORDER);

                ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
                ((AdapterView<ListAdapter>) mListView).invalidate();
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.pet_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Filter

        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPetClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPetClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.OnPetClicked(pets.get(position).getId());
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }
    //Location methods
    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public interface OnPetClickedListener {
        public void OnPetClicked(int id);
    }

    /**Filter results. Still not working correctyl
     *
     * @param filter
     */
    public void filterResults(com.bienprogramming.pound.app.POJO.Filter filter) {
        List<Pet> results = new ArrayList<Pet>();
        results.addAll(pets);
        for (Pet pet : pets) {
            if (!filter(filter, pet))
                results.remove(pet);
        }
        pets = null;
        pets = results;
        Log.d("PETSIZE", pets.size() + "");

        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
    }

    public Boolean filter(com.bienprogramming.pound.app.POJO.Filter filter, Pet pet) {
        if (filter.getSpecies() != null) {
            if (filter.getSpecies().equals(pet.getSpecies())) return true;
        } else if (filter.getBreed() != null) {
            if (filter.getBreed().equals(pet.getBreed())) return true;
        } else if (filter.getColours() != null) {
            for (Color filterColor : filter.getColours()) {
                for (Color petColor : pet.getColours()) {
                    if (filterColor.getName().equals(petColor.getName()))
                        return true;
                }
            }
        } else if (filter.getLocation() != null) {
            if (filter.getLocation().toString().equals(pet.getPetLocation().toString()))
                return true;
        } else if (filter.getRewards() != null) {
            if (filter.getRewards().equals(pet.getReward())) return true;
        } else if (filter.getNotes() != null) {
            for (String note : filter.getNotes().split(" ")) {
                for (String petNote : pet.getNotes().split(" ")) {
                    if (note.equals(petNote)) return true;
                }
            }


        }
        return false;
    }

    //From http://stackoverflow.com/questions/5396286/sort-list-of-lon-lat-points-start-with-nearest

    /**
     * methods to give values to sort by using distance from
     * @param fromLat - pet lat
     * @param fromLon - pet long
     * @param toLat - user lat
     * @param toLon - user long
     * @return a double representing the distance
     */
    static double distFrom(double fromLat, double fromLon, double toLat, double toLon) {
        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = toLat - fromLat;
        double deltaLon = toLon - fromLon;
        double angle = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(deltaLat / 2), 2) +
                        Math.cos(fromLat) * Math.cos(toLat) *
                                Math.pow(Math.sin(deltaLon / 2), 2)
        ));
        return radius * angle;
    }

}
