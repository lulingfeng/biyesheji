package com.example.bishedemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.entity.Cartoon;
import com.show.api.ShowApiRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class MyFragment1 extends Fragment {
    public static final int RECEIVE_DATA=0x1;
    View view;
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.kongbu,container,false);
        initView();
        getCartoonData();
        return view;
    }

    private void initView() {
        listView=view.findViewById(R.id.cartoonData);
    }

    public void getCartoonData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String appid="92329";//要替换成自己的
                String secret="293395973edd4a12ab4a6b4465009d92";//要替换成自己的
                final String res=new ShowApiRequest( "http://route.showapi.com/958-1", appid, secret)
                        .addTextPara("type", "/category/weimanhua/kbmh")
                        .addTextPara("page", "1")
                        .post();
                final  String data=res.replace(" ","");
                final String detailData=data.replace("\n","");
                Message message=Message.obtain();
                message.obj=detailData;
                message.what=RECEIVE_DATA;
                mHandler.sendMessage(message);
            }
        }).start();
    }
    private void readDataArray(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String content = jsonObject.getString("link");
                String id = jsonObject.getString("id");
                String title = jsonObject.getString("title");
                Cartoon cartoon=new Cartoon();
                cartoon.setId(id);
                cartoon.setContent(content);
                cartoon.setTitle(title);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RECEIVE_DATA:
                    try {
                        Object resultObj = new JSONTokener((String) msg.obj).nextValue();
                        JSONObject resultMap=(JSONObject)resultObj;
                        Log.e("接口数据状态",resultMap.getString("showapi_res_code"));
                        JSONObject data=resultMap.getJSONObject("showapi_res_body");
                        JSONObject detailData=data.getJSONObject("pagebean");
                        String content=detailData.getString("contentlist");
                        readDataArray(content);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

}
