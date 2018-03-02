package base.zc.com.project.bean;

/**
 * Created by Darren on 2016/10/14.
 */

public class LoginEvent {

    private String member_name;

    public String getSession() {
        return session;
    }

    public String getMember_avatar() {
        return member_avatar;
    }

    public String getMember_name() {
        return member_name;
    }

    private String member_avatar;
    private String session;
    public LoginEvent(String member_name, String member_avatar, String session) {
        this.member_name = member_name;
        this.member_avatar = member_avatar;
        this.session = session;
    }

}
