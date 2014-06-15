package com.bienprogramming.pound.app.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bienprogramming.pound.app.Adapter.ColorAdapter;
import com.bienprogramming.pound.app.Helper.InternetHelper;
import com.bienprogramming.pound.app.POJO.Color;
import com.bienprogramming.pound.app.POJO.DBHelper;
import com.bienprogramming.pound.app.R;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;

/**
 * A fragment representing a list of Colors.
 * interface.
 *
 */
public class ColorListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private ArrayList<Color> chosenColors;
    private ArrayList<Color> colors;
    private ArrayList<View> selectedViews;
    private OnColorsChosenListener mListener;
    ColorAdapter colorAdapter;
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

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
        colors = new ArrayList<Color>();
        selectedViews = new ArrayList<View>();
        new GetColorsTask().execute(getString(R.string.server_base_address) + "/colors.json");
        try {
            Dao<Color, Integer> colorDao = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getColorDao();
            colors = (ArrayList<Color>)colorDao.queryBuilder().orderBy("name",true).query();

        }catch (Exception e){
            Log.d("Fetch colors Exception", e.getLocalizedMessage());
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attributelist, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        colorAdapter = new ColorAdapter(getActivity().getApplicationContext(), getThemedLayout(), colors);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mListView.setAdapter(colorAdapter);

        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save_list)
            mListener.colorsChosen(CreatePetFragment.Field.FIELD_COLOR, chosenColors);

        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.list_menu, menu);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnColorsChosenListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnColorsChosenListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**When an item is selected it is added to list. Unless its already been selected, then remove it.
     * //View parameters
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListView.setItemChecked(position, true);

        if (chosenColors.contains(colorAdapter.getItem(position))) {
            chosenColors.remove(colorAdapter.getItem(position));
        } else {
            chosenColors.add(colorAdapter.getItem(position));
        }

    }

    /**Listener once done is cllicked
     *
     */
    public interface OnColorsChosenListener {
        public void colorsChosen(CreatePetFragment.Field field, ArrayList<Color> colors);
    }

    /**Async task for fetching the colors and loading them into the database of colors
     *
     */
    private class GetColorsTask extends AsyncTask<String, Integer, String> {
        int TIMEOUT_MILLISEC = 10000;

        @Override
        protected String doInBackground(String... urls) {
            try {
                String result = InternetHelper.fetchData(urls[0], TIMEOUT_MILLISEC);

                Color[] colorArray = new Gson().fromJson(result, Color[].class);
                Dao<Color, Integer> colorDao = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getColorDao();
                for (Color color : colorArray) {
                    colorDao.createOrUpdate(color);
                }

                colors = (ArrayList<Color>)colorDao.queryBuilder().orderBy("name",true).query();

                colorAdapter = new ColorAdapter(getActivity().getApplicationContext(),getThemedLayout(), colors);

            } catch (Exception e) {}
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                chosenColors.clear();
                mListView.setAdapter(colorAdapter);
            } catch (Exception e) {
                Log.d("Background task", "Unable to update list");
            }

        }


    }

    /** Get the current theme to pass through to the adapter so that the list items are the correct theme.
     *
     * @return The id of the theme.
     */
    private int getThemedLayout()
    {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        switch (sharedPref.getInt("theme",0)){
            case R.id.radioFrog :
                return R.layout.color_row_frog;
            case R.id.radioGreen :
                return R.layout.color_row_green;
            case R.id.radioPolar :
                return R.layout.color_row_polar;
            case R.id.radioRed :
                return R.layout.color_row_red;
            case R.id.radioPurple :
                return R.layout.color_row_polar;
            default:
                return R.layout.color_row_red;
        }
    }

}
