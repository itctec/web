package itc.ink.explorefuture_android.common_unit.person_details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.common_unit.person_details.adapter.PersonDetailsWrapAdapter;
import itc.ink.explorefuture_android.common_unit.topic_details.adapter.TopicDetailsWrapAdapter;

/**
 * Created by yangwenjiang on 2018/10/31.
 */

public class PersonDetailsActivity extends Activity {
    public static final String KEY_ID="id";

    private ConstraintLayout topNavigationBar;

    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private PersonDetailsWrapAdapter personDetailsWrapAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        String id_Str=intent.getStringExtra(KEY_ID);
        Toast.makeText(this,"ID:"+id_Str,Toast.LENGTH_SHORT).show();

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.common_unit_person_details_activity);

        topNavigationBar=findViewById(R.id.person_Details_Top_Navigation);

        ImageView backBtn=findViewById(R.id.person_Details_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(new BackBtnClickListener());

        contentRecyclerView=findViewById(R.id.person_Details_RecyclerView);
        personDetailsWrapAdapter = new PersonDetailsWrapAdapter(this, id_Str);
        contentRecyclerView.setAdapter(personDetailsWrapAdapter);
        contentRvLayoutManager = new LinearLayoutManager(this);
        contentRecyclerView.setLayoutManager(contentRvLayoutManager);
        contentRecyclerView.setOnScrollChangeListener(new ContentRecyclerViewScrollChangeListener());

    }


    class BackBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    class ContentRecyclerViewScrollChangeListener implements View.OnScrollChangeListener{
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager)contentRecyclerView.getLayoutManager();

        @Override
        public void onScrollChange(View view, int i, int i1, int i2, int i3) {
            int adapterNowPos = linearLayoutManager.findFirstVisibleItemPosition();
            if(adapterNowPos>1){
                topNavigationBar.setVisibility(View.VISIBLE);
            }else {
                topNavigationBar.setVisibility(View.GONE);
            }
        }
    }
}
