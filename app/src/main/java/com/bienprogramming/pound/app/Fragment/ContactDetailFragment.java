package com.bienprogramming.pound.app.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bienprogramming.pound.app.POJO.ContactDetail;
import com.bienprogramming.pound.app.POJO.DBHelper;
import com.bienprogramming.pound.app.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.util.List;


/** A fragment that allows users to select or enter contact details that other users will be able to contact them on.
 *
 */
public class ContactDetailFragment extends Fragment {
    //The 3 layouts depending on type
    LinearLayout phoneLayout;
    LinearLayout emailLayout;
    LinearLayout addressLayout;
    private OnContactChosenListener mListener;

    public static ContactDetailFragment newInstance() {
        ContactDetailFragment fragment = new ContactDetailFragment();
        return fragment;
    }

    public ContactDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contact_detail, container, false);
        //Layouts
        phoneLayout = (LinearLayout) rootView.findViewById(R.id.contact_phone_layout);
        emailLayout = (LinearLayout) rootView.findViewById(R.id.contact_email_layout);
        addressLayout = (LinearLayout) rootView.findViewById(R.id.contact_address_layout);

        //EditTexts
        final EditText phoneEditText = (EditText) rootView.findViewById(R.id.contact_new_phone_number_text);
        final EditText emailEditText = (EditText) rootView.findViewById(R.id.contact_new_email_text);
        final EditText addressEditText = (EditText) rootView.findViewById(R.id.contact_new_address_text);

        //Plus buttons
        TextView phoneAdd = (TextView) rootView.findViewById(R.id.contact_add_phone_number);
        TextView emailAdd = (TextView) rootView.findViewById(R.id.contact_add_email);
        TextView addressAdd = (TextView) rootView.findViewById(R.id.contact_add_address);
        Dao<ContactDetail, Integer> contactDetailDao = null;

        try {
            contactDetailDao = OpenHelperManager.getHelper(rootView.getContext(), DBHelper.class).getContactDetailDao();
            List<ContactDetail> contacts = contactDetailDao.queryForAll();
            for (final ContactDetail contact : contacts) {
                LinearLayout textContainer = new LinearLayout(rootView.getContext());
                LinearLayout.LayoutParams tclp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                textContainer.setOrientation(LinearLayout.HORIZONTAL);
                textContainer.setPadding(10,10,0,0);

                textContainer.setLayoutParams(tclp);

                TextView contactTextView = new TextView(rootView.getContext());
                contactTextView.setTextSize(20);
                contactTextView.setText(contact.getDetail());
                LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                tlp.weight=1;
                contactTextView.setLayoutParams(tlp);
                contactTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onMethodChosen(contact.getType(), contact.getDetail());
                    }
                });

                TextView actionTextView = new TextView(rootView.getContext());
                actionTextView.setTextSize(30);
                actionTextView.setText("-");
                actionTextView.setTextColor(getResources().getColor(R.color.delete));
                LinearLayout.LayoutParams atlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                atlp.weight=10;
                actionTextView.setLayoutParams(atlp);
                actionTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Dao<ContactDetail, Integer> contactDetailDao = OpenHelperManager.getHelper(view.getContext(), DBHelper.class).getContactDetailDao();
                            contactDetailDao.delete(contact);
                        }catch (Exception e){
                            Log.d("Failure to delete", e.getLocalizedMessage());
                        }
                    }
                });

                textContainer.addView(contactTextView);
                textContainer.addView(actionTextView);

                switch (contact.getType()) {
                    case 0:
                        phoneLayout.addView(textContainer);
                        break;
                    case 1:
                        emailLayout.addView(textContainer);
                        break;
                    case 2:
                        addressLayout.addView(textContainer);
                        break;
                }
            }

        } catch (Exception e) {
            Log.d("Error", "Cannot fetch contact details");
        }

        //OnClickListeners
        phoneAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ContactDetail contactDetail = new ContactDetail(0, phoneEditText.getText().toString());
                    Dao<ContactDetail, Integer> contactDetailDao = OpenHelperManager.getHelper(view.getContext(), DBHelper.class).getContactDetailDao();
                    contactDetailDao.create(contactDetail);
                    onMethodChosen(contactDetail.getType(), contactDetail.getDetail());
                } catch (Exception e) {
                    Log.d("Phone on click Exception",e.getLocalizedMessage());
                }

            }
        });

        emailAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Dao<ContactDetail, Integer> contactDetailDao = OpenHelperManager.getHelper(view.getContext(), DBHelper.class).getContactDetailDao();
                    ContactDetail contactDetail = new ContactDetail(1, emailEditText.getText().toString());
                    contactDetailDao.create(contactDetail);
                    onMethodChosen(contactDetail.getType(), contactDetail.getDetail());
                } catch (Exception e) {
                    Log.d("Email on click Exception",e.getLocalizedMessage());
                }


            }
        });

        addressAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Dao<ContactDetail, Integer> contactDetailDao = OpenHelperManager.getHelper(view.getContext(), DBHelper.class).getContactDetailDao();
                    ContactDetail contactDetail = new ContactDetail(2, addressEditText.getText().toString());
                    contactDetailDao.create(contactDetail);
                    onMethodChosen(contactDetail.getType(), contactDetail.getDetail());
                } catch (Exception e) {
                    Log.d("Address on click Exception",e.getLocalizedMessage());
                }


            }
        });

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater)
    {
        menu.clear();
    }

    public void onMethodChosen(int type, String detail) {
        if (mListener != null) {
            mListener.onContactChosen(type, detail);
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnContactChosenListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnContactChosenListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnContactChosenListener {

        public void onContactChosen(int type, String contactDetail);
    }

}
