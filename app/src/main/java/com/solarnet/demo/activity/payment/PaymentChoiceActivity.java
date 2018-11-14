package com.solarnet.demo.activity.payment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.solarnet.demo.R;
import com.solarnet.demo.activity.TrxActivity;
import com.solarnet.demo.adapter.SlidingImage_Adapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import xbizvej.vg.id.ppob.PPOBController;

public class PaymentChoiceActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout btn_pbb;
    RelativeLayout btn_pulsa;
    RelativeLayout btn_pulsa_prepaid;
    RelativeLayout btn_telkom;
    RelativeLayout btn_pdam;
    RelativeLayout btn_pln_prepaid;
    RelativeLayout btn_pln_pascabayar;
    RelativeLayout btn_multifinance;
    RelativeLayout btn_tv;
    RelativeLayout btn_pln_nontaglis;
    RelativeLayout btn_asuransi;
    RelativeLayout btn_kartu_kredit;
    RelativeLayout btn_remittance_c2a;
    RelativeLayout btn_remittance_c2c;
    RelativeLayout btn_remittance_c2c_cashout;
    PPOBController ppobController;

    String name;


    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.lion_sample,R.drawable.lion_sample,R.drawable.lion_sample,R.drawable.lion_sample};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_choice);
        ppobController = new PPOBController();
        btn_pbb = findViewById(R.id.btn_pbb);
        btn_pulsa = findViewById(R.id.btn_pulsa);
        btn_pulsa_prepaid = findViewById(R.id.btn_pulsa_prepaid);
        btn_telkom = findViewById(R.id.btn_telkom);
        btn_pdam = findViewById(R.id.btn_pdam);
        btn_pln_prepaid = findViewById(R.id.btn_pln_prepaid);
        btn_pln_pascabayar = findViewById(R.id.btn_pln_pascabayar);
        btn_multifinance = findViewById(R.id.btn_multifinance);
        btn_tv = findViewById(R.id.btn_tv);
        btn_pln_nontaglis = findViewById(R.id.btn_pln_nontaglis);
        btn_asuransi = findViewById(R.id.btn_asuransi);
        btn_kartu_kredit = findViewById(R.id.btn_kartu_kredit);
        btn_remittance_c2a = findViewById(R.id.btn_remittance_c2a);
        btn_remittance_c2c = findViewById(R.id.btn_remittance_c2c);
        btn_remittance_c2c_cashout = findViewById(R.id.btn_remittance_c2c_cashout);

        btn_pbb.setOnClickListener(this);
        btn_pulsa.setOnClickListener(this);
        btn_pulsa_prepaid.setOnClickListener(this);
        btn_telkom.setOnClickListener(this);
        btn_pdam.setOnClickListener(this);
        btn_pln_prepaid.setOnClickListener(this);
        btn_pln_pascabayar.setOnClickListener(this);
        btn_multifinance.setOnClickListener(this);
        btn_tv.setOnClickListener(this);
        btn_pln_nontaglis.setOnClickListener(this);
        btn_asuransi.setOnClickListener(this);
        btn_kartu_kredit.setOnClickListener(this);
        btn_remittance_c2a.setOnClickListener(this);
        btn_remittance_c2c.setOnClickListener(this);
        btn_remittance_c2c_cashout.setOnClickListener(this);



        init();

    }

    private void init() {
        for(int i=0;i<IMAGES.length;i++)
        ImagesArray.add(IMAGES[i]);

        mPager = findViewById(R.id.digital_pager);


        mPager.setAdapter(new SlidingImage_Adapter(PaymentChoiceActivity.this,ImagesArray));




        CirclePageIndicator indicator =
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(5 * density);

        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_pulsa_prepaid:
                PPOBController ppobController = new PPOBController();
                ppobController.openPage(PPOBController.PAGE_PULSA,PaymentChoiceActivity.this);
                break;

            case R.id.btn_telkom:
                PPOBController ppobController1 = new PPOBController();
                ppobController1.openPage(PPOBController.PAGE_TELKOM,PaymentChoiceActivity.this);
                break;

            case R.id.btn_tv:
                PPOBController ppobController2 = new PPOBController();
                ppobController2.openPage(PPOBController.PAGE_TVBERLANGGANAN,PaymentChoiceActivity.this);
                break;

            case R.id.btn_multifinance:
                PPOBController ppobController3 = new PPOBController();
                ppobController3.openPage(PPOBController.PAGE_MULTIFINANCE,PaymentChoiceActivity.this);
                break;
            case R.id.btn_kartu_kredit:
                PPOBController ppobController4 = new PPOBController();
                ppobController4.openPage(PPOBController.PAGE_CREDITCARD,PaymentChoiceActivity.this);
                break;
            case R.id.btn_asuransi:
                PPOBController ppobController5 = new PPOBController();
                ppobController5.openPage(PPOBController.PAGE_ASURANSI,PaymentChoiceActivity.this);
                break;
            case R.id.btn_pdam:
                PPOBController ppobController6 = new PPOBController();
                ppobController6.openPage(PPOBController.PAGE_PDAM,PaymentChoiceActivity.this);
                break;
            case R.id.btn_pln_prepaid:
                PPOBController ppobController7 = new PPOBController();
                ppobController7.openPage(PPOBController.PAGE_PLNPREPAID,PaymentChoiceActivity.this);
                break;
            case R.id.btn_pln_pascabayar:
                PPOBController ppobController8 = new PPOBController();
                ppobController8.openPage(PPOBController.PAGE_PLNPOSTPAID,PaymentChoiceActivity.this);
                break;
            case R.id.btn_pbb:
                PPOBController ppobController9 = new PPOBController();
                ppobController9.openPage(PPOBController.PAGE_PBB,PaymentChoiceActivity.this);
                break;
            case R.id.btn_pulsa:
                PPOBController ppobController10 = new PPOBController();
                ppobController10.openPage(PPOBController.PAGE_PULSA,PaymentChoiceActivity.this);
                break;
            default:
                break;
        }
    }



}
