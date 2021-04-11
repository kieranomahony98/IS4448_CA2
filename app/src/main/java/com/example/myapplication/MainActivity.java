package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.example.myapplication.userInterations.covid.line_chart;
import com.example.myapplication.userInterations.covid.view_all_covid;
import com.example.myapplication.userInterations.heroes.create_hero;
import com.example.myapplication.userInterations.heroes.view_heroes;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;


public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener  {
    Button button;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.tbNav);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.mainDrawer);
        navigationView = findViewById(R.id.nvAppNav);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        loadFragment(new view_heroes());

    }

    private void loadFragment(Fragment nextFragment){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fgMain, nextFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.btnGoToCreateHero:
                loadFragment(new create_hero());
                return true;
            case R.id.btnGoToViewHeroes:
                loadFragment(new view_heroes());
                return true;
            case R.id.goToCovidData:
                loadFragment(new view_all_covid());
                return true;
            case R.id.goToMovingAverages:
                loadFragment(new line_chart());
                return true;
        }
        return true;
    }
}