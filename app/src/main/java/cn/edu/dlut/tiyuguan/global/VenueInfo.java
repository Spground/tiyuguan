package cn.edu.dlut.tiyuguan.global;

import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//场馆的剩余数量
public class VenueInfo {
	//全局的体育场地各个时间段数量的信息
	//String 日期，Integer 场馆id,Integer 时间段id;
	private static HashMap<String,HashMap<Integer, HashMap<Integer, Integer>>> venueInfo = new HashMap();

	//根据场馆id 和 查询的日期 得到剩余的数量
	public static HashMap<Integer, Integer> getRemainingQuantity(int venueid,String date) {
		return venueInfo.get(date).get(venueid);
	}
	
	//服务器传过来的只有一个json对象
	public static void setVenueInfo(String responseFromService) {
		//调用解析json的方法
		paresJson(responseFromService);
	}
	//
	public static void setVenueInfo(JSONArray jArray) {
	}
	//返回可用日期
	public static  String[] getAvailableDate() {
		String[] temp = new String[3];
		Set<String> key = venueInfo.keySet();
		Object[] object=key.toArray();
		for(int i = 0;i < key.size();i++) {
			temp[i] = (String)object[i];
		}
		return temp;
		
	}
	
	//解析josn
    private static void paresJson(String str) {
    	//场馆id和hashMap的映射
    	HashMap<Integer, HashMap<Integer, Integer>> hashMap_id_hashMap = new HashMap();

		try {
				JSONObject jsonObj = new JSONObject(str);
				//取得时间
				String date = jsonObj.getString("date");
			    //获取venues_infojson对象数组
				JSONArray jsonArrayVenuesInfo = jsonObj.getJSONArray("venues_info");
				//取得数组的内容
				for(int i = 0;i < jsonArrayVenuesInfo.length();i++) {
					//时间和剩余数量的映射 注意：每一次都要实例化
			    	HashMap<Integer, Integer> hashMap_time_count = new HashMap<Integer, Integer>();
					JSONObject temp = (JSONObject)jsonArrayVenuesInfo.opt(i);

					int venuesId = temp.getInt("venues_id");//体育场的id
					int sum = temp.getInt("sum");	//总的数量

					JSONArray times = temp.getJSONArray("times");
					JSONArray order_count = temp.getJSONArray("order_count");//得到剩余的场地数量

					for(int j=0;j<times.length();j++) {
						//开放时间的整点
						int clock = Integer.parseInt(times.opt(j).toString());
					   //已经预定的数量
						int order = Integer.parseInt(order_count.opt(j).toString());
						//剩下的数量
						int leftCount = sum-order;
						hashMap_time_count.put(clock,leftCount );
			        }
					hashMap_id_hashMap.put(venuesId, hashMap_time_count);
			     }
				//最终的set结果
				venueInfo.put(date, hashMap_id_hashMap);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			
		}
	
}
