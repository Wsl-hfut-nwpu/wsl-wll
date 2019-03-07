package com.example.wanghou;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;

import shezhi_information_doctor_patient.Patient_information;
public class MainFragment_5_pingfen extends AppCompatActivity {
    private TextView closed,submit_pingfen,doctor_name;
    private RatingBar doctor_attidute,doctor_shuiping;
    private String  doctor_name_string;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private ImageButton fanhui_6;
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    alert = null;
                    builder = new AlertDialog.Builder(MainFragment_5_pingfen.this);
                    alert = builder.setIcon(R.mipmap.quickly)
                            .setTitle("提示：")
                            .setMessage("   评价完毕")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alert.dismiss();
                                }
                            }).create();             //创建AlertDialog对象
                    alert.show();
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_5_pingfen);
        //导入医生姓名
        doctor_name_string=getIntent().getStringExtra("id");
        doctor_name=(TextView)findViewById(R.id.doctor_name);
        doctor_name.setText(doctor_name_string);

        fanhui_6=(ImageButton)findViewById(R.id.fanhui_6);
        fanhui_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        doctor_attidute=(RatingBar)findViewById(R.id.doctor_attidute);
        doctor_shuiping=(RatingBar)findViewById(R.id.doctor_shuiping);
        submit_pingfen=(TextView)findViewById(R.id.submit_pingfen);
        submit_pingfen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        HashMap<String,String> params = new HashMap<String, String>(3);
                        //获取预约查看图片标志位，查看医生是否提交报告
                        params.put("state", "pingfen");
                        params.put("patient_phone", Patient_information.user_phone);
                        params.put("doctor_attidute", String.valueOf(doctor_attidute.getRating()));
                        params.put("doctor_shuiping", String.valueOf(doctor_shuiping.getRating()));
                        try {
                            msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragment_5", params, "utf-8").SendDataByPostStart();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mHandler.obtainMessage(1,(String)msg.obj).sendToTarget();
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        });
    }
}
