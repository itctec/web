package itc.ink.explorefuture_android.sort;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.activity.MainActivity;
import itc.ink.explorefuture_android.app.utils.SQLiteDBHelper;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.common_unit.scrollar_navigation.ScrollableNavigationBar;
import itc.ink.explorefuture_android.common_unit.search_bar.SearchBar;
import itc.ink.explorefuture_android.recommend.attention_fragment.adapter.AttentionWrapAdapter;
import itc.ink.explorefuture_android.sort.adapter.SearchHistoryDataAdapter;
import itc.ink.explorefuture_android.sort.adapter.SortDataAdapter;
import itc.ink.explorefuture_android.sort.mode.DataLoad;
import itc.ink.explorefuture_android.sort.mode.mode_navigation.NavigationBarDataMode;
import itc.ink.explorefuture_android.sort.mode.mode_sort.SortListDataMode;


/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class SortFragment extends Fragment {
    public static boolean allTabNow=true;

    private SearchBar searchBar;

    private ScrollableNavigationBar scrollableNavigationBar;
    private static RecyclerView contentRecyclerView;
    private static SortDataAdapter mSortDataAdapter;

    private DataLoad mDataLoad;
    private static ArrayList<SortListDataMode> mSortListData;
    private ArrayList<NavigationBarDataMode> mSortTitleListData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataLoad = new DataLoad();

        //Judge Data Prepare
        if (mDataLoad.outService.prepareData(getContext())) {
            mSortListData = (ArrayList<SortListDataMode>) mDataLoad.outService.loadSortData();
            if (mSortListData.size() >= 0) {
                mSortDataAdapter = new SortDataAdapter(getActivity(), mSortListData,new ContentItemMoreClickCallBack());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sort_fragment, container, false);

        //Search Bar
        searchBar=rootView.findViewById(R.id.sort_Top_Search_Bar);
        searchBar.setOutCallBack(new SearchBarCallBack());

        //Scroll Bar
        scrollableNavigationBar = rootView.findViewById(R.id.sort_Top_Navigation_Bar);
        if (mSortListData.size() >= 0) {
            mSortTitleListData = getSortTitleListData(mSortListData);
            scrollableNavigationBar.setDataAndCallBack(getActivity(), mSortTitleListData, new NavigationBarItemClickCallBack());
        }

        //Content
        contentRecyclerView = rootView.findViewById(R.id.sort_RecyclerView);
        if (mSortDataAdapter != null) {
            contentRecyclerView.setAdapter(mSortDataAdapter);
            RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(getActivity());
            contentRecyclerView.setLayoutManager(contentRvLayoutManager);
        }

        return rootView;
    }

    private ArrayList<NavigationBarDataMode> getSortTitleListData(ArrayList<SortListDataMode> sortDataArray) {
        ArrayList<NavigationBarDataMode> sortTitleListData = new ArrayList<>();
        NavigationBarDataMode firstNavigationBarDataMode = new NavigationBarDataMode("all", getString(R.string.sort_navigation_all_text));
        sortTitleListData.add(firstNavigationBarDataMode);
        for (SortListDataMode sortListDataMode : sortDataArray) {
            NavigationBarDataMode navigationBarData = new NavigationBarDataMode();
            navigationBarData.setId(sortListDataMode.getSort_id());
            navigationBarData.setTitle(sortListDataMode.getSort_title());
            sortTitleListData.add(navigationBarData);
        }

        return sortTitleListData;
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    class NavigationBarItemClickCallBack implements ScrollableNavigationBar.OutCallBack {
        @Override
        public void onTitleClick(String id) {
            if(id.equals("all")){
                allTabNow=true;
            }else{
                allTabNow=false;
            }

            UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(getContext(), id);
            updateAsyncTask.execute();
        }
    }

    class ContentItemMoreClickCallBack implements SortDataAdapter.OutCallBack {
        @Override
        public void onItemClick(String id) {
            if(id.equals("all")){
                allTabNow=true;
            }else{
                allTabNow=false;
            }

            UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(getContext(), id);
            updateAsyncTask.execute();
            int i;
            for(i=0;i<mSortTitleListData.size();i++){
                if(mSortTitleListData.get(i).getId().equals(id)){
                    break;
                }
            }
            scrollableNavigationBar.setCurrentFocusItem(null,i);

        }
    }

    static class UpdateAsyncTask extends AsyncTask<Void, Void, String> {
        private WeakReference<Context> mWeakContextReference;
        private String sort_id = "";

        UpdateAsyncTask(Context mContext, String sort_id) {
            this.mWeakContextReference = new WeakReference<>(mContext);
            this.sort_id = sort_id;
        }

        private Context getContext() {
            if (mWeakContextReference.get() != null) {
                return mWeakContextReference.get();
            }
            return null;
        }

        private String getLocalData(String fileName) {

            StringBuilder stringBuilder = new StringBuilder();

            File localDataFile = new File(getContext().getFilesDir(), fileName);
            BufferedReader bufferedReader;
            if (localDataFile.exists()) {
                try {
                    InputStream inputStream = new FileInputStream(localDataFile);
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    String catchStr;
                    while ((catchStr = bufferedReader.readLine()) != null) {
                        stringBuilder.append(catchStr);
                    }

                    inputStream.close();
                    bufferedReader.close();
                    return stringBuilder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected String doInBackground(Void... params) {
            //Danger Point
            Log.d("ITC", "潜在风险点");
            DataUpdateMode sortDataUpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                    DataUpdateMode.SORT_ALL_DATA_FILE_URL.replace("/all/", "/" + sort_id + "/"),
                    DataUpdateMode.SORT_ALL_DATA_NEWEST_UPDATE_DATE_TIME_KEY.replace("_all_", "_" + sort_id + "_"),
                    DataUpdateMode.SORT_ALL_LOCAL_DATA_FILE_NAME.replace("_all_", "_" + sort_id + "_"));

            String resultStr = DataUpdateUtil.updateData(getContext(),sortDataUpdateMode);

            if (resultStr != null && !resultStr.isEmpty()) {
                switch (resultStr) {
                    case DataUpdateUtil.UPDATE_RESULT_NEWEST_ALREADY:
                        resultStr = getLocalData(DataUpdateMode.SORT_ALL_LOCAL_DATA_FILE_NAME.replace("_all_", "_" + sort_id + "_"));
                        break;
                    case DataUpdateUtil.UPDATE_RESULT_FAILED:
                        resultStr = getLocalData(DataUpdateMode.SORT_ALL_LOCAL_DATA_FILE_NAME.replace("_all_", "_" + sort_id + "_"));
                        break;
                }
            }

            return resultStr;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty()) {
                ArrayList<SortListDataMode> sortDataArray;
                JsonReader jsonReader = new JsonReader(new StringReader(s));
                jsonReader.setLenient(true);
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(jsonReader);
                JsonObject rootObj = jsonElement.getAsJsonObject();
                JsonArray sortDataJsonArray = rootObj.getAsJsonArray("array_base_sort");
                Gson gson = new Gson();
                sortDataArray = gson.fromJson(sortDataJsonArray, new TypeToken<List<SortListDataMode>>() {
                }.getType());

                mSortListData.clear();
                for (SortListDataMode sortListDataMode : sortDataArray) {
                    mSortListData.add(sortListDataMode);
                }
            } else {
                Toast.makeText(getContext(), "更新数据失败", Toast.LENGTH_SHORT).show();
                mSortListData.clear();
            }
            contentRecyclerView.scrollToPosition(0);
            mSortDataAdapter.notifyDataSetChanged();
        }
    }

    class SearchBarCallBack implements SearchBar.OutCallBack{
        @Override
        public void onSearchFocusChange(boolean focused) {
            if (focused){
                scrollableNavigationBar.setVisibility(View.GONE);
                contentRecyclerView.setVisibility(View.GONE);
            }else{
                scrollableNavigationBar.setVisibility(View.VISIBLE);
                contentRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
}
