package com.pshpjr.covid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.pshpjr.covid.activities.GPSManager;
import com.pshpjr.covid.activities.MapActivity;
import com.pshpjr.covid.activities.ShowImageActivity;
import com.pshpjr.covid.activities.StatewiseDataActivity;
import com.pshpjr.covid.activities.WorldDataActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient provider;
    static final int PERMISSIONS_REQUEST = 0x0000001;
    String confirmed;
    String date;
    String deaths;
    String newConfirmed;
    String newDeaths;
    String vaccination;
    String newVaccination;
    public static int confirmation = 0;
    public static boolean isRefreshed;
    private long backPressTime;
    private Toast backToast;
    String version;
    String updateVersion;
    String updateUrl;
    String updateChanges;

    TextView textView_confirmed, textView_confirmed_new, textView_death, textView_death_new, textView_tests_new, textView_vaccination, textView_vaccination_new;
    CardView vaccination_info, quarantine_rule, Rules;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);

        OnCheckPermission();
        textView_confirmed = findViewById(R.id.confirmed_textView);
        textView_confirmed_new = findViewById(R.id.confirmed_new_textView);
        textView_death = findViewById(R.id.death_textView);
        textView_death_new = findViewById(R.id.death_new_textView);
        textView_vaccination = findViewById(R.id.vaccination_textView);
        textView_vaccination_new = findViewById(R.id.vaccination_new_textView);
        vaccination_info = findViewById(R.id.vaccination_info_card);
        swipeRefreshLayout = findViewById(R.id.main_refreshLayout);
        quarantine_rule = findViewById(R.id.quarantine_rules);
        Rules = findViewById(R.id.Rules);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.Title);

        showProgressDialog();
        fetchData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshed = true;
                fetchData();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, "데이터가 갱신되었습니다", Toast.LENGTH_SHORT).show();
            }
        });




    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (backPressTime + 1000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(this, "뒤로 가려면 한번 더 눌러주세요", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressTime = System.currentTimeMillis();
    }

    private void putData() {

        textView_confirmed.setText(confirmed);


        textView_confirmed_new.setText("+" + newConfirmed);


        textView_death.setText(deaths);


        textView_death_new.setText("+" + newDeaths);

    }

    public void fetchData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl = "https://api.corona-19.kr/korea/?serviceKey=b4wSGIf8RhH5MPej3gDiJFstLrCzkqnY1";

        //Fetching the API from URL
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("api","onResponse");
                    //Since the objects of JSON are in an Array we need to define the array from which we can fetch objects


                    if (isRefreshed) {
                        //Inserting the fetched data into variables
                        confirmed = response.getString("TotalCase");
                        date = response.getString("updateTime");
                        deaths = response.getString("TotalDeath");
//                        newConfirmed = statewise.getString("deltaconfirmed");
                        newDeaths = response.getString("TodayDeath");
                        Runnable progressRunnable = new Runnable() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                progressDialog.cancel();

                                String deathsCopy = deaths;
                                String confirmedNewCopy = newConfirmed;

                                putData();


                            }
                        };
                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 0);
                    } else {
                        //Inserting the fetched data into variables
                        confirmed = response.getString("TotalCase");
                        date = response.getString("updateTime");
                        deaths = response.getString("TotalDeath");
//                        newConfirmed = statewise.getString("deltaconfirmed");
                        newDeaths = response.getString("TodayDeath");

                        if (!date.isEmpty()) {
                            Runnable progressRunnable = new Runnable() {

                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    progressDialog.cancel();

                                    String deathsCopy = deaths;
                                    String confirmedNewCopy = newConfirmed;

                                    putData();


                                }
                            };
                            Handler pdCanceller = new Handler();
                            pdCanceller.postDelayed(progressRunnable, 1000);
                            confirmation = 1;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        apiUrl = "https://api.corona-19.kr/korea/country/new/?serviceKey=b4wSGIf8RhH5MPej3gDiJFstLrCzkqnY1";

        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject korea = response.getJSONObject("korea");

                    if (isRefreshed) {


                        newConfirmed = korea.getString("newCase");

                        Runnable progressRunnable = new Runnable() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                progressDialog.cancel();

                                String deathsCopy = deaths;
                                String confirmedNewCopy = newConfirmed;

                                putData();


                            }
                        };
                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 0);
                    } else {
                        //Inserting the fetched data into variables

                        newConfirmed = korea.getString("newCase");

                        if (date == null) {
                            Runnable progressRunnable = new Runnable() {

                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    progressDialog.cancel();

                                    String deathsCopy = deaths;
                                    String confirmedNewCopy = newConfirmed;

                                    putData();


                                }
                            };
                            Handler pdCanceller = new Handler();
                            pdCanceller.postDelayed(progressRunnable, 1000);
                            confirmation = 1;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Date d = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.HOUR,-9);
        cal.add(Calendar.MINUTE,-40);

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDate.format(cal.getTime());

        apiUrl = "https://api.odcloud.kr/api/15077756/v1/vaccine-stat?page=1&perPage=10&cond%5BbaseDate%3A%3AGTE%5D="+today+"&serviceKey=Le2aBd97INBIfgvNSSRX1sh8fiX9UgAPtYK7sotAaug8CrkhE28LFQ9eXR4J%2B%2FBUHu03KzBy5Gro3Hdw2PRxxA%3D%3D";

        JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray data = response.getJSONArray("data");
                    JSONObject today = data.getJSONObject(0);

                        vaccination = today.getString("totalSecondCnt");
                        newVaccination = today.getString("secondCnt");

                        BigInteger vaccinationInt = new BigInteger(vaccination);

                        BigInteger newVaccinationInt = new BigInteger(newVaccination);
                        if(vaccinationInt.compareTo(new BigInteger("1000000")) == 1)
                            textView_vaccination.setTextSize(Dimension.SP,20);
                        textView_vaccination.setText(NumberFormat.getInstance().format(vaccinationInt));
                        textView_vaccination_new.setText("+" +NumberFormat.getInstance().format(newVaccinationInt));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
        requestQueue.add(jsonObjectRequest2);
        requestQueue.add(jsonObjectRequest3);
    }

    public String formatDate(String date, int testCase) {
        Date mDate = null;
        String dateFormat;
        try {
            mDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).parse(date);
            if (testCase == 0) {
                dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US).format(mDate);
                return dateFormat;
            } else if (testCase == 1) {
                dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US).format(mDate);
                return dateFormat;
            } else if (testCase == 2) {
                dateFormat = new SimpleDateFormat("hh:mm a", Locale.US).format(mDate);
                return dateFormat;
            } else {
                Log.d("error", "Wrong input! Choose from 0 to 2");
                return "Error";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }


    public void showProgressDialog() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                if (confirmation != 1) {
                    progressDialog.cancel();
                    Toast.makeText(MainActivity.this,"인터넷 상태가 좋지 않습니다" , Toast.LENGTH_SHORT).show();

                }
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 8000);
    }

    public void openStatewise(View view) {
        Intent intent = new Intent(this, StatewiseDataActivity.class);
        startActivity(intent);
    }

    public void openMoreInfo(View view) {
        Intent intent = new Intent(this, WorldDataActivity.class);
        startActivity(intent);
    }

    public void openMap(View view){
        Intent i = new Intent(MainActivity.this, MapActivity.class);
        if(((LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE)).isProviderEnabled(LocationManager.NETWORK_PROVIDER) &&
                ((LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)
        ) startActivity(i);
        else{ Toast.makeText(this,"위치 설정을 켜 주세요",Toast.LENGTH_SHORT).show();}

    }

    public void openQuarantineRule(View view){
        Intent i = new Intent(MainActivity.this, ShowImageActivity.class);
        i.putExtra("type",0);
        startActivity(i);
    }
    public void openRules(View view){
        Intent i = new Intent(MainActivity.this, ShowImageActivity.class);
        i.putExtra("type",1);
        startActivity(i);
    }


    public void OnCheckPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED

                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                Toast.makeText(this, "앱 실행을 위해서는 권한을 설정해야 합니다", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this,

                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},

                        PERMISSIONS_REQUEST);

            } else {

                ActivityCompat.requestPermissions(this,

                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},

                        PERMISSIONS_REQUEST);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length > 0

                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "앱 실행을 위한 권한이 설정 되었습니다", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "앱 실행을 위한 권한이 취소 되었습니다", Toast.LENGTH_LONG).show();

            }
        }
    }

}
