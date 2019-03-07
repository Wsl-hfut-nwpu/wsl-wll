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
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shezhi_information_doctor_patient.Patient_information;

public class MainFragment_3_PingLun extends AppCompatActivity {
    private LinearLayout linearLayout;
    private List list;
    private ListView add_pinglun_list;
    private String position;
    private EditText neirong;
    private Button submit_neirong;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private ImageButton fanhui_2;
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    buildListForSimpleAdapter((String)msg.obj);
                    break;
                case 2:
                    if ("submit_succeed".equals((String)msg.obj)){
                        //如果数据上传成功，重新携带position跳转到本页面，显示刷新数据
                        Intent intent = new Intent(MainFragment_3_PingLun.this,MainFragment_3_PingLun.class);
                        intent.putExtra("position",String.valueOf(position));
                        startActivity(intent);
                        finish();
                    }
                    if("submit_failed".equals((String)msg.obj)){
                        Toast.makeText(getApplicationContext(),"评论发送失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    private void buildListForSimpleAdapter(String information) {
        //将得到的informain进行二次解析
        Log.e("fanhui=", information);
        String[] result=information.split("qa");
        //在Activity中导入行的布局用这个，context不能瞎用，动态添加和Fragment不同，具体看MainFragment_1添加
        //这里用Fragment那里的方法无法加载数据，所以以后要注意
            View view = View.inflate(this, R.layout.main_fragment_3_ping_lun_show, null);
            TextView line_2_is_phone = (TextView) view.findViewById(R.id.line_2);
            line_2_is_phone.setText(result[0]);
            TextView line_3_is_question = (TextView) view.findViewById(R.id.line_3);
            line_3_is_question.setText("问题描述: " + result[1]);
            TextView line_4_is_answer = (TextView) view.findViewById(R.id.line_4);
            line_4_is_answer.setText("医院解答: " + result[2]);
            TextView line_6_is_riqi = (TextView) view.findViewById(R.id.line_7);
            line_6_is_riqi.setText(result[3]);
            ImageView line_4_is_frist_picture = (ImageView) view.findViewById(R.id.line_5);
            line_4_is_frist_picture.setImageBitmap(bitmap1);
            //返回的result[3]先不用
            //直接上传图片文件太大，需要在手机剪切后上传
            ImageView line_5_is_second_picture = (ImageView) view.findViewById(R.id.line_6);
            line_5_is_second_picture.setImageBitmap(bitmap2);
            ImageView line_5_is_third_picture = (ImageView) view.findViewById(R.id.line_9);
            line_5_is_third_picture.setImageBitmap(bitmap3);
            Log.e("result[7]=",result[7]);
            add_pinglun_list = (ListView) view.findViewById(R.id.add_pinglun_list);
            //获取到的是字符串null而不是空指针null
            if(!"null".equals(result[7])){
                list = buildListForSimpleAdapter_really(result[7]);
                SimpleAdapter notes = new SimpleAdapter(this, list, R.layout.main_fragment_3_pinglun_list, new String[]{"pinglun_de_dianhua", "pinglun_de_neirong"}, new int[]{R.id.pinglun_de_dianhua, R.id.pinglun_de_neirong});
                add_pinglun_list.setAdapter(notes);
                //添加addView之前要使用下面语句，就不会出现重复添加，只显示你想要的。
            }
            linearLayout.removeAllViews();
            linearLayout.addView(view);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_3__ping_lun);
        position=getIntent().getStringExtra("position");
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                try {
                    HashMap<String,String> params = new HashMap<String, String>(2);
                    //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                    params.put("get_pinglun_id_result", position);
                    params.put("state","get_pinglun_id");
                    msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragmnt_3", params, "ISO8859_1").SendDataByPostStart();
                    String data=(String) msg.obj;
                    String[] result=data.split("qa");
                    bitmap1=getBitmap("http://wangshengli.top:8080/wangluo/mainfragment_3/"+result[4]);
                    bitmap2=getBitmap("http://wangshengli.top:8080/wangluo/mainfragment_3/"+result[5]);
                    bitmap3=getBitmap("http://wangshengli.top:8080/wangluo/mainfragment_3/"+result[6]);
                }catch (Exception e){
                }
                msg.what = 1;
                Log.e("返回了",(String)msg.obj);
                //数据封装到message
                mHandler.obtainMessage(1,(String)msg.obj).sendToTarget();
                mHandler.sendMessage(msg);
            }
        }).start();
        neirong=(EditText)findViewById(R.id.neirong);
        submit_neirong=(Button) findViewById(R.id.submit_neirong);
        submit_neirong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(neirong.getText().toString())){
                    //提示输入内容后才能点击提交按钮
                    Toast.makeText(getApplicationContext(),"请输入内容后在发送",Toast.LENGTH_SHORT).show();
                }
                if(!"".equals(neirong.getText().toString())){
                    //获取里面内容发送数据给数据库
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            try {
                                HashMap<String,String> params = new HashMap<String, String>(2);
                                //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                                Patient_information patient_information=new Patient_information();
                                Log.e("插入=","aq"+Patient_information.user_phone+"qb"+neirong.getText().toString());
                                params.put("get_pinglun_id_neirong","aq"+Patient_information.user_phone+"qb"+neirong.getText().toString());
                                params.put("state","update_pinglun_neirong");
                                params.put("position",position);
                                msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragmnt_3", params, "utf-8").SendDataByPostStart();
                            }catch (Exception e){
                            }
                            msg.what = 2;
                            Log.e("返回了",(String)msg.obj);
                            //数据封装到message
                            mHandler.obtainMessage(2,(String)msg.obj).sendToTarget();
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
        fanhui_2=(ImageButton)findViewById(R.id.fanhui_2);
        fanhui_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public List<Map<String, Object>> buildListForSimpleAdapter_really(String information) {
        //将得到的informain进行二次解析
        String[] zhongjian=information.split("aq");
        String[] result;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
        for (int i = 0; i < zhongjian.length; i++) {
            Map<String, Object> map = new HashMap<>(2);
            //    map.put("head_phone", "1");
            result=zhongjian[i].split("qb");
            map.put("pinglun_de_dianhua", result[0]);
            map.put("pinglun_de_neirong", result[1]);
            list.add(map);
        }
        return list;
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
