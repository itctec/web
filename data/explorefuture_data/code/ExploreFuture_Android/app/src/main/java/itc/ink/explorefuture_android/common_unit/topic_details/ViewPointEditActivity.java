package itc.ink.explorefuture_android.common_unit.topic_details;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.SelectionCreator;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.utils.MyGlideEngine;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.app.utils.UnitConversionUtil;
import itc.ink.explorefuture_android.app.utils.permission.DynamicPermission;
import itc.ink.explorefuture_android.common_unit.common_dialog.CommonDialog;

/**
 * Created by yangwenjiang on 2018/11/1.
 */

public class ViewPointEditActivity extends Activity {
    private final int ZHI_HU_GET_IMAGE_REQUEST_CODE = 0x01;

    private ConstraintLayout activityLayout;
    private EditText viewPointTitleEditText;
    private EditText viewPointContentEditText;
    private TextView viewPointContentEditCharCountText;
    private ImageView viewPointCoverImage;
    private ImageView viewPointCoverImageDeleteBtn;

    private Uri coverImageUri=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.common_unit_topic_details_view_point_edit_activity);

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = 0;
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        activityLayout = findViewById(R.id.view_Point_Edit_Activity_Layout);
        FrameLayout.LayoutParams activityLayoutParams = (FrameLayout.LayoutParams) activityLayout.getLayoutParams();
        activityLayoutParams.topMargin = (int) (getResources().getDimension(R.dimen.app_status_bar_height) - statusBarHeight);
        activityLayout.setLayoutParams(activityLayoutParams);

        //Navigation Bar
        ImageView closeBtn = findViewById(R.id.view_Point_Edit_Top_Navigation_Close_Btn);
        closeBtn.setOnClickListener(new CloseBtnClickListener());

        TextView releaseBtn = findViewById(R.id.view_Point_Edit_Top_Navigation_Release_Btn);
        releaseBtn.setOnClickListener(new ReleaseBtnClickListener());


        //Edit Content
        viewPointTitleEditText = findViewById(R.id.view_Point_Title_EditText);
        viewPointTitleEditText.requestFocus();
        viewPointContentEditText = findViewById(R.id.view_Point_Content_EditText);
        viewPointContentEditText.addTextChangedListener(new ViewPointContentEditTextChangeListener());
        viewPointContentEditCharCountText = findViewById(R.id.view_Point_Edit_Char_Count_Text);

        viewPointCoverImage = findViewById(R.id.view_Point_Cover_Image);
        viewPointCoverImage.setOnClickListener(new ViewPointCoverImageClickListener());
        viewPointCoverImageDeleteBtn= findViewById(R.id.view_Point_Cover_Image_Delete_Btn);
        viewPointCoverImageDeleteBtn.setOnClickListener(new ViewPointCoverImageDeleteBtnClickListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ZHI_HU_GET_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            List<Uri> selectedImage = Matisse.obtainResult(data);
            if(selectedImage.size()>0){
                ConstraintLayout.LayoutParams layoutParams=(ConstraintLayout.LayoutParams)viewPointCoverImage.getLayoutParams();
                layoutParams.height= ConstraintLayout.LayoutParams.WRAP_CONTENT;
                viewPointCoverImage.setLayoutParams(layoutParams);
                coverImageUri=selectedImage.get(0);
                Glide.with(ViewPointEditActivity.this).load(coverImageUri).into(viewPointCoverImage);
                viewPointCoverImageDeleteBtn.setVisibility(View.VISIBLE);
            }
        }
    }

    private void saveDataWhenClose() {
        if (viewPointTitleEditText.getText().toString().length() != 0 ||
                viewPointContentEditText.getText().toString().length() != 0||
                coverImageUri!=null) {
            new CommonDialog(ViewPointEditActivity.this, getString(R.string.mind_edit_tip_close_dialog_content_text), new CommonDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if (confirm) {

                    }
                    dialog.dismiss();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    boolean isOpen = imm.isActive();
                    if (isOpen) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    finish();
                }
            }).setTitle(getString(R.string.dialog_title_tip_text))
                    .setNegativeButton(getString(R.string.dialog_negative_btn_text))
                    .setPositiveButton(getString(R.string.dialog_positive_save_btn_text))
                    .show();
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean isOpen = imm.isActive();
            if (isOpen) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            finish();
        }
    }

    private void addMedia(Set<MimeType> mimeTypes, int requestCode) {
        DynamicPermission dynamicPermission = new DynamicPermission();
        if (dynamicPermission.outService.hasPermission(ViewPointEditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                dynamicPermission.outService.hasPermission(ViewPointEditActivity.this, Manifest.permission.READ_PHONE_STATE)) {
            Matisse matisse = Matisse.from(ViewPointEditActivity.this);
            SelectionCreator selectionCreator = matisse.choose(mimeTypes);
            selectionCreator.countable(true);
            selectionCreator.capture(true);
            selectionCreator.captureStrategy(new CaptureStrategy(true, "itc.ink.explorefuture_android.fileProvider"));
            selectionCreator.maxSelectable(1);
            selectionCreator.gridExpectedSize(600);
            selectionCreator.restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            selectionCreator.thumbnailScale(0.85f);
            selectionCreator.theme(R.style.Matisse_Dracula);
            selectionCreator.imageEngine(new MyGlideEngine());
            selectionCreator.forResult(requestCode);
        } else {
            dynamicPermission.outService.showMissingPermissionDialog(ViewPointEditActivity.this, getString(R.string.mind_edit_need_access_sdcard_permission_text));
        }
    }


    class CloseBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            saveDataWhenClose();
        }
    }

    class ReleaseBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String viewPointTitle=viewPointTitleEditText.getText().toString().trim();
            String viewPointContent=viewPointContentEditText.getText().toString().trim();
            if(viewPointTitle!=null&&!viewPointTitle.isEmpty()){
                new CommonDialog(ViewPointEditActivity.this, getString(R.string.mind_edit_release_dialog_content_text), new CommonDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {

                        }
                        dialog.dismiss();
                    }
                }).setTitle(getString(R.string.mind_edit_release_dialog_title_text))
                        .setNegativeButton(getString(R.string.dialog_negative_btn_text))
                        .setPositiveButton(getString(R.string.mind_edit_release_dialog_positive_btn_text))
                        .show();
            }else{
                new CommonDialog(ViewPointEditActivity.this, getString(R.string.view_point_edit_release_dialog_content_text), new CommonDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {

                        }
                        dialog.dismiss();
                    }
                }).setTitle(getString(R.string.mind_edit_release_dialog_title_text))
                        .setNegativeButton(getString(R.string.dialog_negative_btn_text))
                        .setPositiveButton(getString(R.string.dialog_positive_btn_text))
                        .show();
            }

        }
    }

    class ViewPointContentEditTextChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            viewPointContentEditCharCountText.setText(charSequence.length() + "/280");
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    class ViewPointCoverImageClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Set<MimeType> mimeTypes = new HashSet<>();
            mimeTypes.add(MimeType.JPEG);
            mimeTypes.add(MimeType.PNG);
            addMedia(mimeTypes,ZHI_HU_GET_IMAGE_REQUEST_CODE);
        }
    }

    class ViewPointCoverImageDeleteBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            ConstraintLayout.LayoutParams layoutParams=(ConstraintLayout.LayoutParams)viewPointCoverImage.getLayoutParams();
            layoutParams.height= UnitConversionUtil.dip2px(ViewPointEditActivity.this,150);
            viewPointCoverImage.setLayoutParams(layoutParams);
            Glide.with(ViewPointEditActivity.this).load(R.drawable.view_point_cover_image_bg).into(viewPointCoverImage);
            coverImageUri=null;
            viewPointCoverImageDeleteBtn.setVisibility(View.GONE);
        }
    }
}
