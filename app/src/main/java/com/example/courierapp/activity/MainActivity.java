package com.example.courierapp.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.example.courierapp.fragment.MainPageFragment;
import com.example.courierapp.fragment.MineFragment;
import com.example.courierapp.fragment.ReceiveFragment;
import com.example.courierapp.fragment.DeliveryFragment;
import com.example.courierapp.fragment.FeedbackFragment;
import com.example.courierapp.utils.AlphaIndicator;
import com.example.courierapp.utils.AlphaView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    //对控件的声明
    AlphaView mTab1;
    AlphaView mTab2;
    AlphaView mTab3;
    AlphaView mTab4;
    AlphaView mTab5;
    ViewPager viewPager;
    AlphaIndicator alphaIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("MainActivity onCreate()");
        //设置该Activity界面为无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置该Activity界面的布局文件为activity_main.xml
        setContentView(com.example.courierapp.R.layout.activity_main);
        //在布局文件中捕获Id对应的控件，并创建控件实例
        viewPager = (ViewPager) findViewById(com.example.courierapp.R.id.viewPager);
        alphaIndicator = (AlphaIndicator) findViewById(com.example.courierapp.R.id.alphaIndicator);
        mTab1 = (AlphaView) findViewById(com.example.courierapp.R.id.av_tab1);
        mTab2 = (AlphaView) findViewById(com.example.courierapp.R.id.av_tab2);
        mTab3 = (AlphaView) findViewById(com.example.courierapp.R.id.av_tab3);
        mTab4 = (AlphaView) findViewById(com.example.courierapp.R.id.av_tab4);
        mTab5 = (AlphaView) findViewById(com.example.courierapp.R.id.av_tab5);

        //为控件绑定监听器
        initEvent();
    }
    //为控件绑定OnClickListener/ViewPager
    private void initEvent() {
        viewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        alphaIndicator.setViewPager(viewPager);
        mTab1.setOnClickListener(this);
        mTab2.setOnClickListener(this);
        mTab3.setOnClickListener(this);
        mTab4.setOnClickListener(this);
        mTab5.setOnClickListener(this);

    }
    //识别onClick对应的控件，触发不同的内容
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //为AlphaView设置PagerNum
            case com.example.courierapp.R.id.av_tab1:
                alphaIndicator.setPagerNum(0);
                break;
            case com.example.courierapp.R.id.av_tab2:
                alphaIndicator.setPagerNum(1);
                break;
            case com.example.courierapp.R.id.av_tab3:
                alphaIndicator.setPagerNum(2);
                break;
            case com.example.courierapp.R.id.av_tab4:
                alphaIndicator.setPagerNum(3);
                break;
            case com.example.courierapp.R.id.av_tab5:
                alphaIndicator.setPagerNum(4);
                break;
            default:
                break;

        }
    }
    //通过FragmentPagerAdapter类实现fragment页面的跳转
    private class MainAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        //                "第一页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度",
        //                "第二页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度",
        //                "第三页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度",
        //                "第四页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度"};
        public MainAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(new MainPageFragment());
            fragments.add(new DeliveryFragment());
            fragments.add(new ReceiveFragment());
            fragments.add(new FeedbackFragment());
            fragments.add(new MineFragment());
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
        @Override
        public int getCount() {
            return fragments.size();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity", "onDestroy");
    }
}
