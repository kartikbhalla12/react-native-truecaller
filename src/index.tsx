import { useEffect, useState } from 'react';
import {
  NativeModules,
  DeviceEventEmitter,
  Platform,
  NativeEventEmitter,
} from 'react-native';
import axios from 'axios';

import { type IInitializeTruecaller, type IUseTruecaller } from './interfaces';

import {
  TRUECALLER_ANDROID_CUSTOMIZATIONS,
  TRUECALLER_ANDROID_EVENTS,
  DEFAULT_BUTTON_COLOR,
  DEFAULT_BUTTON_TEXT_COLOR,
  TRUECALLER_IOS_EVENTS,
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

//TODO add axios interfaces
//TODO add events interfaces

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
  const [user, setUser] = useState(null);

  //TODO
  // const [errorCode, setErrorCode] = useState(null);
  // const [error, setError] = useState(null);

  useEffect(() => {
    if (Platform.OS !== 'android' || !androidClientId) return;

    //TODO Add failure event;

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
              .get('https://oauth-account-noneu.truecaller.com/v1/userinfo', {
                //TODO Constants
                headers: {
                  Authorization: `Bearer ${accessToken}`,
                },
              })
              .then((resp) => setUser(resp.data));

            //TODO create fixed user interface.
          })
          .catch(() => {
            //TODO error handling
          });
      }
    );
  }, [androidClientId]);

  useEffect(() => {
    if (Platform.OS !== 'ios' || !iosAppKey || !iosAppLink) return;

    //TODO Add failure event;

    const eventEmitter = new NativeEventEmitter(TruecallerIOS);

    eventEmitter.addListener(
      TRUECALLER_IOS_EVENTS.TRUECALLER_SUCCESS,
      (profile) => setUser(profile)
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
    user,
    //TODO error,
    //TODO errorCode,
  };
};

export {
  TRUECALLER_ANDROID_CUSTOMIZATIONS,
  TRUECALLER_ANDROID_EVENTS,
} from './constants';
