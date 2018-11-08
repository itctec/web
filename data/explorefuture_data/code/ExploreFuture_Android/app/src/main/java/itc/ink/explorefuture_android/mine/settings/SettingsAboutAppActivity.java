package itc.ink.explorefuture_android.mine.settings;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.SQLiteDBHelper;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.login.LoginStateInstance;

/**
 * Created by yangwenjiang on 2018/10/29.
 */

public class SettingsAboutAppActivity extends Activity {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "SettingsActivity";
    private ImageView backBtn;
    private ConstraintLayout copyrightLayout;
    private ConstraintLayout serviceAgreementLayout;
    private ConstraintLayout privacyPolicyLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.mine_settings_about_app_activity);

        backBtn=findViewById(R.id.settings_About_App_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(new BackBtnClickListener());

        ItemLayoutClickListener itemLayoutClickListener=new ItemLayoutClickListener();
        copyrightLayout=findViewById(R.id.settings_About_App_Copyright_Layout);
        copyrightLayout.setOnClickListener(itemLayoutClickListener);
        serviceAgreementLayout=findViewById(R.id.settings_About_App_Service_Agreement_Layout);
        serviceAgreementLayout.setOnClickListener(itemLayoutClickListener);
        privacyPolicyLayout=findViewById(R.id.settings_About_App_Privacy_Policy_Layout);
        privacyPolicyLayout.setOnClickListener(itemLayoutClickListener);
    }

    class BackBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    class ItemLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.settings_About_App_Copyright_Layout:
                    Toast.makeText(SettingsAboutAppActivity.this,"版权信息被点击",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.settings_About_App_Service_Agreement_Layout:
                    Toast.makeText(SettingsAboutAppActivity.this,"服务条款被点击",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.settings_About_App_Privacy_Policy_Layout:
                    Toast.makeText(SettingsAboutAppActivity.this,"隐私政策被点击",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
