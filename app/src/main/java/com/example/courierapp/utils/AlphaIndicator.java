package com.example.courierapp.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class AlphaIndicator extends LinearLayout {

    private ViewPager viewPager;
    private List<AlphaView> alphaViews = new ArrayList<>();
    /**
     * 子View的数量
     */
    private static int childCount;
    /**
     * 当前的条目索引
     */
    private int currentItem = 0;
    public AlphaIndicator(Context context) {
        this(context, null);
    }
    public AlphaIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public AlphaIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        init();
    }
    private void init() {
        if (viewPager == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        childCount = getChildCount();
        if (viewPager.getAdapter().getCount() != childCount) {
            throw new IllegalArgumentException("LinearLayout的子View数量必须和ViewPager条目数量一致");
        }
        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i) instanceof AlphaView) {
                AlphaView alphaView = (AlphaView) getChildAt(i);
                alphaViews.add(alphaView);
            } else {
                throw new IllegalArgumentException("AlphaIndicator的子View必须是AlphaView");
            }
        }
                Log.e("alphaViews size",alphaViews.size()+"");
        //对ViewPager添加监听
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        alphaViews.get(currentItem).setIconAlpha(1.0f);
    }
    private class MyOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //滑动时的透明度动画
            if (positionOffset > 0) {
                alphaViews.get(position).setIconAlpha(1 - positionOffset);
                alphaViews.get(position + 1).setIconAlpha(positionOffset);
            }
            //滑动时保存当前按钮索引
            currentItem = position;
        }
    }

    /**
     * 重置所有按钮的状态
     */
    private void resetState() {
        for (int i = 0; i < childCount; i++) {
            alphaViews.get(i).setIconAlpha(0);
        }
    }
    private static final String STATE_INSTANCE = "instance_state";
    private static final String STATE_ITEM = "state_item";

    public void setPagerNum(int currentIndex)
    {
        //点击前先重置所有按钮的状态
        resetState();
        alphaViews.get(currentIndex).setIconAlpha(1.0f);
        //不能使用平滑滚动，否者颜色改变会乱
        viewPager.setCurrentItem(currentIndex, false);
        //点击是保存当前按钮索引
        currentItem = currentIndex;
    }
}
