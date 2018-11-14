package com.solarnet.demo.activity.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.solarnet.demo.MainActivity;
import com.solarnet.demo.R;
import com.solarnet.demo.fragment.PaymentFragment;

public class LoanConfirm extends AppCompatActivity implements View.OnClickListener {

    TextView messagesText,textAmmount;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm_buttons :
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_loan_confirm);
        messagesText = findViewById(R.id.textMessages);
        textAmmount = findViewById(R.id.textAmounts);
        findViewById(R.id.confirm_buttons).setOnClickListener(this);
        messagesText.setText("Peminjaman atas nama "+getIntent().getStringExtra("name")+" berhasil dilakukan");
        textAmmount.setText("Rp. "+getIntent().getStringExtra("amount"));
    }


}
