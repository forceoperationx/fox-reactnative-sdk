/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  View
} from 'react-native';

import CZYFox from './CYZFox.js';


const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
  android: 'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

export default class App extends Component<{}> {
  render() {

    const iosConfig1 = {
        ios :{
            appId : 4879,
            appSalt : "42b199d17f20bffa15d242d37cf9650d9558ed78",
            appKey : "20131c689b5617940ac82919356bab22cdabcf81",
            customizedUserAgentEnabled : true
        }
    }

    const config = {
        ios : {
            appId : 6421,
            appSalt : "5aebe7204d3136fa6c89cd551761d1629ad9ad50",
            appKey : "8c4d483a307c8438222753774bbe0c23f67fbb5e",
            customizedUserAgentEnabled : true
        },
        android : {
            appId : 2684,
            appSalt : "bcb5a88f92c9f13a92adf58e91498a5f942f3c2b",
            appKey : "1678bb2f08e02c81ddaa8dbfa4618eb93ed1e330"
        },
        debugModeEnabled : true
    }
    CZYFox.activate(config);

    const profile = {
        name : "wuwei",
        age : 35,
        married : true
    }
    CZYFox.setUserInfo(profile);
    CZYFox.getUserInfo((info) => {
            if (info) console.log(`profile of user : ${info.name}`)
    });

    const option = {
        buid : "user-fox-react",
        redirectURL : "fox://react",
        optout : true,
        trackingCompletionHandler : () => {console.log("tracking completed implementation")},
        durationSinceClick : 60 * 60 * 12,
        deferredDeeplinkHandler : (deepInfo) => {console.log(`deep link : ${deepInfo? deepInfo["deeplink"] : "empty"}`)}
    }

    CZYFox.trackInstall(option);

    CZYFox.trackSession();

    const event = {
        eventName : "react-purchase",
        ltvId : 5975,
        buid : "react-buid",
        value : 2,
        orderId : "react-orderId",
        sku : "react-sku",
        itemName : "react-itemname",
        price : 66.6,
        quantity : 3,
        currency : "USD",
        eventInfo : {
            name : "wuwei",
            age : 35,
            married : true
        },
        extraInfo : {
            eventId : "pur001",
            coupon : "10%"
        }
    }

    CZYFox.trackEvent(event);
// **/

    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <Text style={styles.instructions}>
          To get started, edit App.js
        </Text>
        <Text style={styles.instructions}>
          {instructions}
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
