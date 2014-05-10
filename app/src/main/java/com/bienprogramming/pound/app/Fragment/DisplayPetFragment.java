package com.bienprogramming.pound.app.Fragment;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bienprogramming.pound.app.POJO.Color;
import com.bienprogramming.pound.app.POJO.DBHelper;
import com.bienprogramming.pound.app.POJO.Pet;
import com.bienprogramming.pound.app.POJO.PetColor;
import com.bienprogramming.pound.app.POJO.PetLocation;
import com.bienprogramming.pound.app.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class DisplayPetFragment extends android.app.Fragment {
    Pet pet;

    public static DisplayPetFragment newInstance(int id) {
        DisplayPetFragment fragment = new DisplayPetFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }
    public DisplayPetFragment() {
    }
    public void setPet(Pet pet){
        this.pet = pet;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_display_pet, container, false);
        final int petId = getArguments().getInt("id");

        rootView.setOnClickListener(null);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            float i=600;
            float baseY=0;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == motionEvent.ACTION_DOWN){
                    baseY=motionEvent.getY();

                }else
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    FrameLayout petImageLayout = (FrameLayout) view.findViewById(R.id.petImageLayout);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0 );
                    Log.d("" + baseY, "" + motionEvent.getY());
                    i = i - (baseY - motionEvent.getY());
                    param.weight = i;
                    petImageLayout.setLayoutParams(param);
                    baseY = motionEvent.getY();
                }
                return false;
            }

        });

        try {
            Dao<Pet,Integer> petDao = OpenHelperManager.getHelper(rootView.getContext(),DBHelper.class).getPetDao();;
            Dao<Color, Integer> colorDao =  OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getColorDao();
            Dao<PetColor, Integer> petColorDao =  OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getPetColorDao();
            Dao<PetLocation, Integer> petLocationDao =  OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getPetLocationDao();
            pet = petDao.queryForId(petId);
            List<PetLocation> petLocationList = petLocationDao.queryForEq("petId",petId);
            if(petLocationList.size()>0)
                    pet.setPetLocation(petLocationList.get(0));


            List<PetColor> petColorList = petColorDao.queryForEq("petId",petId);
            ArrayList<Color> colorArrayList = new ArrayList<Color>();
            for(PetColor petColor : petColorList){
                 colorArrayList.add(colorDao.queryForId(petColor.getColorId()));

            }
            pet.setColours(colorArrayList);
            getActivity().getActionBar().setTitle(pet.getName());
            ImageView petImage = (ImageView) rootView.findViewById(R.id.displayPetImage);
            TextView breedView = (TextView) rootView.findViewById(R.id.displayBreed);
            TextView speciesView = (TextView) rootView.findViewById(R.id.displaySpecies);
            LinearLayout colorView = (LinearLayout) rootView.findViewById(R.id.displayColor);

            TextView locationView = (TextView) rootView.findViewById(R.id.displayLocation);
            TextView noteView = (TextView) rootView.findViewById(R.id.displayNotes);
            TextView rewardView = (TextView) rootView.findViewById(R.id.displayReward);
            TextView ownerView = (TextView) rootView.findViewById(R.id.displayContactOwner);
            FrameLayout petImageLayout = (FrameLayout) rootView.findViewById(R.id.petImageLayout);


            breedView.setText(pet.getBreed().toString());
            speciesView.setText(pet.getSpecies().toString());
            noteView.setText(pet.getNotes());
            locationView.setText(pet.getPetLocation().getSuburb());
            rewardView.setText("$" + pet.getReward());
            ownerView.setText("Contact Owner");
            for(Color color: pet.getColours()){

                LinearLayout col = new LinearLayout(rootView.getContext());
                LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                ilp.weight=1;
                col.setLayoutParams(ilp);
                col.setBackgroundColor(android.graphics.Color.parseColor(color.getValue()));
                colorView.addView(col);
            }

            Bitmap bmp = BitmapFactory.decodeByteArray(pet.getImageBlob(), 0, pet.getImageBlob().length);
            petImage.setImageBitmap(bmp);

        } catch (Exception e){

            Log.d("TAG",e.getMessage());
        }


        return rootView;

    }

}

