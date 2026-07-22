[English](README.md) / [日本語](#jp)

# Aroma Shooter SDK (Java)

**Version 3.1.0**

[![Maven Central](https://img.shields.io/maven-central/v/com.aromajoin.sdk/jvm?style=flat-square&label=Maven%20Central)](https://central.sonatype.com/artifact/com.aromajoin.sdk/jvm)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)

[Aroma Shooter](https://aromajoin.com/products/aroma-shooter)を**USB**経由で接続・制御するためのデスクトップJava向けSDKです。

> **重要 — 香りを噴射するには内部ブースターが必要です。** 噴射のたびに有効にしてください。シンプルAPIでは `internalBooster: true`、濃度指定APIでは `internalBoosterIntensity > 0` を指定します。内部ブースターがオフの場合、香りは出ません。

本SDKは2つのMavenアーティファクトで構成されます。

-   `com.aromajoin.sdk:jvm` — USBコントローラー `USBASController`
-   `com.aromajoin.sdk:core` — 共通のプロトコル・コマンド・モデル（自動的に取得されます）

---

<a id="jp"></a>

## 日本語

### 目次

1. [対応デバイス](#対応デバイス)
2. [前提条件](#前提条件)
3. [インストール](#インストール)
4. [2.xからの移行](#2xからの移行)
5. [使用法](#使用法)
    - [0. セットアップと検索](#0-セットアップと検索)
    - [1. シンプル噴射API (AS1, AS2)](#1-シンプル噴射api-as1-as2)
    - [2. 濃度指定噴射API (AS2, AS3)](#2-濃度指定噴射api-as2-as3)
    - [3. 切断と再接続](#3-切断と再接続)
6. [トラブルシューティング](#トラブルシューティング)
7. [ライセンス](#ライセンス)

---

<a id="対応デバイス"></a>

### 対応デバイス

-   USB接続対応のAroma Shooter
    -   シンプル噴射API：**AS1, AS2** に対応
    -   濃度指定噴射API：**AS2, AS3**（新しいモデル）に対応

---

<a id="前提条件"></a>

### 前提条件

-   JRE **1.8** 以降
-   デバイスがシリアル/COMデバイスとして認識されている必要があります。必要に応じて、お使いのOS用の[FTDIドライバ](http://www.ftdichip.com/FTDrivers.htm)をインストールしてください。

---

<a id="インストール"></a>

### インストール

Gradle:

```gradle
dependencies {
    implementation 'com.aromajoin.sdk:jvm:3.1.0'
}
```

Maven:

```xml
<dependency>
  <groupId>com.aromajoin.sdk</groupId>
  <artifactId>jvm</artifactId>
  <version>3.1.0</version>
</dependency>
```

依存関係管理ツールを使用できない場合は、[最新リリース](https://github.com/aromajoin/aromashooter-sdk-java/releases/latest)にjarを添付しています。[Maven Central](https://central.sonatype.com/artifact/com.aromajoin.sdk/jvm)からも取得できます。`jvm`、`core`、jSerialCommをビルドパスに追加してください。

---

<a id="2xからの移行"></a>

### 2.xからの移行

3.xでは識別子の名称が変更されています。動作は同じですが、呼び出し側の修正が必要です。

| 2.x | 3.x |
|---|---|
| `diffuse*` | `shoot*` |
| `Port` | `AromaChamber` |
| `port.getPortNumber()` | `chamber.getNumber()` |
| `ports` 引数 | `chambers` |
| `stopAllPorts()` | `stopAllChambers()` |

名称変更ではなく、3.1.0で新たに使えるようになったものが2つあります。

-   `stopAllChambersWithIntensity()` — 濃度指定APIで開始した噴射を停止します。`stopAllChambers()` は別のコマンドを送るため、この噴射は止まりません。
-   `connect(aromaShooter, callback)` と `disconnect(aromaShooter, callback)` を実装しました。従来は `UnsupportedOperationException` を投げていたため、ポートを解放する手段は `disconnectAll()` のみでした。

---

<a id="使用法"></a>

## 使用法

完全なプログラムは[サンプルプロジェクト](https://github.com/aromajoin/aromashooter-sdk-java/tree/main/Sample)を参照してください。

<a id="0-セットアップと検索"></a>

### 0. セットアップと検索

検索はPC上のすべてのシリアルポートを調べるため、即座には完了しません。同期実行とコールバックのどちらでも利用できます。

```java
USBASController usb = new USBASController();

// 同期実行。
usb.scanAndConnect();
List<AromaShooter> devices = usb.getConnectedDevices();

// コールバックを使う場合。
usb.scanAndConnect(new DiscoverCallback() {
    @Override
    public void onDiscovered(List<AromaShooter> aromaShooters) {
        for (AromaShooter aromaShooter : aromaShooters) {
            System.out.println(aromaShooter.getSerial());
        }
    }

    @Override
    public void onFailed(String msg) {
        // 応答したデバイスがありません。
    }
});
```

---

<a id="1-シンプル噴射api-as1-as2"></a>

### 1. シンプル噴射API (AS1, AS2)

チャンバー番号は1～6です。噴射時間はミリ秒で、上限は10000です。

接続中のすべてのデバイスから噴射する：

```java
usb.shootAllSimple(3000, true, 2, 5);
```

特定のデバイスから噴射する（オブジェクト指定またはシリアル指定）：

```java
usb.shootSimple(aromaShooter, 3000, true, 2, 5);
usb.shootSimple("ASN3A01192", 3000, true, 2, 5);
```

停止する：

```java
usb.stopAllChambers();              // 接続中のすべてのデバイス
usb.stopAllChambers(aromaShooter);  // 1台のみ
```

---

<a id="2-濃度指定噴射api-as2-as3"></a>

### 2. 濃度指定噴射API (AS2, AS3)

`AromaChamber` はチャンバー番号と濃度を保持します。

```java
public class AromaChamber {
    public int getNumber();        // 1..6
    public int getConcentration(); // 0..100
}
```

ブースターやチャンバーに `0` を指定すると、そのまま維持されるのではなく**オフになります**。

接続中のすべてのデバイスから噴射する：

```java
usb.shootAllWithIntensity(3000, 100, 0,
    new AromaChamber(2, 50), new AromaChamber(5, 100));
```

特定のデバイスから噴射する（オブジェクト指定またはシリアル指定）：

```java
usb.shootWithIntensity(aromaShooter, 3000, 100, 0, new AromaChamber(2, 50));
usb.shootWithIntensity("ASN3A01192", 3000, 100, 0, new AromaChamber(2, 50));
```

停止する：

```java
usb.stopAllChambersWithIntensity();
usb.stopAllChambersWithIntensity(aromaShooter);
```

> 2つのAPIは異なるコマンドを送るため、**それぞれ専用の停止が必要です**。`shootWithIntensity*` で開始した噴射は `stopAllChambers()` では止まりません。

---

<a id="3-切断と再接続"></a>

### 3. 切断と再接続

シリアルポートは同時に1つのプロセスしか保持できません。切断すると、デバイスを停止したうえでポートを解放するため、他のアプリケーションが使用できるようになります。

```java
// 接続中のすべてのデバイス。
usb.disconnectAll();

// 1台のみ。
usb.disconnect(aromaShooter, new DisconnectCallback() {
    @Override
    public void onDisconnect(AromaShooter aromaShooter) {
        // ポートを解放しました。
    }

    @Override
    public void onFailed(AromaShooter aromaShooter, String msg) {
        // 未接続、またはUSBデバイスではありません。
    }
});
```

`connect` は、以前のスキャンで見つかったデバイスを、すべてのポートを調べ直すことなく再度開きます。

```java
usb.connect(aromaShooter, new ConnectCallback() {
    @Override
    public void onConnected(AromaShooter aromaShooter) {
        // 再び噴射できます。
    }

    @Override
    public void onFailed(AromaShooter aromaShooter, String msg) {
        // ポートを開けませんでした。他のアプリケーションが使用中の可能性があります。
    }
});
```

---

<a id="トラブルシューティング"></a>

## トラブルシューティング

**デバイスが見つからない。** FTDIドライバがインストールされているか、またそのポートが他のアプリケーションで開かれていないかを確認してください。シリアルポートは排他的に使用されます。ご自身のプログラムの前回実行時に `disconnectAll()` を呼んでいない場合、そのポートが保持されたままの可能性があります。

**エラーは出ないが香りが出ない。** 内部ブースターがオフになっています。`internalBooster: true`、または `internalBoosterIntensity` に0より大きい値を指定してください。

**噴射が止まらない。** もう一方のAPI用の停止を呼んでいる可能性があります。`shootWithIntensity*` の後は `stopAllChambersWithIntensity()` を、`shootSimple*` の後は `stopAllChambers()` を使用してください。

**0を指定したのにブースターが動き続ける。** 3.1.0で修正されました。以前のバージョンでは強度0のチャンネルをコマンドに含めなかったため、デバイスが直前の指示をそのまま継続していました。

---

<a id="ライセンス"></a>

## ライセンス

詳細は[LICENSE](/LICENSE.md)ファイルを参照してください。

**問題が発生したり、新機能が必要な場合は、[新しい問題](https://github.com/aromajoin/aromashooter-sdk-java/issues)を作成してください。**
