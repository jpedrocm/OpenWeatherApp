package br.com.jpedrocm.openweatherapp.utils;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import br.com.jpedrocm.openweatherapp.R;
import br.com.jpedrocm.openweatherapp.models.CityModel;

public class Utils {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/find?";

    public static String getDistanceString(double distanceToMarker) {
        return String.format(Locale.ENGLISH, "%.0f m", distanceToMarker);
    }

    public enum TEMP_UNIT {Celsius, Fahrenheit, Kelvin}
    public enum WIND_UNIT {MS, KMH, MPH}

    public Utils(){}

    public static boolean isInternetOn(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static String getTempString(double kelvinTemp, TEMP_UNIT tempUnit){

        switch(tempUnit){
            case Celsius:
                double celsiusTemp = convertToCelsius(kelvinTemp);
                return String.format(Locale.ENGLISH, "%.1f °C", celsiusTemp);
            case Fahrenheit:
                double fahrenheitTemp = convertToFahrenheit(kelvinTemp);
                return String.format(Locale.ENGLISH, "%.1f °F", fahrenheitTemp);
            case Kelvin:
                return String.format(Locale.ENGLISH, "%.1f K", kelvinTemp);
            default:
                return "Unknown temperature";
        }
    }

    private static double convertToCelsius(double kelvinTemp){
        return kelvinTemp-273.15;
    }

    private static double convertToFahrenheit(double kelvinTemp){
        return (kelvinTemp*9/5.0) - 459.67;
    }

    private static double convertToKmh(double msWindSpeed){
        return msWindSpeed*3.6;
    }

    private static double convertToMph(double msWindSpeed){
        return msWindSpeed*2.2369;
    }

    public static String getCoordinatesString(LatLng latLng){
        return String.format(Locale.ENGLISH, "(%.2f, %.2f)", latLng.latitude, latLng.longitude);
    }

    public static String getHumidityString(int humidity){
        return String.format(Locale.ENGLISH, "Humidity:  %d %%", humidity);
    }

    public static String getWindString(double msWindSpeed, WIND_UNIT windUnit){

        switch(windUnit){
            case MS:
                return String.format(Locale.ENGLISH, "Wind:  %.1f m/s", msWindSpeed);
            case KMH:
                double kmhWindSpeed = convertToKmh(msWindSpeed);
                return String.format(Locale.ENGLISH, "Wind:  %.1f km/h", kmhWindSpeed);
            case MPH:
                double mphWindSpeed = convertToMph(msWindSpeed);
                return String.format(Locale.ENGLISH, "Wind:  %.1f mph", mphWindSpeed);
            default:
                return "Unknown wind speed";
        }
    }

    public static String getExtremeTempsString(double maxTemp, double minTemp, TEMP_UNIT tempUnit) {
        String maxTempString = getTempString(maxTemp, tempUnit);
        String minTempString = getTempString(minTemp, tempUnit);
        return String.format(Locale.ENGLISH, "Min:  %s  |  Max:  %s", minTempString, maxTempString);
    }

    public static int getWeatherImageId(int weatherId) {
        if(weatherId==800 || weatherId==904){
            return R.drawable.ic_clear;
        } else if(weatherId==801){
            return R.drawable.ic_few_clouds;
        } else if(weatherId==802){
            return R.drawable.ic_scattered_clouds;
        } else if(weatherId==803 || weatherId==804){
            return R.drawable.ic_broken_clouds;
        } else if(weatherId >= 200 && weatherId < 300){
            return R.drawable.ic_thunderstorm;
        } else if(weatherId == 906 ||(weatherId >= 300 && weatherId < 400) || (weatherId >= 520 && weatherId < 600)){
            return R.drawable.ic_shower_rain;
        } else if(weatherId==511 || weatherId==903 || (weatherId >= 600 && weatherId < 700)){
            return R.drawable.ic_snow;
        } else if(weatherId >= 500 && weatherId < 510){
            return R.drawable.ic_rain;
        } else if(weatherId > 700 && weatherId < 780) {
            return R.drawable.ic_mist;
        } else if(weatherId > 950 && weatherId < 960){
            return R.drawable.ic_windy;
        } else {
            return R.drawable.ic_hurricane;
        }
    }

    public static List<CityModel> parseJSONData(JSONObject json, LatLng latLng) throws JSONException {
        final String OK_RESPONSE = "200";
        final String HTTP_RESPONSE = "cod";
        final String CITIES_LIST = "list";
        final String WEATHER = "weather";
        final String MAIN = "main";
        final String WIND = "wind";
        final String ID = "id";
        final String COORD = "coord";
        final String HUMIDITY = "humidity";
        final String CUR_TEMP = "temp";
        final String MAX_TEMP = "temp_max";
        final String MIN_TEMP = "temp_min";
        final String LAT = "lat";
        final String LON = "lon";
        final String SPEED = "speed";
        final String CITY_NAME = "name";
        final String DESCRIPTION = "description";

        List<CityModel> cities = null;

        if(json.getString(HTTP_RESPONSE).equals(OK_RESPONSE)) {
            cities = new ArrayList<>();

            JSONArray citiesJSON = json.getJSONArray(CITIES_LIST);

            for (int i = 0; i < citiesJSON.length(); i++) {
                JSONObject cityJSON = citiesJSON.getJSONObject(i);

                JSONObject weatherJSON = cityJSON.getJSONArray(WEATHER).getJSONObject(0);
                JSONObject tempAndHumidityJSON = cityJSON.getJSONObject(MAIN);
                JSONObject windJSON = cityJSON.getJSONObject(WIND);

                int weatherId = weatherJSON.getInt(ID);
                int humidity = tempAndHumidityJSON.getInt(HUMIDITY);

                double curTemp = tempAndHumidityJSON.getDouble(CUR_TEMP);
                double maxTemp = tempAndHumidityJSON.getDouble(MAX_TEMP);
                double minTemp = tempAndHumidityJSON.getDouble(MIN_TEMP);
                double windSpeed = windJSON.getDouble(SPEED);

                String name = cityJSON.getString(CITY_NAME);
                String description = weatherJSON.getString(DESCRIPTION);
                String cityId = cityJSON.getString(ID);

                JSONObject coordsJSON = cityJSON.getJSONObject(COORD);

                double lat = coordsJSON.getDouble(LAT);
                double lon = coordsJSON.getDouble(LON);

                float[] results = new float[5];

                Location.distanceBetween(latLng.latitude, latLng.longitude,
                        lat, lon, results);

                double distanceToMarker = results[0];

                CityModel city = new CityModel(weatherId, humidity, curTemp, maxTemp, minTemp,
                        windSpeed, distanceToMarker, name, description, cityId);

                cities.add(city);
            }
        }

        return cities;
    }

    public static String formUrlQuery(LatLng coords, int numCities,  String api_key){
        double latitude = coords.latitude;
        double longitude = coords.longitude;

        StringBuilder urlQuery = new StringBuilder(BASE_URL);
        String query = String.format(Locale.ENGLISH, "lat=%f&lon=%f&cnt=%d&appid=%s",
                latitude, longitude, numCities, api_key);
        urlQuery.append(query);

        return urlQuery.toString();
    }

    public static ArrayList<CityModel> sortByGiven(ArrayList<CityModel> cities, Comparator<CityModel> comparator){
        ArrayList<CityModel> orderedCities = new ArrayList<>();
        orderedCities.add(cities.get(0));

        for(int j = 1; j < cities.size(); j++){
            int i;
            boolean added = false;
            for(i = 0; i < orderedCities.size(); i++){
                if(comparator.compare(cities.get(j), orderedCities.get(i)) < 0) {
                    orderedCities.add(i, cities.get(j));
                    added = true;
                    break;
                }
            }
            if(!added) orderedCities.add(i, cities.get(j));
        }

        return orderedCities;
    }
}