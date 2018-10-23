package itc.ink.explorefuture_android.mine;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.activity.MainActivity;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.SQLiteDBHelper;


/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class MineFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.mine_fragment,container,false);
        final ConstraintLayout minePersonInfoLayout=rootView.findViewById(R.id.mine_Person_Info_Layout);
        ImageView mineMessageBtn=rootView.findViewById(R.id.mine_Message_Btn);
        ImageView mineSettingsBtn=rootView.findViewById(R.id.mine_Settings_Btn);
        TextView mineLoginBtn=rootView.findViewById(R.id.mine_Login_Btn);
        ImageView mineHeadPortraitImage=rootView.findViewById(R.id.mine_HeadPortrait_Image);
        TextView mineNicknameText=rootView.findViewById(R.id.mine_Nickname_Text);
        TextView minePersonalizedSignatureText=rootView.findViewById(R.id.mine_Personalized_Signature_Text);
        TextView mineFansCountText=rootView.findViewById(R.id.mine_Fans_Count_Text);
        TextView mineAttentionCountText=rootView.findViewById(R.id.mine_Attention_Count_Text);

        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
        String sqlStr = "select * from tb_person_info where id=?";
        Cursor cursor = sqLiteDBHelper.getReadableDatabase().rawQuery(sqlStr, new String[]{ExploreFutureApplication.TEMP_ACCOUNT});
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
            Glide.with(getContext()).load(cursor.getString(cursor.getColumnIndex("head_portrait_image_url"))).apply(options).into(mineHeadPortraitImage);

            mineNicknameText.setText(cursor.getString(cursor.getColumnIndex("nickname")));
            minePersonalizedSignatureText.setText(cursor.getString(cursor.getColumnIndex("personalized_signature")));
            mineFansCountText.setText(String.format(getResources().getString(R.string.mine_fans_count_text),cursor.getString(cursor.getColumnIndex("fans_count"))));
            mineAttentionCountText.setText(String.format(getResources().getString(R.string.mine_attention_count_text),cursor.getString(cursor.getColumnIndex("attention_count"))));
        }else{
            //Need ReLogin
        }


        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
