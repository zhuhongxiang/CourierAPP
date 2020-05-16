package com.example.courierapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.courierapp.R;
import com.example.courierapp.activity.RecordActivity;
import com.example.courierapp.utils.TimeUtil;
import com.example.courierapp.activity.LoginActivity;
import com.example.courierapp.activity.MainActivity;
import com.example.courierapp.utils.AlphaIndicator;
import com.example.courierapp.utils.GlobalData;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

public class MineFragment extends Fragment {
    private View view;
    // 组件
    private TextView mTvName;// 昵称
    private TextView mTvGreeting;// 问候语

    // 用户缓存
    private SharedPreferences preferences;

    // 我的订单、我的收藏、账号管理
    private RelativeLayout mRlAppoint;
    private RelativeLayout mRlCollects;
    private RelativeLayout mRlInfo;
    private RelativeLayout mRlQuit;

    private boolean mIsLogin;

    private Context mContext;
    private AlphaIndicator alphaIndicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            alphaIndicator = (AlphaIndicator) mainActivity.findViewById(R.id.alphaIndicator);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_mine, null);
        // 初始化控件
        initView();
        // 登录成功之后，获得用户头像
        preferences = getActivity().getSharedPreferences("Login",
                Context.MODE_PRIVATE);
//        int uimage = preferences.getInt("uimage", 0);
        mIsLogin = preferences.getBoolean("mIsLogin", false);
        return view;
    }

    /**
     * 初始化控件方法
     */
    private void initView() {
        mTvGreeting = (TextView) view.findViewById(R.id.id_greeting);
        String time = TimeUtil.getCurrentTime();
        Log.e("TAG",time);
        String times[] = time.split(":");
        int hour = Integer.parseInt(times[0]);
        if (hour >= 18 && hour < 24) mTvGreeting.setText("Good Night!");
        else if (hour >= 0 && hour < 5) mTvGreeting.setText("Good Night!");
        else if (hour >= 5 && hour < 12) mTvGreeting.setText("Good Morning!");
        else if (hour >= 12 && hour < 14) mTvGreeting.setText("Good Noon!");
        else if (hour >= 14 && hour < 18) mTvGreeting.setText("Good Afternoon!");


        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mTvName.setText(GlobalData.getUid()+"号用户");
        // 我的订单、我的收藏、账号管理
        mRlAppoint = (RelativeLayout) view.findViewById(R.id.rl_myinfo);
        mRlCollects = (RelativeLayout) view.findViewById(R.id.rl_history);
//        mRlInfo = (RelativeLayout) view.findViewById(R.id.rl_info);
        mRlQuit = (RelativeLayout) view.findViewById(R.id.rl_quit);


        // 监听事件
        mTvName.setOnClickListener(iv_listener);

        mRlAppoint.setOnClickListener(iv_listener);
        mRlCollects.setOnClickListener(iv_listener);
//        mRlInfo.setOnClickListener(iv_listener);
        mRlQuit.setOnClickListener(iv_listener);
    }

    View.OnClickListener iv_listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.tv_name:
//                    // 跳转到修改信息界面
//                    startActivity(new Intent(mContext, MyInformationActivity.class));
//                    break;
//
//                case R.id.rl_myinfo:
//                    // 跳转到修改信息界面
//                    startActivity(new Intent(mContext, RecordActivity.class));
//
//                    break;
                case R.id.rl_history:
                    // 跳转至投递记录界面
                    startActivity(new Intent(mContext, RecordActivity.class));
                    break;
                case R.id.rl_quit:

                    LemonHello.getInformationHello("您确定要注销吗？", "注销登录后您本地的账号信息将被清空")
                            .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                                @Override
                                public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                    helloView.hide();
                                }
                            }))
                            .addAction(new LemonHelloAction("我要注销", Color.RED, new LemonHelloActionDelegate() {
                                @Override
                                public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                    helloView.hide();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                    clearSP();
                                }
                            }))
                            .show(getActivity());


                    break;
                default:
                    break;
            }

        }
    };


    // 清空SharedPreferences中的数据
    private void clearSP() {
        SharedPreferences sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", "");
        editor.putString("password", "");
        editor.apply();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

}
