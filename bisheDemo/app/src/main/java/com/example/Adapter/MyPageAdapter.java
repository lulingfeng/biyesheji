package com.example.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

public class MyPageAdapter extends PagerAdapter {
  List<View> list;
  public MyPageAdapter(List<View> list){
    this.list=list;
  }
  @Override

  public int getCount() { //返回数量
    return list.size();
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
    return view==o;
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    View view=list.get(position);//取出数据

    container.addView(view);
    return view;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    // super.destroyItem(container, position, object);
    View view=list.get(position);
    container.removeView(view);
  }
}
