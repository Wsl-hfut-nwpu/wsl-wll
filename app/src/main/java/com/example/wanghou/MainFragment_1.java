package com.example.wanghou;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

@SuppressLint("ValidFragment")
public class MainFragment_1 extends Fragment {
    private String content;
    private View view; //整体的Fragment视图
    private AnimationDrawable animationDrawable;
    private LinearLayout minyizaixian;
    private ListView binlizhanxian;
    private String information;
    private List listView_list;
    private Bitmap [] bitmaps=null;
    public MainFragment_1(String content) {
        this.content = content;
    }
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            //获得刚才发送的Message对象，然后在这里进行UI操作
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(getContext(), "欢迎您", Toast.LENGTH_SHORT).show();
                    information=(String) msg.obj;
                    buildListForSimpleAdapter(information);
                    break;
                case 2:
                    Data_bean obj=(Data_bean) msg.obj;
                    listView_list=buildListForSimpleAdapter_really(obj.data,obj.bitmaps);
                    SimpleAdapter notes = new SimpleAdapter(getContext(),listView_list,R.layout.main_fragment_1_2_use_adapter,new String[]{"zhengduan_neirong","zhengduan_picture"},new int[]{R.id.zhengduan_neirong,R.id.zhengduan_picture});
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
                    binlizhanxian.setAdapter(notes);
                    break;
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(
                getActivity(), R.style.day );
        inflater.cloneInContext(contextThemeWrapper);
        View view = inflater.inflate(R.layout.main_fragment_1,container,false);
        //展示Fragment_1的帧动画
        View show_ainimation = view.findViewById(R.id.show_animation_main_fragment_1);
        show_ainimation.setBackgroundResource(R.drawable.main_fragment_1_animation);
        animationDrawable = (AnimationDrawable)show_ainimation.getBackground();
        new Thread(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }}).start();
        //获取二个View列表实现链表显示
        minyizaixian=(LinearLayout)view.findViewById(R.id.minyizaixian);
        binlizhanxian=(ListView)view.findViewById(R.id.binlizhanxian);
        //开辟线程获取数据显示在Adapter，二个View列表实现链表显示
        get_GridView_data();
        get_ListView_data();
        //设置监听做相关处理
        binlizhanxian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),MainFragment_1_anli_introduce.class);
                intent.putExtra("position",String.valueOf(position));
                startActivity(intent);
            }
        });
        binlizhanxian.setSelection(0);

        //返回整个Fragment显示
        return view;
    }
    public void get_GridView_data(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                try {
                    HashMap<String,String> params = new HashMap<String, String>(2);
                    //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                    params.put("state", "gridView_list");
                    msg.obj=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_shouye", params, "utf-8").SendDataByPostStart();
                }catch (Exception e){
                }
                Log.e("返回了",(String)msg.obj);
                //数据封装到message
                mHandler.obtainMessage(1,(String)msg.obj).sendToTarget();
                mHandler.sendMessage(msg);
            }
        }).start();
    }
    public void get_ListView_data() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Data_bean data_bean=new Data_bean();
                Message msg = Message.obtain();
                try {
                    HashMap<String,String> params = new HashMap<String, String>(2);
                    //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                    params.put("state", "ListView_list");
                    data_bean.data=(String)new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_shouye", params, "ISO8859_1").SendDataByPostStart();
                }catch (Exception e){
                }
                //这里可以不采用这个全局的类，可以定义一个公有的数组在本类里面，然后实现线程和主线程共享
                //这里出错解  ：最后发现原因是Adapter添加变量是多放入一个逗号，他妈的，同时顺序出错，妈的气炸了
                String s[]=data_bean.data.split("qa");
                data_bean.bitmaps=data_bean.get(s.length);
                for(int i=0;i<s.length;i++){
                    data_bean.bitmaps[i]=getBitmap("http://wangshengli.top:8080/wangluo/ListView/"+String.valueOf(i+1)+".jpg");
                }
                //跑程序是出现R错误让程序再次运行就可以了。
                //水平竖直滑动在本页面都实现了，可以今天也可以动态添加，可以嵌套动态也可以嵌套动态，动静结合实现好的效果
                //数据封装到message
                msg.what=2;
                msg.obj=data_bean;
                mHandler.sendMessage(msg);
            }
        }).start();
    }
    //后台获取布局可以动态添加相关控件，而如ListView类只能通过Adapter实现，不能动态添加控件
    //ListView功能强大，可以加载TextView，也可以你返回图片链接。然后这里自己写函数下载图片然后通过Adapter添加
    //不过要求的是Adapter添加Bitmap类，且前端页面的是ImageView才可以显示，SimpleAdapter本身是不支持网络图片的。
    //网址如下：https://blog.csdn.net/u011609853/article/details/25513035
    //https://blog.csdn.net/lc547913923/article/details/50333577
    //我这里不是Adapter，直接 竖直滑动视图  里面的线性布局显示直接显示就可以了
    //下面加载图片采用的是前端页面定义WebView控件，这里采用链接可以不用下载直接加载，所以比较慢。
    //ListView可以自由竖直滑动，然而GridVIew是网格布局，需要定义列数，不可以自由左右滑动，也可以上下滑动
    //所以左右滑动一般采用的是   竖直滑动视图控件
    //当然还有一种方式就是图片的上传和接收

    //如果在本地可以直接在按照TextView方法设置图片R.id...加载（通过Adapter）


    //由于这是Fragment，所以采用inflate导入
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void buildListForSimpleAdapter(String information) {
        //将得到的informain进行二次解析
        Log.e("fanhui=", information);
        String[] zhongjian = information.split(";");
        String[] result;
        minyizaixian.removeAllViews();
        for (int i = 0; i < zhongjian.length; i++) {
            View view2 = LayoutInflater.from(getContext()).inflate(R.layout.main_fragment_1_1_use_adapter, minyizaixian, false);
            result = zhongjian[i].split(",");
            //寻找行布局，第一个参数为行布局ID，第二个参数为这个行布局需要放到那个容器上
            //通过View寻找ID实例化控件
            WebView webView = (WebView) view2.findViewById(R.id.head_phone);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            //实例化TextView控件
            TextView tv = (TextView) view2.findViewById(R.id.minyizaixian_name);
            //将int数组中的数据放到ImageView中
            TextView tv2 = (TextView) view2.findViewById(R.id.minyizaixian_miaoshu);
            //给TextView添加文字
            tv.setText(result[0]);
            //把行布局放到linear里
            tv2.setText(result[1]);
            //虽然用的名字一样，但是每次都是新的一个View2，所以可以在这里面设置监听，循环加进去，点击的时候，点击下面滑条就可以
            webView.loadUrl("http://wangshengli.top:8080/wangluo/image/" + result[2]);
            final int finalI = i + 1;//由于OnClick里面拿不到i,所以需要重新赋值给一个final对象
            view2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MainFragment_1_mingyi_introduce.class);
                    intent.putExtra("position", String.valueOf(finalI));
                    startActivity(intent);
                }
            });
            minyizaixian.addView(view2);
        }
    }
    public List<Map<String, Object>> buildListForSimpleAdapter_really(String information,Bitmap bitmaps[]) {
        //将得到的informain进行二次解析
        String[] zhongjian=information.split("qa");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(4);
        for (int i = 0; i < zhongjian.length; i++) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("zhengduan_neirong", zhongjian[i]);
            map.put("zhengduan_picture", bitmaps[i]);
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