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
import com.example.courierapp.entity.Cabinet;

import java.util.List;


public class CabinetAdapter extends BaseAdapter {
    private Context context;
    private List<Cabinet> list;

    public CabinetAdapter(Context context, List<Cabinet> list) {
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Log.e("TAG", "getView: "+position);
        Cabinet cabinet = list.get(position);
        ViewHolder viewHolder = null;
        if (cabinet == null){
            return null;
        }
        if (view != null){
            viewHolder = (ViewHolder) view.getTag();
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_cabinet,null);
            viewHolder = new ViewHolder();

            viewHolder.tv_cab_id = view.findViewById(R.id.tv_cab_id);
            viewHolder.tv_cab_name = view.findViewById(R.id.tv_cab_name);
            viewHolder.tv_addr = view.findViewById(R.id.tv_addr);

            view.setTag(viewHolder);
        }
        viewHolder.tv_cab_id.setText(cabinet.getCab_id());
        viewHolder.tv_cab_name.setText(cabinet.getCab_name());
        viewHolder.tv_addr.setText(cabinet.getAddr());
        return view;
    }

    private static class ViewHolder{
        public TextView tv_cab_id;
        public TextView tv_cab_name;
        public TextView tv_addr;
    }
}
