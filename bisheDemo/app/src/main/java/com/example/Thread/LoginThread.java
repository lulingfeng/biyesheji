package com.example.Thread;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.View.Login;

import static com.example.bishedemo.ZhuYe.VIP_SETTING;

/**
 *判断是否登录，只有登录状态才能进入相关页面
 *
 *
 */


public class LoginThread extends Thread {
    Context mContext;
    Message message;
    Handler handler;
    public LoginThread(Context context, Message message, Handler handler){
        mContext=context;
        this.message=message;
        this.handler=handler;
    }
    @Override
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=VIP_SETTING;
                handler.sendMessage(message);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(mContext, Login.class);
                mContext.startActivity(intent);
            }
        }).start();
    }
}
