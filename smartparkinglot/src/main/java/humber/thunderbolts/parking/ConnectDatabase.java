//Thomas Chang
package humber.thunderbolts.parking;

import android.os.AsyncTask;

//Using java.sql instead of com.mysql?
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Thomas on 2016-11-15.
 */


public class ConnectDatabase extends AsyncTask<Object, Object, ArrayList<ParkingSpot>> {

    public ArrayList<ParkingSpot> getParkingSpotsList() {
        return parkingSpotsList;
    }

    private ArrayList<ParkingSpot> parkingSpotsList;

    public ConnectDatabase() {
        parkingSpotsList = new ArrayList<ParkingSpot>();


    }


    @Override
    protected ArrayList<ParkingSpot> doInBackground(Object... params) {
        String response = "";
        Connection conn = null;
        try {

            // SET CONNECTIONSTRING

            Class.forName("com.mysql.jdbc.Driver");


            conn = (Connection) DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/smartparking", "root", "password");
            Statement stmt = conn.createStatement();

            ResultSet reset = stmt.executeQuery(" select * from parkingspots ");
            ResultSetMetaData rsmd = (ResultSetMetaData) reset.getMetaData();

            int columnsNumber = rsmd.getColumnCount();
            while (reset.next()) {


                ParkingSpot parkingSpot = new ParkingSpot(reset.getInt(rsmd.getColumnName(1)), reset.getString(rsmd.getColumnName(2)), reset.getBoolean(rsmd.getColumnName(3)),reset.getDouble(rsmd.getColumnName(4)),reset.getDouble(rsmd.getColumnName(5)),reset.getDate(rsmd.getColumnName(6)) ) ;

                System.out.println(parkingSpot);
                parkingSpotsList.add(parkingSpot);

            }
            conn.close();

        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        /// EditText num = (EditText) findViewById(R.id.displaymessage);
        // num.setText(reset.getString(1));



         return parkingSpotsList;
    }

}


