package br.com.jpedrocm.openweatherapp;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import br.com.jpedrocm.openweatherapp.models.CityModel;
import br.com.jpedrocm.openweatherapp.utils.Utils;

import static org.junit.Assert.*;

public class LocalUnitTests {

    private final double EPS = 0.00001;

    @Test
    public void cityModel_InitializationUnitTest() throws Exception {
        int weatherId = 2;
        int humidity = 50;
        double currentTemp = 30.5;
        double maxTemp = 32.1;
        double minTemp = 26.7;
        double windSpeed = 3.1;
        String name = "Recife";
        String weatherDescription = "Hot";
        String cityId = "5581";

        CityModel city = new CityModel(weatherId, humidity, currentTemp, maxTemp, minTemp,
                windSpeed, name, weatherDescription, cityId);

        assertNotNull(city);
        assertEquals(weatherId, city.getWeatherId());
        assertEquals(humidity, city.getHumidity());
        assertEquals(currentTemp, city.getCurrentTemp(), EPS);
        assertEquals(maxTemp, city.getMaxTemp(), EPS);
        assertEquals(minTemp, city.getMinTemp(), EPS);
        assertEquals(windSpeed, city.getWindSpeed(), EPS);
        assertEquals(name, city.getName());
        assertEquals(weatherDescription, city.getWeatherDescription());
        assertEquals(cityId, city.getCityId());
    }

    @Test
    public void temperatureStrings_FormatsUnitTest() throws Exception {
        double kelvin = 300;
        String kelvinString = "300.0 K";
        String celsiusString = "26.9 °C";
        String fahrenheitString = "80.3 °F";

        assertEquals(kelvinString, Utils.getTempString(kelvin, Utils.TEMP_UNIT.Kelvin));
        assertEquals(celsiusString, Utils.getTempString(kelvin, Utils.TEMP_UNIT.Celsius));
        assertEquals(fahrenheitString, Utils.getTempString(kelvin, Utils.TEMP_UNIT.Fahrenheit));

        double kelvinMax = 300;
        double kelvinMin = 273.15;
        String extremeKelvinString = "Min:  273.2 K  |  Max:  300.0 K";
        String extremeCelsiusString = "Min:  0.0 °C  |  Max:  26.9 °C";
        String extremeFahrenheitString = "Min:  32.0 °F  |  Max:  80.3 °F";

        assertEquals(extremeKelvinString, Utils.getExtremeTempsString(kelvinMax, kelvinMin, Utils.TEMP_UNIT.Kelvin));
        assertEquals(extremeCelsiusString, Utils.getExtremeTempsString(kelvinMax, kelvinMin, Utils.TEMP_UNIT.Celsius));
        assertEquals(extremeFahrenheitString, Utils.getExtremeTempsString(kelvinMax, kelvinMin, Utils.TEMP_UNIT.Fahrenheit));
    }

    @Test
    public void windSpeedStrings_FormatsUnitTest() throws Exception {
        double ms = 3.1;
        String msString = "Wind:  3.1 m/s";
        String kmhString = "Wind:  11.2 km/h";
        String mphString = "Wind:  6.9 mph";

        assertEquals(msString, Utils.getWindString(ms, Utils.WIND_UNIT.MS));
        assertEquals(kmhString, Utils.getWindString(ms, Utils.WIND_UNIT.KMH));
        assertEquals(mphString, Utils.getWindString(ms, Utils.WIND_UNIT.MPH));
    }

    @Test
    public void otherStrings_FormatsUnitTest() throws Exception {
        int humidity = 1;
        String humidityString = "Humidity:  1 %";

        double latitude = 0.27895;
        double longitude = -2.1212;
        LatLng coords = new LatLng(latitude, longitude);
        String coordsString = "(0.28, -2.12)";

        assertEquals(humidityString, Utils.getHumidityString(humidity));
        assertEquals(coordsString, Utils.getCoordinatesString(coords));
    }

    @Test
    public void imagesIds_TranslationUnitTest() throws Exception {
        int idHurricane = 962;
        int idHeavyRain = 520;
        int idRain = 503;
        int idSnow = 511;
        int idMist = 701;
        int idWindy = 959;
        int idClear = 800;
        int idFewClouds = 801;
        int idScatteredClouds = 802;
        int idBrokenClouds = 804;
        int idThunderstorm = 231;

        assertEquals(R.drawable.ic_hurricane, Utils.getWeatherImageId(idHurricane));
        assertEquals(R.drawable.ic_shower_rain, Utils.getWeatherImageId(idHeavyRain));
        assertEquals(R.drawable.ic_rain, Utils.getWeatherImageId(idRain));
        assertEquals(R.drawable.ic_snow, Utils.getWeatherImageId(idSnow));
        assertEquals(R.drawable.ic_mist, Utils.getWeatherImageId(idMist));
        assertEquals(R.drawable.ic_windy, Utils.getWeatherImageId(idWindy));
        assertEquals(R.drawable.ic_clear, Utils.getWeatherImageId(idClear));
        assertEquals(R.drawable.ic_few_clouds, Utils.getWeatherImageId(idFewClouds));
        assertEquals(R.drawable.ic_scattered_clouds, Utils.getWeatherImageId(idScatteredClouds));
        assertEquals(R.drawable.ic_broken_clouds, Utils.getWeatherImageId(idBrokenClouds));
        assertEquals(R.drawable.ic_thunderstorm, Utils.getWeatherImageId(idThunderstorm));
    }
}