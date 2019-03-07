package com.example.wanghou;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.HashMap;
//Fragmrnt设计往往是一个界面里面有公共元素需要用到，或者界面里面的界面有公共元素也可以用到，Fragment说白了是界面“碎片”
//而完全跳转一个全新界面要用到Activity跳转。
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView qiehuan;
    private TextView zhuce;
    private TextView forget;
    private FrameLayout frameLayout;
    private FragmentManager fManager;
    private int qiehuan_bianhao=0;
    private LoginFragment_1 fg1;
    private LoginFragment_2 fg2;
    private byte []data;
    //数据传送允许放在主线程中，全部放在new的线程里面，线程new在你使用时随时随地，而Handler这个
    //接受Message的车只能按照下面格式书写作为每个类里面独立的函数。
    //handler以后就这样定义，线程直接在里面new就行，最后要记住添加.start
    private  Handler mHandler=new Handler(){
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                   // account.setText("habitable1");
                    break;
            }
        }
    };
    private HashMap<String, String> params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //开始界面和登录界面设置为全屏就行了，里面不设置全屏，只执行前端代码不设置标题就好。
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏应用程序的标题栏，即当前activity的label
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏
        setContentView(R.layout.activity_login);
        getApplicationContext();
        init();
        //调用前要注意先创建管理员
        fManager=getFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();

        //切换对象自带隐藏Fragment，展示Fragment，替换Fragment的函数。
        fg1 = new LoginFragment_1("第一个Fragment");
        fTransaction.add(R.id.add_qiehuan,fg1);
        fTransaction.commitAllowingStateLoss();
//                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
    }
    //获取各个有用控件，并且初始化
    public void init(){
        qiehuan=(TextView)findViewById(R.id.qiehuan);
        zhuce=(TextView)findViewById(R.id.zhuce);
        forget=(TextView)findViewById(R.id.forget);
        frameLayout=(FrameLayout)findViewById(R.id.add_qiehuan);
        qiehuan.setOnClickListener(this);
        zhuce.setOnClickListener(this);
        forget.setOnClickListener(this);
    }
    public void onClick(View v) {
        switch ( v.getId()){
            case R.id.qiehuan:
                addqiehuan(qiehuan_bianhao);
                break;
            case R.id.zhuce:
                Intent intent1=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent1);
                break;
            case R.id.forget:
                Intent intent2=new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent2);
                break;
        }
    }
    public void addqiehuan(int  qiehuan) {
        //调用Feagment管理者创建切换fragment对象
        //一次下面方法只能使用一次后面的commit，不然会出现闪退,处理闪退另一个做法是将commit替换为commitAllowingStateLoss
        //但是一个commit可以提交多个事物，如删除，添加，代替，隐藏，显示
        FragmentTransaction fTransaction = fManager.beginTransaction();
        //切换对象自带隐藏Fragment，展示Fragment，替换Fragment的函数。
        hideAllFragment(fTransaction);
        switch (qiehuan){
            case 0:
                if(fg1 == null){
                    fg1 = new LoginFragment_1("第一个Fragment");
                    fTransaction.add(R.id.add_qiehuan,fg1);
                }else{
                    fTransaction.show(fg1);
                }
                break;
            case 1:
                if(fg2 == null){
                    fg2 = new LoginFragment_2("第二个Fragment");
                    fTransaction.add(R.id.add_qiehuan,fg2);
                }else {
                    fTransaction.show(fg2);
                }
                break;
        }
        fTransaction.commitAllowingStateLoss();
        qiehuan=qiehuan+1;
        this.qiehuan_bianhao=qiehuan%2;
    }
    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fg1 != null)fragmentTransaction.hide(fg1);
        if(fg2 != null)fragmentTransaction.hide(fg2);
    }
}
