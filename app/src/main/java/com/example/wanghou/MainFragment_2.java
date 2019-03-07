package com.example.wanghou;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
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
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

import shezhi_information_doctor_patient.Patient_information;

@SuppressLint("ValidFragment")
public class MainFragment_2 extends Fragment {
    private String content;
    private TextView yuType,yuDoctor,yuBuwei,yuXiangmu,yuTime,submit;
    private TextView yuType_result, yuDoctor_result, yuBuwei_result, yuXiangmu_result, yuXiangmu_jiage, yuDate_result, yuTime_result;
    private TextView tijiao_yuyue,xiugai_yuyue,chakan_yuyue;
    private View view; //整体的Fragment视图
    private AnimationDrawable animationDrawable;
    private FragmentManager fragmentManager;
    private MainFragment_2_1 mainFragment_2_1;
    private MainFragment_2_2 mainFragment_2_2;
    private MainFragment_2_3 mainFragment_2_3;
    private MainFragment_2_4 mainFragment_2_4;
    private MainFragment_2_5 mainFragment_2_5;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private boolean xiugai=false;
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            //该部分启发，以后基本用对话框，少用Toast
            //造线程里面不可以套入对话框，但是可在对话框里面套入线程（完整的一个线程，包含state）
            //handler属于主线程内容，里面可以在开辟线程，但是不能再和本handler交互了，可以再开一个Handler交互
            switch (msg.what) {
                case 1:
                    if("patient_wei_shiming".equals((String)msg.obj)){
                        alert = null;
                        builder = new AlertDialog.Builder(getActivity());
                        alert = builder.setIcon(R.mipmap.quickly)
                                .setTitle("提示：")
                                .setMessage("   请先到设置实名认证")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alert.dismiss();
                                    }
                                }).create();             //创建AlertDialog对象
                        alert.show();
                    }
                    break;
                case 2:
                    if("patient_yi_yuyue".equals((String)msg.obj)){
                        alert = null;
                        builder = new AlertDialog.Builder(getActivity());
                        alert = builder.setIcon(R.mipmap.quickly)
                                .setTitle("提示：")
                                .setMessage("   您已预约,不可重复预约")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alert.dismiss();
                                    }
                                }).create();             //创建AlertDialog对象
                        alert.show();
                    }
                    break;
                case 3:
                    if("yuyue_succeed".equals((String)msg.obj)){
                        alert = null;
                        builder = new AlertDialog.Builder(getActivity());
                        alert = builder.setIcon(R.mipmap.quickly)
                                .setTitle("提示：")
                                .setMessage("   恭喜您预约成功！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alert.dismiss();
                                    }
                                }).create();             //创建AlertDialog对象
                        alert.show();
                        Toast.makeText(getActivity(), "预约成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
                if("patient_wei_yuyue".equals((String)msg.obj)){
                    alert = null;
                    builder = new AlertDialog.Builder(getActivity());
                    alert = builder.setIcon(R.mipmap.quickly)
                            .setTitle("提示：")
                            .setMessage("   您未预约过,不可修改")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alert.dismiss();
                                }
                            }).create();             //创建AlertDialog对象
                    alert.show();
                }
                    break;
                case 5:
                    if("xiugai_yuyue_guo".equals((String)msg.obj)){
                        alert = null;
                        builder = new AlertDialog.Builder(getActivity());
                        alert = builder.setIcon(R.mipmap.quickly)
                                .setTitle("提示：")
                                .setMessage("   您已经修改过,不可再次修改")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alert.dismiss();
                                    }
                                }).create();             //创建AlertDialog对象
                        alert.show();
                    }
                    break;
                case 6:
                    if("xiugai_succeed".equals((String)msg.obj)){
                        alert = null;
                        builder = new AlertDialog.Builder(getActivity());
                        alert = builder.setIcon(R.mipmap.quickly)
                                .setTitle("提示：")
                                .setMessage("   您修改成功,点击查看查询修改")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alert.dismiss();
                                    }
                                }).create();             //创建AlertDialog对象
                        alert.show();
                    }
                    break;
                case 7:
                    if("patient_wei_yuyue".equals((String)msg.obj)){
                        alert = null;
                        builder = new AlertDialog.Builder(getActivity());
                        alert = builder.setIcon(R.mipmap.quickly)
                                .setTitle("提示：")
                                .setMessage("   您没有预约,不可查看")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alert.dismiss();
                                    }
                                }).create();             //创建AlertDialog对象
                        alert.show();
                    }
                    break;
                case 8:
                        buildListForSimpleAdapter((String)msg.obj);
                    break;
                case 9:
                    alert = null;
                    builder = new AlertDialog.Builder(getActivity());
                    alert = builder.setIcon(R.mipmap.quickly)
                            .setTitle("提示：")
                            .setMessage("   请选择全部修改内容")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alert.dismiss();
                                }
                            }).create();             //创建AlertDialog对象
                    alert.show();
                    break;
                case 10:
                    alert = null;
                    builder = new AlertDialog.Builder(getActivity());
                    alert = builder.setIcon(R.mipmap.quickly)
                            .setTitle("提示：一次修改机会")
                            .setMessage("   点击确定按钮即可完成修改,之后可以查看修改")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alert.dismiss();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        HashMap<String, String> params = new HashMap<String, String>(2);
                                                        //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                                                        params.put("patient_phone", Patient_information.user_phone);
                                                        params.put("state", "patient_xiugai_yuyue");
                                                        params.put("yuType_result", yuType_result.getText().toString());
                                                        params.put("yuDoctor_result", yuDoctor_result.getText().toString());
                                                        params.put("yuBuwei_result", yuBuwei_result.getText().toString());
                                                        params.put("yuXiangmu_result", yuXiangmu_result.getText().toString());
                                                        params.put("yuXiangmu_jiage", yuXiangmu_jiage.getText().toString());
                                                        params.put("yuDate_result", yuDate_result.getText().toString());
                                                        params.put("yuTime_result", yuTime_result.getText().toString());
                                                        new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragmnt_2", params, "utf-8").SendDataByPostStart();
                                                    } catch (Exception e) { }
                                                }
                                            }).start();
                                            Message msg=Message.obtain();
                                            msg.obj="asd";
                                            mHandler2.obtainMessage(1, (String) msg.obj).sendToTarget();
                                            mHandler2.sendMessage(msg);
                                            alert.dismiss();
                                        }
                                    }
                            ).create();
                    alert.show();
                    break;
            }
        }
    };
    private final Handler mHandler2 = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            //该部分启发，以后基本用对话框，少用Toast
            //造线程里面不可以套入对话框，但是可在对话框里面套入线程（完整的一个线程，包含state）
            //handler属于主线程内容，里面可以在开辟线程，但是不能再和handler交互了
            switch (msg.what) {
                case 1:
                        alert = null;
                        builder = new AlertDialog.Builder(getActivity());
                        alert = builder.setIcon(R.mipmap.quickly)
                                .setTitle("提示：")
                                .setMessage("   恭喜您修改完成")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alert.dismiss();
                                    }
                                }).create();             //创建AlertDialog对象
                        alert.show();
                    break;
            }
        }
    };
    public MainFragment_2(String content) {
        this.content = content;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment_2,container,false);
        TextView txt_content = (TextView) view.findViewById(R.id.txt_message);
        //展示Fragment_2的帧动画
            //帧动画展示只能以线程方式且只能在设置监听里面展示
        View show_ainimation = view.findViewById(R.id.show_animation);
        show_ainimation.setBackgroundResource(R.drawable.main_fragment_2_animation);
        animationDrawable = (AnimationDrawable)show_ainimation.getBackground();
        new Thread(new Runnable() {
            @Override
                public void run() {
                    animationDrawable.start();
                }}).start();
        //获取控件设置监听
        yuType = (TextView) view.findViewById(R.id.yuType);
        yuDoctor = (TextView) view.findViewById(R.id.yuDoctor);
        yuBuwei = (TextView) view.findViewById(R.id.yuBuwei);
        yuXiangmu=(TextView) view.findViewById(R.id.yuXiangmu);
        yuTime = (TextView) view.findViewById(R.id.yuTime);
        yuDoctor.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void onClick(View v) {
                hanshu(v);
            }
        });
        yuXiangmu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void onClick(View v) {
                hanshu(v);
            }
        });
        yuTime.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                hanshu(v);
            }
        });
        yuBuwei.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                hanshu(v);
              }
            });
        yuType.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                hanshu(v);
            }
        });
        //获取所有结果
        yuType_result=(TextView)view.findViewById(R.id.yuType_result);
        yuDoctor_result=(TextView)view.findViewById(R.id.yuDoctor_result);
        yuBuwei_result=(TextView)view.findViewById(R.id.yuBuwei_result);
        yuXiangmu_result=(TextView)view.findViewById(R.id.yuXiangmu_result);
        yuXiangmu_jiage=(TextView)view.findViewById(R.id.yuXiangmu_jiage);
        yuDate_result=(TextView)view.findViewById(R.id.yuDate_result);
        yuTime_result=(TextView)view.findViewById(R.id.yuTime_result);
        //获取提交按钮设置监听
        xiugai_yuyue=(TextView)view.findViewById(R.id.xiugai_yuyue);
        xiugai_yuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先显示对话框，在对话框确定按钮里面子执行修改
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        try {
                            HashMap<String,String> params = new HashMap<String, String>(2);
                            //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                            params.put("patient_phone", Patient_information.user_phone);
                            params.put("state","panduan_patient_yuyue");
                            msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragmnt_2", params, "utf-8").SendDataByPostStart();
                        }catch (Exception e){
                        }
                        if ("patient_yi_yuyue".equals((String) msg.obj)) {
                            if("".equals(yuType_result.getText().toString())||"".equals(yuDoctor_result.getText().toString())||"".equals(yuBuwei_result.getText().toString())||"".equals(yuXiangmu_result.getText().toString())||"".equals(yuXiangmu_jiage.getText().toString())||"".equals(yuDate_result.getText().toString())||"".equals(yuTime_result.getText().toString())) {
                                Log.e("返回了", (String) msg.obj);
                                //数据封装到message
                                mHandler.obtainMessage(9, (String) msg.obj).sendToTarget();
                                mHandler.sendMessage(msg);
                                return;
                            }
                            //if全部修改选择完成
                            else {
                                try {
                                    HashMap<String, String> params = new HashMap<String, String>(2);
                                    //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                                    params.put("patient_phone", Patient_information.user_phone);
                                    params.put("state", "patient_xiugai_yuyue_guo");
                                    msg.obj = (String) new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragmnt_2", params, "utf-8").SendDataByPostStart();
                                } catch (Exception e) {
                                }
                                if ("wei_xiugai_yuyue_guo".equals((String) msg.obj)) {
                                    Log.e("返回了", (String) msg.obj);
                                    //数据封装到message
                                    mHandler.obtainMessage(10, (String) msg.obj).sendToTarget();
                                    mHandler.sendMessage(msg);
                                    return;
                                }
                                else {
                                    Log.e("返回了", (String) msg.obj);
                                    //数据封装到message
                                    mHandler.obtainMessage(5, (String) msg.obj).sendToTarget();
                                    mHandler.sendMessage(msg);
                                    return;
                                }
                            }
                        } else {
                            Log.e("返回了", (String) msg.obj);
                            //数据封装到message
                            mHandler.obtainMessage(4, (String) msg.obj).sendToTarget();
                            mHandler.sendMessage(msg);
                            return;
                        }
                    }
                }).start();
                //用户确定修改在开始修改
                }
                //首先查询这个人是否存在预约
                //在查询这个人是否修改过，修改过不可再修改，没修改过方可在修改
        });
        tijiao_yuyue=(TextView)view.findViewById(R.id.tijiao_yuyue);
        tijiao_yuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(yuType_result.getText().toString())||"".equals(yuDoctor_result.getText().toString())||"".equals(yuBuwei_result.getText().toString())||"".equals(yuXiangmu_result.getText().toString())||"".equals(yuXiangmu_jiage.getText().toString())||"".equals(yuDate_result.getText().toString())||"".equals(yuTime_result.getText().toString())){
                    Toast.makeText(getActivity(), "您还有选项没有预约", Toast.LENGTH_SHORT).show();
                }
                else{
                    //先判定该病人是否已经实名认证，认证之后才可以预约（通过people中是否有身份证号码判定），否则提示先到设置里面实名认证
                    //在判定该病人是否预约过，预约过提示不可预约（通过yuyue表中yuyue_state判定）
                    //否则才可以预约，预约后提示预约成功，再次预约将不成功
                    //知道该病人报告单出来后方可再次预约
                    //不设置拉黑功能
                    new Thread(new Runnable() {                 //判定实名认证
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            try {
                                HashMap<String, String> params = new HashMap<String, String>(2);
                                //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                                params.put("patient_phone", Patient_information.user_phone);
                                params.put("state", "panduan_patient_shiming");
                                msg.obj = (String) new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragmnt_2", params, "utf-8").SendDataByPostStart();
                            } catch (Exception e) {
                            }
                            //如果实名验证成功了
                            if ("patient_yi_shiming".equals((String) msg.obj)) {
                                try {
                                    HashMap<String, String> params = new HashMap<String, String>(2);
                                    //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                                    params.put("patient_phone", Patient_information.user_phone);
                                    params.put("state", "panduan_patient_yuyue");
                                    msg.obj = (String) new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragmnt_2", params, "utf-8").SendDataByPostStart();
                                } catch (Exception e) {
                                }
                                //该病人实名验证了，但该病人没有预约
                                if ("patient_wei_yuyue".equals((String) msg.obj)) {
                                    try {
                                        HashMap<String, String> params = new HashMap<String, String>(2);
                                        //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                                        params.put("patient_phone", Patient_information.user_phone);
                                        params.put("state", "patient_kaishi_yuyue");
                                        params.put("yuType_result", yuType_result.getText().toString());
                                        params.put("yuDoctor_result", yuDoctor_result.getText().toString());
                                        params.put("yuBuwei_result", yuBuwei_result.getText().toString());
                                        params.put("yuXiangmu_result", yuXiangmu_result.getText().toString());
                                        params.put("yuXiangmu_jiage", yuXiangmu_jiage.getText().toString());
                                        params.put("yuDate_result", yuDate_result.getText().toString());
                                        params.put("yuTime_result", yuTime_result.getText().toString());
                                        msg.obj = (String) new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragmnt_2", params, "utf-8").SendDataByPostStart();
                                    } catch (Exception e) {
                                    }
                                    Log.e("返回了", (String) msg.obj);
                                    //数据封装到message
                                    mHandler.obtainMessage(3, (String) msg.obj).sendToTarget();
                                    mHandler.sendMessage(msg);
                                    return;
                                }
                            //该病人实名验证了，且该病人已经预约
                            if ("patient_yi_yuyue".equals((String) msg.obj)) {
                                Log.e("返回了", (String) msg.obj);
                                //数据封装到message
                                mHandler.obtainMessage(2, (String) msg.obj).sendToTarget();
                                mHandler.sendMessage(msg);
                                return;
                            }
                        }
                            //实名验证没通过，提示先进行实名验证
                            if("patient_wei_shiming".equals((String)msg.obj)) {
                                Log.e("返回了", (String) msg.obj);
                                //数据封装到message
                                mHandler.obtainMessage(1, (String) msg.obj).sendToTarget();
                                mHandler.sendMessage(msg);
                                return;
                            }
                        }
                    }).start();

                }
            }
        });
        chakan_yuyue=(TextView)view.findViewById(R.id.chakan_yuyue);
        chakan_yuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先查询该病人是否预约，预约后方可查看
                //没有预约要提示预约
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        try {
                            HashMap<String,String> params = new HashMap<String, String>(2);
                            //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                            params.put("patient_phone", Patient_information.user_phone);
                            params.put("state","panduan_patient_yuyue");
                            msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragmnt_2", params, "utf-8").SendDataByPostStart();
                        }catch (Exception e){ }
                    //该病人实名验证了，但该病人没有预约
                        if("patient_yi_yuyue".equals((String)msg.obj)) {
                            try {
                                HashMap<String,String> params1 = new HashMap<String, String>(2);
                                //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                                params1.put("patient_phone", Patient_information.user_phone);
                                params1.put("state","patient_chakan_yuyue");
                                msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragmnt_2", params1, "utf-8").SendDataByPostStart();
                            }catch (Exception e){ }
                            Log.e("返回了", (String) msg.obj);
                            //数据封装到message
                            //下面这句话和   msg.what重复了，会执行2次，必须去掉一个。妈的
                            mHandler.obtainMessage(8, (String) msg.obj).sendToTarget();
                            mHandler.sendMessage(msg);
                            return;
                        }
                        if("patient_wei_yuyue".equals((String)msg.obj)) {
                            Log.e("返回了", (String) msg.obj);
                            //数据封装到message
                            mHandler.obtainMessage(7, (String) msg.obj).sendToTarget();
                            mHandler.sendMessage(msg);
                            return;
                        }
                    }
                }).start();
            }
        });

        //导入对话框的布局
        //返回整个Fragment显示
        return view;
    }
    //获取所有TextView

    //设置TextView都没有被选择
    private void setSelected(){
        yuBuwei.setSelected(false);
        yuDoctor.setSelected(false);
        yuTime.setSelected(false);
        yuXiangmu.setSelected(false);
        yuType.setSelected(false);
    }
    //设置TextView的监听
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void hanshu(View v){
        FragmentTransaction fTransaction = getChildFragmentManager().beginTransaction();
        //切换对象自带隐藏Fragment，展示Fragment，替换Fragment的函数。
        hideAllFragment(fTransaction);
        switch (v.getId()){
            case R.id.yuType:
                setSelected();
                yuType.setSelected(true);
                if(mainFragment_2_1 == null){
                    mainFragment_2_1 = new MainFragment_2_1("第三个Fragment");
                    fTransaction.add(R.id.ly_content,mainFragment_2_1);
                }else{
                    fTransaction.show(mainFragment_2_1);
                }
                break;
            case R.id.yuDoctor:
                setSelected();
                yuDoctor.setSelected(true);
                if(mainFragment_2_2 == null){
                    mainFragment_2_2 = new MainFragment_2_2("第一个Fragment");
                    fTransaction.add(R.id.ly_content,mainFragment_2_2);
                }else{
                    fTransaction.show(mainFragment_2_2);
                }
                break;
            case R.id.yuBuwei:
                setSelected();
                yuBuwei.setSelected(true);
                if(mainFragment_2_3 == null){
                    mainFragment_2_3 = new MainFragment_2_3("第四个Fragment");
                    fTransaction.add(R.id.ly_content,mainFragment_2_3);
                }else{
                    fTransaction.show(mainFragment_2_3);
                }
                break;
            case R.id.yuXiangmu:
                setSelected();
                yuXiangmu.setSelected(true);
                if(mainFragment_2_4 == null){
                    mainFragment_2_4 = new MainFragment_2_4("第四个Fragment");
                    fTransaction.add(R.id.ly_content,mainFragment_2_4);
                }else{
                    fTransaction.show(mainFragment_2_4);
                }
                break;
            case R.id.yuTime:
                setSelected();
                yuTime.setSelected(true);
                if(mainFragment_2_5 == null){
                    mainFragment_2_5 = new MainFragment_2_5("第二个Fragment");
                    fTransaction.add(R.id.ly_content,mainFragment_2_5);
                }else {
                    fTransaction.show(mainFragment_2_5);
                }
                break;
        }
        fTransaction.commitAllowingStateLoss();
    }
    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(mainFragment_2_1 != null)fragmentTransaction.hide(mainFragment_2_1);
        if(mainFragment_2_2 != null)fragmentTransaction.hide(mainFragment_2_2);
        if(mainFragment_2_3 != null)fragmentTransaction.hide(mainFragment_2_3);
        if(mainFragment_2_4 != null)fragmentTransaction.hide(mainFragment_2_4);
        if(mainFragment_2_5 != null)fragmentTransaction.hide(mainFragment_2_5);
    }
    public void buildListForSimpleAdapter(String information) {
        String[] zhongjian = information.split("qa");
//        yuType_result.setText(zhongjian[0]);
//        yuType_result.setTextColor(0xffff);
//        yuDoctor_result.setText(zhongjian[1]);
//        yuBuwei_result.setText(zhongjian[2]);
//        yuXiangmu_result.setText(zhongjian[3]);
//        yuXiangmu_jiage.setText(zhongjian[4]);
//        yuDate_result.setText(zhongjian[5]);
//        yuTime_result.setText(zhongjian[6]);
        builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater1 = getActivity().getLayoutInflater();
        View view_custom = inflater1.inflate(R.layout.main_fragment_2_1_use_dialog, null,false);
        builder.setView(view_custom);
        alert = builder.create();
        //下面这句话意思是-外面的不可点击退去对话框
        alert.setCanceledOnTouchOutside(false);
        alert.show();
        TextView yuType_result_d=view_custom.findViewById(R.id.yuType_result_d);
        yuType_result_d.setText(zhongjian[0]);
        TextView yuDoctor_result_d=view_custom.findViewById(R.id.yuDoctor_result_d);
        yuDoctor_result_d.setText(zhongjian[1]);
        TextView yuBuwei_result_d=view_custom.findViewById(R.id.yuBuwei_result_d);
        yuBuwei_result_d.setText(zhongjian[2]);
        TextView yuXiangmu_result_d=view_custom.findViewById(R.id.yuXiangmu_result_d);
        yuXiangmu_result_d.setText(zhongjian[3]);
        TextView yuPrice_result_d=view_custom.findViewById(R.id.yuPrice_result_d);
        yuPrice_result_d.setText(zhongjian[4]);
        TextView yuDate_result_d=view_custom.findViewById(R.id.yuDate_result_d);
        yuDate_result_d.setText(zhongjian[5]);
        TextView yuTime_result_d=view_custom.findViewById(R.id.yuTime_result_d);
        yuTime_result_d.setText(zhongjian[6]);
        TextView btn_cancle=view_custom.findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setCancelable(false);
                alert.dismiss();
                alert.hide();
            }
        });
    }
}