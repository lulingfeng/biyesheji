package com.example.View;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bishedemo.R;
import com.example.bishedemo.ZhuYe;
import com.example.database.DatabaseHelper;
import com.tencent.qcloud.core.auth.OAuth2Credentials;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudResultListener;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.tac.authorization.TACAuthorizationService;
import com.tencent.tac.social.auth.QQAuthProvider;
import com.tencent.tac.social.auth.TACOpenUserInfo;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 *
 */
public class Login extends AppCompatActivity implements View.OnClickListener{
    //EditText
    private EditText userName;
    private EditText userPwd;
    private TextView warnInfo;
    //Button
    private Button btn_login;
    private Button btn_signup;
    //ImageButton
    private ImageButton btn_exit;
    private ImageButton btn_qq;
    private ImageButton btn_weChat;
    private ImageButton btn_weiBo;
    private ImageButton mobile;

    BottomNavigationView bottomNavigationView;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    String userID;
    String userPWD;
    String extraID;
    String data;
    String extraUserName;
    OAuth2Credentials mOAuth2Credentials;
    QQAuthProvider qqAuthProvider;
    TACAuthorizationService service;
    FragmentMy fragmentMy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();//初始化布局
        // MobSDK.init(this);
        databaseHelper = new DatabaseHelper(this, "userInfo.db", null, 2);
        sqLiteDatabase = databaseHelper.getWritableDatabase();//创建数据库
        service = TACAuthorizationService.getInstance();
        qqAuthProvider = service.getQQAuthProvider(this);

    }

    private void qqLogin() {

// 启动登录
        qqAuthProvider.signIn(this, new QCloudResultListener<OAuth2Credentials>() {
            @Override
            public void onSuccess(OAuth2Credentials credentials) {
                // 登录成功，可以拿到QQ的用户凭证
                mOAuth2Credentials = credentials;

                String accessToken = credentials.getAccessToken();
                String openId = credentials.getOpenId();
            }

            @Override
            public void onFailure(QCloudClientException clientException, QCloudServiceException serviceException) {
                // 登录失败
                Log.e("QQ登录失败,clientException", clientException.getLocalizedMessage());
                Log.e("QQ登录失败,serviceException", serviceException.getLocalizedMessage());

            }
        });
    }

    public void initView() {
        userName = findViewById(R.id.user_id);
        userPwd = findViewById(R.id.user_pwd);
        warnInfo = findViewById(R.id.login_warn);

        //   bottomNavigationView = findViewById(R.id.menu_bottom);

        btn_login = findViewById(R.id.login);
        btn_signup = findViewById(R.id.signup);

        btn_qq = findViewById(R.id.qq);
        btn_weChat = findViewById(R.id.weChat);
        btn_weiBo = findViewById(R.id.weiBo);
        btn_exit = findViewById(R.id.exit);
        mobile = findViewById(R.id.mobile);

        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        btn_qq.setOnClickListener(this);
        btn_weChat.setOnClickListener(this);
        btn_weiBo.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        mobile.setOnClickListener(this);
        Intent intent=getIntent();
        data=intent.getStringExtra("注册账号");
        if (data!=null&&(!data.equals(""))){
            userName=findViewById(R.id.user_id);
            userName.setText(data);
        }
    }

    public void getInfoQQ() {
        qqAuthProvider.getUserInfo(mOAuth2Credentials, new QCloudResultListener<TACOpenUserInfo>() {
            @Override
            public void onSuccess(TACOpenUserInfo result) {
                Toast.makeText(Login.this, result.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(QCloudClientException clientException, QCloudServiceException serviceException) {
                // 获取出错
                Log.e("clientException", clientException.getLocalizedMessage());
                Log.e("serviceException", serviceException.getLocalizedMessage());
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                getCurrentInfo(); //每次点击都验证文本内容
                Log.e("llf", userID + "///" + userPWD);
                if (correctInfo()) {
                    try {
                        extraID = userID;
                        Intent intent = new Intent(Login.this, ZhuYe.class);
                        intent.putExtra("用户姓名",extraID);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.signup:
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
                finish();
                break;

            case R.id.exit:
                Intent intent1 = new Intent(Login.this, ZhuYe.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.qq:
                //  qqLogin();
                //  getInfoQQ();
                break;

            case R.id.weChat:

                break;

            case R.id.weiBo:

                break;

            case R.id.mobile:
                sendCode(this);
                break;

            default:
                break;
        }
    }

    public void getCurrentInfo() {
        userID = userName.getText().toString();
        userPWD = userPwd.getText().toString();
    }

    public boolean correctInfo() {
        if (userID == null || userID.equals("")) {
            warnInfo.setText("账号不能为空");
            return false;
        } else if (userPWD == null || userPWD.equals("")) {
            warnInfo.setText("密码不能为空");
        } else {
            warnInfo.setText("");
            Cursor cursor = sqLiteDatabase.query("userInfo", new String[]{"username", "password"}, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) { //如果有数据，遍历
                do {
                    if ((userID.equals(cursor.getString(cursor.getColumnIndex("username")))) &&
                            userPWD.equals(cursor.getString(cursor.getColumnIndex("password")))) {
                        Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (!userID.equals(cursor.getString(cursor.getColumnIndex("username")))) {
                    } else if ((userID.equals(cursor.getString(cursor.getColumnIndex("username")))) ||
                            !userPWD.equals(cursor.getString(cursor.getColumnIndex("password")))) {
                    }

                }
                while (cursor.moveToNext());
                Toast.makeText(Login.this,"账号或密码有误",Toast.LENGTH_LONG).show();
                return false;
            }
            cursor.close();
        }
        return false;
    }

    public void sendCode(Context context) {
        RegisterPage page = new RegisterPage();
        //如果使用我们的ui，没有申请模板编号的情况下需传null
        page.setTempCode(null);
        page.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 处理成功的结果
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country"); // 国家代码，如“86”
                    String phone = (String) phoneMap.get("phone"); // 手机号码，如“13800138000”
                    // TODO 利用国家代码和手机号码进行后续的操作
                    Intent intent = new Intent(Login.this, Signup.class);
                    intent.putExtra("手机号", phone);
                    startActivity(intent);

                    finish();
                } else {
                    // TODO 处理错误的结果
                }
            }
        });
        page.show(context);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (qqAuthProvider != null) {
            qqAuthProvider.handleActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(0, 0);
    }

}
