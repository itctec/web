package itc.ink.explorefuture_android.recommend.mind_fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import itc.ink.explorefuture_android.R;

/**
 * Created by yangwenjiang on 2018/9/20.
 */

public class MindFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.recommend_fragment_empty,container,false);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
