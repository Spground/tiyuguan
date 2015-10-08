package cn.edu.dlut.tiyuguan.global;

/**
 * Created by asus on 2015/10/6.
 * all constants are here
 */
public class NameConstant {
    public static final String SHARED_PREFERENCES_NAME = "cn.edu.dlut.tiyuguan.basicinfo";

    //the api info of remote server
    public static class api{
        //url of the login
        public static String login = "http://192.168.0.104:8080/GymBook/loginmobile.action";
    }

    public static class task{
        public static int login = 1000;
    }

    public static class dir{
        public static String root = "GymBook";
    }

    public static class message_event_code{
        public static String MsgEvntCode_Login = "1000";
    }

    public static class debug{
        public static boolean Debug_Mode = true;
    }

    public static class ErrorCode{
        /**error code like "err4xx" **/
        public static String Network_Error="err400";
    }
}
