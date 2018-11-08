package itc.ink.explorefuture_android.mine.settings;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.ArrayList;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.mine.settings.adapter.SettingsHelpCenterDataAdapter;
import itc.ink.explorefuture_android.mine.settings.adapter.SettingsReceiveAddressDataAdapter;
import itc.ink.explorefuture_android.mine.settings.mode.HelpCenterDataMode;
import itc.ink.explorefuture_android.mine.settings.mode.ReceiveAddressDataMode;

/**
 * Created by yangwenjiang on 2018/11/2.
 */

public class SettingsHelpCenterActivity extends Activity {
    private final static String LOG_TAG = "SettingsHelpCenterActivity";

    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private ArrayList<HelpCenterDataMode> faqListData = new ArrayList<>();
    private SettingsHelpCenterDataAdapter faqListDataAdapter;
    ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Content Data
        UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask();
        updateAsyncTask.execute();

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.mine_help_center_activity);

        NavigationBtnClickListener navigationBtnClickListener=new NavigationBtnClickListener();
        ImageView backBtn = findViewById(R.id.settings_Help_Center_List_Activity_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(navigationBtnClickListener);
        ImageView customerServiceBtn= findViewById(R.id.settings_Help_Center_Top_Navigation_Call_Customer_Service_Btn);
        customerServiceBtn.setOnClickListener(navigationBtnClickListener);

        contentRecyclerView = findViewById(R.id.settings_Help_Center_List_Activity_RecyclerView);
        faqListDataAdapter = new SettingsHelpCenterDataAdapter(this, faqListData);
        contentRecyclerView.setAdapter(faqListDataAdapter);
        contentRvLayoutManager = new LinearLayoutManager(this);
        contentRecyclerView.setLayoutManager(contentRvLayoutManager);

    }

    class NavigationBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.settings_Help_Center_List_Activity_Top_Navigation_Back_Btn:
                    finish();
                    break;
                case R.id.settings_Help_Center_Top_Navigation_Call_Customer_Service_Btn:
                    Toast.makeText(SettingsHelpCenterActivity.this,"联系客服",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }

    class UpdateAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String remoteDataFileUrl = "http://www.itc.ink/data/explorefuture_data/app/mine/faq_data.json";
            String resultStr = DataUpdateUtil.getRemoteDataStr(remoteDataFileUrl);
            return resultStr;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty())
                faqListData.clear();
            faqListData.addAll(phraseStrToArray(HelpCenterDataMode.class, s, "array_faq"));
            contentRecyclerView.scrollToPosition(0);
            faqListDataAdapter.notifyDataSetChanged();
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
    }

}

