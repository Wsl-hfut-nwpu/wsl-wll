package com.example.docoor;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanghou.MainFragment_1_mingyi_introduce;
import com.example.wanghou.R;
import com.example.wanghou.SendDataByPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shezhi_information_doctor_patient.Doctor_information;

@SuppressLint("ValidFragment")
public class DoctorMainFragment_1 extends Fragment {
    private String content;
    private View view; //整体的Fragment视图
    private AnimationDrawable animationDrawable;
    private ListView doctor_mainfragment_1_list;
    private String information;
    private List listView;
    private List list;
    public DoctorMainFragment_1(String content) {
        this.content = content;
    }
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if("".equals((String) msg.obj))
                        Toast.makeText(getContext(), "没人预约您", Toast.LENGTH_SHORT).show();
                    else {
                        information = (String) msg.obj;
                        list = buildListForSimpleAdapter(information);
                        SimpleAdapter notes = new SimpleAdapter(getContext(), list, R.layout.doctor_main_fragment_1_1_use_adapter,
                                new String[]{"yuyue_bianhao", "yuyue_name", "yuyue_sex", "yuyue_age", "yuyue_type", "yuyue_buwei", "yuyue_xiangmu", "yuyue_time"},
                                new int[]{R.id.yuyue_bianhao, R.id.yuyue_name, R.id.yuyue_sex, R.id.yuyue_age, R.id.yuyue_type, R.id.yuyue_buwei, R.id.yuyue_xiangmu, R.id.yuyue_time});
                        doctor_mainfragment_1_list.setAdapter(notes);
                    }
                    break;
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctor_main_fragment_1,container,false);
        //展示Fragment_1的帧动画
        View show_ainimation = view.findViewById(R.id.show_animation_main_fragment_1);
        show_ainimation.setBackgroundResource(R.drawable.main_fragment_1_animation);
        animationDrawable = (AnimationDrawable)show_ainimation.getBackground();
        new Thread(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }}).start();
        doctor_mainfragment_1_list=(ListView)view.findViewById(R.id.doctor_mainfragment_1_list);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                try {
                    HashMap<String,String> params = new HashMap<String, String>(2);
                    //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                    params.put("state", "doctor_get_yuyue_people");
                    params.put("doctor_name",Doctor_information.user_phone);
                    msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/Doctor_MainFragment_1", params, "utf-8").SendDataByPostStart();
                }catch (Exception e){
                }
                msg.what = 1;
                Log.e("返回了",(String)msg.obj);
                //数据封装到message
                mHandler.obtainMessage(1,(String)msg.obj).sendToTarget();
                mHandler.sendMessage(msg);
            }
        }).start();
        //返回整个Fragment显示
        return view;
    }
    public List<Map<String, Object>> buildListForSimpleAdapter(String information) {
        //将得到的informain进行二次解析
        Log.e("fanhui=",information);
        String[] zhongjian=information.split(";");
        String[] result;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(10);
        for (int i = 0; i < zhongjian.length; i++) {
            Map<String, Object> map = new HashMap<>(2);
            //    map.put("head_phone", "1");
            result=zhongjian[i].split(",");
            map.put("yuyue_bianhao", result[0]);
            map.put("yuyue_name", result[1]);
            map.put("yuyue_sex", result[2]);
            map.put("yuyue_age", result[3]);
            map.put("yuyue_type", result[4]);
            map.put("yuyue_buwei", result[5]);
            map.put("yuyue_xiangmu", result[6]);
            map.put("yuyue_time", result[7]);
            list.add(map);
        }
        return list;
    }
}