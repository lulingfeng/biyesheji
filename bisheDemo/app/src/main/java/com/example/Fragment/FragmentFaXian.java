package com.example.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.util.GlideImageLoader;
import com.example.bishedemo.R;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentFaXian extends Fragment {
    Context context;
    Banner banner;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.faxian,container,false);
        banner=new Banner(this.getActivity());
        banner=view.findViewById(R.id.banner);
       List<Integer> images=new ArrayList<>();
        images.add(R.mipmap.house);
        images.add(R.mipmap.house);
        images.add(R.mipmap.house);
        images.add(R.mipmap.house);
        images.add(R.mipmap.house);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(images);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                switch (position){
                    case 0:
                        Toast.makeText(getActivity(),"asdasd",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(),"lalala",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        banner.start();
        return view;

    }

}
