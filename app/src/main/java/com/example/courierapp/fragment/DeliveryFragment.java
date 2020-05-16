package com.example.courierapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.courierapp.activity.DeliverySelectCabActivity;
import com.example.courierapp.utils.GlobalData;
import com.example.courierapp.activity.InformationNumberActivity;
import com.example.courierapp.activity.MainActivity;
import com.example.courierapp.R;
import com.example.courierapp.entity.Record;
import com.example.courierapp.activity.RecordActivity;
import com.example.courierapp.adapter.RecordAdapter;
import com.example.courierapp.activity.RecordDetailActivity;
import com.example.courierapp.utils.AlphaIndicator;

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

public class DeliveryFragment extends Fragment {
    private View view;
    private Context mContext;
    private LinearLayout ll_delivery;
    private LinearLayout ll_record;
    private LinearLayout ll_yuyue;
    private LinearLayout ll_information;
    private ListView lv_record;
    private List<Record> list;
    private RecordAdapter adapter;
    private ArrayList<ArrayList<String>> storeData;
    AlphaIndicator alphaIndicator;
    //是否第一次加载
    private boolean isFirstLoading = true;
    private static final String URL_RECORD = URL_HEAD+"/delivery/order/record";
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            alphaIndicator = (AlphaIndicator) mainActivity.findViewById(R.id.alphaIndicator);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        if (view!=null) {  // mRootView 不为null时候，返回之间创建的mRootView，不会再进行初始化操作了
            return view;
        }
        view= LayoutInflater.from(mContext).inflate(R.layout.fragment_delivery,null);
        ll_delivery = (LinearLayout) view.findViewById(R.id.ll_delivery);
        ll_record = (LinearLayout) view.findViewById(R.id.ll_record);
        ll_yuyue = (LinearLayout) view.findViewById(R.id.ll_yuyue);
        ll_information = (LinearLayout) view.findViewById(R.id.ll_information);
        lv_record = (ListView) view.findViewById(R.id.lv_record);
        list = new ArrayList<>();
        storeData = new ArrayList<>();
        ll_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DeliverySelectCabActivity.class);
                mContext.startActivity(intent);
            }
        });
        ll_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RecordActivity.class);
                mContext.startActivity(intent);
            }
        });
        ll_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InformationNumberActivity.class);
                mContext.startActivity(intent);
            }
        });
        ll_yuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"该功能尚在开发中，敬请等待！",Toast.LENGTH_SHORT).show();
            }
        });
        updateUi();
        initEvent();
        Log.i("type", "onCreateView");
        return view;
    }
    //为ListView绑定单击监听器
    private void initEvent() {
        lv_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(mContext, RecordDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("all", storeData.get(position));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }
    /**
     * 更新数据
     */
    private void updateUi() {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
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
                                Toast.makeText(mContext,"查询成功",Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onResponse: size "+list.size() );
                                for (Iterator iterator = list.iterator();iterator.hasNext();){
                                    Record record = (Record) iterator.next();
                                    Log.e(TAG, "onResponse: "+record.toString() );
                                }
                                adapter = new RecordAdapter(mContext.getApplicationContext(),list);
                                lv_record.setAdapter(adapter);
                            }else {
                                Toast.makeText(mContext,"查询失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "连接服务器失败，请检查网络", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 在fragment可见的时候，刷新数据
     */
    @Override
    public void onResume() {
        super.onResume();

        if (!isFirstLoading) {
            //如果不是第一次加载，刷新数据
            list = new ArrayList<>();
            storeData = new ArrayList<>();
            updateUi();
        }
        isFirstLoading = false;
    }

}
