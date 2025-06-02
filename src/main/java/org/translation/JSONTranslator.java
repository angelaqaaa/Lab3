package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final Map<String, JSONObject> countryData;
    private final List<String> countries;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        countryData = new HashMap<>();
        countries = new ArrayList<>();

        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject country = jsonArray.getJSONObject(i);
                String countryCode = country.getString("alpha3");
                countryData.put(countryCode, country);
                countries.add(countryCode);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        JSONObject countryObj = countryData.get(country);
        if (countryObj == null) {
            return new ArrayList<>();
        }

        List<String> languages = new ArrayList<>();
        for (String key : countryObj.keySet()) {
            // Skip non-language fields
            if (!"id".equals(key) && !"alpha2".equals(key) && !"alpha3".equals(key)) {
                languages.add(key);
            }
        }
        return new ArrayList<>(languages);
    }

    @Override
    public List<String> getCountries() {
        return new ArrayList<>(countries);
    }

    @Override
    public String translate(String country, String language) {
        JSONObject countryObj = countryData.get(country);
        if (countryObj == null || !countryObj.has(language)) {
            return null;
        }
        return countryObj.getString(language);
    }
}
