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
import com.example.courierapp.adapter.CabinetAdapter;
import com.example.courierapp.entity.Cabinet;
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


public class InformationNumberActivity extends Activity {

        private ListView lv_cab;
        private List<Cabinet> list;
        private CabinetAdapter adapter;
        private ArrayList<ArrayList<String>> storeData;

        private static final String URL_NUMBER = URL_HEAD+"/delivery/cabinfo";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_information_number);
            initView();
            initEvent();
        }
        private void initView() {
            lv_cab = findViewById(R.id.lv_cab);
            list = new ArrayList<>();
            storeData = new ArrayList<>();
            getAllData();
        }

        private void initEvent() {
            lv_cab.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(InformationNumberActivity.this, InformationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("all", storeData.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
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
                                    JSONArray lists = body.getJSONArray("cab_list");
                                    for (int i = 0;i < lists.length();i ++){
                                        ArrayList arrayList = new ArrayList();
                                        JSONObject jsonObject = (JSONObject) lists.get(i);
                                        String cab_id = jsonObject.getString("cab_id");
                                        String cab_name = jsonObject.getString("cab_name");
                                        String addr = jsonObject.getString("addr");
                                        Cabinet cabinet = new Cabinet();
                                        cabinet.setCab_id("柜体编号： "+cab_id);
                                        cabinet.setCab_name("柜体名称： "+cab_name);
                                        cabinet.setAddr("柜体地址： "+addr);
                                        arrayList.add(cab_id);
                                        arrayList.add("柜体名称： "+cab_name);
                                        list.add(cabinet);
                                        storeData.add(arrayList);
                                    }
                                    Toast.makeText(InformationNumberActivity.this,"查询成功",Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onResponse: size "+list.size() );
                                    for (Iterator iterator = list.iterator(); iterator.hasNext();){
                                        Cabinet cabinet = (Cabinet) iterator.next();
                                        Log.e(TAG, "onResponse: "+cabinet.toString() );
                                    }
                                    adapter = new CabinetAdapter(getApplicationContext(),list);
                                    lv_cab.setAdapter(adapter);
                                }else {
                                    Toast.makeText(InformationNumberActivity.this,"查询失败",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(InformationNumberActivity.this,"连接服务器失败，请检查网络连接",Toast.LENGTH_SHORT).show();
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
