package itc.ink.explorefuture_android.mine.settings;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

public class SettingsUserInfoActivity extends Activity {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "SettingsActivity";
    private ImageView backBtn;
    private ConstraintLayout changeHeadPortraitLayout;
    private ImageView headPortraitImage;
    private ConstraintLayout changeNicknameLayout;
    private ConstraintLayout changeSexLayout;
    private ConstraintLayout changeBirthdayLayout;
    private ConstraintLayout changeCityLayout;
    private ConstraintLayout changePersonalizedSignatureLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.mine_settings_user_info_activity);

        backBtn=findViewById(R.id.settings_UserInfo_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(new BackBtnClickListener());
        changeHeadPortraitLayout=findViewById(R.id.settings_UserInfo_Change_HeadPortrait_Layout);
        changeHeadPortraitLayout.setOnClickListener(new ChangeHeadPortraitLayoutClickListener());
        headPortraitImage=findViewById(R.id.settings_UserInfo_HeadPortrait_Image);
        changeNicknameLayout=findViewById(R.id.settings_UserInfo_Change_Nickname_Layout);
        changeNicknameLayout.setOnClickListener(new ChangeNicknameLayoutClickListener());
        changeSexLayout=findViewById(R.id.settings_UserInfo_Change_Sex_Layout);
        changeSexLayout.setOnClickListener(new ChangeSexLayoutClickListener());
        changeBirthdayLayout=findViewById(R.id.settings_UserInfo_Change_Birthday_Layout);
        changeBirthdayLayout.setOnClickListener(new ChangeBirthdayLayoutClickListener());
        changeCityLayout=findViewById(R.id.settings_UserInfo_Change_City_Layout);
        changeCityLayout.setOnClickListener(new ChangeCityLayoutClickListener());
        changePersonalizedSignatureLayout=findViewById(R.id.settings_UserInfo_Change_Personalized_Signature_Layout);
        changePersonalizedSignatureLayout.setOnClickListener(new ChangePersonalizedSignatureLayoutClickListener());

        updateView();
    }

    private void updateView(){
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(SettingsUserInfoActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_person_info where id=?";
        Cursor cursor = sqLiteDBHelper.getReadableDatabase().rawQuery(sqlStr, new String[]{LoginStateInstance.getInstance().getId()});
        if (cursor.moveToNext()) {
            RequestOptions options = new RequestOptions();
            options.signature(new ObjectKeyCanNull(cursor.getString(cursor.getColumnIndex("head_portrait_image_update_datetime"))).getObject());
            options.circleCrop();
            Glide.with(SettingsUserInfoActivity.this).load(cursor.getString(cursor.getColumnIndex("head_portrait_image_url"))).apply(options).into(headPortraitImage);
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

    class ChangeHeadPortraitLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingsUserInfoActivity.this,"更改头像被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ChangeNicknameLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingsUserInfoActivity.this,"更改昵称被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ChangeSexLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingsUserInfoActivity.this,"更改性别被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ChangeBirthdayLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingsUserInfoActivity.this,"更改出生日期被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ChangeCityLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingsUserInfoActivity.this,"更改所在城市被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ChangePersonalizedSignatureLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingsUserInfoActivity.this,"更改个性签名被点击",Toast.LENGTH_SHORT).show();
        }
    }

}
