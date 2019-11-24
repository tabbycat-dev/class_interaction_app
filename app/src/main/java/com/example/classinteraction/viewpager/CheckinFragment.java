package com.example.classinteraction.viewpager;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.classinteraction.R;
import com.example.classinteraction.utils.Checkin;
import com.example.classinteraction.utils.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CheckinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckinFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback
{
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private GoogleMap mMap;
    private DatabaseReference ref ;
    private static String ID_KEY = "user_id";
    private static String UNAME_KEY = "user_name";
    private static String CLASS_KEY = "class_code";
    public static final String FRAGMENT_TAG = "CHECKIN_FRAG";
    private String user_id, user_name, class_code, local_address;
    private Checkin newCheckin;
    @BindView(R.id.submitButton)
    Button submitButton;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinator;

    // TODO: Rename and change types of parameters

    public CheckinFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     */
    // TODO: Rename and change types and number of parameters
    public static CheckinFragment newInstance(String class_code, String user_id, String user_name) {
        CheckinFragment fragment = new CheckinFragment();
        Bundle args = new Bundle();
        args.putString(CLASS_KEY, class_code);
        args.putString(ID_KEY, user_id);
        args.putString(UNAME_KEY, user_name);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        if (getArguments() != null) {
            user_id = getArguments().getString(ID_KEY);
            user_name = getArguments().getString(UNAME_KEY);
            class_code = getArguments().getString(CLASS_KEY);
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkin, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (servicesOK()) { //check service of google api is ok
            Log.d(FRAGMENT_TAG, "onCreateView-ok");

            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            if (initMap()) { //display map
                Log.d(FRAGMENT_TAG, "onCreateView-ready to map");
            } else {
                Log.d(FRAGMENT_TAG, "onCreateView-map is not connected");
            }

        } else {
            Log.d(FRAGMENT_TAG, "onCreateView-serviceOk is false");
        }

        ButterKnife.bind(this,view);
        ref = FirebaseDatabase.getInstance().getReference();
        CoordinatorLayout mCoordinator = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout); //this is for snackbar
        return view;
    }
    //display map
    private boolean initMap(){
        if (mMap == null) {
            Log.d(FRAGMENT_TAG, "initMap-null");

        }
        return (mMap != null);

    }
    //check service of google api is ok
    public boolean servicesOK() {

        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getBaseContext());

        if (isAvailable == ConnectionResult.SUCCESS) {
            Log.d(FRAGMENT_TAG, "serviceOK- SUCCESSS");
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Log.d(FRAGMENT_TAG, "serviceOK- ERROR dialog");
            Dialog dialog =
                    GooglePlayServicesUtil.getErrorDialog(isAvailable, getActivity(), ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Log.d(FRAGMENT_TAG, "serviceOK- cant connect");
            updateToast("Can't connect to mapping service");
        }

        return false;
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
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( //Method of Fragment
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION },
                    LOCATION_PERMISSION_REQUEST_CODE );
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        updateToast("Loading current location..");
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
            performCheckin(true);
            //updateToast("Done check-in at "+local_address);
            updateSnackbar();
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
            updateToast("Location permission is granted!");
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
            updateToast("Location permission is denied!");
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getChildFragmentManager(), "dialog");
    }
    private void updateToast(String text){
        Toast.makeText(this.getContext(), text, Toast.LENGTH_LONG).show();
    }

    private void geoLocate() throws IOException {
        Geocoder gc = new Geocoder((this.getContext()));
        List<Address> list =gc.getFromLocation(newCheckin.getLatitude(), newCheckin.getLongitude(),1);
        if(list.size()>0){
            Address address =list.get(0);
            local_address =address.getAddressLine(0);
        }
    }

    public void performCheckin(boolean agree) {
        Log.d(FRAGMENT_TAG," 1 - call TextDialog");
        if(agree){
            ref = FirebaseDatabase.getInstance().getReference("checkin").child(class_code);
            ref.push().setValue(newCheckin);
            updateToast("Successfully Checkin");
        }else{
            updateToast("Cancel checkin");
        }
    }

    /* show status using SNACK BAR */
    private void updateSnackbar(){
        Snackbar mySnackbar =Snackbar.make(mCoordinator, R.string.checkin_added,Snackbar.LENGTH_LONG);
        mySnackbar.setAction(R.string.undo_string, new MyUndoListener());
        mySnackbar.show();
    }

    public class MyUndoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            removeItemFromFirebse();
        }
    }

    private void removeItemFromFirebse() {
        // Code to undo the user's last action
        // ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.orderByChild("userId").equalTo(user_id);
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot classsnapshot: dataSnapshot.getChildren()) {
                    classsnapshot.getRef().removeValue();
                    updateToast("Successfully UNDO");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(FRAGMENT_TAG, "onCancelled", databaseError.toException());
                updateToast("Fail to UNDO");
            }
        });
    }
}
