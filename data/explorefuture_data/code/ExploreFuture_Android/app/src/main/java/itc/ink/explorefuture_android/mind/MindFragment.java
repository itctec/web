package itc.ink.explorefuture_android.mind;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.mind.edit_activity.MindEditActivity;
import itc.ink.explorefuture_android.mind.nodata_fragment.MindNoDataFragment;
import itc.ink.explorefuture_android.mind.data_fragment.DataFragment;


/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class MindFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.mind_fragment,container,false);

        ImageView topEditBtn=rootView.findViewById(R.id.mind_Top_Navigation_Edit_Btn);
        topEditBtn.setOnClickListener(new TopEditBtnClickListener());

        boolean prepareDataFail = (DataUpdateMode.MIND_JSON_DATA_STR == null || DataUpdateMode.MIND_JSON_DATA_STR.trim().equals(""));
        if (prepareDataFail) {
            MindNoDataFragment mindNoDataFragment=new MindNoDataFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.mind_Fragment_Container, mindNoDataFragment).commit();
        } else {
            DataFragment dataFragment=new DataFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.mind_Fragment_Container, dataFragment).commit();
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    class TopEditBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getActivity(), MindEditActivity.class);
            startActivity(intent);
        }
    }
}
