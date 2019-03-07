package com.example.wanghou;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFragment_1_mingyi_introduce extends AppCompatActivity {
    private WebView introduce_mingyi_head;
    private TextView introduce_mingyi_information;
    private TextView introduce_mingyi_goodat;
    private TextView introduce_mingyi_rongyu;
    private TextView introduce_mingyi_jingli;
    private String position;
    private ImageButton fanhui;
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    buildListForSimpleAdapter((String)msg.obj);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_1_mingyi_introduce);
        position=getIntent().getStringExtra("position");
        introduce_mingyi_head=(WebView)findViewById(R.id.introduce_mingyi_head);
        introduce_mingyi_information=(TextView)findViewById(R.id.introduce_mingyi_information);
        introduce_mingyi_goodat=(TextView)findViewById(R.id.introduce_mingyi_goodat);
        introduce_mingyi_rongyu=(TextView)findViewById(R.id.introduce_mingyi_rongyu);
        introduce_mingyi_jingli=(TextView)findViewById(R.id.introduce_mingyi_jingli);
        TextView introduce_mingyi_jingli;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                try {
                    HashMap<String,String> params = new HashMap<String, String>(2);
                    //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                    params.put("gridView_list_introduce_id", position);
                    params.put("state","gridView_list_introduce");
                    msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_shouye", params, "utf-8").SendDataByPostStart();
                }catch (Exception e){
                }
                msg.what = 1;
                Log.e("返回了",(String)msg.obj);
                //数据封装到message
                mHandler.obtainMessage(1,(String)msg.obj).sendToTarget();
                mHandler.sendMessage(msg);
            }
        }).start();
        fanhui=(ImageButton)findViewById(R.id.fanhui) ;
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void buildListForSimpleAdapter(String information) {
        String[] zhongjian = information.split("qa");
        introduce_mingyi_head.getSettings().setJavaScriptEnabled(true);
        introduce_mingyi_head.setWebViewClient(new WebViewClient());
        introduce_mingyi_head.loadUrl("http://wangshengli.top:8080/wangluo/image/" + zhongjian[0]);
        introduce_mingyi_information.setText(zhongjian[1]);
        introduce_mingyi_goodat.setText(zhongjian[2]);
        introduce_mingyi_rongyu.setText(zhongjian[3]);
        introduce_mingyi_jingli.setText(zhongjian[4]);
    }
}
