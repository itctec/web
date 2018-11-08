package itc.ink.explorefuture_android.mine.settings;

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
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.common_unit.content_list.adapter.ContentListDataAdapter;
import itc.ink.explorefuture_android.common_unit.content_list.mode.ContentListDataMode;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.adapter.MindDataAdapter;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.mine.interaction.adapter.MineCommentDataAdapter;
import itc.ink.explorefuture_android.mine.interaction.mode.MineCommentListDataMode;
import itc.ink.explorefuture_android.mine.settings.adapter.SettingsReceiveAddressDataAdapter;
import itc.ink.explorefuture_android.mine.settings.mode.ReceiveAddressDataMode;

/**
 * Created by yangwenjiang on 2018/11/2.
 */

public class SettingsReceiveAddressActivity extends Activity {
    private final static String LOG_TAG = "SettingsReceiveAddressActivity";

    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private ArrayList<ReceiveAddressDataMode> addressListData = new ArrayList<>();
    private SettingsReceiveAddressDataAdapter addressListDataAdapter;
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

        setContentView(R.layout.mine_receive_address_activity);

        NavigationBtnClickListener navigationBtnClickListener=new NavigationBtnClickListener();
        ImageView backBtn = findViewById(R.id.settings_Receive_Address_List_Activity_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(navigationBtnClickListener);
        TextView addBtn= findViewById(R.id.settings_Receive_Address_Top_Navigation_Add_Btn);
        addBtn.setOnClickListener(navigationBtnClickListener);

        contentRecyclerView = findViewById(R.id.settings_Receive_Address_List_Activity_RecyclerView);
        addressListDataAdapter = new SettingsReceiveAddressDataAdapter(this, addressListData);
        contentRecyclerView.setAdapter(addressListDataAdapter);
        contentRvLayoutManager = new LinearLayoutManager(this);
        contentRecyclerView.setLayoutManager(contentRvLayoutManager);

    }

    class NavigationBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.settings_Receive_Address_List_Activity_Top_Navigation_Back_Btn:
                    finish();
                    break;
                case R.id.settings_Receive_Address_Top_Navigation_Add_Btn:
                    Toast.makeText(SettingsReceiveAddressActivity.this,"添加地址被点击",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }

    class UpdateAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String remoteDataFileUrl = "http://www.itc.ink/data/explorefuture_data/account/0000000001/information/receive_address_data.json";
            String resultStr = DataUpdateUtil.getRemoteDataStr(remoteDataFileUrl);
            return resultStr;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty())
                addressListData.clear();
            addressListData.addAll(phraseStrToArray(ReceiveAddressDataMode.class, s, "array_receive_address"));
            contentRecyclerView.scrollToPosition(0);
            addressListDataAdapter.notifyDataSetChanged();
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

