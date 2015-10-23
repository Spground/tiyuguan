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
        public static String login = "http://" + Host.HOST_ADDR + ":" + Host.HTTP_PORT + "/Gym/user/loginMobile.action";
        public static String queryUserRecord = "http://" + Host.HOST_ADDR +":" +Host.HTTP_PORT  +"/Gym/reserve/getReserve.action";
        public static String makeReserve = "http://" + Host.HOST_ADDR + ":" + Host.HTTP_PORT + "/Gym/reserve/makeReserve.action";
        public static String getTopNews = "http://" + Host.HOST_ADDR + ":" + Host.HTTP_PORT + "/Gym/news/getNews.action";
        public static String indexAction = "http://" + Host.HOST_ADDR + ":" + Host.HTTP_PORT + "/GymBook/indexinfo.action";

        public static String queryVenuesInfp = "http://" + Host.HOST_ADDR + ":" + Host.HTTP_PORT + "/Gym/venues/getVenuesInfo.action";
        public static String queryInvalidLocationInfo = "http://" + Host.HOST_ADDR + ":" + Host.HTTP_PORT + "/Gym/venues/queryInvalidLocation.action";

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

    public static class Host{
        public static String HOST_ADDR = "192.168.0.104";
//        public static String HOST_ADDR = "58.155.219.41";
        public static String HTTP_PORT = "8080";
    }
}
