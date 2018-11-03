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
import itc.ink.explorefuture_android.common_unit.content_list.adapter.ContentListDataAdapter;
import itc.ink.explorefuture_android.common_unit.content_list.mode.ContentListDataMode;

/**
 * Created by yangwenjiang on 2018/11/2.
 */

public class SearchResultActivity extends Activity {
    private final static String LOG_TAG ="SearchResultActivity";
    public static final String KEY_SEARCH_TITLE="search_title";

    private ConstraintLayout sortSolutionBtnLayout;
    private TextView sortSolutionBtn;
    private ConstraintLayout sortProductBtnLayout;
    private TextView sortProductBtn;
    private ConstraintLayout sortSubjectBtnLayout;
    private TextView sortSubjectBtn;
    private ConstraintLayout sortActionBtnLayout;
    private TextView sortActionBtn;
    private ConstraintLayout sortPersonBtnLayout;
    private TextView sortPersonBtn;
    private View navigationIndicator;

    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private ArrayList<ContentListDataMode> contentListDataArray=new ArrayList<>();
    private ContentListDataAdapter contentListDataAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        String sort_title=intent.getStringExtra(KEY_SEARCH_TITLE);
        //Toast.makeText(ContentListActivity.this,"Sort ID:"+sort_id+"\nSort Title:"+sort_title,Toast.LENGTH_SHORT).show();

        //Get Content Data
        UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(SearchResultActivity.this);
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
        sortSubjectBtnLayout=findViewById(R.id.search_Result_List_Activity_Top_Sort_Subject_Btn_Layout);
        sortSubjectBtnLayout.setOnClickListener(sortBtnLayoutClickListener);
        sortSubjectBtn=findViewById(R.id.search_Result_List_Activity_Top_Sort_Subject_Btn);
        sortActionBtnLayout=findViewById(R.id.search_Result_List_Activity_Top_Sort_Action_Btn_Layout);
        sortActionBtnLayout.setOnClickListener(sortBtnLayoutClickListener);
        sortActionBtn=findViewById(R.id.search_Result_List_Activity_Top_Sort_Action_Btn);
        sortPersonBtnLayout=findViewById(R.id.search_Result_List_Activity_Top_Sort_Person_Btn_Layout);
        sortPersonBtnLayout.setOnClickListener(sortBtnLayoutClickListener);
        sortPersonBtn=findViewById(R.id.search_Result_List_Activity_Top_Sort_Person_Btn);
        navigationIndicator=findViewById(R.id.search_Result_List_Activity_Top_Sort_Bar_Indicator);

        contentRecyclerView=findViewById(R.id.search_Result_List_Activity_RecyclerView);
        contentListDataAdapter = new ContentListDataAdapter(this, contentListDataArray);
        contentRecyclerView.setAdapter(contentListDataAdapter);
        contentRvLayoutManager = new LinearLayoutManager(this);
        contentRecyclerView.setLayoutManager(contentRvLayoutManager);
    }

    private void updateNavigationTopBtnState(View currentActiveBtn) {
        sortSolutionBtn.setAlpha(0.5F);
        sortProductBtn.setAlpha(0.5F);
        sortSubjectBtn.setAlpha(0.5F);
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
                    break;
                case R.id.search_Result_List_Activity_Top_Sort_Product_Btn_Layout:
                    updateNavigationTopBtnState(sortProductBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth *1/5));
                    break;
                case R.id.search_Result_List_Activity_Top_Sort_Subject_Btn_Layout:
                    updateNavigationTopBtnState(sortSubjectBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth *2/5));
                    break;
                case R.id.search_Result_List_Activity_Top_Sort_Action_Btn_Layout:
                    updateNavigationTopBtnState(sortActionBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth *3/5));
                    break;
                case R.id.search_Result_List_Activity_Top_Sort_Person_Btn_Layout:
                    updateNavigationTopBtnState(sortPersonBtn);
                    navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth *4/5));
                    break;
            }
        }
    }

    class UpdateAsyncTask extends AsyncTask<Void, Void, String> {
        private WeakReference<Context> mWeakContextReference;

        UpdateAsyncTask(Context mContext) {
            this.mWeakContextReference = new WeakReference<>(mContext);
        }

        private Context getContext() {
            if(mWeakContextReference.get() != null){
                return mWeakContextReference.get();
            }
            return null;
        }

        @Override
        protected String doInBackground(Void... params) {
            String remoteDataFileUrl="http://www.itc.ink/data/explorefuture_data/app/sort/a/01/01/content_data.json";
            String resultStr=getRemoteData(remoteDataFileUrl);
            return resultStr;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty()) {
                ArrayList<ContentListDataMode> dataArray;
                JsonReader jsonReader = new JsonReader(new StringReader(s));
                jsonReader.setLenient(true);
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(jsonReader);
                JsonObject rootObj = jsonElement.getAsJsonObject();
                JsonArray contentDataJsonArray = rootObj.getAsJsonArray("array_content");
                Gson gson = new Gson();
                dataArray = gson.fromJson(contentDataJsonArray, new TypeToken<List<ContentListDataMode>>() {
                }.getType());

                contentListDataArray.clear();

                for (int i = 0; i < 20; i++) {
                    contentListDataArray.addAll(dataArray);
                }

                contentListDataAdapter.notifyDataSetChanged();
            }
        }

        private String getRemoteData(String remoteDataFileUrl) {
            HttpURLConnection urlConnection = null;
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL dataFileUrl = new URL(remoteDataFileUrl);

                urlConnection = (HttpURLConnection) dataFileUrl.openConnection();
                urlConnection.setConnectTimeout(3 * 1000);
                urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                InputStream inputStream = urlConnection.getInputStream();

                if (!urlConnection.getContentType().contains("json")) {
                    Log.d(LOG_TAG, "网络已被重定向，需确保网络通畅");
                    inputStream.close();
                    return null;
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String catchStr;
                while ((catchStr = bufferedReader.readLine()) != null) {
                    stringBuilder.append(catchStr);
                }

                bufferedReader.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return stringBuilder.toString();
        }

    }
}
