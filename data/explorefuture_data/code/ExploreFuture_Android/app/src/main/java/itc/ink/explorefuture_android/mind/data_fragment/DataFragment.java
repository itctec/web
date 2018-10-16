package itc.ink.explorefuture_android.mind.data_fragment;

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
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.adapter.MindDataAdapter;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.mind.data_fragment.mode.DataLoad;
import itc.ink.explorefuture_android.recommend.attention_fragment.adapter.AttentionWrapAdapter;
import itc.ink.explorefuture_android.recommend.attention_fragment.mode.mode_recommend.RecommendListDataMode;


/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class DataFragment extends Fragment {
    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private MindDataAdapter mMindDataAdapter;

    private DataLoad mDataLoad;
    private ArrayList<MindListDataMode> mMindListData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataLoad = new DataLoad();
        //Judge Data Prepare
        if (mDataLoad.outService.prepareData()) {
            mMindListData = (ArrayList<MindListDataMode>) mDataLoad.outService.loadMindData();
            //Judge Data Count
            if (mMindListData.size() >= 0) {
                mMindDataAdapter=new MindDataAdapter(getActivity(),mMindListData);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.mind_data_fragment,container,false);

        contentRecyclerView = rootView.findViewById(R.id.mind_Data_RecyclerView);
        if (mMindDataAdapter != null) {
            contentRecyclerView.setAdapter(mMindDataAdapter);
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
