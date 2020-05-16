package com.example.courierapp.utils;

import android.os.CountDownTimer;
import android.widget.Button;

/*
 * TimeCount类用于验证码倒计时按钮的实现
 */
public class TimeCount extends CountDownTimer {
    private Button btn;

    /**
     * 在构造方法中有三个参数
     *
     * @param millisInFuture    倒计时时间
     * @param countDownInterval 计时间隔
     * @param btn               绑定的按钮
     */
    public TimeCount(long millisInFuture, long countDownInterval, Button btn) {
        super(millisInFuture, countDownInterval);//将参数传给父类CountDownTimer
        this.btn = btn;
    }

    @Override
    public void onFinish() {
        // 计时完毕时，将按钮设为可以点击
        btn.setText("获取验证码");
        btn.setClickable(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        // 在计时过程中，将按钮设为不可点击，并且显示剩余时间
        btn.setClickable(false);// 防止重复点击
        btn.setText(millisUntilFinished / 1000 + "秒后\n重新获取");
    }
}
