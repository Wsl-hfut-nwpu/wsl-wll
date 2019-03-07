package com.example.wanghou;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docoor.DoctorMainActivity;

import java.util.HashMap;

import shezhi_information_doctor_patient.Doctor_information;
import shezhi_information_doctor_patient.Patient_information;

import static java.lang.Thread.sleep;

//预约医生
@SuppressLint("ValidFragment")
public class LoginFragment_1 extends Fragment implements View.OnClickListener {
    private String information;
    private String content;
    private EditText telephone;
    private RadioGroup bingren_doctor;
    private RadioButton bingren;
    private RadioButton doctor;
    private EditText password;
    private TextView login;
    private Boolean point_group=false;    //判断是否选择了按钮
    private int point_group_int=1;       //判断选择了那个按钮
    public ProgressDialog pdialog;

    private final Handler mHandler1 = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent intent=new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    pdialog.dismiss();
                    break;
                case 2:
                    Intent intent1=new Intent(getActivity(),DoctorMainActivity.class);
                    startActivity(intent1);
                    pdialog.dismiss();
                    break;
            }
        }
    };
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //出错原因success写错
                    if("login_success".equals((String)msg.obj)){
                        //利用循环实现延时，不通过线程设定睡眠延时
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message msg1 = Message.obtain();
                                msg1.obj="asd";
                                mHandler1.obtainMessage(1,(String)msg1.obj).sendToTarget();
                                mHandler1.sendMessage(msg1);
                            }
                        }).start();
                        //在Fragment里面只能通过getActivity实现跳转
                    }
                    if("login_false".equals((String)msg.obj)){
                        Toast.makeText(getContext(),"手机号或密码错误",Toast.LENGTH_SHORT).show();
                        pdialog.dismiss();
                    }
                    break;
                case 2:
                    //出错原因success写错
                    if("login_success".equals((String)msg.obj)){
                        //利用循环实现延时，不通过线程设定睡眠延时
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message msg1 = Message.obtain();
                                msg1.obj="asd";
                                mHandler1.obtainMessage(2,(String)msg1.obj).sendToTarget();
                                mHandler1.sendMessage(msg1);
                            }
                        }).start();
                        //在Fragment里面只能通过getActivity实现跳转
                    }
                    if("login_false".equals((String)msg.obj)){
                        Toast.makeText(getContext(),"手机号或密码错误",Toast.LENGTH_SHORT).show();
                        pdialog.dismiss();
                    }
                    break;
            }
        }
    };
    public LoginFragment_1(String content) {
        this.content = content;
    }
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_1,container,false);
        bingren_doctor=(RadioGroup)view.findViewById(R.id.bingren_doctor);
        bingren=(RadioButton)view.findViewById(R.id.bingren);
        doctor=(RadioButton)view.findViewById(R.id.doctor);
        bingren_doctor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            //初始化是数字几就在开始选择默认几
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(bingren.getId()==checkedId){
                    point_group_int=1;
                }
                if(doctor.getId()==checkedId){
                    point_group_int=2;
                }
                point_group=true;
            }
        });
        telephone=(EditText)view.findViewById(R.id.telephone);
        password=(EditText)view.findViewById(R.id.password);
        login=(TextView)view.findViewById(R.id.login);
        login.setOnClickListener(this);
        //返回整个Fragment显示
        return view;
    }
    @TargetApi(Build.VERSION_CODES.M)
    public void onClick(View v){
        if(telephone.getText().toString().length()!=11)
            Toast.makeText(getContext(),"请输入有效的手机号码",Toast.LENGTH_SHORT).show();
        else{
            if("".equals(password.getText().toString()))
                Toast.makeText(getContext(),"请输入有效的密码",Toast.LENGTH_SHORT).show();
            else{
                if(telephone.getText().toString().length()==11 &&!"".equals(password.getText().toString())) {
                    if(point_group_int==1)
                        //对话框不可以创建在线程里面
                        pdialog = ProgressDialog.show(getActivity(), "       正在加载...", "\n\n\n");
                    else pdialog = ProgressDialog.show(getActivity(), "医生系统正在加载...", "\n\n\n");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            try {
                                HashMap<String,String> params = new HashMap<String, String>(3);
                                params.put("state", "login");
                                params.put("phone",telephone.getText().toString());
                                params.put("password",password.getText().toString());
                                params.put("bingren_doctor",String.valueOf(point_group_int));
                                //用户名必须先开辟类在调用才可以
                                if(point_group_int==1) {
                                    Patient_information patient_information = new Patient_information();
                                    Patient_information.user_phone = telephone.getText().toString();
                                }
                                if(point_group_int==2) {
                                    Doctor_information patient_information = new Doctor_information();
                                    Doctor_information.user_phone = telephone.getText().toString();
                                }
                                msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/Register_Login", params, "utf-8").SendDataByPostStart();
                            }catch (Exception e){
                            }
                            //初始化标志位
                            msg.what=point_group_int;
                            point_group=false;
                            //数据封装到message
                            mHandler.obtainMessage(point_group_int,(String)msg.obj).sendToTarget();
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        }
    }
}
