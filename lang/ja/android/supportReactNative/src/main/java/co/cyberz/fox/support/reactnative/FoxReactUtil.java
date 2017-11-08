package co.cyberz.fox.support.reactnative;

import android.text.TextUtils;
import android.util.Log;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Garhira on 2017/10/25.
 */
public class FoxReactUtil {

    /**
     * HashMap<String, String>に変換する
     * @param map ReadableMap
     * @return HashMap<String, String>
     */
    public static HashMap<String, String> toHashMap(ReadableMap map) {
        HashMap<String, String> extraMap = new HashMap<String, String>();
        for (Map.Entry<String, Object> extraData : map.toHashMap().entrySet()) {
            try {
                String extraValue = null;
                Object value = extraData.getValue();
                if (value instanceof String) {
                    extraValue = (String) value;
                } else if (value instanceof Integer) {
                    extraValue = String.valueOf((int) value);
                } else if (value instanceof Boolean) {
                    extraValue = String.valueOf((boolean) value);
                } else if (value instanceof Double) {
                    extraValue = String.valueOf((double) value);
                }
                if (!TextUtils.isEmpty(extraValue)) extraMap.put(extraData.getKey(), extraValue);
            } catch (Throwable e) {
                Log.e("FOX-ReactUtil", extraData.getKey() + "'s value is illegal!!");
            }
        }
        return extraMap;
    }

    /**
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static WritableMap convertJsonToMap(JSONObject jsonObject) throws JSONException {
        WritableMap map = new WritableNativeMap();

        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                map.putMap(key, convertJsonToMap((JSONObject) value));
            } else if (value instanceof  JSONArray) {
                map.putArray(key, convertJsonToArray((JSONArray) value));
            } else if (value instanceof  Boolean) {
                map.putBoolean(key, (Boolean) value);
            } else if (value instanceof  Integer) {
                map.putInt(key, (Integer) value);
            } else if (value instanceof  Double) {
                map.putDouble(key, (Double) value);
            } else if (value instanceof String)  {
                map.putString(key, (String) value);
            } else {
                map.putString(key, value.toString());
            }
        }
        return map;
    }

    /**
     *
     * @param jsonArray
     * @return
     * @throws JSONException
     */
    public static WritableArray convertJsonToArray(JSONArray jsonArray) throws JSONException {
        WritableArray array = new WritableNativeArray();

        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                array.pushMap(convertJsonToMap((JSONObject) value));
            } else if (value instanceof  JSONArray) {
                array.pushArray(convertJsonToArray((JSONArray) value));
            } else if (value instanceof  Boolean) {
                array.pushBoolean((Boolean) value);
            } else if (value instanceof  Integer) {
                array.pushInt((Integer) value);
            } else if (value instanceof  Double) {
                array.pushDouble((Double) value);
            } else if (value instanceof String)  {
                array.pushString((String) value);
            } else {
                array.pushString(value.toString());
            }
        }
        return array;
    }
}
