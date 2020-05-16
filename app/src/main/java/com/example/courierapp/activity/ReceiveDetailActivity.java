package com.example.courierapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.courierapp.R;
import com.example.courierapp.utils.GlobalData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.example.courierapp.utils.GlobalData.URL_HEAD;

public class ReceiveDetailActivity extends Activity {
    //声明控件
    private TextView tv_billNumberSend;
    private TextView tv_address;
    private TextView tv_sendTime;
    private TextView tv_passwordReceive;
    private Button btn_receive;
    private static final String URL_APPLICATION = URL_HEAD+"/receive/apply";
    private static final String URL_CHECK = URL_HEAD+"/receive/check";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivedetail);
        initView();
    }
    //通过ID获取布局文件中的控件
    private void initView() {
        tv_billNumberSend = findViewById(R.id.tv_billNumberSend);
        tv_address = findViewById(R.id.tv_address);
        tv_sendTime = findViewById(R.id.tv_sendTime);
        tv_passwordReceive = findViewById(R.id.tv_passwordReceive);
        btn_receive = findViewById(R.id.btn_receive);
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> list = bundle.getStringArrayList("all");
        tv_billNumberSend.setText(list.get(0));
        tv_address.setText(list.get(1));
        tv_sendTime.setText(list.get(2));
        tv_passwordReceive.setText(list.get(3));
        final String id = list.get(4);
        Log.e(TAG, "initView: "+id );
        //为取回快件按钮绑定监听器
        btn_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToServer(id);
            }
        });
    }
    //向服务器发送取件请求
    private void sendToServer(final String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_APPLICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAG", "sendToServer: "+response );
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            String code = jsonObject.getString("code");
                            if (code.equals("0")){
                                check(id);
                            }else if (code.equals("20001")){
                                Toast.makeText(ReceiveDetailActivity.this,"用户不存在",Toast.LENGTH_SHORT).show();
                            }else if (code.equals("10001")){
                                Toast.makeText(ReceiveDetailActivity.this,"柜体无效",Toast.LENGTH_SHORT).show();
                            }else if (code.equals("10002")){
                                Toast.makeText(ReceiveDetailActivity.this,"格口无效",Toast.LENGTH_SHORT).show();
                            }else if (code.equals("15101")){
                                Toast.makeText(ReceiveDetailActivity.this,"订单不存在",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReceiveDetailActivity.this, "连接服务器失败，请检查网络", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> map = new HashMap<>();
                map.put("uid", GlobalData.getUid());
                map.put("order_id",id);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    //检查揽件申请
    private void check(final String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAG", "sendToServer: "+response );
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            String code = jsonObject.getString("code");
                            if (code.equals("0")){
                                JSONObject body = jsonObject.getJSONObject("body");
                                boolean is_receive = body.getBoolean("is_receive");
                                if (is_receive){
                                    Toast.makeText(ReceiveDetailActivity.this,"揽件成功",Toast.LENGTH_SHORT).show();
                                    new Handler(new Handler.Callback() {
                                        @Override
                                        public boolean handleMessage(@NonNull Message msg) {
                                            finish();
                                            return false;
                                        }
                                    }).sendEmptyMessageDelayed(0x123,2000);//延时2秒关闭
                                }else {
                                    Toast.makeText(ReceiveDetailActivity.this,msg,Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(ReceiveDetailActivity.this,msg,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReceiveDetailActivity.this, "连接服务器失败，请检查网络", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> map = new HashMap<>();
                map.put("uid",GlobalData.getUid());
                map.put("order_id",id);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

}