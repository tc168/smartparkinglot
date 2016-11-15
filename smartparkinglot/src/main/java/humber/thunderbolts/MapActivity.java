package humber.thunderbolts;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import humber.thunderbolts.parking.ConnectDatabase;
import humber.thunderbolts.parking.ParkingSpot;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private  ConnectDatabase con;




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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
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

        assert listOfParkingSpots != null;
        for (ParkingSpot p : listOfParkingSpots){
            LatLng parkSpot = new LatLng(p.getLongitude(),p.getLatitude());
            float color = (p.isSpotTaken()) ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_RED;
            MarkerOptions markerOption = new MarkerOptions()
                    .position(parkSpot).title(p.getLicensePlate())
                    .icon(BitmapDescriptorFactory.defaultMarker(color));

            Marker marker =  mMap.addMarker(markerOption);
            marker.showInfoWindow();

        }



        LatLng humber = new LatLng(43.72952382003048, -79.60450954735279);
        // mMap.addMarker(new MarkerOptions().position(humber).title("Marker in Humber Parking"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(humber));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18.5f));


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


          if (id == R.id.drawer_payment) {
            Intent intentPaymentScreen = new Intent(this, PaymentScreen.class);
            startActivity(intentPaymentScreen);


        } else if (id == R.id.drawer_login) {
            Intent intentLoginActivity = new Intent(this, LoginActivity.class);
            startActivity(intentLoginActivity);

        } else if (id == R.id.drawer_settings) {
            Intent intentSettingActivity = new Intent(this, SettingActivity.class);
            startActivity(intentSettingActivity);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
