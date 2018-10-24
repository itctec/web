package itc.ink.explorefuture_android.login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.utils.SQLiteDBHelper;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;

/**
 * Created by yangwenjiang on 2018/10/23.
 */

public class LoginActivity extends Activity {
    private MyHandler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new MyHandler();

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.login_activity);

        Button loginBtn=findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Login
                String id="0000000001";
                String password="123456";
                LoginStateInstance loginStateInstance=LoginStateInstance.getInstance();
                loginStateInstance.setId(id);
                loginStateInstance.setPassword(password);
                loginStateInstance.setLogin_state(LoginStateInstance.STATE_ONLINE);

                updateDatabase(id,password,LoginStateInstance.STATE_ONLINE);

                //Update Data
                List<DataUpdateMode> dataUpdateList=new ArrayList<>();
                DataUpdateMode recommend_Attention_Data_UpdateMode=new DataUpdateMode(DataUpdateMode.ACCOUNT_UPDATE_DATETIME_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, loginStateInstance.getId()),
                        DataUpdateMode.ACCOUNT_ATTENTION_DATA_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE,loginStateInstance.getId()),
                        DataUpdateMode.ACCOUNT_ATTENTION_DATA_NEWEST_UPDATE_DATE_TIME_KEY.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE,loginStateInstance.getId()),
                        DataUpdateMode.RECOMMEND_ATTENTION_LOCAL_DATA_FILE_NAME,
                        loginStateInstance.getId());
                dataUpdateList.add(recommend_Attention_Data_UpdateMode);
                DataUpdateMode mind_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.ACCOUNT_UPDATE_DATETIME_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, loginStateInstance.getId()),
                        DataUpdateMode.ACCOUNT_MIND_DATA_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, loginStateInstance.getId()),
                        DataUpdateMode.ACCOUNT_MIND_DATA_NEWEST_UPDATE_DATE_TIME_KEY.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE,loginStateInstance.getId()),
                        DataUpdateMode.MIND_LOCAL_DATA_FILE_NAME,
                        loginStateInstance.getId());
                dataUpdateList.add(mind_Data_UpdateMode);
                DataUpdateMode mine_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.ACCOUNT_UPDATE_DATETIME_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, loginStateInstance.getId()),
                        DataUpdateMode.ACCOUNT_MINE_DATA_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, loginStateInstance.getId()),
                        DataUpdateMode.ACCOUNT_MINE_DATA_NEWEST_UPDATE_DATE_TIME_KEY.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE,loginStateInstance.getId()),
                        DataUpdateMode.MINE_LOCAL_DATA_FILE_NAME,
                        loginStateInstance.getId());
                dataUpdateList.add(mine_Data_UpdateMode);
                DataUpdateUtil dataUpdateUtil=new DataUpdateUtil(LoginActivity.this,dataUpdateList, mHandler);
                dataUpdateUtil.updateData();
            }
        });
    }

    private void updateDatabase(String id,String password,String login_state){
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(LoginActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_login_info where id=?";
        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, new String[]{id});

        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        contentValues.put("login_state", login_state);

        if (cursor.moveToNext()) {
            sqLiteDatabase.update("tb_login_info", contentValues, "id=?", new String[]{id});
        } else {
            contentValues.put("id", id);
            sqLiteDatabase.insert("tb_login_info", null, contentValues);
        }
        sqLiteDatabase.close();
        sqLiteDBHelper.close();
    }

    class MyHandler extends Handler {

        public void handleMessage(Message msg) {

            if (msg.what == DataUpdateUtil.UPDATE_DATA_FINISH_MSG) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
