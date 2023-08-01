### 下载安装

项目地址： https://github.com/data-baker/OfflineAsrAndroid

### Android Studio 集成 SDK（参考 demo）

**1. 将 lib-authorize-release-xxx.aar、lib-offline-asr-release-xxx.aar 文件拷贝到 module 下的 libs 文件夹内，另在该 module
的 build.gradle 文件中添加上加载 aar 文件的配置信息。**

```groovy
dependencies {
   implementation fileTree(dir: 'libs', include: ['*.aar'])
   …
}
```

**2.  在 module 下 build.gradle 文件中添加 okhttp、gson 基础库包。**

```groovy
dependencies {
   implementation 'com.squareup.okhttp3:okhttp:4.9.1'
   implementation 'com.google.code.gson:gson:2.8.6'
}

```

或者将对应.jar/.aar 包添加至工程 module 下，lib 文件夹里。 注意：SDK 内使用了 okhttp、gson 所以需要开发者添加以这两个库，若工程内已存在，无需重复添加。okhttp 请使用 4.x 版本，以免出现版本兼容 bug。

**3. 在开放平台下载资源文件，拷贝到assets目录下，注意：资源文件名称不能修改**

**4. 在 Module 的 AndroidManifest.xml 文件中添加权限。6.0 以上需要动态申请 RECORD_AUDIO 、WRITE_EXTERNAL_STORAGE
权限。**

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

**5. 关于混淆 **
SDK 中用到了 okhttp 和 gson，所以需要将这两个包的混淆代码添加上。具 体混淆代码可以去官方文档上查阅。如果项目中已经有这两个包的混淆代码，不 必重复添加。请加上我们 SDK其他类的混淆代码，如下：

```properties
-keep class com.databaker.authorize.AuthorizationType {*;}
-keep class com.databaker.authorize.AuthorizationScope {*;}
-keep class com.databaker.authorize.bean.Token {*;}
-keep class com.databaker.authorize.callback.AuthorizationCallback {*;}
-keep class com.databaker.authorize.BakerAuthorizationEngine {*;}
-keep class com.k2fsa.sherpa.ncnn.* {*;}
-keep class com.databaker.offlineasr.ErrorCode {*;}
-keep class com.databaker.offlineasr.BakerASRManager {*;}
-keep class com.databaker.offlineasr.callback.* {*;}
```

### SDK 关键类

1. BakerASRManager：声音转换关键业务处理类，全局只需一个实例 即可。

2. BakerRecognizerCallback：声音转换业务回调类。您可以在这些个回调方法中获取到当前转换的进度状态、转换后声音的播放状态和各类数据结果，或者错误信息。注意回调方法都是运行在子线程的。

### 调用说明

1. 请采用以下示例代码初始化,传入由我们授权邮件里面给到的账户相关的clientId和clientSecret。

```java
    BakerASRManager.getInstance()
        .setContext(this)
        .initSDK(clientId,clientSecret,BakerAuthorizeCallBack);
```

2. 设置语音识别状态结果回调

```
BakerASRManager.getInstance().setASRListener(BakerRecognizerCallback)
```

2. 语音识别支持2种识别场景，第一种是SDK内调用手机麦克风录音、上传完成识别返回识别结果。此种方式需要接入方提前获取应用的录音权限。第二种场景是接收字节流，SDK收到数据后，传入识别引擎完成识别，返回识别结果。

   第一种场景：
   1). 需要应用取得录音权限。
   2). 通过BakerASRManager类设置参数，初始化
   3). BakerASRManager.getInstance().startRecord();  开启识别。
   4). 在BakerRecognizerCallback回调中接收状态、识别结果，或错误信息等.
   5). BakerASRManager.getInstance().stopRecord(); 此方法结束识别。

   第二种场景：
   1). 通过BakerASRManager类设置参数，初始化
   2). BakerASRManager.getInstance().senData(buffer); 方法向SDK输入待识别语音字节 流。此方法单次接收固定长度（5120）字节数组。
   3). 在BakerRecognizerCallback回调中接收状态、识别结果，或错误信息等.

### 方法说明

BakerASRManager 基本调用方法说明

|      参数      |               参数说明               | 是否必填 |
| :------------: | :----------------------------------: | :------: |
|   setContext   |    设置上下文(须initSDK之前调用)     |    是    |
|    setDebug    |  设置调试模式(须在initSDK之前调用)   |    否    |
| setNumThreads  | 设置使用CPU核数(须在initSDK之前调用) |    否    |
|    initSDK     |              初始化SDK               |    是    |
|  isRecording   |             是否正在录音             |    否    |
| setASRListener |      设置ASR状态及结果回调方法       |    是    |
|  startRecord   |             开始ASR转化              |    否    |
|   stopRecord   |             停止ASR转化              |    否    |
|    sendData    |             发送音频数据             |    否    |

BakerAuthorizeCallBack 回调方法说明

|        参数        | 参数名称 |                             说明                             |
| :----------------: | :------: | :----------------------------------------------------------: |
| onAuthorizeSuccess | 授权成功 |         该方法代表授权成功，可以继续执行语音识别操作         |
|      onError       | 授权失败 | code是错误码，错误码具体请参考以下错误码对照表。 message是具体的错误信息。 |

BakerRecognizerCallback 回调方法说明

|      参数       |            参数名称            |                             说明                             |
| :-------------: | :----------------------------: | :----------------------------------------------------------: |
|    onResult     | 流式识别持续返回数据的接口回调 | endFlag代表此次识别是否结束，1表示此次识别结束，0表示当前识别正在进行。response代表识别到的信息 |
| onBeginOfSpeech |        用户开始输入声音        |          SDK内部录音,识别到用户开始输入声音时回调。          |
|  onEndOfSpeech  |          识别正常结束          |       SDK内部录音，正常识别结束时，会唤起此回调方法。        |
| onVolumeChanged |        音频声贝音值回调        |       volume是取值0-100之间的整数，代表声音的分贝值。        |
|     onError     |          识别出现错误          | code是错误码，错误码具体请参考以下错误码对照表。 message是具体的错误信息日志。识别过程中出现各种错误情况都会回调此方法。 |

### 错误码说明

| 错误码 |           含义           |                 解决办法                 |
| :----: | :----------------------: | :--------------------------------------: |
| 100001 |   clientId或secret为空   |       检查clientId、secret是否为空       |
| 100003 |   请授权读写和录音权限   |            授权读写和录音权限            |
| 100004 |      网络连接不成功      |           检查手机网络是否可用           |
| 100005 |    在线请求token失败     |       检查clientId、secret是否正确       |
| 100006 |      请求token失败       |       检查clientId、secret是否正确       |
| 114001 | 需要先调用setContext方法 | 在调用initSDK之前,需先调用setContext方法 |
| 114002 |         授权失败         |         请先调用initSDK进行授权          |
| 114005 |      请授权录音权限      |               授权录音权限               |

### 常见问题

1. 数据的识别结果展示为乱码怎么办?

   答：检查识别的音频格式，要求识别的数据为单声道，16K采样率，16位位深的音频。

