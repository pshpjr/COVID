package com.pshpjr.covid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.pshpjr.covid.R;

import android.os.Bundle;

import java.util.Objects;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("코로나 예방접종센터");
        setContentView(R.layout.activity_map);
    }
    @Override
    protected void onResume() {
        super.onResume();

        setFragment();


    }
    private void setFragment(){
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.mapFragmentLayout);

        if(current == null){
            Fragment fragment = new MapsFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.mapFragmentLayout,fragment)
                    .commit();
        }
    }
}