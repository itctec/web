package itc.ink.explorefuture_android.login;

import android.app.Activity;
import android.content.Context;
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
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;

/**
 * Created by yangwenjiang on 2018/10/23.
 */

public class LoginActivity extends Activity {
    public static MyHandler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new MyHandler(this);

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
                LoginStateInstance loginStateInstance=LoginStateInstance.getInstance();
                loginStateInstance.setId("0000000001");
                loginStateInstance.setPassword("123456");
                loginStateInstance.setLogin_state(LoginStateInstance.STATE_ONLINE);

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

    class MyHandler extends Handler {
        WeakReference<LoginActivity> mActivity;

        MyHandler(LoginActivity activity) {
            mActivity = new WeakReference<LoginActivity>(activity);
        }

        public void handleMessage(Message msg) {
            LoginActivity theActivity = mActivity.get();

            if (msg.what == DataUpdateUtil.UPDATE_DATA_FINISH_MSG) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
