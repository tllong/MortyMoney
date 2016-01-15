/**
 * Created by Jramey on 1/14/16.
 */

import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

import static java.lang.Thread.sleep;

public class MortyMoneyTest {

    private AndroidDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "Nexus 5X");
        capabilities.setCapability("appPackage", "com.turner.pocketmorties");
        capabilities.setCapability("appActivity", "com.prime31.UnityPlayerNativeActivity");
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }


    @Test
    public void getschmeckles() throws InterruptedException {
        // Infinite loop for infinite Schmeckles

        while (true) {
            // Waiting for the app to start (no way to implicitly check sadly.)
            sleep(15000);

            // Tapping on the + button to watch videos
            driver.tap(1, 130, 970, 1);

            // Tapping on the watch video button
            driver.tap(1, 1450, 900, 1);

            // waiting 100 seconds for the ad to finish
            sleep(100000);

            // Checks if a redirect add was played. If yes, press the devices back button.
            checkForAd();
            if (checkForAd())
                driver.pressKeyCode(4);
        }

    }

    public boolean checkForAd() {
        // findElements returns an empty list if nothing is found instead of an exception.
        // This is used to stop it from throwing a NoSuchElementException

        return driver.findElementsByClassName("Impact Webview").size() > 0;
    }
}