package itc.ink.explorefuture_android.recommend.attention_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.login.LoginActivity;
import itc.ink.explorefuture_android.login.LoginStateInstance;
import itc.ink.explorefuture_android.mind.data_fragment.DataFragment;
import itc.ink.explorefuture_android.mind.nodata_fragment.MindNoDataFragment;
import itc.ink.explorefuture_android.recommend.attention_fragment.adapter.AttentionWrapAdapter;
import itc.ink.explorefuture_android.recommend.attention_fragment.mode.DataLoad;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.recommend.attention_fragment.mode.mode_recommend.RecommendListDataMode;

/**
 * Created by yangwenjiang on 2018/9/20.
 */

public class AttentionFragment extends Fragment {
    private final int LOGIN_REQUEST_CODE = 0x01;
    private LoginStateInstance loginStateInstance;
    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private AttentionWrapAdapter mAttentionDataAdapter;

    private DataLoad mDataLoad;
    private ArrayList<RecommendListDataMode> mRecommendListData;
    private ArrayList<MindListDataMode> mMindListData;

    private View mindOffLineLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginStateInstance = LoginStateInstance.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recommend_attention_fragment, container, false);
        contentRecyclerView = rootView.findViewById(R.id.recommend_Attention_RecyclerView);
        mindOffLineLayout=rootView.findViewById(R.id.recommend_Attention_OffLine_Layout);
        Button loginBtn = rootView.findViewById(R.id.app_Login_Btn);
        loginBtn.setOnClickListener(new LoginBtnClickListener());

        updateLayout();

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            updateLayout();
        }
    }

    private void updateLayout() {
        if (loginStateInstance.getLogin_state().equals(LoginStateInstance.STATE_ONLINE)) {
            mindOffLineLayout.setVisibility(View.GONE);
            contentRecyclerView.setVisibility(View.VISIBLE);

            mDataLoad = new DataLoad();
            //Judge Data Prepare
            if (mDataLoad.outService.prepareData(getContext())) {
                mRecommendListData = (ArrayList<RecommendListDataMode>) mDataLoad.outService.loadRecommendData();
                mMindListData = (ArrayList<MindListDataMode>) mDataLoad.outService.loadAttentionData();
                //Judge Data Count
                if (mRecommendListData.size() >= 0 &&
                        mMindListData.size() >= 0) {
                    mAttentionDataAdapter = new AttentionWrapAdapter(getActivity(), mRecommendListData, mMindListData);
                    contentRecyclerView.setAdapter(mAttentionDataAdapter);
                    contentRvLayoutManager = new LinearLayoutManager(getActivity());
                    contentRecyclerView.setLayoutManager(contentRvLayoutManager);
                }
            }
        } else {
            //Show OffLine layout
            contentRecyclerView.setVisibility(View.GONE);
            mindOffLineLayout.setVisibility(View.VISIBLE);
        }
    }

    class LoginBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }
    }

}
