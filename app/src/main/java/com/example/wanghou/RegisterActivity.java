package com.example.wanghou;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docoor.DoctorMainActivity;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.PriorityQueue;
//要注意在相关bmbo的东西导入后，要发送消息就要用getApplicationContext()，单独开的activity直接get就行，如果是
//fragment要在父亲activity中先getApplicationContext()。然后在每个fragment里面getContext就行了。
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

import static java.lang.Thread.sleep;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText RegisterInput_phone;
    private EditText RegisterInput_yanzhengname;
    private EditText RegisterInput_name;
    private EditText RegisterInput_password;
    private TextView RegisterGet_yanzhengpasword;
    private TextView RegisterSubmit;
    private Boolean yanzhengma_successed=false;
    private ImageButton fanhui_8;
    private Boolean yunxu_fasong_yanzheng=true;
    private final Handler mHandler1 = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
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
                    //如果手机号存在被注册，提示注册，不存在直接获取验证码
                     if ("phone_exsit".equals((String)msg.obj)){
                         Toast.makeText(getApplicationContext(),"该手机号已被注册",Toast.LENGTH_SHORT).show();
                     }
                     if("phone_not_exsit".equals((String)msg.obj)){
                         yunxu_fasong_yanzheng=false;
                         CountDownTimer timer = new CountDownTimer(60000, 1000) {
                             @Override
                             public void onTick(long millisUntilFinished) {
                                 RegisterGet_yanzhengpasword.setEnabled(false);
                                 RegisterGet_yanzhengpasword.setText( millisUntilFinished / 1000 + "s重新获取");
                             }
                             @Override
                             public void onFinish() {
                                 RegisterGet_yanzhengpasword.setEnabled(true);
                                 RegisterGet_yanzhengpasword.setText("重新获取");
                                 yunxu_fasong_yanzheng=true;
                             }
                         }.start();
                         //发送验证码
                         BmobSMS.requestSMSCode(getApplicationContext(), RegisterInput_phone.getText().toString(), "wangsheng",new RequestSMSCodeListener() {
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
                case 2:
                    if ("register".equals((String)msg.obj)){
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
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏应用程序的标题栏，即当前activity的label
        setContentView(R.layout.activity_register);
        BmobSMS.initialize(getApplicationContext(), "1f26a6458de6fa489f2a39e67dfa603b");
        init();
    }
    public void init(){
        fanhui_8=(ImageButton)findViewById(R.id.fanhui_8);
        fanhui_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //EdiText不用设置监听直接获取数据就行了
        RegisterInput_phone=(EditText)findViewById(R.id.RegisterInput_phone);
        RegisterInput_yanzhengname=(EditText)findViewById(R.id.RegisterInput_yanzhengname);
        RegisterInput_name=(EditText)findViewById(R.id.RegisterInput_name);
        RegisterInput_password=(EditText)findViewById(R.id.RegisterInput_password);
        //点击获取验证码是将电话号码判定有效（若无效继续监听），若有效-则判定改号码是否注册，若注册则提示注册，没注册则发送短信验证码，然后用户将短信验证验证码填入
        RegisterGet_yanzhengpasword=(TextView)findViewById(R.id.RegisterGet_yanzhengpasword);
        RegisterGet_yanzhengpasword.setOnClickListener(this);
        //点击提交按钮，若昵称密码不符合要求提示出错，若符合，且短信验证码正确则可以注册
        RegisterSubmit=(TextView)findViewById(R.id.RegisterSubmit);
        RegisterSubmit.setOnClickListener(this);
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.RegisterGet_yanzhengpasword:
                if(yunxu_fasong_yanzheng==true)
                    just_phone_exeit();
                break;
            case R.id.RegisterSubmit:
                register();
                break;
        }
    }
    public void register(){
        if(RegisterInput_phone.getText().toString().length()!=11)
            Toast.makeText(getApplicationContext(),"输入有效的电话号码",Toast.LENGTH_SHORT).show();
        else{
            if ("".equals(RegisterInput_name.getText().toString())||"".equals(RegisterInput_password.getText().toString())){
                Toast.makeText(getApplicationContext(),"输入有效的昵称或密码",Toast.LENGTH_SHORT).show();
            }else{
                BmobSMS.verifySmsCode(getApplicationContext(), RegisterInput_phone.getText().toString(), RegisterInput_yanzhengname.getText().toString(), new VerifySMSCodeListener() {
                    @Override
                    public void done(BmobException ex) {// TODO Auto-generated method stub
                        if (ex == null) {//短信验证码已验证成功
                            Log.i("bmob", "验证通过");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Message msg = Message.obtain();
                                    try {
                                        HashMap<String,String> params = new HashMap<String, String>(3);
                                        params.put("state", "zhuce");
                                        params.put("phone",RegisterInput_phone.getText().toString());
                                        params.put("nichen",RegisterInput_name.getText().toString());
                                        params.put("password",RegisterInput_password.getText().toString());
                                        msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/Register_Login", params, "utf-8").SendDataByPostStart();
                                        RegisterSubmit.setText("注册成功，请稍后...");
                                    }catch (Exception e){
                                    }
                                    msg.what = 2;
                                    Log.e("返回了",(String)msg.obj);
                                    //数据封装到message
                                    mHandler.obtainMessage(2,(String)msg.obj).sendToTarget();
                                    mHandler.sendMessage(msg);
                                }
                            }).start();
                            //若出错一定要提示那哪里出错
                        } else {
                            Toast.makeText(getApplicationContext(),"验证码错误",Toast.LENGTH_SHORT).show();
                            Log.i("bmob", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                        }
                    }
                });
                //一切正常可以注册，连接数据库插入数据
            }   //else结束，即输入了有效的昵称和密码
        }//else结束，即输入了有效的电话号码

    }
    public void just_phone_exeit(){
        if(RegisterInput_phone.getText().toString().length()!=11) {
            Toast.makeText(getApplicationContext(),"请输入有效的手机号码",Toast.LENGTH_SHORT).show();
        }
        else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    try {
                        HashMap<String,String> params = new HashMap<String, String>(2);
                        params.put("state", "phone_exist_yes_or_not");
                        params.put("phone",RegisterInput_phone.getText().toString());
                        msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/Register_Login", params, "ISO8859_1").SendDataByPostStart();
                    }catch (Exception e){
                    }
                    msg.what = 1;
                    Log.e("返回了",(String)msg.obj);
                    //数据封装到message
                    mHandler.obtainMessage(1,(String)msg.obj).sendToTarget();
                    mHandler.sendMessage(msg);
                }
            }).start();
        }
    }
}
