package itc.ink.explorefuture_android.recommend.attention_fragment.adapter.implement;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.youth.banner.listener.OnBannerListener;
import itc.ink.explorefuture_android.recommend.attention_fragment.adapter.AttentionDataAdapter;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class RecommendDelegateImplement implements AttentionDataAdapter.DelegateInterface {
    private Context mContext;

    @Override
    public void handleTransaction(Context mContext, AttentionDataAdapter.VH mHolder) {
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
