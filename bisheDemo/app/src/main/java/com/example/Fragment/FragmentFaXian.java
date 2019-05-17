package com.example.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.Adapter.TagAdapter;
import com.example.Interface.OnTagSelectListener;
import com.example.View.FlowTagLayout;
import com.example.bishedemo.R;
import com.example.database.DatabaseHelper;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.ConfigInfo.UrlManager.CARTOON_DBNAME;
import static com.example.ConfigInfo.UrlManager.CARTOON_ID;
import static com.example.ConfigInfo.UrlManager.CARTOON_LINK;
import static com.example.ConfigInfo.UrlManager.CARTOON_TABLE;

public class FragmentFaXian extends Fragment {
    android.support.v7.widget.Toolbar toolbar;
    private FlowTagLayout mMobileFlowTagLayout;
    private FlowTagLayout mShouFeiFlowTagLayout;
    private FlowTagLayout mAreaFlowTagLayout;
    private TagAdapter<String> mMobileTagAdapter;
    private TagAdapter<String> mShouFeiTagAdapter;
    private TagAdapter<String> mAreaTagAdapter;
    private EditText editText;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String usedData;
    private String usedData2;
    private String currentData;
    private String currentData2;
    private ArrayList<String> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.faxian,container,false);
        databaseHelper=new DatabaseHelper(getContext(),CARTOON_DBNAME,null,1);
        sqLiteDatabase=databaseHelper.getReadableDatabase();
        editText=view.findViewById(R.id.searchContent);
        list=new ArrayList<>();
        toolbar=view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_search:
                        usedData="";
                        usedData2="";
                        String searchContent=editText.getText().toString().isEmpty()?"":editText.getText().toString();
                        Cursor cursor=sqLiteDatabase.query(CARTOON_TABLE,new String[]{CARTOON_LINK,CARTOON_ID},null,null,null,null,null);
                        if (cursor!=null&&cursor.moveToFirst()){
                        do {
                            currentData=cursor.getString(cursor.getColumnIndex(CARTOON_LINK));
                            currentData2=cursor.getString(cursor.getColumnIndex(CARTOON_ID));
                        if (currentData.contains(searchContent) && !currentData.equals(usedData) &&!currentData2.equals(usedData2)){
                            Log.e("llf",currentData+currentData2);
                            usedData=currentData;
                            usedData2=currentData2;
                            list.add(usedData2);
                        }
                        }while (cursor.moveToNext());
                        }
                        List<Object> cartoonLink = list.stream().distinct().collect(Collectors.toList());
                        
                        cursor.close();
                    break;
                    case R.id.action_refresh :
                        mAreaFlowTagLayout.clearAllOption();
                        mMobileFlowTagLayout.clearAllOption();
                        mShouFeiFlowTagLayout.clearAllOption();
                        editText.setText("");

                        Log.e("llf","刷新");
                        break;
                }
                return true;
            }
        });

        mMobileTagAdapter=new TagAdapter<>(getContext()) ;
        mMobileFlowTagLayout=view.findViewById(R.id.color_flow_layout);
        mMobileFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        mMobileFlowTagLayout.setAdapter(mMobileTagAdapter);
        mMobileFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        sb.append(" ");
                    }
                    Snackbar.make(parent, "漫画类型:" + sb.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Snackbar.make(parent, "没有选择标签", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        mShouFeiTagAdapter=new TagAdapter<>(getContext());
        mShouFeiFlowTagLayout=view.findViewById(R.id.size_flow_layout);
        mShouFeiFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mShouFeiFlowTagLayout.setAdapter(mShouFeiTagAdapter);
        mShouFeiFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));

                    }
                    Snackbar.make(parent, "收费类型:" + sb.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Snackbar.make(parent, "没有选择标签", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        mAreaTagAdapter=new TagAdapter<>(getContext());
        mAreaFlowTagLayout=view.findViewById(R.id.mobile_flow_layout);
        mAreaFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        mAreaFlowTagLayout.setAdapter(mAreaTagAdapter);
        mAreaFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        sb.append(" ");
                    }
                    Snackbar.make(parent, "地区:" + sb.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Snackbar.make(parent, "没有选择标签", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        initMobileData();
        initShouFeiData();
        initAreaData();
        return view;
    }

    private void initMobileData() {
        List<String> dataSource = new ArrayList<>();
        dataSource.add("恐怖");
        dataSource.add("搞笑");
        dataSource.add("萌宠");
        dataSource.add("冷知识");
        dataSource.add("故事");
        dataSource.add("段子手");
        dataSource.add("奇趣");
        mMobileTagAdapter.onlyAddAll(dataSource);
    }

    private void initShouFeiData(){
        List<String> dataSource = new ArrayList<>();
        dataSource.add("是");
        dataSource.add("否");
        mShouFeiTagAdapter.onlyAddAll(dataSource);
    }

    private void initAreaData(){
        List<String> dataSource = new ArrayList<>();
        dataSource.add("欧美");
        dataSource.add("国产");
        dataSource.add("日韩");
        dataSource.add("其他地区");
        mAreaTagAdapter.onlyAddAll(dataSource);
    }
}
