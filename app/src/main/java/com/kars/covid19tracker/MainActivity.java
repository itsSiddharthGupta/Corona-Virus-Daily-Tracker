package com.kars.covid19tracker;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/";
    private int count = 0;
    private HashMap<String, ModelCountryData> countryWiseMap;
    private RecyclerView rv;
    private PieChart pieChart;
    private long deaths = 0, recovered = 0, confirmed = 0;
    private ProgressDialog dialog;
    private LinearLayout ll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pieChart = findViewById(R.id.mainPieChart);
        rv = findViewById(R.id.rvCountry);
        ll = findViewById(R.id.llMainView);
        ll.setVisibility(View.GONE);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        countryWiseMap = new HashMap<>();
        fetchLatestData();
    }

    private void fetchLatestData() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Fetching the latest data");
        dialog.setCancelable(false);
        dialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        CovidData covidData = retrofit.create(CovidData.class);

        Call<String> confirmedCases = covidData.getConfirmedCases(); // id = 0;
        Call<String> deaths = covidData.getDeaths(); // id = 1;
        Call<String> recoveredCases = covidData.getRecoveredCases(); //id = 2;

        confirmedCases.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                fillFetchedData(0, response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("Fail", t.getMessage());
            }
        });
        deaths.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                fillFetchedData(1, response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("Fail", t.getMessage());
            }
        });
        recoveredCases.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                fillFetchedData(2, response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("Fail", t.getMessage());
            }
        });
    }

    private void fillFetchedData(int id, String covidData) {
        StringReader in = new StringReader(covidData);
        count++;
        try {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
            List<ModelCsvData> temp = new LinkedList<>();
            for (CSVRecord record : records) {
                String province = record.get("Province/State");
                String country = record.get("Country/Region");
                Integer count = Integer.parseInt(record.get(record.size() - 1));
                ModelCsvData cc = new ModelCsvData(province, country, count);

                if(countryWiseMap.containsKey(country)){
                    ModelCountryData old = countryWiseMap.get(country);
                    assert old!=null;
                    switch (id){
                        case 0:
                            confirmed+=count;
                            old.getConfirmedCases().add(cc);
                            break;

                        case 1:
                            deaths+=count;
                            old.getDeathCases().add(cc);
                            break;

                        case 2:
                            recovered+=count;
                            old.getRecoveredCases().add(cc);
                            break;
                    }
                }else{
                    ModelCountryData mcd = new ModelCountryData(country, new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
                    switch (id){
                        case 0:
                            confirmed+=count;
                            mcd.getConfirmedCases().add(cc);
                            break;

                        case 1:
                            deaths+=count;
                            mcd.getDeathCases().add(cc);
                            break;

                        case 2:
                            recovered+=count;
                            mcd.getRecoveredCases().add(cc);
                            break;
                    }
                    countryWiseMap.put(country, mcd);
                }
            }

            if(count==3){
                //all data is fetched
                Log.e("MAP", countryWiseMap + " : SIZE =>" + countryWiseMap.size());
                dialog.dismiss();
                ll.setVisibility(View.VISIBLE);
                showData();
            }
        } catch (IOException e) {
            Log.e("IOException", e.toString());
        }
    }

    private void showData(){
        List<ModelCountryData> countryData = new LinkedList<>();
        for(String country : countryWiseMap.keySet()){
            countryData.add(countryWiseMap.get(country));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            countryData.sort(null);
        }
        CountryAdapter adapter = new CountryAdapter(countryData, this);
        rv.setAdapter(adapter);
        buildPieChart(deaths, recovered, confirmed);
    }



    private void buildPieChart(long deaths, long recoveredCases, long confirmedCases){
        List<PieEntry> entries = new LinkedList<>();
        entries.add(new PieEntry(deaths, "Deaths"));
        entries.add(new PieEntry(confirmedCases, "Confirmed"));
        entries.add(new PieEntry(recoveredCases, "Recovered"));
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
