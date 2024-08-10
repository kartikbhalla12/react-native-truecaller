# @kartikbhalla/react-native-truecaller

Truecaller Integration with React Native for both Android[SDK v3.0.0] and IOS[SDK v0.1.8]

## Installation

```sh
npm install @kartikbhalla/react-native-truecaller
```

## Configuration

### Android

- Add the following <meta-data> tag to your AndroidManifest.xml file:
    ```
    <meta-data 
        android:name="com.truecaller.android.sdk.ClientId"
        android:value="YOUR_CLIENT_ID"
    />
    ```

### iOS

- Add the entry truesdk under LSApplicationQueriesSchemes in into your Info.plist file
    ```
    <key>LSApplicationQueriesSchemes</key>
    <array>
    <string>truesdk</string>
    </array>
    ```
- Add the associated domain provided by Truecaller in Your project -> Capabilities > Associated Domains. The prefix 'applinks:' is needed for universal links to function properly. Note that there is **no** _http://_ or _https://_ prefix when setting up the applinks:

![Associated domains](https://raw.githubusercontent.com/truecaller/ios-sdk/master/documentation/images/associated-domains.png)


- Starting with SDK version 0.1.7, It is mandatory to register urlScheme in your project, in the below format: truecallersdk-YOUR_APP_KEY.

![URL objects](https://docs.truecaller.com/~gitbook/image?url=https%3A%2F%2F3916632365-files.gitbook.io%2F%7E%2Ffiles%2Fv0%2Fb%2Fgitbook-x-prod.appspot.com%2Fo%2Fspaces%252FJzE3kcOEptTlnx7Hgg25%252Fuploads%252FJ62qNp5vHZkbgAJgRyVH%252Fm.png%3Falt%3Dmedia%26token%3D469daddc-0a91-4f04-add6-362806e8c5c4&width=768&dpr=4&quality=100&sign=7959ea37&sv=1
)


## Usage

- Install pods to the project 
    ```
    pod install
    ```

- Import the package using
    ```
    import { useTruecaller, TRUECALLER_ANDROID_CUSTOMIZATIONS } from '@kartikbhalla/react-native-truecaller';

    ```

- Use the truecaller hook
    ```
    const { initializeTruecaller, openTruecallerModal, user } = useTruecaller({
        iosAppKey: 'YOUR_IOS_APP_KEY',
        iosAppLink: 'YOUR_IOS_APP_LINK',
        androidButtonColor: '#FF0000',
        androidButtonStyle: TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_STYLES.ROUND,
        androidButtonText: TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_TEXTS.CONTINUE,
        androidButtonTextColor: '#FFFFFF',
        androidClientId: 'YOUR_ANDROID_CLIENT_ID',
        androidConsentHeading: TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.LOG_IN_TO,
        androidFooterButtonText: TRUECALLER_ANDROID_CUSTOMIZATIONS.FOOTER_BUTTON_TEXTS.SKIP,
    });
    ```
- Initialize trucaller using
    ```
    useEffect(() => {
        initializeTruecaller();
    }, []);

    ```
- Open truecaller modal using
    ```
    openTruecallerModal();
    ```

- Once submitted, the data will be available in the user object.
    ```
    console.log(user);
    ```

## Customizations

Android supports customizations available in [Android Truecaller SDK Customizations](https://docs.truecaller.com/truecaller-sdk/android/oauth-sdk-3.0.0/integration-steps/customisation). To customize these, you can use the following constants available inside TRUECALLER_ANDROID_CUSTOMIZATIONS

## Android Button Texts
```
TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_TEXTS.CONTINUE
TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_TEXTS.ACCEPT
TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_TEXTS.CONFIRM
TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_TEXTS.PROCEED
```

## Android Button Styles
```
TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_STYLES.ROUND
TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_STYLES.RECTANGLE
```

## Android Footer Button Texts
```
TRUECALLER_ANDROID_CUSTOMIZATIONS.FOOTER_BUTTON_TEXTS.SKIP
TRUECALLER_ANDROID_CUSTOMIZATIONS.FOOTER_BUTTON_TEXTS.ANOTHER_MOBILE_NUMBER
TRUECALLER_ANDROID_CUSTOMIZATIONS.FOOTER_BUTTON_TEXTS.ANOTHER_METHOD
TRUECALLER_ANDROID_CUSTOMIZATIONS.FOOTER_BUTTON_TEXTS.MANUALLY
TRUECALLER_ANDROID_CUSTOMIZATIONS.FOOTER_BUTTON_TEXTS.LATER
```

## Android Consent Heading Texts
```
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.LOG_IN_TO
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.SIGN_UP_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.SIGN_IN_TO
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.VERIFY_NUMBER_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.REGISTER_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.GET_STARTED_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.PROCEED_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.VERIFY_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.VERIFY_PROFILE_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.VERIFY_YOUR_PROFILE_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.VERIFY_PHONE_NO_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.VERIFY_YOUR_NO_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.CONTINUE_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.COMPLETE_ORDER_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.PLACE_ORDER_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.COMPLETE_BOOKING_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.CHECKOUT_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.MANAGE_DETAILS_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.MANAGE_YOUR_DETAILS_WITH
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.LOGIN_TO_WITH_ONE_TAP
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.SUBSCRIBE_TO
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.GET_UPDATES_FROM
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.CONTINUE_READING_ON
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.GET_NEW_UPDATES_FROM
TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.LOGIN_SIGNUP_WITH
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---



