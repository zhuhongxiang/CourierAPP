package com.example.courierapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.courierapp.R;
import com.example.courierapp.adapter.GridAdapter;
import com.example.courierapp.entity.Grid;
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

public class InformationActivity extends Activity {
    //声明控件
    private ListView lv_information;
    private List<Grid> list;
    private GridAdapter adapter;
    private static final String URL_GRID = URL_HEAD+"/delivery/information";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_detail);
        init();
    }
    //通过ID获取布局文件中的控件
    private void init() {
        lv_information = findViewById(R.id.lv_information);
        list = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> list = bundle.getStringArrayList("all");
        final String id = list.get(0);
        sendToServer(id);
    }
    //向服务器查询格口信息
    private void sendToServer(final String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GRID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String code = obj.getString("code");
                            String msg = obj.getString("msg");
                            if (code.equals("0")){
                            JSONObject body = obj.getJSONObject("body");
                            int count = body.getInt("count");
                            Log.e(TAG, "onResponse: "+count );
                            JSONArray lists = body.getJSONArray("bin_list");
                                for (int i = 0;i < lists.length();i ++){
                                    ArrayList arrayList = new ArrayList();
                                    JSONObject jsonObject = (JSONObject) lists.get(i);
                                    String bin_id = jsonObject.getString("bin_id");
                                    String type_desc = jsonObject.getString("type_desc");
                                    String status_desc = jsonObject.getString("status_desc");
                                    Grid grid = new Grid();
                                    grid.setGridId(bin_id);
                                    grid.setGridSize(type_desc);
                                    grid.setGridStatus(status_desc);
                                    list.add(grid);
                                }
                                Toast.makeText(InformationActivity.this,"查询成功",Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onResponse: size "+list.size() );
                                for (Iterator iterator = list.iterator(); iterator.hasNext();){
                                    Grid grid = (Grid) iterator.next();
                                    Log.e(TAG, "onResponse: "+grid.toString() );
                                }
                                adapter = new GridAdapter(getApplicationContext(),list);
                                lv_information.setAdapter(adapter);
                            }else {
                                Toast.makeText(InformationActivity.this,msg,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InformationActivity.this,"连接服务器失败，请检查网络连接",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> map = new HashMap<>();
                map.put("uid", GlobalData.getUid());
                map.put("cab_id",id);
                map.put("orderby","asc");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
