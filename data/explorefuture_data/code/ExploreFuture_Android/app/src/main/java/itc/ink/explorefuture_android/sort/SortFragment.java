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
    private ConstraintLayout searchBarLayout;
    private EditText searchBarEdit;
    private ImageView searchBarClearBtn;
    private TextView searchBarCancelBtn;
    private ConstraintLayout searchHistoryLayout;
    private RecyclerView searchHistoryRecyclerView;
    private ArrayList<String> mSearchHistoryListData;
    private SearchHistoryDataAdapter mHistoryDataAdapter;

    private ScrollableNavigationBar scrollableNavigationBar;
    private static RecyclerView contentRecyclerView;
    private static SortDataAdapter mSortDataAdapter;

    private SQLiteDBHelper sqLiteDBHelper;

    private DataLoad mDataLoad;
    private static ArrayList<SortListDataMode> mSortListData;
    private ArrayList<NavigationBarDataMode> mSortTitleListData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataLoad = new DataLoad();

        //Judge Data Prepare
        if (mDataLoad.outService.prepareData()) {
            mSortListData = (ArrayList<SortListDataMode>) mDataLoad.outService.loadSortData();
            if (mSortListData.size() >= 0) {
                mSortDataAdapter = new SortDataAdapter(getActivity(), mSortListData);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sort_fragment, container, false);

        //Search Bar
        searchBarLayout = rootView.findViewById(R.id.sort_Top_Search_Bar_Layout);
        searchBarEdit = rootView.findViewById(R.id.sort_Top_Search_Bar_Edit);
        searchBarEdit.addTextChangedListener(new SearchBarEditTextWatcher());
        searchBarEdit.setOnFocusChangeListener(new SearchBarEditFocusChangedListener());
        searchBarEdit.setOnEditorActionListener(new SearchBarEditActionListener());
        searchBarClearBtn = rootView.findViewById(R.id.sort_Top_Search_Bar_Clear_Btn);
        searchBarClearBtn.setOnClickListener(new SearchBarClearBtnClickListener());
        searchBarCancelBtn = rootView.findViewById(R.id.sort_Top_Search_Bar_Cancel_Btn);
        searchBarCancelBtn.setOnClickListener(new SearchBarCancelBtnClickListener());
        searchHistoryLayout = rootView.findViewById(R.id.sort_Search_History_Layout);
        searchHistoryRecyclerView = rootView.findViewById(R.id.sort_Search_History_RecyclerView);
        RecyclerView.LayoutManager historyRvLayoutManager = new LinearLayoutManager(getActivity());
        searchHistoryRecyclerView.setLayoutManager(historyRvLayoutManager);


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

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    class SearchBarEditFocusChangedListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (b) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) searchBarLayout.getLayoutParams();
                layoutParams.rightMargin = dip2px(getContext(), 45);
                searchBarLayout.setLayoutParams(layoutParams);
                searchBarCancelBtn.setTranslationX(dip2px(getContext(), -45) / 2 - searchBarCancelBtn.getWidth() / 2);
                searchHistoryLayout.setVisibility(View.VISIBLE);
                scrollableNavigationBar.setVisibility(View.GONE);
                contentRecyclerView.setVisibility(View.GONE);

                sqLiteDBHelper=new SQLiteDBHelper(getContext(),SQLiteDBHelper.DATABASE_FILE_NAME,SQLiteDBHelper.DATABASE_VERSION);
                mSearchHistoryListData=new ArrayList<>();
                mHistoryDataAdapter=new SearchHistoryDataAdapter(getContext(),mSearchHistoryListData);
                searchHistoryRecyclerView.setAdapter(mHistoryDataAdapter);

                String sqlStr="select * from tb_search_history order by search_datetime desc";
                Cursor cursor=sqLiteDBHelper.getReadableDatabase().rawQuery(sqlStr,new String[]{});

                mSearchHistoryListData.clear();
                while (cursor.moveToNext()){
                    mSearchHistoryListData.add(cursor.getString(1));
                }

                if(mSearchHistoryListData.size()>0){
                    mSearchHistoryListData.add(getString(R.string.sort_clear_history_text));
                }

                mHistoryDataAdapter.notifyDataSetChanged();

            }
        }
    }

    class SearchBarEditTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() > 0) {
                searchBarClearBtn.setVisibility(View.VISIBLE);
            } else {
                searchBarClearBtn.setVisibility(View.GONE);
            }

            String sqlStr="select * from tb_search_history where search_content like '%"+charSequence.toString()+"%' order by search_datetime desc";
            Cursor cursor=sqLiteDBHelper.getReadableDatabase().rawQuery(sqlStr,new String[]{});

            mSearchHistoryListData.clear();
            while (cursor.moveToNext()){
                mSearchHistoryListData.add(cursor.getString(1));
            }

            if(mSearchHistoryListData.size()>0){
                mSearchHistoryListData.add(getString(R.string.sort_clear_history_text));
            }

            mHistoryDataAdapter.notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    class SearchBarEditActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_SEARCH ||
                    (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String sqlStr="insert or replace into tb_search_history values(null,?,?)";
                sqLiteDBHelper.getReadableDatabase().execSQL(sqlStr,new String[]{textView.getText().toString().trim(),simpleDateFormat.format(new Date())});

                Toast.makeText(getContext(),textView.getText().toString()+"被搜索",Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    class SearchBarClearBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            searchBarEdit.setText("");
        }
    }

    class SearchBarCancelBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            searchBarEdit.setText("");

            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean isOpen = imm.isActive();
            if (isOpen) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            searchBarEdit.clearFocus();

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) searchBarLayout.getLayoutParams();
            layoutParams.rightMargin = dip2px(getContext(), 10);
            searchBarLayout.setLayoutParams(layoutParams);
            searchBarCancelBtn.setTranslationX(0);
            searchHistoryLayout.setVisibility(View.GONE);
            scrollableNavigationBar.setVisibility(View.VISIBLE);
            contentRecyclerView.setVisibility(View.VISIBLE);

            if(sqLiteDBHelper!=null){
                sqLiteDBHelper.close();
            }

            if(mSearchHistoryListData!=null){
                mSearchHistoryListData=null;
            }
        }
    }

    class NavigationBarItemClickCallBack implements ScrollableNavigationBar.OutCallBack {
        @Override
        public void onTitleClick(String id) {
            UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(getContext(), id);
            updateAsyncTask.execute();

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

            String resultStr = DataUpdateUtil.updateData(sortDataUpdateMode);

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

}
