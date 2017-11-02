# Integration Guide

## Index

* **[1. インストール](#install_sdk)**
	* [1.1 OS共通手順](#install_common)
	* [1.2 iOS](#install_ios)
	* [1.3 Android](#install_android)
		* [1.3.1 パーミッション](#android_permission)
		* [1.3.2 ProGuardを利用する場合](#android_proguard)
* **[2. アクティベート](#activation)**
* **[3. 広告成果の計測](#measurement)**
	* [3.1 インストール成果計測](#measurement_install)
	* [3.2 インストールリファラ計測(Androidのみ)](#measurement_installreferrer)
	* [3.3 リエンゲージメント成果計測](#measurement_reengagement)
* **[4. アプリ内イベントの計測](#measurement_event)**
	* [4.1 チュートリアルイベント](#measurement_event_tutorial)
	* [4.2 課金イベント](#measurement_event_purchase)
	* [4.3 セッション計測](#measurement_event_session)


<div id="install_sdk"></div>

## 1. インストール

<div id="install_common"></div>

#### 1.1 OS共通手順

[リリースページ](https://github.com/forceoperationx/react-native-sdk/releases)よりSDKのzipファイルをダウンロードします。<br>
zipファイルの展開後、`js`ディレクトリ内のjsファイルを導入先となるReactNativeプロジェクトにコピーします。

* `fox.js`
* `fox-ios.js`
* `fox-android.js`

<div id="install_ios"></div>

#### 1.2 iOS
















<div id="install_android"></div>

#### 1.3 Android

**【 Gradleによる導入 】**

[ build.gradle ]<br>
```gradle
repositories {
    maven {
            url "https://github.com/cyber-z/public-fox-android-sdk/raw/master/mavenRepo"
          }
}

dependencies {
    compile 'co.cyberz.fox:track-core:4.3.0'
    compile 'co.cyberz.fox.support:track-reactnative:1.0.0'
}  
```

**【 jarファイルによる導入 】**

共通手順でダウンロードしたSDKのzipファイルに同梱されているAndroid SDKのlibs内のjarファイル全てを導入先となるAndroidプロジェクトのlibsにコピーします。
* `FOX_Android_SDK_X.X.X.jar`
* `FOX_Android_SDK_Support_ReactNative_X.X.X.jar`

<div id="android_permission"></div>

#### 1.3.1 パーミッション

F.O.X SDKでは下記3つのパーミッションを利用します。
&lt;Manifest&gt;タグ内に次のパーミッションの設定を追加します。

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

パーミッション|Protection Level|必須|概要
:---|:---:|:---:|:---
INTERNET|Normal|必須|F.O.X SDKが通信を行うために必要となります。
WRITE_EXTERNAL_STORAGE ※1|Dangerous|任意|ストレージを利用した重複排除機能向上に必要となります。(※2)

> ※1 READ_EXTERNAL_STORAGE及びWRITE_EXTERNAL_STORAGEパーミッションは、外部ストレージにデータを記録することでアプリの再インストール時により正確にインストール計測を行うために必要ですが、必須ではありません。

> ※2 Android MよりProtectionLevelが`dangerous`に指定されているパーミッションを必要とする機能を利用するには、ユーザーの許可が必要になります。詳細はF.O.X Android SDKドキュメントの[外部ストレージを利用した重複排除設定(外部リンク)](https://github.com/cyber-z/public-fox-android-sdk/blob/4.x/lang/ja/doc/external_storage/README.md)をご確認ください。

<div id="android_proguard"></div>

#### 1.3.2 ProGuardを利用する場合

ProGuard を利用してアプリケーションの難読化を行う際は F.O.X SDK のメソッドが対象とならないよう、以下の設定 を追加してください。

```
-keepattributes *Annotation*
-keep class co.cyberz.** { *; }
-keep class com.google.android.gms.ads.identifier.* { *; }
-dontwarn co.cyberz.**
# Gradle経由でSDKをインストールしている場合、下記jarファイルの指定は不要です。
-libraryjars libs/FOX_Android_SDK_X.X.X.jar
-libraryjars libs/FOX_Android_SDK_Support_ReactNative_X.X.X.jar
```

<div id="activation"></div>

## 2. アクティベート

F.O.Xのライブラリを呼び出す際は必ず`fox.js`をimportします。以降、サンプル内ではCYZFoxと定義します。

```java
// fox.jsへのパスを通します
import CYZFox from './fox.js';

...

render() {
...
  const config = {
      ios : {
          appId : 6421,
          appSalt : "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
          appKey : "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
          customizedUserAgentEnabled : true
      },
      android : {
          appId : 2684,
          appSalt : "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
          appKey : "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
      },
      // デバッグモードのON/OFF
      debugModeEnabled : false
  }
  CYZFox.activate(config);
...
```


> * `appId`, `appSalt`, `appKey`はF.O.Xのコンソールで発行された値を設定します。
> * `customizedUserAgentEnabled`はiOS固有の設定で、アプリ内のWebViewのUserAgentをカスタマイズしている場合、trueに設定してください。
> * `debugModeEnabled`はF.O.X SDKのデバッグログを出力する際にtrueを設定します。リリースの際はfalseに設定してください。

<div id="measurement"></div>

## 3. 広告成果の計測

<div id="measurement_install"></div>

#### 3.1 インストール成果計測

アプリの初回起動のインストール計測を実装することで、広告の効果測定を行うことができます。<br>
以下のコードをアプリの起動時に必ず呼ばれる箇所に効果測定用の処理を追加します。

```java
// fox.jsへのパスを通します
import CYZFox from './fox.js';

...

const option = {
    buid : "UserID",
    redirectURL : "fox://react",
    trackingCompletionHandler : () => {console.log("tracking completed implementation")},
    durationSinceClick : 60 * 60 * 12,
    deferredDeeplinkHandler : (deepInfo) => {console.log(`deep link : ${deepInfo? deepInfo["deeplink"] : "empty"}`)}
}
//
CYZFox.trackInstall(option);
```
> * trackInstallはオプション(引数)無しでも指定することが可能です。
> * インストール計測のオプション一覧
>
> |項目|詳細|
> |:---:|:---|
> |redirectURL|ブラウザを起動させた時に遷移させる先のURL。アプリ自身のカスタムURLも指定可能。|
> |buid|広告主端末ID(アプリのユーザーを特定するID)|
> |trackingCompletionHandler|インストール計測完了のコールバックハンドラ|
> |durationSinceClick|ディファードディープリンクを実行する際の、対象となるラストクリックの対象期間(秒)（初回起動からどれだけ遡るかを秒数で指定）<br>デフォルト : 86400 (1日)|
> |deferredDeeplinkHandler|ディープリンクを受け取るためのコールバックハンドラ|
>

<div id="measurement_installreferrer"></div>

#### 3.2 インストールリファラ計測(Androidのみ)

インストールリファラーを用いたインストール計測を行うために下記の設定を&lt;application&gt;タグに追加します。

```
<receiver android:name="co.cyberz.fox.FoxInstallReceiver" android:exported="true">
	<intent-filter>
		<action android:name="com.android.vending.INSTALL_REFERRER" />
	</intent-filter>
</receiver>
```

> 既に"com.android.vending.INSTALL_REFERRER"に対するレシーバークラスが定義されている場合には、F.O.X Android SDKドキュメントの[複数のINSTALL_REFERRERレシーバーを共存させる場合の設定(外部リンク)](https://github.com/cyber-z/public-fox-android-sdk/blob/4.x/lang/ja/doc/install_referrer/README.md) をご参照ください。

<div id="measurement_reengagement"></div>

#### 3.3 リエンゲージメント成果計測

リエンゲージメント計測（カスタムURLスキーム経由の起動を計測）するために必要な設定を行います。iOSは設定なしで自動で計測を行いますがAndroidは以下の設定が必要となります。<br>

AndroidManifest.xmlのトップActivity (アプリ内の初めに呼ばれるActivity) の&lt;activity&gt;タグ内に、カスタムURLスキームでアプリを起動出来るよう以下のようにintent-filterを追記します。<br>
※ リエンゲージメント計測はカスタムURLスキームでActivityが呼び出されることで計測を行います。

```xml
  <activity
      android:name=".MainActivity"
      android:configChanges="keyboard|keyboardHidden|orientation|screenSize"
      android:label="@string/app_name"
      android:windowSoftInputMode="adjustResize">
      <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
      </intent-filter>
<!-- ********** 以下を追記します ********** -->
      <intent-filter>
            <action android:name="android.intent.action.VIEW" />

            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />

            <data
                android:scheme="任意のURLスキームを記入します" />
      </intent-filter>
<!-- *********************************** -->      
  </activity>
```

> ※ カスタムURLスキームの詳細は[Android Developers (暗黙的インテントの受信)](https://developer.android.com/guide/components/intents-filters.html#Receiving) をご確認ください。

<div id="measurement_event"></div>

## 4. アプリ内イベントの計測

会員登録、チュートリアル突破、課金など任意の成果地点にイベント計測を実装することで、流入元広告のLTVを測定することができます。<br>
尚、イベント計測が不要の場合には、本項目の実装を省略できます。成果がアプリ内部で発生する場合、成果処理部に以下のように記述してください。<br>
アプリ内イベントを計測するには`trackEvent`メソッドを利用します。

会員登録やアプリ内課金後の課金計測では、登録・課金処理実行後のコールバック内に以下のようにイベント計測処理を記述します。

<div id="measurement_event_tutorial"></div>

#### 4.1 チュートリアルイベント

**【 実装例 】**

```java
const event = {
    ios : {
      eventName : "iOSチュートリアル突破",
      ltvId : 5973,
    },
    android : {
      eventName : "Androidチュートリアル突破",
      ltvId : 5974,
    },
    buid : "USER_001"
}
CYZFox.trackEvent(event);
```

<div id="measurement_event_purchase"></div>

#### 4.2 課金イベント

**【 実装例 】**

```java
const event = {
    ios : {
      eventName : "ios-purchase",
      ltvId : 5975,
    },
    android : {
      eventName : "android-purchase",
      ltvId : 5976,
    },
    buid : "USER_001",
    value : 2,
    orderId : "react-orderId",
    sku : "react-sku",
    itemName : "react-itemname",
    price : 18.6,
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
CYZFox.trackEvent(event);
```

> * イベント計測時に指定するパラメータ一覧
>
> |**型**|**必須**|**パラメータ名**|**概要**|
> |:---:|:---:|:---|:---|
> |String|○|eventName|イベント名|
> |int|○|ltvId|成果地点ID<br>管理者より連絡します。その値を入力してください。|
> |String|-|buid|広告主様が管理しているユニークな識別子（会員IDなど）です。 指定できる値は64文字以内の半角英数字です。|
> |double|-|price|金額|
> |String|-|currency|通貨<br>[ISO 4217](http://ja.wikipedia.org/wiki/ISO_4217) で定義された通貨コードを指定してください。|
> |String|-|sku|SKU|
> |String|-|value|値|
> |String|-|orderId|オーダーID|
> |int|-|quantity|数量|
> |String|-|extraInfo|任意のパラメータのKey/Value<br>Key値にアンダースコア（"_"）をパラメータ名の先頭に記述しないでください。また、半角英数字以外は使用できません。|
> |String|-|eventInfo|任意のパラメータ群|

<div id="measurement_event_session"></div>

#### 4.3 セッション計測

自然流入と広告流入のインストール数比較、アプリケーションの起動数やユニークユーザー数(DAU/MAU)、継続率等を計測することができます。<br>
こちらはSDKがアプリの起動時・バックグラウンドからフォアグラウンドへの遷移をiOS/Androidのネイティブ側で自動検知して計測を行うため、実装する必要はありません。
