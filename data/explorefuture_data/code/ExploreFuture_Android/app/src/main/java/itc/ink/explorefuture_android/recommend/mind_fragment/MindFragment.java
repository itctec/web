package itc.ink.explorefuture_android.recommend.mind_fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.recommend.attention_fragment.adapter.AttentionDataAdapter;
import itc.ink.explorefuture_android.recommend.attention_fragment.mode.mode_attention.AttentionListDataMode;
import itc.ink.explorefuture_android.recommend.attention_fragment.mode.mode_recommend.RecommendListDataMode;
import itc.ink.explorefuture_android.recommend.mind_fragment.mode.DataLoad;
import itc.ink.explorefuture_android.recommend.mind_fragment.mode.mode_mind.MindListDataMode;
import itc.ink.explorefuture_android.recommend.mind_fragment.mode.mode_topic.TopicListDataMode;

/**
 * Created by yangwenjiang on 2018/9/20.
 */

public class MindFragment extends Fragment {
    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;

    private DataLoad mDataLoad;
    private ArrayList<TopicListDataMode> mTopicListData;
    private ArrayList<MindListDataMode> mMindListData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataLoad = new DataLoad(getActivity());
        //Judge Data Prepare
        if (mDataLoad.outService.prepareData(getActivity())) {
            mTopicListData = (ArrayList<TopicListDataMode>) mDataLoad.outService.loadTopicData(getActivity());
            mMindListData = (ArrayList<MindListDataMode>) mDataLoad.outService.loadMindData(getActivity());
            //Judge Data Count
            if (mTopicListData.size() >= 0 &&
                    mMindListData.size() >= 0) {
                //mAttentionDataAdapter = new MindDataAdapter(getActivity(),mRecommendListData, mAttentionListData);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.recommend_mind_fragment,container,false);

        contentRecyclerView = rootView.findViewById(R.id.recommend_Mind_RecyclerView);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
