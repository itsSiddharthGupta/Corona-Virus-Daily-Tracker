package com.kars.covid19tracker;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CovidData  {
    @GET("time_series_19-covid-Confirmed.csv")
    Call<String> getConfirmedCases();

    @GET("time_series_19-covid-Deaths.csv")
    Call<String> getDeaths();

    @GET("time_series_19-covid-Recovered.csv")
    Call<String> getRecoveredCases();
}
