package com.example.wanghou;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MainFragment_1_anli_introduce extends AppCompatActivity {
    private TextView zhengduan_neirong,anli_fenxi,riqi;
    private ImageView zhengduan_picture;
    private String position;
    private Bitmap bitmap;
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
        setContentView(R.layout.main_fragment_1_anli_introduce);
        zhengduan_neirong=(TextView)findViewById(R.id.zhengduan_neirong_1);
        anli_fenxi=(TextView)findViewById(R.id.anli_fenxi_1);
        riqi=(TextView)findViewById(R.id.riqi_1);
        zhengduan_picture=(ImageView) findViewById(R.id.zhengduan_picture_1);
        position=getIntent().getStringExtra("position");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                try {
                    HashMap<String,String> params = new HashMap<String, String>(2);
                    //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                    params.put("position", position);
                    params.put("state","get_anli_id");
                    msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_shouye", params, "utf-8").SendDataByPostStart();
                    bitmap=getBitmap("http://wangshengli.top:8080/wangluo/ListView/"+String.valueOf(Integer.parseInt(position)+1)+".jpg");
                }catch (Exception e){
                }
                Log.e("返回了",(String)msg.obj);
                //数据封装到message
                mHandler.obtainMessage(1,(String)msg.obj).sendToTarget();
                mHandler.sendMessage(msg);
            }
        }).start();
    fanhui=(ImageButton)findViewById(R.id.fanhui_1);
    fanhui.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
    }
    public void buildListForSimpleAdapter(String information) {
        String[] zhongjian = information.split("qa");
        zhengduan_neirong.setText(zhongjian[0]);
        anli_fenxi.setText(zhongjian[1]);
        zhengduan_picture.setImageBitmap(bitmap);
        riqi.setText(zhongjian[2]);
    }
    public Bitmap getBitmap(String imageUrl){
        Bitmap mBitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            mBitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mBitmap;
    }
}
