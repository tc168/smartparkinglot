package humber.thunderbolts;

/**
 * Created by Yan Yu on 2016-12-10.
 *
 * Parking History java code
 */


public class History {

    //private variables
    private String fee;
    private String date;
    private String location;


    public History() {

    }

    public static final History[] history = {
            new History("May 11, 2014", "Finch", "5.60"),
            new History("Mar 22, 2015", "College", "3.00"),
            new History("Jun 05, 2015", "Young", "10.00"),
            new History("Nov 02, 2015", "Humber", "15.99"),
            new History("Dec 21, 2015", "North", "1.10")
    };

    public History(String date, String location, String fee) {
        this.date = date;
        this.location = location;
        this.fee = fee;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getFee() {
        return fee;
    }

    public String toString() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}