package com.example.docoor;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanghou.Data_bean;
import com.example.wanghou.R;
import com.example.wanghou.SendDataByPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shezhi_information_doctor_patient.Doctor_information;

@SuppressLint("ValidFragment")
public class DoctorMainFragment_3 extends Fragment {
    private ListView listView;
    private List list;
    private String content;
    private TextView yuType,yuDoctor,yuBuwei,yuTime,submit;
    private View view; //整体的Fragment视图
    private AnimationDrawable animationDrawable;
    private   int position1;
    private TextView chuansong_dianhuahaoma;
    private Boolean is_not_submit=false;
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if("".equals((String)msg.obj)){
                        Toast.makeText(getContext(), "预约您的病人检查结果没出来", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        list = buildListForSimpleAdapter((String) msg.obj);
                        SimpleAdapter notes = new SimpleAdapter(getContext(), list, R.layout.doctor_main_fragment_3_1_use_adapter, new String[]{"dianhuahaoma", "xingming"}, new int[]{R.id.dianhuahaoma, R.id.xingming});
                        listView.setAdapter(notes);
                    }
                    break;
                case 2:
                    if("wei_submit".equals((String)msg.obj)){
                        is_not_submit=false;
                    }
                    if("yi_submit".equals((String)msg.obj)){
                        is_not_submit=true;
                    }
                    break;
            }
        }
    };
    public DoctorMainFragment_3(String content) {
        this.content = content;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctor_main_fragment_3,container,false);
        listView=(ListView)view.findViewById(R.id.listview_yingxiang);
        //凡是和网络相关的数据传送，都必须开辟线程实现，不能再主线程处理，除了WEb view直接加载，像下面的下载的环节只能在线程里面处理
        new Thread(new Runnable() {
            @Override
            public void run() {
                Data_bean data_bean=new Data_bean();
                Message msg = Message.obtain();
                try {
                    //首先查询影像字段是否上传照片，如果上传，则显示该预约人的报告提交按钮。
                    HashMap<String,String> params = new HashMap<String, String>(2);
                    params.put("state", "find_jiancha_cunzi");
                    params.put("doctor_phone", Doctor_information.user_phone);
                    msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/Docotr_MainFragment_3", params, "utf-8").SendDataByPostStart();
                }catch (Exception e){

                }
                msg.what = 1;
                Log.e("返回了",(String)msg.obj);
                //数据封装到message
                 mHandler.obtainMessage(1,msg.obj).sendToTarget();
                mHandler.sendMessage(msg);
            }
        }).start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //            //重要的是view是该item视图，能通过该view获取个个控件的id然后操作， position就是位置
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chuansong_dianhuahaoma=(TextView) view.findViewById(R.id.dianhuahaoma);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        try {
                            HashMap<String,String> params = new HashMap<String, String>(2);
                            //先将数据库有的不能提交的默认
                            params.put("state", "doctor_submit_is_submit");
                            params.put("doctor_phone", Doctor_information.user_phone);
                            params.put("patient_phone",chuansong_dianhuahaoma.getText().toString() );
                            //这里出错，原因估计是下面的这句话写错了，然后复制了其他对的地方一句话到下面，就发送成功。
                            msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/Docotr_MainFragment_3", params, "utf-8").SendDataByPostStart();
                        }catch (Exception e){
                        }
                        msg.what = 2;
                        Log.e("返回了",(String)msg.obj);
                        //数据封装到message
                        mHandler.obtainMessage(2,(String)msg.obj).sendToTarget();
                        mHandler.sendMessage(msg);
                    }
                }).start();
                view.findViewById(R.id.textView_chakan).setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        if(is_not_submit==false)
                            Toast.makeText(getContext(), "该病人报告未提交,请提交后查看", Toast.LENGTH_SHORT).show();
                        if(is_not_submit==true){
                            Intent intent= new Intent(getActivity(),DoctorMainFragment_3_show_baogao.class);
                            intent.putExtra("position_dianhua",chuansong_dianhuahaoma.getText().toString());
                            startActivity(intent);
                        }
                    }
                });
                view.findViewById(R.id.textView_tijiao).setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        if(is_not_submit==true)
                            Toast.makeText(getContext(), "该病人报告已提交,不可提交重复提交", Toast.LENGTH_SHORT).show();
                        if(is_not_submit==false){
                            Intent intent = new Intent(getActivity(),DoctorMainFragment_3_submit_baogao.class);
                            intent.putExtra("position_dianhua",chuansong_dianhuahaoma.getText().toString());
                            startActivity(intent);
                        }
                    }
                });
            };
        });
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
            map.put("dianhuahaoma", result[0]);
            map.put("xingming", result[1]);
            list.add(map);
        }
        return list;
    }
}