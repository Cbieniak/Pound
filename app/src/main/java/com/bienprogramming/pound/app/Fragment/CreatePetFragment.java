package com.bienprogramming.pound.app.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
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
import com.bienprogramming.pound.app.POJO.Breed;
import com.bienprogramming.pound.app.POJO.Color;
import com.bienprogramming.pound.app.POJO.DBHelper;
import com.bienprogramming.pound.app.POJO.Pet;
import com.bienprogramming.pound.app.POJO.PetColor;
import com.bienprogramming.pound.app.POJO.PetLocation;
import com.bienprogramming.pound.app.POJO.Species;
import com.bienprogramming.pound.app.POJO.User;
import com.bienprogramming.pound.app.R;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**Fragment for created pet listings.
 *
 */
public class CreatePetFragment extends Fragment {
    //Callback ints
    private static final int SELECT_PHOTO = 100;
    private static final int SELECT_LOCATION = 200;
    //Fields for where the data corresponds to
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
    private User user;
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
        if(pet == null)pet = new Pet();
        if (getArguments() != null) {
            lost = getArguments().getBoolean("Lost");
            pet.setLost(lost);
            if (((MainActivity)getActivity()).getCurrentUser() != null) {
                user = ((MainActivity) getActivity()).getCurrentUser();
                pet.setCreator(user.getId());
                pet.setContactName(user.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_create, container, false);
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
                AttributeListFragment fragment = AttributeListFragment.newInstance(Field.FIELD_SPECIES, "/species.json", true);
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        breedEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AttributeListFragment fragment = AttributeListFragment.newInstance(Field.FIELD_BREED,"/breeds.json?q[species_id_eq]="+pet.getSpeciesId(),true);
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
                locationActivityIntent.putExtra("location",((MainActivity)getActivity()).getSettingLocation());
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshUI();

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
                pet.setContactName(contactNameEditText.getText().toString());
               ArrayList<Integer> colors = new ArrayList<Integer>();
                for(Color color : pet.getColours()){
                    colors.add(color.getId());
                }
                pet.setColorIds(colors);

                //Upload to server
                new UploadPetTask().execute(pet);


            } catch (Exception e) {
                Log.d("Create", "Failed to create Pet with Name" + e.getLocalizedMessage());
            }
        }
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnPetCreatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PetCreatedListener");
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
    //Update data depending on the field
    public void updateField(Field field,Object attribute){
        switch (field) {
            case FIELD_SPECIES:
                pet.setSpecies((Species)attribute);
                break;
            case FIELD_BREED:
                pet.setBreed((Breed)attribute);
                break;

        }

    }

    /**Color field updater
     *
     * @param field - Color always
     * @param items - The colors in an array
     */
    public void updateField(Field field,ArrayList<Color> items){
        pet.setColours(items);
    }

    /**Contact detail updater
     *
     * @param field - Not needed yet
     * @param item - The string on the detail
     * @param type - The type of detail
     */
    public void updateField(Field field,String item, int type){
        pet.setContactDetail(item);
        pet.setContactType(type);
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
                    locationEditText.post(new Runnable() {

                        @Override
                        public void run() {
                            locationEditText.setText("Processing");
                        }
                    });

                    new ProcessLocationTask().execute(latitude,longitude);

                }

        }
    }

    /**Processes the image once its been chosen to avoid locking up the ui
     * Adds dots to a text view to indicate progress
     * Gets the image, extracts the bitmap and makes a thumbnail from the image
     */
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

                publishProgress(4);

                publishProgress(5);

                InputStream iStream =   getActivity().getContentResolver().openInputStream(selectedImage);
                byte[] inputData = getBytes(iStream);
                pet.setImageBlob(inputData);

                return petImage;
            } catch (Exception e){
                Log.d("Failure to process image", e.getLocalizedMessage());
            }
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

            refreshUI();
        }


    }

    /**Processes the location the user has chosen to avoid locking up the ui
     * Takes the lat and long, gets the locality and creates the petlocation object.
     */
    private class ProcessLocationTask extends AsyncTask<Double, Integer , PetLocation>
    {

        @Override
        protected PetLocation doInBackground(Double... locations) {

            Double latitude = locations[0];
            Double longitude = locations[1];
            List<Address> addresses = null;
            try {
                Geocoder geocoder = new Geocoder(getView().getContext(), Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                Address address = addresses.get(0);
                PetLocation petLocation = new PetLocation(address.getLocality(), latitude, longitude);
                return petLocation;

            } catch (Exception e) {
                Log.d("Failed to process location", e.getLocalizedMessage());
            }
            return null;
        }
        protected void onPostExecute(PetLocation result) {
            if(result != null)
                pet.setPetLocation(result);

            refreshUI();

        }
    }

    /** Uploads the pet to the server as well as saving it in the database.
     * Uploads it once. gets the id, then uploads the image using that id
     *
     */
    private class UploadPetTask extends AsyncTask<Pet, Integer , String>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Synchronizing, please wait...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
            }
        }

        @Override
        protected String doInBackground(Pet... pets) {
            int TIMEOUT_MILLISEC = 10000;  // = 10 seconds

            Gson gSon = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            String json = gSon.toJson(pets[0]);
            try {
                //Post the pet
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpClient client = new DefaultHttpClient(httpParams);

                HttpPost request = new HttpPost(getString(R.string.server_base_address)+"/pets.json");
                request.setEntity(new ByteArrayEntity(
                        json.getBytes("UTF8")));
                request.setHeader("Accept", "application/json");
                request.setHeader("Content-type", "application/json");
                HttpResponse response = client.execute(request);
                String responseBody = EntityUtils.toString(response.getEntity());

                Gson responseGSon = new GsonBuilder().create();

                pet.setId(Integer.valueOf(responseBody));

                //Post the image
                if(pet.getImageBlob()!=null) {
                    String encodedImage = Base64.encodeToString(pet.getImageBlob(), Base64.DEFAULT);
                    JSONObject jsonObject = new JSONObject();
                    JSONObject classObject = new JSONObject();
                    jsonObject.put("pet_id", pet.getId());
                    jsonObject.put("image", encodedImage);
                    classObject.put("pet_image",jsonObject);
                    HttpPost imageRequest = new HttpPost(getString(R.string.server_base_address) + "/pet_images.json");
                    imageRequest.setEntity(new ByteArrayEntity(
                            classObject.toString().getBytes("UTF8")));
                    imageRequest.setHeader("Accept", "application/json");
                    imageRequest.setHeader("Content-type", "application/json");
                    response = client.execute(imageRequest);
                    responseBody = EntityUtils.toString(response.getEntity());
                    Pet pet2 = responseGSon.fromJson(responseBody,Pet.class);
                    pet.setImageUrl(pet2.getImageUrl());

                }

                Dao<PetLocation, Integer> petlocationDao = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getPetLocationDao();
                Dao<Color, Integer> colorDao =  OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getColorDao();
                Dao<PetColor, Integer> petColorDao =  OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getPetColorDao();
                Dao<Pet, Integer> petDao = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getPetDao();

                petDao.create(pet);



                pet.getPetLocation().setPetId(pet.getId());

                petlocationDao.create(pet.getPetLocation());
                ArrayList<Integer> colors = new ArrayList<Integer>();
                for(Color color : pet.getColours()){
                    if(color.getId() == null) {
                        colorDao.create(color);
                    }
                    colors.add(color.getId());

                    PetColor petColor = new PetColor(pet,color);
                    petColorDao.create(petColor);
                }
                pet.setColorIds(colors);
                return response.toString();

            }catch (Exception e){}



            return "";
        }
        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            getFragmentManager().popBackStack();
        }
    }

    /**Refreshes the UI based on the pets attributes.
     * Runnables because they're the only reliable ways to update edit text's text.
     */
    public void refreshUI()
    {
        if(isVisible()) {
            if (pet.getSpecies() != null) {
                speciesEditText = ((EditText) getView().findViewById(R.id.create_pet_species));
                speciesEditText.post(new Runnable() {

                    @Override
                    public void run() {
                        speciesEditText.setText(pet.getSpecies().toString());
                    }
                });
            }
            if (pet.getBreed() != null) {
                breedEditText = ((EditText) getView().findViewById(R.id.create_pet_breed));
                breedEditText.post(new Runnable() {

                    @Override
                    public void run() {

                        breedEditText.setText(pet.getBreed().toString());
                    }
                });
            }
            if (pet.getPetLocation() != null) {
                locationEditText = ((EditText) getView().findViewById(R.id.create_pet_location));
                locationEditText.post(new Runnable() {

                    @Override
                    public void run() {
                        locationEditText.setText(pet.getPetLocation().toString());
                    }
                });
            }

            if (pet.getColours() != null) {
                TextView colorText = (TextView) getView().findViewById(R.id.colour_text);
                colorText.setVisibility(View.GONE);
                for (Color color : pet.getColours()) {

                    LinearLayout col = new LinearLayout(getView().getContext());
                    LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    ilp.weight = 1;
                    col.setLayoutParams(ilp);
                    col.setBackgroundColor(android.graphics.Color.parseColor(color.getValue()));
                    colorHolder.addView(col);
                }
            }
            if (pet.getContactDetail() != null) {
                contactDetailEditText = ((EditText) getView().findViewById(R.id.create_pet_contact_detail));
                contactDetailEditText.post(new Runnable() {

                    @Override
                    public void run() {
                        contactDetailEditText.setText(pet.getContactDetail());
                    }
                });
            }
            if (pet.getImageBlob() != null) {
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
