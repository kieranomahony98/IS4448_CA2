package com.example.myapplication.userInterations.heroes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.api.HeroApiHandler;
import com.example.myapplication.interfaces.CreateHero;
import com.example.myapplication.model.heroModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

public class create_hero extends Fragment {

    TextInputEditText name, realName;
    Spinner spRating, spTeamAffiliation;
    Button btnCreateHero;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Gson gson = new Gson();
        heroModel editHeroModel = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            editHeroModel = gson.fromJson(getArguments().getString("hero"), heroModel.class);
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_hero, container, false);
        name = v.findViewById(R.id.etName);
        realName = v.findViewById(R.id.etRealName);
        spRating = v.findViewById(R.id.spRating);
        spTeamAffiliation = v.findViewById(R.id.spTeamAffiliation);

        btnCreateHero = v.findViewById(R.id.btnCreateHero);

        if (editHeroModel != null) {
            populateResources(editHeroModel);
        }

        final heroModel finalEditHeroModel = editHeroModel;
        btnCreateHero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String heroName = name.getText().toString() != "" ? name.getText().toString() : null;
                String heroRealName = realName.getText().toString() != "" ? realName.getText().toString() : null;
                int rating = Integer.parseInt(spRating.getSelectedItem().toString());
                String teamAffillation = spTeamAffiliation.getSelectedItem().toString();
                if (heroName == null || heroRealName == null) {
                    Toast.makeText(getContext(), "Please fill out all the form completely.", Toast.LENGTH_SHORT).show();
                    return;
                }

                heroModel h = new heroModel(-1, heroName, heroRealName, rating, teamAffillation);
                HeroApiHandler heroApiHandler = new HeroApiHandler();

                if (finalEditHeroModel != null) {

                    UpdateHeroValidation updateHeroValidation = checkIfValuesUpdated(heroName, heroRealName, rating, teamAffillation, finalEditHeroModel);
                    Boolean isChanged = updateHeroValidation.isChanged();
                    if (!isChanged) {
                        Toast.makeText(getContext(), "Please update at least one value", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    heroApiHandler.updateHero(new CreateHero() {
                        @Override
                        public void onSuccess(heroModel hero) {
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fgMain, new view_heroes());
                            fragmentTransaction.commit();
                            return;
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(getContext(), "Failed to update hero, please try again later", Toast.LENGTH_SHORT).show();
                            return;

                        }
                    }, getContext(), updateHeroValidation.h());
                } else {
                    heroApiHandler.createHero(new CreateHero() {
                        @Override
                        public void onSuccess(heroModel hero) {
                            name.setText("");
                            realName.setText("");
                            spRating.setSelection(0);
                            spTeamAffiliation.setSelection(0);
                            Toast.makeText(getContext(), hero.getName() + " has successfully being added to heroes", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(getContext(), "Failed to create hero, please try again later.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }, h, getContext());
                }
            }
        });
        return v;
    }

    private UpdateHeroValidation checkIfValuesUpdated(String heroName, String heroRealName, int rating, String teamAffillation, final heroModel h) {
        boolean isUpdated = false;
        if (heroName != h.getName()) {
            h.setName(heroName);
            isUpdated = true;
        }
        if (heroRealName != h.getRealName()) {
            h.setRealName(heroRealName);
            isUpdated = true;
        }
        if (rating != h.getRating()) {
            h.setRating(rating);
            isUpdated = true;
        }
        final boolean finalIsUpdated = isUpdated;
        return new UpdateHeroValidation() {
            @Override
            public boolean isChanged() {
                return finalIsUpdated;
            }

            @Override
            public heroModel h() {
                return h;
            }
        };
    }

    private void populateResources(heroModel h) {
        name.setText(h.getName());
        realName.setText(h.getRealName());
        spRating.setSelection(h.getRating() - 1);
        btnCreateHero.setText("Update Hero");

    }


}

interface UpdateHeroValidation {
    boolean isChanged();
    heroModel h();
}