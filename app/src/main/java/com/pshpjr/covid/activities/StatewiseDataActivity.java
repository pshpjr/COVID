package com.pshpjr.covid.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pshpjr.covid.R;
import com.pshpjr.covid.adapters.CountrywiseAdapter;
import com.pshpjr.covid.adapters.StatewiseAdapter;
import com.pshpjr.covid.data.PerStateData;
import com.pshpjr.covid.models.CountrywiseModel;
import com.pshpjr.covid.models.StatewiseModel;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class StatewiseDataActivity extends AppCompatActivity implements StatewiseAdapter.OnItemClickListner {

    public static final String STATE_NAME = "stateName";
    public static final String STATE_CONFIRMED = "stateConfirmed";
    public static final String STATE_ACTIVE = "stateActive";
    public static final String STATE_DECEASED = "stateDeaceased";
    public static final String STATE_NEW_CONFIRMED = "stateNewConfirmed";
    public static final String STATE_NEW_RECOVERED = "stateNewRecovered";
    public static final String STATE_NEW_DECEASED = "stateNewDeceased";
    public static final String STATE_LAST_UPDATE = "stateLastUpdate";
    public static final String STATE_RECOVERED = "stateRecovered";

    int confirm=0,recover=0,death=0;
    private RecyclerView recyclerView;
    private StatewiseAdapter statewiseAdapter;
    private ArrayList<StatewiseModel> statewiseModelArrayList;
    private RequestQueue requestQueue;
    ProgressDialog progressDialog;
    public static int confirmation = 0;
    public static String testValue;
    public static boolean isRefreshed;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText search;
    String stateLastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("지역 정보");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_statewise_data);
        recyclerView = findViewById(R.id.statewise_recyclerview);
        swipeRefreshLayout = findViewById(R.id.statewise_refresh);
        search = findViewById(R.id.search_editText);




        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        statewiseModelArrayList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);
        showProgressDialog();
        extractData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshed = true;
                extractData();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(StatewiseDataActivity.this, "Data refreshed!", Toast.LENGTH_SHORT).show();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void filter(String text) {
        ArrayList<StatewiseModel> filteredList = new ArrayList<>();
        for (StatewiseModel item : statewiseModelArrayList) {
            if (item.getState().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        statewiseAdapter.filterList(filteredList);
    }

    private void extractData() {
        String dataURL = "https://api.corona-19.kr/korea/country/new/?serviceKey=b4wSGIf8RhH5MPej3gDiJFstLrCzkqnY1";
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, dataURL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                statewiseModelArrayList.clear();
                try {
                    if(response.getString("resultCode").equals("0")){
                        ArrayList<JSONObject> state = new ArrayList<>();
                        getState(state, response);
                        int size = state.size();


                        for(int i = 0; i<size;i++){
                            JSONObject stateObj =  state.get(i);
                            String Name = stateObj.getString("countryName");
                            String countryConfirmed = stateObj.getString("totalCase");

                            String countryPercentage= stateObj.getString("percentage");
                            String countryRecovered = stateObj.getString("recovered");

                            String countryDeceased = stateObj.getString("death");

                            String countryNewConfirmed = stateObj.getString("newCase");

                            statewiseModelArrayList.add(new StatewiseModel(Name,countryConfirmed,countryPercentage,countryDeceased,countryNewConfirmed,"","","",countryRecovered));
                        }

                        Collections.sort(statewiseModelArrayList, new Comparator<StatewiseModel>() {
                            @Override
                            public int compare(StatewiseModel o1, StatewiseModel o2) {
                                if (Integer.parseInt(o1.getConfirmed().replaceAll(",",""))>Integer.parseInt(o2.getConfirmed().replaceAll(",",""))){
                                    return -1;
                                } else {
                                    return 1;
                                }
                            }
                        });
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
        requestQueue.add(jsonArrayRequest);
        if (true) {
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    progressDialog.cancel();
                    statewiseAdapter = new StatewiseAdapter(StatewiseDataActivity.this, statewiseModelArrayList);
                    recyclerView.setAdapter(statewiseAdapter);
                    statewiseAdapter.setOnItemClickListner(StatewiseDataActivity.this);
                }
            };
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 500);
            confirmation = 1;
        }

    }

    private void getState(ArrayList<JSONObject> state,JSONObject response) throws JSONException {
        state.add(response.getJSONObject("seoul"));
        state.add(response.getJSONObject("busan"));
        state.add(response.getJSONObject("daegu"));
        state.add(response.getJSONObject("incheon"));
        state.add(response.getJSONObject("gwangju"));
        state.add(response.getJSONObject("daejeon"));
        state.add(response.getJSONObject("ulsan"));
        state.add(response.getJSONObject("sejong"));
        state.add(response.getJSONObject("gyeonggi"));
        state.add(response.getJSONObject("gangwon"));
        state.add(response.getJSONObject("chungbuk"));
        state.add(response.getJSONObject("chungnam"));
        state.add(response.getJSONObject("jeonbuk"));
        state.add(response.getJSONObject("jeonnam"));
        state.add(response.getJSONObject("gyeongbuk"));
        state.add(response.getJSONObject("gyeongnam"));
        state.add(response.getJSONObject("jeju"));
    }

    @Override
    public void onItemClick(int position) {
        Intent perStateIntent = new Intent(this, PerStateData.class);
        StatewiseModel clickedItem = statewiseModelArrayList.get(position);

        perStateIntent.putExtra(STATE_NAME, clickedItem.getState());
        perStateIntent.putExtra(STATE_CONFIRMED, clickedItem.getConfirmed());
        perStateIntent.putExtra(STATE_ACTIVE, clickedItem.getActive());
        perStateIntent.putExtra(STATE_DECEASED, clickedItem.getDeceased());
        perStateIntent.putExtra(STATE_NEW_CONFIRMED, clickedItem.getNewConfirmed());
        perStateIntent.putExtra(STATE_NEW_RECOVERED, clickedItem.getNewRecovered());
        perStateIntent.putExtra(STATE_NEW_DECEASED, clickedItem.getNewDeceased());
        perStateIntent.putExtra(STATE_LAST_UPDATE, clickedItem.getLastupdate());
        perStateIntent.putExtra(STATE_RECOVERED, clickedItem.getRecovered());


        startActivity(perStateIntent);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(StatewiseDataActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                if (confirmation != 1) {
                    progressDialog.cancel();
                    Toast.makeText(StatewiseDataActivity.this, "Internet slow/not available", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 8000);
    }
}
