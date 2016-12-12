//Thomas Chang n01040694
package humber.thunderbolts;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import humber.thunderbolts.parking.ConnectDatabase;
import humber.thunderbolts.parking.MultiDrawable;
import humber.thunderbolts.parking.ParkingSpot;


// The map actitiy screen to display most of the infromation
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ClusterManager.OnClusterClickListener<ParkingSpot>, ClusterManager.OnClusterInfoWindowClickListener<ParkingSpot>, ClusterManager.OnClusterItemClickListener<ParkingSpot>, ClusterManager.OnClusterItemInfoWindowClickListener<ParkingSpot> {

    private GoogleMap mMap;
    private ConnectDatabase con;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private ClusterManager<ParkingSpot> mClusterManager;


    private static final int LOCATION_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (getResources().getBoolean(R.bool.landscape_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //create connection to a database and get the data
        con = new ConnectDatabase();


        HistoryDatabaseHelper db = new HistoryDatabaseHelper(this);
        db.addHistory(new History("Dec/23/2014", "Young", "5"));

        //create a drawer so that we can have item / screens to navigate to
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);

    }


    //kill the request location once it goes on pause
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    //when the map is retreive then...
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //set to satellite mode picture
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);


        double lat;
        double lng;
        float zoom;

        //check  if system is greater than masrhmellow for runtime permisisons
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
            } else {
                //Request Location Permission
                getLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        //set default location
        lat = Double.parseDouble(getString(R.string.defaultLat));
        lng = Double.parseDouble(getString(R.string.defaultLong));
        zoom = Float.parseFloat(getString(R.string.defaultZoom));


        LatLng setLocation = new LatLng(lat, lng);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(setLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));


        //create a cluster manager to handle grouping markers
        mClusterManager = new ClusterManager<ParkingSpot>(this, mMap);

        mClusterManager.setRenderer(new ParkingSpotRender());
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        getParkingList(googleMap);

        mClusterManager.cluster();


    }

    /// create a service call to google api in order to get mylocation
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    //set the parameters for the mylocation service
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    //Create a dialog to confrim if the user wishes to exit
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            new AlertDialog.Builder(this)
                    .setMessage(R.string.back_exit)
                    .setCancelable(false)
                    .setPositiveButton(R.string.menu_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MapActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.menu_no, null)
                    .show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    // display the screen accordingly to witch  button pressed
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        //pay for your parking fee and leave in 20 minutes
        /**drawer_pay and wallet Created by Yan Yu (n00769714)******/
        if (id == R.id.drawer_pay) {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    builder1.setMessage(R.string.dialog_builder1);
                    builder1.setPositiveButton("Ok", null);
                    builder1.create();
                    builder1.show();
                }
            })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    })
                    .setMessage(R.string.dialog_pay_message)
                    .setTitle(R.string.dialog_pay_title);
            builder.create();
            builder.show();

        } else if (id == R.id.drawer_wallet) {
            Intent intentWalletActivity = new Intent(this, WalletActivity.class);
            startActivity(intentWalletActivity);
        }


        //deleted old payment
        else if (id == R.id.drawer_login) {

            Intent intentLoginActivity = new Intent(this, LoginActivity.class);
            startActivity(intentLoginActivity);

        } else if (id == R.id.drawer_settings) {
            Intent intentSettingActivity = new Intent(this, SettingActivity.class);
            startActivity(intentSettingActivity);
        }

        //Add History item @Yan Yu
        else if (id == R.id.drawer_parking_history) {
            Intent intentHistoryListActivity = new Intent(this, ParkingActivityList.class);
            startActivity(intentHistoryListActivity);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //  retrieve the  list of parking spots
    private ArrayList<ParkingSpot> getParkingList(GoogleMap googleMap) {

        ArrayList<ParkingSpot> listOfParkingSpots = null;
        try {
            listOfParkingSpots = con.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        for (ParkingSpot p : listOfParkingSpots) {
            // place all the parking spots into the map cluster
            mClusterManager.addItem(p);


        }

        return listOfParkingSpots;
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //statement to check the permission for location
    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    //when the app recongizes that there is a change of lcoation it will change and set the location on the map
    @Override
    public void onLocationChanged(Location location) {

        //System.out.print(map);
        if (checkLocationPermission() && mMap != null)

            mMap.setMyLocationEnabled(true);


        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);


        LatLng myPosition = new LatLng(latitude, longitude);

        // Add Makrer


        // Zoom into my location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        //remove the serive to get location if the api is not called
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

//function to  request the permission if it not enabled
    private void getLocationPermission() {
        boolean returnCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;


        if (returnCheck) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION);


        }


    }

    //call  back when the permission dialog pops up
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //create the service to display curre location
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);

                    }

                }

                // other 'case' lines to check for other
                // permissions this app might request
            }
        }
    }

    //getter method to get the google map
    protected GoogleMap getMap() {
        return mMap;
    }


    //when clicking on a cluster zoom into it to display more
    @Override
    public boolean onClusterClick(Cluster<ParkingSpot> cluster) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<ParkingSpot> cluster) {

    }

    @Override
    public boolean onClusterItemClick(ParkingSpot parkingSpot) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(ParkingSpot parkingSpot) {

    }


    //custom class to allow a custom image onto the clusters of marker
    private class ParkingSpotRender extends DefaultClusterRenderer<ParkingSpot> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        //set up all the size to display a marker cluster
        public ParkingSpotRender() {
            super(getApplicationContext(), getMap(), mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.marker_image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        //get the correct resrouce for a single item
        @Override
        protected void onBeforeClusterItemRendered(ParkingSpot parkingSpot, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            mImageView.setImageResource(parkingSpot.picture);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(parkingSpot.getLicensePlate());

        }

        //as a group of marker set the items
        @Override
        protected void onBeforeClusterRendered(Cluster<ParkingSpot> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (ParkingSpot p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;


                Drawable drawable = getResources().getDrawable(p.picture);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        // to decide if it should be render  as cluster check the cluster size if its greater than 1
        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }
}
