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
import com.example.courierapp.utils.NetworkUtil2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.courierapp.utils.GlobalData.URL_HEAD;


public class RegisterActivity extends Activity {
    // 对控件的声明
    private EditText et_register_tel;
    private EditText et_register_vercode;
    private EditText et_register_pwd;
    private Button btn_register_getvercode;
    private Button btn_register_yes;
    private String str_phone;
    private String str_vercode;
    private String str_pwd;


    private TimeCount mTimeCount;

    private static final String URL_GETVERCODE = URL_HEAD+"/courier/register/send_pcode";
    private static final String URL_REGISTER = URL_HEAD+"/courier/register/";
    private String mTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_register_tel = (EditText) findViewById(R.id.et_register_tel);
        et_register_vercode = (EditText) findViewById(R.id.et_register_vercode);
        et_register_pwd = (EditText) findViewById(R.id.et_register_pwd);

        // 通过ID匹配发送验证码按钮
        btn_register_getvercode = (Button) findViewById(R.id.btn_register_getvercode);
        // 创建TimeCount实例，并设置倒计时时间60秒、计时间隔1秒，绑定btn_register_getvercode
        mTimeCount = new TimeCount(60 * 1000, 1000, btn_register_getvercode);
        // 为btn_register_getvercode设置单击监听器
        btn_register_getvercode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 调用注册发送验证码方法
                mTel = et_register_tel.getText().toString().trim();
                if (mTel.equals("")) {
                    Toast.makeText(RegisterActivity.this, "请输入手机号",
                            Toast.LENGTH_LONG).show();
                } else {
                    // NetworkUtil2.isNetworkConnected(getApplicationContext())来判断当前网络是否连接可用，可用返回true
                    if (!NetworkUtil2.isNetworkConnected(getApplicationContext())) {
                        Toast.makeText(RegisterActivity.this, "访问服务器失败，请检查网络",
                                Toast.LENGTH_LONG).show();
                    } else {
                    //在保证网络可用的前提下调用获取验证码的方法
                        getVercodeToServer();

                    }
                }

            }
        });

        btn_register_yes = (Button) findViewById(R.id.btn_register_yes);
        btn_register_yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                str_phone = et_register_tel.getText().toString().trim();
                str_vercode = et_register_vercode.getText().toString().trim();
                str_pwd = et_register_pwd.getText().toString().trim();
                // 如果手机号、验证码、密码不为空时执行
                if (!str_phone.equals("") && !str_vercode.equals("")
                        && !str_pwd.equals("")) {

                    // NetworkUtil.isNetworkAvailable(RegisterActivity.this)来判断当前网络环境是否可用，可用返回true
                    if (!NetworkUtil.isNetworkAvailable(RegisterActivity.this)) {
                        Toast.makeText(RegisterActivity.this, "访问服务器失败，请检查网络",
                                Toast.LENGTH_LONG).show();
                    } else {
                    //在保证网络可用的前提下调用向服务器注册用户的方法
                        registerToServer();

                    }
                } else {
                    if (str_phone.equals("")) {
                        Toast.makeText(RegisterActivity.this, "请输入手机号",
                                Toast.LENGTH_LONG).show();
                    } else {
                        if (str_vercode.equals("")) {
                            Toast.makeText(RegisterActivity.this, "请输入验证码",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            if (str_pwd.equals("")) {
                                Toast.makeText(RegisterActivity.this, "请输入密码",
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                }
            }
        });

    }


    /**
     * 向服务器注册用户
     */
void registerToServer( ) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        //将JSONObject作为，将上一步得到的JSONObject对象作为参数传入
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
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
                                    Toast.makeText(RegisterActivity.this, "发送成功",
                                            Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "20005":
                                    Toast.makeText(RegisterActivity.this, "验证码无效",
                                            Toast.LENGTH_LONG).show();
                                    break;
                                case "20010":
                                    Toast.makeText(RegisterActivity.this, "验证码错误",
                                            Toast.LENGTH_LONG).show();
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();

                map.put("phone", str_phone);
                map.put("password", str_pwd);
                map.put("pcode", str_vercode);
                Log.i("REGISTER params", map.toString());
                return map;
            }
        };
        //将请求添加到请求队列
        requestQueue.add(stringRequest);
    }

    /**
     * 从服务器获取注册验证码
     */
    void getVercodeToServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //将JSONObject作为，将上一步得到的JSONObject对象作为参数传入
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETVERCODE,
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
                                Toast.makeText(RegisterActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                    // 倒计时开始
                                    mTimeCount.start();
                                    break;
                                case "20020":
                                    Toast.makeText(RegisterActivity.this, "该手机号已注册", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RegisterActivity.this, "请求服务器失败，请检查网络", Toast.LENGTH_SHORT).show();
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
