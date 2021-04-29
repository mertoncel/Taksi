package com.example.taksi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerView_Config {
    private Context mContext;
    private TaxiAdapter mTaxiAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<YellowTaxi> yellowTaxiList, List<String> keys) {
        mContext = context;
        mTaxiAdapter = new TaxiAdapter(yellowTaxiList, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mTaxiAdapter);
    }

    class TaxiItemView extends RecyclerView.ViewHolder {
        private TextView mTripDistance;
        private TextView mTripDistanceDate;

        private String key;

        public TaxiItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext)
            .inflate(R.layout.taxilookup_list_item,parent,false));

            mTripDistance = itemView.findViewById(R.id.txt_tripDistance);
            mTripDistanceDate = itemView.findViewById(R.id.txt_tripDistanceDate);


        }

        public void bind(YellowTaxi yellowTaxi, String key) {
            mTripDistance.setText(String.valueOf(yellowTaxi.getTrip_distance()));
            mTripDistanceDate.setText(yellowTaxi.getTpep_pickup_datetime());
            this.key = key;
        }
    }

    class TaxiAdapter extends RecyclerView.Adapter<TaxiItemView>{
        private List<YellowTaxi> mYellowTaxiList;
        private List<String> mKeys;

        public TaxiAdapter(List<YellowTaxi> mYellowTaxiList, List<String> mKeys) {
            this.mYellowTaxiList = mYellowTaxiList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public TaxiItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TaxiItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaxiItemView holder, int position) {

            holder.bind(mYellowTaxiList.get(position),mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mYellowTaxiList.size();
        }
    }
}
