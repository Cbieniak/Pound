package com.bienprogramming.pound.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;


import com.bienprogramming.pound.app.R;
import com.bienprogramming.pound.app.dummy.DummyContent;

import java.util.ArrayList;


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

        if (getArguments() != null) {
            field = (CreatePetFragment.Field)getArguments().getSerializable("field_key");
            path = getArguments().getString(PATH);
            hasOne = getArguments().getBoolean(HASONE);
        }

        // TODO: Change Adapter to display your content
        mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);
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
            mItemListener.onItemChosenListener(field,DummyContent.ITEMS.get(position).content);
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
        public void onItemChosenListener(CreatePetFragment.Field field,String Item);
    }

    public interface OnItemsChosenListener {
        // TODO: Update argument type and name
        public void onItemsChosen(CreatePetFragment.Field field,ArrayList<String> items);
    }

}