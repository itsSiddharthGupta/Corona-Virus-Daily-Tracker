package com.kars.covid19tracker;

import java.io.Serializable;

public class ModelCsvData implements Serializable {
    private String province;
    private String country;
    private int count;

    public ModelCsvData(String province, String country, int count) {
        this.province = province;
        this.country = country;
        this.count = count;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "ModelCsvData{" +
                "province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", count=" + count +
                '}';
    }
}
