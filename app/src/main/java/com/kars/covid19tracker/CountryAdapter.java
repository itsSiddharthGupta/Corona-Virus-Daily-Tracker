package com.kars.covid19tracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {
    private List<ModelCountryData> countryDataList;
    private LayoutInflater inflater;
    private Context context;

    public CountryAdapter(List<ModelCountryData> modelCountryData, Context context){
        this.countryDataList = modelCountryData;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vh = inflater.inflate(R.layout.adapter_country_list, parent, false);
        return new MyViewHolder(vh);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtCountry.setText(countryDataList.get(position).getCountry());
    }

    @Override
    public int getItemCount() {
        return countryDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtCountry;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCountry = itemView.findViewById(R.id.txtCountryAdapter);
            txtCountry.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                Intent intent = new Intent(context, CountryDataActivity.class);
                intent.putExtra("Data",countryDataList.get(pos));
                context.startActivity(intent);
            });
        }
    }
}
