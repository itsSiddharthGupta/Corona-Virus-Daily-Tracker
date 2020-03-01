package com.kars.covid19tracker;

import java.io.Serializable;
import java.util.List;

public class ModelCountryData implements Serializable, Comparable<ModelCountryData> {
    private String country;
    private List<ModelCsvData> confirmedCases;
    private List<ModelCsvData> recoveredCases;
    private List<ModelCsvData> deathCases;

    public ModelCountryData(){
    }

    public ModelCountryData(String country, List<ModelCsvData> confirmedCases, List<ModelCsvData> recoveredCases, List<ModelCsvData> deathCases) {
        this.country = country;
        this.confirmedCases = confirmedCases;
        this.recoveredCases = recoveredCases;
        this.deathCases = deathCases;
    }

    public String getCountry() {
        return country;
    }

    public List<ModelCsvData> getConfirmedCases() {
        return confirmedCases;
    }

    public List<ModelCsvData> getRecoveredCases() {
        return recoveredCases;
    }

    public List<ModelCsvData> getDeathCases() {
        return deathCases;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setConfirmedCases(List<ModelCsvData> confirmedCases) {
        this.confirmedCases = confirmedCases;
    }

    public void setRecoveredCases(List<ModelCsvData> recoveredCases) {
        this.recoveredCases = recoveredCases;
    }

    public void setDeathCases(List<ModelCsvData> deathCases) {
        this.deathCases = deathCases;
    }

    @Override
    public String toString() {
        return "ModelCountryData{" +
                "country='" + country + '\'' +
                ", confirmedCases=" + confirmedCases +
                ", recoveredCases=" + recoveredCases +
                ", deathCases=" + deathCases +
                '}';
    }

    @Override
    public int compareTo(ModelCountryData other) {
        return this.country.compareTo(other.country);
    }
}
