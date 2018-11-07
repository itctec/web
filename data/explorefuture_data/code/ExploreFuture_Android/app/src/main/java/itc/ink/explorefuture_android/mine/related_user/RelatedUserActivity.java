package itc.ink.explorefuture_android.mine.related_user;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.ArrayList;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.common_unit.content_list.adapter.ContentListDataAdapter;
import itc.ink.explorefuture_android.common_unit.content_list.mode.ContentListDataMode;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.adapter.MindDataAdapter;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.common_unit.user_details.adapter.SimpleUserDataAdapter;
import itc.ink.explorefuture_android.common_unit.user_details.mode.SimpleUserInfoDataMode;
import itc.ink.explorefuture_android.mine.interaction.adapter.MineCommentDataAdapter;
import itc.ink.explorefuture_android.mine.interaction.mode.MineCommentListDataMode;
import itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.adapter_action.ActionDataAdapter;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_action.ActionListDataModel;

/**
 * Created by yangwenjiang on 2018/11/2.
 */

public class RelatedUserActivity extends Activity {
    private final static String LOG_TAG = "InteractionActivity";
    public static final String KEY_CURRENT_TAB = "current_tab";
    public static final String VALUE_TAB_FANS = "tab_fans";
    public static final String VALUE_TAB_ATTENTION = "tab_attention";

    private String interactionDataStr = null;

    private ConstraintLayout sortFansBtnLayout;
    private TextView sortFansBtn;
    private ConstraintLayout sortAttentionBtnLayout;
    private TextView sortAttentionBtn;
    private View navigationIndicator;

    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private ArrayList<SimpleUserInfoDataMode> mUserListData = new ArrayList<>();
    private SimpleUserDataAdapter mUserDataAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String sort_tab = intent.getStringExtra(KEY_CURRENT_TAB);

        //Get Content Data
        UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(sort_tab);
        updateAsyncTask.execute();

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.mine_related_user_activity);

        ImageView backBtn = findViewById(R.id.mine_Related_User_List_Activity_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(new BackBtnClickListener());

        SortBtnLayoutClickListener sortBtnLayoutClickListener = new SortBtnLayoutClickListener();
        sortFansBtnLayout = findViewById(R.id.mine_Related_User_List_Activity_Top_Sort_Fans_Btn_Layout);
        sortFansBtnLayout.setOnClickListener(sortBtnLayoutClickListener);
        sortFansBtn = findViewById(R.id.mine_Related_User_List_Activity_Top_Sort_Fans_Btn);
        sortAttentionBtnLayout = findViewById(R.id.mine_Related_User_List_Activity_Top_Sort_Attention_Btn_Layout);
        sortAttentionBtnLayout.setOnClickListener(sortBtnLayoutClickListener);
        sortAttentionBtn = findViewById(R.id.mine_Related_User_List_Activity_Top_Sort_Attention_Btn);
        navigationIndicator = findViewById(R.id.mine_Related_User_List_Activity_Top_Sort_Bar_Indicator);
        sortFansBtnLayout.setClickable(false);
        sortAttentionBtnLayout.setClickable(false);
        initSortNavigationBar(sort_tab);

        contentRecyclerView = findViewById(R.id.mine_Related_User_List_Activity_RecyclerView);
        mUserDataAdapter = new SimpleUserDataAdapter(this, mUserListData);
        contentRecyclerView.setAdapter(mUserDataAdapter);
        contentRvLayoutManager = new LinearLayoutManager(this);
        contentRecyclerView.setLayoutManager(contentRvLayoutManager);

    }

    private void initSortNavigationBar(String tabValue) {
        switch (tabValue) {
            case VALUE_TAB_FANS:
                updateNavigationTopBtnState(sortFansBtn);
                navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth * 0 / 2));
                break;
            case VALUE_TAB_ATTENTION:
                updateNavigationTopBtnState(sortAttentionBtn);
                navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth * 1 / 2));
                break;
        }
    }

    private <T> ArrayList<T> phraseStrToArray(Class<T> cls, String sourceStr, String arrayKey) {
        ArrayList<T> dataArray = new ArrayList<>();
        JsonReader jsonReader = new JsonReader(new StringReader(sourceStr));
        jsonReader.setLenient(true);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        JsonObject rootObj = jsonElement.getAsJsonObject();
        JsonArray contentDataJsonArray = rootObj.getAsJsonArray(arrayKey);
        Gson gson = new Gson();

        for (JsonElement jsonElements : contentDataJsonArray) {
            dataArray.add(gson.fromJson(jsonElements, cls));
        }

        return dataArray;
    }

    private void updateNavigationTopBtnState(View currentActiveBtn) {
        sortFansBtn.setAlpha(0.5F);
        sortAttentionBtn.setAlpha(0.5F);

        currentActiveBtn.setAlpha(1F);
    }

    class BackBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    class SortBtnLayoutClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.mine_Related_User_List_Activity_Top_Sort_Fans_Btn_Layout:
                    updateNavigationTopBtnState(sortFansBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth * 0 / 2));

                    if (interactionDataStr != null && !interactionDataStr.isEmpty()) {
                        mUserListData.clear();
                        for (int i = 0; i < 20; i++) {
                            mUserListData.addAll(phraseStrToArray(SimpleUserInfoDataMode.class, interactionDataStr, "array_fans"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        mUserDataAdapter.notifyDataSetChanged();
                    }

                    break;
                case R.id.mine_Related_User_List_Activity_Top_Sort_Attention_Btn_Layout:
                    updateNavigationTopBtnState(sortAttentionBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth * 1 / 2));

                    if (interactionDataStr != null && !interactionDataStr.isEmpty()) {
                        mUserListData.clear();
                        for (int i = 0; i < 20; i++) {
                            mUserListData.addAll(phraseStrToArray(SimpleUserInfoDataMode.class, interactionDataStr, "array_attention"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        mUserDataAdapter.notifyDataSetChanged();
                    }

                    break;
            }
        }
    }

    class UpdateAsyncTask extends AsyncTask<Void, Void, String> {
        String sort_tab = "";

        public UpdateAsyncTask(String sort_tab) {
            this.sort_tab = sort_tab;
        }

        @Override
        protected String doInBackground(Void... params) {
            String remoteDataFileUrl = "http://www.itc.ink/data/explorefuture_data/account/0000000001/information/related_user_data.json";
            String resultStr = DataUpdateUtil.getRemoteDataStr(remoteDataFileUrl);
            return resultStr;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty()) {
                interactionDataStr = s;

                switch (sort_tab) {
                    case VALUE_TAB_FANS:
                        mUserListData.clear();
                        for (int i = 0; i < 20; i++) {
                            mUserListData.addAll(phraseStrToArray(SimpleUserInfoDataMode.class, s, "array_fans"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        mUserDataAdapter.notifyDataSetChanged();
                        break;
                    case VALUE_TAB_ATTENTION:
                        mUserListData.clear();
                        for (int i = 0; i < 20; i++) {
                            mUserListData.addAll(phraseStrToArray(SimpleUserInfoDataMode.class, s, "array_attention"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        mUserDataAdapter.notifyDataSetChanged();
                        break;
                }

                sortFansBtnLayout.setClickable(true);
                sortAttentionBtnLayout.setClickable(true);
            }
        }
    }
}
