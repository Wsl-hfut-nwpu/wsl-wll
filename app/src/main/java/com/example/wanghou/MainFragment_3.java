package com.example.wanghou;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.ImageButton;
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

//凡事设计UI叠加一定是采用帧布局实现的，对于图像变成圆形，采用delector实现就可以了，具体代码参考selecttor
@SuppressLint("ValidFragment")
public class MainFragment_3 extends Fragment {
    private ListView listView;
    private List list;
    private String content;
    private TextView yuType,yuDoctor,yuBuwei,yuTime,submit;
    private View view; //整体的Fragment视图
    private AnimationDrawable animationDrawable;
    private ImageButton plus;
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Data_bean obj=(Data_bean) msg.obj;
                    list=buildListForSimpleAdapter(obj.data,obj.bitmaps);
                    SimpleAdapter notes = new SimpleAdapter(getContext(),list,R.layout.main_fragment_3_1_use_adapter,new String[]{"dianhua","describe_question","answer_question","riqi","frist_picture"},new int[]{R.id.dianhua,R.id.describe_question,R.id.answer_question,R.id.riqi,R.id.frist_picture});
                    notes.setViewBinder(new SimpleAdapter.ViewBinder() {
                        public boolean setViewValue(View view, Object data,
                                                    String textRepresentation) {
                            //判断是否为我们要处理的对象
                            if(view instanceof ImageView  && data instanceof Bitmap){
                                ImageView iv = (ImageView) view;
                                iv.setImageBitmap((Bitmap) data);
                                return true;
                            }else
                                return false;
                        }
                    });
                    listView.setAdapter(notes);
                    break;
            }
        }
    };
    public MainFragment_3(String content) {
        this.content = content;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_3,container,false);
        listView=(ListView)view.findViewById(R.id.mainfragment_3_listview);
        //凡是和网络相关的数据传送，都必须开辟线程实现，不能再主线程处理，除了WEb view直接加载，像下面的下载的环节只能在线程里面处理
        new Thread(new Runnable() {
            @Override
            public void run() {
                Data_bean data_bean=new Data_bean();
                Message msg = Message.obtain();
                try {
                    HashMap<String,String> params = new HashMap<String, String>(2);
                    //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                    params.put("state", "data");
                    data_bean.data=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragmnt_3", params, "ISO8859_1").SendDataByPostStart();
                    params.put("state", "bitmap");
                    data_bean.bitmap_data=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragmnt_3", params, "ISO8859_1").SendDataByPostStart();
                    String s[]=data_bean.bitmap_data.split(";");
                    data_bean.bitmaps=data_bean.get(s.length);
                    for(int i=0;i<s.length;i++){
                        data_bean.bitmaps[i]=getBitmap("http://wangshengli.top:8080/wangluo/mainfragment_3/"+s[i]);
                    }
                }catch (Exception e){

                }
                msg.what = 1;
                msg.obj=data_bean;
                //数据封装到message
                // mHandler.obtainMessage(1,msg.obj).sendToTarget();
                mHandler.sendMessage(msg);
            }
        }).start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //重要的是view是该item视图，能通过该view获取个个控件的id然后操作， position就是位置
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),MainFragment_3_PingLun.class);
                intent.putExtra("position",String.valueOf(position));
                startActivity(intent);
            }
        });
        //设置上传影像案例的按钮事件
        plus=(ImageButton)view.findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            //小图就用src导入吧，并且要注意像素一定要调小，采用帧布局叠加，背景设置为白色才可以显示
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MainFragment_3_Submit_AnLi.class);
                intent.putExtra("patient_phone", Patient_information.user_phone);
                startActivity(intent);
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