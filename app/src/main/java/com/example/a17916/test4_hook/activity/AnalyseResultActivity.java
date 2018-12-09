package com.example.a17916.test4_hook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.a17916.test4_hook.R;
import com.example.a17916.test4_hook.TestGenerateTemple.PageResult;
import com.example.a17916.test4_hook.matchModule.SearchThread;
import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.share.SavePreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnalyseResultActivity extends AppCompatActivity {
    public final static String PageResult = "pageResult";
    private RecyclerView recyclerView;
    private SearchThread searchThread;
    private List<PageResult> pageResults;
    private List<Intent> intentList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_analyse_result);
        init();
    }

    private void init(){
        intentList = new ArrayList<>();
        intentList.add(testIntent());

        pageResults = getIntent().getParcelableArrayListExtra(AnalyseResultActivity.PageResult);

        recyclerView = findViewById(R.id.resultIntent);

        searchThread = new SearchThread(getApplicationContext());

        List<Intent> temp;
        String entityType = "";
        String entityValue = "";
        JSONObject jsonObject;
        for(PageResult pageResult:pageResults){
            entityType = pageResult.getEntityType();
            entityValue = pageResult.getNodeValue();
            jsonObject = new JSONObject();
            try {
                jsonObject.put(entityType,entityValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            temp = searchThread.getIntent(entityType,jsonObject);
            if(temp!=null){
                intentList.addAll(temp);
            }

        }

        ShowIntentAdapter adapter = new ShowIntentAdapter(intentList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private Intent testIntent(){
        SavePreference savePreference = SavePreference.getInstance(getApplicationContext());
        Intent intent = savePreference.getIntent("com.douban.frodo.subject.activity.LegacySubjectActivity");
        String packageName = "com.douban.movie";
        String activityName = "com.douban.frodo.subject.activity.LegacySubjectActivity";
        intent.putExtra(MonitorActivityService.targetPackageName,packageName);
        intent.putExtra(MonitorActivityService.targetActivityName,activityName);
        return intent;
    }
}
