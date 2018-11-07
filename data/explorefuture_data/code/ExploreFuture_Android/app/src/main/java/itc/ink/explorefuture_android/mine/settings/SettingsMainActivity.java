package itc.ink.explorefuture_android.mine.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.GlideCircleWithBorder;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.GlideCacheUtil;
import itc.ink.explorefuture_android.app.utils.SQLiteDBHelper;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.common_unit.common_dialog.CommonDialog;
import itc.ink.explorefuture_android.login.LoginStateInstance;
import itc.ink.explorefuture_android.mind.edit_activity.MindEditActivity;

/**
 * Created by yangwenjiang on 2018/10/29.
 */

public class SettingsMainActivity extends Activity {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "SettingsActivity";
    private ImageView backBtn;
    private ConstraintLayout personInfoLayout;
    private ImageView headPortraitImage;
    private TextView nicknameText;
    private TextView idText;
    private ConstraintLayout receiveAddressLayout;
    private ConstraintLayout realNameAuthenticationLayout;
    private ConstraintLayout accountSafeLayout;
    private ConstraintLayout paySettingsLayout;
    private ConstraintLayout bindAccountLayout;
    private ConstraintLayout cleanCatchLayout;
    private TextView catchText;
    private ConstraintLayout helpCenterLayout;
    private ConstraintLayout aboutLayout;
    private TextView logoutBtn;
    private GlideCacheUtil glideCacheUtil=new GlideCacheUtil();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.mine_settings_main_activity);

        backBtn=findViewById(R.id.settings_Main_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(new BackBtnClickListener());
        personInfoLayout=findViewById(R.id.settings_Main_Person_Info_Layout);
        personInfoLayout.setOnClickListener(new PersonInfoLayoutClickListener());
        headPortraitImage=findViewById(R.id.settings_Main_HeadPortrait_Image);
        nicknameText=findViewById(R.id.settings_Main_Nickname_Text);
        idText=findViewById(R.id.settings_Main_ID_Text);
        receiveAddressLayout=findViewById(R.id.settings_Main_Receive_Address_Layout);
        receiveAddressLayout.setOnClickListener(new ReceiveAddressLayoutClickListener());
        realNameAuthenticationLayout=findViewById(R.id.settings_Main_Real_Name_Authentication_Layout);
        realNameAuthenticationLayout.setOnClickListener(new RealNameAuthenticationLayoutClickListener());
        accountSafeLayout=findViewById(R.id.settings_Main_Account_Safe_Layout);
        accountSafeLayout.setOnClickListener(new AccountSafeLayoutClickListener());
        paySettingsLayout=findViewById(R.id.settings_Main_Pay_Settings_Layout);
        paySettingsLayout.setOnClickListener(new PaySettingsLayoutClickListener());
        bindAccountLayout=findViewById(R.id.settings_Main_Bind_Account_Layout);
        bindAccountLayout.setOnClickListener(new BindAccountLayoutClickListener());
        cleanCatchLayout=findViewById(R.id.settings_Main_Clean_Catch_Layout);
        cleanCatchLayout.setOnClickListener(new CleanCatchLayoutClickListener());
        catchText=findViewById(R.id.settings_Main_Catch_Text);
        catchText.setText(glideCacheUtil.getCacheSize(SettingsMainActivity.this));
        helpCenterLayout=findViewById(R.id.settings_Main_Help_Center_Layout);
        helpCenterLayout.setOnClickListener(new HelpCenterLayoutClickListener());
        aboutLayout=findViewById(R.id.settings_Main_About_Layout);
        aboutLayout.setOnClickListener(new AboutLayoutClickListener());
        logoutBtn=findViewById(R.id.settings_Main_Logout_Btn);
        logoutBtn.setOnClickListener(new LogoutBtnClickListener());

        updateView();
    }

    private void updateView(){
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(SettingsMainActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_person_info where id=?";
        Cursor cursor = sqLiteDBHelper.getReadableDatabase().rawQuery(sqlStr, new String[]{LoginStateInstance.getInstance().getId()});
        if (cursor.moveToNext()) {
            RequestOptions options = new RequestOptions();
            options.signature(new ObjectKeyCanNull(cursor.getString(cursor.getColumnIndex("head_portrait_image_update_datetime"))).getObject());
            options.circleCrop();
            Glide.with(SettingsMainActivity.this).load(cursor.getString(cursor.getColumnIndex("head_portrait_image_url"))).apply(options).into(headPortraitImage);

            nicknameText.setText(cursor.getString(cursor.getColumnIndex("nickname")));
            idText.setText(cursor.getString(cursor.getColumnIndex("id")));
        } else {
            Log.d(LOG_TAG, "加载数据失败");
        }
        sqLiteDBHelper.close();
    }

    class BackBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    class PersonInfoLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(SettingsMainActivity.this,SettingsUserInfoActivity.class);
            startActivity(intent);
        }
    }

    class ReceiveAddressLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingsMainActivity.this,"收货地址被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class RealNameAuthenticationLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingsMainActivity.this,"实名认证被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class AccountSafeLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingsMainActivity.this,"账户安全被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class PaySettingsLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingsMainActivity.this,"支付设置被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class BindAccountLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingsMainActivity.this,"绑定账户被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class CleanCatchLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            new CommonDialog(SettingsMainActivity.this, getString(R.string.settings_main_clean_catch_dialog_content_text), new CommonDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if (confirm) {
                        cleanGlideCatch();
                    }
                    dialog.dismiss();
                }
            }).setTitle(getString(R.string.dialog_title_tip_text))
                    .setNegativeButton(getString(R.string.dialog_negative_btn_text))
                    .setPositiveButton(getString(R.string.dialog_positive_btn_text))
                    .show();
        }

        private void cleanGlideCatch() {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    Glide.get(SettingsMainActivity.this).clearDiskCache();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            catchText.setText(glideCacheUtil.getCacheSize(SettingsMainActivity.this));
                        }
                    });
                }
            }.start();
        }
    }

    class HelpCenterLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingsMainActivity.this,"帮助中心被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class AboutLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingsMainActivity.this,"关于APP被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class LogoutBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            LoginStateInstance loginStateInstance=LoginStateInstance.getInstance();
            updateDatabase(loginStateInstance.getId(),LoginStateInstance.STATE_OFFLINE);

            loginStateInstance.setId("");
            loginStateInstance.setPassword("");
            loginStateInstance.setLogin_state(LoginStateInstance.STATE_OFFLINE);

            finish();
        }

        private void updateDatabase(String id,String login_state){
            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(SettingsMainActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            String sqlStr = "select * from tb_login_info where id=?";
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, new String[]{id});

            ContentValues contentValues = new ContentValues();
            contentValues.put("login_state", login_state);

            if (cursor.moveToNext()) {
                sqLiteDatabase.update("tb_login_info", contentValues, "id=?", new String[]{id});
            }
            sqLiteDatabase.close();
            sqLiteDBHelper.close();
        }
    }
}
