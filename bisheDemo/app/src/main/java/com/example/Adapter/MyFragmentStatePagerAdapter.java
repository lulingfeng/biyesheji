package com.example.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.Fragment.list_duanzishou;
import com.example.Fragment.list_gaoxiao;
import com.example.Fragment.list_kongbu;
import com.example.Fragment.list_gushi;
import com.example.Fragment.list_lengzhishi;
import com.example.Fragment.list_mengchong;
import com.example.Fragment.list_qiqu;


/**
 * @Author: pyz
 * @Package: com.pyz.viewpagerdemo.adapter
 * @Description: TODO
 * @Project: ViewPagerDemo
 * @Date: 2016/8/18 11:49
 */
public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    private String[] tabTilte;

    public MyFragmentStatePagerAdapter(FragmentManager fm, String[] tabTitle) {
        super(fm);

        this.tabTilte = tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new list_kongbu();
            case 1:
                return new list_gushi();
            case 2:
               return new list_lengzhishi();
            case 3:
                return  new list_duanzishou();
            case 4:
                return new list_qiqu();
            case 5:
                return  new list_gaoxiao();
            case 6:
                return  new list_mengchong();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabTilte.length;
    }
}
