package com.example.wanghou;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//预约医生
@SuppressLint("ValidFragment")
public class MainFragment_2_2 extends Fragment  {
    private int mListViewFirstItem = 0;
    //listView中第一项的在屏幕中的位置
    private int mScreenY = 0;
    private ListView listView;
    private TextView yuDoctor_result,chakan_pingfen,close_chafen;
    private List list;
    //返回的医生消息
    private String information;
    //是否向上滚动
    private boolean mIsScrollToUp = false;
    private String content;
    private RatingBar doctor_attidute,doctor_shuiping;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(getContext(), "等待开发", Toast.LENGTH_SHORT).show();
                    information = (String) msg.obj;
                    list=buildListForSimpleAdapter(information);
                    SimpleAdapter notes = new SimpleAdapter(getContext(),list,R.layout.main_fragment_2_2_use_adapter,new String[]{"text_name","text_describe"},new int[]{R.id.text_name,R.id.text_describe});
                    listView.setAdapter(notes);
                    break;
                case 2:
                    //设置星星数量宽度必须wrap才能正常显示
                    String a[]=((String)msg.obj).split(",");
                    builder = new AlertDialog.Builder(getActivity());
                    final LayoutInflater inflater1 = getActivity().getLayoutInflater();
                    View view_custom = inflater1.inflate(R.layout.main_fragment_2_xianshi_fen, null,false);
                    close_chafen=(TextView)view_custom.findViewById(R.id.close_chafen);
                    close_chafen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                        }
                    });
                    builder.setView(view_custom);
                    alert = builder.create();
                    //下面这句话意思是-外面的不可点击退去对话框
                    alert.setCanceledOnTouchOutside(true);
                    builder.setCancelable(false);
                    doctor_attidute=(RatingBar)view_custom.findViewById(R.id.doctor_attidute);
                    doctor_attidute.setRating(Float.parseFloat(a[0])/20);
                    doctor_shuiping=(RatingBar)view_custom.findViewById(R.id.doctor_shuiping);
                    doctor_shuiping.setRating(Float.parseFloat(a[1])/20);
                    alert.show();
                    break;
            }
        }
    };
    public MainFragment_2_2(String content) {
        this.content = content;
    }
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_2_2,container,false);
        listView = (ListView) view.findViewById(R.id.listview);
        //连接数据库返回出医生相关数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                try {
                    HashMap<String,String> params = new HashMap<String, String>(2);
                    //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                    params.put("state", "1");
                    msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/Doctor", params, "ISO8859_1").SendDataByPostStart();
                }catch (Exception e){

                }
                msg.what = 1;
                Log.e("返回了",(String)msg.obj);
                //数据封装到message
                mHandler.obtainMessage(1,(String)msg.obj).sendToTarget();
                mHandler.sendMessage(msg);
            }
        }).start();
        //监听item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                yuDoctor_result=getActivity().findViewById(R.id.yuDoctor_result);
                HashMap<String, String> map = (HashMap<String, String>) parent
                        .getItemAtPosition(position);
                yuDoctor_result.setText(map.get("text_name"));
                chakan_pingfen=(TextView)view.findViewById(R.id.chakan_pingfen);
                chakan_pingfen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = Message.obtain();
                                try {
                                    HashMap<String,String> params = new HashMap<String, String>(2);
                                    //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                                    params.put("position", String.valueOf(position+1));
                                    params.put("state", "2");
                                    msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/Doctor", params, "ISO8859_1").SendDataByPostStart();
                                }catch (Exception e){

                                }
                                Log.e("返回了",(String)msg.obj);
                                //数据封装到message
                                mHandler.obtainMessage(2,(String)msg.obj).sendToTarget();
                                mHandler.sendMessage(msg);
                            }
                        }).start();
                    }
                });
            }
        });
        listView.setSelection(0);
        //listView自己会滑动，只要显示个数大于屏幕
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
            map.put("text_name", result[0]);
            map.put("text_describe", result[1]);
//            map.put("doctor_shuiping_1", String.valueOf(Double.parseDouble(result[2])/20));
//            map.put("doctor_attidute_1", String.valueOf(Double.parseDouble(result[3])/20));
            list.add(map);
        }
        return list;
    }
}
