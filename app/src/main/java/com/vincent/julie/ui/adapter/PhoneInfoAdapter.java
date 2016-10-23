package com.vincent.julie.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vincent.julie.R;
import com.vincent.julie.app.MyApplication;
import com.vincent.julie.entity.PhoneInfo;
import com.vincent.julie.listener.PhoneInfoListener;

import java.util.List;

/**
 * Created by Vincent on 2016/10/23.
 */

public class PhoneInfoAdapter extends RecyclerView.Adapter<PhoneInfoAdapter.ViewHolder> {
    private Context mContext;
    private PhoneInfoListener listener;
    private List<PhoneInfo> data;

    public void setPhoneInfoListener(PhoneInfoListener phoneInfoListener){
        this.listener=phoneInfoListener;
    }

    public void setData(List<PhoneInfo> data){
        this.data=data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent!=null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_info_layout,null);
        return new PhoneInfoAdapter.ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PhoneInfo phoneInfo=data.get(position);
        holder.tvName.setText(phoneInfo.getName());
        holder.tvInfo.setText(phoneInfo.getInfo());
    }

    @Override
    public int getItemCount() {
        return (data!=null&&!data.isEmpty()?data.size():0);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName;
        private TextView tvInfo;
        private LinearLayout llItem;

        public ViewHolder(View itemView,PhoneInfoListener listener) {
            super(itemView);
            tvInfo=(TextView)itemView.findViewById(R.id.tv_info);
            tvName=(TextView)itemView.findViewById(R.id.tv_name);
            llItem=(LinearLayout)itemView.findViewById(R.id.ll_item);

            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v,getAdapterPosition());
                }
            });
        }
    }
}
