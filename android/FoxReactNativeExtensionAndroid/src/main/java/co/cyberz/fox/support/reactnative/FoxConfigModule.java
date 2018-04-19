package co.cyberz.fox.support.reactnative;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import co.cyberz.fox.FoxConfig;

/**
 * Created by Garhira on 2017/10/20.
 */

public class FoxConfigModule extends ReactContextBaseJavaModule {

    public FoxConfigModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "CYZFoxConfig";
    }

    @ReactMethod
    public void activate(int appId, String appKey, String appSalt, boolean isDebug) {
        FoxConfig mFoxConfig = new FoxConfig(getReactApplicationContext(), appId, appKey, appSalt);
        mFoxConfig.addDebugOption(isDebug);
        mFoxConfig.activate();
    }
}
