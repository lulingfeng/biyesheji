package com.example.View;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bishedemo.R;
import com.example.database.DatabaseHelper;

public class Signup extends Activity implements View.OnClickListener {
    private EditText signupID;
    private EditText signPwd;
    private EditText signPwd2;
    private TextView warnInfo;

    private static final String KEY_USERNAME="username";
    private static final String KEY_PASSWORD="password";
    private static final String TABLE_NAME="userInfo";
    private static final String DATABASE_NAME="userInfo.db";
    private Button signup;
    private ImageButton exit;

    String userID;
    String userPWD;
    String userPWD2;
    String extraData;

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        databaseHelper=new DatabaseHelper(this,DATABASE_NAME,null,2);
         sqLiteDatabase=databaseHelper.getWritableDatabase();
        initView();
    }

    private void initView() {
        signupID=findViewById(R.id.signup_id);
        signPwd=findViewById(R.id.signup_pwd);
        signPwd2=findViewById(R.id.sign_pwd2);
        warnInfo=findViewById(R.id.warnInfo);
        signup=findViewById(R.id.btn_signup);
        exit=findViewById(R.id.exit2);
        Intent intent=getIntent();
        extraData=intent.getStringExtra("手机号");
        if (extraData!=null&&(!extraData.equals(""))){
            signupID.setText(extraData);
        }
        signup.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signup:
                getInfo();//获取文本框信息
                if (correctInfo()){
                    ContentValues values=new ContentValues();
                    values.put(KEY_USERNAME,userID);
                    values.put(KEY_PASSWORD,userPWD);
                    sqLiteDatabase.insert(TABLE_NAME,null,values);
                    Intent intent=new Intent(Signup.this, Login.class);
                    intent.putExtra("注册账号",userID);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
                break;

            case R.id.exit2:
                Intent intent=new Intent(Signup.this,Login.class);
                startActivity(intent);
                break;
        }
    }

    public void getInfo() {
        userID=signupID.getText().toString().trim();
        userPWD=signPwd.getText().toString().trim();
        userPWD2=signPwd2.getText().toString().trim();
    }

    public boolean correctInfo(){

        if (userID.isEmpty()){
            warnInfo.setText("账号不能为空");
        }
        else if (userPWD.isEmpty()||userPWD2.isEmpty()){
            warnInfo.setText("密码不能为空");
        }else if (!userPWD.equals(userPWD2)){
            warnInfo.setText("密码不一致");
        }
        else {
            Cursor cursor = sqLiteDatabase.query("userInfo", new String[]{"username", "password"}, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) { //如果有数据，遍历
                do {
                    while (true) {
                        if ((!userID.equals(cursor.getString(cursor.getColumnIndex("username"))))) {
                            break;
                        } else {
                            warnInfo.setText("账号已存在");
                            return false;
                        }
                    }
                }
                while (cursor.moveToNext());
                cursor.close();
                return true;
            }else {
                return true;
            }
        }
        return false;
    }
}
