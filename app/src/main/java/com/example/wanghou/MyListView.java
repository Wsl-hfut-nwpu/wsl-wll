package com.example.wanghou;

import android.widget.ListView;
//由于ListView本身具有滑动效果，所以如果嵌套在另一个ListView或者ScrollView中，那么此时
//二者滑动冲突，导致ListView只可以显示一行数据，办法就是重写ListVIew，禁止它滑动
public class MyListView extends ListView {
    public MyListView(android.content.Context context,android.util.AttributeSet attrs){
        super(context, attrs);
    }
    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
