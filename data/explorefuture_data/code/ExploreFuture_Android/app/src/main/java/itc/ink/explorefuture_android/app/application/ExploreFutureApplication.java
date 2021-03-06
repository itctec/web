package itc.ink.explorefuture_android.app.application;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.app.utils.SQLiteDBHelper;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.app.utils.SharedPreferenceUtil;
import itc.ink.explorefuture_android.login.LoginStateInstance;

/**
 * Created by yangwenjiang on 2018/9/25.
 */

public class ExploreFutureApplication extends Application {
    public static final String LOG_TAG = "ItcApp";
    private LoginStateInstance loginStateInstance;

    public static Context applicationContext;


    public static int screenWidth = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        //Init Login State Instance
        loginStateInstance=initLoginStateInstance();

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

    private LoginStateInstance initLoginStateInstance(){
        LoginStateInstance loginStateInstance=LoginStateInstance.getInstance();

        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_login_info where login_state=?";
        Cursor cursor = sqLiteDBHelper.getReadableDatabase().rawQuery(sqlStr, new String[]{LoginStateInstance.STATE_ONLINE});
        if (cursor.moveToNext()) {
            loginStateInstance.setId(cursor.getString(cursor.getColumnIndex("id")));
            loginStateInstance.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            loginStateInstance.setLogin_state(LoginStateInstance.STATE_ONLINE);
        }else{
            loginStateInstance.setId("");
            loginStateInstance.setPassword("");
            loginStateInstance.setLogin_state(LoginStateInstance.STATE_OFFLINE);
        }

        return loginStateInstance;
    }

    private void updateData() {
        List<DataUpdateMode> dataUpdateList = new ArrayList<>();
        DataUpdateMode recommend_Handpick_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                DataUpdateMode.RECOMMEND_HANDPICK_DATA_FILE_URL,
                DataUpdateMode.RECOMMEND_HANDPICK_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                DataUpdateMode.RECOMMEND_HANDPICK_LOCAL_DATA_FILE_NAME,
                "");
        dataUpdateList.add(recommend_Handpick_Data_UpdateMode);
        DataUpdateMode recommend_Mind_Hottest_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                DataUpdateMode.RECOMMEND_MIND_HOTTEST_DATA_FILE_URL,
                DataUpdateMode.RECOMMEND_MIND_HOTTEST_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                DataUpdateMode.RECOMMEND_MIND_HOTTEST_LOCAL_DATA_FILE_NAME,
                "");
        dataUpdateList.add(recommend_Mind_Hottest_Data_UpdateMode);
        DataUpdateMode recommend_Mind_Newest_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                DataUpdateMode.RECOMMEND_MIND_NEWEST_DATA_FILE_URL,
                DataUpdateMode.RECOMMEND_MIND_NEWEST_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                DataUpdateMode.RECOMMEND_MIND_NEWEST_LOCAL_DATA_FILE_NAME,
                "");
        dataUpdateList.add(recommend_Mind_Newest_Data_UpdateMode);
        DataUpdateMode sort_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                DataUpdateMode.SORT_ALL_DATA_FILE_URL,
                DataUpdateMode.SORT_ALL_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                DataUpdateMode.SORT_ALL_LOCAL_DATA_FILE_NAME,
                "");
        dataUpdateList.add(sort_Data_UpdateMode);
        DataUpdateMode find_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                DataUpdateMode.FIND_DATA_FILE_URL,
                DataUpdateMode.FIND_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                DataUpdateMode.FIND_LOCAL_DATA_FILE_NAME,
                "");
        dataUpdateList.add(find_Data_UpdateMode);


        if(loginStateInstance.getLogin_state().equals(LoginStateInstance.STATE_ONLINE)){
            DataUpdateMode recommend_Attention_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.ACCOUNT_UPDATE_DATETIME_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, loginStateInstance.getId()),
                    DataUpdateMode.ACCOUNT_ATTENTION_DATA_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, loginStateInstance.getId()),
                    DataUpdateMode.ACCOUNT_ATTENTION_DATA_NEWEST_UPDATE_DATE_TIME_KEY.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, loginStateInstance.getId()),
                    DataUpdateMode.RECOMMEND_ATTENTION_LOCAL_DATA_FILE_NAME,
                    loginStateInstance.getId());
            dataUpdateList.add(recommend_Attention_Data_UpdateMode);
            DataUpdateMode mind_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.ACCOUNT_UPDATE_DATETIME_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, loginStateInstance.getId()),
                    DataUpdateMode.ACCOUNT_MIND_DATA_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, loginStateInstance.getId()),
                    DataUpdateMode.ACCOUNT_MIND_DATA_NEWEST_UPDATE_DATE_TIME_KEY.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, loginStateInstance.getId()),
                    DataUpdateMode.MIND_LOCAL_DATA_FILE_NAME,
                    loginStateInstance.getId());
            dataUpdateList.add(mind_Data_UpdateMode);
            DataUpdateMode mine_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.ACCOUNT_UPDATE_DATETIME_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, loginStateInstance.getId()),
                    DataUpdateMode.ACCOUNT_MINE_DATA_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, loginStateInstance.getId()),
                    DataUpdateMode.ACCOUNT_MINE_DATA_NEWEST_UPDATE_DATE_TIME_KEY.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, loginStateInstance.getId()),
                    DataUpdateMode.MINE_LOCAL_DATA_FILE_NAME,
                    loginStateInstance.getId());
            dataUpdateList.add(mine_Data_UpdateMode);
        }

        DataUpdateUtil dataUpdateUtil = new DataUpdateUtil(this, dataUpdateList, null);
        dataUpdateUtil.updateData();
    }

}
