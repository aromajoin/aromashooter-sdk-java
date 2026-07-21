import com.aromajoin.sdk.core.device.AromaShooter;
import com.aromajoin.sdk.core.device.AromaChamber;
import com.aromajoin.sdk.jvm.DiscoverCallback;
import com.aromajoin.sdk.jvm.usb.USBASController;
import java.util.List;

public class AromaShooterUSBSample {
  public static void main(String[] args) {
    System.out.println("Aroma Shooter's SDK sample!");

    // Shoot scents with intensity control
    // Aroma Shooter 2 only is supported
    syncScanAndShootWithIntensity();
    //asyncScanAndShootWithIntensity();

    // Shoot scents without intensity control
    // Aroma Shooter 1 and 2 both are supported
    //syncScanAndShoot();
    //asyncScanAndShoot();

    System.out.println("Completed.");
  }

  /**
   * Scans Aroma Shooter synchronously and shoot scents with intensity control
   * Supported Aroma Shooter: AS2
   */
  private static void syncScanAndShootWithIntensity() {
    USBASController usbController = new USBASController();
    usbController.scanAndConnect();
    if (!usbController.getConnectedDevices().isEmpty()) {
      for (AromaShooter aromaShooter : usbController.getConnectedDevices()) {
        System.out.println("AromaShooter: " + aromaShooter.getSerial());
      }
      AromaChamber chamber1 = new AromaChamber(1, 25);
      AromaChamber chamber2 = new AromaChamber(2, 50);
      AromaChamber chamber5 = new AromaChamber(5, 100);
      usbController.shootAllWithIntensity(3000, 100, 100, chamber1, chamber2, chamber5);

      //usbController.shootWithIntensity("ASN2A00010", 3000,100, 100, chamber1, chamber2, chamber5);
      usbController.disconnectAll();
    }
  }

  /**
   * Scans Aroma Shooter asynchronously and shoot scents
   * Supported Aroma Shooter: AS1 & AS2
   */
  private static void asyncScanAndShoot() {
    USBASController usbController = new USBASController();
    usbController.scanAndConnect(new DiscoverCallback() {
      @Override public void onDiscovered(List<AromaShooter> aromaShooters) {
        for (AromaShooter aromaShooter : aromaShooters) {
          System.out.println("AromaShooter: " + aromaShooter.getSerial());
        }
        usbController.shootAllSimple(3000, true, 1, 2, 5);
        usbController.disconnectAll();
      }

      @Override public void onFailed(String msg) {

      }
    });
  }

  /**
   * Scans Aroma Shooter asynchronously and shoot scents with intensity control
   * Supported Aroma Shooter: AS2
   */
  private static void asyncScanAndShootWithIntensity() {
    USBASController usbController = new USBASController();
    usbController.scanAndConnect(new DiscoverCallback() {
      @Override public void onDiscovered(List<AromaShooter> aromaShooters) {
        for (AromaShooter aromaShooter : aromaShooters) {
          System.out.println("AromaShooter: " + aromaShooter.getSerial());
        }
        AromaChamber chamber1 = new AromaChamber(1, 25);
        AromaChamber chamber2 = new AromaChamber(2, 50);
        AromaChamber chamber5 = new AromaChamber(5, 100);
        usbController.shootAllWithIntensity(3000, 100, 100, chamber1, chamber2, chamber5);
        usbController.disconnectAll();
      }

      @Override public void onFailed(String msg) {

      }
    });
  }

  /**
   * Scans Aroma Shooter synchronously and shoot scents
   * Supported Aroma Shooter: AS1 & AS2
   */
  private static void syncScanAndShoot() {
    USBASController usbController = new USBASController();
    usbController.scanAndConnect();
    if (!usbController.getConnectedDevices().isEmpty()) {
      for (AromaShooter aromaShooter : usbController.getConnectedDevices()) {
        System.out.println("AromaShooter: " + aromaShooter.getSerial());
      }
      usbController.shootAllSimple(3000, true, 1, 2, 5);
      usbController.disconnectAll();
    }
  }
}
