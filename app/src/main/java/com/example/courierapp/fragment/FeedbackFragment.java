package com.example.courierapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.courierapp.utils.GlobalData;
import com.example.courierapp.activity.MainActivity;
import com.example.courierapp.utils.NetworkUtil;
import com.example.courierapp.R;
import com.example.courierapp.utils.AlphaIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.courierapp.utils.GlobalData.URL_HEAD;

public class FeedbackFragment extends Fragment {
    private View view;
    private Context mContext;
    private Spinner spin_back;
    private EditText et_backmsg;
    private Button btn_feedback;
    private String str_backmsg;
    private String str_backtype;
    AlphaIndicator alphaIndicator;
    private static final String URL_FEEDBACK =URL_HEAD+"/feedback/courier/back";
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
        view = inflater.inflate(R.layout.fragment_feedback, container, false);
        initView();
        return view;
    }
    void initView() {
        spin_back = (Spinner) view.findViewById(R.id.spin_back);
        et_backmsg = (EditText) view.findViewById(R.id.et_backmsg);
        btn_feedback = (Button) view.findViewById(R.id.btn_feedback);
        spin_back.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_backtype = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_backmsg = et_backmsg.getText().toString().trim();
                // 如果手机号、验证码、密码不为空时执行
                if (!str_backtype.equals("") && !str_backmsg.equals("")) {

                    // NetworkUtil.isNetworkAvailable(RegisterActivity.this)来判断当前网络环境是否可用，可用返回true
                    if (!NetworkUtil.isNetworkAvailable(mContext)) {
                        Toast.makeText(mContext, "访问服务器失败，请检查网络",
                                Toast.LENGTH_LONG).show();
                    } else {
                        //在保证网络可用的前提下调用向服务器注册用户的方法
                        feedbackToServer();
                        et_backmsg.setText(null);
                    }
                }
            }
        });
    }
    /**
     * 向服务器发送反馈信息
     */
    void feedbackToServer( ) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        //将JSONObject作为，将上一步得到的JSONObject对象作为参数传入
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FEEDBACK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("请求成功", "response -> " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            String code = json.getString("code");
                            String msg = json.getString("msg");
                            // 判断解析后的code状态码
                            switch (code) {
                                case "0":
                                    Toast.makeText(mContext, "反馈成功",
                                            Toast.LENGTH_LONG).show();
                                    et_backmsg.setText("");
                                    spin_back.setPrompt(null);
                                    break;
                                default:
                                    Toast.makeText(mContext, msg,
                                            Toast.LENGTH_LONG).show();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("请求失败", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();

                map.put("id", GlobalData.getUid());
                map.put("backtype", str_backtype);
                map.put("backmsg", str_backmsg);
                Log.i("FEEDBACK params", map.toString());
                return map;
            }
        };
        //将请求添加到请求队列
        requestQueue.add(stringRequest);
    }

}
