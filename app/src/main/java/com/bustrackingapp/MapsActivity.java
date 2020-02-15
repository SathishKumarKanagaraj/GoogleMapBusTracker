package com.bustrackingapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends Fragment implements OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private List<BusModel> busStopingList;

    private ArrayList<LatLng> markerPoints;

    private List<Marker> mPickupLocationMarkers;

    private GoogleApiClient mGoogleApiClient;

    private Activity activity;

    private LocationRequest mLocationRequest;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    /*
     *current bus location marker create
     **/
    private Marker busLocationMarker;

    Handler handler;

    private Runnable runnable;

    private static final int OVERVIEW = 0;

    private String selectedBus = "";

    private TextView routeNumber;

    private TextView busRouteTitle;

    private TextView startTimeText;

    private TextView endTimeText;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activity = getActivity();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        markerPoints = new ArrayList<>();
        mPickupLocationMarkers = new ArrayList<>();

        routeNumber = view.findViewById(R.id.routeNumber);
        busRouteTitle = view.findViewById(R.id.busRouteTitle);
        startTimeText = view.findViewById(R.id.startTime);
        endTimeText = view.findViewById(R.id.endTime);

        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                busCurrentLocation();
                handler.postDelayed(this, 10000);
            }
        };

        addStopList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_maps, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        /*initialize value from selected bus id
         * */
        if (getArguments() != null) {
            selectedBus = getArguments().getString("route_value");
        }
        return v;
    }

    /**
     * Manipulates the map once available. This callback is triggered when the map is ready to be used. This is where we
     * can add markers or lines, add listeners or move the camera. In this case, we just add a marker near Sydney,
     * Australia. If Google Play services is not installed on the device, the user will be prompted to install it inside
     * the SupportMapFragment. This method will only be triggered once the user has installed Google Play services and
     * returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMarkerClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);

            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        // Add a marker in Sydney and move the camera
        /*LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location
            .getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of
                    the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }*/

        mapBusMarkers();

    }

    private void addStopList() {
        busStopingList = new ArrayList<>();

        if (selectedBus.equalsIgnoreCase("Tiruvotriyur to Poonamallee")) {
            busStopingList.add(new BusModel(13.1322, 80.2920, "Tondiarpet", "101", "9:30 AM", "12:30 PM"));
            busStopingList.add(new BusModel(13.1137, 80.2954, "Kalmandapam", "101", "10:30 AM", "10:50 AM"));
            busStopingList.add(new BusModel(13.0950, 80.2926, "Beach", "101", "11:00 AM", "11:45 AM"));
            busStopingList.add(new BusModel(13.0822, 80.2756, "Central", "101", "12:00 PM", "12:30 PM"));
        } else if (selectedBus.equalsIgnoreCase("Tiruvotriyur to CMBT")) {
            busStopingList.add(new BusModel(13.0418, 80.2341, "Tnagar", "159 A", "9:30 AM", "12:30 PM"));
            busStopingList.add(new BusModel(13.0706, 80.2279, "Aminjakarai", "159 A", "10:30 AM", "10:50 AM"));
            busStopingList.add(new BusModel(13.0500, 80.2121, "vadapalani", "159 A", "11:00 AM", "11:45 AM"));
            busStopingList.add(new BusModel(13.0694, 80.1948, "CMBT", "159 A", "12:00 PM", "12:30 PM"));
        } else if (selectedBus.equalsIgnoreCase("potheri to tambaram")) {
            busStopingList.add(new BusModel(12.8259, 80.0395, "Potheri", "39", "2:30 AM", "3:30 AM"));
            busStopingList.add(new BusModel(12.8913, 80.0810, "Vandalur", "39", "3:30 AM", "4:30 AM"));
        }

    }

    private void mapBusMarkers() {

        LatLng busLocation = new LatLng(busStopingList.get(0).getLatitude(), busStopingList.get(0).getLongtitude());

        MarkerOptions markerOptionBus = new MarkerOptions();
        markerOptionBus.position(busLocation);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bus);
        markerOptionBus.icon(BitmapDescriptorFactory.fromBitmap(getResizedBitmap(bitmap, 160, 65)));
        busLocationMarker = mMap.addMarker(markerOptionBus);
        busLocationMarker.setTitle(busStopingList.get(0).getStopingName());

        routeNumber.setText(busStopingList.get(0).getBusNo());
        busRouteTitle.setText(selectedBus);
        startTimeText.setText(busStopingList.get(0).getStartTime());
        endTimeText.setText(busStopingList.get(0).getEndTime());
        // Waypoints
        String waypoints = "";

        mPickupLocationMarkers = new ArrayList<>();
        for (BusModel busModel : busStopingList) {
            Log.e("latLng", "" + busModel.getLatitude() + busModel.getLongtitude());
            waypoints = waypoints + busModel.getLatitude() + "," + busModel.getLongtitude() + "|";

            markerPoints.add(new LatLng(busModel.getLatitude(), busModel.getLongtitude()));

            MarkerOptions busStopping = new MarkerOptions();
            busStopping.position(new LatLng(busModel.getLatitude(), busModel.getLongtitude()));
            BitmapDescriptor iconStop = BitmapDescriptorFactory.fromResource(R.drawable.pickup_point);
            busStopping.icon(iconStop);

            Marker currentPickup = mMap.addMarker(busStopping);
            currentPickup.setTitle(busModel.getStopingName());
            currentPickup.setSnippet("ETA:" + busModel.getEndTime());
            mPickupLocationMarkers.add(currentPickup);

        }

        getDirectionFromGoogleApi(waypoints);
    }

    @Override
    public void onLocationChanged(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

    }

    @Override
    public void onResume() {
        super.onResume();

        handler.postDelayed(runnable, 10000);
        mPickupLocationMarkers = new ArrayList<>();
        if (mMap != null) {
            mMap.clear();
        }

        if ((mGoogleApiClient != null) && (mGoogleApiClient.isConnected())) {
            Log.v("locationapi bus", "started");
            /*stop location updates */
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
            /*goes to pause state zoomable will be false*/
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * @return value has been either true or false
     */
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    /**
     * resized bmp  image
     *
     * @param bm        bitmap
     * @param newHeight height
     * @param newWidth  width
     * @return bmp
     */
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    private void busCurrentLocation() {
        LatLng latLng = new LatLng(busStopingList.get(0).getLatitude(), busStopingList.get(0).getLongtitude());

        LatLng busLatLng = busLocationMarker.getPosition();
        if (busLocationMarker != null && ((latLng.latitude != busLatLng.latitude) ||
                (latLng.longitude != busLatLng.longitude))) {
            MarkerAnimation.animateMarkerToGB(busLocationMarker, latLng, 3, new LatLngInterpolator.Spherical());
        }
    }

    private void getDirectionFromGoogleApi(String wayPoints) {
        DirectionsResult result = getDirectionDetails(TravelMode.WALKING, wayPoints);
        if (result != null) {
            addPolyline(result, mMap);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(busLocationMarker.getPosition()));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
        }
    }

    /**
     * draw polyline functionality between routes
     *
     * @param results get results from google api
     * @param mMap    pass map object
     */
    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[OVERVIEW].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath).width(12).color(Color.parseColor("#5ce8b9")));
    }

    private DirectionsResult getDirectionDetails(TravelMode mode, String wayPoints) {
        String src = "" + markerPoints.get(0).latitude + "," + markerPoints.get(0).longitude;
        String des = "" + markerPoints.get(1).longitude + "," + markerPoints.get(1).longitude;

        try {
            return DirectionsApi.newRequest(geoApiContext())
                    .mode(mode)
                    .origin(src)
                    .destination(des)
                    .waypoints(wayPoints)
                    .await();
        } catch (ApiException | InterruptedException | IOException e) {
            return null;
        }
    }

    private GeoApiContext geoApiContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.
                setQueryRateLimit(3).
                setApiKey("AIzaSyAUtw7qVHyOh3g8BOD23W92Xq9SYeRS5Qw").
                setConnectTimeout(2, TimeUnit.SECONDS).
                setReadTimeout(2, TimeUnit.SECONDS).
                setWriteTimeout(2, TimeUnit.SECONDS);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
