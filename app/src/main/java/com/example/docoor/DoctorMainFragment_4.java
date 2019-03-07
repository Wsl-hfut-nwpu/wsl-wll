package com.example.docoor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.wanghou.Data_bean;
import com.example.wanghou.LoginActivity;
import com.example.wanghou.MainFragment_3_PingLun;
import com.example.wanghou.MainFragment_5_Update_Mi;
import com.example.wanghou.R;
import com.example.wanghou.SendDataByPost;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shezhi_information_doctor_patient.Doctor_information;
import shezhi_information_doctor_patient.Patient_information;

@SuppressLint("ValidFragment")
public class DoctorMainFragment_4 extends Fragment {
    private RatingBar doctor_attidute,doctor_shuiping;
    private String content;
    private TextView doctor_information,doctor_update_mima,doctor_pingjia,doctor_suggest_fan,doctor_tuichu;
    private TextView close_chafen;
    private View view; //整体的Fragment视图
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String a[]=((String)msg.obj).split(",");
                    builder = new AlertDialog.Builder(getActivity());
                    final LayoutInflater inflater1 = getActivity().getLayoutInflater();
                    View view_custom = inflater1.inflate(R.layout.doctor_main_fragment_4_information, null,false);
                    builder.setView(view_custom);
                    //电话11.密码16.姓名12，证件13，性别15，类型14
                    TextView sm11=(TextView)view_custom.findViewById(R.id.sm11);
                    sm11.setText(a[0]);
                    TextView sm16=(TextView)view_custom.findViewById(R.id.sm16);
                    sm16.setText(a[1]);
                    TextView sm12=(TextView)view_custom.findViewById(R.id.sm12);
                    sm12.setText(a[2]);
                    TextView sm13=(TextView)view_custom.findViewById(R.id.sm13);
                    sm13.setText(a[3]);
                    TextView sm15=(TextView)view_custom.findViewById(R.id.sm15);
                    sm15.setText(a[4]);
                    TextView sm14=(TextView)view_custom.findViewById(R.id.sm14);
                    sm14.setText(a[5]);
                    TextView closed=(TextView)view_custom.findViewById(R.id.closed);
                    closed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                        }
                    });
                    alert = builder.create();
                    //下面这句话意思是-外面的不可点击退去对话框
                    alert.setCanceledOnTouchOutside(true);
                    builder.setCancelable(false);
                    alert.show();
                    break;
                case 2:
                    String b[]=((String)msg.obj).split(",");
                    builder = new AlertDialog.Builder(getActivity());
                    final LayoutInflater inflater2 = getActivity().getLayoutInflater();
                    View view_custom1 = inflater2.inflate(R.layout.main_fragment_2_xianshi_fen, null,false);
                    close_chafen=(TextView)view_custom1.findViewById(R.id.close_chafen);
                    close_chafen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                        }
                    });
                    builder.setView(view_custom1);
                    alert = builder.create();
                    //下面这句话意思是-外面的不可点击退去对话框
                    alert.setCanceledOnTouchOutside(true);
                    builder.setCancelable(false);
                    doctor_attidute=(RatingBar)view_custom1.findViewById(R.id.doctor_attidute);
                    doctor_attidute.setRating(Float.parseFloat(b[0])/20);
                    doctor_shuiping=(RatingBar)view_custom1.findViewById(R.id.doctor_shuiping);
                    doctor_shuiping.setRating(Float.parseFloat(b[1])/20);
                    alert.show();
                    break;
            }
        }
    };
    public DoctorMainFragment_4(String content) {
        this.content = content;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctor_main_fragment_4,container,false);
        doctor_update_mima=(TextView)view.findViewById(R.id.doctor_update_mima);
        doctor_update_mima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求的是病人的界面系统，要注意bug
                Intent intent = new Intent(getActivity(),MainFragment_5_Update_Mi.class);
                startActivity(intent);
            }
        });
        doctor_pingjia=(TextView)view.findViewById(R.id.doctor_pingjia);
        doctor_pingjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        try {
                            HashMap<String,String> params = new HashMap<String, String>(3);
                            //获取预约查看图片标志位，查看医生是否提交报告
                            params.put("state", "doctor_get_fen");
                            params.put("doctor_phone", Doctor_information.user_phone);
                            msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/Docotr_MainFragment_4", params, "utf-8").SendDataByPostStart();
                        }catch (Exception e){
                        }
                        //数据封装到message
                        mHandler.obtainMessage(2,(String)msg.obj).sendToTarget();
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        });
        doctor_suggest_fan=(TextView)view.findViewById(R.id.doctor_suggest_fan);
        doctor_tuichu=(TextView)view.findViewById(R.id.doctor_tuichu);
        doctor_tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //直接结束所有除了LoginActivity之外所有的Activity，漂亮
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        doctor_information=(TextView)view.findViewById(R.id.doctor_information);
        doctor_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        try {
                            HashMap<String,String> params = new HashMap<String, String>(3);
                            //获取预约查看图片标志位，查看医生是否提交报告
                            params.put("state", "doctor_information");
                            params.put("doctor_phone", Doctor_information.user_phone);
                            msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/Docotr_MainFragment_4", params, "utf-8").SendDataByPostStart();
                        }catch (Exception e){
                        }
                        //数据封装到message
                        mHandler.obtainMessage(1,(String)msg.obj).sendToTarget();
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        });
        //返回整个Fragment显示
        return view;
    }
    public List<Map<String, Object>> buildListForSimpleAdapter(String information,Bitmap []bitmaps) {
        //将得到的informain进行二次解析
        Log.e("fanhui=",information);
        String[] zhongjian=information.split(";");
        String[] result;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(10);
        for (int i = 0; i < zhongjian.length; i++) {
            Map<String, Object> map = new HashMap<>(2);
            //    map.put("head_phone", "1");
            result=zhongjian[i].split("qa");
            map.put("dianhua", result[0]);
            map.put("describe_question", result[1]);
            map.put("answer_question", result[2]);
            map.put("riqi", result[3]);
            map.put("frist_picture",bitmaps[i]);
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