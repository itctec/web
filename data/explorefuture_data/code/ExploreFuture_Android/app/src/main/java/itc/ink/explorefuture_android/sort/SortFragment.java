package itc.ink.explorefuture_android.sort;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.activity.MainActivity;
import itc.ink.explorefuture_android.common_unit.scrollar_navigation.ScrollableNavigationBar;
import itc.ink.explorefuture_android.recommend.attention_fragment.adapter.AttentionWrapAdapter;
import itc.ink.explorefuture_android.sort.adapter.SortDataAdapter;
import itc.ink.explorefuture_android.sort.mode.DataLoad;
import itc.ink.explorefuture_android.sort.mode.mode_navigation.NavigationBarDataMode;
import itc.ink.explorefuture_android.sort.mode.mode_sort.SortListDataMode;


/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class SortFragment extends Fragment {
    private ScrollableNavigationBar scrollableNavigationBar;
    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private SortDataAdapter mSortDataAdapter;

    private DataLoad mDataLoad;
    private ArrayList<SortListDataMode> mSortListData;
    private ArrayList<NavigationBarDataMode> mSortTitleListData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataLoad = new DataLoad();

        //Judge Data Prepare
        if (mDataLoad.outService.prepareData()) {
            mSortListData = (ArrayList<SortListDataMode>) mDataLoad.outService.loadSortData();
            if (mSortListData.size() >= 0) {
                mSortDataAdapter = new SortDataAdapter(getActivity(),mSortListData);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sort_fragment, container, false);

        scrollableNavigationBar = rootView.findViewById(R.id.sort_Top_Navigation_Bar);
        //Judge Data Count
        if (mSortListData.size() >= 0) {
            mSortTitleListData = getSortTitleListData(mSortListData);
            scrollableNavigationBar.setDataAndCallBack(getActivity(), mSortTitleListData, new NavigationBarItemClickCallBack());
        }

        contentRecyclerView = rootView.findViewById(R.id.sort_RecyclerView);

        if (mSortDataAdapter != null) {
            contentRecyclerView.setAdapter(mSortDataAdapter);
            contentRvLayoutManager = new LinearLayoutManager(getActivity());
            contentRecyclerView.setLayoutManager(contentRvLayoutManager);
        }

        return rootView;
    }

    private ArrayList<NavigationBarDataMode> getSortTitleListData(ArrayList<SortListDataMode> sortDataArray) {
        ArrayList<NavigationBarDataMode> sortTitleListData = new ArrayList<>();
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
        public void onTitleClick(String titleStr) {
            Toast.makeText(getActivity(), titleStr + "被点击", Toast.LENGTH_SHORT).show();
        }
    }
}
