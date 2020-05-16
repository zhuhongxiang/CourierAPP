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
import com.example.courierapp.R;
import com.example.courierapp.activity.ReceiveRecordActivity;
import com.example.courierapp.activity.MainActivity;
import com.example.courierapp.activity.ReceiveActivity;
import com.example.courierapp.activity.ReceiveDetailActivity;
import com.example.courierapp.adapter.ReceiveAdapter;
import com.example.courierapp.entity.Receive;
import com.example.courierapp.utils.AlphaIndicator;
import com.example.courierapp.utils.GlobalData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.courierapp.utils.GlobalData.URL_HEAD;

public class ReceiveFragment extends Fragment {
    private View view;
    private Context mContext;
    private LinearLayout ll_receive;
    private LinearLayout ll_rec_record;
    private ListView lv_rec_record;
    private ReceiveAdapter adapter;
    private List<Receive> lists;
    private ArrayList<ArrayList<String>> storeData;
    AlphaIndicator alphaIndicator;
    //是否第一次加载
    private boolean isFirstLoading = true;
    private static final String URL_PICKUP = URL_HEAD+"/receive/list";
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
        view= LayoutInflater.from(mContext).inflate(R.layout.fragment_receive,null);
        init();
        initEvent();
        return view;
    }
    private void init() {
        //通过ID获取ListView控件
        ll_receive = (LinearLayout) view.findViewById(R.id.ll_receive);
        ll_rec_record = (LinearLayout) view.findViewById(R.id.ll_rec_record);
        lv_rec_record = (ListView) view.findViewById(R.id.lv_rec_record);
        lists = new ArrayList<>();
        storeData = new ArrayList<>();
        ll_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReceiveActivity.class);
                mContext.startActivity(intent);
            }
        });
        ll_rec_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReceiveRecordActivity.class);
                mContext.startActivity(intent);
            }
        });
        updateUi();
    }
    //为ListView绑定单击监听器
    private void initEvent() {
        lv_rec_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(mContext, ReceiveDetailActivity.class);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PICKUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("TAG", "onResponse: content"+response );
                            String code = jsonObject.getString("code");
                            if (code.equals("0")){
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
                                    arrayList.add("柜体格口信息："+address);
                                    arrayList.add("寄件时间： "+in_time);
                                    arrayList.add("揽件密码： "+open_code);
                                    arrayList.add(list.getString("id"));
                                    lists.add(receive);
                                    storeData.add(arrayList);
                                    Log.e("TAG", "onResponse: "+receive.toString() );
                                }
                                adapter = new ReceiveAdapter(mContext.getApplicationContext(),lists);
                                lv_rec_record.setAdapter(adapter);
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
            protected Map<String, String> getParams(){
                Map<String,String> map = new HashMap<>();
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
            lists = new ArrayList<>();
            storeData = new ArrayList<>();
            updateUi();
        }

        isFirstLoading = false;
    }

}
