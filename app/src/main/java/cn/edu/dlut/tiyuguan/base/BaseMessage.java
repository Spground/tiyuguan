package cn.edu.dlut.tiyuguan.base;

/**
 * Created by asus on 2015/10/6.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * JSON字符串,封装成消息，提供消息转Model实例的能力
 */
public class BaseMessage {
    private int code;
    private String message;
    private String dataStr;

    private HashMap<String, BaseModel> dataMap;
    private HashMap<String, ArrayList<BaseModel>> dataMapList;

    public BaseMessage() {
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

    /**
     * 将data属性中的json数据转为各种model
     **/
    public void setDataStr(String dataStr, String modelClassSimleName) throws Exception {
        this.dataStr = dataStr;
        //parse the dataStr
        if (dataStr.length() <= 0) return;

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(dataStr);
            Iterator<String> it = jsonObject.keys();
            while (it.hasNext()) {
                String jsonKey = it.next();
                String modelClassName = "cn.edu.dlut.tiyuguan.model." + modelClassSimleName;
                JSONArray modelJsonArray = jsonObject.optJSONArray(jsonKey);
                //jsonObject
                if (modelJsonArray == null) {
                    JSONObject modelJsonObj = jsonObject.optJSONObject(jsonKey);
                    if (modelJsonObj == null) {
                        throw new Exception("message data is invalid!");
                    }
                    this.dataMap.put(modelClassSimleName, json2Model(modelClassName, modelJsonObj));
                }//jsonArray
                else {
                    ArrayList<BaseModel> modelList = new ArrayList<>();
                    for (int i = 0; i < modelJsonArray.length(); i++) {
                        JSONObject modelJsonObj = modelJsonArray.optJSONObject(i);
                        modelList.add(json2Model(modelClassName, modelJsonObj));
                    }
                    this.dataMapList.put(modelClassSimleName, modelList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据名字得到model
     **/
    public BaseModel getData(String modelName) {
        return this.dataMap.get(modelName);
    }

    /**
     * 根据名字得到很多model
     **/
    public ArrayList<? extends BaseModel> getDataList(String modelName) {
        return this.dataMapList.get(modelName);
    }

    /**
     * TODO 需要考虑完整
     **/
    public BaseModel json2Model(String modelClassFullName, JSONObject modelJsonObject) throws Exception {
        BaseModel modelObj;
        try {
            modelObj = (BaseModel) (Class.forName(modelClassFullName).newInstance());
        } catch (InstantiationException e) {
            throw e;
        } catch (IllegalAccessException e) {
            /**单例模式的情况**/
            Object[] objects = null;
            Class<?>[] classes = null;

            Method getInstanceMethod = Class.forName(modelClassFullName).getDeclaredMethod("getInstance", classes);
            modelObj = (BaseModel) getInstanceMethod.invoke(null, objects);
        } catch (ClassNotFoundException e) {
            throw e;
        }
        /**得到这个实例的类对象**/
        Class<? extends BaseModel> modelClass = modelObj.getClass();
        // auto-setting model fields
        Iterator<String> it = modelJsonObject.keys();
        while (it.hasNext()) {
            String jsonKey = it.next();
            String jsonValue = modelJsonObject.getString(jsonKey);
            Field field = modelClass.getDeclaredField(jsonKey);//reflect useage
            field.setAccessible(true); // have private to be accessible

            //根据域类型来赋值
            String filedTypeName = field.getType().getSimpleName().trim();
            switch (filedTypeName) {
                case "int" :
                    field.set(modelObj, Integer.valueOf(jsonValue));
                    break;
                case "long" :
                    field.set(modelObj, Long.valueOf(jsonValue));
                    break;
                case "double" :
                    field.set(modelObj, Double.valueOf(jsonValue));
                    break;
                case "boolean" :
                    field.set(modelObj, Boolean.valueOf(jsonValue));
                    break;
                case "float" :
                    field.set(modelObj, Float.valueOf(jsonValue));
                    break;
                default:
                    field.set(modelObj, jsonValue);
                    break;
            }

        }
        return modelObj;
    }

    /**
     * json对象转为HashMap
     **/
    private Map<String, String> jsonToMap(JSONObject jsonObject) {
        HashMap<String, String> hashMap = new HashMap<>();
        Iterator<String> it = jsonObject.keys();
        while (it.hasNext()) {
            String jsonKey = it.next();
            try {
                hashMap.put(jsonKey, jsonObject.getString(jsonKey));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return hashMap;
    }

    /**
     * 消息是否成功
     **/
    public boolean isSuccessful() {

        boolean success;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(message);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        success = jsonToMap(jsonObject).get("result").equals("success") ? true : false;
        return success;
    }
}
