# Integration Guide

## Index

* **[1. 导入](#install_sdk)**
	* [1.1 OS通用步骤](#install_common)
	* [1.2 iOS](#install_ios)
	* [1.3 Android](#install_android)
		* [1.3.1 权限](#android_permission)
		* [1.3.2 使用ProGuard](#android_proguard)
* **[2. 激活](#activation)**
* **[3. 广告成果计测](#measurement)**
	* [3.1 Install成果计测](#measurement_install)
	* [3.2 InstallReferrer计测(仅Android)](#measurement_installreferrer)
	* [3.3 流失唤回广告(Reengagement)成果计测](#measurement_reengagement)
* **[4. APP内事件计测](#measurement_event)**
	* [4.1 新手引导事件](#measurement_event_tutorial)
	* [4.2 消费事件](#measurement_event_purchase)
	* [4.3 Session计测](#measurement_event_session)


<div id="install_sdk"></div>

## 1. 导入

<div id="install_common"></div>

#### 1.1 OS通用步骤

请在[发布页面](https://github.com/forceoperationx/react-native-sdk/releases)下载SDK的zip文件。<br>
解压展开zip文件以后、将`js`目录内的js文件拷贝到导入目标的ReactNative项目。

* `fox.js`
* `fox-ios.js`
* `fox-android.js`

<div id="install_ios"></div>

#### 1.2 iOS
**【 依靠library文件导入 】**
按照通用步骤，将下载SDK时的zip文件同捆的iOS SDK Library的全部文件拷贝到目标iOS项目的library/framework search path里、并用xcode来link。
* `libFoxReact.a`
* `CYZFox.framework`


<div id="install_android"></div>

#### 1.3 Android

**【 依靠Gradle导入 】**

[ build.gradle ]<br>
```gradle
repositories {
    maven {
            url "https://github.com/cyber-z/public-fox-android-sdk/raw/master/mavenRepo"
          }
}

dependencies {
    compile 'co.cyberz.fox:track-core:X.X.X'
    compile 'co.cyberz.fox.support:track-reactnative:X.X.X'
}  
```

> ※ 请将`co.cyberz.fox:track-core:`的版本指定为4.3.0及以上。目前无法支持不足4.3.0的版本。

> ※ `co.cyberz.fox.support:track-reactnative:`的版本请从发布页面里选择。

**【 依靠jar文件导入 】**

按照通用步骤，将下载SDK时的zip文件同捆的Android SDK的libs内所有jar文件拷贝到Android目标项目的libs里。
* `FOX_Android_SDK_X.X.X.jar`
* `FOX_Android_SDK_Support_ReactNative_X.X.X.jar`

<div id="android_permission"></div>

#### 1.3.1 权限

在F.O.X SDK里可以利用下面3种权限。
请在&lt;Manifest&gt;标签内设置下面的权限。

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

权限|Protection Level|必须|概要
:---|:---:|:---:|:---
INTERNET|Normal|必须|F.O.X SDK进行通信所必须的。
WRITE_EXTERNAL_STORAGE ※1|Dangerous|任意|利用外部存储提高排除重复精度。(※2)

> ※1 通过将数据记录在外部存储器的方式，在APP再次被安装的时候能够更精确地计测Install成果的时候，可以添加READ_EXTERNAL_STORAGE以及WRITE_EXTERNAL_STORAGE权限，并不是必须。

> ※2 从Android M版本开始、使用ProtectionLevel为dangerous且需要权限的功能时，必须得到用户许可。详细请查看F.O.X Android SDK文档的[利用外部储存来优化排除重复(外部链接)](https://github.com/cyber-z/public-fox-android-sdk/blob/4.x/lang/ja/doc/external_storage/README.md)。

<div id="android_proguard"></div>

#### 1.3.2 使用ProGuard

使用Proguard进行APP代码混淆时，为排除F.O.X SDK的调用方法，请添加以下设置。

```
-keepattributes *Annotation*
-keep class co.cyberz.** { *; }
-keep class com.google.android.gms.ads.identifier.* { *; }
-dontwarn co.cyberz.**
# 经由Gradle导入SDK的话，不需要指定下面jar文件。
-libraryjars libs/FOX_Android_SDK_X.X.X.jar
-libraryjars libs/FOX_Android_SDK_Support_ReactNative_X.X.X.jar
```

<div id="activation"></div>

## 2. 激活

调用F.O.X类库之前请务必导入`fox.js`。之后，在案例代码里定义为CYZFox。

```js
// 指定访问fox.js的路径
import CYZFox from './fox.js';

...

componentDidMount() {
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
      // DEGUG模式的ON/OFF
      debugModeEnabled : false
  }
  CYZFox.activate(config);
...
```


> * 将F.O.X管理画面发行的值设置到`appId`, `appSalt`, `appKey`里。
> * `customizedUserAgentEnabled`为iOS固有的设置，如果定制化APP内WebView的UserAgent、请设置为true。
> * `debugModeEnabled`在希望输出F.O.X SDK的DEBUG LOG的时候设置为true。发布的时候设置为false。

<div id="measurement"></div>

## 3. 广告成果计测

<div id="measurement_install"></div>

#### 3.1 Install成果计测

通过在APP的初次启动处理里执行Install计测，能够实施广告的效果测定。<br>
请把下面做效果测定的代码添加到在APP启动时必被调用的处理里

```js
// 指定访问fox.js的路径
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
> * trackInstall方法即使不指定option参数也可以执行。
> * Install计测的option一览
>
> |項目|詳細|
> |:---:|:---|
> |redirectURL|启动浏览器的时候跳转目的地的URL。可以指定APP本身的定制化URL。|
> |buid|广告主终端ID(app的特定用户的ID)|
> |trackingCompletionHandler|Install计测完成的callback handler|
> |durationSinceClick|执行Deferred DeepLink的时候、对象最终点击的对象期间(秒)（指定从初次启动开始追溯到之前多少秒）<br>默认 : 86400 (1天)|
> |deferredDeeplinkHandler|为接收DeepLink的callback handler|
>

<div id="measurement_installreferrer"></div>

#### 3.2 Install Referrer计测計測(只有Android)

为了进行使用InstallReferrer的Install计测，请在&lt;application&gt;标签里添加如下设置。

```
<receiver android:name="co.cyberz.fox.FoxInstallReceiver" android:exported="true">
	<intent-filter>
		<action android:name="com.android.vending.INSTALL_REFERRER" />
	</intent-filter>
</receiver>
```

> "com.android.vending.INSTALL_REFERRER"的Receiver类已经被定义的情况下，请参照F.O.X Android SDK文档的[让多个INSTALL_REFERRER RECEIVER共存的设置(外部链接)](https://github.com/cyber-z/public-fox-android-sdk/blob/4.x/lang/ja/doc/install_referrer/README.md) 。

<div id="measurement_reengagement"></div>

#### 3.3 流失唤回（Reengagement）广告成果计测リエンゲージメント成果計測

为实施流失唤回广告成果计测(计测经由定制化URLScheme的启动)做必要的设定。iOS环境无需设定就能自动计测，Android环境需要下面的测试<br>

在AndroidManifest.xml的主Activity（APP里初次调用的Activity）的&lt;activity&gt;标签里，为了使用定制化的URLScheme让APP启动起来，请按照如下事例添加intent-filter。<br>
※ 流失唤回广告成果计测是通过使用定制化URLScheme调用Activity的方式来实现计测的。

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
<!-- ********** 添加如下代码 ********** -->
      <intent-filter>
            <action android:name="android.intent.action.VIEW" />

            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />

            <data
                android:scheme="填写任意的URLScheme" />
      </intent-filter>
<!-- *********************************** -->      
  </activity>
```

> ※ 定制化URLScheme的详细请参考[Android Developers (隐式intent的接收)](https://developer.android.com/guide/components/intents-filters.html#Receiving) 。

<div id="measurement_event"></div>

## 4. APP内事件的计测

可以在会员注册，完成新手引导，付费等任意成果地点执行事件计测，能够测定广告流入源的LTV。<br>
不需要事件计测的话，可以忽略本项。在APP内部产生成果的时候，请按照下面那样书写成果处理部分。<br>
为了计测APP内事件请利用`trackEvent`方法。

对于会员注册或APP内消费后的消费计测，请在注册・消费处理执行后的回调处理里执行如下事件计测处理。

<div id="measurement_event_tutorial"></div>

#### 4.1 新手引导事件

**【 执行案例 】**

```js
const event = {
    ios : {
      eventName : "iOS新手引导突破",
      ltvId : 5973,
    },
    android : {
      eventName : "Android新手引导突破",
      ltvId : 5974,
    },
    buid : "USER_001"
}
CYZFox.trackEvent(event);
```

<div id="measurement_event_purchase"></div>

#### 4.2 付费事件

**【 执行案例 】**

```js
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

> * 事件计测时候指定的参数一览
>
> |**类型**|**必须**|**参数名**|**概要**|
> |:---:|:---:|:---|:---|
> |String|○|eventName|事件名|
> |int|○|ltvId|成果地点ID<br>请输入管理者告知的值。|
> |String|-|buid|是广告主管理的唯一的判别ID（会员ID等）。 能指定的值为64文字以内的半角英文数字。|
> |double|-|price|金额|
> |String|-|currency|货币<br>请使用[ISO 4217](http://ja.wikipedia.org/wiki/ISO_4217) 定义的货币代码。|
> |String|-|sku|SKU|
> |String|-|value|値|
> |String|-|orderId|订单ID|
> |int|-|quantity|数量|
> |String|-|extraInfo|任意的参数的Key/Value<br>在Key値里请不要将下划线（"_"）写在参数名的最前面。此外，无法使用半角英文数字以外的字符。|
> |String|-|eventInfo|任意的参数群|

<div id="measurement_event_session"></div>

#### 4.3 Session计测

能够进行自然流入和广告流入的安装数比较，计测APP的启动数或唯一用户数(DAU/MAU)，留存率等。<br>
这个是指在iOS/Android的native代码里，SDK自动探知在APP启动・后台跳转前台的迁移时进行计测，不需要编写代码。
