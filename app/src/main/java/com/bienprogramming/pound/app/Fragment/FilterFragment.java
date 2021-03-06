package com.bienprogramming.pound.app.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bienprogramming.pound.app.Activity.PetLocationActivity;
import com.bienprogramming.pound.app.POJO.Breed;
import com.bienprogramming.pound.app.POJO.Color;
import com.bienprogramming.pound.app.POJO.Filter;
import com.bienprogramming.pound.app.POJO.PetLocation;
import com.bienprogramming.pound.app.POJO.Species;
import com.bienprogramming.pound.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/** A fragment that is used to filter the list of pets. Not fully working.
 *
 *
 */
public class FilterFragment extends Fragment {
    private static final String FILTERS = "filters";

    private int filters;

    EditText speciesText;
    EditText breedText;
    LinearLayout colorLayout;
    EditText locationText;
    EditText rewardText;
    EditText notesText;

    private Filter filter;
    private static final int SELECT_LOCATION = 200;


    private OnFiltersChosenListener mListener;

    public static FilterFragment newInstance(int filters) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putInt(FILTERS, filters);
        fragment.setArguments(args);
        return fragment;
    }
    public FilterFragment() {
        // Required empty public constructor
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.action_save_list)
        {
            filter.setRewards(rewardText.getText().toString());
            filter.setNotes(notesText.getText().toString());
            mListener.onFiltersChosen(filter);
            getFragmentManager().popBackStack();
        }


        return true;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.list_menu,menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            filters = getArguments().getInt(FILTERS);

        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(filter == null)  filter = new Filter();
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);
        speciesText = (EditText) rootView.findViewById(R.id.filter_species);
        breedText = (EditText) rootView.findViewById(R.id.filter_breed);
        colorLayout = (LinearLayout) rootView.findViewById(R.id.filter_color);
        locationText = (EditText) rootView.findViewById(R.id.filter_location);
        rewardText = (EditText) rootView.findViewById(R.id.filter_reward);
        notesText = (EditText) rootView.findViewById(R.id.filter_notes);


        //OnClickListeners

        speciesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AttributeListFragment fragment = AttributeListFragment.newInstance(CreatePetFragment.Field.FIELD_SPECIES, "species", true);
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        breedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AttributeListFragment fragment = AttributeListFragment.newInstance(CreatePetFragment.Field.FIELD_BREED,"breed?species="+speciesText.getText(),true);
                ft.replace(R.id.container,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        colorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ColorListFragment fragment = ColorListFragment.newInstance();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationActivityIntent = new Intent(view.getContext(),PetLocationActivity.class);
                startActivityForResult(locationActivityIntent, SELECT_LOCATION);
            }
        });



        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFiltersChosenListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFiltersChosen");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);

        switch(requestCode) {

            case SELECT_LOCATION:
                if(resultCode == getActivity().RESULT_OK) {
                    Bundle intentData = returnedIntent.getExtras();
                    Double latitude = intentData.getDouble("Latitude");
                    Double longitude = intentData.getDouble("Longitude");
                    List<Address> addresses = null;
                    try {
                        Geocoder geocoder = new Geocoder(getView().getContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        Address address = addresses.get(0);
                        PetLocation petLocation = new PetLocation(address.getLocality(), latitude, longitude);
                        filter.setLocation(petLocation);
                        refreshUI();
                    } catch (Exception e) {

                    }
                }

        }
    }

    public void refreshUI()
    {

        if(filter.getSpecies()!=null) {
            speciesText = ((EditText) getView().findViewById(R.id.filter_species));
            speciesText.post(new Runnable() {

                @Override
                public void run() {
                    speciesText.setText(filter.getSpecies().toString());
                }
            });
        }
        if(filter.getBreed()!=null) {
            breedText = ((EditText) getView().findViewById(R.id.filter_breed));
            breedText.post(new Runnable() {

                @Override
                public void run() {
                    breedText.setText(filter.getBreed().toString());
                }
            });
        }
        if(filter.getLocation()!=null) {
            locationText = ((EditText) getView().findViewById(R.id.filter_location));
            locationText.post(new Runnable() {

                @Override
                public void run() {
                    locationText.setText(filter.getLocation().toString());
                }
            });
        }

        if(filter.getColours() !=null){
            TextView colorText =(TextView) getView().findViewById(R.id.filter_color_text);
            colorText.setVisibility(View.GONE);
            for(Color color: filter.getColours()){

                LinearLayout col = new LinearLayout(getView().getContext());
                LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                ilp.weight=1;
                col.setLayoutParams(ilp);
                col.setBackgroundColor(android.graphics.Color.parseColor(color.getValue()));
                colorLayout.addView(col);
            }
        }

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFiltersChosenListener {
        public void onFiltersChosen(Filter filter);
    }

    public void updateField(CreatePetFragment.Field field,Object attribute){
        switch (field) {
            case FIELD_SPECIES:
                filter.setSpecies((Species)attribute);
                break;
            case FIELD_BREED:
                filter.setBreed((Breed)attribute);
                break;

        }

    }

    public void updateField(CreatePetFragment.Field field,ArrayList<Color> items){
        filter.setColours(items);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshUI();

    }

}
