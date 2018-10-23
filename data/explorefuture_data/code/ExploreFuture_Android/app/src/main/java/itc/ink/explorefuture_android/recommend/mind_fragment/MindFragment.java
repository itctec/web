package itc.ink.explorefuture_android.recommend.mind_fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.SharedPreferenceUtil;
import itc.ink.explorefuture_android.recommend.mind_fragment.adapter.MindWrapAdapter;
import itc.ink.explorefuture_android.recommend.mind_fragment.mode.DataLoad;
import itc.ink.explorefuture_android.recommend.mind_fragment.mode.mode_topic.TopicListDataMode;

/**
 * Created by yangwenjiang on 2018/9/20.
 */

public class MindFragment extends Fragment {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "MindFragment";
    private MindWrapAdapter mMindDataAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataLoad mDataLoad = new DataLoad();
        //Judge Data Prepare
        if (mDataLoad.outService.prepareData(getContext())) {
            ArrayList<TopicListDataMode> mTopicListData = (ArrayList<TopicListDataMode>) mDataLoad.outService.loadTopicData();
            ArrayList<MindListDataMode> mMindListData=null;
            if(SharedPreferenceUtil.getString(MindWrapAdapter.RECOMMEND_MIND_TAB_KEY).equals(MindWrapAdapter.RECOMMEND_MIND_VALUE_NEWEST)){
                mMindListData = (ArrayList<MindListDataMode>) mDataLoad.outService.loadMindNewestData();
            }else {
                mMindListData = (ArrayList<MindListDataMode>) mDataLoad.outService.loadMindHottestData();
            }
            //Judge Data Count
            if (mTopicListData.size() >= 0 &&
                    mMindListData.size() >= 0) {
                mMindDataAdapter = new MindWrapAdapter(getActivity(),mTopicListData, mMindListData);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.recommend_mind_fragment,container,false);

        RecyclerView contentRecyclerView = rootView.findViewById(R.id.recommend_Mind_RecyclerView);

        if (mMindDataAdapter != null) {
            contentRecyclerView.setAdapter(mMindDataAdapter);
            RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(getActivity());
            contentRecyclerView.setLayoutManager(contentRvLayoutManager);
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
