package itc.ink.explorefuture_android.app.utils.dataupdate;

/**
 * Created by yangwenjiang on 2018/9/28.
 */

public class DataUpdateMode {
    public static final String ACCOUNT_ID_NEED_REPLACE="account_id_need_replace";
    //Datetime File Remote Url
    public static final String APP_UPDATE_DATETIME_FILE_URL="http://www.itc.ink/data/explorefuture_data/app/data_newest_update_date_time.json";
    public static final String ACCOUNT_UPDATE_DATETIME_FILE_URL="http://www.itc.ink/data/explorefuture_data/account/account_id_need_replace/data_newest_update_date_time.json";

    //Datetime File Newest Time Key
    public static final String RECOMMEND_HANDPICK_DATA_NEWEST_UPDATE_DATE_TIME_KEY = "recommend_handpick_data_newest_update_date_time";
    public static final String ACCOUNT_ATTENTION_DATA_NEWEST_UPDATE_DATE_TIME_KEY = "recommend_attention_data_newest_update_date_time";
    public static final String RECOMMEND_MIND_HOTTEST_DATA_NEWEST_UPDATE_DATE_TIME_KEY = "recommend_mind_hottest_data_newest_update_date_time";
    public static final String RECOMMEND_MIND_NEWEST_DATA_NEWEST_UPDATE_DATE_TIME_KEY = "recommend_mind_newest_data_newest_update_date_time";
    public static final String SORT_ALL_DATA_NEWEST_UPDATE_DATE_TIME_KEY="sort_all_data_newest_update_date_time";
    public static final String ACCOUNT_MIND_DATA_NEWEST_UPDATE_DATE_TIME_KEY="mind_data_newest_update_date_time";
    public static final String FIND_DATA_NEWEST_UPDATE_DATE_TIME_KEY="find_data_newest_update_date_time";

    //Data File Remote Url
    public static final String RECOMMEND_HANDPICK_DATA_FILE_URL = "http://www.itc.ink/data/explorefuture_data/app/recommend/handpick/data.json";
    public static final String ACCOUNT_ATTENTION_DATA_FILE_URL = "http://www.itc.ink/data/explorefuture_data/account/account_id_need_replace/attention/data.json";
    public static final String RECOMMEND_MIND_HOTTEST_DATA_FILE_URL = "http://www.itc.ink/data/explorefuture_data/app/recommend/mind/data_hottest.json";
    public static final String RECOMMEND_MIND_NEWEST_DATA_FILE_URL = "http://www.itc.ink/data/explorefuture_data/app/recommend/mind/data_newest.json";
    public static final String SORT_ALL_DATA_FILE_URL = "http://www.itc.ink/data/explorefuture_data/app/sort/all/data.json";
    public static final String ACCOUNT_MIND_DATA_FILE_URL = "http://www.itc.ink/data/explorefuture_data/account/account_id_need_replace/mind/data.json";
    public static final String FIND_DATA_FILE_URL = "http://www.itc.ink/data/explorefuture_data/app/find/data.json";


    //Local Data File Name
    public static final String RECOMMEND_HANDPICK_LOCAL_DATA_FILE_NAME = "recommend_handpick_data.json";
    public static final String RECOMMEND_ATTENTION_LOCAL_DATA_FILE_NAME = "recommend_attention_data.json";
    public static final String RECOMMEND_MIND_HOTTEST_LOCAL_DATA_FILE_NAME = "recommend_mind_data_hottest.json";
    public static final String RECOMMEND_MIND_NEWEST_LOCAL_DATA_FILE_NAME = "recommend_mind_data_newest.json";
    public static final String SORT_ALL_LOCAL_DATA_FILE_NAME = "sort_all_data.json";
    public static final String MIND_LOCAL_DATA_FILE_NAME = "mind_data.json";
    public static final String FIND_LOCAL_DATA_FILE_NAME = "find_data.json";
    public static final String MINE_LOCAL_DATA_FILE_NAME = "mine_data.json";

    //Catch Data Str
    public static String RECOMMEND_HANDPICK_JSON_DATA_STR="";
    public static String RECOMMEND_ATTENTION_JSON_DATA_STR="";
    public static String RECOMMEND_MIND_HOTTEST_JSON_DATA_STR="";
    public static String RECOMMEND_MIND_NEWEST_JSON_DATA_STR="";
    public static String SORT_ALL_JSON_DATA_STR="";
    public static String MIND_JSON_DATA_STR="";
    public static String FIND_JSON_DATA_STR="";
    public static String MINE_JSON_DATA_STR="";


    private String updateDatetimeFileUrl="";
    private String remoteDataFileUrl="";
    private String dataNewestUpdateDateTimeKey="";
    private String localDataFileName="";

    private boolean checkUpdateFinish=false;

    public DataUpdateMode() {
    }

    public DataUpdateMode(String updateDatetimeFileUrl, String remoteDataFileUrl, String dataNewestUpdateDateTimeKey, String localDataFileName) {
        this.updateDatetimeFileUrl = updateDatetimeFileUrl;
        this.remoteDataFileUrl = remoteDataFileUrl;
        this.dataNewestUpdateDateTimeKey = dataNewestUpdateDateTimeKey;
        this.localDataFileName = localDataFileName;
    }

    @Override
    public String toString() {
        return "DataUpdateMode{" +
                "updateDatetimeFileUrl='" + updateDatetimeFileUrl + '\'' +
                ", remoteDataFileUrl='" + remoteDataFileUrl + '\'' +
                ", dataNewestUpdateDateTimeKey='" + dataNewestUpdateDateTimeKey + '\'' +
                ", localDataFileName='" + localDataFileName + '\'' +
                ", checkUpdateFinish=" + checkUpdateFinish +
                '}';
    }

    public String getUpdateDatetimeFileUrl() {
        return updateDatetimeFileUrl;
    }

    public void setUpdateDatetimeFileUrl(String updateDatetimeFileUrl) {
        this.updateDatetimeFileUrl = updateDatetimeFileUrl;
    }

    public String getRemoteDataFileUrl() {
        return remoteDataFileUrl;
    }

    public void setRemoteDataFileUrl(String dataFileUrl) {
        this.remoteDataFileUrl = dataFileUrl;
    }

    public String getDataNewestUpdateDateTimeKey() {
        return dataNewestUpdateDateTimeKey;
    }

    public void setDataNewestUpdateDateTimeKey(String dataNewestUpdateDateTimeKey) {
        this.dataNewestUpdateDateTimeKey = dataNewestUpdateDateTimeKey;
    }

    public String getLocalDataFileName() {
        return localDataFileName;
    }

    public void setLocalDataFileName(String localDataFileName) {
        this.localDataFileName = localDataFileName;
    }

    public boolean isCheckUpdateFinish() {
        return checkUpdateFinish;
    }

    public void setCheckUpdateFinish(boolean checkUpdateFinish) {
        this.checkUpdateFinish = checkUpdateFinish;
    }
}
