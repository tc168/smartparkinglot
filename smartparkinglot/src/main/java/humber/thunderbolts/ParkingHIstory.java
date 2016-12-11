package humber.thunderbolts;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Yan Yu on 2016-12-10.
 *
 * Showing Parking Details
 */

public class ParkingHistory extends Activity {

    public static final String EXTRA_HISTORYNO = "historyNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        int historyNo = (Integer) getIntent().getExtras().get(EXTRA_HISTORYNO);
        History history = History.history[historyNo];

        TextView date = (TextView) findViewById(R.id.textView2);
        date.setText(history.getDate());

        TextView location = (TextView) findViewById(R.id.textView4);
        location.setText(history.getLocation());

        TextView fee = (TextView) findViewById(R.id.textView6);
        fee.setText(history.getFee());
    }
}
