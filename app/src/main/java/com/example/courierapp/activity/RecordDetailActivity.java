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

public class RecordDetailActivity extends Activity {
    //声明控件
    private TextView tv_numberRecord;
    private TextView tv_phoneRecord;
    private TextView tv_addressRecord;
    private TextView tv_deliveryTimeRecord;
    private TextView tv_pickupTimeRecord;
    private Button btn_retrieve;
    private static final String URL_CALLBACK = URL_HEAD+"/delivery/order/callback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveryrecorddetail);
        initView();
    }
    //通过ID获取布局文件中的控件
    private void initView() {
        tv_numberRecord = findViewById(R.id.tv_numberRecord);
        tv_phoneRecord = findViewById(R.id.tv_phoneRecord);
        tv_addressRecord = findViewById(R.id.tv_addressRecord);
        tv_deliveryTimeRecord = findViewById(R.id.tv_deliveryTimeRecord);
        tv_pickupTimeRecord = findViewById(R.id.tv_pickupTimeRecord);
        btn_retrieve = findViewById(R.id.btn_retrieve);
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> list = bundle.getStringArrayList("all");
        tv_numberRecord.setText(list.get(0));
        tv_phoneRecord.setText(list.get(1));
        tv_deliveryTimeRecord.setText(list.get(2));
        tv_addressRecord.setText(list.get(3));
        tv_pickupTimeRecord.setText(list.get(5));
        final String id = list.get(4);
        Log.e(TAG, "initView: "+id );
        //为取回快件按钮绑定监听器
        btn_retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieve(id);
            }
        });
    }
    //向服务器发送取回快件申请
    private void retrieve(final String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CALLBACK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            String code = jsonObject.getString("code");
                            if (code.equals("0")){
                                Toast.makeText(RecordDetailActivity.this,"取回快件成功",Toast.LENGTH_SHORT).show();
                                new Handler(new Handler.Callback() {
                                    @Override
                                    public boolean handleMessage(@NonNull Message msg) {
                                        finish();
                                        return false;
                                    }
                                }).sendEmptyMessageDelayed(0x123,2000);//延时2秒关闭
                            }else{
                                Toast.makeText(RecordDetailActivity.this,"取回快件失败，"+msg,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecordDetailActivity.this, "连接服务器失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> map = new HashMap<>();
                map.put("uid", GlobalData.getUid());
                map.put("order_id",id);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

}