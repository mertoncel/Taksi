package com.example.taksi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.CollectionCertStoreParameters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Tip3Activity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    Query queryTip3;


    private GoogleMap mMap;
    Button btnEnKisaYolculuk;
    Button btnEnUzunYolculuk;
    private MarkerOptions puEnUzun, doEnUzun, puEnKisa, doEnKisa;
    private Polyline currentPolyline;
    YellowTaxi minValue, maxValue;

    Query queryTaxiLookup;

    TaxiLookup enYakinPuID, enYakinDoID, enUzakPuID, enUzakDoID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip3);




        queryTip3  = FirebaseDatabase.getInstance().getReference("yellowtaxi")
                .orderByChild("passenger_count")
                .startAt(3);

        queryTaxiLookup = FirebaseDatabase.getInstance().getReference("taxilookup");

        new FirebaseDatabaseHelper().tip3VEL(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<YellowTaxi> tip3List, List<String> keys) {
                 minValue = Collections.min(tip3List, new Comparator<YellowTaxi>() {
                    @Override
                    public int compare(YellowTaxi o1, YellowTaxi o2) {
                        return Double.compare(o1.getTrip_distance(),o2.getTrip_distance());
                    }
                });
                Toast.makeText(Tip3Activity.this, String.valueOf(minValue.getTrip_distance()), Toast.LENGTH_SHORT).show();

                maxValue = Collections.max(tip3List, new Comparator<YellowTaxi>() {
                    @Override
                    public int compare(YellowTaxi o1, YellowTaxi o2) {
                        return Double.compare(o1.getTrip_distance(),o2.getTrip_distance());
                    }
                });
                Toast.makeText(Tip3Activity.this, String.valueOf(maxValue.getTrip_distance()), Toast.LENGTH_SHORT).show();


                new FirebaseDatabaseHelper().taxiLookupVEL(new FirebaseDatabaseHelper.DataStatus2() {
                    @Override
                    public void DataIsLoaded(List<TaxiLookup> taxiLookupList, List<String> keys) {

                        for(TaxiLookup t : taxiLookupList) {
                            if(t.getLocationID() == maxValue.getPULocationID()) {
                                enUzakPuID = t;
                            }
                            if(t.getLocationID() == maxValue.getDOLocationID()) {
                                enUzakDoID = t;
                            }
                            if(t.getLocationID() == minValue.getPULocationID()) {
                                enYakinPuID = t;
                            }
                            if(t.getLocationID() == minValue.getDOLocationID()) {
                                enYakinDoID = t;
                            }

                        }

                        puEnUzun = new MarkerOptions().position(new LatLng(enUzakPuID.getLongitude(), enUzakPuID.getLatitude())).title(enUzakPuID.getBorough());
                        doEnUzun = new MarkerOptions().position(new LatLng(enUzakDoID.getLongitude(), enUzakDoID.getLatitude())).title(enUzakDoID.getBorough());

                        puEnKisa = new MarkerOptions().position(new LatLng(enYakinPuID.getLongitude(), enYakinPuID.getLatitude())).title(enYakinPuID.getBorough());
                        doEnKisa = new MarkerOptions().position(new LatLng(enYakinDoID.getLongitude(), enYakinDoID.getLatitude())).title(enYakinDoID.getBorough());



                        btnEnKisaYolculuk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                new FetchURL(Tip3Activity.this).execute(getUrl(puEnKisa.getPosition(), doEnKisa.getPosition(), "driving"), "driving");

                            }
                        });




                        btnEnUzunYolculuk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                new FetchURL(Tip3Activity.this).execute(getUrl(puEnUzun.getPosition(), doEnUzun.getPosition(), "driving"), "driving");

                            }
                        });



                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFrag);
                        mapFragment.getMapAsync(Tip3Activity.this);


                    }
                },queryTaxiLookup);


            }
        }, queryTip3);











        btnEnKisaYolculuk = findViewById(R.id.btnEnKisaYolculuk);
        btnEnUzunYolculuk = findViewById(R.id.btnEnUzunYolculuk);






    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        mMap.addMarker(puEnUzun);
        mMap.addMarker(doEnUzun);
        mMap.addMarker(puEnKisa);
        mMap.addMarker(doEnKisa);
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}