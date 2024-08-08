import { type ColorValue } from 'react-native';

import type { TRUECALLER_ANDROID_CUSTOMIZATIONS } from './constants';

export type IButtonTextKey =
  keyof typeof TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_TEXTS;
export type IButtonTextValue =
  (typeof TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_TEXTS)[IButtonTextKey];

export type IButtonStyleKey =
  keyof typeof TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_STYLES;
export type IButtonStyleValue =
  (typeof TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_STYLES)[IButtonStyleKey];

export type IFooterButtonTextKey =
  keyof typeof TRUECALLER_ANDROID_CUSTOMIZATIONS.FOOTER_BUTTON_TEXTS;
export type IFooterButtonTextValue =
  (typeof TRUECALLER_ANDROID_CUSTOMIZATIONS.FOOTER_BUTTON_TEXTS)[IFooterButtonTextKey];

export type IConsetHeadingKey =
  keyof typeof TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS;
export type IConsentHeadingValue =
  (typeof TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS)[IConsetHeadingKey];

//TODO extract interface

export interface IInitializeTruecaller {
  iosAppKey?: string;
  iosAppLink?: string;

  androidButtonColor?: ColorValue;
  androidButtonTextColor?: ColorValue;
  androidButtonStyle?: IButtonStyleValue;
  androidButtonText?: IButtonTextValue;
  androidFooterButtonText?: IFooterButtonTextValue;
  androidConsentHeading?: IConsentHeadingValue;
}

//TODO extract interface

export interface IUseTruecaller {
  androidClientId?: string;
  iosAppKey?: string;
  iosAppLink?: string;

  androidButtonColor?: ColorValue;
  androidButtonTextColor?: ColorValue;
  androidButtonStyle?: IButtonStyleValue;
  androidButtonText?: IButtonTextValue;
  androidFooterButtonText?: IFooterButtonTextValue;
  androidConsentHeading?: IConsentHeadingValue;
}
