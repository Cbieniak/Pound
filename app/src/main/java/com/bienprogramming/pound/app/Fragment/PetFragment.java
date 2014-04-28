package com.bienprogramming.pound.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
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

import com.bienprogramming.pound.app.POJO.DBHelper;
import com.bienprogramming.pound.app.POJO.Pet;
import com.bienprogramming.pound.app.R;

import com.bienprogramming.pound.app.Fragment.dummy.DummyContent;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 */
public class PetFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnPetClickedListener mListener;
    private List<Pet> pets;
    private Button sortNameButton;
    private Button sortLocationButton;
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static PetFragment newInstance() {
        PetFragment fragment = new PetFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PetFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    try {
        Dao<Pet, Integer> petDao = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getPetDao();
        pets = petDao.queryForAll();
        mAdapter = new ArrayAdapter<Pet>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, pets);
    }catch (Exception e){}

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
        sortNameButton = (Button)  rootView.findViewById(R.id.list_button_name);
        sortLocationButton = (Button) rootView.findViewById(R.id.list_button_location);

        Comparator<Pet> ALPHABETICAL_ORDER = new Comparator<Pet>() {
            public int compare(Pet object1, Pet object2) {
                if(object1.getName() == null)
                    object1.setName("");
                if(object2.getName() == null)
                    object2.setName("");
                return String.CASE_INSENSITIVE_ORDER.compare(object1.toString(), object2.toString());

            }
        };
        Collections.sort(pets,ALPHABETICAL_ORDER);

        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        sortNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortLocationButton.setBackgroundColor(getResources().getColor(R.color.highlight));
                sortNameButton.setBackgroundColor(getResources().getColor(R.color.background));
                Comparator<Pet> ALPHABETICAL_ORDER = new Comparator<Pet>() {
                    public int compare(Pet object1, Pet object2) {
                        if(object1.getName() == null)
                            object1.setName("");
                        if(object2.getName() == null)
                            object2.setName("");
                        return String.CASE_INSENSITIVE_ORDER.compare(object1.toString(), object2.toString());

                    }
                };
                Collections.sort(pets,ALPHABETICAL_ORDER);

                ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

            }
        });

        sortLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortNameButton.setBackgroundColor(getResources().getColor(R.color.highlight));
                sortLocationButton.setBackgroundColor(getResources().getColor(R.color.background));
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.pet_list_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Filter
        if(item.getItemId() == R.id.action_filter) {

        }
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
    public interface OnPetClickedListener {
        // TODO: Update argument type and name
        public void OnPetClicked(int id);
    }

}
