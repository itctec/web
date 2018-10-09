package itc.ink.explorefuture_android.app.utils.dataupdate;

/**
 * Created by yangwenjiang on 2018/9/28.
 */

public class DataUpdateMode {
    public static final String ACCOUNT_ID_NEED_REPLACE="account_id_need_replace";
    public static final String APP_UPDATE_DATETIME_FILE_URL="http://www.itc.ink/data/explorefuture_data/app/data_newest_update_date_time.json";
    public static final String ACCOUNT_UPDATE_DATETIME_FILE_URL="http://www.itc.ink/data/explorefuture_data/account/account_id_need_replace/data_newest_update_date_time.json";

    public static final String RECOMMEND_HANDPICK_DATA_FILE_URL = "http://www.itc.ink/data/explorefuture_data/app/recommend/handpick/data.json";
    public static final String ACCOUNT_ATTENTION_DATA_FILE_URL = "http://www.itc.ink/data/explorefuture_data/account/account_id_need_replace/attention/data.json";
    public static final String RECOMMEND_MIND_DATA_FILE_URL = "http://www.itc.ink/data/explorefuture_data/app/recommend/mind/data.json";

    public static final String RECOMMEND_HANDPICK_DATA_NEWEST_UPDATE_DATE_TIME_KEY = "recommend_handpick_data_newest_update_date_time";
    public static final String ACCOUNT_DATA_NEWEST_UPDATE_DATE_TIME_KEY = "recommend_attention_data_newest_update_date_time";
    public static final String RECOMMEND_MIND_DATA_NEWEST_UPDATE_DATE_TIME_KEY = "recommend_mind_data_newest_update_date_time";

    public static final String RECOMMEND_HANDPICK_LOCAL_DATA_FILE_NAME = "recommend_handpick_data.json";
    public static final String RECOMMEND_ATTENTION_LOCAL_DATA_FILE_NAME = "recommend_attention_data.json";
    public static final String RECOMMEND_MIND_LOCAL_DATA_FILE_NAME = "recommend_mind_data.json";
    public static final String SORT_LOCAL_DATA_FILE_NAME = "sort_data.json";
    public static final String MIND_LOCAL_DATA_FILE_NAME = "mind_data.json";
    public static final String FIND_LOCAL_DATA_FILE_NAME = "find_data.json";
    public static final String MINE_LOCAL_DATA_FILE_NAME = "mine_data.json";


    private String updateDatetimeFileUrl="";
    private String remoteDataFileUrl="";
    private String dataNewestUpdateDateTimeKey="";
    private String localDataFileName="";

    private boolean checkUpdateFinish=false;

    public DataUpdateMode() {
    }

    public DataUpdateMode(String updateDatetimeFileUrl, String remoteDataFileUrl, String dataNewestUpdateDateTimeKey, String localDataFileName, boolean checkUpdateFinish) {
        this.updateDatetimeFileUrl = updateDatetimeFileUrl;
        this.remoteDataFileUrl = remoteDataFileUrl;
        this.dataNewestUpdateDateTimeKey = dataNewestUpdateDateTimeKey;
        this.localDataFileName = localDataFileName;
        this.checkUpdateFinish = checkUpdateFinish;
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
