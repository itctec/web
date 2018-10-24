package itc.ink.explorefuture_android.mine;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.activity.MainActivity;
import itc.ink.explorefuture_android.app.app_level.GlideCircleWithBorder;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.SQLiteDBHelper;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.login.LoginActivity;
import itc.ink.explorefuture_android.login.LoginStateInstance;
import itc.ink.explorefuture_android.mind.data_fragment.DataFragment;
import itc.ink.explorefuture_android.mind.nodata_fragment.MindNoDataFragment;


/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class MineFragment extends Fragment {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "MineFragment";
    private final int LOGIN_REQUEST_CODE = 0x01;
    private LoginStateInstance loginStateInstance=LoginStateInstance.getInstance();

    private ConstraintLayout minePersonInfoLayout;
    private ImageView mineMessageBtn;
    private ImageView mineSettingsBtn;
    private TextView mineLoginBtn;
    private ImageView mineHeadPortraitImage;
    private TextView mineNicknameText;
    private TextView minePersonalizedSignatureText;
    private TextView mineFansCountText;
    private TextView mineAttentionCountText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.mine_fragment,container,false);
        minePersonInfoLayout=rootView.findViewById(R.id.mine_Person_Info_Layout);
        mineMessageBtn=rootView.findViewById(R.id.mine_Message_Btn);
        mineSettingsBtn=rootView.findViewById(R.id.mine_Settings_Btn);
        mineLoginBtn=rootView.findViewById(R.id.mine_Login_Btn);
        mineLoginBtn.setOnClickListener(new MineLoginBtnClickListener());
        mineHeadPortraitImage=rootView.findViewById(R.id.mine_HeadPortrait_Image);
        mineNicknameText=rootView.findViewById(R.id.mine_Nickname_Text);
        minePersonalizedSignatureText=rootView.findViewById(R.id.mine_Personalized_Signature_Text);
        mineFansCountText=rootView.findViewById(R.id.mine_Fans_Count_Text);
        mineAttentionCountText=rootView.findViewById(R.id.mine_Attention_Count_Text);

        updateLayout();

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE&& resultCode == Activity.RESULT_OK) {
            updateLayout();
        }
    }

    private void updateLayout(){
        if(loginStateInstance.getLogin_state().equals(LoginStateInstance.STATE_ONLINE)){
            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            String sqlStr = "select * from tb_person_info where id=?";
            Cursor cursor = sqLiteDBHelper.getReadableDatabase().rawQuery(sqlStr, new String[]{LoginStateInstance.getInstance().getId()});
            if (cursor.moveToNext()) {
                RequestOptions options = new RequestOptions();
                options.signature(new ObjectKeyCanNull(cursor.getString(cursor.getColumnIndex("personal_cover_bg_image_update_datetime"))).getObject());
                SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        minePersonInfoLayout.setBackground(resource);
                    }
                };
                Glide.with(getContext()).load(cursor.getString(cursor.getColumnIndex("personal_cover_bg_image_url"))).apply(options).into(simpleTarget);

                options.signature(new ObjectKeyCanNull(cursor.getString(cursor.getColumnIndex("head_portrait_image_update_datetime"))).getObject());
                options.circleCrop();
                options.transform(new GlideCircleWithBorder(1, Color.parseColor("#ccffffff")));
                Glide.with(getContext()).load(cursor.getString(cursor.getColumnIndex("head_portrait_image_url"))).apply(options).into(mineHeadPortraitImage);

                mineLoginBtn.setVisibility(View.GONE);
                mineSettingsBtn.setVisibility(View.VISIBLE);
                mineNicknameText.setText(cursor.getString(cursor.getColumnIndex("nickname")));
                minePersonalizedSignatureText.setText(cursor.getString(cursor.getColumnIndex("personalized_signature")));
                mineFansCountText.setText(String.format(getResources().getString(R.string.mine_fans_count_text),cursor.getString(cursor.getColumnIndex("fans_count"))));
                mineAttentionCountText.setText(String.format(getResources().getString(R.string.mine_attention_count_text),cursor.getString(cursor.getColumnIndex("attention_count"))));
            }else{
                Log.d(LOG_TAG,"加载数据失败");
            }
        }else{
            minePersonInfoLayout.setBackgroundResource(R.drawable.picture_place_image);
            mineSettingsBtn.setVisibility(View.GONE);
            mineLoginBtn.setVisibility(View.VISIBLE);

            mineNicknameText.setText(getString(R.string.app_offline_tip_text));
            minePersonalizedSignatureText.setText("");
            mineFansCountText.setText(String.format(getResources().getString(R.string.mine_fans_count_text),"0"));
            mineAttentionCountText.setText(String.format(getResources().getString(R.string.mine_attention_count_text),"0"));
        }
    }

    class MineLoginBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent,LOGIN_REQUEST_CODE);
        }
    }

}
