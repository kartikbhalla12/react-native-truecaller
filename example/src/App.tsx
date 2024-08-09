import { useEffect } from 'react';
import { StyleSheet, View, Text, Pressable } from 'react-native';

import {
  TRUECALLER_ANDROID_CUSTOMIZATIONS,
  useTruecaller,
} from '@kartikbhalla/react-native-truecaller';

export default function App() {
  const { initializeTruecaller, openTruecallerModal, user } = useTruecaller({
    androidClientId: 'YOUR_ANDROID_CLIENT_ID',
    androidButtonColor: '#212121',
    androidButtonTextColor: '#FFFFFF',
    androidButtonStyle: TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_STYLES.ROUND,
    androidButtonText: TRUECALLER_ANDROID_CUSTOMIZATIONS.BUTTON_TEXTS.ACCEPT,
    androidFooterButtonText:
      TRUECALLER_ANDROID_CUSTOMIZATIONS.FOOTER_BUTTON_TEXTS.ANOTHER_METHOD,
    androidConsentHeading:
      TRUECALLER_ANDROID_CUSTOMIZATIONS.CONSENT_HEADING_TEXTS.CHECKOUT_WITH,
  });

  console.log(user);

  useEffect(() => {
    initializeTruecaller();
  }, [initializeTruecaller]);

  return (
    <View style={styles.container}>
      <Pressable onPress={openTruecallerModal}>
        <Text>Sign in with truecaller</Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
