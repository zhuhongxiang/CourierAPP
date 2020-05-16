package com.example.courierapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.courierapp.R;
import com.example.courierapp.entity.Grid;

import java.util.List;


public class GridAdapter extends BaseAdapter {
    private Context context;
    private List<Grid> list;

    public GridAdapter(Context context, List<Grid> list) {
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        Log.e("TAG", "getView: "+position);
        Grid grid = list.get(position);
//        Log.e(TAG, "onResponse: " + grid.getGridId() + "\t"+grid.getGridSize()+"\t" + grid.getGridStatus());
        ViewHolder holder = null;
        if (grid == null){
            return null;
        }
        if (view != null){
            holder = (ViewHolder) view.getTag();

        }else {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_information,null);

            holder = new ViewHolder();
            holder.tv_informationNumber = view.findViewById(R.id.tv_informationNumber);
            holder.tv_informationSize = view.findViewById(R.id.tv_informationSize);
            holder.tv_informationStatus = view.findViewById(R.id.tv_informationStatus);
            view.setTag(holder);
        }


        holder.tv_informationNumber.setText(""+grid.getGridId());
        holder.tv_informationSize.setText(grid.getGridSize());
        holder.tv_informationStatus.setText(grid.getGridStatus());
        return view;
    }
    private static class ViewHolder{
        public TextView tv_informationNumber;
        public TextView tv_informationSize;
        public TextView tv_informationStatus;
    }
}
