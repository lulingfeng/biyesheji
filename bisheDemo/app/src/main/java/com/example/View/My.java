package com.example.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bishedemo.R;

public class My extends Activity implements View.OnClickListener {
    TextView myLogin,myVipSetting,myVipPolicy,myVipMoney,myProtocol,myService,myJob,myCustomService;
    ImageView myPicture,imageVipSetting,imageVipPolicy,imageVipMoney,imageProtocol,imageService,imageJob,imageCustomService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my);
        initView();
        initData();
    }

    private void initView() {
        myLogin=findViewById(R.id.loginMain);

        myPicture=findViewById(R.id.iv_personal_icon);

        myVipSetting=findViewById(R.id.vipSetting);
        imageVipSetting=findViewById(R.id.vipImageSetting);

        myVipPolicy=findViewById(R.id.vipPolicy);
        imageVipPolicy=findViewById(R.id.vipImagePolicy);

        myVipMoney=findViewById(R.id.vipMoney);
        imageVipMoney=findViewById(R.id.vipImageMoney);

        myProtocol=findViewById(R.id.myPingTai);
        imageProtocol=findViewById(R.id.imageXieYi);

        myService=findViewById(R.id.myService);
        imageService=findViewById(R.id.imageService);

        myJob=findViewById(R.id.myJob);
        imageJob=findViewById(R.id.imageJob);

        myCustomService=findViewById(R.id.myCustom);
        imageCustomService=findViewById(R.id.imageCustomService);





    }

    private void initData() {
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vipImageSetting:
            case R.id.vipSetting:
             //   Toast.makeText(My.this,"点击会员管理",Toast.LENGTH_LONG).show();

                break;
        }

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

}