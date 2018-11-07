package itc.ink.explorefuture_android.mine.interaction;

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

public class InteractionActivity extends Activity {
    private final static String LOG_TAG = "InteractionActivity";
    public static final String KEY_CURRENT_TAB = "current_tab";
    public static final String VALUE_TAB_HISTORY = "tab_history";
    public static final String VALUE_TAB_COMMENT = "tab_comment";
    public static final String VALUE_TAB_RETRANSMISSION = "tab_retransmission";
    public static final String VALUE_TAB_COLLECTION = "ab_collection";
    public static final String VALUE_TAB_SUPPORT = "tab_support";

    private String interactionDataStr = null;

    private ConstraintLayout sortHistoryBtnLayout;
    private TextView sortHistoryBtn;
    private ConstraintLayout sortCommentBtnLayout;
    private TextView sortCommentBtn;
    private ConstraintLayout sortRetransmissionBtnLayout;
    private TextView sortRetransmissionBtn;
    private ConstraintLayout sortCollectionBtnLayout;
    private TextView sortCollectionBtn;
    private ConstraintLayout sortSupportBtnLayout;
    private TextView sortSupportBtn;
    private View navigationIndicator;

    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private ArrayList<ContentListDataMode> contentListData = new ArrayList<>();
    private ContentListDataAdapter contentListDataAdapter;
    private ArrayList<MineCommentListDataMode> mCommentListData = new ArrayList<>();
    private MineCommentDataAdapter mMineCommentDataAdapter;
    private ArrayList<MindListDataMode> mMindListData = new ArrayList<>();
    private MindDataAdapter mMindDataAdapter;

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

        setContentView(R.layout.mine_interaction_activity);

        ImageView backBtn = findViewById(R.id.mine_Interaction_List_Activity_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(new BackBtnClickListener());

        SortBtnLayoutClickListener sortBtnLayoutClickListener = new SortBtnLayoutClickListener();
        sortHistoryBtnLayout = findViewById(R.id.mine_Interaction_List_Activity_Top_Sort_History_Btn_Layout);
        sortHistoryBtnLayout.setOnClickListener(sortBtnLayoutClickListener);
        sortHistoryBtn = findViewById(R.id.mine_Interaction_List_Activity_Top_Sort_History_Btn);
        sortCommentBtnLayout = findViewById(R.id.mine_Interaction_List_Activity_Top_Sort_Comment_Btn_Layout);
        sortCommentBtnLayout.setOnClickListener(sortBtnLayoutClickListener);
        sortCommentBtn = findViewById(R.id.mine_Interaction_List_Activity_Top_Sort_Comment_Btn);
        sortRetransmissionBtnLayout = findViewById(R.id.mine_Interaction_List_Activity_Top_Sort_Retransmission_Btn_Layout);
        sortRetransmissionBtnLayout.setOnClickListener(sortBtnLayoutClickListener);
        sortRetransmissionBtn = findViewById(R.id.mine_Interaction_List_Activity_Top_Sort_Retransmission_Btn);
        sortCollectionBtnLayout = findViewById(R.id.mine_Interaction_List_Activity_Top_Sort_Collection_Btn_Layout);
        sortCollectionBtnLayout.setOnClickListener(sortBtnLayoutClickListener);
        sortCollectionBtn = findViewById(R.id.mine_Interaction_List_Activity_Top_Sort_Collection_Btn);
        sortSupportBtnLayout = findViewById(R.id.mine_Interaction_List_Activity_Top_Sort_Support_Btn_Layout);
        sortSupportBtnLayout.setOnClickListener(sortBtnLayoutClickListener);
        sortSupportBtn = findViewById(R.id.mine_Interaction_List_Activity_Top_Sort_Support_Btn);
        navigationIndicator = findViewById(R.id.mine_Interaction_List_Activity_Top_Sort_Bar_Indicator);
        sortHistoryBtnLayout.setClickable(false);
        sortCommentBtnLayout.setClickable(false);
        sortRetransmissionBtnLayout.setClickable(false);
        sortCollectionBtnLayout.setClickable(false);
        sortSupportBtnLayout.setClickable(false);
        initSortNavigationBar(sort_tab);

        contentRecyclerView = findViewById(R.id.mine_Interaction_List_Activity_RecyclerView);
        contentListDataAdapter = new ContentListDataAdapter(this, contentListData);
        contentRecyclerView.setAdapter(contentListDataAdapter);
        contentRvLayoutManager = new LinearLayoutManager(this);
        contentRecyclerView.setLayoutManager(contentRvLayoutManager);

        mMineCommentDataAdapter = new MineCommentDataAdapter(InteractionActivity.this, mCommentListData);
        mMindDataAdapter = new MindDataAdapter(InteractionActivity.this, mMindListData);
    }

    private void initSortNavigationBar(String tabValue) {
        switch (tabValue) {
            case VALUE_TAB_HISTORY:
                updateNavigationTopBtnState(sortHistoryBtn);
                navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth * 0 / 5));
                break;
            case VALUE_TAB_COMMENT:
                updateNavigationTopBtnState(sortCommentBtn);
                navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth * 1 / 5));
                break;
            case VALUE_TAB_RETRANSMISSION:
                updateNavigationTopBtnState(sortRetransmissionBtn);
                navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth * 2 / 5));
                break;
            case VALUE_TAB_COLLECTION:
                updateNavigationTopBtnState(sortCollectionBtn);
                navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth * 3 / 5));
                break;
            case VALUE_TAB_SUPPORT:
                updateNavigationTopBtnState(sortSupportBtn);
                navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth * 4 / 5));
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
        sortHistoryBtn.setAlpha(0.5F);
        sortCommentBtn.setAlpha(0.5F);
        sortRetransmissionBtn.setAlpha(0.5F);
        sortCollectionBtn.setAlpha(0.5F);
        sortSupportBtn.setAlpha(0.5F);

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
                case R.id.mine_Interaction_List_Activity_Top_Sort_History_Btn_Layout:
                    updateNavigationTopBtnState(sortHistoryBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth * 0 / 5));

                    if (interactionDataStr != null && !interactionDataStr.isEmpty()) {
                        contentListData.clear();
                        for (int i = 0; i < 20; i++) {
                            contentListData.addAll(phraseStrToArray(ContentListDataMode.class, interactionDataStr, "array_history"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        contentListDataAdapter.notifyDataSetChanged();
                    }

                    contentRecyclerView.setAdapter(contentListDataAdapter);
                    break;
                case R.id.mine_Interaction_List_Activity_Top_Sort_Comment_Btn_Layout:
                    updateNavigationTopBtnState(sortCommentBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth * 1 / 5));

                    if (interactionDataStr != null && !interactionDataStr.isEmpty()) {
                        mCommentListData.clear();
                        for (int i = 0; i < 20; i++) {
                            mCommentListData.addAll(phraseStrToArray(MineCommentListDataMode.class, interactionDataStr, "array_comment"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        mMineCommentDataAdapter.notifyDataSetChanged();
                    }

                    contentRecyclerView.setAdapter(mMineCommentDataAdapter);
                    break;
                case R.id.mine_Interaction_List_Activity_Top_Sort_Retransmission_Btn_Layout:
                    updateNavigationTopBtnState(sortRetransmissionBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth * 2 / 5));

                    if (interactionDataStr != null && !interactionDataStr.isEmpty()) {
                        mMindListData.clear();
                        for (int i = 0; i < 20; i++) {
                            mMindListData.addAll(phraseStrToArray(MindListDataMode.class, interactionDataStr, "array_retransmission"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        contentListDataAdapter.notifyDataSetChanged();
                    }

                    contentRecyclerView.setAdapter(mMindDataAdapter);
                    break;
                case R.id.mine_Interaction_List_Activity_Top_Sort_Collection_Btn_Layout:
                    updateNavigationTopBtnState(sortCollectionBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth * 3 / 5));

                    if (interactionDataStr != null && !interactionDataStr.isEmpty()) {
                        contentListData.clear();
                        for (int i = 0; i < 20; i++) {
                            contentListData.addAll(phraseStrToArray(ContentListDataMode.class, interactionDataStr, "array_collection"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        contentListDataAdapter.notifyDataSetChanged();
                    }

                    contentRecyclerView.setAdapter(contentListDataAdapter);
                    break;
                case R.id.mine_Interaction_List_Activity_Top_Sort_Support_Btn_Layout:
                    updateNavigationTopBtnState(sortSupportBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth * 4 / 5));

                    if (interactionDataStr != null && !interactionDataStr.isEmpty()) {
                        contentListData.clear();
                        for (int i = 0; i < 20; i++) {
                            contentListData.addAll(phraseStrToArray(ContentListDataMode.class, interactionDataStr, "array_support"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        contentListDataAdapter.notifyDataSetChanged();
                    }

                    contentRecyclerView.setAdapter(contentListDataAdapter);
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
            String remoteDataFileUrl = "http://www.itc.ink/data/explorefuture_data/account/0000000001/information/interaction_data.json";
            String resultStr = DataUpdateUtil.getRemoteDataStr(remoteDataFileUrl);
            return resultStr;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty()) {
                interactionDataStr = s;

                switch (sort_tab) {
                    case VALUE_TAB_HISTORY:
                        contentListData.clear();
                        for (int i = 0; i < 20; i++) {
                            contentListData.addAll(phraseStrToArray(ContentListDataMode.class, s, "array_history"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        contentListDataAdapter.notifyDataSetChanged();
                        contentRecyclerView.setAdapter(contentListDataAdapter);
                        break;
                    case VALUE_TAB_COMMENT:
                        mCommentListData.clear();
                        for (int i = 0; i < 20; i++) {
                            mCommentListData.addAll(phraseStrToArray(MineCommentListDataMode.class, s, "array_comment"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        mMineCommentDataAdapter.notifyDataSetChanged();
                        contentRecyclerView.setAdapter(mMineCommentDataAdapter);
                        break;
                    case VALUE_TAB_RETRANSMISSION:
                        mMindListData.clear();
                        for (int i = 0; i < 20; i++) {
                            mMindListData.addAll(phraseStrToArray(MindListDataMode.class, s, "array_retransmission"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        contentListDataAdapter.notifyDataSetChanged();
                        contentRecyclerView.setAdapter(mMindDataAdapter);
                        break;
                    case VALUE_TAB_COLLECTION:
                        contentListData.clear();
                        for (int i = 0; i < 20; i++) {
                            contentListData.addAll(phraseStrToArray(ContentListDataMode.class, s, "array_collection"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        contentListDataAdapter.notifyDataSetChanged();
                        contentRecyclerView.setAdapter(contentListDataAdapter);
                        break;
                    case VALUE_TAB_SUPPORT:
                        contentListData.clear();
                        for (int i = 0; i < 20; i++) {
                            contentListData.addAll(phraseStrToArray(ContentListDataMode.class, s, "array_support"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        contentListDataAdapter.notifyDataSetChanged();
                        contentRecyclerView.setAdapter(contentListDataAdapter);
                        break;
                }

                sortHistoryBtnLayout.setClickable(true);
                sortCommentBtnLayout.setClickable(true);
                sortRetransmissionBtnLayout.setClickable(true);
                sortCollectionBtnLayout.setClickable(true);
                sortSupportBtnLayout.setClickable(true);
            }
        }
    }
}
