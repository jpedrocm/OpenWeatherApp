package br.com.jpedrocm.openweatherapp;

import android.content.Context;
import android.os.Parcel;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.jpedrocm.openweatherapp.models.CityModel;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class InstrumentedUnitTests {

    private final double EPS = 0.00001;

    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("br.com.jpedrocm.openweatherapp", appContext.getPackageName());
    }

    @Test
    public void CityModel_ParcelableReadWrite(){
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

        Parcel parcel = Parcel.obtain();
        city.writeToParcel(parcel, city.describeContents());

        parcel.setDataPosition(0);

        CityModel createdFromParcel = city.CREATOR.createFromParcel(parcel);

        assertEquals(weatherId, createdFromParcel.getWeatherId());
        assertEquals(humidity, createdFromParcel.getHumidity());
        assertEquals(currentTemp, createdFromParcel.getCurrentTemp(), EPS);
        assertEquals(maxTemp, createdFromParcel.getMaxTemp(), EPS);
        assertEquals(minTemp, createdFromParcel.getMinTemp(), EPS);
        assertEquals(windSpeed, createdFromParcel.getWindSpeed(), EPS);
        assertEquals(name, createdFromParcel.getName());
        assertEquals(weatherDescription, createdFromParcel.getWeatherDescription());
        assertEquals(cityId, createdFromParcel.getCityId());
    }
}
