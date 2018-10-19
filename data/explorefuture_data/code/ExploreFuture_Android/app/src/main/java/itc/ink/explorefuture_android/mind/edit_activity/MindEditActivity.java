package itc.ink.explorefuture_android.mind.edit_activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.SelectionCreator;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.activity.MainActivity;
import itc.ink.explorefuture_android.app.utils.GetPathFromUri4kitkat;
import itc.ink.explorefuture_android.app.utils.MyGlideEngine;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.app.utils.permission.DynamicPermission;
import itc.ink.explorefuture_android.common_unit.common_dialog.CommonDialog;
import itc.ink.explorefuture_android.common_unit.video_view.VideoViewerActivity;
import itc.ink.explorefuture_android.mind.edit_activity.adapter.MindEditImageDataAdapter;
import itc.ink.explorefuture_android.mind.edit_activity.mode.MindEditImageListDataMode;
import itc.ink.explorefuture_android.sort.adapter.SortDataAdapter;

/**
 * Created by yangwenjiang on 2018/10/17.
 */

public class MindEditActivity extends Activity {
    private final int ZHI_HU_GET_IMAGE_REQUEST_CODE = 0x01;
    private final int ZHI_HU_TAKE_PHOTO_REQUEST_CODE = 0x02;
    private final int ZHI_HU_GET_VIDEO_REQUEST_CODE = 0x03;
    private final int L_FILE_PICKER_GET_FILE_REQUEST_CODE = 0x04;

    private EditText mindEditText;
    private RecyclerView mindImageRecyclerView;
    private ArrayList<MindEditImageListDataMode> imageDataList = new ArrayList<>();
    private MindEditImageListDataMode mindEditImageListDataAddBtnItem = new MindEditImageListDataMode(null, false);
    private MindEditImageDataAdapter contentRvAdapter;

    private ImageView mindVideoFrameView;
    private ImageView mindVideoFrameViewDeleteBtn;
    private String videoUriStr = null;

    private TextView mindAttachment;
    private ImageView mindAttachmentDeleteBtn;
    private String attachmentFilePath = null;

    private TextView mindEditCharCountText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.mind_edit_activity);

        //Navigation Bar
        ImageView closeBtn = findViewById(R.id.mind_Edit_Top_Navigation_Close_Btn);
        closeBtn.setOnClickListener(new CloseBtnClickListener());

        TextView saveBtn = findViewById(R.id.mind_Edit_Top_Navigation_Save_Btn);
        saveBtn.setOnClickListener(new SaveBtnClickListener());

        TextView releaseBtn = findViewById(R.id.mind_Edit_Top_Navigation_Release_Btn);
        releaseBtn.setOnClickListener(new ReleaseBtnClickListener());

        //Edit Content
        mindEditText = findViewById(R.id.mind_Edit_EditText);
        mindEditText.requestFocus();
        mindEditText.addTextChangedListener(new MindEditTextChangeListener());

        imageDataList.add(mindEditImageListDataAddBtnItem);
        mindImageRecyclerView = findViewById(R.id.mind_Edit_RecyclerView);
        contentRvAdapter = new MindEditImageDataAdapter(MindEditActivity.this, imageDataList, mindEditText.getText().toString(), new MindEditImageListDataAddBtnItemClickCallBack());
        mindImageRecyclerView.setAdapter(contentRvAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new GridLayoutManager(MindEditActivity.this, 3);
        mindImageRecyclerView.setLayoutManager(contentRvLayoutManager);
        DividerItemDecoration dividerItemDecorationOne = new DividerItemDecoration(MindEditActivity.this, DividerItemDecoration.VERTICAL);
        dividerItemDecorationOne.setDrawable(ContextCompat.getDrawable(MindEditActivity.this, R.drawable.mind_image_divider_horizontal));
        mindImageRecyclerView.addItemDecoration(dividerItemDecorationOne);
        DividerItemDecoration dividerItemDecorationTwo = new DividerItemDecoration(MindEditActivity.this, DividerItemDecoration.HORIZONTAL);
        dividerItemDecorationTwo.setDrawable(ContextCompat.getDrawable(MindEditActivity.this, R.drawable.mind_image_divider_vertical));
        mindImageRecyclerView.addItemDecoration(dividerItemDecorationTwo);

        mindVideoFrameView = findViewById(R.id.mind_Edit_Video_Frame_View);
        mindVideoFrameView.setOnClickListener(new MindVideoFrameViewClickListener());
        mindVideoFrameViewDeleteBtn = findViewById(R.id.mind_Edit_Video_Frame_View_Delete_Btn);
        mindVideoFrameViewDeleteBtn.setOnClickListener(new MindVideoFrameViewDeleteBtnClickListener());

        mindAttachment = findViewById(R.id.mind_Edit_Attachment);
        mindAttachmentDeleteBtn = findViewById(R.id.mind_Edit_Attachment_Delete_Btn);
        mindAttachmentDeleteBtn.setOnClickListener(new MindAttachmentDeleteBtnClickListener());

        mindEditCharCountText=findViewById(R.id.mind_Edit_Char_Count_Text);
        mindEditCharCountText.setText(mindEditText.getText().toString().length()+"/280");


        //ToolsBar
        ImageView addImageBtn = findViewById(R.id.bottom_Tools_Bar_Add_Image_Btn);
        addImageBtn.setOnClickListener(new AddImageBtnClickListener());
        ImageView takePhotoBtn = findViewById(R.id.bottom_Tools_Bar_Take_Photo_Btn);
        takePhotoBtn.setOnClickListener(new TakePhotoBtnClickListener());
        ImageView addVideoBtn = findViewById(R.id.bottom_Tools_Bar_Add_Video_Btn);
        addVideoBtn.setOnClickListener(new AddVideoBtnClickListener());
        ImageView addAttachmentBtn = findViewById(R.id.bottom_Tools_Bar_Add_Attachment_Btn);
        addAttachmentBtn.setOnClickListener(new AddAttachmentBtnClickListener());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                saveDataWhenClose();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ZHI_HU_GET_IMAGE_REQUEST_CODE || requestCode == ZHI_HU_TAKE_PHOTO_REQUEST_CODE) && resultCode == RESULT_OK) {
            videoUriStr=null;
            mindVideoFrameView.setVisibility(View.GONE);
            mindVideoFrameViewDeleteBtn.setVisibility(View.GONE);
            mindImageRecyclerView.setVisibility(View.VISIBLE);

            List<Uri> selectedImage = Matisse.obtainResult(data);
            for (Uri uri : selectedImage) {
                MindEditImageListDataMode mindEditImageListDataItem = new MindEditImageListDataMode(uri.toString(), true);
                imageDataList.add(mindEditImageListDataItem);
            }
            if (imageDataList.size() < 11) {
                if (imageDataList.contains(mindEditImageListDataAddBtnItem)) {
                    imageDataList.remove(mindEditImageListDataAddBtnItem);
                    imageDataList.add(mindEditImageListDataAddBtnItem);
                }
            }
            contentRvAdapter.notifyDataSetChanged();
            contentRvAdapter.notifyItemRangeChanged(0, contentRvAdapter.getItemCount());
        } else if (requestCode == ZHI_HU_GET_VIDEO_REQUEST_CODE && resultCode == RESULT_OK) {
            imageDataList.clear();
            imageDataList.add(mindEditImageListDataAddBtnItem);
            contentRvAdapter.notifyDataSetChanged();

            mindImageRecyclerView.setVisibility(View.GONE);
            mindVideoFrameView.setVisibility(View.VISIBLE);
            mindVideoFrameViewDeleteBtn.setVisibility(View.VISIBLE);
            List<Uri> selectedImage = Matisse.obtainResult(data);
            if (selectedImage.size() > 0 && selectedImage.get(0) != null) {
                videoUriStr = GetPathFromUri4kitkat.getPath(MindEditActivity.this, selectedImage.get(0));
                mindVideoFrameView.setImageBitmap(getVideoThumb(videoUriStr));
            }
        } else if (requestCode == L_FILE_PICKER_GET_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> fileList = data.getStringArrayListExtra(Constant.RESULT_INFO);

            if (fileList.size() > 0 && fileList.get(0) != null) {
                mindAttachment.setVisibility(View.VISIBLE);
                mindAttachmentDeleteBtn.setVisibility(View.VISIBLE);
                attachmentFilePath = fileList.get(0);
                File attachmentFile = new File(attachmentFilePath);
                mindAttachment.setText(attachmentFile.getName());
            }
        }
    }

    private void addMedia(Set<MimeType> mimeTypes, boolean canTakePhoto, int requestCode) {
        DynamicPermission dynamicPermission = new DynamicPermission();
        if (dynamicPermission.outService.hasPermission(MindEditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                dynamicPermission.outService.hasPermission(MindEditActivity.this, Manifest.permission.READ_PHONE_STATE)) {
            int canAddPicCount = 0;
            if (imageDataList.contains(mindEditImageListDataAddBtnItem)) {
                canAddPicCount = 10;
            } else {
                canAddPicCount = 9;
            }

            if ((canAddPicCount - imageDataList.size()) > 0) {
                Matisse matisse = Matisse.from(MindEditActivity.this);
                SelectionCreator selectionCreator = matisse.choose(mimeTypes);
                selectionCreator.countable(true);
                if (canTakePhoto) {
                    selectionCreator.capture(true);
                    selectionCreator.captureStrategy(new CaptureStrategy(true, "itc.ink.explorefuture_android.fileProvider"));
                }

                if (mimeTypes.contains(MimeType.MP4)) {
                    selectionCreator.maxSelectable(1);
                } else {
                    selectionCreator.maxSelectable(canAddPicCount - imageDataList.size());
                }
                selectionCreator.gridExpectedSize(300);
                selectionCreator.restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                selectionCreator.thumbnailScale(0.85f);
                selectionCreator.theme(R.style.Matisse_Dracula);
                selectionCreator.imageEngine(new MyGlideEngine());
                selectionCreator.forResult(requestCode);
            } else {
                new CommonDialog(MindEditActivity.this, getString(R.string.mind_edit_tip_full_dialog_content_text), new CommonDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        dialog.dismiss();
                    }
                }).setTitle(getString(R.string.dialog_title_tip_text))
                        .setNegativeButton(getString(R.string.dialog_negative_btn_text))
                        .setPositiveButton(getString(R.string.mind_edit_tip_full_dialog_positive_btn_text))
                        .show();
            }
        } else {
            dynamicPermission.outService.showMissingPermissionDialog(MindEditActivity.this, getString(R.string.mind_edit_need_access_sdcard_permission_text));
        }
    }

    private Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        Bitmap frameBitmap = media.getFrameAtTime();
        Bitmap tempBitmap = Bitmap.createBitmap(frameBitmap.getWidth(), frameBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(frameBitmap, 0, 0, null);
        Bitmap playBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.video_play_btn_image);
        canvas.drawBitmap(playBitmap, frameBitmap.getWidth() / 2 - playBitmap.getWidth() / 2, frameBitmap.getHeight() / 2 - playBitmap.getHeight() / 2, null);
        frameBitmap.recycle();
        playBitmap.recycle();
        return tempBitmap;
    }

    private void saveDataWhenClose(){
        if(mindEditText.getText().toString().length()!=0||imageDataList.size()>1||videoUriStr!=null||attachmentFilePath!=null){
            new CommonDialog(MindEditActivity.this, getString(R.string.mind_edit_tip_close_dialog_content_text), new CommonDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if(confirm){

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
        }else{
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean isOpen = imm.isActive();
            if (isOpen) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            finish();
        }
    }

    class CloseBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            saveDataWhenClose();
        }
    }

    class SaveBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Toast.makeText(MindEditActivity.this, "内容已保存", Toast.LENGTH_SHORT).show();
        }
    }

    class ReleaseBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            new CommonDialog(MindEditActivity.this, getString(R.string.mind_edit_release_dialog_content_text), new CommonDialog.OnCloseListener() {
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
        }
    }

    class MindEditTextChangeListener implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mindEditCharCountText.setText(charSequence.length()+"/280");
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    class MindVideoFrameViewClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MindEditActivity.this, VideoViewerActivity.class);
            intent.putExtra(VideoViewerActivity.KEY_VIDEO_URL, videoUriStr);
            intent.putExtra(VideoViewerActivity.KEY_CONTENT_TEXT, "");
            startActivity(intent);
        }
    }

    class MindVideoFrameViewDeleteBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            videoUriStr=null;
            mindVideoFrameView.setVisibility(View.GONE);
            mindVideoFrameViewDeleteBtn.setVisibility(View.GONE);
            mindImageRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    class MindAttachmentDeleteBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mindAttachment.setVisibility(View.GONE);
            mindAttachmentDeleteBtn.setVisibility(View.GONE);
            attachmentFilePath = null;
            mindAttachment.setText("");
        }
    }

    class AddImageBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mindVideoFrameView.getVisibility() == View.GONE) {
                Set<MimeType> mimeTypes = new HashSet<>();
                mimeTypes.add(MimeType.JPEG);
                mimeTypes.add(MimeType.PNG);
                addMedia(mimeTypes, false, ZHI_HU_GET_IMAGE_REQUEST_CODE);
            } else {
                new CommonDialog(MindEditActivity.this, getString(R.string.mind_edit_tip_add_picture_confirm_dialog_content_text), new CommonDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if(confirm){
                            Set<MimeType> mimeTypes = new HashSet<>();
                            mimeTypes.add(MimeType.JPEG);
                            mimeTypes.add(MimeType.PNG);
                            addMedia(mimeTypes, false, ZHI_HU_GET_IMAGE_REQUEST_CODE);
                        }
                        dialog.dismiss();
                    }
                }).setTitle(getString(R.string.dialog_title_tip_text))
                        .setNegativeButton(getString(R.string.dialog_negative_btn_text))
                        .setPositiveButton(getString(R.string.dialog_positive_btn_text))
                        .show();
            }
        }
    }

    class TakePhotoBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mindVideoFrameView.getVisibility() == View.GONE) {
                Set<MimeType> mimeTypes = new HashSet<>();
                mimeTypes.add(MimeType.JPEG);
                mimeTypes.add(MimeType.PNG);
                addMedia(mimeTypes, true, ZHI_HU_TAKE_PHOTO_REQUEST_CODE);
            } else {
                new CommonDialog(MindEditActivity.this, getString(R.string.mind_edit_tip_add_picture_confirm_dialog_content_text), new CommonDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if(confirm){
                            Set<MimeType> mimeTypes = new HashSet<>();
                            mimeTypes.add(MimeType.JPEG);
                            mimeTypes.add(MimeType.PNG);
                            addMedia(mimeTypes, false, ZHI_HU_GET_IMAGE_REQUEST_CODE);
                        }
                        dialog.dismiss();
                    }
                }).setTitle(getString(R.string.dialog_title_tip_text))
                        .setNegativeButton(getString(R.string.dialog_negative_btn_text))
                        .setPositiveButton(getString(R.string.dialog_positive_btn_text))
                        .show();
            }
        }
    }

    class AddVideoBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (imageDataList.size() == 1) {
                Set<MimeType> mimeTypes = new HashSet<>();
                mimeTypes.add(MimeType.MP4);
                addMedia(mimeTypes, false, ZHI_HU_GET_VIDEO_REQUEST_CODE);
            } else {
                new CommonDialog(MindEditActivity.this, getString(R.string.mind_edit_tip_add_video_confirm_dialog_content_text), new CommonDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if(confirm){
                            Set<MimeType> mimeTypes = new HashSet<>();
                            mimeTypes.add(MimeType.MP4);
                            addMedia(mimeTypes, false, ZHI_HU_GET_VIDEO_REQUEST_CODE);
                        }
                        dialog.dismiss();
                    }
                }).setTitle(getString(R.string.dialog_title_tip_text))
                        .setNegativeButton(getString(R.string.dialog_negative_btn_text))
                        .setPositiveButton(getString(R.string.dialog_positive_btn_text))
                        .show();
            }
        }
    }

    class AddAttachmentBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            DynamicPermission dynamicPermission = new DynamicPermission();
            if (dynamicPermission.outService.hasPermission(MindEditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new LFilePicker()
                        .withActivity(MindEditActivity.this)
                        .withMutilyMode(false)
                        .withTitle(getString(R.string.mind_edit_choose_file_title_text))
                        .withFileFilter(new String[]{".txt", ".doc", ".docx", ".xls", ".xlsx", ".zip", ".rar", ".pdf", ".ppt", ".pptx"})
                        .withIconStyle(Constant.ICON_STYLE_BLUE)
                        .withBackIcon(Constant.BACKICON_STYLETHREE)
                        .withRequestCode(L_FILE_PICKER_GET_FILE_REQUEST_CODE)
                        .start();
            }
        }
    }

    class MindEditImageListDataAddBtnItemClickCallBack implements MindEditImageDataAdapter.OutCallBack {
        @Override
        public void onAddClick() {
            Set<MimeType> mimeTypes = new HashSet<>();
            mimeTypes.add(MimeType.JPEG);
            mimeTypes.add(MimeType.PNG);
            addMedia(mimeTypes, false, ZHI_HU_GET_IMAGE_REQUEST_CODE);
        }
    }

}
