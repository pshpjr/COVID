package com.pshpjr.covid.data;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.pshpjr.covid.MainActivity;
import com.pshpjr.covid.R;

import java.text.NumberFormat;
import java.util.Objects;

import static com.pshpjr.covid.activities.DistrictwiseDataActivity.DISTRICT_ACTIVE;
import static com.pshpjr.covid.activities.DistrictwiseDataActivity.DISTRICT_CONFIRMED;
import static com.pshpjr.covid.activities.DistrictwiseDataActivity.DISTRICT_DECEASED;
import static com.pshpjr.covid.activities.DistrictwiseDataActivity.DISTRICT_NAME;
import static com.pshpjr.covid.activities.DistrictwiseDataActivity.DISTRICT_NEW_CONFIRMED;
import static com.pshpjr.covid.activities.DistrictwiseDataActivity.DISTRICT_NEW_DECEASED;
import static com.pshpjr.covid.activities.DistrictwiseDataActivity.DISTRICT_NEW_RECOVERED;
import static com.pshpjr.covid.activities.DistrictwiseDataActivity.DISTRICT_RECOVERED;

public class PerDistrictData extends AppCompatActivity {

    TextView perDistrictConfirmed, perDistrictActive, perDistrictDeceased, perDistrictNewConfirmed, perDistrictNewRecovered, perDistrictNewDeceased, perDistrictUpdate, perDistrictRecovered, perDistrictName;

    String districtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_district_data);

        Intent intent = getIntent();
        districtName = intent.getStringExtra(DISTRICT_NAME);
        String districtConfirmed = intent.getStringExtra(DISTRICT_CONFIRMED);
        String districtActive = intent.getStringExtra(DISTRICT_ACTIVE);
        String districtDeceased = intent.getStringExtra(DISTRICT_DECEASED);
        String districtNewConfirmed = intent.getStringExtra(DISTRICT_NEW_CONFIRMED);
        String districtNewRecovered = intent.getStringExtra(DISTRICT_NEW_RECOVERED);
        String districtNewDeceased = intent.getStringExtra(DISTRICT_NEW_DECEASED);
        String districtRecovery = intent.getStringExtra(DISTRICT_RECOVERED);

        Objects.requireNonNull(getSupportActionBar()).setTitle(districtName);
        perDistrictConfirmed = findViewById(R.id.district_confirmed_textview);
        perDistrictActive = findViewById(R.id.district_active_textView);
        perDistrictRecovered = findViewById(R.id.district_recovered_textView);
        perDistrictDeceased = findViewById(R.id.district_death_textView);
        perDistrictNewConfirmed = findViewById(R.id.district_confirmed_new_textView);
        perDistrictNewRecovered = findViewById(R.id.district_recovered_new_textView);
        perDistrictNewDeceased = findViewById(R.id.district_death_new_textView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        String activeCopy = districtActive;
        String recoveredCopy = districtRecovery;
        String deceasedCopy = districtDeceased;

        int districtActiveInt = Integer.parseInt(districtActive);
        districtActive = NumberFormat.getInstance().format(districtActiveInt);

        int districtDeceasedInt = Integer.parseInt(districtDeceased);
        districtDeceased = NumberFormat.getInstance().format(districtDeceasedInt);

        int districtRecoveredInt = Integer.parseInt(districtRecovery);
        districtRecovery = NumberFormat.getInstance().format(districtRecoveredInt);

        int confirmedInt = Integer.parseInt(districtConfirmed);
        districtConfirmed = NumberFormat.getInstance().format(confirmedInt);

        //assert districtActive != null;



        MainActivity object = new MainActivity();
        perDistrictConfirmed.setText(districtConfirmed);
        perDistrictActive.setText(districtActive);
        perDistrictDeceased.setText(districtDeceased);
        perDistrictNewConfirmed.setText("+" + districtNewConfirmed);
        perDistrictNewRecovered.setText(districtNewRecovered);
        perDistrictNewDeceased.setText(districtNewDeceased);
        perDistrictRecovered.setText(districtRecovery);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}