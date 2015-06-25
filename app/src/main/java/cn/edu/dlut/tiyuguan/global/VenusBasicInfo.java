package cn.edu.dlut.tiyuguan.global;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class VenusBasicInfo {
	private static HashMap<String, String> VenuesInfo=null;
	//得到今天明天后天详细的日期
	public static HashMap<String,String> getOrderDate()
	{
		HashMap<String, String> temp=new HashMap<String, String>();
		//获取当前时间
		//取系统当前时间
		String today,tomorrow,theDayAfterTomorrow;
		Calendar calendar0=Calendar.getInstance();
		Calendar calendar1=Calendar.getInstance();
		Calendar calendar2=Calendar.getInstance();
		calendar1.add(Calendar.DATE, 1);
		calendar2.add(Calendar.DATE, 2);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		today=formatter.format(calendar0.getTime());
		tomorrow=formatter.format(calendar1.getTime());
		theDayAfterTomorrow=formatter.format(calendar2.getTime());
		
		temp.put("Today",today);
		temp.put("Tomorrow", tomorrow);
		temp.put("TheDayAfterTomorrow",theDayAfterTomorrow);
		return temp;
	}
	//得到篮球馆的详细信息
	public static  HashMap<String,String> getBasketBallVenusInfo()
	{
		HashMap<String, String> temp=new HashMap<String, String>();
		temp.put("title", "篮球馆");
		temp.put("openTime", "每天早上九点到晚上十点，节假日另行通知");
		temp.put("chargeStandar", "普通12元/小时，会员10元/小时");
		return temp;
	}
	//得到游泳馆的详细信息
	public static  HashMap<String,String> getSwimingVenusInfo()
	{
		HashMap<String, String> temp=new HashMap<String, String>();
		temp.put("title", "游泳馆");
		temp.put("openTime", "每天早上九点到晚上十点，节假日另行通知");
		temp.put("chargeStandar", "普通10元/小时，会员8元/小时");
		return temp;
	}
	//得到台球馆的详细信息
	public static  HashMap<String,String> getTaiQiuVenusInfo()
	{
		HashMap<String, String> temp=new HashMap<String, String>();
		temp.put("title", "台球馆");
		temp.put("openTime", "每天早上九点到晚上十点，节假日另行通知");
		temp.put("chargeStandar", "普通6元/小时，会员4元/小时");
		return temp;
	}
	//得到羽毛球馆的详细信息
	public static  HashMap<String,String> getBadmintonVenusInfo()
	{
		HashMap<String, String> temp=new HashMap<String, String>();
		temp.put("title", "羽毛球馆");
		temp.put("openTime", "每天早上九点到晚上十点，节假日另行通知");
		temp.put("chargeStandar", "普通15元/小时，会员12元/小时");
		return temp;
	}
	//得到乒乓球馆的详细信息
	public static  HashMap<String,String> getTableTennisVenusInfo()
	{
		HashMap<String, String> temp=new HashMap<String, String>();
		temp.put("title", "乒乓球馆");
		temp.put("openTime", "每天早上九点到晚上十点，节假日另行通知");
		temp.put("chargeStandar", "普通5元/小时，会员4元/小时");
		return temp;
	}
	//场馆的信息
	public static void setVenuesInfo(HashMap<String, String> temp)
	{
		VenuesInfo=temp;
	}
	//取信息
	public static HashMap<String, String> getVenuesInfo()
	{
		//注意是否为null
		  return VenuesInfo;
		
	}
	
}
