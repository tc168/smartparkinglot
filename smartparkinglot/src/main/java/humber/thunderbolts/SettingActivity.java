package humber.thunderbolts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import static humber.thunderbolts.R.styleable.CompoundButton;

public class SettingActivity extends AppCompatActivity {

    static int flag=1;
    private TextView switchStatus;
    private Switch mySwitch;
    private TextView switchStatus2;
    private Switch mySwitch2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        switchStatus = (TextView) findViewById(R.id.switchStatus);
        mySwitch = (Switch) findViewById(R.id.mySwitch);
        switchStatus2 = (TextView) findViewById(R.id.switchStatus2);
        mySwitch2 = (Switch) findViewById(R.id.mySwitch2);
        //set the switch to ON
        mySwitch.setChecked(true);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    switchStatus.setText("Switch is currently ON");
                } else {
                    switchStatus.setText("Switch is currently OFF");
                }

            }
        });

        //check the current state before we display the screen
        if (mySwitch.isChecked()) {
            switchStatus.setText("Switch is currently ON");
        } else {
            switchStatus.setText("Switch is currently OFF");
        }
    }

   /*// mySwitch2.setChecked(true);
    //attach a listener to check for changes in state
   // mySwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

    {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
        boolean isChecked) {

        if (isChecked) {
            switchStatus2.setText("Switch is currently ON");
        } else {
            switchStatus2.setText("Switch is currently OFF");
        }

    }
    });

    //check the current state before we display the screen
    if(mySwitch2.isChecked())
    {
        flag=flag+1;
    }


    if(flag%2==0)
    {
        Theme.changeToTheme(this,Theme.BlueTheme);
        setContentView(R.layout.activity_setting);
    }
    else
    {
        Theme.changeToTheme(this,Theme.GreenTheme);
        setContentView(R.layout.activity_setting);
    }
*/
}


