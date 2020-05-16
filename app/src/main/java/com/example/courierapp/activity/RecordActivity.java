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
import com.example.courierapp.adapter.RecordAdapter;
import com.example.courierapp.entity.Record;
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

public class RecordActivity extends Activity {
    //声明控件
    private List<Record> list;
    private ListView lv_record;
    private RecordAdapter adapter;
    private ArrayList<ArrayList<String>> storeData;

    private static final String URL_RECORD = URL_HEAD+"/delivery/order/record";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveryrecord);
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
                Intent intent = new Intent(RecordActivity.this, RecordDetailActivity.class);
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
                                JSONArray lists = body.getJSONArray("record_list");
                                for (int i = 0;i < lists.length();i ++){
                                    ArrayList arrayList = new ArrayList();
                                    JSONObject jsonObject = (JSONObject) lists.get(i);
                                    String delivery_id = jsonObject.getString("delivery_id");
                                    String id = jsonObject.getString("id");
                                    String out_time = jsonObject.getString("out_time");
                                    String in_time = jsonObject.getString("in_time");
                                    String rec_phone = jsonObject.getString("rec_phone");
                                    String addr = jsonObject.getString("addr");
                                    Record record = new Record();
                                    record.setDeliveryNumber("运单号： "+id);
                                    record.setDeliveryTime("投递时间： "+in_time);
                                    arrayList.add("运单号： "+id);
                                    arrayList.add("收件人手机号："+rec_phone);
                                    arrayList.add("投递时间： "+in_time);
                                    arrayList.add("存储位置： "+addr);
                                    arrayList.add(delivery_id);
                                    if (out_time.equals("未取件")){
                                        record.setStatus("未取件");
                                        arrayList.add("未取件");
                                    }else {
                                        record.setStatus(out_time +"  已取件");
                                        arrayList.add(out_time +"  已取件");
                                    }
                                    list.add(record);
                                    storeData.add(arrayList);
                                }
                                Toast.makeText(RecordActivity.this,"查询成功",Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onResponse: size "+list.size() );
                                for (Iterator iterator = list.iterator();iterator.hasNext();){
                                    Record record = (Record) iterator.next();
                                    Log.e(TAG, "onResponse: "+record.toString() );
                                }
                                adapter = new RecordAdapter(getApplicationContext(),list);
                                lv_record.setAdapter(adapter);
                            }else {
                                Toast.makeText(RecordActivity.this,"查询失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecordActivity.this, "连接服务器失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("uid", GlobalData.getUid());
                map.put("status","1");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
