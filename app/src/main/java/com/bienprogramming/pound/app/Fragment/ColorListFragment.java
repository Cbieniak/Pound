package com.bienprogramming.pound.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.bienprogramming.pound.app.POJO.Color;
import com.bienprogramming.pound.app.R;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * interface.
 */
public class ColorListFragment extends ListFragment {

    private ArrayList<Color> chosenColors;
    private ArrayList<Color> colors;
    private OnColorsChosenListener mListener;

    public static ColorListFragment newInstance() {
        ColorListFragment fragment = new ColorListFragment();

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ColorListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        chosenColors = new ArrayList<Color>();

        Color newColor = new Color("White",getResources().getColor(R.color.raised));
        Color color1 = new Color("orange",getResources().getColor(R.color.highlight));
        colors = new ArrayList<Color>();
        colors.add(newColor);
        colors.add(color1);

        setListAdapter(new ArrayAdapter<Color>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, colors));



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.action_save_list)
        mListener.colorsChosen(CreatePetFragment.Field.FIELD_COLOR,chosenColors);

        return true;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.list_menu,menu);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnColorsChosenListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            chosenColors.add(colors.get(position));
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
    public interface OnColorsChosenListener {
        // TODO: Update argument type and name
        public void colorsChosen(CreatePetFragment.Field field,ArrayList<Color> colors);
    }

}
