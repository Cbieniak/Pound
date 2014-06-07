package com.bienprogramming.pound.app.Fragment;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bienprogramming.pound.app.Helper.InternetHelper;
import com.bienprogramming.pound.app.POJO.Breed;
import com.bienprogramming.pound.app.POJO.Color;
import com.bienprogramming.pound.app.POJO.ContactDetail;
import com.bienprogramming.pound.app.POJO.DBHelper;
import com.bienprogramming.pound.app.POJO.Pet;
import com.bienprogramming.pound.app.POJO.PetColor;
import com.bienprogramming.pound.app.POJO.PetLocation;
import com.bienprogramming.pound.app.POJO.Species;
import com.bienprogramming.pound.app.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
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

    public DisplayPetFragment() {}

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
                    i = i - (baseY - motionEvent.getY());
                    param.weight = i;
                    petImageLayout.setLayoutParams(param);
                    baseY = motionEvent.getY();
                }
                return false;
            }
        });

        try {
           pet = InternetHelper.fetchPet(rootView.getContext(),petId);
        }catch (SQLException e) {
            Toast.makeText(rootView.getContext(),"Unable to find Pet details",Toast.LENGTH_SHORT);
        }
        //setUpForPet(rootView,pet);



        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpForPet(view,pet);
        view.invalidate();
    }

    private void setUpForPet(View rootView,Pet pet)
    {


            if(pet.getName()!=null)
                getActivity().getActionBar().setTitle(pet.getName());
            ImageView petImage = (ImageView) rootView.findViewById(R.id.displayPetImage);
            TextView breedView = (TextView) rootView.findViewById(R.id.displayBreed);
            TextView speciesView = (TextView) rootView.findViewById(R.id.displaySpecies);
            LinearLayout colorView = (LinearLayout) rootView.findViewById(R.id.displayColor);

            TextView locationView = (TextView) rootView.findViewById(R.id.displayLocation);
            TextView noteView = (TextView) rootView.findViewById(R.id.displayNotes);
            TextView rewardView = (TextView) rootView.findViewById(R.id.displayReward);
            Button contactOwnerButton = (Button) rootView.findViewById(R.id.displayContactOwner);
            FrameLayout petImageLayout = (FrameLayout) rootView.findViewById(R.id.petImageLayout);


            breedView.setText(pet.getBreed().toString());
            speciesView.setText(pet.getSpecies().toString());
            noteView.setText(pet.getNotes());
            locationView.setText(pet.getPetLocation().getSuburb());
            rewardView.setText("$" + pet.getReward());

            final ContactDetail contactDetail = new ContactDetail(pet.getContactType(),pet.getContactDetail());
            contactOwnerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    switch (contactDetail.getType()) {
                        case(0):
                            //Phone
                            String uri = "tel:" + contactDetail.getDetail().trim() ;
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse(uri));
                            startActivity(intent);
                            break;
                        case (1):
                            //Email
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setType("text/html");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, contactDetail.getDetail());
                            startActivity(Intent.createChooser(emailIntent, "Send Email"));
                            break;
                        case (2):
                            //Address
                            Intent geoIntent = new Intent (android.content.Intent.ACTION_VIEW, Uri.parse ("geo:0,0?q=" + contactDetail.getDetail()));
                            startActivity(geoIntent);
                            break;
                    }
                }
            });
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
            if(pet.getImageBlob() != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(pet.getImageBlob(), 0, pet.getImageBlob().length);
                int size = (bmp.getWidth() > bmp.getHeight()) ? bmp.getHeight() : bmp.getWidth();
                petImage.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp, size, size));
            }



    }



}

