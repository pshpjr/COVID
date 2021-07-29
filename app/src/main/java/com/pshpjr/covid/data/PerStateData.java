package com.pshpjr.covid.data;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.pshpjr.covid.MainActivity;
import com.pshpjr.covid.R;
import com.pshpjr.covid.activities.DistrictwiseDataActivity;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.Objects;

import static com.pshpjr.covid.activities.StatewiseDataActivity.STATE_ACTIVE;
import static com.pshpjr.covid.activities.StatewiseDataActivity.STATE_CONFIRMED;
import static com.pshpjr.covid.activities.StatewiseDataActivity.STATE_DECEASED;
import static com.pshpjr.covid.activities.StatewiseDataActivity.STATE_LAST_UPDATE;
import static com.pshpjr.covid.activities.StatewiseDataActivity.STATE_NAME;
import static com.pshpjr.covid.activities.StatewiseDataActivity.STATE_NEW_CONFIRMED;
import static com.pshpjr.covid.activities.StatewiseDataActivity.STATE_NEW_DECEASED;
import static com.pshpjr.covid.activities.StatewiseDataActivity.STATE_NEW_RECOVERED;
import static com.pshpjr.covid.activities.StatewiseDataActivity.STATE_RECOVERED;

public class PerStateData extends AppCompatActivity {

    TextView perStateConfirmed, perStateActive, perStateDeceased, perStateNewConfirmed, perStateNewRecovered, perStateNewDeceased, perStateUpdate, perStateRecovered;
    String stateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_state_data);

        Intent intent = getIntent();
        stateName = intent.getStringExtra(STATE_NAME);
        String stateConfirmed = intent.getStringExtra(STATE_CONFIRMED);
        String stateActive = intent.getStringExtra(STATE_ACTIVE);
        String stateDeceased = intent.getStringExtra(STATE_DECEASED);
        String stateNewConfirmed = intent.getStringExtra(STATE_NEW_CONFIRMED);
        String stateNewRecovered = intent.getStringExtra(STATE_NEW_RECOVERED);
        String stateNewDeceased = intent.getStringExtra(STATE_NEW_DECEASED);
        String stateLastUpdate = intent.getStringExtra(STATE_LAST_UPDATE);
        String stateRecovery = intent.getStringExtra(STATE_RECOVERED);

        Objects.requireNonNull(getSupportActionBar()).setTitle(stateName);
        perStateConfirmed = findViewById(R.id.perstate_confirmed_textview);
        perStateActive = findViewById(R.id.perstate_active_textView);
        perStateRecovered = findViewById(R.id.perstate_recovered_textView);
        perStateDeceased = findViewById(R.id.perstate_death_textView);
//        perStateUpdate = findViewById(R.id.perstate_lastupdate_textView);
        perStateNewConfirmed = findViewById(R.id.perstate_confirmed_new_textView);
        perStateNewRecovered = findViewById(R.id.perstate_recovered_new_textView);
        perStateNewDeceased = findViewById(R.id.perstate_death_new_textView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        String activeCopy = stateActive;
        String recoveredCopy = stateRecovery;
        String deceasedCopy = stateDeceased;



        //assert stateActive != null;


        MainActivity object = new MainActivity();
        String formatDate = object.formatDate(stateLastUpdate, 0);
        perStateConfirmed.setText(stateConfirmed);
        perStateActive.setText(stateActive);
        perStateDeceased.setText(stateDeceased);
//        perStateUpdate.setText(formatDate);
        perStateNewConfirmed.setText("+" + stateNewConfirmed);
        perStateNewRecovered.setText(stateNewRecovered);
        perStateNewDeceased.setText(stateNewDeceased);
        perStateRecovered.setText(stateRecovery);

        int c =Integer.parseInt(stateConfirmed.replaceAll(",",""));
        int r = Integer.parseInt(stateRecovery.replaceAll(",",""));
        int d = Integer.parseInt(stateDeceased.replaceAll(",",""));

        PieChart mPieChart = (PieChart) findViewById(R.id.piechart_perstate);
        mPieChart.addPieSlice(new PieModel(c-r-d,ContextCompat.getColor(getApplicationContext(), R.color.blue_pie)));
        mPieChart.addPieSlice(new PieModel(r,ContextCompat.getColor(getApplicationContext(), R.color.green_pie)));
        mPieChart.addPieSlice(new PieModel(d,ContextCompat.getColor(getApplicationContext(), R.color.red_pie)));
        mPieChart.startAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void openDistrictData(View view){
        Intent intent = new Intent(this, DistrictwiseDataActivity.class);
        intent.putExtra(STATE_NAME, stateName);
        startActivity(intent);
    }
}
