package com.example.taksi;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class FirebaseDatabaseHelper {

    private FirebaseDatabase mDatabase;
    private List<YellowTaxi> Tip1List = new ArrayList<>();
    private List<YellowTaxi> Tip2List = new ArrayList<>();
    private List<YellowTaxi> Tip3List = new ArrayList<>();
    private List<TaxiLookup> taxiLookupList = new ArrayList<>();

    public FirebaseDatabaseHelper() {


    }


    public interface DataStatus {
        void DataIsLoaded(List<YellowTaxi> tipList, List<String> keys);

    }

    public interface DataStatus2 {
        void DataIsLoaded(List<TaxiLookup> tipList, List<String> keys);

    }



    public void taxiLookupVEL(final DataStatus2 dataStatus2, Query queryTip1) {
        queryTip1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taxiLookupList.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    TaxiLookup taxiLookup = keyNode.getValue(TaxiLookup.class);
                    taxiLookupList.add(taxiLookup);

                }
                dataStatus2.DataIsLoaded(taxiLookupList, keys);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void tip1VEL(final DataStatus dataStatus, Query queryTip1) {
        queryTip1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tip1List.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    YellowTaxi yellowTaxi = keyNode.getValue(YellowTaxi.class);
                    Tip1List.add(yellowTaxi);

                }
                Collections.reverse(Tip1List);
                dataStatus.DataIsLoaded(Tip1List, keys);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void tip2VEL(final DataStatus dataStatus, Query queryTip2) {
        queryTip2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tip2List.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    YellowTaxi yellowTaxi = keyNode.getValue(YellowTaxi.class);
                    Tip2List.add(yellowTaxi);

                }
                Collections.sort(Tip2List, new Comparator<YellowTaxi>() {
                    @Override
                    public int compare(YellowTaxi o1, YellowTaxi o2) {
                       return o1.getTrip_distance() < o2.getTrip_distance() ? -1 : (o1.getTrip_distance() > o2.getTrip_distance() ? 1 : 0);
                    }
                });

                Iterator<YellowTaxi> i = Tip2List.iterator();

                while(i.hasNext()) {
                    double tripDistance = i.next().getTrip_distance();
                    if(tripDistance == 0) {
                        i.remove();
                    }
                }
                dataStatus.DataIsLoaded(Tip2List.subList(0,5), keys);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void tip3VEL(final DataStatus dataStatus, Query queryTip3) {
        queryTip3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tip3List.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    YellowTaxi yellowTaxi = keyNode.getValue(YellowTaxi.class);
                    Tip3List.add(yellowTaxi);

                }
                Collections.sort(Tip3List, new Comparator<YellowTaxi>() {
                    @Override
                    public int compare(YellowTaxi o1, YellowTaxi o2) {
                        return o1.getTrip_distance() < o2.getTrip_distance() ? -1 : (o1.getTrip_distance() > o2.getTrip_distance() ? 1 : 0);
                    }
                });

                Iterator<YellowTaxi> i = Tip3List.iterator();

                while(i.hasNext()) {
                    YellowTaxi ii = i.next();
                    double tripDistance = ii.getTrip_distance();
                    int puLocID = ii.getPULocationID();
                    int doLocID = ii.getDOLocationID();
                    if(tripDistance == 0 || puLocID == doLocID || puLocID == 264 || puLocID == 265 || doLocID == 264 || doLocID == 265) {
                        i.remove();
                    }
                }
                dataStatus.DataIsLoaded(Tip3List, keys);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
