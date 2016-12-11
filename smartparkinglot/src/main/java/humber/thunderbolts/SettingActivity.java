package humber.thunderbolts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener
{


    private TextView switchStatus;
    private Switch mySwitch;
    ToggleButton btn;
    static int flag=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Theme.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_setting);
        switchStatus = (TextView) findViewById(R.id.switchStatus);
        mySwitch = (Switch) findViewById(R.id.mySwitch);
        btn=(ToggleButton)findViewById(R.id.toggleButton);
        findViewById(R.id.toggleButton).setOnClickListener(this);


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
    @Override
    public void onClick(View v)
    {
        if (((ToggleButton) v).isChecked())
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

    }
}


