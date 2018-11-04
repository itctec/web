package itc.ink.explorefuture_android.common_unit.search_result;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.common_unit.content_list.adapter.ContentListDataAdapter;
import itc.ink.explorefuture_android.common_unit.content_list.mode.ContentListDataMode;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.adapter.MindDataAdapter;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.common_unit.user_details.adapter.SimpleUserDataAdapter;
import itc.ink.explorefuture_android.common_unit.user_details.mode.SimpleUserInfoDataMode;
import itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.adapter_action.ActionDataAdapter;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_action.ActionListDataModel;

/**
 * Created by yangwenjiang on 2018/11/2.
 */

public class SearchResultActivity extends Activity {
    private final static String LOG_TAG ="SearchResultActivity";
    public static final String KEY_SEARCH_TITLE="search_title";

    private String searchResultStr=null;

    private ConstraintLayout sortSolutionBtnLayout;
    private TextView sortSolutionBtn;
    private ConstraintLayout sortProductBtnLayout;
    private TextView sortProductBtn;
    private ConstraintLayout sortMindBtnLayout;
    private TextView sortMindBtn;
    private ConstraintLayout sortActionBtnLayout;
    private TextView sortActionBtn;
    private ConstraintLayout sortPersonBtnLayout;
    private TextView sortPersonBtn;
    private View navigationIndicator;

    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private ArrayList<ContentListDataMode> contentListData=new ArrayList<>();
    private ContentListDataAdapter contentListDataAdapter;
    private ArrayList<MindListDataMode> mMindListData = new ArrayList<>();
    private MindDataAdapter mMindDataAdapter;
    private ArrayList<ActionListDataModel> mActionListData=new ArrayList<>();
    private ActionDataAdapter mActionDataAdapter;
    private ArrayList<SimpleUserInfoDataMode> mUserListData=new ArrayList<>();
    private SimpleUserDataAdapter mUserDataAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        String sort_title=intent.getStringExtra(KEY_SEARCH_TITLE);
        //Toast.makeText(ContentListActivity.this,"Sort ID:"+sort_id+"\nSort Title:"+sort_title,Toast.LENGTH_SHORT).show();

        //Get Content Data
        UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask();
        updateAsyncTask.execute();

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.common_unit_search_result_list_activity);

        ImageView backBtn=findViewById(R.id.search_Result_List_Activity_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(new BackBtnClickListener());
        TextView topNavigationTitle=findViewById(R.id.search_Result_List_Activity_Top_Navigation_Title_Text);
        topNavigationTitle.setText(sort_title);

        SortBtnLayoutClickListener sortBtnLayoutClickListener=new SortBtnLayoutClickListener();
        sortSolutionBtnLayout=findViewById(R.id.search_Result_List_Activity_Top_Sort_Solution_Btn_Layout);
        sortSolutionBtnLayout.setOnClickListener(sortBtnLayoutClickListener);
        sortSolutionBtn=findViewById(R.id.search_Result_List_Activity_Top_Sort_Solution_Btn);
        sortProductBtnLayout=findViewById(R.id.search_Result_List_Activity_Top_Sort_Product_Btn_Layout);
        sortProductBtnLayout.setOnClickListener(sortBtnLayoutClickListener);
        sortProductBtn=findViewById(R.id.search_Result_List_Activity_Top_Sort_Product_Btn);
        sortMindBtnLayout=findViewById(R.id.search_Result_List_Activity_Top_Sort_Mind_Btn_Layout);
        sortMindBtnLayout.setOnClickListener(sortBtnLayoutClickListener);
        sortMindBtn=findViewById(R.id.search_Result_List_Activity_Top_Sort_Mind_Btn);
        sortActionBtnLayout=findViewById(R.id.search_Result_List_Activity_Top_Sort_Action_Btn_Layout);
        sortActionBtnLayout.setOnClickListener(sortBtnLayoutClickListener);
        sortActionBtn=findViewById(R.id.search_Result_List_Activity_Top_Sort_Action_Btn);
        sortPersonBtnLayout=findViewById(R.id.search_Result_List_Activity_Top_Sort_Person_Btn_Layout);
        sortPersonBtnLayout.setOnClickListener(sortBtnLayoutClickListener);
        sortPersonBtn=findViewById(R.id.search_Result_List_Activity_Top_Sort_Person_Btn);
        navigationIndicator=findViewById(R.id.search_Result_List_Activity_Top_Sort_Bar_Indicator);
        sortSolutionBtnLayout.setClickable(false);
        sortProductBtnLayout.setClickable(false);
        sortMindBtnLayout.setClickable(false);
        sortActionBtnLayout.setClickable(false);
        sortPersonBtnLayout.setClickable(false);

        contentRecyclerView=findViewById(R.id.search_Result_List_Activity_RecyclerView);
        contentListDataAdapter = new ContentListDataAdapter(this, contentListData);
        contentRecyclerView.setAdapter(contentListDataAdapter);
        contentRvLayoutManager = new LinearLayoutManager(this);
        contentRecyclerView.setLayoutManager(contentRvLayoutManager);

        mMindDataAdapter = new MindDataAdapter(SearchResultActivity.this, mMindListData);
        mActionDataAdapter = new ActionDataAdapter(SearchResultActivity.this, mActionListData);
        mUserDataAdapter = new SimpleUserDataAdapter(SearchResultActivity.this, mUserListData);
    }

    private <T> ArrayList<T> phraseStrToArray(Class<T> cls,String sourceStr, String arrayKey){
        ArrayList<T> dataArray=new ArrayList<>();
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

        return  dataArray;
    }

    private void updateNavigationTopBtnState(View currentActiveBtn) {
        sortSolutionBtn.setAlpha(0.5F);
        sortProductBtn.setAlpha(0.5F);
        sortMindBtn.setAlpha(0.5F);
        sortActionBtn.setAlpha(0.5F);
        sortPersonBtn.setAlpha(0.5F);

        currentActiveBtn.setAlpha(1F);
    }

    class BackBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    class SortBtnLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.search_Result_List_Activity_Top_Sort_Solution_Btn_Layout:
                    updateNavigationTopBtnState(sortSolutionBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth *0/5));

                    if(searchResultStr!=null&&!searchResultStr.isEmpty()){
                        contentListData.clear();
                        for (int i = 0; i < 20; i++) {
                            contentListData.addAll(phraseStrToArray(ContentListDataMode.class,searchResultStr,"array_solution"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        contentListDataAdapter.notifyDataSetChanged();
                    }

                    contentRecyclerView.setAdapter(contentListDataAdapter);
                    break;
                case R.id.search_Result_List_Activity_Top_Sort_Product_Btn_Layout:
                    updateNavigationTopBtnState(sortProductBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth *1/5));

                    if(searchResultStr!=null&&!searchResultStr.isEmpty()){
                        contentListData.clear();
                        for (int i = 0; i < 20; i++) {
                            contentListData.addAll(phraseStrToArray(ContentListDataMode.class,searchResultStr,"array_product"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        contentListDataAdapter.notifyDataSetChanged();
                    }

                    contentRecyclerView.setAdapter(contentListDataAdapter);
                    break;
                case R.id.search_Result_List_Activity_Top_Sort_Mind_Btn_Layout:
                    updateNavigationTopBtnState(sortMindBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth *2/5));

                    if(searchResultStr!=null&&!searchResultStr.isEmpty()){
                        mMindListData.clear();
                        for (int i = 0; i < 20; i++) {
                            mMindListData.addAll(phraseStrToArray(MindListDataMode.class,searchResultStr,"array_mind"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        contentListDataAdapter.notifyDataSetChanged();
                    }

                    contentRecyclerView.setAdapter(mMindDataAdapter);
                    break;
                case R.id.search_Result_List_Activity_Top_Sort_Action_Btn_Layout:
                    updateNavigationTopBtnState(sortActionBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth *3/5));

                    if(searchResultStr!=null&&!searchResultStr.isEmpty()){
                        mActionListData.clear();
                        for (int i = 0; i < 20; i++) {
                            mActionListData.addAll(phraseStrToArray(ActionListDataModel.class,searchResultStr,"array_action"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        mActionDataAdapter.notifyDataSetChanged();
                    }

                    contentRecyclerView.setAdapter(mActionDataAdapter);
                    break;
                case R.id.search_Result_List_Activity_Top_Sort_Person_Btn_Layout:
                    updateNavigationTopBtnState(sortPersonBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth *4/5));

                    if(searchResultStr!=null&&!searchResultStr.isEmpty()){
                        mUserListData.clear();
                        for (int i = 0; i < 20; i++) {
                            mUserListData.addAll(phraseStrToArray(SimpleUserInfoDataMode.class,searchResultStr,"array_user"));
                        }
                        contentRecyclerView.scrollToPosition(0);
                        mUserDataAdapter.notifyDataSetChanged();
                    }

                    contentRecyclerView.setAdapter(mUserDataAdapter);
                    break;
            }
        }
    }

    class UpdateAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String remoteDataFileUrl="http://www.itc.ink/data/explorefuture_data/app/find/result_data.json";
            String resultStr= DataUpdateUtil.getRemoteDataStr(remoteDataFileUrl);
            return resultStr;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty()) {
                searchResultStr=s;

                contentListData.clear();
                for (int i = 0; i < 20; i++) {
                    contentListData.addAll(phraseStrToArray(ContentListDataMode.class,s,"array_solution"));
                }
                contentListDataAdapter.notifyDataSetChanged();

                sortSolutionBtnLayout.setClickable(true);
                sortProductBtnLayout.setClickable(true);
                sortMindBtnLayout.setClickable(true);
                sortActionBtnLayout.setClickable(true);
                sortPersonBtnLayout.setClickable(true);

            }
        }

    }
}
