package com.kars.covid19tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {
    private ModelCountryData countryData;
    private Context context;
    private LayoutInflater inflater;
    public DataAdapter(ModelCountryData countryData, Context context){
        this.countryData = countryData;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(inflater.inflate(R.layout.adapter_data, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtCountry.setText(countryData.getCountry());
        holder.txtState.setText(countryData.getConfirmedCases().get(position).getProvince());
        holder.txtConfirmed.setText(countryData.getConfirmedCases().get(position).getCount() + "");
        holder.txtDeaths.setText(countryData.getDeathCases().get(position).getCount() + "");
        holder.txtRecovered.setText(countryData.getRecoveredCases().get(position).getCount() + "");
    }

    @Override
    public int getItemCount() {
        return countryData.getRecoveredCases().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtCountry, txtState;
        TextView txtConfirmed, txtRecovered, txtDeaths;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCountry = itemView.findViewById(R.id.txtCountry);
            txtState = itemView.findViewById(R.id.txtState);
            txtConfirmed = itemView.findViewById(R.id.txtConfirmed);
            txtRecovered = itemView.findViewById(R.id.txtRecovered);
            txtDeaths = itemView.findViewById(R.id.txtDeaths);
        }
    }
}
