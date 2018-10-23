package itc.ink.explorefuture_android.login;

/**
 * Created by yangwenjiang on 2018/10/23.
 */

public class LoginStateInstance {
    public static final String STATE_ONLINE="ONLINE";
    public static final String STATE_OFFLINE="OFFLINE";
    private String id="";
    private String password="";
    private String login_state=STATE_OFFLINE;

    private static volatile LoginStateInstance loginStateInstance;

    private LoginStateInstance() {
    }

    public static LoginStateInstance getInstance() {
        if (loginStateInstance == null) {
            synchronized (LoginStateInstance.class) {
                if (loginStateInstance == null) {
                    loginStateInstance = new LoginStateInstance();
                }
            }
        }
        return loginStateInstance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin_state() {
        return login_state;
    }

    public void setLogin_state(String login_state) {
        this.login_state = login_state;
    }
}
