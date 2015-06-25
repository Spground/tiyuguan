package cn.edu.dlut.tiyuguan.global;

import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//场馆的剩余数量
public class VenusInfo {

	//全局的体育场地各个时间段数量的信息
	//String 日期，Integer 场馆id,Integer 时间段id;
	private static HashMap<String,HashMap<Integer, HashMap<Integer, Integer>>> venusInfo=new HashMap<String, HashMap<Integer,HashMap<Integer,Integer>>>();
	
	public static HashMap<Integer, Integer> getRemainingQuantity(int venusid,String date)
	{
		//初始化hashmap
		HashMap<Integer, Integer> temp=null;
		//返回根据日期，和，场馆id的hashmap
		if(venusInfo!=null)
		   temp=venusInfo.get(date).get(venusid);
		return temp;
	}
	
	
	//服务器闯过来的只有一个json对象
	public static void setVenusInfo(String responseFromService)
	{
		//调用解析json的方法
		paresJson(responseFromService);
		
	}
	//
	public static void setVenusInfo(JSONArray jArray)
	{
		
		
	
	}
	//返回可用日期
	public static  String[] getAvailableDate()
	{
		String[] temp=new String[3];
		Set<String> key=venusInfo.keySet();
		 Object[] object=key.toArray();
		for(int i=0;i<key.size();i++)
		{
			temp[i]=(String)object[i];
		}
		return temp;
		
	}
	
	//解析josn的方法私有的++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private static void paresJson(String str)
	{
		
    	//场馆id和hashMap的映射
    	HashMap<Integer, HashMap<Integer, Integer>> hashMap_id_hashMap=new HashMap<Integer, HashMap<Integer,Integer>>();
    	
		try {
				//全部字符串的json对象
				JSONObject jsonObj=new JSONObject(str);
				//取得时间
				String date=jsonObj.getString("date");
			    //获取venues_infojson对象数组
				JSONArray jsonArrayVenuesInfo=jsonObj.getJSONArray("venues_info");
				//取得数组的内容
				//String info=date+"\n";
				for(int i=0;i<jsonArrayVenuesInfo.length();i++)
				{
					//时间和剩余数量的映射 注意：每一次都要实例化
			    	HashMap<Integer, Integer> hashMap_time_count=new HashMap<Integer, Integer>();
					JSONObject temp=(JSONObject)jsonArrayVenuesInfo.opt(i);
					//体育场的id
					int venuesId=temp.getInt("venues_id");
					//总的数量
					int sum=temp.getInt("sum");
					//String venuesId=temp.getString("venues_id");
					//info+=venuesId+"\t"+venuesName+"\t";
					JSONArray times=temp.getJSONArray("times");
					//得到剩余的场地数量
					JSONArray order_count=temp.getJSONArray("order_count");
					for(int j=0;j<times.length();j++)
					{
						//开放时间的整点
						int clock=Integer.parseInt(times.opt(j).toString());
						//info+=clock+":00到"+(clock+1)+":00\t";	
					//已经预定的数量
						int order=Integer.parseInt(order_count.opt(j).toString());
						//剩下的数量
						int leftCount=sum-order;
						//info+=sum+"剩余"+leftCount+"\n";
						hashMap_time_count.put(clock,leftCount );
			        }
					hashMap_id_hashMap.put(venuesId, hashMap_time_count);
					
			     }
				//返回的结果
				//最终的set结果
				venusInfo.put(date, hashMap_id_hashMap);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			
		}
	
}
