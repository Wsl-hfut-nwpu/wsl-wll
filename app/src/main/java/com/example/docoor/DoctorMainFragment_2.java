package com.example.docoor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.wanghou.Data_bean;
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
public class DoctorMainFragment_2 extends Fragment {
    private String content;
    private TextView  yuyue_bianhao, patient_xingming, yueyue_doctor,zhengduan_keshi, songjian , yuyue_xiangmu, result ;
    private ListView listView;
    private List list;
    public ProgressDialog pdialog;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if("".equals((String) msg.obj))
                        Toast.makeText(getContext(), "病人影像并未上传", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(getContext(), "欢迎您", Toast.LENGTH_SHORT).show();
                        list = buildListForSimpleAdapter((String) msg.obj);
                        SimpleAdapter notes = new SimpleAdapter(getContext(), list, R.layout.doctor_main_fragment_2_1_use_adapter,
                                new String[]{"yuyue_bianhao", "patient_xingming", "yueyue_doctor", "zhengduan_keshi", "zhengduan_doctor", "songjian", "yuyue_xiangmu", "result"},
                                new int[]{R.id.yuyue_bianhao, R.id.patient_xingming, R.id.yueyue_doctor, R.id.zhengduan_keshi, R.id.zhengduan_doctor, R.id.songjian, R.id.yuyue_xiangmu, R.id.result});
                        listView.setAdapter(notes);
                    }
                    break;
                case 2:
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
    public DoctorMainFragment_2(String content) {
        this.content = content;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_main_fragment_2,container,false);
        listView=(ListView)view.findViewById(R.id.listview_yingxiang) ;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                try {
                    HashMap<String,String> params = new HashMap<String, String>(2);
                    //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                    params.put("state", "doctor_get_yuyue_yingxiang");
                    params.put("doctor_phone", Doctor_information.user_phone);
                    msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/Docotr_MainFragment_2", params, "utf-8").SendDataByPostStart();
                }catch (Exception e){
                }
                msg.what = 1;
                Log.e("返回了",(String)msg.obj);
                //数据封装到message
                mHandler.obtainMessage(1,(String)msg.obj).sendToTarget();
                mHandler.sendMessage(msg);
            }
        }).start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 final TextView yuyue_bianhao=(TextView)view.findViewById(R.id.yuyue_bianhao);
                 TextView cha_yingxiang=(TextView)view.findViewById(R.id.cha_yingxiang);
                 cha_yingxiang.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         pdialog = ProgressDialog.show(getActivity(), "     影像正在加载...", "\n\n\n");
                         new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 Message msg = Message.obtain();
                                 Data_bean data_bean = new Data_bean();
                                 try {
                                     HashMap<String, String> params = new HashMap<String, String>(3);
                                     //获取预约查看图片标志位，查看医生是否提交报告
                                     params.put("state", "yingxiang_chakan");
                                     params.put("patient_bianhao", yuyue_bianhao.getText().toString());
                                     data_bean.bitmap_data = (String) new SendDataByPost("http://47.104.218.123:8080/wangluo/Docotr_MainFragment_2", params, "utf-8").SendDataByPostStart();
                                 } catch (Exception e) {
                                 }
                                 //数据此时放在data_bean里面，就不能再和msg比较了，要注意
                                 String s[] = data_bean.bitmap_data.split(";");
                                 data_bean.bitmaps = data_bean.get(s.length);
                                 for (int i = 0; i < s.length; i++) {
                                     data_bean.bitmaps[i] = getBitmap("http://wangshengli.top:8080/wangluo/All_patient_yingxiang/" + s[i]);
                                 }
                                 msg.what = 2;
                                 msg.obj = data_bean;
                                 mHandler.sendMessage(msg);
                             }
                         }).start();
                     }
                 });
            }
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
            //出现不知名闪退，重启就可以了
            result=zhongjian[i].split(",");
            map.put("yuyue_bianhao", result[0]);
            map.put("patient_xingming", result[1]);
            map.put("yueyue_doctor", result[2]);
            map.put("zhengduan_keshi", result[3]);
            map.put("zhengduan_doctor", result[4]);
            map.put("songjian", result[5]);
            map.put("yuyue_xiangmu", result[6]);
            map.put("result", result[7]);
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