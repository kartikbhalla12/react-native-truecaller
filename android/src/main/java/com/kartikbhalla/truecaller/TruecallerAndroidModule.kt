package com.kartikbhalla.truecaller

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.annotation.Nullable
import androidx.fragment.app.FragmentActivity
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.truecaller.android.sdk.oAuth.CodeVerifierUtil
import com.truecaller.android.sdk.oAuth.TcOAuthCallback
import com.truecaller.android.sdk.oAuth.TcOAuthData
import com.truecaller.android.sdk.oAuth.TcOAuthError
import com.truecaller.android.sdk.oAuth.TcSdk
import com.truecaller.android.sdk.oAuth.TcSdkOptions
import java.math.BigInteger
import java.security.SecureRandom

class TruecallerAndroidModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

  private var codeVerifier: String? = null

  override fun getName(): String = "TruecallerAndroidModule"

  private val tcOAuthCallback: TcOAuthCallback = object : TcOAuthCallback {
    override fun onSuccess(tcOAuthData: TcOAuthData) {
      val params = Arguments.createMap().apply {
        putString("authorizationCode", tcOAuthData.authorizationCode)
        putString("codeVerifier", codeVerifier)
      }
      sendEvent("TruecallerAndroidSuccess", params)
    }

    override fun onFailure(tcOAuthError: TcOAuthError) {
      sendTruecallerFailureEvent(tcOAuthError.errorCode, tcOAuthError.errorMessage)
    }

    override fun onVerificationRequired(tcOAuthError: TcOAuthError) {
      // No-op for now
    }
  }

  @ReactMethod
  fun initialize(
    buttonColor: String,
    buttonTextColor: String,
    buttonText: String,
    buttonShape: String,
    footerButtonText: String,
    consentTitleText: String
  ) {
    try {
      val options = TcSdkOptions.Builder(reactContext, tcOAuthCallback)
        .buttonColor(Color.parseColor(buttonColor))
        .buttonTextColor(Color.parseColor(buttonTextColor))
        .ctaText(getButtonText(buttonText))
        .buttonShapeOptions(getButtonShape(buttonShape))
        .footerType(getFooterButtonText(footerButtonText))
        .consentHeadingOption(getConsentHeadingText(consentTitleText))
        .build()
      TcSdk.init(options)
    } catch (e: Exception) {
      sendTruecallerFailureEvent(0, e.message)
    }
  }

  @ReactMethod
  fun invoke() {
    try {
      val random = SecureRandom()
      val stateRequested = BigInteger(130, random).toString(32)
      TcSdk.getInstance().setOAuthState(stateRequested)
      TcSdk.getInstance().setOAuthScopes(arrayOf("profile", "phone", "email", "profile"))

      codeVerifier = CodeVerifierUtil.generateRandomCodeVerifier()
      val codeChallenge = CodeVerifierUtil.getCodeChallenge(codeVerifier)
      codeChallenge?.let { TcSdk.getInstance().setCodeChallenge(it) }

      TcSdk.getInstance().getAuthorizationCode(currentActivity as FragmentActivity)
    } catch (e: Exception) {
      sendTruecallerFailureEvent(0, e.message)
    }
  }

  @ReactMethod
  fun isUsable(): Boolean {
    return try {
      TcSdk.getInstance().isOAuthFlowUsable
    } catch (e: Exception) {
      sendTruecallerFailureEvent(0, e.message)
      false
    }
  }

  // region Helper mappings
  private fun getButtonText(text: String): Int = when (text) {
    "TRUECALLER_ANDROID_BUTTON_TEXT_ACCEPT" -> TcSdkOptions.CTA_TEXT_ACCEPT
    "TRUECALLER_ANDROID_BUTTON_TEXT_CONFIRM" -> TcSdkOptions.CTA_TEXT_CONFIRM
    "TRUECALLER_ANDROID_BUTTON_TEXT_PROCEED" -> TcSdkOptions.CTA_TEXT_PROCEED
    else -> TcSdkOptions.CTA_TEXT_CONTINUE
  }

  private fun getButtonShape(shape: String): Int = when (shape) {
    "TRUECALLER_ANDROID_BUTTON_RECTANGLE" -> TcSdkOptions.BUTTON_SHAPE_RECTANGLE
    else -> TcSdkOptions.BUTTON_SHAPE_ROUNDED
  }

  private fun getFooterButtonText(text: String): Int = when (text) {
    "TRUECALLER_ANDROID_FOOTER_BUTTON_TEXT_ANOTHER_MOBILE_NUMBER" -> TcSdkOptions.FOOTER_TYPE_ANOTHER_MOBILE_NO
    "TRUECALLER_ANDROID_FOOTER_BUTTON_TEXT_ANOTHER_METHOD" -> TcSdkOptions.FOOTER_TYPE_ANOTHER_METHOD
    "TRUECALLER_ANDROID_FOOTER_BUTTON_TEXT_MANUALLY" -> TcSdkOptions.FOOTER_TYPE_MANUALLY
    "TRUECALLER_ANDROID_FOOTER_BUTTON_TEXT_LATER" -> TcSdkOptions.FOOTER_TYPE_LATER
    else -> TcSdkOptions.FOOTER_TYPE_SKIP
  }

  private fun getConsentHeadingText(text: String): Int = when (text) {
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_SIGN_UP_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_SIGN_UP_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_SIGN_IN_TO" -> TcSdkOptions.SDK_CONSENT_HEADING_SIGN_IN_TO
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_VERIFY_NUMBER_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_VERIFY_NUMBER_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_REGISTER_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_REGISTER_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_GET_STARTED_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_GET_STARTED_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_PROCEED_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_PROCEED_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_VERIFY_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_VERIFY_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_VERIFY_PROFILE_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_VERIFY_PROFILE_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_VERIFY_YOUR_PROFILE_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_VERIFY_YOUR_PROFILE_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_VERIFY_PHONE_NO_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_VERIFY_PHONE_NO_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_VERIFY_YOUR_NO_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_VERIFY_YOUR_NO_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_CONTINUE_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_CONTINUE_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_COMPLETE_ORDER_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_COMPLETE_ORDER_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_PLACE_ORDER_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_PLACE_ORDER_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_COMPLETE_BOOKING_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_COMPLETE_BOOKING_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_CHECKOUT_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_CHECKOUT_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_MANAGE_DETAILS_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_MANAGE_DETAILS_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_MANAGE_YOUR_DETAILS_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_MANAGE_YOUR_DETAILS_WITH
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_LOGIN_TO_WITH_ONE_TAP" -> TcSdkOptions.SDK_CONSENT_HEADING_LOGIN_TO_WITH_ONE_TAP
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_SUBSCRIBE_TO" -> TcSdkOptions.SDK_CONSENT_HEADING_SUBSCRIBE_TO
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_GET_UPDATES_FROM" -> TcSdkOptions.SDK_CONSENT_HEADING_GET_UPDATES_FROM
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_CONTINUE_READING_ON" -> TcSdkOptions.SDK_CONSENT_HEADING_CONTINUE_READING_ON
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_GET_NEW_UPDATES_FROM" -> TcSdkOptions.SDK_CONSENT_HEADING_GET_NEW_UPDATES_FROM
    "TRUECALLER_ANDROID_CONSENT_HEADING_TEXT_LOGIN_SIGNUP_WITH" -> TcSdkOptions.SDK_CONSENT_HEADING_LOGIN_SIGNUP_WITH
    else -> TcSdkOptions.SDK_CONSENT_HEADING_LOG_IN_TO
  }
  // endregion

  private val activityEventListener = object : BaseActivityEventListener() {
    override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, intent: Intent?) {
      if (requestCode == TcSdk.SHARE_PROFILE_REQUEST_CODE) {
        TcSdk.getInstance().onActivityResultObtained(activity as FragmentActivity, requestCode, resultCode, intent)
      }
    }
  }

  init {
    reactContext.addActivityEventListener(activityEventListener)
  }

  private fun sendEvent(eventName: String, @Nullable params: WritableMap?) {
    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java).emit(eventName, params)
  }

  private fun sendTruecallerFailureEvent(errorCode: Int, errorMessage: String?) {
    val map = Arguments.createMap().apply {
      putInt("errorCode", errorCode)
      putString("errorMessage", errorMessage)
    }
    sendEvent("TruecallerAndroidFailure", map)
  }
} 