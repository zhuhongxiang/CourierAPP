package com.example.courierapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.courierapp.R;
import com.example.courierapp.entity.Receive;

import java.util.List;

public class ReceiveAdapter extends BaseAdapter {
    private Context context;
    private List<Receive> list;
    //建立PickUpAdapter方法
    public ReceiveAdapter(Context context, List<Receive> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    //建立getView方法，实现对应控件的获取
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Receive receive = list.get(position);
        ViewHolder holder = null;
        if (list == null){
            return null;
        }
        if (view != null){
            holder = (ViewHolder) view.getTag();
        }else {
            //通过ID获取具体的控件
            view = LayoutInflater.from(context).inflate(R.layout.list_item_receive,null);
            holder = new ViewHolder();
            holder.tv_billNumberSend = view.findViewById(R.id.tv_billNumberSend);
            holder.tv_address = view.findViewById(R.id.tv_address);
            holder.tv_sendTime = view.findViewById(R.id.tv_sendTime);
            view.setTag(holder);

        }
        //设置对应控件的内容
        holder.tv_billNumberSend.setText("寄件订单编号："+receive.getCode());
        holder.tv_address.setText("存储位置："+receive.getAddress());
        holder.tv_sendTime.setText("寄件时间："+receive.getSendTime());
        return view;
    }
    //建立ViewHolder类，实例化控件
    private static class ViewHolder{
        public TextView tv_billNumberSend;
        public TextView tv_address;
        public TextView tv_sendTime;
    }


}
