import { useEffect, useState } from 'react';
import {
  NativeModules,
  DeviceEventEmitter,
  Platform,
  NativeEventEmitter,
} from 'react-native';
import axios from 'axios';

import {
  type IAndroidUserResponse,
  type IInitializeTruecaller,
  type IUser,
  type IUseTruecaller,
} from './interfaces';

import {
  TRUECALLER_ANDROID_CUSTOMIZATIONS,
  TRUECALLER_ANDROID_EVENTS,
  DEFAULT_BUTTON_COLOR,
  DEFAULT_BUTTON_TEXT_COLOR,
  TRUECALLER_IOS_EVENTS,
  IOSGender,
} from './constants';

const TruecallerAndroid = NativeModules.TruecallerAndroidModule;
const TruecallerIOS = NativeModules.TruecallerIOS;

const initialize = ({
  androidButtonColor,
  androidButtonTextColor,
  androidButtonStyle,
  androidButtonText,
  androidFooterButtonText,
  androidConsentHeading,
  iosAppKey,
  iosAppLink,
}: IInitializeTruecaller) => {
  if (Platform.OS === 'android')
    TruecallerAndroid.initialize(
      androidButtonColor || DEFAULT_BUTTON_COLOR,
      androidButtonTextColor || DEFAULT_BUTTON_TEXT_COLOR,
      androidButtonText ||
        TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_TEXTS.ACCEPT,
      androidButtonStyle ||
        TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_STYLES.ROUND,
      androidFooterButtonText ||
        TRUECALLER_ANDROID_CUSTOMIZATIONS.FOOTER_BUTTON_TEXTS.ANOTHER_METHOD,
      androidConsentHeading ||
        TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.CHECKOUT_WITH
    );
  else if (Platform.OS === 'ios' && iosAppKey && iosAppLink)
    TruecallerIOS.initialize(iosAppKey, iosAppLink);
  else {
    //TODO error handling
  }
};

const openTruecallerModal = () => {
  if (Platform.OS === 'android') TruecallerAndroid.invoke();
  else if (Platform.OS === 'ios') TruecallerIOS.requestProfile();
};

const isTruecallerSupported = () => {
  if (Platform.OS === 'android') return TruecallerAndroid.isUsable();
  else if (Platform.OS === 'ios') return TruecallerIOS.isSupported();
  return false;
};

export const useTruecaller = ({
  androidClientId,
  iosAppKey,
  iosAppLink,
  androidButtonColor,
  androidButtonStyle,
  androidButtonText,
  androidButtonTextColor,
  androidConsentHeading,
  androidFooterButtonText,
}: IUseTruecaller) => {
  const [user, setUser] = useState<IUser | null>(null);

  const [errorCode, setErrorCode] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (Platform.OS !== 'android' || !androidClientId) return;

    DeviceEventEmitter.addListener(
      TRUECALLER_ANDROID_EVENTS.TRUECALLER_SUCCESS,
      ({ authorizationCode, codeVerifier }) => {
        axios
          .post(
            'https://oauth-account-noneu.truecaller.com/v1/token',
            //TODO Constants
            {
              grant_type: 'authorization_code',
              client_id: androidClientId,
              code: authorizationCode,
              code_verifier: codeVerifier,
            },
            {
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
              },
            }
          )
          .then((response) => {
            const accessToken = response.data.access_token;

            axios
              .get<IAndroidUserResponse>(
                'https://oauth-account-noneu.truecaller.com/v1/userinfo',
                {
                  //TODO Constants
                  headers: {
                    Authorization: `Bearer ${accessToken}`,
                  },
                }
              )
              .then((resp) =>
                setUser({
                  firstName: resp.data.given_name,
                  lastName: resp.data.family_name || null,
                  mobileNumber: `+${resp.data.phone_number}`,
                  countryCode: resp.data.phone_number_country_code,
                  gender: resp.data.gender || null,
                  email: resp.data.email || null,
                })
              );

            //TODO create fixed user interface.
          })
          .catch(() => {
            //TODO error handling
          });
      }
    );

    DeviceEventEmitter.addListener(
      TRUECALLER_ANDROID_EVENTS.TRUECALLER_FAILURE,
      ({ errorMessage: errorMessageAndroid, errorCode: errorCodeAndroid }) => {
        setError(errorMessageAndroid);
        setErrorCode(errorCodeAndroid);
      }
    );
  }, [androidClientId]);

  useEffect(() => {
    if (Platform.OS !== 'ios' || !iosAppKey || !iosAppLink) return;

    //TODO Add failure event;

    const eventEmitter = new NativeEventEmitter(TruecallerIOS);

    eventEmitter.addListener(
      TRUECALLER_IOS_EVENTS.TRUECALLER_SUCCESS,
      (profile) =>
        setUser({
          firstName: profile.firstName,
          lastName: profile.lastName,
          email: profile.email || null,
          countryCode: profile.countryCode,
          gender: IOSGender?.[profile.gender] || null,
          mobileNumber: profile.phoneNumber,
        })
    );
  }, [iosAppKey, iosAppLink]);

  return {
    initializeTruecaller: () =>
      initialize({
        iosAppKey,
        iosAppLink,
        androidButtonColor,
        androidButtonStyle,
        androidButtonText,
        androidButtonTextColor,
        androidConsentHeading,
        androidFooterButtonText,
      }),
    openTruecallerModal,
    isTruecallerSupported,
    error,
    errorCode,
    user,
  };
};

export {
  TRUECALLER_ANDROID_CUSTOMIZATIONS,
  TRUECALLER_ANDROID_EVENTS,
} from './constants';
