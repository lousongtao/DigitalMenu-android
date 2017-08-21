package com.shuishou.digitalmenu.ui;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.shuishou.digitalmenu.InstantValue;
import com.shuishou.digitalmenu.R;
import com.shuishou.digitalmenu.io.IOOperator;
import com.shuishou.digitalmenu.uibean.ChoosedFood;

/**
 * Created by Administrator on 2017/7/21.
 */

public class MaintainDialog {

    private EditText txtConfirmCode;
    private EditText txtServerURL;
    private MainActivity mainActivity;

    private AlertDialog dlg;

    private Handler handler;

    private final static int MSG_REFRESHDATA = 1;
    private final static int MSG_SAVESERVERURL = 2;

    public MaintainDialog(@NonNull MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        initUI();
    }

    private void initUI(){
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.maintain_layout, null);
        //init TabHost
        TabHost host = (TabHost) view.findViewById(R.id.tabhost);
        host.setup();
        TabHost.TabSpec spec1 = host.newTabSpec("Refresh Data");
        spec1.setContent(R.id.tabRefreshMenu);
        spec1.setIndicator("Refresh Data");
        host.addTab(spec1);
        TabHost.TabSpec spec2 = host.newTabSpec("Save Server URL");
        spec2.setContent(R.id.tabServerURL);
        spec2.setIndicator("Save Server URL");
        host.addTab(spec2);

        txtConfirmCode = (EditText) view.findViewById(R.id.txtConfirmCode);
        txtServerURL = (EditText) view.findViewById(R.id.txtServerURL);
        Button btnSaveURL = (Button) view.findViewById(R.id.btnSaveServerURL);
        Button btnRefresh = (Button) view.findViewById(R.id.btnRefreshMenu);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRefresh();
            }
        });
        btnSaveURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSaveURL();
            }
        });

        loadServerURL();

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Maintain");
        builder.setIcon(R.drawable.info);
        builder.setNegativeButton("Close", null);
        builder.setView(view);
        dlg = builder.create();

        initHandler();
    }

    private void initHandler() {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_REFRESHDATA){

                } else if (msg.what == MSG_SAVESERVERURL){

                }
            }
        };
    }

    private void loadServerURL(){
        String url = IOOperator.loadServerURL();
        if (url != null)
            txtServerURL.setText(url);
    }

    private void doRefresh(){
        final String code = txtConfirmCode.getText().toString();
        if (code == null || code.length() == 0){
            Toast.makeText(mainActivity, "Please input confirm code.", Toast.LENGTH_LONG).show();
            return;
        }
        new Thread(){
            @Override
            public void run() {
                boolean bCheckCode = mainActivity.getHttpOperator().checkConfirmCodeSync(code);
                if (bCheckCode){
                    mainActivity.onRefreshData();
                    dlg.dismiss();
                }
            }
        }.start();

    }

    private void doSaveURL(){
        final String code = txtConfirmCode.getText().toString();
        if (code == null || code.length() == 0){
            Toast.makeText(mainActivity, "Please input confirm code.", Toast.LENGTH_LONG).show();
            return;
        }
        final String url = txtServerURL.getText().toString();
        if (url == null || url.length() == 0){
            Toast.makeText(mainActivity, "Please input server URL.", Toast.LENGTH_LONG).show();
            return;
        }

        if (code.equals("2017")){
            IOOperator.saveServerURL(url);
            InstantValue.URL_TOMCAT = url;
            dlg.dismiss();
        }
    }

    public void showDialog(){
        dlg.show();
    }

    public void dismiss(){
        dlg.dismiss();
    }
}
