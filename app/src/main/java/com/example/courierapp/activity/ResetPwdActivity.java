package com.example.courierapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.courierapp.R;
import com.example.courierapp.utils.TimeCount;
import com.example.courierapp.utils.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.courierapp.utils.GlobalData.URL_HEAD;

public class ResetPwdActivity extends Activity {
    private EditText et_resetpwd_tel;
    private EditText et_resetpwd_vercode;
    private EditText et_resetpwd_pwd;
    private EditText et_resetpwd_pwd1;
    private Button btn_resetpwd_getvercode;
    private Button btn_resetpwd_yes;
    private String mTel;
    private String mVercode;
    private String mPassword;
    private String mPassword1;

    private static final String URL_RESETPWD =  URL_HEAD+"/courier/register/password";
    private static final String URL_RESETPWD_VERCODE = URL_HEAD+"/courier/register/password/send_pcode";
    private TimeCount mTimeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        et_resetpwd_tel = (EditText) findViewById(R.id.et_resetpwd_tel);
        et_resetpwd_vercode = (EditText) findViewById(R.id.et_resetpwd_vercode);
        et_resetpwd_pwd = (EditText) findViewById(R.id.et_resetpwd_pwd);
        et_resetpwd_pwd1 = (EditText) findViewById(R.id.et_resetpwd_pwd1);
        btn_resetpwd_getvercode = (Button) findViewById(R.id.btn_resetpwd_getvercode);
        mTimeCount = new TimeCount(60 * 1000, 1000, btn_resetpwd_getvercode);

        btn_resetpwd_getvercode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mTel = et_resetpwd_tel.getText().toString().trim();
                if (!NetworkUtil.isNetworkAvailable(ResetPwdActivity.this)) {
                    Toast.makeText(ResetPwdActivity.this, "访问服务器失败，请检查网络",
                            Toast.LENGTH_LONG).show();
                } else {
                    getVercodeToServer();
                }

            }
        });

        btn_resetpwd_yes = (Button) findViewById(R.id.btn_resetpwd_yes);
        btn_resetpwd_yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mTel = et_resetpwd_tel.getText().toString().trim();
                mVercode = et_resetpwd_vercode.getText().toString().trim();
                mPassword = et_resetpwd_pwd.getText().toString().trim();
                mPassword1 = et_resetpwd_pwd1.getText().toString().trim();
                if (!mTel.equals("") && !mVercode.equals("")
                        && !mPassword.equals("")&& mPassword1.equals(mPassword)) {
                    if (!NetworkUtil.isNetworkAvailable(ResetPwdActivity.this)) {
                        Toast.makeText(ResetPwdActivity.this, "访问服务器失败，请检查网络",
                                Toast.LENGTH_LONG).show();
                    } else {
                        resetPwdToServer();

                    }
                } else {
                    if (mTel.equals("")) {
                        Toast.makeText(ResetPwdActivity.this, "请输入手机号",
                                Toast.LENGTH_LONG).show();
                    } else if (mVercode.equals("")) {
                            Toast.makeText(ResetPwdActivity.this, "请输入验证码",
                                    Toast.LENGTH_LONG).show();
                        } else if (mPassword.equals("")||mPassword1.equals("")) {
                                Toast.makeText(ResetPwdActivity.this, "请输入密码",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                        Toast.makeText(ResetPwdActivity.this, "两次密码输入不一致",
                                                Toast.LENGTH_LONG).show();
                                    }
                }
            }
        });
    }


    /**
     * 请求服务器重置密码
     */
    void resetPwdToServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        //将JSONObject作为，将上一步得到的JSONObject对象作为参数传入
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RESETPWD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("请求成功", "response -> " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            String code = json.getString("code");
                            String msg = json.getString("msg");
                            // 判断解析后的code状态码
                            switch (code) {
                                case "0":
                                Toast.makeText(ResetPwdActivity.this, msg,
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ResetPwdActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            default:
                                Toast.makeText(ResetPwdActivity.this, msg,Toast.LENGTH_SHORT).show();
                                break;
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("请求失败", error.getMessage(), error);
                Toast.makeText(ResetPwdActivity.this, "请求服务器失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("phone", mTel);
                map.put("password", mPassword);
                map.put("pcode", mVercode);
                return map;
            }
        };
        //将请求添加到请求队列
        requestQueue.add(stringRequest);
    }

    /**
     * 请求服务器验证码
     */
    void getVercodeToServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //将JSONObject作为，将上一步得到的JSONObject对象作为参数传入
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RESETPWD_VERCODE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("请求成功", "response -> " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String code = json.getString("code");
                            String msg = json.getString("msg");
                            switch (code) {
                                case "0":
                                Toast.makeText(ResetPwdActivity.this, "发送成功",
                                        Toast.LENGTH_LONG).show();
                                mTimeCount.start();// 开始计时
                                break;
                            default:
                                Toast.makeText(ResetPwdActivity.this, msg,Toast.LENGTH_SHORT).show();
                                break;
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("请求失败", error.getMessage(), error);
                Toast.makeText(ResetPwdActivity.this, "请求服务器失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("phone", mTel);
                return map;
            }
        };
        //将请求添加到请求队列
        requestQueue.add(stringRequest);
    }
}
