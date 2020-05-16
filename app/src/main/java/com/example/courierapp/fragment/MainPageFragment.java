package com.example.courierapp.fragment;

/**
 * 该文件代码主要实现图片的轮播
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.courierapp.R;
import com.example.courierapp.activity.ReceiveActivity;
import com.example.courierapp.activity.RecordActivity;
import com.example.courierapp.activity.DeliverySelectCabActivity;
import com.example.courierapp.activity.InformationNumberActivity;
import com.example.courierapp.activity.MainActivity;
import com.example.courierapp.adapter.GuidePageAdapter;
import com.example.courierapp.utils.AlphaIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class MainPageFragment extends Fragment {
    private View view;
    // 广告
    private ViewPager viewPager;
    // 广告数组
    private List<View> ar;
    private GuidePageAdapter adapter;
    private AtomicInteger atomicInteger = new AtomicInteger();
    // ImageView
    private ImageView mImages[];
    private ImageView mImage;
    private Timer timer;
    private TimerTask task;
    private Handler handler;
    AlphaIndicator alphaIndicator;
    private LinearLayout ll_delivery;
    private LinearLayout ll_pickup;
    private LinearLayout ll_record;
    private LinearLayout ll_information;
    private Context mContext;

    //通过onAttach给Fragment添加回调接口，让Activity继承并实现
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //instanceof是测试一个对象是否为一个类的实例
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            alphaIndicator = (AlphaIndicator) mainActivity.findViewById(R.id.alphaIndicator);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        //将fragment_mainpage布局文件实例化为view控件对象
        view = inflater.inflate(R.layout.fragment_mainpage, null);
        // 初始化控件
        initView();
        return view;
    }

    // 初始化控件
    private void initView() {
        ll_delivery = (LinearLayout) view.findViewById(R.id.ll_delivery);
        ll_pickup = (LinearLayout) view.findViewById(R.id.ll_pickup);
        ll_record = (LinearLayout) view.findViewById(R.id.ll_record);
        ll_information = (LinearLayout) view.findViewById(R.id.ll_information);
        ll_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DeliverySelectCabActivity.class);
                mContext.startActivity(intent);
            }
        });
        ll_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReceiveActivity.class);
                mContext.startActivity(intent);
            }
        });
        ll_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RecordActivity.class);
                mContext.startActivity(intent);
            }
        });
        ll_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InformationNumberActivity.class);
                mContext.startActivity(intent);
            }
        });
        // 第一步：初始化ViewPager
        viewPager = (ViewPager) view.findViewById(R.id.vp_advertise);
        // 创建ViewGroup对象，用来存放图片数组
        ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.rounddot);
        // 第二步：创建广告对象
        ar = new ArrayList<View>();
        View v0 = getActivity().getLayoutInflater().inflate(
                R.layout.advertise_item, null);
        LinearLayout l0 = (LinearLayout) v0.findViewById(R.id.advertise_item);
        l0.setBackgroundResource(R.drawable.ad1);
        ar.add(l0);
        View v1 = getActivity().getLayoutInflater().inflate(
                R.layout.advertise_item, null);
        LinearLayout l1 = (LinearLayout) v1.findViewById(R.id.advertise_item);
        l1.setBackgroundResource(R.drawable.ad2);
        ar.add(l1);
        View v2 = getActivity().getLayoutInflater().inflate(
                R.layout.advertise_item, null);
        LinearLayout l2 = (LinearLayout) v2.findViewById(R.id.advertise_item);
        l2.setBackgroundResource(R.drawable.ad3);
        ar.add(l2);
        View v3 = getActivity().getLayoutInflater().inflate(
                R.layout.advertise_item, null);
        LinearLayout l3 = (LinearLayout) v3.findViewById(R.id.advertise_item);
        l3.setBackgroundResource(R.drawable.ad4);
        ar.add(l3);

        adapter = new GuidePageAdapter(getActivity(), ar);
        viewPager.setAdapter(adapter);

        mImages = new ImageView[ar.size()];
        for (int i = 0; i < ar.size(); i++) {
            mImage = new ImageView(getActivity());
            // 设置图片宽和高
            LayoutParams layoutParams = new LayoutParams(9, 9);
            layoutParams.setMargins(10, 5, 10, 5);
            mImage.setLayoutParams(layoutParams);

            mImages[i] = mImage;

            if (i == 0) {
                mImages[i].setBackgroundResource(R.drawable.small_bg1);
            } else {
                mImages[i].setBackgroundResource(R.drawable.small_bg);
            }
            viewGroup.addView(mImages[i]);
        }

        viewPager.setOnPageChangeListener(vp_listener);

        if (handler == null) {
            handler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    // 显示第几项
                    viewPager.setCurrentItem(msg.what);

                    if (atomicInteger.get() == ar.size()) {
                        atomicInteger.set(0);
                    }
                }


            };
        }
        // 创建定时器
        if (timer == null)
            timer = new Timer();

        if (task == null) {
            task = new TimerTask() {

                @Override
                public void run() {
                    handler.sendEmptyMessage(atomicInteger.incrementAndGet() - 1);
                    long time = System.currentTimeMillis();
                    Log.e("timertask", time + "");
                }
            };
            timer.schedule(task, 2000, 2000);
        }


    }
    //设置图片页面Page改变监听器
    ViewPager.OnPageChangeListener vp_listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            atomicInteger.getAndSet(arg0);
            for (int i = 0; i < mImages.length; i++) {
                mImages[i].setBackgroundResource(R.drawable.small_bg1);
                if (arg0 != i) {
                    mImages[i].setBackgroundResource(R.drawable.small_bg);
                }
            }

        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
        handler = null;
    }
}

