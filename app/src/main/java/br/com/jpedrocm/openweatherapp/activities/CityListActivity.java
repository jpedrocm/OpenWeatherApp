package br.com.jpedrocm.openweatherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.jpedrocm.openweatherapp.R;
import br.com.jpedrocm.openweatherapp.adapters.CityListAdapter;
import br.com.jpedrocm.openweatherapp.models.CityModel;
import br.com.jpedrocm.openweatherapp.utils.Utils;

public class CityListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener {

    protected static final String CITY_KEY = "city";

    private LatLng mCoords;
    private ArrayList<CityModel> mCities;

    private AlertDialog mDialog;
    private TextView mDialogTextView;
    private Button mDialogButton;
    private ListView mCityListView;
    private CityListAdapter mCityListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        initializeViews();

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(MainActivity.COORDS_KEY)) {
            mCoords = intent.getParcelableExtra(MainActivity.COORDS_KEY);
            downloadCitiesWeather();
        }
    }

    private void initializeViews(){
        setContentView(R.layout.activity_city_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCityListView = (ListView) findViewById(R.id.city_list_view);
        mCityListAdapter = new CityListAdapter(this, R.layout.item_city_list, new ArrayList<CityModel>());
        mCityListView.setOnItemClickListener(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom, null);

        mDialog = new AlertDialog.Builder(this).create();
        mDialog.setView(dialogView);
        mDialog.setCancelable(false);

        mDialogTextView = (TextView) dialogView.findViewById(R.id.tv_dialog_msg);

        mDialogButton = (Button) dialogView.findViewById(R.id.btn_ok);
        mDialogButton.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CityModel chosenCity = mCities.get(position);
        Intent intent = new Intent(this, CityWeatherActivity.class);
        intent.putExtra(CITY_KEY, chosenCity);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.btn_ok:
                clickedDialogButton();
            default:
                break;
        }
    }

    private void clickedDialogButton(){
        mDialog.dismiss();
    }

    private void showErrorDialog(){
        mDialogButton.setVisibility(View.VISIBLE);
        mDialogTextView.setText(R.string.dialog_no_results_msg);
        mDialog.show();
    }

    private void showDownloadDialog(){
        mDialogButton.setVisibility(View.INVISIBLE);
        mDialogTextView.setText(R.string.dialog_download_msg);
        mDialog.show();
    }

    private void downloadCitiesWeather(){
        if(mCoords != null) {
            showDownloadDialog();

            int numCities = 15;
            String API_KEY = getResources().getString(R.string.OPEN_WEATHER_KEY);

            String urlQuery = Utils.formUrlQuery(mCoords, numCities, API_KEY);

            JsonObjectRequest request = new JsonObjectRequest(urlQuery, null,
                    mListener, mErrorListener);

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }
    }

    Response.Listener<JSONObject> mListener = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mCities = (ArrayList<CityModel>) Utils.parseJSONData(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(mCities==null || mCities.size() == 0)
                showErrorDialog();
            else
                updateViewSuccessfulDownload();
        }
    };

    Response.ErrorListener mErrorListener = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            showErrorDialog();
        }
    };

    private void updateViewSuccessfulDownload(){
        mCityListAdapter.addAll(mCities);
        mCityListView.setAdapter(mCityListAdapter);
        clickedDialogButton();
    }
}