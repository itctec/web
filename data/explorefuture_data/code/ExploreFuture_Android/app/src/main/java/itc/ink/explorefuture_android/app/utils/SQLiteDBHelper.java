package itc.ink.explorefuture_android.app.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yangwenjiang on 2018/10/15.
 */

public class SQLiteDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_FILE_NAME="local_db.db3";
    public static final int DATABASE_VERSION=1;
    private final String TB_CREATE_LOGIN_INFO="create table tb_login_info(id primary key,password,login_state)";
    private final String TB_CREATE_PERSON_INFO="create table tb_person_info(id,nickname,personalized_signature,fans_count,attention_count,head_portrait_image_url,head_portrait_image_update_datetime,personal_cover_bg_image_url,personal_cover_bg_image_update_datetime)";
    private final String TB_CREATE_SEARCH_HISTORY="create table tb_search_history(_id integer primary key autoincrement,search_content,search_datetime,UNIQUE(search_content))";

    public SQLiteDBHelper(Context context,String name,int version){
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TB_CREATE_LOGIN_INFO);
        sqLiteDatabase.execSQL(TB_CREATE_PERSON_INFO);
        sqLiteDatabase.execSQL(TB_CREATE_SEARCH_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
