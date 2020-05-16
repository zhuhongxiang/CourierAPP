package com.example.courierapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.courierapp.R;
import com.example.courierapp.entity.DeliveryInfo;

import java.util.List;


public class DeliveryInfoAdapter extends BaseAdapter {
    private List<DeliveryInfo> mContentList;
    private LayoutInflater mInflater;

    public DeliveryInfoAdapter(Context context, List<DeliveryInfo> contentList) {
       mContentList = contentList;
       mInflater = LayoutInflater.from(context);
   }

    @Override
    public int getCount() {
        return mContentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Log.e("TAG", "getView: "+position);
        DeliveryInfo deliveryInfo = mContentList.get(position);
        ViewHolder viewHolder = null;
        if (deliveryInfo == null){
            return null;
        }
        if (view != null){
            viewHolder = (ViewHolder) view.getTag();
        }else {
            view = mInflater.inflate(R.layout.list_item_deliveryinfo,null);
            viewHolder = new ViewHolder();

            viewHolder.tv_infoNumber = view.findViewById(R.id.tv_infoNumber);
            viewHolder.tv_recphone = view.findViewById(R.id.tv_recphone);
            viewHolder.tv_recaddr = view.findViewById(R.id.tv_recaddr);
            view.setTag(viewHolder);
        }
        viewHolder.tv_infoNumber.setText(deliveryInfo.getId());
        viewHolder.tv_recphone.setText(deliveryInfo.getRec_phone());
        viewHolder.tv_recaddr.setText(deliveryInfo.getRec_addr()+deliveryInfo.getRec_detail());
        return view;
    }

    private static class ViewHolder{
        public TextView tv_infoNumber;
        public TextView tv_recphone;
        public TextView tv_recaddr;
    }

}
