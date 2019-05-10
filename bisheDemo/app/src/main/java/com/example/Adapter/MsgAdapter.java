package com.example.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.entity.Msg;
import com.example.bishedemo.R;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private List<Msg> mMsgList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        public ViewHolder(@NonNull View view) {
            super(view);
            leftLayout=view.findViewById(R.id.leftLayout);
            rightLayout=view.findViewById(R.id.rightLayout);
            leftMsg=view.findViewById(R.id.leftMsg);
            rightMsg=view.findViewById(R.id.rightMsg);
        }
    }
    public  MsgAdapter(List<Msg> msgList){
        mMsgList=msgList;
    }
    @NonNull
    @Override
    public MsgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.msg_item,viewGroup,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgAdapter.ViewHolder holder, int position) {
                Msg msg=mMsgList.get(position);
                if (msg.getType()==Msg.TYPE_RECEIVED){  //左边消息布局
                    holder.leftLayout.setVisibility(View.VISIBLE);
                    holder.rightLayout.setVisibility(View.GONE);
                    holder.leftMsg.setText(msg.getContent());
                }else if (msg.getType()==Msg.TYPE_SENT){
                    holder.rightLayout.setVisibility(View.VISIBLE);
                    holder.leftLayout.setVisibility(View.GONE);
                    holder.rightMsg.setText(msg.getContent());
                }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
}
