package com.example.wanghou;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;

import shezhi_information_doctor_patient.Patient_information;

@SuppressLint("ValidFragment")
public class MainFragment_5 extends Fragment {
    private String content;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private TextView patient_phone,shiming,update_mima,pingjia,suggest_fan,tuichu;
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    alert = null;
                    builder = new AlertDialog.Builder(getActivity());
                    alert = builder.setIcon(R.mipmap.quickly)
                            .setTitle("提示：")
                            .setMessage("   您已经实名认证过了")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alert.dismiss();
                                }
                            }).create();             //创建AlertDialog对象
                    alert.show();
                    break;
                case 2:
                    alert = null;
                    builder = new AlertDialog.Builder(getActivity());
                    alert = builder.setIcon(R.mipmap.quickly)
                            .setTitle("提示：")
                            .setMessage("   您本次预约医生已评分完毕")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alert.dismiss();
                                }
                            }).create();             //创建AlertDialog对象
                    alert.show();
                    break;
                case 3:
                    Intent intent=new Intent(getActivity(),MainFragment_5_pingfen.class);
                    intent.putExtra("id",(String)msg.obj);
                    startActivity(intent);
                    break;
            }
        }
    };
    public MainFragment_5(String content) {
        this.content = content;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_5,container,false);
        //病人电话
        patient_phone=(TextView)view.findViewById(R.id.patient_phone);
        patient_phone.setText(Patient_information.user_phone);
        //实名认证逻辑处理
        shiming=(TextView)view.findViewById(R.id.shiming);
        shiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先查询系统，判断该用户是否实名认证过
                //认证过提示已认证
                //未认证过跳转页面开始认证，认证结束后返回该页面
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        try {
                            HashMap<String,String> params = new HashMap<String, String>(2);
                            //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                            params.put("state", "panduan_patient_shiming");
                            params.put("patient_phone", Patient_information.user_phone);
                            msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragmnt_2", params, "utf-8").SendDataByPostStart();
                        }catch (Exception e){
                        }
                        if("patient_wei_shiming".equals((String)msg.obj)){
                            Intent intent = new Intent(getActivity(),MainFragment_5_ShiMing.class);
                            startActivity(intent);
                        }
                        if("patient_yi_shiming".equals((String)msg.obj)){
                            Log.e("返回了",(String)msg.obj);
                            //数据封装到message
                            mHandler.obtainMessage(1,(String)msg.obj).sendToTarget();
                            mHandler.sendMessage(msg);
                        }
                    }
                }).start();
            }
        });
        //更换密码逻辑处理
        update_mima=(TextView)view.findViewById(R.id.update_mima);
        update_mima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainFragment_5_Update_Mi.class);
                startActivity(intent);
            }
        });
        //评价系统逻辑护理
        pingjia=(TextView)view.findViewById(R.id.pingjia);
        pingjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先检查是否评过分
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        try {
                            HashMap<String,String> params = new HashMap<String, String>(3);
                            //获取预约查看图片标志位，查看医生是否提交报告
                            params.put("state", "is_or_pingfen");
                            params.put("patient_phone", Patient_information.user_phone);
                            msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragment_5", params, "utf-8").SendDataByPostStart();
                        }catch (Exception e){
                        }
                        if(!"yi_ping".equals((String)msg.obj)){
                            mHandler.obtainMessage(3,(String)msg.obj).sendToTarget();
                            mHandler.sendMessage(msg);
                            //连接数据库插入数据
                            //从数据库查看预约了哪个医生就给哪个医生评分
                        }
                        else{
                            Log.e("返回了",(String)msg.obj);
                            //数据封装到message
                            mHandler.obtainMessage(2,(String)msg.obj).sendToTarget();
                            mHandler.sendMessage(msg);
                        }
                    }
                }).start();
            }
        });
        //建议逻辑处理
        suggest_fan=(TextView)view.findViewById(R.id.suggest_fan);
        //退出登录逻辑处理
        tuichu=(TextView)view.findViewById(R.id.tuichu);
        tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //直接结束所有除了LoginActivity之外所有的Activity，漂亮
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}