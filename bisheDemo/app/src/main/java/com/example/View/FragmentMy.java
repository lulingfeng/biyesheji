package com.example.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;
import com.example.bishedemo.R;

public class FragmentMy extends Fragment {
    private boolean isGetData = false;
    TextView textView;
    String data,mData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.my,container,false);
        textView=view.findViewById(R.id.loginMain);
        return view;
    }



    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;
            //   这里可以做网络请求或者需要的数据刷新操作
            if ((data = getArguments().getString("用户姓名"))!=null&&!(data = getArguments().getString("用户姓名")).equals("")) {
                textView.setText(data);
                Log.e("llf","刷新数据了");
            }

        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

}
