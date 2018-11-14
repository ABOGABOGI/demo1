package com.solarnet.demo.activity.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.solarnet.demo.R;

public class LoanActivity extends AppCompatActivity implements View.OnClickListener {

    EditText inputnama,inputalamat,inputnohp,inputnominal;
    String nama,amount;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                nama = inputnama.getText().toString();
                amount = inputnominal.getText().toString();
                if (nama.equals("") || amount.equals("")){
                    Toast.makeText(this,"Masukan data yang sesuai",Toast.LENGTH_SHORT).show();
                }else{
                    sendData(nama,amount);
                }
                break;
        }

    }

    private void sendData(String nama, String amount) {
        Intent intent = new Intent(this,LoanConfirm.class);
        intent.putExtra("name",nama);
        intent.putExtra("amount",amount);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);
        inputnama = findViewById(R.id.edit_name);
        inputalamat = findViewById(R.id.edit_alamat);
        inputnohp = findViewById(R.id.edit_phone);
        inputnominal = findViewById(R.id.editAmounts);

        findViewById(R.id.btn_confirm).setOnClickListener(this);

    }
}
