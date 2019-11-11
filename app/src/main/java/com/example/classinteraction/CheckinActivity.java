package com.example.classinteraction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.classinteraction.utils.Checkin;
import com.example.classinteraction.utils.PermissionUtils;
import com.example.classinteraction.utils.TextDialog;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckinActivity extends  AppCompatActivity
        implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        TextDialog.TextDialogListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    private DatabaseReference ref ;
    private final String ID_KEY = "user_id";
    private final String NAME_KEY = "user_name";
    private final String CLASS_KEY = "class_code";
    private String user_id, user_name, class_code, local_address;
    private Checkin newCheckin;
    public static final String DIALOG_TAG = "dialog_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        initUI();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ButterKnife.bind(this);
        ref = FirebaseDatabase.getInstance().getReference();
    }

    private void initUI(){
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            user_id = extras.getString(ID_KEY);
            user_name = extras.getString(NAME_KEY);
            class_code = extras.getString(CLASS_KEY);
            updateToast(user_id+user_name+class_code);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);

        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        updateToast("MyLocation button clicked");
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        //updateToast("Current location:\n" + location);
        newCheckin = new Checkin();
        newCheckin.setLatitude(location.getLatitude());
        newCheckin.setLongitude(location.getLongitude());
        newCheckin.setDatetime(new Date());
        newCheckin.setName(user_name);
        newCheckin.setUserId(user_id);
        //from location to address
        try{
            geoLocate();
            LatLng position = new LatLng(newCheckin.getLatitude(), newCheckin.getLongitude());
            mMap.addMarker(new MarkerOptions().position(position).title(local_address));
        }catch(IOException e){updateToast(e.getLocalizedMessage());}



    }

    /*
     * submit checkin  */
    @OnClick(R.id.submitButton) void submitCheckin(){
        //push checkin object to Firebase
        if (newCheckin==null){
            updateToast("Click on your current location to get location for check-in.");
        }else{
            ////use dialog to confirm
            TextDialog dialog = TextDialog.newInstance("Confirm", "Are you sure to checkin \nat "+local_address+"?");
            dialog.show(getSupportFragmentManager(), DIALOG_TAG);
        }

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
    private void updateToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void geoLocate() throws IOException {
        Geocoder gc = new Geocoder((this));
        List<Address> list =gc.getFromLocation(newCheckin.getLatitude(), newCheckin.getLongitude(),1);
        if(list.size()>0){
            Address address =list.get(0);
            local_address =address.getAddressLine(0);
        }
    }
    @Override
    public void onTextDialogOK(boolean agree) {
        if(agree){
            ref = FirebaseDatabase.getInstance().getReference("checkin").child(class_code);
            ref.push().setValue(newCheckin);
            updateToast("Successfully Checkin");
        }else{
            updateToast("Cancel checkin");
        }
    }
}
