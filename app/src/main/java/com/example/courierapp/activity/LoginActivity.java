package com.example.courierapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.courierapp.utils.GlobalData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.courierapp.utils.GlobalData.URL_HEAD;


public class LoginActivity extends Activity {

    // 对控件的声明
    private EditText et_login_username;
    private EditText et_login_password;
    private Button btn_login_yes;
    private Button btn_login_register;
    private Button btn_login_resetpwd;
    private String str_name;
    private String str_pwd;
    private Handler handler = new Handler();

    // 对端口号和URI的定义
    private static String URL_LOGIN = URL_HEAD+"/courier/login";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置该Activity界面为无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置该Activity界面的布局文件为activity_login.xml
        setContentView(com.example.courierapp.R.layout.activity_login);

        et_login_username = (EditText) findViewById(com.example.courierapp.R.id.et_login_username);
        et_login_password = (EditText) findViewById(com.example.courierapp.R.id.et_login_password);
        final Runnable loginDelay = new Runnable() {
            @Override
            public void run() {
                if (et_login_username.getText().toString().trim().equals("")){
                    Toast.makeText(LoginActivity.this,"请输入账号", Toast.LENGTH_SHORT).show();
                }else if (!et_login_username.getText().toString().toString().matches("1[13578]\\d{9}")){
                    Toast.makeText(LoginActivity.this,"账号格式不正确",Toast.LENGTH_SHORT).show();
                }else {
                    str_name = et_login_username.getText().toString().trim();
                    str_pwd = et_login_password.getText().toString().trim();
                }
            }
        };
        //设置文本监听器
        et_login_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (loginDelay!= null){
                    handler.removeCallbacks(loginDelay);//如果loginDelay不为空，删除loginDelay，使进程停止进行
                }
                //延迟1000毫秒启动loginDelay
                handler.postDelayed(loginDelay,1000);
            }
        });

        // 在布局文件中捕获Id为btn_login_yes的Button控件，并创建Button控件实例
        btn_login_yes = (Button) findViewById(com.example.courierapp.R.id.btn_login_yes);
        // 为btn_login_yes（Button控件）绑定单击监听器
        btn_login_yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                str_name = et_login_username.getText().toString().trim();
                str_pwd = et_login_password.getText().toString().trim();
                // 验证手机号格式，要求首字母为1,11位
                if (str_name.equals("") || str_name.length() != 11 || str_name.charAt(0) != '1') {
                    Toast.makeText(LoginActivity.this, "手机号格式不正确，请重新输入",
                            Toast.LENGTH_LONG).show();
                } else {
                    login();
                }
            }
        });
        btn_login_register = (Button) findViewById(com.example.courierapp.R.id.btn_login_register);
        // 为btn_login_register（Button控件）绑定单击监听器
        btn_login_register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 单击该Button时就会创建一个Intent实例，该Intent主要实现从该Activity到RegisterActivity的跳转
                Intent it_toRegisterActivity = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                // startActivity方法激活该Intent实现跳转
                startActivity(it_toRegisterActivity);
            }
        });
        btn_login_resetpwd = (Button) findViewById(com.example.courierapp.R.id.btn_login_resetpwd);
        // 为btn_login_resetpwd（Button控件）绑定单击监听器
        btn_login_resetpwd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                 单击该Button时就会创建一个Intent实例，该Intent主要实现从该Activity到ResetPwdActivity的跳转
                Intent it_toResetPwdActivity = new Intent(LoginActivity.this,
                        ResetPwdActivity.class);
                // startActivity方法激活该Intent实现跳转
                startActivity(it_toResetPwdActivity);
            }
        });

    }


    void login() {
        //创建RequestQueue对象
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //将JSONObject作为，将上一步得到的JSONObject对象作为参数传入
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Json解析
                            JSONObject obj = new JSONObject(response);
                            String code = obj.getString("code");
                            switch (code){
                                case "0":
                                    JSONObject body = obj.getJSONObject("body");
                                    String uid = body.getString("id");
                                    Log.e(TAG, "onResponse: "+body );
                                    saveAccountAndPwd();
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    GlobalData.setUid(uid);
                                    Log.e(TAG, "uid: "+uid );
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                    break;
                                case "20001":
                                    Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                                    break;
                                case "20005":
                                    Toast.makeText(LoginActivity.this, "密码验证错误", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginActivity.this, "连接服务器失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        }) {
            //Map作为HTTP-POST参数发送到服务器，发送一个用于POST请求的参数映射
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("phone", str_name);
                map.put("password", str_pwd);
                return map;
            }
        };

        requestQueue.add(stringRequest);

    }

    private void saveAccountAndPwd() {
        //使用sharedPreferences存储数据
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);//创建一个SharedPreference对象，私有数据，只能被应用本身访问
        SharedPreferences.Editor editor = sp.edit();//获取一个Editor对象
        //往Editor editor编辑器对象中添加(键值对)数据
        editor.putString("username", str_name);
        editor.putString("password", str_pwd);
        //提交Editor对象添加的数据
        editor.apply();
    }


}



