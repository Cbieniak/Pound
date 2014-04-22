package com.bienprogramming.pound.app.Fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import java.util.List;

import static com.bienprogramming.pound.app.R.id.foundPetsScrollView;
import static com.bienprogramming.pound.app.R.id.missingPetsScrollView;



public class MainFragment extends android.app.Fragment {
    static HorizontalScrollView lostPets;
    static HorizontalScrollView foundPets;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
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
                ft.replace(R.id.container,fragment);
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
                ft.replace(R.id.container,fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        try {
            Dao<Pet, Integer> petDao = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class).getPetDao();
            List<Pet> pets = petDao.queryForAll();
            if(pets.size() == 0){
                Pet pet = new Pet("name", "Species2", "Breed2");
                Pet pet1 = new Pet("name", "Species1", "Breed1");
                Pet pet2= new Pet("name", "Species3", "Breed3");
                petDao.create(pet);
                petDao.create(pet1);
                petDao.create(pet2);
                pets.add(pet);
                pets.add(pet1);
                pets.add(pet2);
            }

            fillPets(lostPets, pets);
            fillPets(foundPets, pets);
        } catch(Exception e){

        }




        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void fillPets(HorizontalScrollView scrollView, List<Pet> pets) {
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
            //Download image set placeholder
            imageView.setImageResource(R.drawable.paw_print);
            LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 6f);
            imageView.setLayoutParams(ilp);
            imageView.setBackgroundColor(getResources().getColor(R.color.highlight));
            //load image
            //set image
            //textview
            TextView petName = new TextView(this.getActivity().getApplicationContext());
            petName.setText(pet.getName());
            petName.setGravity(Gravity.CENTER_HORIZONTAL);


            layout.addView(imageView);
            layout.addView(petName);
            final Pet petCopy = pet;
            //Set onclickListener
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    DisplayPetFragment fragment = DisplayPetFragment.newInstance(id);

                    fragment.setPet(petCopy);
                    ft.replace(R.id.container,fragment);
                    ft.addToBackStack(null);


                    ft.commit();

                }
            });

            mainLayout.addView(layout);
        }

        scrollView.addView(mainLayout);


    }
    //Download images
}


