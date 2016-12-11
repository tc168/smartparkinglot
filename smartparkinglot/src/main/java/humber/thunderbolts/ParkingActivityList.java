package humber.thunderbolts;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Yan Yu on 2016-12-10.
 *
 *  Parking History List View
 */

public class ParkingActivityList extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listDrinks = getListView();
        ArrayAdapter<History> listAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, History.history);
        listDrinks.setAdapter(listAdapter);
    }

    @Override
    public void onListItemClick(ListView listView,
                                View itemView,
                                int position,
                                long id) {
        Intent intent = new Intent(ParkingActivityList.this, ParkingHistory.class);
        intent.putExtra(ParkingHistory.EXTRA_HISTORYNO, (int) id);
        startActivity(intent);
    }

}
