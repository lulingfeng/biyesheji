package com.example.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.bishedemo.R;

import java.util.List;

public class DataAdapter extends ArrayAdapter<Bitmap> {
    private final int viewId;
    private boolean scrollState=false;
    public DataAdapter(Context context, int resourceId, List<Bitmap> list){
        super(context,resourceId,list);
        viewId=resourceId;
    }

    public void setScrollState(boolean scrollState) {
        this.scrollState = scrollState;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bitmap imageData=getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view= LayoutInflater.from(getContext()).inflate(viewId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.cartoonImage=view.findViewById(R.id.cartoonImage);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }if (!scrollState){
            viewHolder.cartoonImage.setImageBitmap(getItem(position)); //填充数据
            viewHolder.cartoonImage.setTag("1");
        }else {
            viewHolder.cartoonImage.setImageResource(R.mipmap.denglu);
            viewHolder.cartoonImage.setTag("2");
        }

        return view;
    }

    private class ViewHolder {
        ImageView cartoonImage;

    }
}
