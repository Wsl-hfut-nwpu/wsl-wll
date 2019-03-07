package com.example.wanghou;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

@SuppressLint("ValidFragment")
public class MainFragment_4 extends Fragment {
    private String content;
    private TextView yuType,yuDoctor,yuBuwei,yuTime,submit;
    private View view; //整体的Fragment视图
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView7;
    private TextView textView8;
    private TextView textView9;
    private TextView textView10;
    private TextView textView11;
    private TextView textView12;
    private TextView textView13;
    private TextView textView14;
    private TextView textView15;
    private TextView textView16;
    private TextView textView17;
    private TextView textView18;
    private TextView textView19;
    private TextView textView20;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private TextView chakan_yingxiang;
    public ProgressDialog pdialog;
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if("wei_submit".equals((String)msg.obj)){
                        alert = null;
                        builder = new AlertDialog.Builder(getContext());
                        alert = builder.setIcon(R.mipmap.quickly)
                                .setTitle("提示：")
                                .setMessage("   医院还未提交报告")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alert.dismiss();
                                    }
                                }).create();
                        alert.show();
                    }
                    else{
                        buildListForSimpleAdapter((String)msg.obj);
                    }
                    break;
                case 2:
                        pdialog.dismiss();
                        alert = null;
                        builder = new AlertDialog.Builder(getContext());
                        alert = builder.setIcon(R.mipmap.quickly)
                                .setTitle("提示：")
                                .setMessage("   医院还未提交影像")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alert.dismiss();
                                    }
                                }).create();
                        alert.show();
                    break;
                case 3:
                    pdialog.dismiss();
                    Data_bean obj=(Data_bean) msg.obj;
                    builder = new AlertDialog.Builder(getActivity());
                    final LayoutInflater inflater1 = getActivity().getLayoutInflater();
                    View view_custom = inflater1.inflate(R.layout.main_fragment_4_1_use_dialog, null,false);
                    builder.setView(view_custom);
                    alert = builder.create();
                    //下面这句话意思是-外面的不可点击退去对话框
                    alert.setCanceledOnTouchOutside(true);
                    builder.setCancelable(false);
                    alert.show();
                    ImageView imageView1=(ImageView)view_custom.findViewById(R.id.yingxiang_1);
                    imageView1.setImageBitmap(obj.bitmaps[0]);
                    ImageView imageView2=(ImageView)view_custom.findViewById(R.id.yingxiang_2);
                    imageView2.setImageBitmap(obj.bitmaps[1]);
                    ImageView imageView3=(ImageView)view_custom.findViewById(R.id.yingxiang_3);
                    imageView3.setImageBitmap(obj.bitmaps[2]);
                    TextView btn_cancle=view_custom.findViewById(R.id.close);
                    btn_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                        }
                    });
                    break;
            }
        }
    };
    public MainFragment_4(String content) {
        this.content = content;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment_4,container,false);
        //忘记调用该函数，没有初始化，所以出错
        init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                try {
                    HashMap<String,String> params = new HashMap<String, String>(3);
                    //获取预约查看图片标志位，查看医生是否提交报告
                    params.put("state", "doctor_submit_is_submit");
                    params.put("patient_phone", Patient_information.user_phone);
                    msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragment_4", params, "utf-8").SendDataByPostStart();
                }catch (Exception e){
                }
                    Log.e("返回了",(String)msg.obj);
                    //数据封装到message
                    mHandler.obtainMessage(1,(String)msg.obj).sendToTarget();
                    mHandler.sendMessage(msg);
            }
        }).start();
        chakan_yingxiang=(TextView)view.findViewById(R.id.chakan_yingxiang);
        chakan_yingxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdialog = ProgressDialog.show(getActivity(), "     影像正在加载...", "\n\n\n");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        Data_bean data_bean=new Data_bean();
                        try {
                            HashMap<String,String> params = new HashMap<String, String>(3);
                            //获取预约查看图片标志位，查看医生是否提交报告
                            params.put("state", "yingxiang_shangchuan");
                            params.put("patient_phone", Patient_information.user_phone);
                            data_bean.bitmap_data=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragment_4", params, "utf-8").SendDataByPostStart();
                        }catch (Exception e){
                        }
                        //数据此时放在data_bean里面，就不能再和msg比较了，要注意
                        if(!"yingxiang_wei_shangchuan".equals(data_bean.bitmap_data)){
                            String s[]=data_bean.bitmap_data.split(";");
                            data_bean.bitmaps=data_bean.get(s.length);
                            for(int i=0;i<s.length;i++){
                                data_bean.bitmaps[i]=getBitmap("http://wangshengli.top:8080/wangluo/All_patient_yingxiang/"+s[i]);
                            }
                            msg.what = 3;
                            msg.obj=data_bean;
                            mHandler.sendMessage(msg);
                        }else{
                            //数据封装到message
                            msg.obj="asda";
                            mHandler.obtainMessage(2,(String)msg.obj).sendToTarget();
                            mHandler.sendMessage(msg);
                            return;
                        }
                    }
                }).start();
            }
        });
        return view;
    }
    public void init(){
        //编号
        textView1=(TextView)view.findViewById(R.id.textView1);
        //姓名
        textView2=(TextView)view.findViewById(R.id.textView2);
        //性别
        textView3=(TextView)view.findViewById(R.id.textView3);
        //类型
        textView4=(TextView)view.findViewById(R.id.textView4);
        //年龄
        textView5=(TextView)view.findViewById(R.id.textView5);
        //费用
        textView6=(TextView)view.findViewById(R.id.textView6);
        //注册时间
        textView7=(TextView)view.findViewById(R.id.textView7);
        //检查时间
        textView8=(TextView)view.findViewById(R.id.textView8);
        //收费类型
        textView9=(TextView)view.findViewById(R.id.textView9);
        //检查部位
        textView10=(TextView)view.findViewById(R.id.textView10);
        //预约医生
        textView11=(TextView)view.findViewById(R.id.textView11);
        //送检医生
        textView12=(TextView)view.findViewById(R.id.textView12);
        //送检医院
        textView13=(TextView)view.findViewById(R.id.textView13);
        //送检科室
        textView14=(TextView)view.findViewById(R.id.textView14);
        //临床诊断
        textView15=(TextView)view.findViewById(R.id.textView15);
        //简要病史
        textView16=(TextView)view.findViewById(R.id.textView16);
        //临床诊断大格子
        textView17=(TextView)view.findViewById(R.id.textView17);
        //影像所见
        textView18=(TextView)view.findViewById(R.id.textView18);
        //检查印象
        textView19=(TextView)view.findViewById(R.id.textView19);
        //医生签名
        textView20=(TextView)view.findViewById(R.id.textView20);
    }
    public void buildListForSimpleAdapter(String information) {
        //将得到的informain进行二次解析
        Log.e("fanhui=",information);
        String[] zhongjian=information.split(";");
        Log.e("ad=",zhongjian[0]);
        textView1.setText(zhongjian[0]);
        textView2.setText(zhongjian[1]);
        textView3.setText(zhongjian[2]);
        textView4.setText(zhongjian[3]);
        textView5.setText(zhongjian[4]);
        textView6.setText(zhongjian[5]);
        textView7.setText(zhongjian[6]);
        textView8.setText(zhongjian[7]);
        textView9.setText(zhongjian[8]);
        textView10.setText(zhongjian[9]);
        textView11.setText(zhongjian[10]);
        textView12.setText(zhongjian[11]);
        textView13.setText(zhongjian[12]);
        textView14.setText(zhongjian[13]);
        textView15.setText(zhongjian[14]);
        textView16.setText(zhongjian[15]);
        textView17.setText(zhongjian[16]);
        textView18.setText(zhongjian[17]);
        textView19.setText(zhongjian[18]);
        textView20.setText(zhongjian[19]);
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