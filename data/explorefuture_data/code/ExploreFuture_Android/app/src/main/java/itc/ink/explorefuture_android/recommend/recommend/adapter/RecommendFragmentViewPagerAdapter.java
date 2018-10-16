package itc.ink.explorefuture_android.recommend.recommend.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by yangwenjiang on 2018/9/20.
 */

public class RecommendFragmentViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public RecommendFragmentViewPagerAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
        fm.beginTransaction().commitAllowingStateLoss();
    }

    @Override
    public int getCount() {
        return this.mFragments.size();
    }

    @Override
    public Fragment getItem(int i) {
        return this.mFragments.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
