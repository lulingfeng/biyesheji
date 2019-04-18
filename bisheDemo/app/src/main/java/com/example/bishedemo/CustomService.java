package com.example.bishedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Thread.SocketCloseInterface;
import com.example.Thread.SocketHeartBeatThread;
import com.example.util.SocketUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CustomService extends AppCompatActivity {

    Socket msocket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    String used_data;
    SocketHeartBeatThread socketHeartBeatThread;
    SocketCloseInterface socketCloseInterface;
    Thread llf;
    Boolean reConnect=true;
    InputStream in;
    Msg msg;
    private List<Msg> msgList=new ArrayList<>();
    private EditText inputText;
    private Button btn_send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private String content;
    private Boolean connectedFlag=false;
    private static final String IP="192.168.1.4";
    private boolean stopFlag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_service);
        initView();
        llf=new myThread();
        llf.start();    //开始连接服务器
        Toast.makeText(CustomService.this, "程序开始执行", Toast.LENGTH_LONG).show();
    }

    private void initMsg() {
        if (connectedFlag) {
            Msg msg = new Msg("你好，请问有什么可以帮到您",Msg.TYPE_RECEIVED);
            msgList.add(msg);
        }else {
            Msg msg=new Msg("未连接到远程服务器，请稍后重试",Msg.TYPE_RECEIVED);
            msgList.add(msg);
        }
    }

    private void initView() {
        inputText=findViewById(R.id.inputText);
        btn_send=findViewById(R.id.btn_send);
        msgRecyclerView=findViewById(R.id.msgRecyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter=new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 content=inputText.getText().toString();
                if (!content.equals("")){
                    msg=new Msg(content,Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");
                        Log.e("点击事件socket",msocket.getInetAddress().toString());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                OutputStream osSend = msocket.getOutputStream();
                                OutputStreamWriter osWrite = new OutputStreamWriter(osSend);
                                BufferedWriter bufWrite = new BufferedWriter(osWrite);
                                // 代表可以立即向服务器端发送单字节数据
                                msocket.setOOBInline(true);
                                // 数据不经过输出缓冲区，立即发送
                                msocket.sendUrgentData(0x44);
                                // 向服务器端写数据，写入一个缓冲区
                                // 注：此处字符串最后必须包含“\r\n\r\n”，告诉服务器HTTP头已经结束，可以处理数据，否则会造成下面的读取数据出现阻塞
                                bufWrite.write(content+"\r\n\r\n");
                                // 发送缓冲区中数据 -
                                bufWrite.flush();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            }
                        }).start();

                }
            }
        });
    }
    public void stopThread() {
        Message message = Message.obtain();
        message.what = 0x14;
        mhandler.sendMessage(message);

        if (llf != null) {
            close();
            llf = null;
        }
        if (socketHeartBeatThread != null) {
            socketHeartBeatThread.close();
        }
        closeSocket();
        if (reConnect) { //断线重连
            SocketUtil.toWait(this, 10000);
            Thread reThread = new myThread();
            reThread.start();

        }
    }

    private void closeSocket() {
        if (msocket != null) {
            if (!msocket.isClosed() && msocket.isConnected()) {
                try {
                    msocket.close();
                } catch (IOException e) {
                    Message message = Message.obtain();
                    message.what = 0x14;
                    mhandler.sendMessage(message);
                    e.printStackTrace();
                }
            }
            msocket = null;
        }
    }

    public void close() {
        if (bufferedReader != null) {
            if (socketCloseInterface != null) {
                socketCloseInterface.onSocketShutdownInput();
            }
            SocketUtil.closeBufferedReader(bufferedReader);
            bufferedReader = null;
        }
    }
    public Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x1:
                    //        textView1.setText("读取到数据");
                    String str = (String) msg.obj;
                    try {
                        if (str.equals(used_data) && str != null) {
                            break;
                        }
                        if (!str.equals("")){

                        }
                        Object json = new JSONTokener((String) msg.obj).nextValue();
                        if (json instanceof JSONObject) {
                            //自行解析即可
                            JSONObject jsonObject = (JSONObject) json;
                            if (jsonObject.has("service")){
                                        if (jsonObject.getString("state").equals("true")){
                                            Msg msgService = new Msg(jsonObject.getString("content"),Msg.TYPE_RECEIVED);
                                            msgList.add(msgService);
                                            adapter.notifyItemInserted(msgList.size()-1);
                                            msgRecyclerView.scrollToPosition(msgList.size()-1);
                                        }
                            }
                            if (jsonObject.has("IMGURL")) {
                                final String imgUrl = jsonObject.getString("IMGURL");
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String timage = "http://"+"192.168.1.156"+":8080" + imgUrl;
                                        Bitmap bitmap = getHttpBitmap(timage);
                                        Message msg = Message.obtain();
                                        msg.obj = bitmap;
                                        HeadImageHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                                    }
                                }).start();
                            }
                        } else if (json instanceof JSONArray) {
                            //自行解析即可
                            readDataArray((String) msg.obj);
                        }
                        //       Log.e("llf", "接收数据成功");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x12:
                    msocket = (Socket) msg.obj;
                    //   textView1.setText("成功连接服务器:" + msocket.getInetAddress() + "//" + msocket.getPort());
                    Log.e("llf", "IP地址:" + msocket.getInetAddress() + "端口:" + msocket.getPort());
                    socketHeartBeatThread = new SocketHeartBeatThread("llf", printWriter, msocket, socketCloseInterface);
                    socketHeartBeatThread.start();
                    break;
                case 0x13:
                    //     textView1.setText("程序奔溃");
                    Log.e("llf", "程序奔溃");
                    break;
                case 0x14:
                    //    textView1.setText("等待线程自动重连");
                    Toast.makeText(CustomService.this, "关闭线程，稍后自动重连", Toast.LENGTH_LONG).show();
                    break;
              /*  case 0x123:  实时显示时间
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("EEEE");
                    SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("HH时mm分");
                    data_year.setText(simpleDateFormat.format(date)); //更新时间
                    data_day.setText(simpleDateFormat2.format(date));
                    data_minute.setText(simpleDateFormat3.format(date));
                    break;*/
            }
        }
    };
    private Handler HeadImageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            //imageView.setImageBitmap(bitmap);
        }
    };
    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            url = url.replace("\\", "/");
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public class myThread extends Thread {
        public void run() {
            try {
                while (stopFlag) {
                    String result;
                    Socket socket = new Socket(IP, 60010);
                    msocket=socket;
                    connectedFlag=true;
                    Log.e("llf", socket.getInetAddress() + "");
                    initMsg();
                    Message message1 = Message.obtain();
                    message1.obj = socket;
                    message1.what = 0x12;
                    mhandler.sendMessage(message1);
                    in = socket.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(in, "GBK"));
                    while ((result = bufferedReader.readLine()) != null) {  //一直读消息，直到换行符将内容提交给主线程更新UI
                        Message message = Message.obtain();
                        message.what = 0x1;
                        message.obj = result;
                        mhandler.sendMessage(message);
                        Log.e("llf", "服务器传来的数据:" + result);
                    }
                }

            } catch (Exception e) {
                Message message = Message.obtain();
                message.what = 0x13;
                mhandler.sendMessage(message);
                e.printStackTrace();
                stopThread();
            }
        }
    }

    private void readDataArray(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String id = jsonObject.getString("id");
                Log.e("llf", name);
                Log.e("llf", id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            stopThread();
        }
    }

    private void startTimeThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        Message msg = Message.obtain(); //获取对象一定要用obtain()方法
                        msg.what = 0x123;  //刷新时间
                        mhandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Log.e("llf","客服活动销毁");
            stopFlag=false;
            finish();
        }
        return true;
    }
}
