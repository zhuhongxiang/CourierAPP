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
import com.example.courierapp.entity.Record;
import com.example.courierapp.adapter.RecordAdapter;
import com.example.courierapp.utils.GlobalData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.courierapp.utils.GlobalData.URL_HEAD;

public class ReceiveRecordActivity extends Activity {
    //声明控件
    private List<Record> list;
    private ListView lv_record;
    private RecordAdapter adapter;
    private ArrayList<ArrayList<String>> storeData;

    private static final String URL_RECORD = URL_HEAD+"/receive/record";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiverecord);
        initView();
        initEvent();
    }
    //通过ID获取布局文件中的控件
    private void initView() {
        lv_record = findViewById(R.id.lv_record);
        list = new ArrayList<>();
        storeData = new ArrayList<>();
        getAllData();
    }
    //为ListView绑定单击监听器
    private void initEvent() {
        lv_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(ReceiveRecordActivity.this, ReceiveRecordDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("all", storeData.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
    //向服务器查询投递记录信息
    private void getAllData(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RECORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String code = obj.getString("code");
                            if (code.equals("0")){
                                JSONObject body = obj.getJSONObject("body");
                                int count = body.getInt("count");
                                Log.e(TAG, "onResponse: "+count );
                                JSONArray lists = body.getJSONArray("order_list");
                                for (int i = 0;i < lists.length();i ++){
                                    ArrayList arrayList = new ArrayList();
                                    JSONObject jsonObject = (JSONObject) lists.get(i);
                                    String inTime = jsonObject.getString("in_time");
                                    String outTime = jsonObject.getString("out_time");
                                    String status = jsonObject.getString("status_desc");
                                    String exp_code = jsonObject.getString("exp_code");
                                    String sender_phone = jsonObject.getString("sender_phone");
                                    JSONObject addr_info = jsonObject.getJSONObject("addr_info");
                                    String address = addr_info.getString("name")+addr_info.getString("desc");
                                    Record record = new Record();
                                    record.setDeliveryNumber("寄件订单号： "+exp_code);
                                    record.setDeliveryTime("寄件时间： "+inTime);
                                    arrayList.add("寄件订单号： "+exp_code);
                                    arrayList.add("寄件人手机号："+sender_phone);
                                    arrayList.add("存储位置："+address);
                                    arrayList.add("寄件时间： "+inTime);
                                    if (status.equals("未揽件")){
                                        record.setStatus(status);
                                        arrayList.add(status);
                                    }else {
                                        record.setStatus(outTime +"  "+status);
                                        arrayList.add("揽件时间： " + outTime);
                                    }
                                    list.add(record);
                                    storeData.add(arrayList);
                                }
                                Toast.makeText(ReceiveRecordActivity.this,"查询成功",Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onResponse: size "+list.size() );
                                for (Iterator iterator = list.iterator();iterator.hasNext();){
                                    Record record = (Record) iterator.next();
                                    Log.e(TAG, "onResponse: "+record.toString() );
                                }
                                adapter = new RecordAdapter(getApplicationContext(),list);
                                lv_record.setAdapter(adapter);
                            }else {
                                Toast.makeText(ReceiveRecordActivity.this,"查询失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReceiveRecordActivity.this, "连接服务器失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("uid", GlobalData.getUid());
                map.put("orderby","desc");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
