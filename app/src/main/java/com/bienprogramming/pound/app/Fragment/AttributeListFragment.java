package com.bienprogramming.pound.app.Fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.bienprogramming.pound.app.POJO.Breed;
import com.bienprogramming.pound.app.POJO.DBHelper;
import com.bienprogramming.pound.app.POJO.Pet;
import com.bienprogramming.pound.app.POJO.Species;
import com.bienprogramming.pound.app.R;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class AttributeListFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FIELD = "field";
    private static final String PATH = "path";
    private static final String HASONE = "hasOne";

    // TODO: Rename and change types of parameters
    private CreatePetFragment.Field field;
    private String path;
    private Boolean hasOne;
    List<?> items;
    private OnItemChosenListener mItemListener;
    private OnItemsChosenListener mItemsListener;

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
    public static AttributeListFragment newInstance(CreatePetFragment.Field field, String type, Boolean hasOne) {
        AttributeListFragment fragment = new AttributeListFragment();
        Bundle args = new Bundle();
        args.putSerializable("field_key", field);
        args.putString(PATH, type);
        args.putBoolean(HASONE, hasOne);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AttributeListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getActivity().getActionBar().setTitle((field == CreatePetFragment.Field.FIELD_SPECIES) ? "Species" : "Breed");
        items = new ArrayList<Object>();
        if (getArguments() != null) {
            field = (CreatePetFragment.Field)getArguments().getSerializable("field_key");
            path = getArguments().getString(PATH);
            hasOne = getArguments().getBoolean(HASONE);
        }
         new GetItemsTask().execute("http://192.168.1.12:3000/"+path);
        getActivity().getActionBar().setTitle((field == CreatePetFragment.Field.FIELD_SPECIES) ? "Species" : "Breed");
        try {
            switch (field) {
                case FIELD_SPECIES:
                    Dao<Species, Integer> speciesDao = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getSpeciesDao();
                    items = speciesDao.queryForAll();
                    mAdapter = new ArrayAdapter<Species>(getActivity(),
                            android.R.layout.simple_list_item_1, android.R.id.text1,(List<Species>)items);
                    mListView.setAdapter(mAdapter);

                    break;
                case FIELD_BREED:
                    Dao<Breed, Integer> breedDao = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getBreedDao();
                    items = breedDao.queryForEq("speciesId",1);
                    mAdapter = new ArrayAdapter<Breed>(getActivity(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, (List<Breed>)items);
                    mListView.setAdapter(mAdapter);
                    mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    break;
            }
        }catch (Exception e){}


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attributelist, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mItemListener = (OnItemChosenListener) activity;
            mItemsListener = (OnItemsChosenListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mItemListener = null;
        mItemsListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mItemListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mItemListener.onItemChosenListener(field, mAdapter.getItem(position));
            getFragmentManager().popBackStack();

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
    public interface OnItemChosenListener {
        // TODO: Update argument type and name
        public void onItemChosenListener(CreatePetFragment.Field field,Object Item);
    }

    public interface OnItemsChosenListener {
        // TODO: Update argument type and name
        public void onItemsChosen(CreatePetFragment.Field field,ArrayList<String> items);
    }

    private class GetItemsTask extends AsyncTask<String, Integer , String>
    {
        int TIMEOUT_MILLISEC = 10000;
        @Override
        protected String doInBackground(String... urls) {
            try{
            String result;

            InputStream is = null;
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpClient client = new DefaultHttpClient(httpParams);

            HttpGet request = new HttpGet(urls[0]);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            HttpResponse response = client.execute(request);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
               sb.append(line + "\n");
            }
            result = sb.toString();
            Log.d("JSONRESULT", result);


                Gson gson = new Gson();
                switch (field){
                    case FIELD_SPECIES:
                        Species[] speciesArray = gson.fromJson(result,Species[].class);
                        mAdapter = new ArrayAdapter<Species>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, speciesArray);

                        break;
                    case FIELD_BREED:
                        Breed[] breedArray = gson.fromJson(result,Breed[].class);

                        mAdapter = new ArrayAdapter<Breed>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, breedArray);

                        break;
                }
            }catch (Exception e){
                Log.d("JSONRESULT", e.toString());
            }
                return null;
        }

        @Override
        protected void onPostExecute(String result) {

            mListView.setAdapter(mAdapter);
        }


    }

}
