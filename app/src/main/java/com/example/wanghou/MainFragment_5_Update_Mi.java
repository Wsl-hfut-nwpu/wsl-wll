package com.example.wanghou;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docoor.BiaoZhi;

import java.util.HashMap;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

import static java.lang.Thread.sleep;
public class MainFragment_5_Update_Mi extends AppCompatActivity implements View.OnClickListener {
    private EditText ForgetPasswordPhone;
    private EditText ForgetPasswordPassword_one;
    private EditText ForgetPasswordPassword_two;
    private EditText ForgetPasswordInput_yanzhengname;
    private TextView ForgetPasswordGet_yanzhengpassword;
    private TextView ForgetPasswordSubmit;
    private Boolean yanzhengma_success = false;
    private ImageButton fanhui_5;
    private Boolean yunxu_fasong_yanzheng=true;
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if ("phone_not_exsit".equals((String) msg.obj)) {
                        Toast.makeText(getApplicationContext(), "该手机号未注册", Toast.LENGTH_SHORT).show();
                    }
                    if ("phone_exsit".equals((String) msg.obj)) {
                        yunxu_fasong_yanzheng=false;
                        CountDownTimer timer = new CountDownTimer(60000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                ForgetPasswordGet_yanzhengpassword.setEnabled(false);
                                ForgetPasswordGet_yanzhengpassword.setText( millisUntilFinished / 1000 + "s重新获取");
                            }
                            @Override
                            public void onFinish() {
                                ForgetPasswordGet_yanzhengpassword.setEnabled(true);
                                ForgetPasswordGet_yanzhengpassword.setText("重新获取");
                                yunxu_fasong_yanzheng=true;
                            }
                        }.start();
                        //发送验证码
                        BmobSMS.initialize(getApplicationContext(), "1f26a6458de6fa489f2a39e67dfa603b");
                        BmobSMS.requestSMSCode(getApplicationContext(), ForgetPasswordPhone.getText().toString(), "wangsheng", new RequestSMSCodeListener() {
                            @Override
                            public void done(Integer smsId, BmobException ex) {
                                // TODO Auto-generated method stub
                                if (ex == null) {//验证码发送成功
                                    Log.i("bmob", "短信id：" + smsId);//用于查询本次短信发送详情
                                }
                            }
                        });
                    }
                    break;
                case 2:
                    if ("OK".equals((String) msg.obj)) {
                        //修改成功跳转5界面
                        Intent intent = new Intent(MainFragment_5_Update_Mi.this, MainActivity.class);
                        intent.putExtra("id",5);
                        BiaoZhi biaoZhi=new BiaoZhi();
                        biaoZhi.Just=true;
                        startActivity(intent);
                        Toast.makeText(MainFragment_5_Update_Mi.this,"密码修改成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(getApplicationContext(), "网络延时请稍后", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_5__update__mi);
        BmobSMS.initialize(getApplicationContext(), "1f26a6458de6fa489f2a39e67dfa603b");
        init();
        fanhui_5=(ImageButton)findViewById(R.id.fanhui_5);
        fanhui_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void init() {
        ForgetPasswordPhone = (EditText) findViewById(R.id.ForgetPasswordPhone);
        ForgetPasswordInput_yanzhengname = (EditText) findViewById(R.id.ForgetPasswordInput_yanzhengname);
        ForgetPasswordPassword_one = (EditText) findViewById(R.id.ForgetPasswordPassword_one);
        ForgetPasswordPassword_two = (EditText) findViewById(R.id.ForgetPasswordPassword_two);
        ForgetPasswordGet_yanzhengpassword = (TextView) findViewById(R.id.ForgetPasswordGet_yanzhengpassword);
        ForgetPasswordSubmit = (TextView) findViewById(R.id.ForgetPasswordSubmit);
        ForgetPasswordGet_yanzhengpassword.setOnClickListener((View.OnClickListener) this);
        ForgetPasswordSubmit.setOnClickListener((View.OnClickListener) this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ForgetPasswordGet_yanzhengpassword:
                if(yunxu_fasong_yanzheng==true){
                    if (ForgetPasswordPhone.getText().toString().length() != 11)
                        Toast.makeText(getApplicationContext(), "请输入有效的手机号码", Toast.LENGTH_SHORT).show();
                    else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = Message.obtain();
                                try {
                                    HashMap<String, String> params = new HashMap<String, String>(2);
                                    params.put("state", "phone_exist_yes_or_not");
                                    params.put("phone", ForgetPasswordPhone.getText().toString());
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
            case R.id.ForgetPasswordSubmit:
                if(ForgetPasswordPhone.getText().toString().length()!=11)
                    Toast.makeText(getApplicationContext(),"请输入有效的手机号码",Toast.LENGTH_SHORT).show();
                else{
                    if("".equals(ForgetPasswordPassword_one.getText().toString())){
                        Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(!ForgetPasswordPassword_one.getText().toString().equals(ForgetPasswordPassword_two.getText().toString()))
                            Toast.makeText(getApplicationContext(),"两次密码不一致",Toast.LENGTH_SHORT).show();
                        else {
                            BmobSMS.verifySmsCode(getApplicationContext(), ForgetPasswordPhone.getText().toString(), ForgetPasswordInput_yanzhengname.getText().toString(), new VerifySMSCodeListener() {
                                @Override
                                public void done(BmobException ex) {// TODO Auto-generated method stub
                                    if (ex == null) {//短信验证码已验证成功
                                        Log.i("bmob", "验证通过");
                                        ForgetPasswordSubmit.setText("修改成功，请稍后...");
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Message msg = Message.obtain();
                                                try {
                                                    HashMap<String, String> params = new HashMap<String, String>(3);
                                                    params.put("state", "update_password");
                                                    params.put("phone", ForgetPasswordPhone.getText().toString());
                                                    params.put("password", ForgetPasswordPassword_one.getText().toString());
                                                    msg.obj = (String) new SendDataByPost("http://47.104.218.123:8080/wangluo/Register_Login", params, "ISO8859_1").SendDataByPostStart();
                                                } catch (Exception e) {
                                                }
                                                msg.what = 2;
                                                Log.e("返回了", (String) msg.obj);
                                                //数据封装到message
                                                mHandler.obtainMessage(1, (String) msg.obj).sendToTarget();
                                                mHandler.sendMessage(msg);
                                            }
                                        }).start();
                                        //若出错一定要提示那哪里出错
                                    } else {
                                        Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                                        Log.i("bmob", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                                    }
                                }
                            });
                        }
                    }
                }
                break;
        }
    }
}
