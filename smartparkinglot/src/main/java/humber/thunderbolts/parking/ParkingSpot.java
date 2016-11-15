package humber.thunderbolts.parking;

import java.sql.Date;

/**
 * Created by Thomas on 2016-11-14.
 */

public class ParkingSpot {
    private int id;
    private String licensePlate;
    private Date date;

    public ParkingSpot(int id, String licensePlate, boolean isSpotTaken, double longitude, double latitude, Date date) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.isSpotTaken = isSpotTaken;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
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
}
