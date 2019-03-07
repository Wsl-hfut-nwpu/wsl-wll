package com.example.wanghou;

import android.annotation.SuppressLint;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//预约医生
@SuppressLint("ValidFragment")
public class MainFragment_2_4 extends Fragment  {
    private int mListViewFirstItem = 0;
    //listView中第一项的在屏幕中的位置
    private int mScreenY = 0;
    private ListView listView;
    private List list;
    private TextView yuXiangmu_result,yuXiangmu_jiage;
    //返回的医生消息
    private String information;
    //是否向上滚动
    private boolean mIsScrollToUp = false;
    private String content;
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
                    SimpleAdapter notes = new SimpleAdapter(getContext(),list,R.layout.main_fragment_2_4_use_adapter,new String[]{"text_xiang","text_price"},new int[]{R.id.text_xiang,R.id.text_price});
                    listView.setAdapter(notes);
                    break;
            }
        }
    };
    public MainFragment_2_4(String content) {
        this.content = content;
    }
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_2_4,container,false);
        listView = (ListView) view.findViewById(R.id.listview);
        //连接数据库返回出医生相关数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                try {
                    HashMap<String,String> params = new HashMap<String, String>(2);
                    //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                    params.put("account", "1");
                    msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/Xiangmu", params, "ISO8859_1").SendDataByPostStart();
                }catch (Exception e){
                }
                msg.what = 1;
                Log.e("返回了",(String)msg.obj);
                //数据封装到message
                mHandler.obtainMessage(1,(String)msg.obj).sendToTarget();
                mHandler.sendMessage(msg);
            }
        }).start();

        //定义List显示数据

        //监听item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yuXiangmu_result=getActivity().findViewById(R.id.yuXiangmu_result);
                yuXiangmu_jiage=getActivity().findViewById(R.id.yuXiangmu_jiage);
                HashMap<String, String> map = (HashMap<String, String>) parent
                        .getItemAtPosition(position);
                yuXiangmu_result.setText(map.get("text_xiang"));
                yuXiangmu_jiage.setText(map.get("text_price"));
            }
        });
        listView.setSelection(0);
        //listView自己会滑动，只要显示个数大于屏幕，监听滑动
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
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
            result=zhongjian[i].split(",");
            map.put("text_xiang", result[0]);
            map.put("text_price", result[1]);
            list.add(map);


        }
        return list;
    }
}
