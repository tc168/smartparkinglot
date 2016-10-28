package humber.thunderbolts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {


    private TextView switchStatus;
    private Switch mySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        switchStatus = (TextView) findViewById(R.id.switchStatus);
        mySwitch = (Switch) findViewById(R.id.mySwitch);

        //set the switch to ON
        mySwitch.setChecked(true);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    switchStatus.setText("Switch is currently ON");
                }else{
                    switchStatus.setText("Switch is currently OFF");
                }

            }
        });

        //check the current state before we display the screen
        if(mySwitch.isChecked()){
            switchStatus.setText("Switch is currently ON");
        }
        else {
            switchStatus.setText("Switch is currently OFF");
        }
    }

    }
