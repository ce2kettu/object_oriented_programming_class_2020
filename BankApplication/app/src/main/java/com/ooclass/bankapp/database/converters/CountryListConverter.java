package com.ooclass.bankapp.database.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ooclass.bankapp.models.Country;

import java.util.List;

public class CountryListConverter {
    @TypeConverter
    public static List<Country> restoreList(String listOfCountries) {
        return new Gson().fromJson(listOfCountries, new TypeToken<List<Country>>() {}.getType());
    }

    @TypeConverter
    public static String saveList(List<Country> countryList) {
        return new Gson().toJson(countryList);
    }
}
