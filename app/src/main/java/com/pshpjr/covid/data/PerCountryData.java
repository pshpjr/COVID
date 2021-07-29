package com.pshpjr.covid.data;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.pshpjr.covid.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Objects;

import static com.pshpjr.covid.activities.CountrywiseDataActivity.COUNTRY_ACTIVE;
import static com.pshpjr.covid.activities.CountrywiseDataActivity.COUNTRY_CONFIRMED;
import static com.pshpjr.covid.activities.CountrywiseDataActivity.COUNTRY_DECEASED;
import static com.pshpjr.covid.activities.CountrywiseDataActivity.COUNTRY_NAME;
import static com.pshpjr.covid.activities.CountrywiseDataActivity.COUNTRY_NEW_CONFIRMED;
import static com.pshpjr.covid.activities.CountrywiseDataActivity.COUNTRY_NEW_DECEASED;
import static com.pshpjr.covid.activities.CountrywiseDataActivity.COUNTRY_NEW_RECOVERED;
import static com.pshpjr.covid.activities.CountrywiseDataActivity.COUNTRY_RECOVERED;
import static com.pshpjr.covid.activities.CountrywiseDataActivity.COUNTRY_TESTS;


public class PerCountryData extends AppCompatActivity {
    TextView perCountryConfirmed, perCountryActive, perCountryDeceased, perCountryNewConfirmed, perCountryTests, perCountryNewDeceased, perCountryRecovered, perCountryNewRecovered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_country_data);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        Intent intent = getIntent();
        String countryName = intent.getStringExtra(COUNTRY_NAME);
        String countryConfirmed = intent.getStringExtra(COUNTRY_CONFIRMED);
        String countryActive = intent.getStringExtra(COUNTRY_ACTIVE);
        String countryDeceased = intent.getStringExtra(COUNTRY_DECEASED);
        String countryRecovery = intent.getStringExtra(COUNTRY_RECOVERED);
        String countryNewConfirmed = intent.getStringExtra(COUNTRY_NEW_CONFIRMED);
        String countryNewDeceased = intent.getStringExtra(COUNTRY_NEW_DECEASED);
        String countryTests = intent.getStringExtra(COUNTRY_TESTS);
        String countryNewRecovered = intent.getStringExtra(COUNTRY_NEW_RECOVERED);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Objects.requireNonNull(getSupportActionBar()).setTitle(countryName);
        perCountryConfirmed = findViewById(R.id.percountry_confirmed_textview);
        perCountryActive = findViewById(R.id.percountry_active_textView);
        perCountryRecovered = findViewById(R.id.percountry_recovered_textView);
        perCountryDeceased = findViewById(R.id.percountry_death_textView);
        perCountryNewConfirmed = findViewById(R.id.percountry_confirmed_new_textView);
        perCountryTests = findViewById(R.id.percountry_tests_textView);
        perCountryNewDeceased = findViewById(R.id.percountry_death_new_textView);

        perCountryNewRecovered = findViewById(R.id.percountry_recovered_new_textView);

        String activeCopy = countryActive;
        String recoveredCopy = countryRecovery;
        String deceasedCopy = countryDeceased;

        BigInteger activeInt = new BigInteger(countryActive);
        countryActive = NumberFormat.getInstance().format(activeInt);

        BigInteger recoveredInt = new BigInteger(countryRecovery);
        countryRecovery = NumberFormat.getInstance().format(recoveredInt);

        BigInteger deceasedInt = new BigInteger(countryDeceased);
        countryDeceased = NumberFormat.getInstance().format(deceasedInt);

        BigInteger confirmedInt = new BigInteger(countryConfirmed);
        countryConfirmed = NumberFormat.getInstance().format(confirmedInt);

        BigInteger testsInt = new BigInteger(countryTests);
        countryTests = NumberFormat.getInstance().format(testsInt);


        perCountryConfirmed.setText(countryConfirmed);
        perCountryActive.setText(countryActive);
        perCountryDeceased.setText(countryDeceased);
        perCountryTests.setText(countryTests);
        perCountryNewConfirmed.setText("+" + countryNewConfirmed);
        perCountryNewDeceased.setText("+" + countryNewDeceased);
        perCountryRecovered.setText(countryRecovery);
        perCountryNewRecovered.setText("+"+countryNewRecovered);

        PieChart mPieChart = (PieChart) findViewById(R.id.piechart_percountry);
        mPieChart.addPieSlice(new PieModel(activeInt.intValue(),ContextCompat.getColor(getApplicationContext(), R.color.blue_pie)));
        mPieChart.addPieSlice(new PieModel(recoveredInt.intValue(),ContextCompat.getColor(getApplicationContext(), R.color.green_pie)));
        mPieChart.addPieSlice(new PieModel(deceasedInt.intValue(),ContextCompat.getColor(getApplicationContext(), R.color.red_pie)));
        mPieChart.startAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
