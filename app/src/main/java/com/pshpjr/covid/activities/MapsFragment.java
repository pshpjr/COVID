package com.pshpjr.covid.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.pshpjr.covid.MainActivity;
import com.pshpjr.covid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class MapsFragment extends Fragment {
    ArrayList<MarkerOptions> data = new ArrayList<>();
    ProgressDialog progressDialog;
    GoogleMap GMap;
    private Location myLocation = new android.location.Location("");
    public static Handler mHandler;
    boolean getLocation = false;
    int confirmation;
    Context context;
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(GoogleMap googleMap) {
            GMap = googleMap;
            Log.d("gps","gmap ready");
            if (getLocation) {
                GMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 13));
                GMap.setMyLocationEnabled(true);
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        myLocation.setLatitude(35.115229);
        myLocation.setLongitude(129.039702);
        getLocation = false;
        context = getContext();
        showProgressDialog();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }



        myLocation = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (myLocation != null) {
            getLocation = true;
            progressDialog.cancel();
            confirmation = 1;
            Log.d("gpsTest", "getlast");
            Runnable progressRunnable = new Runnable() {

                @SuppressLint("SetTextI18n")
                @Override
                public void run() {

                    getData();


                }
            };
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1000);
        }


        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback mLocationCallback = new LocationCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d("gpsTest", "getslow");
//                if (!getLocation) {
                    if (locationResult == null) {
                        return;
                    }
                    if (locationResult.getLastLocation() != null) {
                        getLocation = true;
                        myLocation = locationResult.getLastLocation();
                        if(GMap != null) {
                            GMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 13));
                            GMap.setMyLocationEnabled(true);
                        }
                        LocationServices.getFusedLocationProviderClient(context).removeLocationUpdates(this);
                        progressDialog.cancel();
                        confirmation = 1;
                        Runnable progressRunnable = new Runnable() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {


                                getData();


                            }
                        };
                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 1000);

                    }

                //}
            }
        };

        Log.d("gps","startRequset");
        LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(mLocationRequest, mLocationCallback, null);



        return inflater.inflate(R.layout.fragment_maps, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String apiUrl =
                "https://api.odcloud.kr/api/15077586/v1/centers?page=1&perPage=1000&returnType=json&serviceKey=Le2aBd97INBIfgvNSSRX1sh8fiX9UgAPtYK7sotAaug8CrkhE28LFQ9eXR4J%2B%2FBUHu03KzBy5Gro3Hdw2PRxxA%3D%3D";

        //Fetching the API from URL
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("data");

                    Log.d("api", "onResponse");
                    //Since the objects of JSON are in an Array we need to define the array from which we can fetch object

                    int size = array.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject j = array.getJSONObject(i);
                        String centerName = j.getString("centerName");
                        Float lat = Float.parseFloat(j.getString("lat"));
                        Float lng = Float.parseFloat(j.getString("lng"));

                        Location l = new Location("");
                        l.setLatitude(lat);
                        l.setLongitude(lng);

                        Float distance = myLocation.distanceTo(l);

                        if (distance <= 7000)
                            data.add(new MarkerOptions().position(new LatLng(lat, lng)).title(centerName));
                    }
                    IntiMap();


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
    }

    private void IntiMap() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        int size = data.size();
        if (size == 0)
            Toast.makeText(context, "가까운 센터가 없습니다", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < size; i++) {
            GMap.addMarker(data.get(i));
        }

    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        ((TextView) progressDialog.findViewById(R.id.loading_text)).setText("위치 확인중...");
        progressDialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {

                if (confirmation != 1) {
                    progressDialog.cancel();
                    Toast.makeText(context, "Internet slow/not available", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 8000);
    }
}

