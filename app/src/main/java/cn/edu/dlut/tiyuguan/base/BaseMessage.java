package cn.edu.dlut.tiyuguan.base;

/**
 * Created by asus on 2015/10/6.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * JSON字符串,封装成消息，提供消息转Model实例的能力
 */
public class BaseMessage {
    private int code;
    private String message;
    private String dataStr;

    private HashMap<String,BaseModel> dataMap;
    private HashMap<String,ArrayList<BaseModel>> dataMapList;

    public BaseMessage(){
        this.dataMap = new HashMap<>();
        this.dataMapList = new HashMap<>();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDataStr() {
        return dataStr;
    }

    /**将data属性中的json数据转为各种model**/
    public void setDataStr(String dataStr) throws Exception{
        this.dataStr = dataStr;
        //parse the dataStr
        if(dataStr.length() <= 0) return;

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(dataStr);
            Iterator<String> it = jsonObject.keys();
            while (it.hasNext()){
                String jsonKey = it.next();
                String modelName =  getModelName(jsonKey);
                String modelClassName = "cn.edu.dlut.tiyuguan.model." +modelName;
                JSONArray modelJsonArray = jsonObject.optJSONArray(jsonKey);
                //jsonObject
                if(modelJsonArray == null){
                   JSONObject modelJsonObj = jsonObject.optJSONObject(jsonKey);
                    if(modelJsonObj == null){
                        throw new Exception("message data is invalid!");
                    }
                    this.dataMap.put(jsonKey,json2Model(modelClassName,modelJsonObj));
                }//jsonArray
                else{
                    ArrayList<BaseModel> modelList = new ArrayList<>();
                    for(int i = 0 ; i < modelJsonArray.length(); i++){
                        JSONObject modelJsonObj = modelJsonArray.optJSONObject(i);
                        modelList.add(json2Model(modelClassName,modelJsonObj));
                    }
                    this.dataMapList.put(jsonKey,modelList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**根据名字得到model**/
    public BaseModel getData(String modelName){
        return this.dataMap.get(modelName);
    }
    /**根据名字得到很多model**/
    public ArrayList<BaseModel> getDataList(String modelName){
        return this.dataMapList.get(modelName);
    }
    private String getModelName(String jsonKey){
        return jsonKey;
    }

    /**json对象转model对象**/
    private BaseModel json2Model(String modelClassName,JSONObject modelJsonObject) throws Exception{
        BaseModel modelObj = (BaseModel)(Class.forName(modelClassName).newInstance());
        Class<? extends BaseModel> modelClass = modelObj.getClass();
        // auto-setting model fields
        Iterator<String> it = modelJsonObject.keys();
        while (it.hasNext()) {
            String varField = it.next();
            String varValue = modelJsonObject.getString(varField);
            Field field = modelClass.getDeclaredField(varField);//reflect useage
            field.setAccessible(true); // have private to be accessable
            field.set(modelObj, varValue);
        }
        return modelObj;
    }
}
