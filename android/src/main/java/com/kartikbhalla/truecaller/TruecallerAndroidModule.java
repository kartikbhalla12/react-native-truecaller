package com.kartikbhalla.truecaller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.truecaller.android.sdk.oAuth.CodeVerifierUtil;
import com.truecaller.android.sdk.oAuth.TcOAuthCallback;
import com.truecaller.android.sdk.oAuth.TcOAuthData;
import com.truecaller.android.sdk.oAuth.TcOAuthError;
import com.truecaller.android.sdk.oAuth.TcSdk;
import com.truecaller.android.sdk.oAuth.TcSdkOptions;

import java.math.BigInteger;
import java.security.SecureRandom;


public class TruecallerAndroidModule extends ReactContextBaseJavaModule {
  private static ReactApplicationContext reactContext;
  private String codeVerifier;

  TruecallerAndroidModule(ReactApplicationContext context) {
    super(context);
    reactContext = context;

    reactContext.addActivityEventListener(mActivityEventListener);
  }

  @Override
  public String getName() {
    return "TruecallerAndroidModule";
  }

  private final TcOAuthCallback tcOAuthCallback = new TcOAuthCallback() {
    @Override
    public void onSuccess(TcOAuthData tcOAuthData) {

      WritableMap params = Arguments.createMap();
      params.putString("authorizationCode", tcOAuthData.getAuthorizationCode());
      params.putString("codeVerifier", codeVerifier);

      sendEvent(reactContext, "TruecallerAndroidSuccess", params);
    }

    @Override
    public void onFailure(TcOAuthError tcOAuthError) {
      WritableMap params = Arguments.createMap();
      params.putInt("errorCode", tcOAuthError.getErrorCode());
      params.putString("errorMessage", tcOAuthError.getErrorMessage());

      sendEvent(reactContext, "TruecallerAndroidFailure", params);
    }

    @Override
    public void onVerificationRequired(TcOAuthError tcOAuthError) {
    }

  };

  private int getButtonText(String text) {
    switch (text) {
      case "TRUECALLER_ANDROID_BUTTON_TEXT_ACCEPT":
        return TcSdkOptions.CTA_TEXT_ACCEPT;
      case "TRUECALLER_ANDROID_BUTTON_TEXT_CONFIRM":
        return TcSdkOptions.CTA_TEXT_CONFIRM;
      case "TRUECALLER_ANDROID_BUTTON_TEXT_PROCEED":
        return TcSdkOptions.CTA_TEXT_PROCEED;
      default:
        return TcSdkOptions.CTA_TEXT_CONTINUE;
    }
  }

  private int getButtonShape(String shape) {
    switch (shape) {
      case "TRUECALLER_ANDROID_BUTTON_RECTANGLE":
        return TcSdkOptions.BUTTON_SHAPE_RECTANGLE;
      default:
        return TcSdkOptions.BUTTON_SHAPE_ROUNDED;
    }
  }

  private int getFooterButtonText(String text) {
    switch (text) {
      case "TRUECALLER_ANDROID_FOOTER_BUTTON_TEXT_ANOTHER_MOBILE_NUMBER":
        return TcSdkOptions.FOOTER_TYPE_ANOTHER_MOBILE_NO;
      case "TRUECALLER_ANDROID_FOOTER_BUTTON_TEXT_ANOTHER_METHOD":
        return TcSdkOptions.FOOTER_TYPE_ANOTHER_METHOD;
      case "TRUECALLER_ANDROID_FOOTER_BUTTON_TEXT_MANUALLY":
        return TcSdkOptions.FOOTER_TYPE_MANUALLY;
      case "TRUECALLER_ANDROID_FOOTER_BUTTON_TEXT_LATER":
        return TcSdkOptions.FOOTER_TYPE_LATER;
      default:
        return TcSdkOptions.FOOTER_TYPE_SKIP;
    }
  }


  private int getConsentHeadingText(String text) {
    switch (text) {
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_SIGN_UP_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_SIGN_UP_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_SIGN_IN_TO":
        return TcSdkOptions.SDK_CONSENT_HEADING_SIGN_IN_TO;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_VERIFY_NUMBER_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_VERIFY_NUMBER_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_REGISTER_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_REGISTER_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_GET_STARTED_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_GET_STARTED_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_PROCEED_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_PROCEED_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_VERIFY_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_VERIFY_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_VERIFY_PROFILE_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_VERIFY_PROFILE_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_VERIFY_YOUR_PROFILE_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_VERIFY_YOUR_PROFILE_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_VERIFY_PHONE_NO_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_VERIFY_PHONE_NO_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_VERIFY_YOUR_NO_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_VERIFY_YOUR_NO_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_CONTINUE_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_CONTINUE_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_COMPLETE_ORDER_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_COMPLETE_ORDER_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_PLACE_ORDER_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_PLACE_ORDER_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_COMPLETE_BOOKING_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_COMPLETE_BOOKING_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_CHECKOUT_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_CHECKOUT_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_MANAGE_DETAILS_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_MANAGE_DETAILS_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_MANAGE_YOUR_DETAILS_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_MANAGE_YOUR_DETAILS_WITH;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_LOGIN_TO_WITH_ONE_TAP":
        return TcSdkOptions.SDK_CONSENT_HEADING_LOGIN_TO_WITH_ONE_TAP;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_SUBSCRIBE_TO":
        return TcSdkOptions.SDK_CONSENT_HEADING_SUBSCRIBE_TO;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_GET_UPDATES_FROM":
        return TcSdkOptions.SDK_CONSENT_HEADING_GET_UPDATES_FROM;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_CONTINUE_READING_ON":
        return TcSdkOptions.SDK_CONSENT_HEADING_CONTINUE_READING_ON;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_GET_NEW_UPDATES_FROM":
        return TcSdkOptions.SDK_CONSENT_HEADING_GET_NEW_UPDATES_FROM;
      case "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_LOGIN_SIGNUP_WITH":
        return TcSdkOptions.SDK_CONSENT_HEADING_LOGIN_SIGNUP_WITH;
      default:
        return TcSdkOptions.SDK_CONSENT_HEADING_LOG_IN_TO;
    }
  }


  @ReactMethod
  public void initialize(String buttonColor, String buttonTextColor, String buttonText, String buttonShape, String footerButtonText, String consentTitleText) {

    try {
      TcSdkOptions tcSdkOptions = new TcSdkOptions.Builder(reactContext, tcOAuthCallback)
        .buttonColor(Color.parseColor(buttonColor))
        .buttonTextColor(Color.parseColor(buttonTextColor))
        .ctaText(getButtonText(buttonText))
        .buttonShapeOptions(getButtonShape(buttonShape))

        .footerType(getFooterButtonText(footerButtonText))
        .consentHeadingOption(getConsentHeadingText(consentTitleText))
        .build();

      TcSdk.init(tcSdkOptions);
    } catch (Exception e) {
      sendTruecallerFailureEvent(0, e.getMessage());
    }

  }

  @ReactMethod
  public void invoke() {
    try {
      SecureRandom random = new SecureRandom();
      BigInteger stateRequestedBigInt = new BigInteger(130, random);
      String stateRequested = stateRequestedBigInt.toString(32);

      TcSdk.getInstance().setOAuthState(stateRequested);
      TcSdk.getInstance().setOAuthScopes(new String[]{"profile", "phone", "email"});

      codeVerifier = CodeVerifierUtil.Companion.generateRandomCodeVerifier();
      String codeChallenge = CodeVerifierUtil.Companion.getCodeChallenge(codeVerifier);

      if (codeChallenge != null) {
        TcSdk.getInstance().setCodeChallenge(codeChallenge);
      }

      TcSdk.getInstance().getAuthorizationCode((FragmentActivity) getCurrentActivity());
    } catch (Exception e) {
      sendTruecallerFailureEvent(0, e.getMessage());
    }

  }


  @ReactMethod
  public boolean isUsable() {

    try {
      return TcSdk.getInstance().isOAuthFlowUsable();
    } catch (Exception e) {
      sendTruecallerFailureEvent(0, e.getMessage());

      return false;
    }
  }

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
      super.onActivityResult(activity, requestCode, resultCode, intent);

      if (requestCode == TcSdk.SHARE_PROFILE_REQUEST_CODE) {
        TcSdk.getInstance().onActivityResultObtained((FragmentActivity) activity, requestCode, resultCode, intent);
      }
    }
  };

  private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
  }

  private void sendTruecallerFailureEvent(int errorCode, String errorMessage) {
    WritableMap params = Arguments.createMap();
    params.putInt("errorCode", errorCode);
    params.putString("errorMessage", errorMessage);

    sendEvent(reactContext, "TruecallerAndroidFailure", params);
  }
}
