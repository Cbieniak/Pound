package com.bienprogramming.pound.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
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

import com.bienprogramming.pound.app.POJO.Color;
import com.bienprogramming.pound.app.POJO.ContactDetail;
import com.bienprogramming.pound.app.POJO.DBHelper;
import com.bienprogramming.pound.app.POJO.Pet;
import com.bienprogramming.pound.app.POJO.PetColor;
import com.bienprogramming.pound.app.POJO.PetLocation;
import com.bienprogramming.pound.app.Activity.PetLocationActivity;
import com.bienprogramming.pound.app.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreatePetFragment.OnPetCreatedListener} interface
 * to handle interaction events.
 * Use the {@link CreatePetFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */


public class CreatePetFragment extends Fragment {
    private static final int SELECT_PHOTO = 100;
    private static final int SELECT_LOCATION = 200;

    public enum Field {
        FIELD_PET_NAME,
        FIELD_SPECIES,
        FIELD_BREED,
        FIELD_COLOR,
        FIELD_LOCATION,
        FIELD_REWARD,
        FIELD_NOTES,
        FIELD_OWNER_NAME,
        FIELD_CONTACT_DETAIL
    }

    private LinearLayout createPetLayout;
    private EditText nameEditText;
    private RelativeLayout petImageLayout;
    private TextView speciesEditText;
    private EditText breedEditText;
    private LinearLayout colorHolder;
    private EditText locationEditText;
    private EditText rewardEditText;
    private EditText notesEditText;
    private EditText contactNameEditText;
    private TextView contactDetailEditText;

    private boolean lost;
    private Pet pet;
    private OnPetCreatedListener mListener;


    public static CreatePetFragment newInstance(Boolean lost) {
        CreatePetFragment  fragment = new CreatePetFragment();
        Bundle args = new Bundle();
        args.putBoolean("Lost", lost);
        fragment.setArguments(args);
        return fragment;
    }
    public CreatePetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            lost = getArguments().getBoolean("Lost");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_create, container, false);
        if(pet == null)pet= new Pet();
        getActivity().getActionBar().setTitle((lost) ? "Lost Pet" : "Found Pet");
        createPetLayout = (LinearLayout) rootView.findViewById(R.id.create_pet_layout);
        petImageLayout = (RelativeLayout) rootView.findViewById(R.id.create_pet_add_image);
        nameEditText = (EditText) rootView.findViewById(R.id.create_pet_name);
        speciesEditText = (TextView) rootView.findViewById(R.id.create_pet_species);
        breedEditText = (EditText) rootView.findViewById(R.id.create_pet_breed);
        colorHolder = (LinearLayout) rootView.findViewById(R.id.create_color_holder);
        locationEditText = (EditText) rootView.findViewById(R.id.create_pet_location);
        rewardEditText = (EditText) rootView.findViewById(R.id.create_pet_reward);
        notesEditText = (EditText) rootView.findViewById(R.id.create_pet_notes);
        contactNameEditText = (EditText) rootView.findViewById(R.id.create_pet_owner_name);
        contactDetailEditText = (TextView) rootView.findViewById(R.id.create_pet_contact_detail);
        //OnClickListeners

        petImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take image or image picker
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);


            }
        });

        speciesEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AttributeListFragment fragment = AttributeListFragment.newInstance(Field.FIELD_SPECIES, "species", true);
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        breedEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AttributeListFragment fragment = AttributeListFragment.newInstance(Field.FIELD_BREED,"breed?species="+speciesEditText.getText(),true);
                ft.replace(R.id.container,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        colorHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ColorListFragment fragment = ColorListFragment.newInstance();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        locationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationActivityIntent = new Intent(view.getContext(),PetLocationActivity.class);
                startActivityForResult(locationActivityIntent, SELECT_LOCATION);
            }
        });

        contactDetailEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ContactDetailFragment fragment = ContactDetailFragment.newInstance();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.create,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Save
        if(item.getItemId() == R.id.action_save_create) {
            try {
                pet.setName(nameEditText.getText().toString());
                pet.setReward(Double.parseDouble(rewardEditText.getText().toString()));
                pet.setNotes(notesEditText.getText().toString());
                Dao<PetLocation, Integer> petlocationDao = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getPetLocationDao();
                Dao<Color, Integer> colorDao =  OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getColorDao();
                Dao<PetColor, Integer> petColorDao =  OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getPetColorDao();
                Dao<Pet, Integer> petDao = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getPetDao();

                petDao.create(pet);


                pet.getPetLocation().setPetId(pet.getId());

                petlocationDao.create(pet.getPetLocation());

                for(Color color : pet.getColours()){
                    if(color.getId() == null) {
                        colorDao.create(color);
                    }

                    PetColor petColor = new PetColor(pet,color);
                    petColorDao.create(petColor);
                }

                getFragmentManager().popBackStack();
            } catch (Exception e) {
                Log.d("Create", "Failed to create Pet with Name" + e.getLocalizedMessage());
            }
        }
        return true;
    }


    public void onSavePressed(int id) {
        if (mListener != null) {
            mListener.onPetCreated(pet.getId());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnPetCreatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PetCreated");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPetCreatedListener {
        public void onPetCreated(int id);
    }

    public void updateField(Field field,String attribute){
        switch (field) {
            case FIELD_SPECIES:
                pet.setSpecies(attribute);
                break;
            case FIELD_BREED:
                pet.setBreed(attribute);
                break;

        }

    }

    public void updateField(Field field,ArrayList<Color> items){
        pet.setColours(items);
    }

    public void updateField(Field field,String item, int type){
        pet.setContactDetail(item);
        pet.setContactType(type);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
           refreshUI();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == getActivity().RESULT_OK){
                    try {


                        Uri selectedImage = returnedIntent.getData();

                        new ProcessFilesTask().execute(selectedImage);


                    }catch (Exception e){
                        Log.d("Failure","Error picking Image");
                    }
                }
                break;
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
                        pet.setPetLocation(petLocation);
                        refreshUI();
                    } catch (Exception e) {

                    }
                }

        }
    }

    public void refreshUI()
    {

        if(pet.getSpecies()!=null) {
            speciesEditText = ((EditText) getView().findViewById(R.id.create_pet_species));
            speciesEditText.post(new Runnable() {

                @Override
                public void run() {
                    speciesEditText.setText(pet.getSpecies());
                }
            });
        }
        if(pet.getBreed()!=null) {
                breedEditText = ((EditText) getView().findViewById(R.id.create_pet_breed));
                breedEditText.post(new Runnable() {

                    @Override
                    public void run() {
                        breedEditText.setText(pet.getBreed());
                    }
                });
            }
        if(pet.getPetLocation()!=null) {
            locationEditText = ((EditText) getView().findViewById(R.id.create_pet_location));
            locationEditText.post(new Runnable() {

                @Override
                public void run() {
                    locationEditText.setText(pet.getPetLocation().toString());
                }
            });
        }

        if(pet.getColours() !=null){
            TextView colorText =(TextView) getView().findViewById(R.id.colour_text);
            colorText.setVisibility(View.GONE);
            for(Color color: pet.getColours()){

                LinearLayout col = new LinearLayout(getView().getContext());
                LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                ilp.weight=1;
                col.setLayoutParams(ilp);
                col.setBackgroundColor(color.getColorValue());
                colorHolder.addView(col);
            }
        }
        if(pet.getContactDetail() !=null)
        {
            contactDetailEditText = ((EditText) getView().findViewById(R.id.create_pet_contact_detail));
            contactDetailEditText.post(new Runnable() {

                @Override
                public void run() {
                    contactDetailEditText.setText(pet.getContactDetail());
                }
            });
        }
        if(pet.getImageBlob() !=null) {
            petImageLayout = ((RelativeLayout) getView().findViewById(R.id.create_pet_add_image));

            petImageLayout.post(new Runnable() {

                @Override
                public void run() {
                    Bitmap bmp = BitmapFactory.decodeByteArray(pet.getImageBlob(), 0, pet.getImageBlob().length);
                    ImageView petImage = new ImageView(getView().getContext());
                    petImage.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp, petImageLayout.getWidth(), petImageLayout.getHeight()));
                    petImageLayout.addView(petImage);
                }
            });
        }

        }

    private class ProcessFilesTask extends AsyncTask<Uri, Integer , ImageView> {
        protected ImageView doInBackground(Uri... uris) {


            try {
                Uri selectedImage = uris[0];
                InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                publishProgress(1);

                Bitmap chosenImage = BitmapFactory.decodeStream(imageStream);
                //Upload
                ImageView petImage = new ImageView(getView().getContext());
                petImage.setImageBitmap(ThumbnailUtils.extractThumbnail(chosenImage, petImageLayout.getWidth(), petImageLayout.getHeight()));
                publishProgress(2);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                publishProgress(3);
                //chosenImage.compress(Bitmap.CompressFormat.PNG, 100, stream);

                publishProgress(4);
                //byte[] byteArray = stream.toByteArray();
                publishProgress(5);

                InputStream iStream =   getActivity().getContentResolver().openInputStream(selectedImage);
                byte[] inputData = getBytes(iStream);
                pet.setImageBlob(inputData);

                return petImage;
            } catch (Exception e){}
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            TextView plusTextView = (TextView) getView().findViewById(R.id.create_pet_image_progress);
            String sb="";
            for(int i = 0; i < progress[0];i++) {
                sb = sb + ".";
            }
            plusTextView.setText(sb);
        }

        protected void onPostExecute(ImageView result) {
            petImageLayout.addView(result);
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


}
