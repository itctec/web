package itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.implement;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.common_unit.content_details.ContentDetailsActivity;
import itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.HandPickWrapperAdapter;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_action.ActionSubjectDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class ActionSubjectDelegateImplement implements HandPickWrapperAdapter.DelegateInterface {
    private WeakReference<Context> mWeakContextReference;

    private Context getContext() {
        if(mWeakContextReference.get() != null){
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public void handleTransaction(Context mContext, HandPickWrapperAdapter.WrapperVH mHolder, Object mData) {
        this.mWeakContextReference = new WeakReference<>(mContext);

        ArrayList<ActionSubjectDataMode> actionData=(ArrayList<ActionSubjectDataMode>)mData;

        RequestOptions options = new RequestOptions();

        mHolder.actionRecommendTopTextView.setOnClickListener(new ActionRecommendTopTextViewClickListener());
        mHolder.actionSubjectOneLayout.setTag(actionData.get(0).getId());
        mHolder.actionSubjectOneLayout.setOnClickListener(new ActionSubjectOneLayoutClickListener());
        mHolder.actionSubjectOneTitleTextView.setText(actionData.get(0).getTitle());
        mHolder.actionSubjectOneSummaryTextView.setText(actionData.get(0).getSummary());
        options.signature(new ObjectKeyCanNull(actionData.get(0).getGif_update_datetime()).getObject());
        Glide.with(mContext).load(actionData.get(0).getGifurl()).apply(options).into(mHolder.actionSubjectOneGifImageView);
    }

    class ActionRecommendTopTextViewClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),"活动推荐榜单被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ActionSubjectOneLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String content_id=(String) view.getTag();
            Intent intent =new Intent(getContext(), ContentDetailsActivity.class);
            intent.putExtra(ContentDetailsActivity.KEY_CONTENT_ID,content_id);
            getContext().startActivity(intent);
        }
    }

}
