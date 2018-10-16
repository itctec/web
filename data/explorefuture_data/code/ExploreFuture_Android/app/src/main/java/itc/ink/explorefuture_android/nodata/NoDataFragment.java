package itc.ink.explorefuture_android.nodata;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.activity.MainActivity;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;


/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class NoDataFragment extends Fragment {
    private Button refreshDataBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.nodata_fragment,container,false);
        refreshDataBtn=rootView.findViewById(R.id.app_Refresh_Data_Btn);
        refreshDataBtn.setOnClickListener(new RefreshDataBtnClickListener());

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    class RefreshDataBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            //Update Data
            List<DataUpdateMode> dataUpdateList=new ArrayList<>();
            DataUpdateMode recommend_Handpick_Data_UpdateMode=new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                    DataUpdateMode.RECOMMEND_HANDPICK_DATA_FILE_URL,
                    DataUpdateMode.RECOMMEND_HANDPICK_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                    DataUpdateMode.RECOMMEND_HANDPICK_LOCAL_DATA_FILE_NAME);
            dataUpdateList.add(recommend_Handpick_Data_UpdateMode);
            DataUpdateMode recommend_Attention_Data_UpdateMode=new DataUpdateMode(DataUpdateMode.ACCOUNT_UPDATE_DATETIME_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, ExploreFutureApplication.TEMP_ACCOUNT),
                    DataUpdateMode.ACCOUNT_ATTENTION_DATA_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE,ExploreFutureApplication.TEMP_ACCOUNT),
                    DataUpdateMode.ACCOUNT_ATTENTION_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                    DataUpdateMode.RECOMMEND_ATTENTION_LOCAL_DATA_FILE_NAME);
            dataUpdateList.add(recommend_Attention_Data_UpdateMode);
            DataUpdateMode recommend_Mind_Hottest_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                    DataUpdateMode.RECOMMEND_MIND_HOTTEST_DATA_FILE_URL,
                    DataUpdateMode.RECOMMEND_MIND_HOTTEST_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                    DataUpdateMode.RECOMMEND_MIND_HOTTEST_LOCAL_DATA_FILE_NAME);
            dataUpdateList.add(recommend_Mind_Hottest_Data_UpdateMode);
            DataUpdateMode recommend_Mind_Newest_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                    DataUpdateMode.RECOMMEND_MIND_NEWEST_DATA_FILE_URL,
                    DataUpdateMode.RECOMMEND_MIND_NEWEST_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                    DataUpdateMode.RECOMMEND_MIND_NEWEST_LOCAL_DATA_FILE_NAME);
            dataUpdateList.add(recommend_Mind_Newest_Data_UpdateMode);
            DataUpdateMode sort_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                    DataUpdateMode.SORT_ALL_DATA_FILE_URL,
                    DataUpdateMode.SORT_ALL_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                    DataUpdateMode.SORT_ALL_LOCAL_DATA_FILE_NAME);
            dataUpdateList.add(sort_Data_UpdateMode);
            DataUpdateMode mind_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.ACCOUNT_UPDATE_DATETIME_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, ExploreFutureApplication.TEMP_ACCOUNT),
                    DataUpdateMode.ACCOUNT_MIND_DATA_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, ExploreFutureApplication.TEMP_ACCOUNT),
                    DataUpdateMode.ACCOUNT_MIND_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                    DataUpdateMode.MIND_LOCAL_DATA_FILE_NAME);
            dataUpdateList.add(mind_Data_UpdateMode);

            DataUpdateUtil dataUpdateUtil=new DataUpdateUtil(getActivity(),dataUpdateList, MainActivity.mHandler);
            dataUpdateUtil.updateData();
        }
    }

}
