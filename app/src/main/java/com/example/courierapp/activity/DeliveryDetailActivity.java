package com.example.courierapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.courierapp.utils.GlobalData;
import com.example.courierapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.courierapp.utils.GlobalData.URL_HEAD;

public class DeliveryDetailActivity extends Activity implements View.OnClickListener {
    //声明控件
    private TextView tv_cabinetNumber;
    private EditText et_waybillNumber;
    private EditText et_recipientPhone;
    private RadioButton rb_big;
    private RadioButton rb_middle;
    private RadioButton rb_small;
    private RadioButton rb_tiny;
    private TextView tv_big;
    private TextView tv_middle;
    private TextView tv_small;
    private TextView tv_tiny;
    private Button btn_nextDetail;
    private Button bt_deliveryinfo;
    private String number;
    private String phone;
    private String cabinetName ;
    private String cabinetNumber;
    private String cellType;
    private static final String URL_NUMBER = URL_HEAD+"/delivery/bininfo";
    private static final String URL_OPEN = URL_HEAD+"/delivery/openbin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_detail);
        initView();
        initEvent();
    }
    //通过ID获取布局文件中的控件实例
    private void initView() {
        tv_cabinetNumber = findViewById(R.id.tv_cabinetNumber);
        et_waybillNumber = findViewById(R.id.et_waybillNumber);
        et_recipientPhone = findViewById(R.id.et_recipientPhone);

        rb_big = findViewById(R.id.rb_big);
        rb_middle = findViewById(R.id.rb_middle);
        rb_small = findViewById(R.id.rb_small);
        rb_tiny = findViewById(R.id.rb_tiny);

        tv_big = findViewById(R.id.tv_big);
        tv_middle = findViewById(R.id.tv_middle);
        tv_small = findViewById(R.id.tv_small);
        tv_tiny = findViewById(R.id.tv_tiny);
        bt_deliveryinfo = findViewById(R.id.bt_deliveryinfo);
        btn_nextDetail = findViewById(R.id.btn_nextDetail);
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> list = bundle.getStringArrayList("all");
        tv_cabinetNumber.setText("柜体编号:"+list.get(0));
        final String id = list.get(0);
        getAllData(id);
        GlobalData.setCabinetCode(id);


    }
    //为控件绑定具体的单击监听器
    private void initEvent() {
        rb_big.setOnClickListener(this);
        rb_middle.setOnClickListener(this);
        rb_small.setOnClickListener(this);
        rb_tiny.setOnClickListener(this);
        bt_deliveryinfo.setOnClickListener(this);
        btn_nextDetail.setOnClickListener(this);

    }
    //设置控件的背景和文本颜色
    private void clearBackground(){
        tv_big.setBackgroundColor(Color.WHITE);
        tv_big.setTextColor(Color.BLACK);
        tv_middle.setBackgroundColor(Color.WHITE);
        tv_middle.setTextColor(Color.BLACK);
        tv_small.setBackgroundColor(Color.WHITE);
        tv_small.setTextColor(Color.BLACK);
        tv_tiny.setBackgroundColor(Color.WHITE);
        tv_tiny.setTextColor(Color.BLACK);
        rb_big.setTextColor(Color.BLACK);
        rb_middle.setTextColor(Color.BLACK);
        rb_small.setTextColor(Color.BLACK);
        rb_tiny.setTextColor(Color.BLACK);
    }
    //根据选择的控件设置具体单击监听器的执行代码
    @Override
    public void onClick(View view) {
        clearBackground();
        switch (view.getId()){
            case R.id.rb_big:
                tv_big.setBackgroundColor(Color.parseColor("#0079fe"));
                tv_big.setTextColor(Color.WHITE);
                rb_big.setTextColor(Color.WHITE);
                cabinetName = "大箱";
                cabinetNumber = String.valueOf(GlobalData.getBigNumber());
                cellType = GlobalData.getBigCellType();
                break;
            case R.id.rb_middle:
                tv_middle.setBackgroundColor(Color.parseColor("#0079fe"));
                tv_middle.setTextColor(Color.WHITE);
                rb_middle.setTextColor(Color.WHITE);
                cabinetName = "中箱";
                cabinetNumber = String.valueOf(GlobalData.getMiddleNumber());
                cellType = GlobalData.getMiddleCellType();
                break;
            case R.id.rb_small:
                tv_small.setBackgroundColor(Color.parseColor("#0079fe"));
                tv_small.setTextColor(Color.WHITE);
                rb_small.setTextColor(Color.WHITE);
                cabinetName = "小箱";
                cabinetNumber = String.valueOf(GlobalData.getSmallNumber());
                cellType = GlobalData.getSmallCellType();
                break;
            case R.id.rb_tiny:
                tv_tiny.setBackgroundColor(Color.parseColor("#0079fe"));
                tv_tiny.setTextColor(Color.WHITE);
                rb_tiny.setTextColor(Color.WHITE);
                cabinetName = "超小箱";
                cabinetNumber = String.valueOf(GlobalData.getTinyNumber());
                cellType = GlobalData.getTinyCellType();
                break;
            case R.id.bt_deliveryinfo:
                Intent intent = new Intent(DeliveryDetailActivity.this, DeliveryInfoActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.btn_nextDetail:
                if (et_waybillNumber.getText().toString().trim().equals("")){
                    Toast.makeText(DeliveryDetailActivity.this,"请输入快递单号",Toast.LENGTH_SHORT).show();
                }else if (et_recipientPhone.getText().toString().trim().equals("")){
                    Toast.makeText(DeliveryDetailActivity.this,"请输入收件人手机号",Toast.LENGTH_SHORT).show();
                }else {
                    number = et_waybillNumber.getText().toString().trim();
                    phone = et_recipientPhone.getText().toString().trim();
                    Log.e("msg:",cellType+number+phone);
                    Log.e("msg",GlobalData.getUid());
                    Log.e("msg",GlobalData.getCabinetCode());
                    sendToServer(cellType,number,phone);
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if(resultCode==RESULT_OK){
                Bundle bundle = data.getExtras();
                ArrayList<String> list = bundle.getStringArrayList("info");
                et_waybillNumber.setText(list.get(0));
                et_recipientPhone.setText(list.get(1));
            }
        }
    }
    //向服务器查询快递柜格口信息
    private void getAllData(final String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_NUMBER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String msg = jsonObject.getString("msg");
                            if (code.equals("0")){
                                Toast.makeText(DeliveryDetailActivity.this,"查询成功",Toast.LENGTH_SHORT).show();
                                JSONObject body = jsonObject.getJSONObject("body");
                                Bundle bundle = new Bundle();
                                String name = body.getString("name");
                                Log.e(TAG, "onResponse: name = "+name);
                                JSONArray avail_cells = body.getJSONArray("avail_cells");
                                for (int i = 0; i<avail_cells.length();i++){
                                    JSONObject son = avail_cells.getJSONObject(i);
                                    String type = son.getString("type");
                                    switch (type){
                                        case "10901":
                                            GlobalData.setBigCellType(type);
                                            GlobalData.setBigNumber(son.getInt("idle_count"));
                                            tv_big.setText("剩余"+GlobalData.getBigNumber()+"个");
                                            break;
                                        case "10902":
                                            GlobalData.setMiddleCellType(type);
                                            GlobalData.setMiddleNumber(son.getInt("idle_count"));
                                            tv_middle.setText("剩余"+GlobalData.getMiddleNumber()+"个");
                                            break;
                                        case "10903":
                                            GlobalData.setSmallCellType(type);
                                            GlobalData.setSmallNumber(son.getInt("idle_count"));
                                            tv_small.setText("剩余"+GlobalData.getSmallNumber()+"个");
                                            break;
                                        case "10904":
                                            GlobalData.setTinyCellType(type);
                                            GlobalData.setTinyNumber(son.getInt("idle_count"));
                                            tv_tiny.setText("剩余"+GlobalData.getTinyNumber()+"个");
                                            break;
                                        default:
                                            break;

                                    }
                                }

                            }else {
                                Toast.makeText(DeliveryDetailActivity.this,msg,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeliveryDetailActivity.this,"连接服务器失败，请检查网络连接",Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> map = new HashMap<>();
                map.put("uid",GlobalData.getUid());
                map.put("cab_id",id);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    //向服务器发送具体的打开格口请求
    private void sendToServer(final String cellType, final String number, final String phone) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_OPEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("TAG", "onResponse: "+response);
                            JSONObject json = new JSONObject(response);
                            String code = json.getString("code");
                            if (!code.equals("0")){
                                Toast.makeText(DeliveryDetailActivity.this,
                                        json.getString("msg"),Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(DeliveryDetailActivity.this, "开箱成功！",
                                        Toast.LENGTH_SHORT).show();
                                JSONObject body = json.getJSONObject("body");
                                String bin_id = body.getString("bin_id");
                                Intent intent = new Intent(DeliveryDetailActivity.this, DeliveryFinishedActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("number",number);
                                bundle.putString("phone",phone);
                                bundle.putString("cab_id",GlobalData.getCabinetCode());
                                bundle.putString("bin_id",bin_id);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DeliveryDetailActivity.this,"连接服务器失败，请检查网络连接",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("uid",GlobalData.getUid());
                map.put("cab_id",GlobalData.getCabinetCode());
                map.put("cell_type",cellType);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}

