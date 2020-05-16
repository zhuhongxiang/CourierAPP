package com.example.courierapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.courierapp.R;
import com.example.courierapp.adapter.ReceiveAdapter;
import com.example.courierapp.entity.Receive;
import com.example.courierapp.utils.GlobalData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.courierapp.utils.GlobalData.URL_HEAD;


public class ReceiveActivity extends Activity {
    //声明控件
    private ListView lv_receive;
    private ReceiveAdapter adapter;
    private List<Receive> lists;
    private ArrayList<ArrayList<String>> storeData;
    private static final String URL_PICKUP = URL_HEAD+"/receive/list";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        init();
        initEvent();
    }
    private void init() {
        //通过ID获取ListView控件
    lv_receive = findViewById(R.id.lv_receive);
    lists = new ArrayList<>();
    storeData = new ArrayList<>();
    postToServer();
}
    //为ListView绑定单击监听器
    private void initEvent() {
        lv_receive.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(ReceiveActivity.this, ReceiveDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("all", storeData.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
    //向服务器查询待取件信息
    private void postToServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PICKUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("TAG", "onResponse: content"+response );
                            String code = jsonObject.getString("code");
                            if (code.equals("0")){
                                Toast.makeText(ReceiveActivity.this,"查询成功",Toast.LENGTH_SHORT).show();
                                JSONObject body = jsonObject.getJSONObject("body");
                                JSONArray order_list = body.getJSONArray("order_list");
                                for (int i = 0;i<order_list.length();i++){
                                    ArrayList arrayList = new ArrayList();
                                    JSONObject list = (JSONObject) order_list.get(i);
                                    String in_time = list.getString("in_time");
                                    String open_code = list.getString("open_code");
                                    JSONObject addr_info = list.getJSONObject("addr_info");
                                    String number = list.getString("exp_code");
                                    String address = addr_info.getString("name")+addr_info.getString("desc");
                                    Receive receive = new Receive();
                                    receive.setCode(number);
                                    receive.setAddress(address);
                                    receive.setOpenCode(open_code);
                                    receive.setSendTime(in_time);
                                    arrayList.add("寄件订单号： "+number);
                                    arrayList.add("存储位置："+address);
                                    arrayList.add("寄件时间： "+in_time);
                                    arrayList.add("揽件密码： "+open_code);
                                    arrayList.add(list.getString("id"));
                                    lists.add(receive);
                                    storeData.add(arrayList);
                                    Log.e("TAG", "onResponse: "+receive.toString() );
                                }
                                adapter = new ReceiveAdapter(getApplicationContext(),lists);
                                lv_receive.setAdapter(adapter);
                            }else {
                                Toast.makeText(ReceiveActivity.this,"查询失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReceiveActivity.this, "连接服务器失败，请检查网络", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> map = new HashMap<>();
                map.put("uid", GlobalData.getUid());
                map.put("status","1");
                Log.i("Receive params", map.toString());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
