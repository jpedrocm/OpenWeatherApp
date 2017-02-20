package br.com.jpedrocm.openweatherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.jpedrocm.openweatherapp.R;
import br.com.jpedrocm.openweatherapp.models.CityModel;
import br.com.jpedrocm.openweatherapp.utils.Utils;

public class CityWeatherActivity extends AppCompatActivity {

    private CityModel city;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent!=null && intent.hasExtra(CityListActivity.CITY_KEY)){
            city = intent.getParcelableExtra(CityListActivity.CITY_KEY);
        }

        initializeViews();
    }

    private void initializeViews(){
        setContentView(R.layout.activity_city_weather);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imgView = (ImageView) findViewById(R.id.img_weather);
        int imageResourceId = Utils.getWeatherImageId(city.getWeatherId());
        imgView.setImageResource(imageResourceId);

        TextView tvName = (TextView) findViewById(R.id.tv_city_name);
        TextView tvCurTemp = (TextView) findViewById(R.id.tv_cur_temp);
        TextView tvExtremeTemp = (TextView) findViewById(R.id.tv_extreme_temp);
        TextView tvHumidity = (TextView) findViewById(R.id.tv_humidity);
        TextView tvWindSpeed = (TextView) findViewById(R.id.tv_wind_speed);
        TextView tvDescription = (TextView) findViewById(R.id.tv_description);

        tvName.setText(city.getName());
        tvExtremeTemp.setText(Utils.getExtremeTempsString(city.getMaxTemp(), city.getMinTemp(), Utils.TEMP_UNIT.Celsius));
        tvCurTemp.setText(Utils.getTempString(city.getCurrentTemp(), Utils.TEMP_UNIT.Celsius));
        tvHumidity.setText(Utils.getHumidityString(city.getHumidity()));
        tvWindSpeed.setText(Utils.getWindString(city.getWindSpeed(), Utils.WIND_UNIT.KMH));
        tvDescription.setText(city.getWeatherDescription());
    }
}