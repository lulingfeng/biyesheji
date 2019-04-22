package com.example.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.entity.Cartoon;

import java.util.List;

public class CartoonAdapter extends ArrayAdapter {
    private final int viewId;
    List<Cartoon> list;
    public CartoonAdapter(Context context, int resourceId, List<Cartoon>list){
        super(context,resourceId,list);
        viewId=resourceId;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
}
