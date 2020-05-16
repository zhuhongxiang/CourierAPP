package com.example.courierapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.courierapp.utils.GlobalData;
import com.example.courierapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.courierapp.utils.GlobalData.URL_HEAD;

public class DeliveryFinishedActivity extends Activity implements View.OnClickListener {
    //声明控件
    private TextView tv_numberFinished;
    private TextView tv_phoneFinished;
    private TextView tv_cabinetNameFinished;
    private TextView tv_cabinetNumberFinished;
    private Button btn_deliveryCancel;
    private Button btn_deliverySure;
    private String id;
    private String cab_id;
    private String bin_id;
    private static final String URL_FINISH = URL_HEAD+"/delivery/order/new";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_finished);
        initView();
        initEvent();
    }
    //通过ID获取布局文件中的控件
    private void initView() {
        tv_numberFinished = findViewById(R.id.tv_numberFinished);
        tv_phoneFinished = findViewById(R.id.tv_phoneFinished);
        tv_cabinetNameFinished = findViewById(R.id.tv_cabinetNameFinished);
        tv_cabinetNumberFinished = findViewById(R.id.tv_cabinetNumberFinished);

        btn_deliveryCancel = findViewById(R.id.btn_deliveryCancel);
        btn_deliverySure = findViewById(R.id.btn_deliverySure);
        //使控件显示具体的投件信息
        Bundle bundle = getIntent().getExtras();
        tv_numberFinished.setText(bundle.getString("number"));
        tv_phoneFinished.setText( bundle.getString("phone"));
        tv_cabinetNameFinished.setText(bundle.getString("cab_id"));
        tv_cabinetNumberFinished.setText( bundle.getString("bin_id"));
    }
    //为Button绑定具体的单击监听器
    private void initEvent() {
        btn_deliveryCancel.setOnClickListener(this);
        btn_deliverySure.setOnClickListener(this);
    }
    //根据Button的不同执行不同的监听器代码
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_deliveryCancel:
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        finish();
                        return false;
                    }
                }).sendEmptyMessageDelayed(0x123,2000);//延时2秒关闭
                break;
            case R.id.btn_deliverySure:
                id=tv_numberFinished.getText().toString().trim();
                cab_id=tv_cabinetNameFinished.getText().toString().trim();
                bin_id=tv_cabinetNumberFinished.getText().toString().trim();
                postToServe();
                break;
            default:
                break;
        }
    }
    //向服务器发送具体的投递信息
    private void postToServe() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FINISH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String code = json.getString("code");
                            if (code.equals("0")){
                                Toast.makeText(DeliveryFinishedActivity.this,"投递成功",Toast.LENGTH_SHORT).show();
                                new Handler(new Handler.Callback() {
                                    @Override
                                    public boolean handleMessage(@NonNull Message msg) {
                                        finish();
                                        return false;
                                    }
                                }).sendEmptyMessageDelayed(0x123,2000);//延时2秒关闭
                            }else if (code.equals("15100")){
                                Toast.makeText(DeliveryFinishedActivity.this,"运单不存在",Toast.LENGTH_SHORT).show();
                            }else if (code.equals("10001")){
                                Toast.makeText(DeliveryFinishedActivity.this,"柜体无效",Toast.LENGTH_SHORT).show();
                            }else if (code.equals("10002")){
                                Toast.makeText(DeliveryFinishedActivity.this,"格口无效",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DeliveryFinishedActivity.this,"连接服务器失败，请检查网络连接",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("uid", GlobalData.getUid());
                map.put("id",id);
                map.put("cab_id",cab_id);
                map.put("bin_id",bin_id);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
