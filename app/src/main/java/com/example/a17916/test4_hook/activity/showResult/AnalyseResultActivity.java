package com.example.a17916.test4_hook.activity.showResult;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.a17916.test4_hook.R;
import com.example.a17916.test4_hook.TestGenerateTemple.PageResult;
import com.example.a17916.test4_hook.matchModule.MatchManager;
import com.example.a17916.test4_hook.receive.LocalActivityReceiver;
import com.example.a17916.test4_hook.share.SavePreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnalyseResultActivity extends AppCompatActivity {
    public final static String PageResult = "pageResult";
    private RecyclerView recyclerView;

    private List<PageResult> pageResults;
    private List<ShowItem> itemList;
    private MatchManager matchManager;
    private ShowIntentAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_analyse_result);
        init();
    }

    private void init(){
        recyclerView = findViewById(R.id.resultIntent);
        itemList = new ArrayList<>();
        matchManager = MatchManager.getInstance();
        Intent intent = getIntent();
        pageResults = intent.getParcelableArrayListExtra(AnalyseResultActivity.PageResult);

        List<ShowItem> tempItems;
        for(PageResult pageResult:pageResults){
            Log.i("LZH","资源名:"+pageResult.getNodeValue()+"资源类型: "+pageResult.getEntityType());
            tempItems = matchManager.queryItem(pageResult.getNodeValue(),pageResult.getEntityType());
            itemList.addAll(tempItems);
        }
        adapter = new ShowIntentAdapter(itemList,this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }


}
