package com.example.wanghou;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docoor.BiaoZhi;
import com.example.docoor.DoctorMainActivity;

import java.util.HashMap;

import shezhi_information_doctor_patient.Doctor_information;
import shezhi_information_doctor_patient.Patient_information;

public class MainFragment_5_ShiMing extends AppCompatActivity {
    private TextView sm5,shiming_submit;
    private EditText sm1,sm3,sm4;
    private RadioGroup boy_or_gril;
    private RadioButton boy,gril;
    private int point_group_int=1;
    private ImageButton fanhui_4;
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent(MainFragment_5_ShiMing.this, MainActivity.class);
                    intent.putExtra("id",5);
                    BiaoZhi biaoZhi=new BiaoZhi();
                    biaoZhi.Just=true;
                    startActivity(intent);
                    Toast.makeText(MainFragment_5_ShiMing.this,"实名验证成功，可以预约！",Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_5_shi_ming);
        sm5=(TextView)findViewById(R.id.sm5);
        sm5.setText(Patient_information.user_phone);
        //1是真是姓名   3是id     4是age
        sm1=(EditText)findViewById(R.id.sm1);
        sm3=(EditText)findViewById(R.id.sm3);
        sm4=(EditText)findViewById(R.id.sm4);
        boy_or_gril=(RadioGroup)findViewById(R.id.boy_or_gril);
        boy=(RadioButton) findViewById(R.id.boy);
        gril=(RadioButton) findViewById(R.id.gril);
        boy_or_gril.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(boy.getId()==checkedId){
                    point_group_int=1;
                }
                if(gril.getId()==checkedId){
                    point_group_int=2;
                }
            }
        });
        shiming_submit=(TextView)findViewById(R.id.shiming_submit);
        shiming_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(sm1.getText().toString())||"".equals(sm3.getText().toString())||"".equals(sm4.getText().toString())){
                    Toast.makeText(MainFragment_5_ShiMing.this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            try {
                                HashMap<String,String> params = new HashMap<String, String>(3);
                                params.put("state", "shiming_renzheng");
                                params.put("patient_phone",Patient_information.user_phone);
                                params.put("name",sm1.getText().toString());
                                params.put("id_card",sm3.getText().toString());
                                params.put("age",sm4.getText().toString());
                                if(point_group_int==1)   params.put("sex","男");
                                if(point_group_int==2)   params.put("sex","女");
                                msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragment_5", params, "utf-8").SendDataByPostStart();
                            }catch (Exception e){
                            }
                            //初始化标志位
                            msg.what=1;
                            //数据封装到message
                            mHandler.obtainMessage(1,(String)msg.obj).sendToTarget();
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
        fanhui_4=(ImageButton)findViewById(R.id.fanhui_4);
        fanhui_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
