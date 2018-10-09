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

    public static Handler mHandler;

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

    private int currentFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        initHandler();

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

        boolean prepareDataOk = dataCheck(DataUpdateMode.RECOMMEND_HANDPICK_LOCAL_DATA_FILE_NAME) &&
                dataCheck(DataUpdateMode.RECOMMEND_ATTENTION_LOCAL_DATA_FILE_NAME) &&
                dataCheck(DataUpdateMode.RECOMMEND_MIND_LOCAL_DATA_FILE_NAME);
        if (prepareDataOk) {
            RecommendFragment recommendFragment = new RecommendFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, recommendFragment).commit();
            currentFragment = 0;
        } else {
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
            DataUpdateMode recommend_Mind_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                    DataUpdateMode.RECOMMEND_MIND_DATA_FILE_URL,
                    DataUpdateMode.RECOMMEND_MIND_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                    DataUpdateMode.RECOMMEND_MIND_LOCAL_DATA_FILE_NAME,
                    false);
            dataUpdateList.add(recommend_Mind_Data_UpdateMode);

            DataUpdateUtil dataUpdateUtil = new DataUpdateUtil(MainActivity.this, dataUpdateList, mHandler);
            dataUpdateUtil.updateData();
        }

    }

    private void initHandler(){
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == DataUpdateUtil.UPDATE_DATA_FINISH_MSG) {
                    Log.d(LOG_TAG, "UPDATE_DATA_FINISH_MSG");
                    Fragment fragment = null;
                    String dataFileName;
                    boolean prepareDataOk = false;
                    switch (currentFragment) {
                        case 0:
                            fragment = new RecommendFragment();
                            prepareDataOk = dataCheck(DataUpdateMode.RECOMMEND_HANDPICK_LOCAL_DATA_FILE_NAME) &&
                                    dataCheck(DataUpdateMode.RECOMMEND_ATTENTION_LOCAL_DATA_FILE_NAME) &&
                                    dataCheck(DataUpdateMode.RECOMMEND_MIND_LOCAL_DATA_FILE_NAME);
                            break;
                        case 1:
                            fragment = new SortFragment();
                            dataFileName = DataUpdateMode.SORT_LOCAL_DATA_FILE_NAME;
                            prepareDataOk = dataCheck(dataFileName);
                            break;
                        case 2:
                            fragment = new MindFragment();
                            dataFileName = DataUpdateMode.MIND_LOCAL_DATA_FILE_NAME;
                            prepareDataOk = dataCheck(dataFileName);
                            break;
                        case 3:
                            fragment = new FindFragment();
                            dataFileName = DataUpdateMode.FIND_LOCAL_DATA_FILE_NAME;
                            prepareDataOk = dataCheck(dataFileName);
                            break;
                        case 4:
                            fragment = new MineFragment();
                            dataFileName = DataUpdateMode.MINE_LOCAL_DATA_FILE_NAME;
                            prepareDataOk = dataCheck(dataFileName);
                            break;
                    }

                    if (prepareDataOk == false) {
                        fragment = new NoDataFragment();

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, fragment).commit();
                }
            }
        };
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

    //Data Check
    private boolean dataCheck(String fileName) {
        File dataFile = new File(getFilesDir(), fileName);
        if (dataFile.exists()) {
            return true;
        }
        return false;
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
            boolean prepareDataOk = dataCheck(DataUpdateMode.RECOMMEND_HANDPICK_LOCAL_DATA_FILE_NAME);
            if (prepareDataOk) {
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
            boolean prepareDataOk = dataCheck(DataUpdateMode.SORT_LOCAL_DATA_FILE_NAME);
            if (prepareDataOk) {
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
            boolean prepareDataOk = dataCheck(DataUpdateMode.MIND_LOCAL_DATA_FILE_NAME);
            if (prepareDataOk) {
                MindFragment mindFragment = new MindFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, mindFragment).commit();
            } else {
                NoDataFragment noDataFragment = new NoDataFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, noDataFragment).commit();
            }
            currentFragment = 2;
            updateBtnState(view);
        }
    }

    class NavigationFindBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            boolean prepareDataOk = dataCheck(DataUpdateMode.FIND_LOCAL_DATA_FILE_NAME);
            if (prepareDataOk) {
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
            boolean prepareDataOk = dataCheck(DataUpdateMode.MINE_LOCAL_DATA_FILE_NAME);
            if (prepareDataOk) {
                MineFragment mineFragment = new MineFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, mineFragment).commit();
            } else {
                NoDataFragment noDataFragment = new NoDataFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.app_Fragment_Container, noDataFragment).commit();
            }
            currentFragment = 4;
            updateBtnState(view);
        }
    }


}
