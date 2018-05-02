package base.zc.com.project.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.json.JSONObject;

import java.util.List;

import base.zc.com.project.R;
import base.zc.com.project.util.UiUtil;

/**
 * Created by Darren on 2018/1/11.
 */

public class BaseListAdapter extends RecyclerView.Adapter<BaseListAdapter.ViewHolder> {
    private Context context;
    private List<JSONObject> dataList;
    public BaseListAdapter(Context context) {
        this.context = context;
    }
    public void updateData(List<JSONObject> list){
        if(dataList == null){
            dataList = list;
        }else{
            dataList.clear();
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }
    public void addData(List<JSONObject> list){
        dataList.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.base_list_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        JSONObject json = dataList.get(position);
        String name = json.optString("name");
        holder.tv_name.setText(name);
        if(position == 0){
            holder.itemView.setBackgroundColor(Color.BLUE);
        }else if(position == 3){
            holder.itemView.setBackgroundColor(Color.BLACK);
        }else{
            holder.itemView.setBackgroundColor(Color.GREEN);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtil.showToast(context, "提示信息" + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(dataList == null){
            return 0;
        }
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
