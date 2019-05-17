package com.example.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Activity.ReadCartoon;
import com.example.Adapter.CartoonAdapter;
import com.example.bishedemo.R;
import com.example.entity.Cartoon;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.show.api.ShowApiRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import static com.example.ConfigInfo.UrlManager.BASEURL;
import static com.example.ConfigInfo.UrlManager.KONGBU;


public class list_kongbu extends Fragment {
    public static final int RECEIVE_DATA=0x1;
    CartoonAdapter cartoonAdapter;
    View view;
    ListView listView;
    ArrayList<Cartoon> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.list_kongbu,container,false);
        final RefreshLayout refreshLayout =view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                list.clear();
                getCartoonData(BASEURL,KONGBU,RECEIVE_DATA);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                list.clear();
                getCartoonData(BASEURL,KONGBU,RECEIVE_DATA);
            }
        });
        refreshLayout.setRefreshHeader(new BezierRadarHeader(getContext()).setEnableHorizontalDrag(true));
        listView=view.findViewById(R.id.kongbuData);
        list=new ArrayList<>();
        getCartoonData(BASEURL,KONGBU,RECEIVE_DATA);
        return view;
    }

    private void initData() {
        try {
            cartoonAdapter = new CartoonAdapter(getContext(), R.layout.cartoon_data, list);
            listView.setAdapter(cartoonAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(getActivity(), ReadCartoon.class);
                    intent.putExtra("漫画链接",list.get(position).getDetaiInfo());
                    startActivity(intent);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getCartoonData(final String url, final String type, final int id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                    String appid = "92329";
                    String secret = "293395973edd4a12ab4a6b4465009d92";
                    final String res = new ShowApiRequest(url, appid, secret)
                            .addTextPara("type", type)
                            .addTextPara("page", "1")
                            .post();
                final  String data=res.replace(" ","");
                final String detailData=data.replace("\n","");
                Message message=Message.obtain();
                message.obj=detailData;
                message.what=id;
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
                Cartoon cartoon=new Cartoon(title);
                cartoon.setId(content);
                cartoon.setDetaiInfo(id);
                list.add(cartoon);

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
                        view.setBackgroundResource(R.drawable.t);
                        JSONObject resultMap=(JSONObject)resultObj;
                        JSONObject data=resultMap.getJSONObject("showapi_res_body");
                        JSONObject detailData=data.getJSONObject("pagebean");
                        String content=detailData.getString("contentlist");
                        readDataArray(content);
                        initData();
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(),"无法连接服务器，请打开网络重试",Toast.LENGTH_LONG).show();
                        view.setBackgroundResource(R.drawable.tt);
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };



}
