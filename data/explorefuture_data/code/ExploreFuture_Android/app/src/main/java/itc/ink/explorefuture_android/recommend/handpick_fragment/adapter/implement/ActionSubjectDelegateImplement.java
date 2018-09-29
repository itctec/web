package itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.implement;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.HandPickWrapperAdapter;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_action.ActionSubjectDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class ActionSubjectDelegateImplement implements HandPickWrapperAdapter.DelegateInterface {
    private Context mContext;
    @Override
    public void handleTransaction(Context mContext, HandPickWrapperAdapter.WrapperVH mHolder, Object mData) {
        this.mContext=mContext;

        ArrayList<ActionSubjectDataMode> actionData=(ArrayList<ActionSubjectDataMode>)mData;

        RequestOptions options = new RequestOptions();

        mHolder.actionRecommendTopTextView.setOnClickListener(new ActionRecommendTopTextViewClickListener());
        mHolder.actionSubjectOneLayout.setOnClickListener(new ActionSubjectOneLayoutClickListener());
        mHolder.actionSubjectOneTitleTextView.setText(actionData.get(0).getTitle());
        mHolder.actionSubjectOneSummaryTextView.setText(actionData.get(0).getSummary());
        options.signature(new ObjectKeyCanNull(actionData.get(0).getGif_update_datetime()).getObject());
        Glide.with(mContext).load(actionData.get(0).getGifurl()).apply(options).into(mHolder.actionSubjectOneGifImageView);
    }

    class ActionRecommendTopTextViewClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext,"活动推荐榜单被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ActionSubjectOneLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext,"活动专题被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ActionSubjectOneVideoVideoViewErrorListener implements MediaPlayer.OnErrorListener{
        private VideoView mVideoView;
        public ActionSubjectOneVideoVideoViewErrorListener(VideoView mVideoView) {
            this.mVideoView=mVideoView;
        }

        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            mVideoView.stopPlayback();
            Toast.makeText(mContext,"活动专题视频无法播放",Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    class ActionSubjectOneVideoVideoViewCompletionListener implements  MediaPlayer.OnCompletionListener{
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
    }
}
