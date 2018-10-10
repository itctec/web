package itc.ink.explorefuture_android.recommend.recommend;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.recommend.recommend.adapter.RecommendFragmentViewPagerAdapter;
import itc.ink.explorefuture_android.recommend.attention_fragment.AttentionFragment;
import itc.ink.explorefuture_android.recommend.handpick_fragment.HandpickFragment;
import itc.ink.explorefuture_android.recommend.mind_fragment.MindFragment;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class RecommendFragment extends Fragment {
    private TextView navigationChoicenessBtn;
    private TextView navigationAttentionBtn;
    private TextView navigationMindBtn;
    private View navigationIndicator;
    private ViewPager recommendContentViewPager;

    List<Fragment> mFragmentList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recommend_fragment, container, false);

        navigationIndicator = rootView.findViewById(R.id.recommend_Top_Navigation_Indicator);
        navigationChoicenessBtn = rootView.findViewById(R.id.recommend_Top_Navigation_Handpick_Btn);
        navigationChoicenessBtn.setOnClickListener(new NavigationChoicenessBtnClickListener());
        navigationAttentionBtn = rootView.findViewById(R.id.recommend_Top_Navigation_Attention_Btn);
        navigationAttentionBtn.setOnClickListener(new NavigationAttentionBtnClickListener());
        navigationMindBtn = rootView.findViewById(R.id.recommend_Top_Navigation_Mind_Btn);
        navigationMindBtn.setOnClickListener(new NavigationMindBtnClickListener());

        recommendContentViewPager = rootView.findViewById(R.id.recommend_Content_ViewPager);
        mFragmentList=initFragmentList();
        recommendContentViewPager.setAdapter(new RecommendFragmentViewPagerAdapter(getChildFragmentManager(),mFragmentList));
        recommendContentViewPager.setOffscreenPageLimit(3);
        recommendContentViewPager.addOnPageChangeListener(new RecommendContentViewPagerChangeListener());

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private List<Fragment> initFragmentList(){
        List<Fragment> mFragmentList = new ArrayList<>();
        HandpickFragment choicenessFragment=new HandpickFragment();
        AttentionFragment attentionFragment=new AttentionFragment();
        MindFragment mindFragment=new MindFragment();

        mFragmentList.add(choicenessFragment);
        mFragmentList.add(attentionFragment);
        mFragmentList.add(mindFragment);

        return mFragmentList;
    }

    private void updateNavigationTopBtnState(View currentActiveBtn) {
        navigationChoicenessBtn.setAlpha(0.5F);
        navigationAttentionBtn.setAlpha(0.5F);
        navigationMindBtn.setAlpha(0.5F);

        currentActiveBtn.setAlpha(1F);
    }

    class RecommendContentViewPagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
            navigationIndicator.setTranslationX((ExploreFutureApplication.screenWidth / 3) * i + (ExploreFutureApplication.screenWidth / 3) * v);
        }

        @Override
        public void onPageSelected(int i) {
            switch (i) {
                case 0:
                    updateNavigationTopBtnState(navigationChoicenessBtn);
                    break;
                case 1:
                    updateNavigationTopBtnState(navigationAttentionBtn);
                    break;
                case 2:
                    updateNavigationTopBtnState(navigationMindBtn);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    class NavigationChoicenessBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            recommendContentViewPager.setCurrentItem(0);
        }
    }

    class NavigationAttentionBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            recommendContentViewPager.setCurrentItem(1);
        }
    }

    class NavigationMindBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            recommendContentViewPager.setCurrentItem(2);
        }
    }
}
