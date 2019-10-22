import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

public class MyFirstTest {
    private AppiumDriver driver;

    @Before
    public void  setUp() throws Exception
    {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName","Android");
        capabilities.setCapability("deviceName","AndroidTestDevice");
        capabilities.setCapability("platformVersion","8.1");
        capabilities.setCapability("automationName","Appium");
        capabilities.setCapability("appPackage","org.wikipedia");
        capabilities.setCapability("appActivity",".main.MainActivity");
        capabilities.setCapability("app","D:\\Projects\\avt-mob_lesson2\\apks\\org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown()
    {
        driver.quit();
    }

    @Test
    public void testWaitForTextAndFail()
    {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia'",
                5
        );

        WebElement title_element = waitForElementPresent(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Cannot find 'Search…'",
                5
        );

        String required_text = title_element.getAttribute("text");

        Assert.assertEquals(
                "We see unexpected text",
                "Search…",
                required_text
        );

    }

    private WebElement waitForElementPresent(By by, String error_massege, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_massege + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
    }

    private WebElement waitForElementPresent(By by, String error_massege)
    {
        return waitForElementPresent(by, error_massege, 5);
    }

    private WebElement waitForElementAndClick(By by, String error_massege, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_massege, timeoutInSeconds);
        element.click();
        return element;
    }

}
