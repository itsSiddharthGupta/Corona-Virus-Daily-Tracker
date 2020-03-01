package com.kars.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.LinkedList;
import java.util.List;

public class CountryDataActivity extends AppCompatActivity {
    private RecyclerView rv;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_data);
        rv = findViewById(R.id.rvData);
        pieChart = findViewById(R.id.pieChart);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        Bundle bundle = getIntent().getExtras();
        assert bundle!=null;
        ModelCountryData mcd = (ModelCountryData) bundle.get("Data");
        showData(mcd);
    }

    private void showData(ModelCountryData mcd){
        DataAdapter adapter = new DataAdapter(mcd,this);
        rv.setAdapter(adapter);
        int deaths = getTotalCount(mcd.getDeathCases());
        int recoveredCases = getTotalCount(mcd.getRecoveredCases());
        int confirmedCases = getTotalCount(mcd.getConfirmedCases());
        buildPieChart(deaths, recoveredCases, confirmedCases);
    }

    private int getTotalCount(List<ModelCsvData> cases) {
        int count = 0;
        for(ModelCsvData data : cases){
            count += data.getCount();
        }
        return count;
    }


    private void buildPieChart(int deaths, int recoveredCases, int confirmedCases){
        List<PieEntry> entries = new LinkedList<>();
        entries.add(new PieEntry(deaths, "Deaths"));
        entries.add(new PieEntry(confirmedCases, "Confirmed Cases"));
        entries.add(new PieEntry(recoveredCases, "Recovered Cases"));
        PieDataSet set = new PieDataSet(entries,"");
        set.setColors(new int[] {R.color.death, R.color.confirmed, R.color.recovered}, this);
        PieData data = new PieData(set);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(14);
        pieChart.setData(data);
        Description desc = new Description();
        desc.setText("Report COVID-19");
        desc.setTextSize(14.0f);
        pieChart.setDescription(desc);
        pieChart.animateXY(3000,3000, Easing.EaseInQuart);
        pieChart.invalidate();
        pieChart.setDrawEntryLabels(false);
    }
}
