package com.bienprogramming.pound.app.Fragment;

import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bienprogramming.pound.app.Activity.MainActivity;
import com.bienprogramming.pound.app.POJO.DBHelper;
import com.bienprogramming.pound.app.POJO.Pet;
import com.bienprogramming.pound.app.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.bienprogramming.pound.app.R.id.foundPetsScrollView;
import static com.bienprogramming.pound.app.R.id.missingPetsScrollView;


public class MainFragment extends android.app.Fragment {
    static HorizontalScrollView lostPets;
    static HorizontalScrollView foundPets;
    FillPetTask fillPetTask;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        lostPets = (HorizontalScrollView) rootView.findViewById(missingPetsScrollView);
        foundPets = (HorizontalScrollView) rootView.findViewById(foundPetsScrollView);
        Button foundPetButton = (Button) rootView.findViewById(R.id.foundAPet);
        Button lostPetButton = (Button) rootView.findViewById(R.id.lostAPet);
        foundPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                CreatePetFragment fragment = CreatePetFragment.newInstance(false);
                ((MainActivity) getActivity()).setCreatedPet(fragment);
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });

        lostPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                CreatePetFragment fragment = CreatePetFragment.newInstance(true);
                ((MainActivity) getActivity()).setCreatedPet(fragment);
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        fillPetTask = new FillPetTask();
        refreshPets();

        return rootView;
    }

    public void refreshPets() {
        ((MainActivity)getActivity()).canUpdate.set(false);
        try {
            Dao<Pet, Integer> petDao = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getPetDao();
            QueryBuilder<Pet, Integer> lostPetQueryBuilder = petDao.queryBuilder();
            lostPetQueryBuilder.orderBy("id", false);
            lostPetQueryBuilder.where().eq("lost", true);
            lostPetQueryBuilder.limit((long) 10);
            QueryBuilder<Pet, Integer> foundPetQueryBuilder = petDao.queryBuilder();
            foundPetQueryBuilder.orderBy("id", false);
            foundPetQueryBuilder.where().eq("lost", false);
            foundPetQueryBuilder.limit((long) 10);

            new FillPetTask().execute(lostPetQueryBuilder.query(), foundPetQueryBuilder.query());
        } catch (Exception e) {
            Log.d("MAD EXCEPTIONS", e.getLocalizedMessage());
        }
    }

    private class FillPetTask extends AsyncTask<List<Pet>, LinearLayout, ArrayList<LinearLayout>> {
        @Override
        protected ArrayList<LinearLayout> doInBackground(List<Pet>... pets) {
            ArrayList<LinearLayout> resultArrayList = new ArrayList<LinearLayout>();

            publishProgress(fillPets(pets[0]));
            resultArrayList.add(fillPets(pets[1]));
            return resultArrayList;
        }

        @Override
        protected void onProgressUpdate(LinearLayout... values) {
            if(lostPets != null)
            lostPets.addView(values[0]);

        }

        @Override
        protected void onPostExecute(ArrayList<LinearLayout> result) {
            if(foundPets != null)
            foundPets.addView(result.get(0));
            if (((MainActivity)getActivity()).canUpdate.get()) {
                ((MainActivity)getActivity()).refreshMain();
                Log.d("refreshing", "pets");
            } else {
                ((MainActivity)getActivity()).canUpdate.set(false);
            }



        }


    }

    public LinearLayout fillPets(List<Pet> pets) {
        LinearLayout mainLayout = new LinearLayout(this.getActivity().getApplicationContext());
        LinearLayout.LayoutParams mllp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mainLayout.setLayoutParams(mllp);
        for (Pet pet : pets) {
            final int id = pet.getId();
            //create layout
            LinearLayout layout = new LinearLayout(this.getActivity().getApplicationContext());
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(llp);
            layout.setPadding(20, 20, 20, 20);

            ImageView imageView = new ImageView(this.getActivity().getApplicationContext());
            imageView.setAdjustViewBounds(true);
            LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 6f);
            imageView.setLayoutParams(ilp);
            imageView.setPadding(5, 5, 5, 5);
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getActivity().getTheme();
            theme.resolveAttribute(R.attr.highlight_color, typedValue, true);
            int color = typedValue.data;
            imageView.setBackgroundColor(color);
            if (pet.getImageBlob() == null && pet.getImageUrl() != null) {
                //Download the image.
                InputStream in = null;
                int responseCode = -1;
                try {

                    URL url = new URL(pet.getImageUrl());
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.connect();
                    responseCode = con.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        //download
                        in = con.getInputStream();
                        pet.setImageBlob(getBytes(in));
                        in.close();
                    }
                    Dao<Pet, Integer> petDao = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getPetDao();
                    petDao.update(pet);

                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
            }
            if (pet.getImageBlob() != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(pet.getImageBlob(), 0, pet.getImageBlob().length);

                layout.addView(imageView);

                imageView.setImageBitmap(bmp);
                imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp, 450, 300));
            } else {
                imageView.setImageResource(R.drawable.paw_print);
                layout.addView(imageView);
            }
            //load image
            //set image
            //textview
            TextView petName = new TextView(this.getActivity().getApplicationContext());
            petName.setText(pet.getName());
            petName.setTextColor(typedValue.data);
            petName.setGravity(Gravity.CENTER_HORIZONTAL);


            layout.addView(petName);
            final Pet petCopy = pet;
            //Set onclickListener
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    DisplayPetFragment fragment = DisplayPetFragment.newInstance(id);

                    fragment.setPet(petCopy);
                    ft.replace(R.id.container, fragment);
                    ft.addToBackStack(null);


                    ft.commit();

                }
            });

            mainLayout.addView(layout);
        }

        return mainLayout;


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


