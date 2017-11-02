package co.cyberz.fox.support.reactnative;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import org.json.JSONObject;

import java.util.HashMap;

import co.cyberz.fox.Fox;
import co.cyberz.fox.FoxTrackOption;

/**
 * Created by z00066 on 2017/10/18.
 */

/**
 * Created by Garhira on 2017/10/20.
 */
public class FoxModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private FoxTrackOption option;

    public FoxModule(ReactApplicationContext reactContext) {
        super(reactContext);
        option = new FoxTrackOption();
        reactContext.addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return "CYZFox";
    }

    @ReactMethod
    public void addOptionRedirectUrl(String url) {
        if (!TextUtils.isEmpty(url)) option.addRedirectUrl(url);
    }

    @ReactMethod
    public void addOptionBuid(String buid) {
        if (!TextUtils.isEmpty(buid)) option.addBuid(buid);
    }

    @ReactMethod
    public void setTrackingStateListener(final Callback onComplete) {
        option.setTrackingStateListener(new FoxTrackOption.TrackingStateListener() {
            @Override
            public void onComplete() {
                if (onComplete != null) onComplete.invoke();
            }
        });
    }

    @ReactMethod
    public void registerDeeplinkHandler(final long duration, final Callback deeplinkCallback) {
        option.registerDeeplinkHandler(new FoxTrackOption.DeeplinkHandler() {

            @Override
            public long getDuration() {
                if (0l < duration) {
                    return duration;
                }
                return super.getDuration();
            }

            @Override
            public void onReceived(Context context, @Nullable JSONObject deeplinkInfo) {
                if (deeplinkInfo != null) {
                    try {
                        if (deeplinkCallback != null)
                            deeplinkCallback.invoke(FoxReactUtil.convertJsonToMap(deeplinkInfo));
                    } catch (Exception e) {
                        Log.e("FOX", "registerDeeplinkHandler", e);
                    }
                }
            }
        });
    }

    @ReactMethod
    public void trackInstall() {
        Fox.trackInstall(option);
    }

    @ReactMethod
    public void trackSession() {
        Fox.trackSession();
    }

    @ReactMethod
    public void setUserInfo(ReadableMap userInfoMap) {
        if (userInfoMap.hasKey("redirectURL")) userInfoMap.getString("userInfoMap.");
        try {
            HashMap<String, String> userInfo = FoxReactUtil.toHashMap(userInfoMap);
            Fox.setUserInfo(new JSONObject(userInfo));
        } catch (Throwable t) {
            Log.e("FOX", "setUserInfo", t);
        }
    }

    @ReactMethod
    public void getUserInfo(Callback userInfoCallback) {
        if (userInfoCallback == null) return;
        try {
            JSONObject userInfo = Fox.getUserInfo();
            if (userInfo == null) {
                userInfoCallback.invoke();
            } else {
                userInfoCallback.invoke(FoxReactUtil.convertJsonToMap(userInfo));
            }
        } catch (Throwable t) {
            Log.e("FOX", "getUserInfo", t);
        }
    }

    @Override
    public void onHostResume() {
        if (Fox.isConversionCompleted()) {
            Fox.trackSession();
        }
        if (getReactApplicationContext().hasCurrentActivity()) {
            Fox.trackDeeplinkLaunch(getCurrentActivity());
        }
    }

    @Override
    public void onHostPause() {
    }

    @Override
    public void onHostDestroy() {
    }
}
