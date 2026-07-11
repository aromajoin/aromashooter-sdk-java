[English](https://github.com/aromajoin/aromashooter-sdk-java) / [日本語](README-JP.md). 

# Aroma Shooter SDK (Java)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.aromajoin.sdk/jvm/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.aromajoin.sdk/jvm)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Join the community on Spectrum](https://withspectrum.github.io/badge/badge.svg)](https://spectrum.chat/aromajoin-software/?tab=posts)

**A Java library version of AromaShooter Controller SDK which is used to communicate with [Aroma Shooter devices](https://aromajoin.com/products/aroma-shooter)**  

# Table of Contents
1. [Supported devices](#supported-devices)  
2. [Prerequisites](#prerequisites)
3. [Installation](#installation)
4. [Usage](#usage)
    * [Setup](#setup)
    * [Scan and connect device](#scan-and-connect-device)
    * [Connected devices](#connected-devices)
    * [Shoot](#shoot)
    * [Stop](#stop)
    * [Disconnect](#disconnect)
5. [Issues](#issues)
7. [License](#license)


## Supported devices
* Aroma Shooter 1 USB version
* Aroma Shooter 2

## Connection
* USB

## Prerequisites
* JRE version: >= 1.8+
* Donwload and install [drivers](http://www.ftdichip.com/FTDrivers.htm) based on your OS.

## Installation

### Gradle
Please add `controller-sdk` dependency.
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

### Binary files (.jar)
1. Directly download the [latest controller-sdk-java.jar file](https://github.com/aromajoin/aromashooter-sdk-java/releases/tag/v2.4.0). 
2. Add it into your project's build path.

## Usage
For details, please check our [Sample project](https://github.com/aromajoin/aromashooter-sdk-java/tree/master/Sample) (Recommended).

### Setup
```java
// Initialize an instance of USBASController
USBASController usbController = new USBASController();
```
### Scan and connect device

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

### Connected devices
```java
List<AromaShooter> connectedDevices = usbController.getConnectedDevices();
```

### Shoot 
```java
/**
 * Shoots aroma at specific chambers from all connected devices.
 * @param duration     diffusing duration in milliseconds.
 * @param internalBooster      whether booster is used or not. 
 * @param chambers        cartridge numbers to shoot aroma. Value: 1 ~ 6.
 */
// For example, the following codes will shoot aroma at cartridge 2 and 5 for 3 seconds.
usbController.shootAllSimple(duration, internalBooster, chambers);
```

* Shoot scents method for AS2 (AromaShooter 2) devices only
```java
/**
 * Shoots aroma at specific chambers from all connected devices.
 * @param duration              diffusing duration in milliseconds.
 * @param boosterIntensity      booster port. Value: 0~100.
 * @param fanIntensity          fan port. Value: 0~100.
 * @param chambers                 array of chambers. Value: AromaChamber(number, concentration)
 */

controller.shootAllWithIntensity(duration, internalBoosterIntensity, externalBoosterIntensity, chambers);
```

* Other shoot methods
```java
// Shoot with one Aroma Shooter
controller.shootSimple(aromaShooter, duration, internalBooster, chambers);
// Shoot with one Aroma Shooter with its serial number input
controller.shootSimple(aromaShooterSerial, duration, internalBooster, chambers);
// Shoot with one Aroma Shooter with intensity (Aroma Shooter 2 supported only)
controller.shootWithIntensity(aromaShooter, durationMilliSec, internalBoosterIntensity, externalBoosterIntensity, chambers);
// Shoot with one Aroma Shooter with intensity and serial number input (Aroma Shooter 2 supported only)
controller.shootWithIntensity(aromaShooterSerial, durationMilliSec, internalBoosterIntensity, externalBoosterIntensity, chambers);
```

### Stop
* Stop all ports of current connected devices if they are diffusing 
```java
usbController.stopAllPorts();
```
* Stop all ports of one device if it is diffusing scents
```java
usbController.stopAllPorts(aromaShooter);
```

### Disconnect
Disconnect all devices.
```java
usbController.disconnectAll();
```
## Issues
**If you get any issues or require any new features, please create a [new issue](https://github.com/aromajoin/aromashooter-sdk-java/issues).**


## License
Please check the [LICENSE](/LICENSE.md) file for the details.
