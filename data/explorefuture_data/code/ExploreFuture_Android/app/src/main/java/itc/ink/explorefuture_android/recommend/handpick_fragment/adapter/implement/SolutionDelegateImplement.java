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
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_solution.SolutionDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class SolutionDelegateImplement implements HandPickWrapperAdapter.DelegateInterface {
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

        ArrayList<SolutionDataMode> solutionData=(ArrayList<SolutionDataMode>)mData;

        RequestOptions options = new RequestOptions();

        mHolder.solutionRecommendTopTextView.setOnClickListener(new SolutionRecommendTopTextViewClickListener());

        SolutionSortClickListener solutionSortClickListener=new SolutionSortClickListener();
        mHolder.solutionSortOneLayout.setTag(solutionData.get(0).getId());
        mHolder.solutionSortOneLayout.setOnClickListener(solutionSortClickListener);
        mHolder.solutionSortOneTitleTextView.setText(solutionData.get(0).getTitle());
        mHolder.solutionSortOneSummaryTextView.setText(solutionData.get(0).getSummary());
        options.signature(new ObjectKeyCanNull(solutionData.get(0).getImage_left_update_datetime()).getObject());
        Glide.with(mContext).load(solutionData.get(0).getImageurl_left()).apply(options).into(mHolder.solutionSortOneLeftImageImageView);
        options.signature(new ObjectKeyCanNull(solutionData.get(0).getImage_right_update_datetime()).getObject());
        Glide.with(mContext).load(solutionData.get(0).getImageurl_right()).apply(options).into(mHolder.solutionSortOneRightImageImageView);

        mHolder.solutionSortTwoLayout.setTag(solutionData.get(1).getId());
        mHolder.solutionSortTwoLayout.setOnClickListener(solutionSortClickListener);
        mHolder.solutionSortTwoTitleTextView.setText(solutionData.get(1).getTitle());
        mHolder.solutionSortTwoSummaryTextView.setText(solutionData.get(1).getSummary());
        options.signature(new ObjectKeyCanNull(solutionData.get(1).getImage_left_update_datetime()).getObject());
        Glide.with(mContext).load(solutionData.get(1).getImageurl_left()).apply(options).into(mHolder.solutionSortTwoLeftImageImageView);
        options.signature(new ObjectKeyCanNull(solutionData.get(1).getImage_right_update_datetime()).getObject());
        Glide.with(mContext).load(solutionData.get(1).getImageurl_right()).apply(options).into(mHolder.solutionSortTwoRightImageImageView);

        mHolder.solutionSortThreeLayout.setTag(solutionData.get(2).getId());
        mHolder.solutionSortThreeLayout.setOnClickListener(solutionSortClickListener);
        mHolder.solutionSortThreeTitleTextView.setText(solutionData.get(2).getTitle());
        mHolder.solutionSortThreeSummaryTextView.setText(solutionData.get(2).getSummary());
        options.signature(new ObjectKeyCanNull(solutionData.get(2).getImage_left_update_datetime()).getObject());
        Glide.with(mContext).load(solutionData.get(2).getImageurl_left()).apply(options).into(mHolder.solutionSortThreeLeftImageImageView);
        options.signature(new ObjectKeyCanNull(solutionData.get(2).getImage_right_update_datetime()).getObject());
        Glide.with(mContext).load(solutionData.get(2).getImageurl_right()).apply(options).into(mHolder.solutionSortThreeRightImageImageView);

        mHolder.solutionSortFourLayout.setTag(solutionData.get(3).getId());
        mHolder.solutionSortFourLayout.setOnClickListener(solutionSortClickListener);
        mHolder.solutionSortFourTitleTextView.setText(solutionData.get(3).getTitle());
        mHolder.solutionSortFourSummaryTextView.setText(solutionData.get(3).getSummary());
        options.signature(new ObjectKeyCanNull(solutionData.get(3).getImage_left_update_datetime()).getObject());
        Glide.with(mContext).load(solutionData.get(3).getImageurl_left()).apply(options).into(mHolder.solutionSortFourLeftImageImageView);
        options.signature(new ObjectKeyCanNull(solutionData.get(3).getImage_right_update_datetime()).getObject());
        Glide.with(mContext).load(solutionData.get(3).getImageurl_right()).apply(options).into(mHolder.solutionSortFourRightImageImageView);

        SolutionClickListener solutionClickListener=new SolutionClickListener();
        mHolder.solutionSubjectOneLayout.setTag(solutionData.get(4).getId());
        mHolder.solutionSubjectOneLayout.setOnClickListener(solutionClickListener);
        options.signature(new ObjectKeyCanNull(solutionData.get(4).getGif_update_datetime()).getObject());
        Glide.with(mContext).load(solutionData.get(4).getGifurl()).apply(options).into(mHolder.solutionSubjectOneGifImageView);
        mHolder.solutionSubjectOneTitleTextView.setText(solutionData.get(4).getTitle());
        mHolder.solutionSubjectOneSummaryTextView.setText(solutionData.get(4).getSummary());

        mHolder.solutionOneLayout.setTag(solutionData.get(5).getId());
        mHolder.solutionOneLayout.setOnClickListener(solutionClickListener);
        options.signature(new ObjectKeyCanNull(solutionData.get(5).getImage_update_datetime()).getObject());
        Glide.with(mContext).load(solutionData.get(5).getImageurl()).apply(options).into(mHolder.solutionOneImageImageView);
        mHolder.solutionOneTitleTextView.setText(solutionData.get(5).getTitle());
        mHolder.solutionOneSummaryTextView.setText(solutionData.get(5).getSummary());

        mHolder.solutionTwoLayout.setTag(solutionData.get(6).getId());
        mHolder.solutionTwoLayout.setOnClickListener(solutionClickListener);
        options.signature(new ObjectKeyCanNull(solutionData.get(6).getImage_update_datetime()).getObject());
        Glide.with(mContext).load(solutionData.get(6).getImageurl()).apply(options).into(mHolder.solutionTwoImageImageView);
        mHolder.solutionTwoTitleTextView.setText(solutionData.get(6).getTitle());
        mHolder.solutionTwoSummaryTextView.setText(solutionData.get(6).getSummary());

        mHolder.solutionThreeLayout.setTag(solutionData.get(7).getId());
        mHolder.solutionThreeLayout.setOnClickListener(solutionClickListener);
        options.signature(new ObjectKeyCanNull(solutionData.get(7).getImage_update_datetime()).getObject());
        Glide.with(mContext).load(solutionData.get(7).getImageurl()).apply(options).into(mHolder.solutionThreeImageImageView);
        mHolder.solutionThreeTitleTextView.setText(solutionData.get(7).getTitle());
        mHolder.solutionThreeSummaryTextView.setText(solutionData.get(7).getSummary());

        mHolder.solutionFourLayout.setTag(solutionData.get(8).getId());
        mHolder.solutionFourLayout.setOnClickListener(solutionClickListener);
        options.signature(new ObjectKeyCanNull(solutionData.get(8).getImage_update_datetime()).getObject());
        Glide.with(mContext).load(solutionData.get(8).getImageurl()).apply(options).into(mHolder.solutionFourImageImageView);
        mHolder.solutionFourTitleTextView.setText(solutionData.get(8).getTitle());
        mHolder.solutionFourSummaryTextView.setText(solutionData.get(8).getSummary());

        mHolder.solutionSimpleOneLayout.setTag(solutionData.get(9).getId());
        mHolder.solutionSimpleOneLayout.setOnClickListener(solutionClickListener);
        mHolder.solutionSimpleOneTitleTextView.setText(solutionData.get(9).getTitle());
        mHolder.solutionSimpleOneSummaryTextView.setText(solutionData.get(9).getSummary());

        mHolder.solutionSimpleTwoLayout.setTag(solutionData.get(10).getId());
        mHolder.solutionSimpleTwoLayout.setOnClickListener(solutionClickListener);
        mHolder.solutionSimpleTwoTitleTextView.setText(solutionData.get(10).getTitle());
        mHolder.solutionSimpleTwoSummaryTextView.setText(solutionData.get(10).getSummary());

        mHolder.solutionSimpleThreeLayout.setTag(solutionData.get(11).getId());
        mHolder.solutionSimpleThreeLayout.setOnClickListener(solutionClickListener);
        mHolder.solutionSimpleThreeTitleTextView.setText(solutionData.get(11).getTitle());
        mHolder.solutionSimpleThreeSummaryTextView.setText(solutionData.get(11).getSummary());
    }

    class SolutionRecommendTopTextViewClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),"方案推荐榜单被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class SolutionSortClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String sort_id=(String) view.getTag();
            Toast.makeText(getContext(),"方案类别"+sort_id+"被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class SolutionClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String content_id=(String) view.getTag();
            Intent intent =new Intent(getContext(), ContentDetailsActivity.class);
            intent.putExtra(ContentDetailsActivity.KEY_CONTENT_ID,content_id);
            getContext().startActivity(intent);
        }
    }

}
