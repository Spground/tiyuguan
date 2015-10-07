package cn.edu.dlut.tiyuguan.global;

public class LoginInfo {

  private static Boolean isLogin = false;
  public static String userID = "";
  @SuppressWarnings("unused")
  private static String passWord = "";
  public static String sessionID = "";//当前登陆的会话ID
  public static Boolean rememberme;
	public static Boolean getLoginState() {
		return isLogin;
}
	public static void settLoginState(Boolean isLogin) {
		LoginInfo.isLogin = isLogin;
	}
	public static void settLoginInfo(String userid,String password,Boolean rememberme0) {
	   userID = userid;
	   passWord = password;
	   rememberme = rememberme0;
	}
	public static String getLoginUserId() {
		return userID;
	}
	
}
