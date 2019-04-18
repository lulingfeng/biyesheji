package com.example.bishedemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Thread.LoginThread;
import com.example.View.FragmentFaXian;
import com.example.View.FragmentMy;
import com.example.View.Login;
import com.youth.banner.Banner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ZhuYe extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup navigationBar;
    private RadioButton btn1, btn2, btn3, btn4,btn5;
    private Fragment fragment1, fragment2, fragment3, fragment4,fragment5;

    private Fragment mFragment;//当前显示的Fragment

    private String data;

    Bundle bundle;
    Message message;
    private TextView loginMain,personSign;
    Banner banner;
    Context context;

    public static final int VIP_SETTING=0x11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuye);
        initViews();
        getPermissionn();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.main_fragment, fragment1).commit();
        mFragment = fragment1;

    }

    private void getPermissionn() {
        List<String> permissionList = new ArrayList<>();
//        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.
//                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }
        if (ContextCompat.checkSelfPermission(ZhuYe.this, Manifest.
                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(ZhuYe.this,Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.
//                permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED){
//            permissionList.add(Manifest.permission.BODY_SENSORS);
//        }
        if (!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(ZhuYe.this,permissions,1);
        }else {

        }

    }
    private void initViews() {
        navigationBar = (RadioGroup) findViewById(R.id.navigation_btn);
        btn1 = (RadioButton) findViewById(R.id.btn1);
        btn2 = (RadioButton) findViewById(R.id.btn2);
        btn3 = (RadioButton) findViewById(R.id.btn3);
        btn4 = (RadioButton) findViewById(R.id.btn4);
        btn5=findViewById(R.id.btn5);
        navigationBar.setOnCheckedChangeListener(this);

        LayoutInflater inflater=LayoutInflater.from(ZhuYe.this); //找到res/layout目录下的xml文件
        View view=inflater.inflate(R.layout.my,null);
        loginMain=view.findViewById(R.id.loginMain);
        personSign=view.findViewById(R.id.personSign);
        fragment1 = new Fragment1();
        fragment2=new FragmentFaXian();
        fragment3 = new FragmentFaXian();
        fragment4=new FragmentFaXian();
        fragment5=new FragmentMy();
        getData();
        fragment5.setArguments(bundle);
    }

    public boolean getData() {
        Intent intent=getIntent();
        data=intent.getStringExtra("用户姓名");
        Log.e("llf","ZhuYe中data获取状况"+data);
        if (data!=null&&(!data.equals(""))){
            bundle = new Bundle();
            bundle.putString("用户姓名", data);
            return true;
        }else {
            bundle=new Bundle();
            bundle.putString("","");
            return false;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.btn1:
                btn1.setChecked(true);
                btn2.setChecked(false);
                btn3.setChecked(false);
                btn4.setChecked(false);
                switchFragment(fragment1);
                break;
            case R.id.btn2:
                btn1.setChecked(false);
                btn2.setChecked(true);
                btn3.setChecked(false);
                btn4.setChecked(false);
                switchFragment(fragment2);

                break;
            case R.id.btn3:
                btn1.setChecked(false);
                btn2.setChecked(false);
                btn3.setChecked(true);
                btn4.setChecked(false);
                switchFragment(fragment3);

                break;
            case R.id.btn4:
                btn1.setChecked(false);
                btn2.setChecked(false);
                btn3.setChecked(false);
                btn4.setChecked(true);
                switchFragment(fragment4);
                break;
            case R.id.btn5:
                btn1.setChecked(false);
                btn2.setChecked(false);
                btn3.setChecked(false);
                btn4.setChecked(false);
                btn5.setChecked(true);
                switchFragment(fragment5);
                break;
        }
    }

    public  void testClick(View v){
        switch (v.getId()){
            case R.id.vipSetting:
            case R.id.vipImageSetting:
                setVipSetting();
                break;
            case R.id.vipPolicy:
            case R.id.vipImagePolicy:
                setVipSetting();
                break;
            case R.id.vipMoney:
            case R.id.vipImageMoney:
                setVipSetting();
                break;
            case R.id.imageCustomService:
            case R.id.myCustom:
                Intent intent=new Intent(ZhuYe.this,CustomService.class);
                startActivity(intent);
                break;
            case R.id.iv_personal_icon:
                showChoosePicDialog();
                break;
            case R.id.loginMain:
                Intent intent2=new Intent(ZhuYe.this, Login.class);
                startActivity(intent2);
                finish();
            default:
                break;
        }

    }
    public void showChoosePicDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[]strings={"选择本地照片","拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(strings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:

                        break;

                    case 1:

                        break;
                }
            }
        });
        builder.create().show();
    }

    /*
    会员办理
     */
    public void setVipSetting(){
       if (getData()){


       }else { //子线程更新UI 包括Toast
            new LoginThread(this,message,mHandler).start();
       }
    }

    private void switchFragment(Fragment fragment) {
        //判断当前显示的Fragment是不是切换的Fragment
        if (mFragment != fragment) {
            //判断切换的Fragment是否已经添加过
            if (!fragment.isAdded()) {
                //如果没有，则先把当前的Fragment隐藏，把切换的Fragment添加上
                getSupportFragmentManager().beginTransaction().hide(mFragment)
                        .add(R.id.main_fragment, fragment).commit();
            } else {
                //如果已经添加过，则先把当前的Fragment隐藏，把切换的Fragment显示出来
                getSupportFragmentManager().beginTransaction().hide(mFragment).show(fragment).commit();
            }
            mFragment = fragment;
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment1 == null && fragment instanceof Fragment1){
            fragment1 = fragment;
        } else if (fragment2 == null && fragment instanceof FragmentFaXian){
            fragment2 = fragment;
        }else if (fragment3==null && fragment instanceof FragmentMy){
            fragment3=fragment;
        }
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case VIP_SETTING:
                    Toast.makeText(ZhuYe.this,"您尚未登录，三秒后进入登录界面",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}
