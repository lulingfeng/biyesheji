package com.example.View;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.bishedemo.R;

public class MySettings extends Activity implements View.OnClickListener {
    TextView vipSetting,vipPolicy,vipMoney;
    ImageView vipImageSetting,vipImagePolicy,vipImageMoney;
    LinearLayout layout1,layout2,layout3,layout4,layout5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my);
        initView();

    }

    private void initView() {
        vipImageSetting=findViewById(R.id.vipImageSetting);
        layout1=findViewById(R.id.vipLL1);
        layout1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vipLL1:
                Toast.makeText(MySettings.this,"点击vip管理",Toast.LENGTH_LONG).show();
                break;
        }
    }


}
