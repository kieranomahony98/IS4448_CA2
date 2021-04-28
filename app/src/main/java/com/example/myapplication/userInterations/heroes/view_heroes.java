package com.example.myapplication.userInterations.heroes;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.api.HeroApiHandler;
import com.example.myapplication.interfaces.DeleteHero;
import com.example.myapplication.interfaces.VolleyCallback;
import com.example.myapplication.interfaces.onBackPressed;
import com.example.myapplication.model.heroModel;
import com.google.gson.Gson;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class view_heroes extends Fragment implements onBackPressed {
    RecyclerView recyclerView;
    EditText etSearchBox;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar pbLoading;
    RecyclerAdapter rAdapter;
    ArrayList<heroModel> heroModels = null;

    ItemTouchHelper itemTouchHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_heroes, container, false);
        recyclerView = v.findViewById(R.id.rvHeroes);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        pbLoading = v.findViewById(R.id.pbLoading);
        etSearchBox = v.findViewById(R.id.etSearchBox);
        etSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterHeroes(s.toString());
            }
        });

        return v;
    }

    private void filterHeroes(String searchText) {
        ArrayList<heroModel> heroList = new ArrayList<>();
        for (heroModel h : heroModels) {
            if (h.getName().toLowerCase().contains(searchText.toLowerCase())) {
                heroList.add(h);
            }
            if (h.getRealName().toLowerCase().contains(searchText.toLowerCase())) {
                heroList.add(h);
            }
        }
        rAdapter.filterList(heroList);

    }

    @Override
    public void onResume() {
        super.onResume();
        pbLoading.setVisibility(View.VISIBLE);

        HeroApiHandler heroApiHandler = HeroApiHandler.getInstance();
        heroApiHandler.getHeros(new VolleyCallback() {
            @Override
            public void onSuccess(ArrayList<heroModel> heroes) {
                heroModels = heroes;
                pbLoading.setVisibility(View.INVISIBLE);
                createRecyclerView();
            }

            @Override
            public void onError() {
                Toast.makeText(getContext(), "Failed to get heroes, please try again later", Toast.LENGTH_SHORT).show();
            }
        }, this.getContext());
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            HeroApiHandler heroApiHandler = HeroApiHandler.getInstance();
            Log.d("direction", String.valueOf(direction));

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    int heroId = heroModels.get(position).getId();
                    heroApiHandler.deleteHero(heroId, getContext(), new DeleteHero() {
                        @Override
                        public void onSuccess() {
                            heroModels.remove(position);
                            rAdapter.notifyItemRemoved(position);
                            return;
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(getContext(), "Failed to delete hero, please try again later", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                    break;

                case ItemTouchHelper.RIGHT:
                    Gson gson = new Gson();
                    Fragment fragment = new create_hero();
                    Bundle bundle = new Bundle();
                    bundle.putString("hero", gson.toJson(heroModels.get(position)));
                    fragment.setArguments(bundle);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fgMain, fragment)
                            .addToBackStack(null)
                            .commit();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public void createRecyclerView() {
        try {
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this.getContext());
            recyclerView.setLayoutManager(layoutManager);
            rAdapter = new RecyclerAdapter(heroModels, this.getContext());
            itemTouchHelper.attachToRecyclerView(recyclerView);
            recyclerView.setAdapter(rAdapter);

        } catch (Exception e) {
            Log.e("Recycler View Fragment", "Error creating the recycler view" + e.getMessage());
            throw e;
        }
    }

    //https://medium.com/@Wingnut/onbackpressed-for-fragments-357b2bf1ce8e
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}