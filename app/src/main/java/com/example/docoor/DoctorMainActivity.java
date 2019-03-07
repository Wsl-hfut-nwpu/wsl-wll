package com.example.docoor;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.wanghou.R;

public class DoctorMainActivity extends AppCompatActivity implements View.OnClickListener{
    //UI Object
    private TextView today_yuyue;
    private TextView today_yingxiang;
    private TextView submit_bingli;
    private TextView geren_set;
    private FrameLayout doctor_fragment;
    //Fragment Object
    private DoctorMainFragment_1 fg1;
    private DoctorMainFragment_2 fg2;
    private DoctorMainFragment_3 fg3;
    private DoctorMainFragment_4 fg4;
    //这里要注意：https://blog.csdn.net/u013937916/article/details/50456994
    private FragmentManager fManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //下面在Activity使用就好了，因为Fragment在Activity中生存
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏应用程序的标题栏，即当前activity的label
        setContentView(R.layout.doctor_activity_main);
        fManager = getFragmentManager();
            //UI组件初始化与事件绑定
        bindViews();
        if(BiaoZhi.Just==false)
          today_yuyue.performClick();   //模拟一次点击，既进去后选择第一项
    }
    //执行顺序是onCreat（），onStart（）,onResume();所以当那边要跳转到这里是，先加载onCreat（），是的第一个FragMent又执行，报错,所以设置全局共享静态变量处理返回时不可访问
    protected void onResume() {
        int id = getIntent().getIntExtra("id", 0);
        if (id == 2) {
            submit_bingli.performClick();
        }
        BiaoZhi.Just=false;
        super.onResume();
    }
    //以后形成良好的Java代码编写能力，严格按照逻辑思维来实现，类的成立原因以及类里面函数的成立原因都要自己明白
    //以后获取界面各个控件就这样获取
    private void bindViews() {
        today_yuyue = (TextView) findViewById(R.id.today_yuyue);
        today_yingxiang = (TextView) findViewById(R.id.today_yingxiang);
        submit_bingli = (TextView) findViewById(R.id.submit_bingli);
        geren_set = (TextView) findViewById(R.id.geren_set);
        doctor_fragment = (FrameLayout) findViewById(R.id.doctor_fragment);
        //以后设置今天函数对于相同类型的控件就是如此书写，统一书写一监听函数
        today_yuyue.setOnClickListener(this);
        today_yingxiang.setOnClickListener(this);
        submit_bingli.setOnClickListener(this);
        geren_set.setOnClickListener(this);
    }
    //重置所有文本的选中状态
    private void setSelected(){
        today_yuyue.setSelected(false);
        today_yingxiang.setSelected(false);
        submit_bingli.setSelected(false);
        geren_set.setSelected(false);
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
            case R.id.today_yuyue:
                //以后利用selector选择器就是利用下面两行，关闭所有选择，开启新的选择
                //理论上来说每个控件都会调用自己的选择函数
                setSelected();
                today_yuyue.setSelected(true);
                if(fg1 == null){
                    fg1 = new DoctorMainFragment_1("第一个Fragment");
                    fTransaction.add(R.id.doctor_fragment,fg1);
                }else{
                    fTransaction.show(fg1);
                }
                break;
            case R.id.today_yingxiang:
                setSelected();
                today_yingxiang.setSelected(true);
                if(fg2 == null){
                    fg2 = new DoctorMainFragment_2("第二个Fragment");
                    fTransaction.add(R.id.doctor_fragment,fg2);
                }else {
                    fTransaction.show(fg2);
                }
                break;
            case R.id.submit_bingli:
                setSelected();
                submit_bingli.setSelected(true);
                if(fg3 == null){
                    fg3 = new DoctorMainFragment_3("第三个Fragment");
                    fTransaction.add(R.id.doctor_fragment,fg3);
                }else{
                    fTransaction.show(fg3);
                }
                break;
            case R.id.geren_set:
                setSelected();
                geren_set.setSelected(true);
                if(fg4 == null){
                    fg4 = new DoctorMainFragment_4("第三个Fragment");
                    fTransaction.add(R.id.doctor_fragment,fg4);
                }else{
                    fTransaction.show(fg4);
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
    }
}
