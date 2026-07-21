[English](#en) / [日本語](README-JP.md)

# Aroma Shooter SDK (Java)

**Version 3.1.0**

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.aromajoin.sdk/jvm/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.aromajoin.sdk/jvm)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)

A desktop Java SDK for connecting to and controlling [Aroma Shooter devices](https://aromajoin.com/products/aroma-shooter) over **USB**.

> **Important — the internal booster is required for scent to be emitted.** Enable it on every shoot: pass `internalBooster: true` (simple API) or `internalBoosterIntensity > 0` (intensity API). With the internal booster off, no scent comes out.

This SDK ships as two Maven artifacts:

-   `com.aromajoin.sdk:jvm` — the USB controller, `USBASController`
-   `com.aromajoin.sdk:core` — shared protocol, commands and models, pulled in automatically

---

<a id="en"></a>

## English

### Table of Contents

1. [Supported devices](#supported-devices)
2. [Prerequisites](#prerequisites)
3. [Installation](#installation)
4. [Upgrading from 2.x](#upgrading-from-2x)
5. [Usage](#usage)
    - [0. Setup / discovery](#0-setup--discovery)
    - [1. Simple shooting API (AS1, AS2)](#1-simple-shooting-api-as1-as2)
    - [2. Intensity shooting API (AS2, AS3)](#2-intensity-shooting-api-as2-as3)
    - [3. Disconnecting / reconnecting](#3-disconnecting--reconnecting)
6. [Troubleshooting](#troubleshooting)
7. [License](#license)

---

<a id="supported-devices"></a>

### Supported devices

-   USB-compatible Aroma Shooter
    -   Simple shooting API: compatible with **AS1, AS2**
    -   Intensity shooting API: compatible with **AS2, AS3** (newer models)

---

<a id="prerequisites"></a>

### Prerequisites

-   JRE **1.8** or later
-   The device must be recognised as a serial/COM device. Install the [FTDI driver](http://www.ftdichip.com/FTDrivers.htm) for your OS if required.

---

<a id="installation"></a>

### Installation

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

If you cannot use a dependency manager, the jars are attached to the [latest release](https://github.com/aromajoin/aromashooter-sdk-java/releases/latest), and are also on [Maven Central](https://central.sonatype.com/artifact/com.aromajoin.sdk/jvm). Add `jvm`, `core` and jSerialComm to your build path.

---

<a id="upgrading-from-2x"></a>

### Upgrading from 2.x

3.x renames a number of identifiers. Behaviour is unchanged, but calling code has to be updated.

| 2.x | 3.x |
|---|---|
| `diffuse*` | `shoot*` |
| `Port` | `AromaChamber` |
| `port.getPortNumber()` | `chamber.getNumber()` |
| `ports` parameter | `chambers` |
| `stopAllPorts()` | `stopAllChambers()` |

Two things are new in 3.1.0 rather than renamed:

-   `stopAllChambersWithIntensity()` stops a shoot started with the intensity API. `stopAllChambers()` sends the shorter command, which does not stop it.
-   `connect(aromaShooter, callback)` and `disconnect(aromaShooter, callback)` are implemented. They used to throw `UnsupportedOperationException`, so `disconnectAll()` was the only way to release a port.

---

<a id="usage"></a>

## Usage

For a complete program, see the [Sample project](https://github.com/aromajoin/aromashooter-sdk-java/tree/main/Sample).

<a id="0-setup--discovery"></a>

### 0. Setup / discovery

Scanning walks every serial port on the machine, so it is not instant. It can be run synchronously or with a callback.

```java
USBASController usb = new USBASController();

// Synchronous.
usb.scanAndConnect();
List<AromaShooter> devices = usb.getConnectedDevices();

// With a callback.
usb.scanAndConnect(new DiscoverCallback() {
    @Override
    public void onDiscovered(List<AromaShooter> aromaShooters) {
        for (AromaShooter aromaShooter : aromaShooters) {
            System.out.println(aromaShooter.getSerial());
        }
    }

    @Override
    public void onFailed(String msg) {
        // No device answered.
    }
});
```

---

<a id="1-simple-shooting-api-as1-as2"></a>

### 1. Simple shooting API (AS1, AS2)

Chambers are numbered 1 to 6. Duration is in milliseconds, capped at 10000.

Shoot from all connected devices:

```java
usb.shootAllSimple(3000, true, 2, 5);
```

Shoot from a specific device, by object or by serial:

```java
usb.shootSimple(aromaShooter, 3000, true, 2, 5);
usb.shootSimple("ASN3A01192", 3000, true, 2, 5);
```

Stop:

```java
usb.stopAllChambers();              // every connected device
usb.stopAllChambers(aromaShooter);  // one device
```

---

<a id="2-intensity-shooting-api-as2-as3"></a>

### 2. Intensity shooting API (AS2, AS3)

`AromaChamber` carries a chamber number and a concentration:

```java
public class AromaChamber {
    public int getNumber();        // 1..6
    public int getConcentration(); // 0..100
}
```

A booster or chamber given `0` is switched off, not left as it was.

Shoot from all connected devices:

```java
usb.shootAllWithIntensity(3000, 100, 0,
    new AromaChamber(2, 50), new AromaChamber(5, 100));
```

Shoot from a specific device, by object or by serial:

```java
usb.shootWithIntensity(aromaShooter, 3000, 100, 0, new AromaChamber(2, 50));
usb.shootWithIntensity("ASN3A01192", 3000, 100, 0, new AromaChamber(2, 50));
```

Stop:

```java
usb.stopAllChambersWithIntensity();
usb.stopAllChambersWithIntensity(aromaShooter);
```

> The two APIs send different commands, and **each needs its own stop**. `stopAllChambers()` will not stop a shoot started with `shootWithIntensity*`.

---

<a id="3-disconnecting--reconnecting"></a>

### 3. Disconnecting / reconnecting

A serial port can only be held by one process at a time. Disconnecting stops the device and releases its port, so another application can take it over.

```java
// Every connected device.
usb.disconnectAll();

// One device.
usb.disconnect(aromaShooter, new DisconnectCallback() {
    @Override
    public void onDisconnect(AromaShooter aromaShooter) {
        // Port released.
    }

    @Override
    public void onFailed(AromaShooter aromaShooter, String msg) {
        // Not connected, or not a USB device.
    }
});
```

`connect` re-opens a device found by an earlier scan, without probing every port again:

```java
usb.connect(aromaShooter, new ConnectCallback() {
    @Override
    public void onConnected(AromaShooter aromaShooter) {
        // Ready to shoot again.
    }

    @Override
    public void onFailed(AromaShooter aromaShooter, String msg) {
        // The port could not be opened - another application may still hold it.
    }
});
```

---

<a id="troubleshooting"></a>

### Troubleshooting

**No devices found.** Check the FTDI driver is installed and that the port is not already open in another application — a serial port is exclusive. If a previous run of your own program did not call `disconnectAll()`, its port may still be held.

**Nothing comes out, but no error.** The internal booster is off. Pass `internalBooster: true`, or an `internalBoosterIntensity` above 0.

**The scent does not stop.** You are probably calling the stop that belongs to the other API. Use `stopAllChambersWithIntensity()` after `shootWithIntensity*`, and `stopAllChambers()` after `shootSimple*`.

**A booster keeps running after you asked for 0.** Fixed in 3.1.0. Earlier versions left a channel untouched when its intensity was 0, and the device carried on with its previous instruction.

---

<a id="license"></a>

### License

Please check the [LICENSE](/LICENSE.md) file for the details.

**If you get any issues or require any new features, please create a [new issue](https://github.com/aromajoin/aromashooter-sdk-java/issues).**
