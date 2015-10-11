package cn.edu.dlut.tiyuguan.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.edu.dlut.tiyuguan.base.BaseModel;

/**
 * Created by asus on 2015/10/9.
 */
/**体育场实例**/
public class Sport extends BaseModel {
    /**数据库字段**/
    private String sportId;
    private String sportName;

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    private HashMap<String,Venues> venuesHashMap;

    private static  String[] venues_names = new String[]{"篮球馆","羽毛球馆","乒乓球馆","游泳馆","台球馆"};
    private static HashMap<String,Integer> venues_id_name = new HashMap<>();
    static {
        for(int i = 0; i < 5; i++){
            venues_id_name.put(venues_names[i],i + 1);
        }
    }

    public static int getVenuesId(String v_name){
        return venues_id_name.get(v_name);
    }


    public HashMap<String, Venues> getVenuesHashMap() {
        return venuesHashMap;
    }

    public void setVenuesHashMap(HashMap<String, Venues> venuesHashMap) {
        this.venuesHashMap = venuesHashMap;
    }


    //news
    private HashMap<String,News> newsMap;

    public HashMap<String, News> getNewsMap() {
        return newsMap;
    }

    public void setNewsMap(HashMap<String, News> newsMap) {
        this.newsMap = newsMap;
    }


    private static Sport sportInstance = new Sport();
    //single instance
    private Sport(){

    }
    public static Sport getInstance(){

        if(sportInstance == null){
            sportInstance = new Sport();
        }

        return sportInstance;
    }

}
