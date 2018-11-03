package itc.ink.explorefuture_android.common_unit.search_bar;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.utils.SQLiteDBHelper;
import itc.ink.explorefuture_android.common_unit.content_list.ContentListActivity;
import itc.ink.explorefuture_android.common_unit.search_result.SearchResultActivity;

/**
 * Created by yangwenjiang on 2018/10/19.
 */

public class SearchBar extends ConstraintLayout {
    private ConstraintLayout searchBarLayout;
    private EditText searchBarEdit;
    private ImageView searchBarClearBtn;
    private TextView searchBarCancelBtn;
    private ConstraintLayout searchHistoryLayout;
    private RecyclerView searchHistoryRecyclerView;
    private ArrayList<String> mSearchHistoryListData;
    private SearchHistoryDataAdapter mHistoryDataAdapter;

    private SQLiteDBHelper sqLiteDBHelper;

    private int searchType=0;

    private OutCallBack outCallBack;

    public SearchBar(Context context) {
        this(context, null);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchBar);
        String hintText="";
        if (typedArray != null) {
            searchType=typedArray.getInt(R.styleable.SearchBar_type,0);
            hintText=typedArray.getString(R.styleable.SearchBar_search_box_hint);
            typedArray.recycle();
        }

        LayoutInflater.from(context).inflate(R.layout.common_unit_search_bar, this);

        //Search Bar
        searchBarLayout = findViewById(R.id.search_Bar_Layout);
        searchBarEdit = findViewById(R.id.search_Bar_Edit);
        searchBarEdit.setHint(hintText);
        searchBarEdit.addTextChangedListener(new SearchBarEditTextWatcher());
        searchBarEdit.setOnFocusChangeListener(new SearchBarEditFocusChangedListener());
        searchBarEdit.setOnEditorActionListener(new SearchBarEditActionListener());
        searchBarClearBtn = findViewById(R.id.search_Bar_Clear_Btn);
        searchBarClearBtn.setOnClickListener(new SearchBarClearBtnClickListener());
        searchBarCancelBtn = findViewById(R.id.search_Bar_Cancel_Btn);
        searchBarCancelBtn.setOnClickListener(new SearchBarCancelBtnClickListener());
        searchHistoryLayout = findViewById(R.id.search_History_Layout);
        searchHistoryRecyclerView = findViewById(R.id.search_History_RecyclerView);
        RecyclerView.LayoutManager historyRvLayoutManager = new LinearLayoutManager(getContext());
        searchHistoryRecyclerView.setLayoutManager(historyRvLayoutManager);
    }

    public void setOutCallBack(OutCallBack outCallBack) {
        this.outCallBack = outCallBack;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    class SearchBarEditFocusChangedListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (b) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) searchBarLayout.getLayoutParams();
                layoutParams.rightMargin = dip2px(getContext(), 45);
                searchBarLayout.setLayoutParams(layoutParams);
                searchBarCancelBtn.setTranslationX(dip2px(getContext(), -45) / 2 - searchBarCancelBtn.getWidth() / 2);
                searchHistoryLayout.setVisibility(View.VISIBLE);
                outCallBack.onSearchFocusChange(true);

                sqLiteDBHelper = new SQLiteDBHelper(getContext(), SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
                mSearchHistoryListData = new ArrayList<>();
                mHistoryDataAdapter = new SearchHistoryDataAdapter(getContext(), mSearchHistoryListData);
                searchHistoryRecyclerView.setAdapter(mHistoryDataAdapter);

                String sqlStr = "select * from tb_search_history order by search_datetime desc";
                Cursor cursor = sqLiteDBHelper.getReadableDatabase().rawQuery(sqlStr, new String[]{});

                mSearchHistoryListData.clear();
                while (cursor.moveToNext()) {
                    mSearchHistoryListData.add(cursor.getString(1));
                }

                if (mSearchHistoryListData.size() > 0) {
                    mSearchHistoryListData.add(getContext().getString(R.string.search_clear_history_text));
                }else{
                    mSearchHistoryListData.add(getContext().getString(R.string.search_history_empty_text));
                }

                mHistoryDataAdapter.notifyDataSetChanged();

            }
        }
    }

    class SearchBarEditTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() > 0) {
                searchBarClearBtn.setVisibility(View.VISIBLE);
            } else {
                searchBarClearBtn.setVisibility(View.GONE);
            }

            String sqlStr = "select * from tb_search_history where search_content like '%" + charSequence.toString() + "%' order by search_datetime desc";
            Cursor cursor = sqLiteDBHelper.getReadableDatabase().rawQuery(sqlStr, new String[]{});

            mSearchHistoryListData.clear();
            while (cursor.moveToNext()) {
                mSearchHistoryListData.add(cursor.getString(1));
            }

            if (mSearchHistoryListData.size() > 0) {
                mSearchHistoryListData.add(getContext().getString(R.string.search_clear_history_text));
            }else{
                mSearchHistoryListData.add(getContext().getString(R.string.search_history_empty_text));
            }

            mHistoryDataAdapter.notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    class SearchBarEditActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_SEARCH ||
                    (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String sqlStr = "insert or replace into tb_search_history values(null,?,?)";
                sqLiteDBHelper.getReadableDatabase().execSQL(sqlStr, new String[]{textView.getText().toString().trim(), simpleDateFormat.format(new Date())});

                //Search Result Activity
                if(searchType==0){
                    Intent intent =new Intent(getContext(), ContentListActivity.class);
                    intent.putExtra(ContentListActivity.KEY_SORT_TITLE,textView.getText().toString());
                    getContext().startActivity(intent);
                }else{
                    Intent intent =new Intent(getContext(), SearchResultActivity.class);
                    intent.putExtra(SearchResultActivity.KEY_SEARCH_TITLE,textView.getText().toString());
                    getContext().startActivity(intent);
                }

                String sqlQueryStr = "select * from tb_search_history where search_content like '%" + searchBarEdit.getText().toString() + "%' order by search_datetime desc";
                Cursor cursor = sqLiteDBHelper.getReadableDatabase().rawQuery(sqlQueryStr, new String[]{});

                mSearchHistoryListData.clear();
                while (cursor.moveToNext()) {
                    mSearchHistoryListData.add(cursor.getString(1));
                }

                if (mSearchHistoryListData.size() > 0) {
                    mSearchHistoryListData.add(getContext().getString(R.string.search_clear_history_text));
                }else{
                    mSearchHistoryListData.add(getContext().getString(R.string.search_history_empty_text));
                }

                mHistoryDataAdapter.notifyDataSetChanged();
            }
            return false;
        }
    }

    class SearchBarClearBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            searchBarEdit.setText("");
        }
    }

    class SearchBarCancelBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            searchBarEdit.setText("");

            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean isOpen = imm.isActive();
            if (isOpen) {
                imm.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            searchBarEdit.clearFocus();

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) searchBarLayout.getLayoutParams();
            layoutParams.rightMargin = dip2px(getContext(), 10);
            searchBarLayout.setLayoutParams(layoutParams);
            searchBarCancelBtn.setTranslationX(0);
            searchHistoryLayout.setVisibility(View.GONE);

            outCallBack.onSearchFocusChange(false);

            if (sqLiteDBHelper != null) {
                sqLiteDBHelper.close();
            }

            if (mSearchHistoryListData != null) {
                mSearchHistoryListData = null;
            }
        }
    }

    public interface OutCallBack {
        void onSearchFocusChange(boolean focused);
    }
}
