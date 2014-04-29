package com.bienprogramming.pound.app.Fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bienprogramming.pound.app.Activity.MainActivity;
import com.bienprogramming.pound.app.Activity.PetLocationActivity;
import com.bienprogramming.pound.app.POJO.Color;
import com.bienprogramming.pound.app.POJO.Filter;
import com.bienprogramming.pound.app.POJO.PetLocation;
import com.bienprogramming.pound.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterFragment.OnFiltersChosenListener} interface
 * to handle interaction events.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FilterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FILTERS = "filters";


    // TODO: Rename and change types of parameters
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param  filters
     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
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
                    speciesText.setText(filter.getSpecies());
                }
            });
        }
        if(filter.getBreed()!=null) {
            breedText = ((EditText) getView().findViewById(R.id.filter_breed));
            breedText.post(new Runnable() {

                @Override
                public void run() {
                    breedText.setText(filter.getBreed());
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
                col.setBackgroundColor(color.getColorValue());
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

    public void updateField(CreatePetFragment.Field field,String attribute){
        switch (field) {
            case FIELD_SPECIES:
                filter.setSpecies(attribute);
                break;
            case FIELD_BREED:
                filter.setBreed(attribute);
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
