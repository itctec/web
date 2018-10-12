package itc.ink.explorefuture_android.app.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tencent.bugly.Bugly;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.app.utils.permission.DynamicPermission;
import itc.ink.explorefuture_android.find.FindFragment;
import itc.ink.explorefuture_android.mind.MindFragment;
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
            System.out.println("本页面已无权限需求限制！");
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


        boolean prepareDataFail = (DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR == null || DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR.trim().equals("")) ||
                (DataUpdateMode.RECOMMEND_ATTENTION_JSON_DATA_STR == null || DataUpdateMode.RECOMMEND_ATTENTION_JSON_DATA_STR.trim().equals("")) ||
                (DataUpdateMode.RECOMMEND_MIND_HOTTEST_JSON_DATA_STR == null || DataUpdateMode.RECOMMEND_MIND_HOTTEST_JSON_DATA_STR.trim().equals("")) ||
                (DataUpdateMode.RECOMMEND_MIND_NEWEST_JSON_DATA_STR == null || DataUpdateMode.RECOMMEND_MIND_NEWEST_JSON_DATA_STR.trim().equals(""));

        if (prepareDataFail) {
            //Update Data
            List<DataUpdateMode> dataUpdateList = new ArrayList<>();
            DataUpdateMode recommend_Handpick_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                    DataUpdateMode.RECOMMEND_HANDPICK_DATA_FILE_URL,
                    DataUpdateMode.RECOMMEND_HANDPICK_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                    DataUpdateMode.RECOMMEND_HANDPICK_LOCAL_DATA_FILE_NAME,
                    false);
            dataUpdateList.add(recommend_Handpick_Data_UpdateMode);
            DataUpdateMode recommend_Attention_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.ACCOUNT_UPDATE_DATETIME_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, ExploreFutureApplication.TEMP_ACCOUNT),
                    DataUpdateMode.ACCOUNT_ATTENTION_DATA_FILE_URL.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, ExploreFutureApplication.TEMP_ACCOUNT),
                    DataUpdateMode.ACCOUNT_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                    DataUpdateMode.RECOMMEND_ATTENTION_LOCAL_DATA_FILE_NAME,
                    false);
            dataUpdateList.add(recommend_Attention_Data_UpdateMode);
            DataUpdateMode recommend_Mind_Hottest_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                    DataUpdateMode.RECOMMEND_MIND_HOTTEST_DATA_FILE_URL,
                    DataUpdateMode.RECOMMEND_MIND_HOTTEST_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                    DataUpdateMode.RECOMMEND_MIND_HOTTEST_LOCAL_DATA_FILE_NAME,
                    false);
            dataUpdateList.add(recommend_Mind_Hottest_Data_UpdateMode);
            DataUpdateMode recommend_Mind_Newest_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                    DataUpdateMode.RECOMMEND_MIND_NEWEST_DATA_FILE_URL,
                    DataUpdateMode.RECOMMEND_MIND_NEWEST_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                    DataUpdateMode.RECOMMEND_MIND_NEWEST_LOCAL_DATA_FILE_NAME,
                    false);
            dataUpdateList.add(recommend_Mind_Newest_Data_UpdateMode);

            DataUpdateUtil dataUpdateUtil = new DataUpdateUtil(MainActivity.this, dataUpdateList, mHandler);
            dataUpdateUtil.updateData();
        } else {
            RecommendFragment recommendFragment = new RecommendFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, recommendFragment).commit();
            currentFragment = 0;
        }

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
                System.out.println("已动态获取所有权限");
            } else {
                System.out.println("以下权限获取失败：\n" + deniedPermissionList.toString());
            }
        }
    }

    //Update Test
    private void updateCheck() {
        if (obtainAllPermissionSuccess == false && deniedPermissionList.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            String dialogMsg = "APP升级需要申请访问SD卡权限，请前往设置授予相应权限";
            dynamicPermission.outService.showMissingPermissionDialog(MainActivity.this, dialogMsg);
        } else if (obtainAllPermissionSuccess == false && deniedPermissionList.contains(Manifest.permission.READ_PHONE_STATE)) {
            String dialogMsg = "APP升级需要读取手机状态权限，请前往设置授予相应权限";
            dynamicPermission.outService.showMissingPermissionDialog(MainActivity.this, dialogMsg);
        } else {
            //Tencent Bugly Self Update Check
            Bugly.init(MainActivity.this, TENCENT_BUGLY_APPID, false);
            System.out.println("执行升级检测");
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
            boolean prepareDataFail = (DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR == null || DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR.trim().equals("")) ||
                    (DataUpdateMode.RECOMMEND_ATTENTION_JSON_DATA_STR == null || DataUpdateMode.RECOMMEND_ATTENTION_JSON_DATA_STR.trim().equals("")) ||
                    (DataUpdateMode.RECOMMEND_MIND_HOTTEST_JSON_DATA_STR == null || DataUpdateMode.RECOMMEND_MIND_HOTTEST_JSON_DATA_STR.trim().equals("") ||
                            (DataUpdateMode.RECOMMEND_MIND_NEWEST_JSON_DATA_STR == null || DataUpdateMode.RECOMMEND_MIND_NEWEST_JSON_DATA_STR.trim().equals("")));

            if (prepareDataFail) {
                NoDataFragment noDataFragment = new NoDataFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, noDataFragment).commit();
            } else {
                RecommendFragment recommendFragment = new RecommendFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, recommendFragment).commit();
            }
            currentFragment = 0;
            updateBtnState(view);
        }
    }

    class NavigationSortBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            boolean prepareDataFail = (DataUpdateMode.SORT_JSON_DATA_STR == null || DataUpdateMode.SORT_JSON_DATA_STR.trim().equals(""));
            if (prepareDataFail) {
                NoDataFragment noDataFragment = new NoDataFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, noDataFragment).commit();
            } else {
                SortFragment sortFragment = new SortFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, sortFragment).commit();
            }
            currentFragment = 1;
            updateBtnState(view);
        }
    }

    class NavigationMindBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            boolean prepareDataFail = (DataUpdateMode.MIND_JSON_DATA_STR == null || DataUpdateMode.MIND_JSON_DATA_STR.trim().equals(""));
            if (prepareDataFail) {
                NoDataFragment noDataFragment = new NoDataFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, noDataFragment).commit();
            } else {
                MindFragment mindFragment = new MindFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, mindFragment).commit();
            }
            currentFragment = 2;
            updateBtnState(view);
        }
    }

    class NavigationFindBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            boolean prepareDataFail = (DataUpdateMode.FIND_JSON_DATA_STR == null || DataUpdateMode.FIND_JSON_DATA_STR.trim().equals(""));
            if (prepareDataFail) {
                NoDataFragment noDataFragment = new NoDataFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, noDataFragment).commit();
            } else {
                FindFragment findFragment = new FindFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, findFragment).commit();
            }
            currentFragment = 3;
            updateBtnState(view);
        }
    }

    class NavigationMineBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            boolean prepareDataFail = (DataUpdateMode.MINE_JSON_DATA_STR == null || DataUpdateMode.MINE_JSON_DATA_STR.trim().equals(""));
            if (prepareDataFail) {
                NoDataFragment noDataFragment = new NoDataFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, noDataFragment).commit();
            } else {
                MineFragment mineFragment = new MineFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, mineFragment).commit();
            }
            currentFragment = 4;
            updateBtnState(view);
        }
    }

    static class MyHandler extends Handler {
        WeakReference<MainActivity> mActivity;

        MyHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        public void handleMessage(Message msg) {
            MainActivity theActivity = mActivity.get();

            if (msg.what == DataUpdateUtil.UPDATE_DATA_FINISH_MSG) {
                Fragment fragment = null;
                boolean prepareDataFail = true;
                switch (currentFragment) {
                    case 0:
                        fragment = new RecommendFragment();
                        Log.d("ITC","1->"+DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR);
                        Log.d("ITC","2->"+DataUpdateMode.RECOMMEND_ATTENTION_JSON_DATA_STR);
                        Log.d("ITC","3->"+DataUpdateMode.RECOMMEND_MIND_HOTTEST_JSON_DATA_STR);
                        Log.d("ITC","4->"+DataUpdateMode.RECOMMEND_MIND_NEWEST_JSON_DATA_STR);

                        prepareDataFail = (DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR == null || DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR.trim().equals("")) ||
                                (DataUpdateMode.RECOMMEND_ATTENTION_JSON_DATA_STR == null || DataUpdateMode.RECOMMEND_ATTENTION_JSON_DATA_STR.trim().equals("")) ||
                                (DataUpdateMode.RECOMMEND_MIND_HOTTEST_JSON_DATA_STR == null || DataUpdateMode.RECOMMEND_MIND_HOTTEST_JSON_DATA_STR.trim().equals("")) ||
                                (DataUpdateMode.RECOMMEND_MIND_NEWEST_JSON_DATA_STR == null || DataUpdateMode.RECOMMEND_MIND_NEWEST_JSON_DATA_STR.trim().equals(""));
                        break;
                    case 1:
                        fragment = new SortFragment();
                        prepareDataFail = (DataUpdateMode.SORT_JSON_DATA_STR == null || DataUpdateMode.SORT_JSON_DATA_STR.trim().equals(""));
                        break;
                    case 2:
                        fragment = new MindFragment();
                        prepareDataFail = (DataUpdateMode.MIND_JSON_DATA_STR == null || DataUpdateMode.MIND_JSON_DATA_STR.trim().equals(""));
                        break;
                    case 3:
                        fragment = new FindFragment();
                        prepareDataFail = (DataUpdateMode.FIND_JSON_DATA_STR == null || DataUpdateMode.FIND_JSON_DATA_STR.trim().equals(""));
                        break;
                    case 4:
                        fragment = new MineFragment();
                        prepareDataFail = (DataUpdateMode.MINE_JSON_DATA_STR == null || DataUpdateMode.MINE_JSON_DATA_STR.trim().equals(""));
                        break;
                }


                if (prepareDataFail) {
                    fragment = new NoDataFragment();
                }

                theActivity.getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, fragment).commit();
            }
        }
    }

    ;


}
