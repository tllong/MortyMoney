import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

import static java.lang.Thread.sleep;

public class MortyMoneyTest extends Utils {

    private static AndroidDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", getDeviceName());
        capabilities.setCapability("appPackage", "com.turner.pocketmorties");
        capabilities.setCapability("appActivity", "com.prime31.UnityPlayerNativeActivity");
        capabilities.setCapability("newCommandTimeout", "0");
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void getschmeckles() throws InterruptedException {
        int newPlusX = suggestPlusButtonCords(getDeviceSize())[0];
        int newPlusY = suggestPlusButtonCords(getDeviceSize())[1];

        int newWatchX = suggestWatchButtonCords(getDeviceSize())[0];
        int newWatchY = suggestWatchButtonCords(getDeviceSize())[1];

        // Waiting for the app to start (no way to implicitly check sadly.)
        System.out.println("waiting 15 seconds");
        sleep(15000);

        // Tapping on the + button to watch videos
        driver.tap(1, newPlusX, newPlusY, 1);
        
        // Infinite loop for infinite Schmeckles
        while (true) {
            // Tapping on the watch video button
            driver.tap(1, newWatchX, newWatchY, 1);

            // waiting 30 seconds for the ad to finish
            System.out.println("waiting 30 seconds");
            sleep(30000);

            // Checks if a redirect add was played. If yes, press the devices back button.
            checkForAd();

            if (checkForAd()) {
                System.out.println("Fancy Ad Found!!!!!!");
                driver.pressKeyCode(4);
            } else {
                System.out.println("No fancy Ad Found");
            }

            checkForError();

            if (checkForError()) {
                System.out.println("Error message found!!!!!");
                driver.findElementByName("Dismiss").click();
            } else {
                System.out.println("No Error message found");
            }
        }

    }

    public static boolean checkForAd() {
        // findElements returns an empty list if nothing is found instead of an exception.
        // This is used to stop it from throwing a NoSuchElementException
        return driver.findElementsByClassName("android.webkit.WebView").size() > 0;
    }

    public static boolean checkForError() {
        return driver.findElementsByName("We're sorry, something went wrong. Please try again.").size() > 0;
    }
}

