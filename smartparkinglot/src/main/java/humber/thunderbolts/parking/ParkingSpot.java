package humber.thunderbolts.parking;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.sql.Date;

import humber.thunderbolts.R;

/**
 * Created by Thomas on 2016-11-14.
 */
// Parking Spot model data ,all these  are data used to desribe the parking spot and to   be a map cluster item
public class ParkingSpot implements ClusterItem {
    private int id;
    private String licensePlate;
    private Date date;
    private  final LatLng mPosition;
    public final int picture;

    public ParkingSpot(int id, String licensePlate, boolean isSpotTaken, double latitude, double longitude, Date date) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.isSpotTaken = isSpotTaken;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
        mPosition = new LatLng(latitude,longitude);
        if (isSpotTaken){
            picture = R.drawable.parking_spot_taken;
        }
        else {
         picture = R.drawable.parking_spot;
        }
    }

    private boolean isSpotTaken;
    private double longitude;
    private double latitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public boolean isSpotTaken() {
        return isSpotTaken;
    }

    public void setSpotTaken(boolean spotTaken) {
        isSpotTaken = spotTaken;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    @Override
    public String toString() {
        return "ParkingSpot{" +
                "id=" + id +
                ", licensePlate='" + licensePlate + '\'' +
                ", date=" + date +
                ", isSpotTaken=" + isSpotTaken +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
