package com.example.wanghou;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docoor.DoctorMainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import shezhi_information_doctor_patient.Patient_information;

//预约医生
@SuppressLint("ValidFragment")
public class LoginFragment_2 extends Fragment implements View.OnClickListener {
    private String information;
    private String content;
    private TextView getyan_password;
    private EditText telephone;
    private EditText yan_password;
    private TextView login;
    private Boolean yunxu_fasong_yanzheng=true;
    //医生不添加验证码登录，为安全考虑
    private Boolean yanzhengma_success=false;
    private final Handler mHandler1 = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent intent=new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    login.setText("登   录");
                    break;
            }
        }
    };
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if("phone_not_exsit".equals((String)msg.obj)){
                        Toast.makeText(getContext(),"该手机号未注册",Toast.LENGTH_SHORT).show();
                    }
                    if ("phone_exsit".equals((String)msg.obj)){
                        yunxu_fasong_yanzheng=false;
                        CountDownTimer timer = new CountDownTimer(60000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                getyan_password.setEnabled(false);
                                getyan_password.setText( millisUntilFinished / 1000 + "s重新获取");
                            }
                            @Override
                            public void onFinish() {
                                getyan_password.setEnabled(true);
                                getyan_password.setText("重新获取");
                                yunxu_fasong_yanzheng=true;
                            }
                        }.start();
                        //发送验证码
                        BmobSMS.initialize(getContext(), "1f26a6458de6fa489f2a39e67dfa603b");
                        BmobSMS.requestSMSCode(getContext(), telephone.getText().toString(), "wangsheng",new RequestSMSCodeListener() {
                            @Override
                            public void done(Integer smsId,BmobException ex) {
                                // TODO Auto-generated method stub
                                if(ex==null){//验证码发送成功
                                    Log.i("bmob", "短信id："+smsId);//用于查询本次短信发送详情
                                }
                            }
                        });
                    }
                    break;
            }
        }
    };
    public LoginFragment_2(String content) {
        this.content = content;
    }
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_2,container,false);
        telephone=(EditText)view.findViewById(R.id.telephone);
        //在Fragment里面只能设置Context只能用getConText
        //使用下面的操作方式，前提是注册，买短信，不用实名验证，设置引用名称，设置短信模板，
        //要在两个gradle里面添加东西，要遭mainfest里面添加权限http://doc.bmob.cn/data/android/#bmobsdk
        //获取比目云登录方式http://doc.bmob.cn/sms/android/index.html
        //比目云账号18856336193，密码是我的学号
       //点击验证码按钮获取验证码
        yan_password=(EditText)view.findViewById(R.id.yan_password);
        getyan_password=(TextView) view.findViewById(R.id.getyan_password);
        login=(TextView)view.findViewById(R.id.login);
        getyan_password.setOnClickListener(this);
        login.setOnClickListener(this);
        return view;
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.getyan_password:
                    if(yunxu_fasong_yanzheng==true){
                        if (telephone.getText().toString().length() != 11)
                            Toast.makeText(getContext(), "请输入有效的手机号码", Toast.LENGTH_SHORT).show();
                        else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Message msg = Message.obtain();
                                    try {
                                        HashMap<String, String> params = new HashMap<String, String>(2);
                                        params.put("state", "phone_exist_yes_or_not");
                                        params.put("phone", telephone.getText().toString());
                                        Patient_information patient_information=new Patient_information();
                                        Patient_information.user_phone=telephone.getText().toString();
                                        msg.obj = (String) new SendDataByPost("http://47.104.218.123:8080/wangluo/Register_Login", params, "ISO8859_1").SendDataByPostStart();
                                    } catch (Exception e) {
                                    }
                                    msg.what = 1;
                                    Log.e("返回了", (String) msg.obj);
                                    //数据封装到message
                                    mHandler.obtainMessage(1, (String) msg.obj).sendToTarget();
                                    mHandler.sendMessage(msg);
                                }
                            }).start();
                        }
                    }
                    break;
                case R.id.login:
                    if (telephone.getText().toString().length() != 11)
                        Toast.makeText(getContext(), "请输入有效的手机号码", Toast.LENGTH_SHORT).show();
                    if ("".equals(yan_password.getText().toString().length()))
                        Toast.makeText(getContext(), "请输入有效验证码", Toast.LENGTH_SHORT).show();
                    if (telephone.getText().toString().length() == 11 && !"".equals(yan_password.getText().toString().length())) {
                        BmobSMS.initialize(getContext(), "1f26a6458de6fa489f2a39e67dfa603b");
                        BmobSMS.verifySmsCode(getContext(), telephone.getText().toString(), yan_password.getText().toString(), new VerifySMSCodeListener() {
                            @Override
                            public void done(BmobException ex) {// TODO Auto-generated method stub
                                if (ex == null) {//短信验证码已验证成功
                                    Log.i("bmob", "验证通过");
                                    login.setText("登录成功，请稍后...");
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
                                    //若出错一定要提示那哪里出错
                                } else {
                                    Toast.makeText(getContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                                    Log.i("bmob", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                                }
                            }
                        });
                        break;
                    }
            }
        }
}
