package com.example.home.hajjability;

import java.util.ArrayList;
import java.util.List;

public class Language {
    public String country_code;
    public String language_code;

    Language(String country_code, String language_code) {
        this.country_code = country_code;
        this.language_code = language_code;
    }

    Language(String country_code) {
        this.country_code = country_code;
        this.language_code = getLanguageCode(country_code);
    }

    private static String getLanguageCode(String country_code) {
        if (country_code == "SA") {
            return "ar";
        } else if (country_code == "EG") {
            return "ar";
        } else if (country_code == "US") {
            return "en";
        } else if (country_code == "FR") {
            return "fr";
        } else {
            return "na";
        }
    }


    @Override
    public String toString() {
        return language_code.toLowerCase() + "-" + country_code.toUpperCase();
    }
}
