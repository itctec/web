package itc.ink.explorefuture_android.recommend.handpick_fragment;

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
import itc.ink.explorefuture_android.common_unit.content_list.mode.ContentListDataMode;
import itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.HandPickWrapperAdapter;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.DataLoad;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_action.ActionListDataModel;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_action.ActionSubjectDataMode;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_banner.BannerDataMode;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_interest.InterestDataModel;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_product.ProductDataMode;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_solution.SolutionDataMode;

/**
 * Created by yangwenjiang on 2018/9/20.
 */

public class HandpickFragment extends Fragment {
    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private HandPickWrapperAdapter contentRvWrapperAdapter;

    private DataLoad mDataLoad;
    private ArrayList<BannerDataMode> mBannerData=new ArrayList();
    private ArrayList<SolutionDataMode> mSolutionData;
    private ArrayList<ActionSubjectDataMode> mActionSubjectData;
    private ArrayList<ActionListDataModel> mActionListData;
    private ArrayList<ProductDataMode> mProductData;
    private ArrayList<ContentListDataMode> mInterestListData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataLoad = new DataLoad();
        //Judge Data Prepare
        if(mDataLoad.outService.prepareData(getContext())){
            mBannerData = (ArrayList<BannerDataMode>) mDataLoad.outService.loadBannerData();
            mSolutionData = (ArrayList<SolutionDataMode>) mDataLoad.outService.loadSolutionData();
            mActionSubjectData = (ArrayList<ActionSubjectDataMode>) mDataLoad.outService.loadActionSubjectData();
            mActionListData = (ArrayList<ActionListDataModel>) mDataLoad.outService.loadActionListData();
            mProductData = (ArrayList<ProductDataMode>) mDataLoad.outService.loadProductData();
            mInterestListData = (ArrayList<ContentListDataMode>) mDataLoad.outService.loadInterestData();
            //Judge Data Count
            if(mBannerData.size()>=1&&
                    mSolutionData.size()>=12&&
                    mActionSubjectData.size()>=1
                    &&mActionListData.size()>=0
                    &&mProductData.size()>=9
                    &&mInterestListData.size()>=0){
                contentRvWrapperAdapter = new HandPickWrapperAdapter(getActivity(), mBannerData, mSolutionData, mActionSubjectData, mActionListData, mProductData, mInterestListData);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.recommend_handpick_fragment,container,false);
        contentRecyclerView = rootView.findViewById(R.id.recommend_Handpick_RecyclerView);
        if(contentRvWrapperAdapter!=null){
            contentRecyclerView.setAdapter(contentRvWrapperAdapter);
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
