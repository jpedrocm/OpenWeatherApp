package br.com.jpedrocm.openweatherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.jpedrocm.openweatherapp.R;
import br.com.jpedrocm.openweatherapp.utils.Utils;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener, View.OnClickListener {

    private static final float MARKER_ALPHA = 0.8f;
    private static final float MARKER_COLOR = 75;
    protected static final String COORDS_KEY = "coords";

    private AlertDialog mDialog;
    private TextView mDialogTextView;

    private MarkerOptions mMarkerOptions;
    private Marker mMarker;
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMarker = null;
        initializeViews();
    }

    private void initializeViews(){
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        FloatingActionButton fabSearch = (FloatingActionButton) findViewById(R.id.fab_search);
        fabSearch.setOnClickListener(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom, null);

        mDialog = new AlertDialog.Builder(this).create();
        mDialog.setView(dialogView);
        mDialog.setCancelable(false);

        mDialogTextView = (TextView) dialogView.findViewById(R.id.tv_dialog_msg);

        Button buttonOk = (Button) dialogView.findViewById(R.id.btn_ok);
        buttonOk.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.action_settings:
                //intent = new Intent(this, SettingsActivity.class);
                //startActivity(intent);
                return true;
            case R.id.action_about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mMarkerOptions = new MarkerOptions().draggable(true).alpha(MARKER_ALPHA)
                    .icon(BitmapDescriptorFactory.defaultMarker(MARKER_COLOR));

        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnMarkerDragListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(mMarker != null) mMarker.remove();
        mMarker = mGoogleMap.addMarker(mMarkerOptions.position(latLng));
        updateMarkerTitleAndCamera();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {}

    @Override
    public void onMarkerDrag(Marker marker) {}

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mMarker = marker;
        updateMarkerTitleAndCamera();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.fab_search:
                clickedFabButton();
                break;
            case R.id.btn_ok:
                clickedDialogButton();
            default:
                break;
        }
    }

    private void updateMarkerTitleAndCamera(){
        LatLng latLng = mMarker.getPosition();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        String title = Utils.getCoordinatesString(latLng);
        mMarker.setTitle(title);
    }

    private void clickedDialogButton(){
        mDialog.dismiss();
    }

    private void clickedFabButton(){
        if(mMarker==null)
            showDialogWithGivenText(R.string.dialog_no_marker_msg);
        else if(!Utils.isInternetOn(this))
            showDialogWithGivenText(R.string.dialog_no_internet_msg);
        else
            changeActivity();
    }

    private void showDialogWithGivenText(int textId){
        mDialogTextView.setText(textId);
        mDialog.show();
    }

    private void changeActivity(){
        Intent intent = new Intent(this, CityListActivity.class);
        intent.putExtra(COORDS_KEY, mMarker.getPosition());
        startActivity(intent);
    }
}