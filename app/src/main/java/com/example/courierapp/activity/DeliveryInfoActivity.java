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
import com.example.courierapp.adapter.DeliveryInfoAdapter;
import com.example.courierapp.utils.GlobalData;
import com.example.courierapp.R;
import com.example.courierapp.entity.DeliveryInfo;

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

public class DeliveryInfoActivity extends Activity {
    private ListView lv_deliveryinfo;
    private List<DeliveryInfo> list;
    private DeliveryInfoAdapter adapter;
    private ArrayList<ArrayList<String>> storeData;

    private static final String URL_NUMBER = URL_HEAD+"/delivery/deliveryinfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_info);
        initView();
        initEvent();
    }
    private void initView() {
        lv_deliveryinfo = findViewById(R.id.lv_deliveryinfo);
        list = new ArrayList<>();
        storeData = new ArrayList<>();
        getAllData();
    }
    private void initEvent() {
        lv_deliveryinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(DeliveryInfoActivity.this, DeliveryDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("info", storeData.get(position));
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
    private void getAllData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_NUMBER,
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
                                JSONArray lists = body.getJSONArray("info_list");
                                for (int i = 0;i < lists.length();i ++){
                                    ArrayList arrayList = new ArrayList();
                                    JSONObject jsonObject = (JSONObject) lists.get(i);
                                    String id = jsonObject.getString("id");
                                    String rec_phone = jsonObject.getString("rec_phone");
                                    String rec_addr = jsonObject.getString("rec_addr");
                                    String rec_detail = jsonObject.getString("rec_detail");
                                    DeliveryInfo deliveryInfo = new DeliveryInfo();
                                    deliveryInfo.setId("运单号： "+id);
                                    deliveryInfo.setRec_phone("收件人手机号： "+rec_phone);
                                    deliveryInfo.setRec_addr("收件地址： "+rec_addr);
                                    deliveryInfo.setRec_detail(rec_detail);
                                    arrayList.add(id);
                                    arrayList.add(rec_phone);
                                    list.add(deliveryInfo);
                                    storeData.add(arrayList);
                                }
                                Toast.makeText(DeliveryInfoActivity.this,"查询成功",Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onResponse: size "+list.size() );
                                for (Iterator iterator = list.iterator(); iterator.hasNext();){
                                    DeliveryInfo deliveryInfo = (DeliveryInfo) iterator.next();
                                    Log.e(TAG, "onResponse: "+deliveryInfo.toString() );
                                }
                                adapter = new DeliveryInfoAdapter(getApplicationContext(),list);
                                lv_deliveryinfo.setAdapter(adapter);
                            }else {
                                Toast.makeText(DeliveryInfoActivity.this,"查询失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeliveryInfoActivity.this,"连接服务器失败，请检查网络连接",Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> map = new HashMap<>();

                map.put("uid", GlobalData.getUid());
                map.put("orderby","asc");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

}
