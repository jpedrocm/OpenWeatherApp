package br.com.jpedrocm.openweatherapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CityModel implements Parcelable {

    private int mWeatherId;
    private int mHumidity;

    private double mCurrentTemp;
    private double mMaxTemp;
    private double mMinTemp;
    private double mWindSpeed;

    private String mName;
    private String mWeatherDescription;
    private String mCityId;

    public CityModel(){}

    public CityModel(int weatherId, int humidity, double currentTemp, double maxTemp, double minTemp, double windSpeed,
                     String name, String weatherDescription, String cityId){
        mWeatherId = weatherId;
        mHumidity = humidity;
        mCurrentTemp = currentTemp;
        mMaxTemp = maxTemp;
        mMinTemp = minTemp;
        mWindSpeed = windSpeed;
        mName = name;
        mWeatherDescription = weatherDescription;
        mCityId = cityId;
    }

    public int getWeatherId(){
        return mWeatherId;
    }

    public int getHumidity(){
        return mHumidity;
    }

    public double getCurrentTemp(){
        return mCurrentTemp;
    }

    public double getMaxTemp(){
        return mMaxTemp;
    }

    public double getMinTemp(){
        return mMinTemp;
    }

    public double getWindSpeed(){ return mWindSpeed;}

    public String getName(){
        return mName;
    }

    public String getWeatherDescription(){
        return mWeatherDescription;
    }

    public String getCityId(){
        return mCityId;
    }

    private CityModel(Parcel src){
        mWeatherId = src.readInt();
        mHumidity = src.readInt();

        mCurrentTemp = src.readDouble();
        mMaxTemp = src.readDouble();
        mMinTemp = src.readDouble();
        mWindSpeed = src.readDouble();

        mName = src.readString();
        mWeatherDescription = src.readString();
        mCityId = src.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mWeatherId);
        dest.writeInt(mHumidity);

        dest.writeDouble(mCurrentTemp);
        dest.writeDouble(mMaxTemp);
        dest.writeDouble(mMinTemp);
        dest.writeDouble(mWindSpeed);

        dest.writeString(mName);
        dest.writeString(mWeatherDescription);
        dest.writeString(mCityId);
    }

    public static final Parcelable.Creator<CityModel> CREATOR = new Parcelable.Creator<CityModel>() {
        public CityModel createFromParcel(Parcel in) {
            return new CityModel(in);
        }
        public CityModel[] newArray(int size) {
            return new CityModel[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }
}