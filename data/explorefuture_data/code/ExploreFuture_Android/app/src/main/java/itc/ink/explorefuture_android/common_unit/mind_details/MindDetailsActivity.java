package itc.ink.explorefuture_android.common_unit.mind_details;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.common_unit.mind_details.adapter.MindDetailsWrapAdapter;
import itc.ink.explorefuture_android.common_unit.mind_details.mode.CommentDataMode;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.recommend.attention_fragment.adapter.AttentionWrapAdapter;

/**
 * Created by yangwenjiang on 2018/10/29.
 */

public class MindDetailsActivity extends Activity {
    public static final String KEY_MIND_ITEM="mind_item";

    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private MindDetailsWrapAdapter mindDetailsWrapAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        MindListDataMode mindListDataItem=(MindListDataMode)intent.getSerializableExtra(KEY_MIND_ITEM);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.common_unit_mind_details_activity);

        ImageView backBtn=findViewById(R.id.mind_Details_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(new BackBtnClickListener());

        contentRecyclerView=findViewById(R.id.mind_Details_RecyclerView);
        mindDetailsWrapAdapter = new MindDetailsWrapAdapter(this, mindListDataItem);
        contentRecyclerView.setAdapter(mindDetailsWrapAdapter);
        contentRvLayoutManager = new LinearLayoutManager(this);
        contentRecyclerView.setLayoutManager(contentRvLayoutManager);
    }

    class BackBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }


}
