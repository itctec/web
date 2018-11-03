package itc.ink.explorefuture_android.common_unit.content_list;

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
import android.widget.TextView;
import android.widget.Toast;

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
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.common_unit.content_list.adapter.ContentListDataAdapter;
import itc.ink.explorefuture_android.common_unit.content_list.mode.ContentListDataMode;

/**
 * Created by yangwenjiang on 2018/11/2.
 */

public class ContentListActivity extends Activity {
    private final static String LOG_TAG ="ContentListActivity";
    public static final String KEY_SORT_ID="sort_id";
    public static final String KEY_SORT_TITLE="sort_title";

    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private ArrayList<ContentListDataMode> contentListDataArray=new ArrayList<>();
    private ContentListDataAdapter contentListDataAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        String sort_id=intent.getStringExtra(KEY_SORT_ID);
        String sort_title=intent.getStringExtra(KEY_SORT_TITLE);
        Toast.makeText(ContentListActivity.this,"Sort ID:"+sort_id+"\nSort Title:"+sort_title,Toast.LENGTH_SHORT).show();

        //Get Content Data
        UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(ContentListActivity.this);
        updateAsyncTask.execute();

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.common_unit_content_list_activity);

        ImageView backBtn=findViewById(R.id.content_List_Activity_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(new BackBtnClickListener());
        TextView topNavigationTitle=findViewById(R.id.content_List_Activity_Top_Navigation_Title_Text);
        topNavigationTitle.setText(sort_title);

        contentRecyclerView=findViewById(R.id.content_List_Activity_RecyclerView);
        contentListDataAdapter = new ContentListDataAdapter(this, contentListDataArray);
        contentRecyclerView.setAdapter(contentListDataAdapter);
        contentRvLayoutManager = new LinearLayoutManager(this);
        contentRecyclerView.setLayoutManager(contentRvLayoutManager);
    }

    class BackBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
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
                for (ContentListDataMode contentListDataMode : dataArray) {
                    contentListDataArray.add(contentListDataMode);
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
