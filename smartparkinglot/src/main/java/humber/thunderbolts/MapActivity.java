//Thomas Chang
package humber.thunderbolts;

import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

//import humber.thunderbolts.parking.ConnectDatabase;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ClusterManager.OnClusterClickListener<ParkingSpot>, ClusterManager.OnClusterInfoWindowClickListener<ParkingSpot>, ClusterManager.OnClusterItemClickListener<ParkingSpot>, ClusterManager.OnClusterItemInfoWindowClickListener<ParkingSpot> {


    private GoogleMap mMap;

    private ConnectDatabase con;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private ClusterManager<ParkingSpot> mClusterManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        con = new ConnectDatabase();

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

//        //Testing history database
//        HistoryDatabaseHelper db = new HistoryDatabaseHelper(this);
//        db.addHistory(new History("Dec/23/2014", "Young", "5"));


        HistoryDatabaseHelper db = new HistoryDatabaseHelper(this);
        db.addHistory(new History("Dec/23/2014", "Young", "5"));


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

        // Add a marker in Sydney and move the camera
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        ArrayList<ParkingSpot> listOfParkingSpots = null;
        try {
            listOfParkingSpots = con.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        lat = Double.parseDouble(getString(R.string.defaultLat));
        lng = Double.parseDouble(getString(R.string.defaultLong));
        zoom = Float.parseFloat(getString(R.string.defaultZoom));

        }



        LatLng humber = new LatLng(43.72952382003048, -79.60450954735279);
        // mMap.addMarker(new MarkerOptions().position(humber).title("Marker in Humber Parking"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(humber));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18.5f));

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



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {

                Log.d("arg0", arg0.latitude + "-" + arg0.longitude);
            }
        });

    }


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
        // Inflate the menu; this adds items to the action bar if it is present.
      // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      //  int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
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


    private ArrayList<ParkingSpot> getParkingList(GoogleMap googleMap) {

        ArrayList<ParkingSpot> listOfParkingSpots = null;
        try {
            listOfParkingSpots = con.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        for (ParkingSpot p : listOfParkingSpots) {
       /*    LatLng parkSpot = new LatLng(p.getLongitude(), p.getLatitude());
            float color = (p.isSpotTaken()) ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_RED;
            MarkerOptions markerOption = new MarkerOptions()
                    .position(parkSpot).title(p.getLicensePlate())
                    .icon(BitmapDescriptorFactory.defaultMarker(color));

            Marker marker = googleMap.addMarker(markerOption);
            marker.showInfoWindow();

*/
            mClusterManager.addItem(p);

           // System.out.println("add list" + p.getPosition());

        }

        return listOfParkingSpots;
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

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

        System.out.println("Lat " + latitude);
        System.out.println("long " + longitude);

        LatLng myPosition = new LatLng(latitude, longitude);

        // Add Makrer


        // Zoom into my location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

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


    protected GoogleMap getMap() {
        return mMap;
    }


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


    private class ParkingSpotRender extends DefaultClusterRenderer<ParkingSpot> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

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

        @Override
        protected void onBeforeClusterItemRendered(ParkingSpot parkingSpot, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            mImageView.setImageResource(parkingSpot.picture);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title( parkingSpot.getLicensePlate());

        }

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

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

}
