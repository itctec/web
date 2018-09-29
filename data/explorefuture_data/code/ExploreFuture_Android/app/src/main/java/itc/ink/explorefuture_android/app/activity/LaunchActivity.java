package itc.ink.explorefuture_android.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.SharedPreferenceUtil;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;

/**
 * Created by yangwenjiang on 2018/9/25.
 */

public class LaunchActivity extends Activity {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "LaunchActivity";
    private final int MSG_FINISH_LAUNCHACTIVITY = 0x01;
    private final int LAUNCHACTIVITY_STAY_TIME = 1000;

    private boolean firstExec = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.app_launch_activity);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (firstExec) {
            long appAttachTime = SharedPreferenceUtil.getLong("application_attach_time");
            long diffTime = System.currentTimeMillis() - appAttachTime;
            mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHACTIVITY, LAUNCHACTIVITY_STAY_TIME - diffTime);
            firstExec = false;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_FINISH_LAUNCHACTIVITY) {
                boolean isFirstLaunch = SharedPreferenceUtil.getBoolean("app_first_launch", true);
                Intent intent;
                if (isFirstLaunch) {
                    //Start GuideActivity
                    intent = new Intent(LaunchActivity.this, GuideActivity.class);
                } else {
                    //Start MainActivity
                    intent = new Intent(LaunchActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }
    };
}
