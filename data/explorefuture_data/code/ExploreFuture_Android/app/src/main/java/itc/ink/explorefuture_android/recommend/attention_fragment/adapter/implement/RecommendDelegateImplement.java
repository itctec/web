package itc.ink.explorefuture_android.recommend.attention_fragment.adapter.implement;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import itc.ink.explorefuture_android.recommend.attention_fragment.adapter.AttentionWrapAdapter;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class RecommendDelegateImplement implements AttentionWrapAdapter.DelegateInterface {
    private WeakReference<Context> mWeakContextReference;

    private Context getContext() {
        if(mWeakContextReference.get() != null){
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public void handleTransaction(Context mContext, AttentionWrapAdapter.WrapperVH mHolder) {
        this.mWeakContextReference = new WeakReference<>(mContext);

        mHolder.recommendUpdateThemBtn.setOnClickListener(new RecommendUpdateThemBtnClickListener());
    }


    class RecommendUpdateThemBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),"换一批被点击",Toast.LENGTH_SHORT).show();
        }
    }
}
