package co.cyberz.fox.support.reactnative;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import org.json.JSONObject;

import java.util.HashMap;

import co.cyberz.fox.Fox;
import co.cyberz.fox.service.FoxEvent;

/**
 * FoxEventを作るクラス
 * Created by Garhira on 2017/10/24.
 */
public class FoxEventModule extends ReactContextBaseJavaModule {

    private static final String NAME      = "eventName";
    private static final String ID        = "ltvId";
    private static final String PRICE     = "price";
    private static final String SKU       = "sku";
    private static final String CURRENCY  = "currency";
    private static final String BUID      = "buid";
    private static final String VALUE     = "value";
    private static final String ORDER_ID  = "orderId";
    private static final String ITEM_NAME = "itemName";
    private static final String QUANTITY  = "quantity";
    private static final String EXTRA_INFO = "extraInfo";
    private static final String EVENT_INFO  = "eventInfo";

    public FoxEventModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "CYZFoxEvent";
    }

    /**
     * イベント計測の必須情報をセット
     * @param map
     */
    @ReactMethod
    public void track(ReadableMap map) {
        if (map == null) {
            Log.e("FOX-Event", "Event description must be not null!!!");
            return;
        } else if (!map.hasKey(NAME)) {
            Log.e("FOX-Event", "Event name is require parameter!!");
            return;
        }
        ReactFoxEvent event;
        if (map.hasKey(ID)) {
            event = new ReactFoxEvent(map.getString(NAME), map.getInt(ID));
        } else {
            event = new ReactFoxEvent(map.getString(NAME));
        }
        if (map.hasKey(PRICE)) event.price = map.getDouble(PRICE);
        if (map.hasKey(SKU)) event.sku = map.getString(SKU);
        if (map.hasKey(CURRENCY)) event.currency = map.getString(CURRENCY);
        if (map.hasKey(BUID)) event.buid = map.getString(BUID);
        if (map.hasKey(VALUE)) event.value = map.getInt(VALUE);
        if (map.hasKey(ORDER_ID)) event.orderId = map.getString(ORDER_ID);
        if (map.hasKey(ITEM_NAME)) event.itemName = map.getString(ITEM_NAME);
        if (map.hasKey(QUANTITY)) event.quantity = map.getInt(QUANTITY);
        if (map.hasKey(EXTRA_INFO)) event.addExtraInfo(map.getMap(EXTRA_INFO));

        if (map.hasKey(EVENT_INFO)) {
            try {
                HashMap<String, Object> eventInfoHashMap = map.getMap(EVENT_INFO).toHashMap();
                if (eventInfoHashMap != null && 0 < eventInfoHashMap.size())
                    event.eventInfo = new JSONObject(eventInfoHashMap);
            } catch (Throwable t) {
                Log.e("FOX-Event", "setEventInfo", t);
            }
        }
        Fox.trackEvent(event);
    }

    private class ReactFoxEvent extends FoxEvent {

        public ReactFoxEvent(String eventName) {
            super(eventName);
        }

        public ReactFoxEvent(String eventName, int ltvId) {
            super(eventName, ltvId);
        }

        public void addExtraInfo(ReadableMap extraInfoMap) {
            if (extraInfo == null) {
                extraInfo = new HashMap<String, String>();
            }
            extraInfo.putAll(FoxReactUtil.toHashMap(extraInfoMap));
        }
    }

}
