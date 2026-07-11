[English](https://github.com/aromajoin/aromashooter-sdk-java) / [日本語](README-JP.md)

# Aroma Shooter SDK (Java)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.aromajoin.sdk/jvm/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.aromajoin.sdk/jvm)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Join the community on Spectrum](https://withspectrum.github.io/badge/badge.svg)](https://spectrum.chat/aromajoin-software/)

**[Aroma Shooter](https://aromajoin.com/products/aroma-shooter)との通信に使用されるAromaShooterController SDKのJava版です。**  

# 目次
1. [対応デバイス](#対応デバイス)  
2. [前提条件](#前提条件)
3. [インストール](#インストール)
4. [使用法](#使用法)
    * [セットアップ](#セットアップ)
    * [デバイスのスキャンと接続](#デバイスのスキャンと接続)
    * [接続されたデバイス](#接続されたデバイス)
    * [香りを噴射する](#香りを噴射する)
    * [噴射を止める](#噴射を止める)
    * [切断する](#切断する)
5. [問題](#問題)
6. [ライセンス](#ライセンス)

## 対応デバイス
* Aroma Shooter USBタイプ
* Aroma Shooter 2

## 対応接続
* USB 

## 前提条件
* JRE版: >= 1.8+
* [ドライバ](http://www.ftdichip.com/FTDrivers.htm)をダウンロードしてインストールしてください。

## インストール

### Gradle
`controller-sdk`依存関係を追加します。
```gradle
dependencies {
    // ... other dependencies
    compile 'com.aromajoin.sdk:jvm:2.4.0'
}
```
### Maven
```xml
<dependency>
  <groupId>com.aromajoin.sdk</groupId>
  <artifactId>jvm</artifactId>
  <version>2.4.0</version>
  <type>pom</type>
</dependency>
```
### バイナリファイル（.jar)
1. [最新のcontroller-sdk-java.jarファイル](https://github.com/aromajoin/aromashooter-sdk-java/releases/tag/v2.4.0)をダウンロードしてください。
2. それをプロジェクトのビルドパスに追加します。

## 使用法
詳細は[サンプルプロジェクト](https://github.com/aromajoin/aromashooter-sdk-java/tree/master/Sample)をチェックしてください。

### セットアップ
```java
// USBAromaShooterを初期化する
USBASController usbController = new USBASController();
```
### デバイスのスキャンと接続

```java
usbController.scanAndConnect(new DiscoverCallback() {
    @Override
    public void onDiscovered(List<AromaShooter> aromaShooters) {
        for(AromaShooter aromaShooter : aromaShooters){
            // Detected Aroma Shooter.
        }
    }

    @Override
    public void onFailed(String msg) {
       // Failed on scanning.
    }
});
```

### 接続されたデバイス
```java
List<AromaShooter> connectedDevices = usbController.getConnectedDevices();
```

### 香りを噴射する
```java
/**
 * @param duration     噴射持続時間（ミリ秒）。
 * @param internalBooster      ブースターが使用されているかどうかを判定する。(true: より強く噴射する, false: より弱く噴射する)
 * @param chambers        カートリッジ番号を噴射する。値：1 ~ 6.
 */
// 例：以下のコードは、カートリッジ2と5を3秒間噴射します。
usbController.shootAllWithIntensity(duration, internalBoosterIntensity, externalBoosterIntensity, chambers);
```

* AS2（Aroma Shooter 2）デバイスのみのディフューザー香りメソッド
```java
/**
 * AS2のすべての接続デバイスからの特定のポートでの香りの拡散
 * @param duration              拡散時間（ミリ秒）
 * @param boosterIntensity      ブースターポート。値： 0~100
 * @param fanIntensity          ファンポート。値： 0~100
 * @param chambers                 ポートの配列。値: AromaChamber(number, concentration)
 */
 
usbController.shootAllWithIntensity(duration, internalBoosterIntensity, externalBoosterIntensity, chambers);
```

* その他の噴射法
```java
// 1つのアロマシューターに香りを噴射する
controller.shootSimple(aromaShooter, duration, internalBooster, chambers);
// シリアル番号入力で1つのアロマシューターに香りを噴射する
controller.shootSimple(aromaShooterSerial, duration, internalBooster, chambers);
// 1つのアロマシューターに香りを噴射する [アロマシューター2のみがサポートされています](香りの強度を調整可)
controller.shootWithIntensity(aromaShooter, durationMilliSec, internalBoosterIntensity, externalBoosterIntensity, chambers);
// シリアル番号入力で1つのアロマシューターに香りを噴射する [アロマシューター2のみがサポートされています](香りの強度を調整可)
controller.shootWithIntensity(aromaShooterSerial, durationMilliSec, internalBoosterIntensity, externalBoosterIntensity, chambers);
```

### 噴射を止める
* 噴射している場合は、接続されているデバイスのすべてのポートを停止します。
```java
usbController.stopAllPorts();
```
* 噴射している場合は、１つのデバイスのすべてのポートを停止します。
```java
usbController.stopAllPorts(aromaShooter);
```

### 切断する
すべてのデバイスを切断します。
```java
usbController.disconnectAll();
```
## 問題
**問題が発生したり、新機能が必要な場合は、[新しい問題](https://github.com/aromajoin/aromashooter-sdk-java/issues)を作成してください。**

## ライセンス
[こちら](https://github.com/aromajoin/aromashooter-sdk-java/blob/master/LICENSE.md)を参照してください。
