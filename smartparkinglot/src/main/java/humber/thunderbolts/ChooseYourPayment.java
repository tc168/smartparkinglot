package humber.thunderbolts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Tony on 10/16/16.
 */

public class ChooseYourPayment extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_choice);

        Button btnGoogle = (Button) findViewById(R.id.google);

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseYourPayment.this, GooglePayPage.class);
                startActivity(intent);
            }
        });



    }
}


