package itc.ink.explorefuture_android.recommend.attention_fragment;

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
import itc.ink.explorefuture_android.recommend.attention_fragment.adapter.AttentionDataAdapter;
import itc.ink.explorefuture_android.recommend.attention_fragment.mode.DataLoad;
import itc.ink.explorefuture_android.recommend.attention_fragment.mode.mode_attention.AttentionListDataMode;
import itc.ink.explorefuture_android.recommend.attention_fragment.mode.mode_recommend.RecommendListDataMode;

/**
 * Created by yangwenjiang on 2018/9/20.
 */

public class AttentionFragment extends Fragment {
    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private AttentionDataAdapter mAttentionDataAdapter;

    private DataLoad mDataLoad;
    private ArrayList<RecommendListDataMode> mRecommendListData;
    private ArrayList<AttentionListDataMode> mAttentionListData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataLoad = new DataLoad(getActivity());
        //Judge Data Prepare
        if (mDataLoad.outService.prepareData(getActivity())) {
            mRecommendListData = (ArrayList<RecommendListDataMode>) mDataLoad.outService.loadRecommendData(getActivity());
            mAttentionListData = (ArrayList<AttentionListDataMode>) mDataLoad.outService.loadAttentionData(getActivity());
            //Judge Data Count
            if (mRecommendListData.size() >= 0 &&
                    mAttentionListData.size() >= 0) {
                mAttentionDataAdapter = new AttentionDataAdapter(getActivity(),mRecommendListData, mAttentionListData);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recommend_attention_fragment, container, false);
        contentRecyclerView = rootView.findViewById(R.id.recommend_Attention_RecyclerView);

        if (mAttentionDataAdapter != null) {
            contentRecyclerView.setAdapter(mAttentionDataAdapter);
            contentRvLayoutManager = new LinearLayoutManager(getActivity());
            contentRecyclerView.setLayoutManager(contentRvLayoutManager);
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
