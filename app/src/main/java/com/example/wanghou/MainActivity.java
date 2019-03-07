package com.example.wanghou;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.docoor.BiaoZhi;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //UI Object
    private TextView txt_topbar;
    private TextView txt_channel;
    private TextView txt_message;
    private TextView txt_better;
    private TextView txt_cha;
    private TextView txt_setting;
    private FrameLayout ly_content;
    //Fragment Object
    private MainFragment_1 fg1;
    private MainFragment_2 fg2;
    private MainFragment_3 fg3;
    private MainFragment_4 fg4;
    private MainFragment_5 fg5;
    //这里要注意：https://blog.csdn.net/u013937916/article/details/50456994
    private FragmentManager fManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏应用程序的标题栏，即当前activity的label
        setContentView(R.layout.activity_main);
        fManager = getFragmentManager();
        //UI组件初始化与事件绑定
        bindViews();
        if(BiaoZhi.Just==false)
           txt_channel.performClick();   //模拟一次点击，既进去后选择第一项
    }
    protected void onResume() {
        int id = getIntent().getIntExtra("id", 0);
        if (id == 3) {
            txt_better.performClick();
        }
        if(id == 5){
            txt_setting.performClick();
        }
        BiaoZhi.Just=false;
        super.onResume();
    }
    //以后形成良好的Java代码编写能力，严格按照逻辑思维来实现，类的成立原因以及类里面函数的成立原因都要自己明白
    //以后获取界面各个控件就这样获取
    private void bindViews() {
        //主页
        txt_channel = (TextView) findViewById(R.id.txt_channel);
        //预约
        txt_message = (TextView) findViewById(R.id.txt_message);
        //评论
        txt_better = (TextView) findViewById(R.id.txt_better);
        //查看
        txt_cha = (TextView) findViewById(R.id.txt_cha);
        //设置
        txt_setting = (TextView) findViewById(R.id.txt_setting);
        //Fragement帧布局
        ly_content = (FrameLayout) findViewById(R.id.ly_content);
        //以后设置今天函数对于相同类型的控件就是如此书写，统一书写一监听函数
        txt_channel.setOnClickListener(this);
        txt_message.setOnClickListener(this);
        txt_better.setOnClickListener(this);
        txt_cha.setOnClickListener(this);
        txt_setting.setOnClickListener(this);
    }
    //重置所有文本的选中状态
    private void setSelected(){
        txt_channel.setSelected(false);
        txt_message.setSelected(false);
        txt_better.setSelected(false);
        txt_cha.setSelected(false);
        txt_setting.setSelected(false);
    }
    //@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
   @Override
    public void onClick(View v) {
        //调用Feagment管理者创建切换fragment对象
        //一次下面方法只能使用一次后面的commit，不然会出现闪退,处理闪退另一个做法是将commit替换为commitAllowingStateLoss
        //但是一个commit可以提交多个事物，如删除，添加，代替，隐藏，显示
        FragmentTransaction fTransaction = fManager.beginTransaction();
        //切换对象自带隐藏Fragment，展示Fragment，替换Fragment的函数。
        hideAllFragment(fTransaction);
        switch (v.getId()){
            case R.id.txt_channel:
                //以后利用selector选择器就是利用下面两行，关闭所有选择，开启新的选择
                //理论上来说每个控件都会调用自己的选择函数
                setSelected();
                txt_channel.setSelected(true);
                if(fg1 == null){
                    fg1 = new MainFragment_1("第一个Fragment");
                    fTransaction.add(R.id.ly_content,fg1);
                }else{
                    fTransaction.show(fg1);
                }
                break;
                //新创的可以刷新，所以每次需要刷新的就新创，不用刷星的就不用直接显示
            case R.id.txt_message:
                setSelected();
                txt_message.setSelected(true);
                if(fg2 == null){
                    fg2 = new MainFragment_2("第二个Fragment");
                    fTransaction.add(R.id.ly_content,fg2);
                    fTransaction.show(fg2);
                }else {
                    fTransaction.show(fg2);
                }
                break;
            case R.id.txt_better:
                setSelected();
                txt_better.setSelected(true);
                if(fg3 == null){
                    fg3 = new MainFragment_3("第三个Fragment");
                    fTransaction.add(R.id.ly_content,fg3);
                }else{
                    fTransaction.show(fg3);
                }
                break;
            case R.id.txt_cha:
                setSelected();
                txt_cha.setSelected(true);
                if(fg4 == null){
                    fg4 = new MainFragment_4("第三个Fragment");
                    fTransaction.add(R.id.ly_content,fg4);
                }else{
                    fTransaction.show(fg4);
                }
                break;
            case R.id.txt_setting:
                setSelected();
                txt_setting.setSelected(true);
                if(fg5 == null){
                    fg5 = new MainFragment_5("第四个Fragment");
                    fTransaction.add(R.id.ly_content,fg5);
                }else{
                    fTransaction.show(fg5);
                }
                break;
        }
        fTransaction.commitAllowingStateLoss();
    }
    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fg1 != null)fragmentTransaction.hide(fg1);
        if(fg2 != null)fragmentTransaction.hide(fg2);
        if(fg3 != null)fragmentTransaction.hide(fg3);
        if(fg4 != null)fragmentTransaction.hide(fg4);
        if(fg5 != null)fragmentTransaction.hide(fg5);
    }
}