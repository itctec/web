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
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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
import itc.ink.explorefuture_android.common_unit.view.AppInnerBadge;
import itc.ink.explorefuture_android.common_unit.view.EarningsBightView;
import itc.ink.explorefuture_android.common_unit.view.TextViewWithIndicator;
import itc.ink.explorefuture_android.login.LoginActivity;
import itc.ink.explorefuture_android.login.LoginStateInstance;
import itc.ink.explorefuture_android.mind.data_fragment.DataFragment;
import itc.ink.explorefuture_android.mind.nodata_fragment.MindNoDataFragment;
import itc.ink.explorefuture_android.mine.interaction.InteractionActivity;
import itc.ink.explorefuture_android.mine.settings.SettingsMainActivity;


/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class MineFragment extends Fragment {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "MineFragment";
    private final int LOGIN_REQUEST_CODE = 0x01;
    private LoginStateInstance loginStateInstance = LoginStateInstance.getInstance();

    private View mineOfflineMaskView;

    private ConstraintLayout minePersonInfoLayout;
    private ImageView mineMessageBtn;
    private AppInnerBadge mineMessageCountView;
    private ImageView mineSettingsBtn;
    private TextView mineLoginBtn;
    private ImageView mineHeadPortraitImage;
    private TextView mineNicknameText;
    private TextView minePersonalizedSignatureText;
    private TextView mineFansCountText;
    private TextView mineAttentionCountText;

    private TextView interactionHistoryBtn;
    private TextView interactionCommentBtn;
    private TextView interactionRetransmissionBtn;
    private TextView interactionCollectionBtn;
    private TextView interactionSupportBtn;

    private TextViewWithIndicator orderWaitPayBtn;
    private TextViewWithIndicator orderWaitShipBtn;
    private TextViewWithIndicator orderWaitGetBtn;
    private TextViewWithIndicator orderWaitEvaluateBtn;
    private TextViewWithIndicator orderAfterSaleBtn;

    private TextView totalEarningsValueText;
    private TextView todayEarningsValueText;
    private EarningsBightView earningsBightView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mine_fragment, container, false);
        minePersonInfoLayout = rootView.findViewById(R.id.mine_Person_Info_Layout);
        mineMessageBtn = rootView.findViewById(R.id.mine_Message_Btn);
        mineMessageBtn.setOnClickListener(new MineMessageBtnClickListener());
        mineMessageCountView = rootView.findViewById(R.id.mine_Message_Count_View);
        mineMessageCountView.setOnClickListener(new MineMessageBtnClickListener());
        mineSettingsBtn = rootView.findViewById(R.id.mine_Settings_Btn);
        mineSettingsBtn.setOnClickListener(new MineSettingsBtnClickListener());
        mineLoginBtn = rootView.findViewById(R.id.mine_Login_Btn);
        mineLoginBtn.setOnClickListener(new MineLoginBtnClickListener());
        mineHeadPortraitImage = rootView.findViewById(R.id.mine_HeadPortrait_Image);
        mineNicknameText = rootView.findViewById(R.id.mine_Nickname_Text);
        minePersonalizedSignatureText = rootView.findViewById(R.id.mine_Personalized_Signature_Text);
        mineFansCountText = rootView.findViewById(R.id.mine_Fans_Count_Text);
        mineFansCountText.setOnClickListener(new MineFansCountTextClickListener());
        mineAttentionCountText = rootView.findViewById(R.id.mine_Attention_Count_Text);
        mineAttentionCountText.setOnClickListener(new MineAttentionCountTextClickListener());

        mineOfflineMaskView = rootView.findViewById(R.id.mine_Offline_Mask_View);

        InteractionBtnClickListener interactionBtnClickListener = new InteractionBtnClickListener();
        interactionHistoryBtn = rootView.findViewById(R.id.mine_Interaction_History_Btn);
        interactionHistoryBtn.setOnClickListener(interactionBtnClickListener);
        interactionCommentBtn = rootView.findViewById(R.id.mine_Interaction_Comment_Btn);
        interactionCommentBtn.setOnClickListener(interactionBtnClickListener);
        interactionRetransmissionBtn = rootView.findViewById(R.id.mine_Interaction_Retransmission_Btn);
        interactionRetransmissionBtn.setOnClickListener(interactionBtnClickListener);
        interactionCollectionBtn = rootView.findViewById(R.id.mine_Interaction_Collection_Btn);
        interactionCollectionBtn.setOnClickListener(interactionBtnClickListener);
        interactionSupportBtn = rootView.findViewById(R.id.mine_Interaction_Support_Btn);
        interactionSupportBtn.setOnClickListener(interactionBtnClickListener);

        OrderBtnClickListener orderBtnClickListener = new OrderBtnClickListener();
        orderWaitPayBtn = rootView.findViewById(R.id.mine_Order_Wait_Payment_Btn);
        orderWaitPayBtn.setOnClickListener(orderBtnClickListener);
        orderWaitShipBtn = rootView.findViewById(R.id.mine_Order_Wait_Shipment_Btn);
        orderWaitShipBtn.setOnClickListener(orderBtnClickListener);
        orderWaitGetBtn = rootView.findViewById(R.id.mine_Order_Wait_Get_Btn);
        orderWaitGetBtn.setOnClickListener(orderBtnClickListener);
        orderWaitEvaluateBtn = rootView.findViewById(R.id.mine_Order_Wait_Evaluate_Btn);
        orderWaitEvaluateBtn.setOnClickListener(orderBtnClickListener);
        orderAfterSaleBtn = rootView.findViewById(R.id.mine_Order_After_Sale_Btn);
        orderAfterSaleBtn.setOnClickListener(orderBtnClickListener);

        totalEarningsValueText = rootView.findViewById(R.id.mine_Total_Earnings_Value);
        todayEarningsValueText = rootView.findViewById(R.id.mine_Today_Earnings_Value);
        earningsBightView = rootView.findViewById(R.id.mine_Earnings_Bight_View);

        updateLayout();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLayout();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            updateLayout();
        }
    }

    private void updateLayout() {
        if (loginStateInstance.getLogin_state().equals(LoginStateInstance.STATE_ONLINE)) {
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

                mineMessageBtn.setClickable(true);
                mineMessageCountView.setCount(7);
                mineLoginBtn.setVisibility(View.GONE);
                mineSettingsBtn.setVisibility(View.VISIBLE);
                mineNicknameText.setText(cursor.getString(cursor.getColumnIndex("nickname")));
                minePersonalizedSignatureText.setText(cursor.getString(cursor.getColumnIndex("personalized_signature")));
                mineFansCountText.setClickable(true);
                mineFansCountText.setText(String.format(getResources().getString(R.string.mine_fans_count_text), cursor.getString(cursor.getColumnIndex("fans_count"))));
                mineAttentionCountText.setClickable(true);
                mineAttentionCountText.setText(String.format(getResources().getString(R.string.mine_attention_count_text), cursor.getString(cursor.getColumnIndex("attention_count"))));

                mineOfflineMaskView.setVisibility(View.GONE);

                interactionHistoryBtn.setClickable(true);
                interactionCommentBtn.setClickable(true);
                interactionRetransmissionBtn.setClickable(true);
                interactionCollectionBtn.setClickable(true);
                interactionSupportBtn.setClickable(true);

                orderWaitPayBtn.setClickable(true);
                orderWaitShipBtn.setClickable(true);
                orderWaitGetBtn.setClickable(true);
                orderWaitGetBtn.showIndicator(true);
                orderWaitEvaluateBtn.setClickable(true);
                orderWaitEvaluateBtn.showIndicator(true);
                orderAfterSaleBtn.setClickable(true);

                totalEarningsValueText.setText("2798.53");
                todayEarningsValueText.setText("178.64");
                ArrayList<Float> earningsData=new ArrayList<>();
                earningsData.add(56.73f);
                earningsData.add(28.59f);
                earningsData.add(103.00f);
                earningsData.add(78.65f);
                earningsData.add(127.84f);
                earningsData.add(88.25f);
                earningsData.add(178.64f);
                earningsBightView.updateData(earningsData);
            } else {
                Log.d(LOG_TAG, "加载数据失败");
            }

            sqLiteDBHelper.close();
        } else {
            minePersonInfoLayout.setBackgroundResource(R.drawable.picture_place_image);
            RequestOptions options = new RequestOptions();
            options.circleCrop();
            Glide.with(getContext()).load(R.drawable.app_head_portrait_place_image).apply(options).into(mineHeadPortraitImage);

            mineMessageBtn.setClickable(false);
            mineMessageCountView.setCount(0);
            mineSettingsBtn.setVisibility(View.GONE);
            mineLoginBtn.setVisibility(View.VISIBLE);

            mineNicknameText.setText(getString(R.string.app_offline_tip_text));
            minePersonalizedSignatureText.setText("");
            mineFansCountText.setText(String.format(getResources().getString(R.string.mine_fans_count_text), "0"));
            mineFansCountText.setClickable(false);
            mineAttentionCountText.setText(String.format(getResources().getString(R.string.mine_attention_count_text), "0"));
            mineAttentionCountText.setClickable(false);

            mineOfflineMaskView.setVisibility(View.VISIBLE);

            interactionHistoryBtn.setClickable(false);
            interactionCommentBtn.setClickable(false);
            interactionRetransmissionBtn.setClickable(false);
            interactionCollectionBtn.setClickable(false);
            interactionSupportBtn.setClickable(false);

            orderWaitPayBtn.setClickable(false);
            orderWaitShipBtn.setClickable(false);
            orderWaitGetBtn.setClickable(false);
            orderWaitGetBtn.showIndicator(false);
            orderWaitEvaluateBtn.setClickable(false);
            orderWaitEvaluateBtn.showIndicator(false);
            orderAfterSaleBtn.setClickable(false);

            totalEarningsValueText.setText("00.00");
            todayEarningsValueText.setText("00.00");
            ArrayList<Float> earningsData=new ArrayList<>();
            earningsData.add(00.00f);
            earningsData.add(00.00f);
            earningsData.add(00.00f);
            earningsData.add(00.00f);
            earningsData.add(00.00f);
            earningsData.add(00.00f);
            earningsData.add(00.00f);
            earningsBightView.updateData(earningsData);
        }
    }

    class MineMessageBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "消息按钮被点击", Toast.LENGTH_SHORT).show();
        }
    }

    class MineLoginBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }
    }

    class MineSettingsBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), SettingsMainActivity.class);
            startActivity(intent);
        }
    }

    class MineFansCountTextClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "粉丝数量被点击", Toast.LENGTH_SHORT).show();
        }
    }

    class MineAttentionCountTextClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "关注数量被点击", Toast.LENGTH_SHORT).show();
        }
    }

    class InteractionBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getContext(), InteractionActivity.class);
            switch (view.getId()) {
                case R.id.mine_Interaction_History_Btn:
                    intent.putExtra(InteractionActivity.KEY_CURRENT_TAB,InteractionActivity.VALUE_TAB_HISTORY);
                    break;
                case R.id.mine_Interaction_Comment_Btn:
                    intent.putExtra(InteractionActivity.KEY_CURRENT_TAB,InteractionActivity.VALUE_TAB_COMMENT);
                    break;
                case R.id.mine_Interaction_Retransmission_Btn:
                    intent.putExtra(InteractionActivity.KEY_CURRENT_TAB,InteractionActivity.VALUE_TAB_RETRANSMISSION);
                    break;
                case R.id.mine_Interaction_Collection_Btn:
                    intent.putExtra(InteractionActivity.KEY_CURRENT_TAB,InteractionActivity.VALUE_TAB_COLLECTION);
                    break;
                case R.id.mine_Interaction_Support_Btn:
                    intent.putExtra(InteractionActivity.KEY_CURRENT_TAB,InteractionActivity.VALUE_TAB_SUPPORT);
                    break;
            }
            getContext().startActivity(intent);
        }
    }

    class OrderBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.mine_Order_Wait_Payment_Btn:
                    Toast.makeText(getContext(), "待付款按钮被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.mine_Order_Wait_Shipment_Btn:
                    Toast.makeText(getContext(), "待发货按钮被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.mine_Order_Wait_Get_Btn:
                    Toast.makeText(getContext(), "待收货按钮被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.mine_Order_Wait_Evaluate_Btn:
                    Toast.makeText(getContext(), "待评价按钮被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.mine_Order_After_Sale_Btn:
                    Toast.makeText(getContext(), "售后按钮被点击", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
