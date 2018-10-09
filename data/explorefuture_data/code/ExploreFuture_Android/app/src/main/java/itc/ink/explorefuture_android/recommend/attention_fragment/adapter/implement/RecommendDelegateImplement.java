package itc.ink.explorefuture_android.recommend.attention_fragment.adapter.implement;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import itc.ink.explorefuture_android.recommend.attention_fragment.adapter.AttentionWrapAdapter;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class RecommendDelegateImplement implements AttentionWrapAdapter.DelegateInterface {
    private Context mContext;

    @Override
    public void handleTransaction(Context mContext, AttentionWrapAdapter.WrapperVH mHolder) {
        this.mContext = mContext;

        mHolder.recommendUpdateThemBtn.setOnClickListener(new RecommendUpdateThemBtnClickListener());
    }


    class RecommendUpdateThemBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext,"换一批被点击",Toast.LENGTH_SHORT).show();
        }
    }
}
