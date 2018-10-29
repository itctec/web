package itc.ink.explorefuture_android.login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.GlideCircleWithBorder;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.SQLiteDBHelper;
import itc.ink.explorefuture_android.app.utils.SharedPreferenceUtil;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.mine.settings.SettingsMainActivity;

/**
 * Created by yangwenjiang on 2018/10/23.
 */

public class LoginActivity extends Activity {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "LoginActivity";
    private final String KEY_PASSWORD_VISIBLE="password_visible";
    private ImageView closeBtn;
    private TextView helpBtn;
    private ImageView headPortraitImage;
    private EditText idEdit;
    private EditText passwordEdit;
    private ImageView passwordHideBtn;
    private TextView forgetPasswordBtn;
    private Button loginBtn;
    private TextView smsLoginBtn;
    private TextView registerBtn;

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

        closeBtn=findViewById(R.id.login_Activity_Top_Navigation_Close_Btn);
        closeBtn.setOnClickListener(new CloseBtnClickListener());
        helpBtn=findViewById(R.id.login_Activity_Top_Navigation_Help_Btn);
        helpBtn.setOnClickListener(new HelpBtnClickListener());
        headPortraitImage=findViewById(R.id.login_Activity_HeadPortrait_Image);
        idEdit=findViewById(R.id.login_Activity_ID_Edit);
        idEdit.setOnFocusChangeListener(new IdEditFocusChangeListener());
        passwordEdit=findViewById(R.id.login_Activity_Password_Edit);
        passwordHideBtn=findViewById(R.id.login_Activity_Password_Hide_Btn);
        if(SharedPreferenceUtil.getBoolean(KEY_PASSWORD_VISIBLE,false)){
            passwordHideBtn.setImageResource(R.drawable.vector_drawable_password_visible);
            passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT);
        }else{
            passwordHideBtn.setImageResource(R.drawable.vector_drawable_password_gone);
            passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        passwordHideBtn.setOnClickListener(new PasswordHideBtnClickListener());
        forgetPasswordBtn=findViewById(R.id.login_Activity_Forget_Password_Btn);
        forgetPasswordBtn.setOnClickListener(new ForgetPasswordBtnClickListener());
        loginBtn=findViewById(R.id.login_Activity_Login_Btn);
        loginBtn.setOnClickListener(new LoginBtnClickListener());
        smsLoginBtn=findViewById(R.id.login_Activity_SMS_Login_Btn);
        smsLoginBtn.setOnClickListener(new SmsLoginBtnClickListener());

        registerBtn=findViewById(R.id.login_Activity_New_User_Register_Btn);
        registerBtn.setOnClickListener(new RegisterBtnClickListener());
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

    class CloseBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    class HelpBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(LoginActivity.this,"帮助按钮被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class IdEditFocusChangeListener implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b){
                SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(LoginActivity.this, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
                String sqlStr = "select * from tb_person_info where id=?";
                Cursor cursor = sqLiteDBHelper.getReadableDatabase().rawQuery(sqlStr, new String[]{idEdit.getText().toString().trim()});
                if (cursor.moveToNext()) {
                    RequestOptions options = new RequestOptions();
                    options.signature(new ObjectKeyCanNull(cursor.getString(cursor.getColumnIndex("head_portrait_image_update_datetime"))).getObject());
                    options.circleCrop();
                    options.transform(new GlideCircleWithBorder(1, Color.parseColor("#5588FF")));
                    Glide.with(LoginActivity.this).load(cursor.getString(cursor.getColumnIndex("head_portrait_image_url"))).apply(options).into(headPortraitImage);

                } else {
                    Log.d(LOG_TAG, "加载数据失败");
                }
                sqLiteDBHelper.close();
            }
        }
    }

    class PasswordHideBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(SharedPreferenceUtil.getBoolean(KEY_PASSWORD_VISIBLE,false)){
                passwordHideBtn.setImageResource(R.drawable.vector_drawable_password_gone);
                SharedPreferenceUtil.putBoolean(KEY_PASSWORD_VISIBLE,false);
                passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }else{
                passwordHideBtn.setImageResource(R.drawable.vector_drawable_password_visible);
                SharedPreferenceUtil.putBoolean(KEY_PASSWORD_VISIBLE,true);
                passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        }
    }

    class ForgetPasswordBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(LoginActivity.this,"忘记密码按钮被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class SmsLoginBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(LoginActivity.this,"短信验证码登录按钮被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class RegisterBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(LoginActivity.this,"新用户注册按钮被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class LoginBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String idStr=idEdit.getText().toString().trim();
            String passwordStr=passwordEdit.getText().toString().trim();
            if(idStr.equals("0000000001")&&passwordStr.equals("123456")){
                //Login
                LoginStateInstance loginStateInstance=LoginStateInstance.getInstance();
                loginStateInstance.setId(idStr);
                loginStateInstance.setPassword(passwordStr);
                loginStateInstance.setLogin_state(LoginStateInstance.STATE_ONLINE);

                updateDatabase(idStr,passwordStr,LoginStateInstance.STATE_ONLINE);

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
            }else{
                Toast.makeText(LoginActivity.this,"用户名/密码错误，请重新输入",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class MyHandler extends Handler {

        public void handleMessage(Message msg) {

            if (msg.what == DataUpdateUtil.UPDATE_DATA_FINISH_MSG) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();
                if (isOpen) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
