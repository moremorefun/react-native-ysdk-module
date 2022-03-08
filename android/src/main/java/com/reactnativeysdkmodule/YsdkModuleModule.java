package com.reactnativeysdkmodule;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.tencent.ysdk.api.YSDKApi;
import com.tencent.ysdk.module.antiaddiction.listener.AntiAddictListener;
import com.tencent.ysdk.module.antiaddiction.model.AntiAddictRet;
import com.tencent.ysdk.module.user.UserListener;
import com.tencent.ysdk.module.user.UserLoginRet;
import com.tencent.ysdk.module.user.UserRelationRet;
import com.tencent.ysdk.module.user.WakeupRet;
import com.tencent.ysdk.framework.common.ePlatform;

@ReactModule(name = YsdkModuleModule.NAME)
public class YsdkModuleModule extends ReactContextBaseJavaModule {
    public static final String NAME = "YsdkModule";

    public YsdkModuleModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }


  @ReactMethod
  public void init(boolean autoLogin) {
    YSDKApi.init(autoLogin);
    YSDKApi.setUserListener(new UserListener() {
      @Override
      public void OnLoginNotify(UserLoginRet userLoginRet) {
        String loginRetStr = userLoginRet.toString();
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("ret_str", loginRetStr);
        writableMap.putInt("ret", userLoginRet.ret);
        writableMap.putInt("flag", userLoginRet.flag);
        writableMap.putString("msg", userLoginRet.msg);
        writableMap.putInt("errorCode", userLoginRet.errorCode);
        writableMap.putInt("platform", userLoginRet.platform);
        writableMap.putString("open_id", userLoginRet.open_id);
        writableMap.putInt("user_type", userLoginRet.getUserType());
        writableMap.putInt("login_type", userLoginRet.getLoginType());
        writableMap.putString("pf", userLoginRet.pf);
        writableMap.putString("pf_key", userLoginRet.pf_key);
        writableMap.putString("access_token", userLoginRet.getAccessToken());
        writableMap.putString("Token_GUEST_PAY", userLoginRet.getPayToken());
        YsdkModuleModule.this.sendEvent("OnLoginNotify", writableMap);
      }

      @Override
      public void OnWakeupNotify(WakeupRet wakeupRet) {

      }

      @Override
      public void OnRelationNotify(UserRelationRet userRelationRet) {

      }
    });
    YSDKApi.setAntiAddictListener(new AntiAddictListener() {
      @Override
      public void onLoginLimitNotify(AntiAddictRet antiAddictRet) {
        String retStr = antiAddictRet.toString();
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("ret_str", retStr);
        writableMap.putInt("type", antiAddictRet.type);
        writableMap.putString("title", antiAddictRet.title);
        writableMap.putString("content", antiAddictRet.content);
        writableMap.putString("url", antiAddictRet.url);
        writableMap.putString("traceId", antiAddictRet.getTraceId());
        YsdkModuleModule.this.sendEvent("onLoginLimitNotify", writableMap);

        YSDKApi.reportAntiAddictExecute(antiAddictRet, System.currentTimeMillis());
      }

      @Override
      public void onTimeLimitNotify(AntiAddictRet antiAddictRet) {
        String retStr = antiAddictRet.toString();
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("ret_str", retStr);
        writableMap.putInt("type", antiAddictRet.type);
        writableMap.putString("title", antiAddictRet.title);
        writableMap.putString("content", antiAddictRet.content);
        writableMap.putString("url", antiAddictRet.url);
        writableMap.putString("traceId", antiAddictRet.getTraceId());
        YsdkModuleModule.this.sendEvent("onTimeLimitNotify", writableMap);

        YSDKApi.reportAntiAddictExecute(antiAddictRet, System.currentTimeMillis());
      }
    });
  }

  @ReactMethod
  public void login() {
    YSDKApi.login(ePlatform.Guest);
  }

  @ReactMethod
  public void logout() {
    YSDKApi.logout();
  }

  @ReactMethod
  public void setAntiAddictGameStart() {
    YSDKApi.setAntiAddictGameStart();
  }

  @ReactMethod
  public void setAntiAddictGameEnd() {
    YSDKApi.setAntiAddictGameEnd();
  }

  @ReactMethod
  public void addListener(String eventName) {

  }

  @ReactMethod
  public void removeListeners(Integer count) {

  }

  protected void sendEvent(String eventName, Object data) {
    this.getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, data);
  }
}
