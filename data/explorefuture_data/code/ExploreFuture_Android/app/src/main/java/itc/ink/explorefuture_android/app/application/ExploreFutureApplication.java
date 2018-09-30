package itc.ink.explorefuture_android.app.application;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.app.utils.SharedPreferenceUtil;

/**
 * Created by yangwenjiang on 2018/9/25.
 */

public class ExploreFutureApplication extends Application {
    public static final String LOG_TAG = "ItcApp";
    public static final String TEMP_ACCOUNT="0000000001";

    public static Context applicationContext;

    public static int screenWidth = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        //Update Data
        updateData();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        applicationContext = this;

        SharedPreferenceUtil.putLong("application_attach_time", System.currentTimeMillis());

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
    }

    private void updateData(){
        List<DataUpdateMode> dataUpdateList=new ArrayList<>();
        DataUpdateMode recommend_Handpick_Data_UpdateMode=new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                DataUpdateMode.RECOMMEND_HANDPICK_DATA_FILE_URL,
                DataUpdateMode.RECOMMEND_HANDPICK_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                DataUpdateMode.RECOMMEND_HANDPICK_LOCAL_DATA_FILE_NAME,
                false);
        dataUpdateList.add(recommend_Handpick_Data_UpdateMode);
        DataUpdateMode recommend_Attention_Data_UpdateMode=new DataUpdateMode(DataUpdateMode.ACCOUNT_UPDATE_DATETIME_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE,TEMP_ACCOUNT),
                DataUpdateMode.ACCOUNT_ATTENTION_DATA_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE,TEMP_ACCOUNT),
                DataUpdateMode.ACCOUNT_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                DataUpdateMode.RECOMMEND_ATTENTION_LOCAL_DATA_FILE_NAME,
                false);
        dataUpdateList.add(recommend_Attention_Data_UpdateMode);

        DataUpdateUtil dataUpdateUtil=new DataUpdateUtil(this,dataUpdateList);
        dataUpdateUtil.updateData();
    }

}