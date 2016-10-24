package humber.thunderbolts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PaymentScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screen);

        Button btnAddPay = (Button) findViewById(R.id.add_payment);

        btnAddPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Intent intent = new Intent(PaymentScreen.this, ChooseYourPayment.class);
               // startActivity(intent);
            }
        });


    }
}
