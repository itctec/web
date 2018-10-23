package itc.ink.explorefuture_android.mind;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.login.LoginActivity;
import itc.ink.explorefuture_android.login.LoginStateInstance;
import itc.ink.explorefuture_android.mind.edit_activity.MindEditActivity;
import itc.ink.explorefuture_android.mind.nodata_fragment.MindNoDataFragment;
import itc.ink.explorefuture_android.mind.data_fragment.DataFragment;
import itc.ink.explorefuture_android.sort.mode.mode_sort.SortListDataMode;


/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class MindFragment extends Fragment {
    private final int LOGIN_REQUEST_CODE = 0x01;
    private LoginStateInstance loginStateInstance;
    private String MIND_JSON_DATA_STR="";

    private ConstraintLayout mindFragmentContainer;
    private View mindOffLineLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginStateInstance = LoginStateInstance.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.mind_fragment, container, false);
        ImageView topEditBtn = rootView.findViewById(R.id.mind_Top_Navigation_Edit_Btn);
        topEditBtn.setOnClickListener(new TopEditBtnClickListener());

        mindFragmentContainer=rootView.findViewById(R.id.mind_Fragment_Container);
        mindOffLineLayout=rootView.findViewById(R.id.mind_OffLine_Layout);
        Button loginBtn=rootView.findViewById(R.id.app_Login_Btn);
        loginBtn.setOnClickListener(new LoginBtnClickListener());

        updateLayout();

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE&& resultCode == Activity.RESULT_OK) {
            updateLayout();
        }
    }

    private void updateLayout(){
        if(loginStateInstance.getLogin_state().equals(LoginStateInstance.STATE_ONLINE)){
            mindOffLineLayout.setVisibility(View.GONE);
            mindFragmentContainer.setVisibility(View.VISIBLE);

            boolean prepareDataSuccess = prepareData(getContext());
            if (prepareDataSuccess) {
                DataFragment dataFragment = new DataFragment();
                getChildFragmentManager().beginTransaction().replace(R.id.mind_Fragment_Container, dataFragment).commit();
            } else {
                MindNoDataFragment mindNoDataFragment = new MindNoDataFragment();
                getChildFragmentManager().beginTransaction().replace(R.id.mind_Fragment_Container, mindNoDataFragment).commit();
            }
        }else{
            //Show OffLine layout
            mindFragmentContainer.setVisibility(View.GONE);
            mindOffLineLayout.setVisibility(View.VISIBLE);
        }
    }

    private boolean prepareData(Context mContext) {
        StringBuilder stringBuilder = new StringBuilder();
        String filePath="/"+ LoginStateInstance.getInstance().getId();
        File localDataFile = new File(mContext.getFilesDir()+filePath, DataUpdateMode.MIND_LOCAL_DATA_FILE_NAME.replace(DataUpdateMode.ACCOUNT_ID_NEED_REPLACE, LoginStateInstance.getInstance().getId()));
        BufferedReader bufferedReader;
        if (localDataFile.exists()) {
            try {
                InputStream inputStream = new FileInputStream(localDataFile);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String catchStr;
                while ((catchStr = bufferedReader.readLine()) != null) {
                    stringBuilder.append(catchStr);
                }
                MIND_JSON_DATA_STR = stringBuilder.toString();

                inputStream.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (MIND_JSON_DATA_STR == null || MIND_JSON_DATA_STR.trim().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    class TopEditBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), MindEditActivity.class);
            startActivity(intent);
        }
    }

    class LoginBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent,LOGIN_REQUEST_CODE);
        }
    }
}
