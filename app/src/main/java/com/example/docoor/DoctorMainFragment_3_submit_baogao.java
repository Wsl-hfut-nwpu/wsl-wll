package com.example.docoor;

import android.content.Intent;
import android.os.Build;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanghou.R;
import com.example.wanghou.SendDataByPost;

import java.util.HashMap;

import shezhi_information_doctor_patient.Doctor_information;

import static cn.bmob.newim.core.BmobIMClient.getContext;

public class DoctorMainFragment_3_submit_baogao extends AppCompatActivity {
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
    private EditText editView17;
    private EditText editView18;
    private EditText editView19;
    private EditText editView20;
    private String position_dianhua;
    private TextView submit_baogao;

    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    buildListForSimpleAdapter((String) msg.obj);
                    break;
                case 2:
                    if("update_success".equals((String)msg.obj)) {
                        Toast.makeText(getApplicationContext(), "提交成功,不可更改", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DoctorMainFragment_3_submit_baogao.this, DoctorMainActivity.class);
                        intent.putExtra("id",2);
                        BiaoZhi biaoZhi=new BiaoZhi();
                        biaoZhi.Just=true;
                        startActivity(intent);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_main_fragment_3_submit_baogao);
        position_dianhua=getIntent().getStringExtra("position_dianhua");
        init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                try {
                    HashMap<String,String> params = new HashMap<String, String>(2);
                    //先将数据库有的不能提交的默认
                    params.put("state", "doctor_submit_baogao_bufen_data");
                    params.put("doctor_phone", Doctor_information.user_phone);
                    params.put("patient_phone",position_dianhua );
                    //和原话没有区别，可能是编码发生了局部错误，以后就用这句话实现数据的上传
                    msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/Docotr_MainFragment_3", params, "utf-8").SendDataByPostStart();
                }catch (Exception e){
                }
                msg.what = 1;
                Log.e("返回了",(String)msg.obj);
                //数据封装到message
                mHandler.obtainMessage(1,(String)msg.obj).sendToTarget();
                mHandler.sendMessage(msg);
            }
        }).start();

        //设置提交按钮进行监听，获取里面的4个格子的数目，然后按照该医生电话  病人电话  提交数据库数据
        submit_baogao=(TextView)findViewById(R.id.submit_baogao);
        submit_baogao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(editView17.getText().toString())){
                    Toast.makeText(getApplicationContext(),"临床诊断不能为空",Toast.LENGTH_SHORT).show();
                }
                if("".equals(editView18.getText().toString())){
                    Toast.makeText(getApplicationContext(),"影像所见不能为空",Toast.LENGTH_SHORT).show();
                }
                if("".equals(editView19.getText().toString())){
                    Toast.makeText(getApplicationContext(),"检查印象不能为空",Toast.LENGTH_SHORT).show();
                }
                if("".equals(editView20.getText().toString())){
                    Toast.makeText(getApplicationContext(),"请签名",Toast.LENGTH_SHORT).show();
                }
                if(!"".equals(editView17.getText().toString())&&!"".equals(editView18.getText().toString())&&!"".equals(editView19.getText().toString())&&!"".equals(editView20.getText().toString())){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            try {
                                HashMap<String,String> params = new HashMap<String, String>(2);
                                //先将数据库有的不能提交的默认
                                params.put("state", "doctor_submit_baogao");
                                params.put("doctor_phone", Doctor_information.user_phone);
                                params.put("patient_phone",position_dianhua );
                                params.put("editView17",editView17.getText().toString());
                                params.put("editView18",editView18.getText().toString());
                                params.put("editView19",editView19.getText().toString());
                                params.put("editView20",editView20.getText().toString());
                                //和原话没有区别，可能是编码发生了局部错误，以后就用这句话实现数据的上传
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
                }
            }
        });
    }
    public void init(){
        //编号
        textView1=(TextView)findViewById(R.id.textView1);
        //姓名
        textView2=(TextView)findViewById(R.id.textView2);
        //性别
        textView3=(TextView)findViewById(R.id.textView3);
        //类型
        textView4=(TextView)findViewById(R.id.textView4);
        //年龄
        textView5=(TextView)findViewById(R.id.textView5);
        //费用
        textView6=(TextView)findViewById(R.id.textView6);
        //注册时间
        textView7=(TextView)findViewById(R.id.textView7);
        //检查时间
        textView8=(TextView)findViewById(R.id.textView8);
        //收费类型
        textView9=(TextView)findViewById(R.id.textView9);
        //检查部位
        textView10=(TextView)findViewById(R.id.textView10);
        //预约医生
        textView11=(TextView)findViewById(R.id.textView11);
        //送检医生
        textView12=(TextView)findViewById(R.id.textView12);
        //送检医院
        textView13=(TextView)findViewById(R.id.textView13);
        //送检科室
        textView14=(TextView)findViewById(R.id.textView14);
        //临床诊断
        textView15=(TextView)findViewById(R.id.textView15);
        //简要病史
        textView16=(TextView)findViewById(R.id.textView16);
        //临床诊断大格子
        editView17=(EditText) findViewById(R.id.editView17);
        //影像所见
        editView18=(EditText) findViewById(R.id.editView18);
        //检查印象
        editView19=(EditText) findViewById(R.id.editView19);
        //医生签名
        editView20=(EditText) findViewById(R.id.editView20);
    }
    public void buildListForSimpleAdapter(String information) {
        //将得到的informain进行二次解析
        Log.e("fanhui=",information);
        String[] zhongjian=information.split(";");
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
    }
}
