package itc.ink.explorefuture_android.find;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.utils.SQLiteDBHelper;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.adapter.MindDataAdapter;
import itc.ink.explorefuture_android.common_unit.search_bar.SearchBar;
import itc.ink.explorefuture_android.find.adapter.HotSearchListDataAdapter;
import itc.ink.explorefuture_android.find.mode.DataLoad;
import itc.ink.explorefuture_android.find.mode.mode_banner.BannerDataMode;
import itc.ink.explorefuture_android.sort.adapter.SearchHistoryDataAdapter;


/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class FindFragment extends Fragment {
    private SearchBar searchBar;
    private Banner findBanner;
    private TextView hotSearchTitle;
    private RecyclerView hotSearchRecyclerView;
    private HotSearchListDataAdapter mHotSearchDataAdapter;
    private ArrayList<String> mHotSearchListData;
    private TextView hotSearchMore;
    private TextView searchHistoryTitle;
    private RecyclerView searchHistoryRecyclerView;
    private ArrayList<String> mSearchHistoryListData=new ArrayList<>();
    private SearchHistoryDataAdapter mHistoryDataAdapter;
    private SQLiteDBHelper sqLiteDBHelper;

    private DataLoad mDataLoad;
    private ArrayList<BannerDataMode> mBannerListData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataLoad = new DataLoad();
        //Judge Data Prepare
        if (mDataLoad.outService.prepareData()) {
            mBannerListData = (ArrayList<BannerDataMode>) mDataLoad.outService.loadBannerData();
            mHotSearchListData = (ArrayList<String>) mDataLoad.outService.loadHotSearchData();

            if (mHotSearchListData != null && mHotSearchListData.size() >= 0) {
                mHotSearchDataAdapter = new HotSearchListDataAdapter(getContext(), mHotSearchListData);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.find_fragment, container, false);
        searchBar = rootView.findViewById(R.id.find_Top_Search_Bar);
        searchBar.setOutCallBack(new SearchBarCallBack());

        findBanner = rootView.findViewById(R.id.find_Banner);
        if (mBannerListData.size() >= 0) {
            findBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            findBanner.setImageLoader(new MyLoader());
            findBanner.setImages(mBannerListData);
            findBanner.setBannerAnimation(Transformer.Default);
            findBanner.isAutoPlay(true);
            findBanner.setIndicatorGravity(BannerConfig.CENTER).start();
            findBanner.setOnBannerListener(new FindBannerListener());
        }

        hotSearchTitle = rootView.findViewById(R.id.find_Hot_Search_Title);
        hotSearchRecyclerView = rootView.findViewById(R.id.find_Hot_Search_RecyclerView);
        if (mHotSearchDataAdapter != null) {
            hotSearchRecyclerView.setAdapter(mHotSearchDataAdapter);
            FlexboxLayoutManager hotSearchRvLayoutManager = new FlexboxLayoutManager();
            hotSearchRvLayoutManager.setFlexDirection(FlexDirection.ROW);
            hotSearchRvLayoutManager.setFlexWrap(FlexWrap.WRAP);
            hotSearchRvLayoutManager.setAlignItems(AlignItems.STRETCH);
            hotSearchRvLayoutManager.setJustifyContent(JustifyContent.SPACE_BETWEEN);
            hotSearchRecyclerView.setLayoutManager(hotSearchRvLayoutManager);
        }
        hotSearchMore = rootView.findViewById(R.id.find_Hot_Search_More_Btn);

        searchHistoryTitle = rootView.findViewById(R.id.find_Search_History_Title);
        searchHistoryRecyclerView= rootView.findViewById(R.id.find_search_History_RecyclerView);
        RecyclerView.LayoutManager historyRvLayoutManager = new LinearLayoutManager(getContext());
        searchHistoryRecyclerView.setLayoutManager(historyRvLayoutManager);
        initSearchHistoryData();
        mHistoryDataAdapter=new SearchHistoryDataAdapter(getContext(), mSearchHistoryListData);
        searchHistoryRecyclerView.setAdapter(mHistoryDataAdapter);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initSearchHistoryData(){

        sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_search_history order by search_datetime desc";
        Cursor cursor = sqLiteDBHelper.getReadableDatabase().rawQuery(sqlStr, new String[]{});
        mSearchHistoryListData.clear();
        while (cursor.moveToNext()) {
            mSearchHistoryListData.add(cursor.getString(1));
        }

        if (mSearchHistoryListData.size() > 0) {
            mSearchHistoryListData.add(getContext().getString(R.string.search_clear_history_text));
        }
    }

    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            BannerDataMode bannerData = (BannerDataMode) path;

            RequestOptions options = new RequestOptions()
                    .signature(new ObjectKeyCanNull(bannerData.getImage_update_datetime()).getObject());

            Glide.with(context).load(bannerData.getImageurl()).apply(options).into(imageView);
        }
    }

    class FindBannerListener implements OnBannerListener {
        @Override
        public void OnBannerClick(int position) {
            Toast.makeText(getContext(), "FindBanner" + position + "被点击", Toast.LENGTH_SHORT).show();
        }
    }

    class SearchBarCallBack implements SearchBar.OutCallBack {
        @Override
        public void onSearchFocusChange(boolean focused) {
            if (focused) {
                findBanner.setVisibility(View.GONE);
                hotSearchTitle.setVisibility(View.GONE);
                hotSearchRecyclerView.setVisibility(View.GONE);
                hotSearchMore.setVisibility(View.GONE);
                searchHistoryTitle.setVisibility(View.GONE);
                searchHistoryRecyclerView.setVisibility(View.GONE);
            } else {
                findBanner.setVisibility(View.VISIBLE);
                hotSearchTitle.setVisibility(View.VISIBLE);
                hotSearchRecyclerView.setVisibility(View.VISIBLE);
                hotSearchMore.setVisibility(View.VISIBLE);
                searchHistoryTitle.setVisibility(View.VISIBLE);
                searchHistoryRecyclerView.setVisibility(View.VISIBLE);
                initSearchHistoryData();
                mHistoryDataAdapter.notifyDataSetChanged();
            }
        }
    }
}
