package com.example.taksi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

public class Tip1Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    Query queryTip1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip1);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_tip1);

        // en fazla yolcu taşınan 5 günü ve toplam yolcu sayılarını listeleyiniz.
        queryTip1 = FirebaseDatabase.getInstance().getReference("yellowtaxi")
                .orderByChild("trip_distance").startAfter(null).limitToLast(5);




        new FirebaseDatabaseHelper().tip1VEL(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<YellowTaxi> tip1List, List<String> keys) {
                new RecyclerView_Config().setConfig(mRecyclerView,Tip1Activity.this,tip1List,keys);
            }
        }, queryTip1);
    }
}