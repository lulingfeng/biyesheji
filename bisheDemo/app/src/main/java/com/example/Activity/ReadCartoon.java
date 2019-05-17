package com.example.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Adapter.DataAdapter;
import com.example.bishedemo.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.show.api.ShowApiRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.ConfigInfo.UrlManager.CHANGE_BG;
import static com.example.ConfigInfo.UrlManager.DETAILURL;

public class ReadCartoon extends AppCompatActivity {
    String data="";
    List<String> cartoonData;
    List<Bitmap> bitmapList;
    DataAdapter dataAdapter;
    ListView listView;
    RefreshLayout refreshLayout;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_cartoon);
        initView();
        dataAdapter=new DataAdapter(getApplicationContext(),R.layout.read_data,bitmapList);
        listView.setAdapter(dataAdapter);
        receiveData(DETAILURL);
    }

    private void initView() {
        linearLayout=findViewById(R.id.bg);
        final RefreshLayout refreshLayout =findViewById(R.id.refreshData);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                bitmapList.removeAll(bitmapList);
                cartoonData.removeAll(cartoonData);
                readData();

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
            if (isListViewReachBottomEdge(listView)) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                Log.e("llf","到底了");
                Toast.makeText(ReadCartoon.this,"您已阅读完此章",Toast.LENGTH_SHORT).show();
            }else {
                refreshlayout.finishLoadMore(false);//传入false表示加载失败
                Toast.makeText(ReadCartoon.this,"请稍后，正在加载",Toast.LENGTH_SHORT).show();
                Log.e("llf","还没到底");
            }
            }
        });
        refreshLayout.setRefreshHeader(new BezierRadarHeader(getApplicationContext()).setEnableHorizontalDrag(true));
        cartoonData=new ArrayList<>();
        bitmapList=new ArrayList<>();
        listView=findViewById(R.id.readCartoon);
    }
    public boolean isListViewReachBottomEdge(final ListView listView) {
        boolean result=false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result= (listView.getHeight()>=bottomChildView.getBottom());
        };
        return result;
    }
    public void readData(){
        try{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                                try {
                                    if (cartoonData.size()==0){
                                        Message msg = Message.obtain();
                                        msg.what=CHANGE_BG;
                                        mHandler.sendMessage(msg);
                                    }
                                    for (int i = 0; i < cartoonData.size(); i++) {
                                        String timage = cartoonData.get(i).replace("\"","");
                                        Bitmap bitmap = getHttpBitmap(timage);
                                        Message msg = Message.obtain();
                                        msg.obj = bitmap;
                                        HeadImageHandler.sendMessage(msg);
                                    }
                                }catch (Exception e) {
                                    Log.e("Exception", e.getMessage());
                                }
                            }
                    }).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void receiveData(final String url) {
        Intent intent=getIntent();
        data=intent.getStringExtra("漫画链接");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String appid = "92329";
                String secret = "293395973edd4a12ab4a6b4465009d92";
                final String res = new ShowApiRequest(url, appid, secret)
                        .addTextPara("id", data)
                        .post();
                final  String data=res.replace(" ","");
                final String detailData=data.replace("\n","");
                Message message=Message.obtain();
                message.obj=detailData;
                message.what=0x1;
                mHandler.sendMessage(message);
            }
        }).start();
    }
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


    private Handler HeadImageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap= (Bitmap) msg.obj;
            bitmapList.add(bitmap);
            dataAdapter.notifyDataSetChanged();

        }
    };
    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x1:
                    try {
                        linearLayout.setBackgroundResource(R.drawable.t);
                        Object resultObj=new JSONTokener((String)msg.obj).nextValue();
                        JSONObject resultMap=(JSONObject)resultObj;
                        JSONObject showData=resultMap.getJSONObject("showapi_res_body");
                        JSONObject item=showData.getJSONObject("item");
                        String time=item.getString("time");
                        String imgList=item.getString("imgList");
                        String secondList=imgList.replace("[","");
                        String thirdList=secondList.replace("]","");
                        String realList=thirdList.replace("\\/","/");
                        String [] realData=realList.split(",");
                        for (int i=0;i<realData.length;i++){
                            cartoonData.add(realData[i]);
                        }
                        readData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        linearLayout.setBackgroundResource(R.drawable.tt);
                        Toast.makeText(ReadCartoon.this,"无法连接服务器，请打开网络重试",Toast.LENGTH_LONG).show();
                    }
                    break;
                case CHANGE_BG:
                    linearLayout.setBackgroundResource(R.drawable.tt);
                    break;
                    default:
                    break;
            }
        }
    };
}
