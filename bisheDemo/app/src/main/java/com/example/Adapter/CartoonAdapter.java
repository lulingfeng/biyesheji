package com.example.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bishedemo.R;
import com.example.entity.Cartoon;

import java.util.List;

public class CartoonAdapter extends ArrayAdapter<Cartoon> {
    private final int viewId;
    public CartoonAdapter(Context context, int resourceId, List<Cartoon>list){
        super(context,resourceId,list);
        viewId=resourceId;
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        Cartoon cartoon=getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view=LayoutInflater.from(getContext()).inflate(viewId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.cartoonContent=view.findViewById(R.id.cartoonContent);
            viewHolder.cartoonImage=view.findViewById(R.id.cartoonImage);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.cartoonContent.setText(cartoon.getContent());
        return view;
    }

    private class ViewHolder {
        TextView cartoonImage;
        TextView cartoonContent;
    }
}

