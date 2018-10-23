package itc.ink.explorefuture_android.app.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.tencent.bugly.Bugly;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.app.utils.permission.DynamicPermission;
import itc.ink.explorefuture_android.common_unit.common_dialog.CommonDialog;
import itc.ink.explorefuture_android.find.FindFragment;
import itc.ink.explorefuture_android.mind.MindFragment;
import itc.ink.explorefuture_android.mind.edit_activity.MindEditActivity;
import itc.ink.explorefuture_android.mine.MineFragment;
import itc.ink.explorefuture_android.nodata.NoDataFragment;
import itc.ink.explorefuture_android.recommend.recommend.RecommendFragment;
import itc.ink.explorefuture_android.sort.SortFragment;

public class MainActivity extends FragmentActivity {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "MainActivity";
    private final String TENCENT_BUGLY_APPID = "267157272d";

    public static MyHandler mHandler;

    private DynamicPermission dynamicPermission;
    private String[] permissionsNeeded = null;
    private final int PERMISSION_REQUEST_CODE = 0X001;
    private boolean obtainAllPermissionSuccess = false;
    private ArrayList<String> deniedPermissionList = new ArrayList<>();

    private TextView navigationRecommendBtn;
    private TextView navigationSortBtn;
    private TextView navigationMindBtn;
    private TextView navigationFindBtn;
    private TextView navigationMineBtn;

    private static int currentFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        mHandler = new MyHandler(this);

        //Dynamic Apply Permission
        permissionsNeeded = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
        dynamicPermission = new DynamicPermission();
        obtainAllPermissionSuccess = dynamicPermission.outService.requestPermissions(MainActivity.this, PERMISSION_REQUEST_CODE, permissionsNeeded);
        if (obtainAllPermissionSuccess) {
            Log.d(LOG_TAG, "本页面已无权限需求限制");
        }

        //Tencent Bugly Self Update Check
        updateCheck();

        setContentView(R.layout.app_main_activity);

        navigationRecommendBtn = findViewById(R.id.bottom_Navigation_Recommend_Btn);
        navigationRecommendBtn.setOnClickListener(new NavigationRecommendBtnClickListener());
        navigationSortBtn = findViewById(R.id.bottom_Navigation_Sort_Btn);
        navigationSortBtn.setOnClickListener(new NavigationSortBtnClickListener());
        navigationMindBtn = findViewById(R.id.bottom_Navigation_Mind_Btn);
        navigationMindBtn.setOnClickListener(new NavigationMindBtnClickListener());
        navigationFindBtn = findViewById(R.id.bottom_Navigation_Find_Btn);
        navigationFindBtn.setOnClickListener(new NavigationFindBtnClickListener());
        navigationMineBtn = findViewById(R.id.bottom_Navigation_Mine_Btn);
        navigationMineBtn.setOnClickListener(new NavigationMineBtnClickListener());

        boolean prepareDataSuccess = (new itc.ink.explorefuture_android.recommend.handpick_fragment.mode.DataLoad().outService.prepareData(MainActivity.this)) &&
                (new itc.ink.explorefuture_android.recommend.mind_fragment.mode.DataLoad().outService.prepareData(MainActivity.this));

        if (prepareDataSuccess) {
            RecommendFragment recommendFragment = new RecommendFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, recommendFragment).commit();
        } else {
            //Update Data
            List<DataUpdateMode> dataUpdateList = new ArrayList<>();
            DataUpdateMode recommend_Handpick_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                    DataUpdateMode.RECOMMEND_HANDPICK_DATA_FILE_URL,
                    DataUpdateMode.RECOMMEND_HANDPICK_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                    DataUpdateMode.RECOMMEND_HANDPICK_LOCAL_DATA_FILE_NAME);
            dataUpdateList.add(recommend_Handpick_Data_UpdateMode);
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

            DataUpdateUtil dataUpdateUtil = new DataUpdateUtil(MainActivity.this, dataUpdateList, mHandler);
            dataUpdateUtil.updateData();

            NoDataFragment noDataFragment = new NoDataFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, noDataFragment).commit();
        }
        currentFragment = 0;

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (dynamicPermission.outService.hasPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            deniedPermissionList.remove(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (dynamicPermission.outService.hasPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)) {
            deniedPermissionList.remove(Manifest.permission.READ_PHONE_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            deniedPermissionList.clear();
            obtainAllPermissionSuccess = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    deniedPermissionList.add(permissions[i]);
                    obtainAllPermissionSuccess = false;
                }
            }

            if (obtainAllPermissionSuccess) {
                Log.d(LOG_TAG, "已动态获取所有权限");
            } else {
                Log.d(LOG_TAG, "以下权限获取失败：\n" + deniedPermissionList.toString());
            }
        }
    }

    //Update Test
    private void updateCheck() {
        if (obtainAllPermissionSuccess == false && deniedPermissionList.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            dynamicPermission.outService.showMissingPermissionDialog(MainActivity.this, getString(R.string.app_update_need_access_sdcard_permission_text));
        } else if (obtainAllPermissionSuccess == false && deniedPermissionList.contains(Manifest.permission.READ_PHONE_STATE)) {
            dynamicPermission.outService.showMissingPermissionDialog(MainActivity.this, getString(R.string.app_update_need_access_phone_state_permission_text));
        } else {
            //Tencent Bugly Self Update Check
            Bugly.init(MainActivity.this, TENCENT_BUGLY_APPID, false);
            Log.d(LOG_TAG, "执行升级检测");
        }
    }

    private void updateBtnState(View currentActiveBtn) {
        navigationRecommendBtn.setAlpha(0.5F);
        navigationSortBtn.setAlpha(0.5F);
        navigationMindBtn.setAlpha(0.5F);
        navigationFindBtn.setAlpha(0.5F);
        navigationMineBtn.setAlpha(0.5F);

        currentActiveBtn.setAlpha(1F);
    }

    class NavigationRecommendBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            boolean prepareDataSuccess = (new itc.ink.explorefuture_android.recommend.handpick_fragment.mode.DataLoad().outService.prepareData(MainActivity.this)) &&
                    (new itc.ink.explorefuture_android.recommend.mind_fragment.mode.DataLoad().outService.prepareData(MainActivity.this));

            if (prepareDataSuccess) {
                RecommendFragment recommendFragment = new RecommendFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, recommendFragment).commit();
            } else {
                NoDataFragment noDataFragment = new NoDataFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, noDataFragment).commit();
            }
            currentFragment = 0;
            updateBtnState(view);
        }
    }

    class NavigationSortBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            boolean prepareDataSuccess=new itc.ink.explorefuture_android.sort.mode.DataLoad().outService.prepareData(MainActivity.this);
            if (prepareDataSuccess) {
                SortFragment sortFragment = new SortFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, sortFragment).commit();
            } else {
                NoDataFragment noDataFragment = new NoDataFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, noDataFragment).commit();
            }
            currentFragment = 1;
            updateBtnState(view);
        }
    }

    class NavigationMindBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            MindFragment mindFragment = new MindFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, mindFragment).commit();
            currentFragment = 2;
            updateBtnState(view);
        }
    }

    class NavigationFindBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            boolean prepareDataSuccess=new itc.ink.explorefuture_android.find.mode.DataLoad().outService.prepareData(MainActivity.this);
            if (prepareDataSuccess) {
                FindFragment findFragment = new FindFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, findFragment).commit();
            } else {
                NoDataFragment noDataFragment = new NoDataFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, noDataFragment).commit();
            }
            currentFragment = 3;
            updateBtnState(view);
        }
    }

    class NavigationMineBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            MineFragment mineFragment = new MineFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, mineFragment).commit();
            currentFragment = 4;
            updateBtnState(view);
        }
    }

    class MyHandler extends Handler {
        WeakReference<MainActivity> mActivity;

        MyHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        public void handleMessage(Message msg) {
            MainActivity theActivity = mActivity.get();

            if (msg.what == DataUpdateUtil.UPDATE_DATA_FINISH_MSG) {
                Fragment fragment = null;
                boolean prepareDataFail = true;
                boolean prepareDataSuccess = false;
                switch (currentFragment) {
                    case 0:
                        fragment = new RecommendFragment();
                        prepareDataSuccess = (new itc.ink.explorefuture_android.recommend.handpick_fragment.mode.DataLoad().outService.prepareData(MainActivity.this)) &&
                                (new itc.ink.explorefuture_android.recommend.mind_fragment.mode.DataLoad().outService.prepareData(MainActivity.this));

                        break;
                    case 1:
                        fragment = new SortFragment();
                        prepareDataSuccess=new itc.ink.explorefuture_android.sort.mode.DataLoad().outService.prepareData(MainActivity.this);
                        break;
                    case 2:
                        fragment = new MindFragment();
                        prepareDataSuccess = true;
                        break;
                    case 3:
                        fragment = new FindFragment();
                        prepareDataSuccess=new itc.ink.explorefuture_android.find.mode.DataLoad().outService.prepareData(MainActivity.this);
                        break;
                    case 4:
                        fragment = new MineFragment();
                        prepareDataSuccess = true;
                        break;
                }


                if (!prepareDataSuccess) {
                    fragment = new NoDataFragment();
                }

                theActivity.getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, fragment).commit();
            }
        }
    }

}
