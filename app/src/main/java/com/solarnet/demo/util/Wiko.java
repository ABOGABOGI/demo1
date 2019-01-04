package com.solarnet.demo.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.solarnet.demo.R;
import com.solarnet.demo.SplashScreen;

import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class Wiko extends AppCompatActivity {

    private static final String TAG = "TAG";
    TextView vsimstatus ;
    private AlertDialog alertDialog;
    private AlertDialog mDialog;
    private static final int NOT_NOTICE = 2;//如果勾选了不再询问
    private LocalSocketListenReceive mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiko);
        myRequetPermission();
        vsimstatus = (TextView)findViewById(R.id.Vsimstatus);
        vsimstatus.setText("VSIM Not Used");
        mReceiver = new LocalSocketListenReceive();
        IntentFilter filter = new IntentFilter();

        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(mReceiver, filter);
    }

    private void myRequetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }else {
            Toast.makeText(this,"您已经申请了权限!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {//选择了“始终允许”
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])){//用户选择了禁止不再询问

                        AlertDialog.Builder builder = new AlertDialog.Builder(Wiko.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (mDialog != null && mDialog.isShowing()) {
                                            mDialog.dismiss();
                                        }
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);//注意就是"package",不用改成自己的包名
                                        intent.setData(uri);
                                        startActivityForResult(intent, NOT_NOTICE);
                                    }
                                });
                        mDialog = builder.create();
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();



                    }else {//选择禁止
                        AlertDialog.Builder builder = new AlertDialog.Builder(Wiko.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (alertDialog != null && alertDialog.isShowing()) {
                                            alertDialog.dismiss();
                                        }
                                        ActivityCompat.requestPermissions(Wiko.this,
                                                new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                                    }
                                });
                        alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }

                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==NOT_NOTICE){
            myRequetPermission();//由于不知道是否选择了允许所以需要再次判断
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public boolean getVsimIsInSlot3BySubscriptionManager(int subid) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false;
        }
        List<SubscriptionInfo> list = SubscriptionManager.from(Wiko.this).getActiveSubscriptionInfoList();
        if (list == null) {
            return false;
        }
        for (SubscriptionInfo info : list) {
            if(info != null){
                Log.d(TAG, "subid-->" + subid);
                Log.d(TAG, "ICCID-->" + info.getIccId());
                Log.d(TAG, "subId-->" + info.getSubscriptionId());
                Log.d(TAG, "DisplayName-->" + info.getDisplayName());
                Log.d(TAG, "CarrierName-->" + info.getCarrierName());
                Log.d(TAG, "getSimSlotIndex()-->" + info.getSimSlotIndex());
                Log.d(TAG, "info.tostring" + info.toString());
                if ((info.getSimSlotIndex() == 2)&&(subid == info.getSubscriptionId())&&(info.getCarrierName().toString().contains("SKYROAM"))) {
                    return true;
                }
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int getDefaultDataSubId()
    {
        int subId = -1;
        SubscriptionManager mSubscriptionManager;
        mSubscriptionManager = SubscriptionManager.from(Wiko.this);
        subId = mSubscriptionManager.getDefaultDataSubscriptionId();

        return subId;
    }

    class LocalSocketListenReceive extends BroadcastReceiver {

        @TargetApi(Build.VERSION_CODES.N)
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);//TelephonyManager.from(context);
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                boolean isWifiConn = networkInfo.isConnected();
                Log.d(TAG, "isWifiConn " + isWifiConn);
                networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                boolean isMobileConn = networkInfo.isConnected();
                Log.d(TAG, "isMobileConn " + isMobileConn);
                if (isMobileConn) {
                    int dataState = tm.getDataState();
                    int subid = getDefaultDataSubId();
                    Log.d(TAG, " isMobileConn subid " + subid);
                    if(subid != -1)
                    {
                        if(getVsimIsInSlot3BySubscriptionManager(subid))
                        {
//                            Log.d(TAG, " R.string.vsim_used " );
                            vsimstatus.setText("VSIM Used");
                            Intent intentSplash = new Intent(Wiko.this,SplashScreen.class);
                            startActivity(intentSplash);
                        }
                        else
                        {
                            vsimstatus.setText("VSIM Not Used");
//                            Log.d(TAG, " R.string.vsim_not_used " );
                        }

                    }
                }
                else
                {
                    vsimstatus.setText("VSIM Not Used");
                }
                if(isWifiConn)
                {
                    vsimstatus.setText("VSIM Not Used");
                }

            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        //vsimstatus.setText(statusStr+text);
    }
}
